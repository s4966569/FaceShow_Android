package com.test.yanxiu.im_ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.stetho.inspector.protocol.module.Database;
import com.test.yanxiu.common_base.ui.KeyboardChangeListener;
import com.test.yanxiu.common_base.utils.ScreenUtils;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.SaveTextMsgRequest;
import com.test.yanxiu.im_core.http.SaveTextMsgResponse;
import com.test.yanxiu.im_core.http.TopicCreateTopicRequest;
import com.test.yanxiu.im_core.http.TopicCreateTopicResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.RecyclerViewPullToRefreshHelper;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ImMsgListActivity extends FragmentActivity {
    private DbTopic topic;

    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;
    private MsgListAdapter mMsgListAdapter;
    private RecyclerViewPullToRefreshHelper ptrHelper;
    private EditText mMsgEditText;
    private ImageView mTakePicImageView;

    private long memberId = -1;
    private String memberName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberId = getIntent().getLongExtra(Constants.kCreateTopicMemberId, -1);
        memberName = getIntent().getStringExtra(Constants.kCreateTopicMemberName);

        setResult(RESULT_CANCELED); // 只为有返回，code无意义

        topic = SharedSingleton.getInstance().get(Constants.kShareTopic);
        if ((topic == null) || (topic.mergedMsgs.size() == 0)) {
            hasMoreMsgs = false;
        }

        setContentView(R.layout.activity_msg_list);
        setupView();
        setupData();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setupView() {
        mTitleLayout = findViewById(R.id.title_layout);
        mTitleLayout.setTitle("");

        mTitleLayout.setOnNaviLeftBackCallback(new OnNaviLeftBackCallback() {
            @Override
            public void onNaviBack() {
                finish();
            }
        });

        if (topic == null) {
            mTitleLayout.setTitle(memberName);
        } else {
            mTitleLayout.setTitle(DatabaseDealer.getTopicTitle(topic, Constants.imId));
        }

        mMsgListRecyclerView = findViewById(R.id.msg_list_recyclerview);
        mMsgListRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        mMsgListAdapter = new MsgListAdapter(this);
        mMsgListAdapter.setTopic(topic);
        mMsgListRecyclerView.setAdapter(mMsgListAdapter);

        if (topic != null) {
            mMsgListAdapter.setmDatas(topic.mergedMsgs);
        } else {
            mMsgListAdapter.setmDatas(new ArrayList<DbMsg>());
        }

        mMsgListAdapter.notifyDataSetChanged();

        mMsgListAdapter.setmOnItemClickCallback(onDbMsgCallback);
        mMsgListRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mMsgListRecyclerView.getAdapter().getItemCount() > 1) {
                    mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
                }
            }
        });

        mTakePicImageView = findViewById(R.id.takepic_imageview);
        mTakePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SrtLogger.log("imui", "TBD: 拍照");
            }
        });

        mMsgEditText = findViewById(R.id.msg_edittext);
        mMsgEditText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mMsgEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mMsgEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == event.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    SrtLogger.log("imui", "TBD: 发送");
                    String msg = mMsgEditText.getText().toString();
                    String trimMsg = msg.trim();
                    if (trimMsg.length() == 0) {
                        return true;
                    }

                    doSend(msg, null);
                    return true;
                }
                return false;
            }
        });

        // 弹出键盘后处理
        KeyboardChangeListener keyboardListener = new KeyboardChangeListener(this);
        keyboardListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if ((isShow) && (mMsgListRecyclerView.getAdapter().getItemCount() > 1)) {
                    mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
                }
            }
        });

        // pull to refresh，由于覆盖了OnTouchListener，所以需要在这里处理点击外部键盘收起
        ptrHelper = new RecyclerViewPullToRefreshHelper(this, mMsgListRecyclerView, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(ImMsgListActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mMsgListRecyclerView.clearFocus();
                return false;
            }
        });
        ptrHelper.setmCallback(mOnLoadMoreCallback);
    }

    private void setupData() {
    }

    private RequestQueueHelper httpQueueHelper = new RequestQueueHelper();

    public class NewTopicCreatedEvent {
        public DbTopic dbTopic;
    }

    public class MockTopicRemovedEvent {
        public DbTopic dbTopic;
    }


    private void doSendMsg(final String msg, final String reqId) {
        SaveTextMsgRequest saveTextMsgRequest = new SaveTextMsgRequest();
        saveTextMsgRequest.imToken = Constants.imToken;
        saveTextMsgRequest.topicId = Long.toString(topic.getTopicId());
        saveTextMsgRequest.msg = msg;

        if (reqId != null) {
            // resend需要走相同的路径，但是msg已经有reqId了
            saveTextMsgRequest.reqId = reqId;
        }

        // 我发送的必然已经存在于队列
        DbMyMsg sendMsg = null;
        for (DbMsg m : topic.mergedMsgs) {
            if (m.getReqId().equals(reqId)) {
                sendMsg = (DbMyMsg) m;
            }
        }
        final DbMyMsg myMsg = sendMsg;

        mMsgListAdapter.setmDatas(topic.mergedMsgs);
        mMsgListAdapter.notifyDataSetChanged();
        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);

        // 数据存储，UI显示都完成后，http发送
        httpQueueHelper.addRequest(saveTextMsgRequest, SaveTextMsgResponse.class, new HttpCallback<SaveTextMsgResponse>() {
            @Override
            public void onSuccess(RequestBase request, SaveTextMsgResponse ret) {
                myMsg.setState(DbMyMsg.State.Success.ordinal());
                myMsg.save();
                mMsgListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                myMsg.setState(DbMyMsg.State.Failed.ordinal());
                myMsg.save();
                mMsgListAdapter.notifyDataSetChanged();
            }
        });

        mMsgEditText.setText("");
    }


    private void doSend(final String msg, final String reqId)
    {
        final String msgReqId = (reqId == null ? UUID.randomUUID().toString() : reqId);


        // 预先插入mock topic
        if ((memberId > 0) && (topic == null)) {
            // 私聊尚且没有topic，需要创建mock topic
            DbTopic mockTopic = DatabaseDealer.mockTopic();
            DbMember myself = DatabaseDealer.getMemberById(Constants.imId);
            DbMember member = DatabaseDealer.getMemberById(memberId);
            mockTopic.getMembers().add(myself);
            mockTopic.getMembers().add(member);
            mockTopic.save();
            topic = mockTopic;
            mMsgListAdapter.setTopic(topic);

            NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
            newTopicEvent.dbTopic = mockTopic;
            EventBus.getDefault().post(newTopicEvent);
        }

        // 预先插入mock msg
        //if (DatabaseDealer.isMockTopic(topic)) {
            DbMyMsg myMsg = new DbMyMsg();
            myMsg.setState(DbMyMsg.State.Sending.ordinal());
            myMsg.setReqId(msgReqId);
            myMsg.setMsgId(-1);
            myMsg.setTopicId(topic.getTopicId());
            myMsg.setSenderId(Constants.imId);
            myMsg.setSendTime(new Date().getTime());
            myMsg.setContentType(10);
            myMsg.setMsg(msg);
            myMsg.setFrom("local");
            myMsg.save();
            topic.mergedMsgs.add(0, myMsg);
            mMsgListAdapter.setmDatas(topic.mergedMsgs);
            mMsgListAdapter.notifyDataSetChanged();
        //}

        // 对于是mock topic的需要先创建topic
        if (DatabaseDealer.isMockTopic(topic)) {
            // 是新建的Topic，需要先create topic
            TopicCreateTopicRequest createTopicRequest = new TopicCreateTopicRequest();
            createTopicRequest.imToken = Constants.imToken;
            createTopicRequest.topicType = "1"; // 私聊
            createTopicRequest.imMemberIds = Long.toString(Constants.imId) + "," + Long.toString(memberId);
            createTopicRequest.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
                @Override
                public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                    ImTopic imTopic = null;
                    for (ImTopic topic : ret.data.topic) {
                        imTopic = topic;
                    }
                    // 应该只有一个imTopic

                    // 1，通知移除mock topic
                    DbTopic mockTopic = topic;
                    MockTopicRemovedEvent mockRemoveEvent = new MockTopicRemovedEvent();
                    mockRemoveEvent.dbTopic = mockTopic;
                    EventBus.getDefault().post(mockRemoveEvent);


                    // 2，添加server返回的real topic
                    DbTopic realTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                    realTopic.latestMsgTime = imTopic.latestMsgTime;
                    realTopic.latestMsgId = imTopic.latestMsgId;
                    realTopic.save();
                    topic = realTopic;

                    // 3，做mock topic 和 real topic间msgs的转换
                    DatabaseDealer.migrateMsgsForMockTopic(mockTopic, realTopic);

                    // 4, 通知新增real topic
                    NewTopicCreatedEvent newTopicEvent = new NewTopicCreatedEvent();
                    newTopicEvent.dbTopic = realTopic;
                    EventBus.getDefault().post(newTopicEvent);

                    mMsgListAdapter.notifyDataSetChanged();
                    doSendMsg(msg, msgReqId);
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    DatabaseDealer.topicCreateFailed(topic);
                    mMsgListAdapter.notifyDataSetChanged();
                }
            });
        } else {
            // 已经有对话，直接发送即可
            doSendMsg(msg, msgReqId);
        }
    }

    private void doTakePic() {

    }

    // region handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMsgListAdapter.setIsLoading(false);
            ptrHelper.loadingComplete();
        }
    };
    // endregion

    //region callback
    private OnRecyclerViewItemClickCallback<DbMsg> onDbMsgCallback = new OnRecyclerViewItemClickCallback<DbMsg>() {
        @Override
        public void onItemClick(int position, DbMsg dbMsg) {
            if (dbMsg instanceof DbMyMsg) {
                final DbMyMsg myMsg = (DbMyMsg) dbMsg;
                if (myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                    // 重新发送
                    topic.mergedMsgs.remove(myMsg);
                    // 1, 先更新数据库中
                    myMsg.setState(DbMyMsg.State.Sending.ordinal());
                    myMsg.setMsgId(latestMsgId());
                    myMsg.save();

                    doSend(myMsg.getMsg(), myMsg.getReqId());
                }
            }
        }
    };

    private boolean hasMoreMsgs = true;
    private OnPullToRefreshCallback mOnLoadMoreCallback = new OnPullToRefreshCallback() {
        @Override
        public void onLoadMore() {
            if (hasMoreMsgs) {
                mMsgListAdapter.setIsLoading(true);
                mMsgListAdapter.notifyItemRangeInserted(0, 1);
                // 先从网络取，如果失败了则由数据库重建
                final DbMsg earliestMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);
                GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
                getTopicMsgsRequest.imToken = Constants.imToken;
                getTopicMsgsRequest.topicId = Long.toString(topic.getTopicId());
                getTopicMsgsRequest.startId = Long.toString(earliestMsg.getMsgId());
                getTopicMsgsRequest.order = "desc";
                getTopicMsgsRequest.startRequest(GetTopicMsgsResponse.class, new HttpCallback<GetTopicMsgsResponse>() {
                    @Override
                    public void onSuccess(RequestBase request, GetTopicMsgsResponse ret) {
                        ptrHelper.loadingComplete();
                        mMsgListAdapter.setIsLoading(false);
                        mMsgListAdapter.notifyItemRangeRemoved(0, 1);

                        final DbMsg theRefreshingMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);

                        if (ret.data.topicMsg.size() < DatabaseDealer.pagesize) {
                            hasMoreMsgs = false;
                        }

                        if (ret.data.topicMsg.size() > 0) {
                            // 去除最后一条重复的
                            ret.data.topicMsg.remove(0);
                        }

                        for (ImMsg msg : ret.data.topicMsg) {
                            DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "http", Constants.imId);
                            topic.mergedMsgs.add(dbMsg);

                            if (dbMsg.getMsgId() > topic.latestMsgId) {
                                topic.latestMsgId = dbMsg.getMsgId();
                            }
                        }

                        mMsgListAdapter.setmDatas(topic.mergedMsgs);
                        int num = mMsgListAdapter.uiAddedNumberForMsg(theRefreshingMsg);
                        if (num > 0) {
                            mMsgListAdapter.notifyItemRangeRemoved(0, 1); // 最后的Datetime需要去掉
                            mMsgListAdapter.notifyItemRangeInserted(0, num);
                        }
                    }

                    @Override
                    public void onFail(RequestBase request, Error error) {
                        // 从数据库获取
                        ptrHelper.loadingComplete();
                        mMsgListAdapter.setIsLoading(false);
                        mMsgListAdapter.notifyItemRangeRemoved(0, 1);

                        final DbMsg theRefreshingMsg = topic.mergedMsgs.get(topic.mergedMsgs.size() - 1);
                        
                        List<DbMsg> msgs = DatabaseDealer.getTopicMsgs(topic.getTopicId(),
                                earliestMsg.getMsgId(),
                                DatabaseDealer.pagesize);

                        if (msgs.size() < DatabaseDealer.pagesize) {
                            hasMoreMsgs = false;
                        }
                        topic.mergedMsgs.addAll(msgs);
                        mMsgListAdapter.setmDatas(topic.mergedMsgs);
                        int num = mMsgListAdapter.uiAddedNumberForMsg(theRefreshingMsg);
                        if (num > 0) {
                            mMsgListAdapter.notifyItemRangeRemoved(0, 1); // 最后的Datetime需要去掉
                            mMsgListAdapter.notifyItemRangeInserted(0, num);
                        }
                    }
                });
            }

        }
    };
    //endregion

    //region mqtt
    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", Constants.imId);

        if (msg.topicId != topic.getTopicId()) {
            // 不是本topic的直接抛弃
            return;
        }

        //topic.mergedMsgs.add(0, dbMsg);
        DatabaseDealer.pendingMsgToTopic(dbMsg, topic);
        if (dbMsg.getMsgId() > topic.latestMsgId) {
            topic.latestMsgId = dbMsg.getMsgId();
            topic.latestMsgTime = dbMsg.getSendTime();
        }

        mMsgListAdapter.setmDatas(topic.mergedMsgs);
        mMsgListAdapter.notifyDataSetChanged();
        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);
    }
    //endregion

    //region util
    private long latestMsgId() {
        long latestMsgId = -1;
        for (DbMsg dbMsg : topic.mergedMsgs) {
            if (dbMsg.getMsgId() > latestMsgId) {
                latestMsgId = dbMsg.getMsgId();
            }
        }
        return latestMsgId;
    }
    //endregion
}
