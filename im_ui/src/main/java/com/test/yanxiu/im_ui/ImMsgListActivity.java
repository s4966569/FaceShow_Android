package com.test.yanxiu.im_ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.test.yanxiu.common_base.ui.KeyboardChangeListener;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.common_base.utils.permission.OnPermissionCallback;
import com.test.yanxiu.faceshow_ui_base.ImBaseActivity;
import com.test.yanxiu.faceshow_ui_base.imagePicker.GlideImageLoader;
import com.test.yanxiu.im_core.RequestQueueHelper;
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
import com.test.yanxiu.im_ui.view.ChoosePicsDialog;
import com.test.yanxiu.im_ui.view.RecyclerViewPullToRefreshHelper;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImMsgListActivity extends ImBaseActivity {
    private DbTopic topic;
    private static final int IMAGE_PICKER = 0x03;
    private static final int REQUEST_CODE_SELECT = 0x04;
    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;
    private MsgListAdapter mMsgListAdapter;
    private RecyclerViewPullToRefreshHelper ptrHelper;
    private EditText mMsgEditText;
    private ImageView mTakePicImageView;

    private String memberIds = null;
    private String memberName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberIds = getIntent().getStringExtra(Constants.kCreateTopicMemberIds);
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
                    doSend();
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
                InputMethodManager imm = (InputMethodManager) getSystemService(ImMsgListActivity.this.INPUT_METHOD_SERVICE);
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


    private void doSendMsg() {
        String msg = mMsgEditText.getText().toString();
        String trimMsg = msg.trim();
        if (trimMsg.length() == 0) {
            return;
        }

        SaveTextMsgRequest saveTextMsgRequest = new SaveTextMsgRequest();
        saveTextMsgRequest.imToken = Constants.imToken;
        saveTextMsgRequest.topicId = Long.toString(topic.getTopicId());
        saveTextMsgRequest.msg = msg;

        final DbMyMsg myMsg = new DbMyMsg();
        myMsg.setState(DbMyMsg.State.Sending.ordinal());
        myMsg.setReqId(saveTextMsgRequest.reqId);
        myMsg.setMsgId(latestMsgId());
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

    private void doSend() {
        if ((memberIds != null) && (topic == null)) {
            // 是新建的Topic，需要先create topic
            TopicCreateTopicRequest createTopicRequest = new TopicCreateTopicRequest();
            createTopicRequest.imToken = Constants.imToken;
            createTopicRequest.topicType = "1"; // 私聊
            createTopicRequest.imMemberIds = memberIds;
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
                        doSendMsg();
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    // TBD:cailei 这里需要弹个toast？
                }
            });
        } else {
            // 已经有对话，直接发送即可
            doSendMsg();
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
                    topic.mergedMsgs.add(0, myMsg);
                    mMsgListAdapter.setmDatas(topic.mergedMsgs);
                    mMsgListAdapter.notifyDataSetChanged();

                    SaveTextMsgRequest saveTextMsgRequest = new SaveTextMsgRequest();
                    saveTextMsgRequest.reqId = myMsg.getReqId();
                    saveTextMsgRequest.imToken = Constants.imToken;
                    saveTextMsgRequest.topicId = Long.toString(topic.getTopicId());
                    saveTextMsgRequest.msg = myMsg.getMsg();
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
                createSelectedImagesList(data, false);
                break;
            case REQUEST_CODE_SELECT:
                createSelectedImagesList(data, true);
            default:
                break;
        }

    }

    /**
     * 构造需要的图片数据
     * @param data
     * @param isReSize
     */
    private void createSelectedImagesList(Intent data, boolean isReSize) {
        ArrayList<ImageItem> images = null;
        try {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        } catch (Exception e) {

        }
        if (images == null) {
            return;
        }


    }
}
