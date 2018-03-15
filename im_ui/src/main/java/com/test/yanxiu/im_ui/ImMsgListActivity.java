package com.test.yanxiu.im_ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
import com.test.yanxiu.im_ui.view.RecyclerViewPullToRefreshHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ImMsgListActivity extends FragmentActivity {
    private DbTopic topic;

    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;
    private MsgListAdapter mMsgListAdapter;
    private RecyclerViewPullToRefreshHelper ptrHelper;
    private EditText mMsgEditText;
    private ImageView mTakePicImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topic = SharedSingleton.getInstance().get(Constants.kShareTopic);

        setContentView(R.layout.activity_msg_list);
        setupView();
        setupData();
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
        mTitleLayout.setTitle(DatabaseDealer.getTopicTitle(topic, Constants.imId));

        mMsgListRecyclerView = findViewById(R.id.msg_list_recyclerview);
        mMsgListRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        mMsgListAdapter = new MsgListAdapter(this);
        mMsgListRecyclerView.setAdapter(mMsgListAdapter);
        mMsgListAdapter.setmDatas(topic.mergedMsgs);
        mMsgListAdapter.setmOnItemClickCallback(onDbMsgCallback);

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
                    doSend();
                    return true;
                }
                return false;
            }
        });

        // 点击外部收起键盘
        mMsgListRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(ImMsgListActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mMsgListRecyclerView.clearFocus();
                return false;
            }
        });

        // 弹出键盘后处理
        KeyboardChangeListener keyboardListener = new KeyboardChangeListener(this);
        keyboardListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    mMsgListRecyclerView.smoothScrollToPosition(mMsgListRecyclerView.getAdapter().getItemCount() - 1);//滚动到底部
                }
            }
        });

        // pull to refresh
        ptrHelper = new RecyclerViewPullToRefreshHelper(this, mMsgListRecyclerView);
        ptrHelper.setmCallback(new OnPullToRefreshCallback() {
            @Override
            public void onLoadMore() {
                mMsgListAdapter.setIsLoading(true);
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    private void setupData() {
    }

    private void doSend() {
        long latestMsgId = -1;
        for (DbMsg msg : topic.mergedMsgs) {
            if (msg.getMsgId() > latestMsgId) {
                latestMsgId = msg.getMsgId();
            }
        }

        DbMyMsg myMsg = new DbMyMsg();
        myMsg.setSenderId(Constants.imId);
        myMsg.setMsgId(latestMsgId);
        myMsg.setState(DbMyMsg.State.Sending.ordinal());
        myMsg.setReqId("000");
        myMsg.setMsg(mMsgEditText.getText().toString());

        DbMyMsg myMsg1 = new DbMyMsg();
        myMsg1.setSenderId(Constants.imId);
        myMsg1.setMsgId(latestMsgId);
        myMsg1.setState(DbMyMsg.State.Failed.ordinal());
        myMsg1.setReqId("000");
        myMsg1.setMsg(mMsgEditText.getText().toString());

        topic.mergedMsgs.add(0, myMsg);
        topic.mergedMsgs.add(0, myMsg1);

        mMsgListAdapter.setmDatas(topic.mergedMsgs);
        mMsgListRecyclerView.smoothScrollToPosition(mMsgListAdapter.getItemCount() - 1);
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
                DbMyMsg myMsg = (DbMyMsg) dbMsg;
                if (myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                    // 重新发送
                    topic.mergedMsgs.remove(myMsg);
                    myMsg.setState(DbMyMsg.State.Sending.ordinal());
                    topic.mergedMsgs.add(0, myMsg);

                    mMsgListAdapter.setmDatas(topic.mergedMsgs);
                    mMsgListAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    //endregion
}
