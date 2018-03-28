package com.test.yanxiu.im_ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.test.yanxiu.common_base.ui.KeyboardChangeListener;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.common_base.utils.permission.OnPermissionCallback;
import com.test.yanxiu.faceshow_ui_base.ImBaseActivity;
import com.test.yanxiu.faceshow_ui_base.imagePicker.GlideImageLoader;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.GetQiNiuTokenRequest;
import com.test.yanxiu.im_core.http.GetQiNiuTokenResponse;
import com.test.yanxiu.im_core.http.GetTopicMembersInfoRequest;
import com.test.yanxiu.im_core.http.GetTopicMembersInfoResponse;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.SaveImageMsgRequest;
import com.test.yanxiu.im_core.http.SaveImageMsgResponse;
import com.test.yanxiu.im_core.http.SaveTextMsgRequest;
import com.test.yanxiu.im_core.http.SaveTextMsgResponse;
import com.test.yanxiu.im_core.http.TopicCreateTopicRequest;
import com.test.yanxiu.im_core.http.TopicCreateTopicResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImDataForUpdateMemberInfo;
import com.test.yanxiu.im_core.http.common.ImMember;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.ChoosePicsDialog;
import com.test.yanxiu.im_ui.view.RecyclerViewPullToRefreshHelper;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImMsgListActivity extends ImBaseActivity {
    private final String TAG = getClass().getSimpleName();

//    private DbTopic topic;


    private DbTopic topic;
    private static final int IMAGE_PICKER = 0x03;
    private static final int REQUEST_CODE_SELECT = 0x04;
    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;
    private MsgListAdapter mMsgListAdapter;
    private RecyclerViewPullToRefreshHelper ptrHelper;
    private EditText mMsgEditText;
    private ImageView mTakePicImageView;

    private long memberId = -1;
    private String memberName = null;
    private String mQiniuToken;
    private boolean mKeyBoardShown;//键盘已经显示了


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
        initImagePicker();
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
        mMsgListRecyclerView.setLayoutManager(new FoucsLinearLayoutManager(this,
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
                //发送照片入口

                showChoosePicsDialog();

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
                    mMsgEditText.setText("");
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
                mKeyBoardShown = isShow;
                if ((isShow) && (mMsgListRecyclerView.getAdapter().getItemCount() > 1)) {
                    mMsgListRecyclerView.scrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
                }
            }
        });

        // pull to refresh，由于覆盖了OnTouchListener，所以需要在这里处理点击外部键盘收起
        ptrHelper = new RecyclerViewPullToRefreshHelper(this, mMsgListRecyclerView, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(mKeyBoardShown){
                    InputMethodManager imm = (InputMethodManager) getSystemService(ImMsgListActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    mMsgListRecyclerView.clearFocus();
                }
                return false;
            }
        });
        ptrHelper.setmCallback(mOnLoadMoreCallback);
    }


    private void setupData() {
        if (topic != null) {
            // 每次进入话题更新用户信息
            updateTopicFromHttp(topic.getTopicId() + "");
        }
    }

    private void updateTopicFromHttp(final String topicId) {
        // http, mqtt 公用
        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = Constants.imToken;
        getTopicsRequest.topicIds = topicId;

        getTopicsRequest.startRequest(TopicGetTopicsResponse.class, new HttpCallback<TopicGetTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetTopicsResponse ret) {
                //正确的长度 为1
                Log.i(TAG, "onSuccess: " + new Gson().toJson(ret));
                for (ImTopic imTopic : ret.data.topic) {
                    //更新数据库 topic 信息
                    DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                    dbTopic.latestMsgTime = imTopic.latestMsgTime;
                    dbTopic.latestMsgId = imTopic.latestMsgId;

                    //请求成功 消除红点
                    dbTopic.setShowDot(false);
                    dbTopic.save();
                    //保证 topic 的持有
                    topic.setName(dbTopic.getName());
                    topic.setChange(dbTopic.getChange());
                    topic.setGroup(dbTopic.getGroup());
                    topic.setType(dbTopic.getType());
                    topic.setTopicId(dbTopic.getTopicId());
                    topic.latestMsgId = dbTopic.latestMsgId;
                    topic.latestMsgTime = dbTopic.latestMsgTime;
                    topic.setShowDot(dbTopic.isShowDot());

                    //请求当前topic 下的members 信息更新
                    updateMemberInfoRequest(topic);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

    }

    // HTTP请求 更新 当前topic 的members 信息
    private void updateMemberInfoRequest(final DbTopic topic) {
        GetTopicMembersInfoRequest topicMembersInfoRequest = new GetTopicMembersInfoRequest();
        StringBuilder stringBuilder = new StringBuilder();

        int length = topic.getMembers().size();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(topic.getMembers().get(i).getImId());
            if (i != length - 1) {
                stringBuilder.append(",");
            }
        }

        topicMembersInfoRequest.imMemberIds = stringBuilder.toString();
        topicMembersInfoRequest.imToken = Constants.imToken;
        topicMembersInfoRequest.startRequest(GetTopicMembersInfoResponse.class, new HttpCallback<GetTopicMembersInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTopicMembersInfoResponse ret) {
                //对 对话menmbers进行信息修正
                for (ImDataForUpdateMemberInfo.MembersBean member : ret.getData().getMembers()) {
                    //转换数据
                    ImMember imMember = new ImMember();
                    imMember.avatar = member.getAvatar();
                    imMember.imId = member.getId();
                    imMember.memberName = member.getMemberName();
                    imMember.memberType = member.getMemberType();
                    imMember.state = member.getState();
                    imMember.userId = member.getUserId();
                    DbMember updatedMember = DatabaseDealer.updateDbMemberWithImMember(imMember);

                    //对私聊topic 的 title 进行修正
                    if (topic.getType().equals("1")) {
                        if (imMember.imId != Constants.imId) {
                            mTitleLayout.setTitle(imMember.memberName);
                            topic.setName(imMember.memberName);
                        }
                    }


                    //对topic 的member进行更新 暂时不考虑  成员数量的变化 只考虑成员信息的变化
                    for (DbMember dbMember : topic.getMembers()) {
                        if (dbMember.getImId() == updatedMember.getImId()) {
                            topic.getMembers().remove(dbMember);
                            topic.getMembers().add(updatedMember);
                            break;
                        }
                    }
                }
                //使用最新的 成员信息
                if (topic.getType().equals("2")) {
                    mTitleLayout.setTitle("班级群聊 (" + topic.getMembers().size() + ")");
                }
                mMsgListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                Log.i("updatemember", "onFail: ");
            }
        });
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

    /**
     * 发送图片
     */
    private void doSendImage(String imagePath, String rid, int width, int height) {
        if (TextUtils.isEmpty(rid)) {
            return;
        }
        Log.e("frc", "doSendImage width:   " + width);
        Log.e("frc", "doSendImage height:   " + height);

        SaveImageMsgRequest saveImageMsgRequest = new SaveImageMsgRequest();
        saveImageMsgRequest.imToken = Constants.imToken;
        saveImageMsgRequest.topicId = Long.toString(topic.getTopicId());
        saveImageMsgRequest.rid = rid;
        saveImageMsgRequest.height = String.valueOf(height);
        saveImageMsgRequest.width = String.valueOf(width);


        final DbMyMsg myMsg = new DbMyMsg();
        myMsg.setState(DbMyMsg.State.Sending.ordinal());
        myMsg.setReqId(saveImageMsgRequest.reqId);
        myMsg.setMsgId(latestMsgId());
        myMsg.setTopicId(topic.getTopicId());
        myMsg.setSenderId(Constants.imId);
        myMsg.setSendTime(new Date().getTime());
        //type==20 为图片
        myMsg.setContentType(20);
        myMsg.setFrom("local");
        myMsg.setViewUrl(imagePath);
        myMsg.setHeight(height);
        myMsg.setWith(width);
        myMsg.save();
        topic.mergedMsgs.add(0, myMsg);

        mMsgListAdapter.setmDatas(topic.mergedMsgs);
        mMsgListAdapter.notifyDataSetChanged();
        mMsgListRecyclerView.scrollToPosition(mMsgListAdapter.getItemCount() - 1);

        // 数据存储，UI显示都完成后，http发送


        httpQueueHelper.addRequest(saveImageMsgRequest, SaveImageMsgResponse.class, new HttpCallback<SaveImageMsgResponse>() {
            @Override
            public void onSuccess(RequestBase request, SaveImageMsgResponse ret) {
                Log.e("frc", "onSuccess:: ");
                Log.e("frc", "onSuccess:: w   " + ret.data.topicMsg.get(0).contentData.width);
                Log.e("frc", "onSuccess:: h   " + ret.data.topicMsg.get(0).contentData.height);
                myMsg.setState(DbMyMsg.State.Success.ordinal());
                myMsg.save();
                mMsgListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                Log.e("frc", "Error:: " + error.getMessage());
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

    private void doSendImgMsg(final String imagePath, final String rid, final int with, final int height) {
        if ((memberId > 0) && (topic == null)) {
            // 是新建的Topic，需要先create topic
            TopicCreateTopicRequest createTopicRequest = new TopicCreateTopicRequest();
            createTopicRequest.imToken = Constants.imToken;
            createTopicRequest.topicType = "1"; // 私聊
            createTopicRequest.imMemberIds = Long.toString(Constants.imId) + Long.toString(memberId);
            createTopicRequest.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
                @Override
                public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                    for (ImTopic imTopic : ret.data.topic) {
                        DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                        dbTopic.latestMsgTime = imTopic.latestMsgTime;
                        dbTopic.latestMsgId = imTopic.latestMsgId;
                        dbTopic.save();
                        topic = dbTopic;

                        NewTopicCreatedEvent event = new NewTopicCreatedEvent();
                        event.dbTopic = dbTopic;
                        EventBus.getDefault().post(event);

                        doSendImage(imagePath, rid, with, height);
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    // TBD:cailei 这里需要弹个toast？
                }
            });
        } else {
            // 已经有对话，直接发送即可
            doSendImage(imagePath, rid, with, height);

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
        Log.e("frc", "picW  :" + dbMsg.getWith());
        Log.e("frc", "picH :" + dbMsg.getHeight());
        Log.e("frc", "picV :" + dbMsg.getViewUrl());
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
        //在对话内收到消息 默认取消红点的显示  bug1307
        topic.setShowDot(false);
        topic.save();

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


    /*-------------------------------  发送图片逻辑     ------------------------------------*/
    private ImagePicker imagePicker;

    private void initImagePicker() {
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(glideImageLoader);
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //裁剪框的形状
    }

    private ChoosePicsDialog mClassCircleDialog;

    private void showChoosePicsDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ChoosePicsDialog(ImMsgListActivity.this);
            mClassCircleDialog.setClickListener(new ChoosePicsDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    ImMsgListActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent(ImMsgListActivity.this, ImageGridActivity.class);
                            startActivityForResult(intent, IMAGE_PICKER);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            Toast.makeText(ImMsgListActivity.this, R.string.no_storage_permissions, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    ImMsgListActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {

                            Intent intent = new Intent(ImMsgListActivity.this, ImageGridActivity.class);
                            // 是否是直接打开相机
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                            startActivityForResult(intent, REQUEST_CODE_SELECT);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            Toast.makeText(ImMsgListActivity.this, R.string.no_storage_permissions, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER:
            case REQUEST_CODE_SELECT:

                getQiNiuToken(createSelectedImagesList(data));

//                uploadPicsByQiNiu(images);
                break;
            default:
                break;
        }

    }


    /**
     * 先在列表中显示压缩后图片
     *
     * @param imgs 图片数据
     */
    private void showReSizedPics(List<String> imgs) {

    }

    /**
     * 构造需要的图片数据
     *
     * @param data
     */
    private ArrayList<ImageItem> createSelectedImagesList(Intent data) {
        ArrayList<ImageItem> images = null;
        try {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        } catch (Exception e) {

        }
        if (images == null) {
            return null;
        }

        return images;

    }

    /**
     * 使用鲁班压缩图片至200kb左右
     *
     * @param imageItemArrayList
     * @return
     */
    private List<String> reSizePics(List<ImageItem> imageItemArrayList) {
        List<String> imagePathList = new ArrayList<>();
        final List<String> imageReSizedPathList = new ArrayList<>();
        for (ImageItem imageItem : imageItemArrayList) {
            imagePathList.add(imageItem.path);
        }
        Luban.with(ImMsgListActivity.this)
                .load(imagePathList)
                .ignoreBy(200)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.e("frc", "Luban   onStart");
                    }

                    @Override
                    public void onSuccess(File file) {
                        imageReSizedPathList.add(file.getAbsolutePath());
                        uploadPicByQiNiu(file.getAbsolutePath());

                        Log.e("frc", "Luban onSuccess " + file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("frc", "Luban onError: " + e.getMessage());
                    }
                }).launch();

        return imageReSizedPathList;
    }

    UUID mGetQiNiuTokenUUID;

    /**
     * 获取七牛token
     *
     * @param imageItemArrayList
     */
    private void getQiNiuToken(final ArrayList<ImageItem> imageItemArrayList) {
        GetQiNiuTokenRequest getQiNiuTokenRequest = new GetQiNiuTokenRequest();
        getQiNiuTokenRequest.from = "100";
        getQiNiuTokenRequest.dtype = "app";
        getQiNiuTokenRequest.token = Constants.token;
        mGetQiNiuTokenUUID = getQiNiuTokenRequest.startRequest(GetQiNiuTokenResponse.class, new HttpCallback<GetQiNiuTokenResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetQiNiuTokenResponse ret) {
                mGetQiNiuTokenUUID = null;
//                mCancelQiNiuUploadPics = false;
                if (ret != null) {
                    if (ret.code == 0) {
                        mQiniuToken = ret.getData().getToken();
                        Log.e("frc", "token is :" + mQiniuToken);
                        reSizePics(imageItemArrayList);

                    } else {
//                        rootView.hiddenLoadingView();
//                        ToastUtil.showToast(getApplicationContext(), ret.getError() != null ? ret.getError().getMessage() : getString(R.string.get_qiniu_token_error));
                    }

                } else {
//                    rootView.hiddenLoadingView();
//                    ToastUtil.showToast(getApplicationContext(), getString(R.string.get_qiniu_token_error));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mGetQiNiuTokenUUID = null;
//                rootView.hiddenLoadingView();
//                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }

    private UploadManager uploadManager = null;
    /**
     * 此参数设置为true时 则正在执行的七牛上传图片将被停止
     */
    private boolean mCancelQiNiuUploadPics = false;
    private Configuration config = new Configuration.Builder()
            // 分片上传时，每片的大小。 默认256K
            .chunkSize(2 * 1024 * 1024)
            // 启用分片上传阀值。默认512K
            .putThreshhold(4 * 1024 * 1024)
            // 链接超时。默认10秒
            .connectTimeout(10)
            // 服务器响应超时。默认60秒
            .responseTimeout(60)
            .build();

    private void uploadPicByQiNiu(final String picPath) {
        if (uploadManager == null) {
            uploadManager = new UploadManager(config);
        }
        uploadManager.put(picPath, null, mQiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                try {
                    Integer[] widthAndHeight = getPicWithAndHeight(picPath);
                    doSendImgMsg(picPath, jsonObject.getString("key"), widthAndHeight[0], widthAndHeight[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String s, double v) {
                Log.e("frc", "progress   s:" + s);
                Log.e("frc", "progress   v:" + v);
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return mCancelQiNiuUploadPics;
            }
        }));
    }

    /**
     * 计算图片的宽高
     *
     * @param imgPath 图片路径
     * @return String【】 第一个参数表示width 第二个参数表示height
     */
    private Integer[] getPicWithAndHeight(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        return new Integer[]{options.outWidth, options.outHeight};
    }
}
