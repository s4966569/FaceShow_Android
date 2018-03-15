package com.test.yanxiu.im_ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.test.yanxiu.common_base.ui.InputMethodUtil;
import com.test.yanxiu.common_base.ui.KeyboardChangeListener;
import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.adapter.BaseRecyclerViewAdapter;
import com.test.yanxiu.im_ui.adapter.ChartRoomAdapter;
import com.test.yanxiu.im_ui.bean.ImMessageBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.test.yanxiu.im_ui.bean.ImMessageBean.MessageBean;
import com.test.yanxiu.im_ui.view.ImRecyclerView;

import static com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_MYSELF;

/**
 * 聊天室界面
 * 注释：应该把基类FaceShowBaseActivity，移动到commonbase里，然后app以及imui都依赖commonbase。
 *
 * @author 戴延枫
 *         2018年3月13日15:19:47
 */
public class ChatRoomActivity extends Activity implements KeyboardChangeListener.KeyBoardListener, View.OnClickListener, BaseRecyclerViewAdapter.RecyclerViewItemClickListener, ImRecyclerView.LoadMoreListener {

    //    private PublicLoadLayout mRootView;
    private ImRecyclerView mRecyclerView;
    private ChartRoomAdapter mAdapter;
    private ArrayList<ImMessageBean.MessageBean> mMessageList;

    //评论 start
    private EditText mEditComment;//输入评论
    private TextView mTvSend;//发送按钮
    private String mCommentContent;//评论内容
    //评论 end

    private KeyboardChangeListener mKeyboardChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);
        initView();
        initListener();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mEditComment = findViewById(R.id.ed_comment);
        mTvSend = findViewById(R.id.tv_send);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.chartroom_recyclerview);
        mAdapter = new ChartRoomAdapter(this);
        mAdapter.addItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setLoadMoreListener(this);
    }

    private void initListener() {
        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);
        mTvSend.setOnClickListener(this);
        mEditComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTvSend.setEnabled(false);
                } else {
                    mTvSend.setEnabled(true);
                }
            }
        });
//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (keyboardIsShow) {
//                        InputMethodUtil.closeInputMethod(ChatRoomActivity.this, mEditComment);
//                        keyboardIsShow = false;
////                        return true;
//                    }
//                }
//                return false;
//            }
//        });

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (keyboardIsShow && isShouldHideInput(v, ev)) {
                InputMethodUtil.closeInputMethod(ChatRoomActivity.this, mEditComment);
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    private void setData() {
        mAdapter.setData(mMessageList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        ImMessageBean bean = parserJson(ImMessageBean.class);
        if (bean != null) {
            mMessageList = bean.getMsg();
            setData();
        } else {
            //无数据
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        if (TextUtils.isEmpty(mCommentContent))
            return;
        generateMessageBean();
    }

    /**
     * 模拟发送，并生成返回的MessageBean数据
     */
    private void generateMessageBean() {
        MessageBean bean = new MessageBean();
        bean.setMsgContent(mCommentContent);
        bean.setType(MESSAGE_TYPE_MYSELF);
        mMessageList.add(bean);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);//滚动到底部
        mEditComment.setText(null);
        mEditComment.clearFocus();
        closeInputMethod(this, mEditComment);
    }

    /**
     * 模拟获取更多数据
     */
    private void getMoreData() {
        mAdapter.loadMoreData(mRecyclerView);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.finishLoadMore();
            }
        }, 1000);
    }


    @Override
    public void onClick(View view) {
        if (mTvSend == view) {
            mCommentContent = mEditComment.getText().toString();
            sendMessage();
        }
    }

    public static void invoke(Context activity) {
        Intent intent = new Intent(activity, ChatRoomActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == HomeFragment.CHOOSE_CLASS) {
//            if (resultCode == RESULT_OK) {
//            }
//        }
    }

    @Override
    public void onItemClick(int position) {

    }

    /**
     * 加载更多中
     *
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(ImRecyclerView refreshLayout) {
        getMoreData();
    }

    /**
     * 加载完毕
     */
    @Override
    public void onLoadmoreComplte() {
        mAdapter.finishLoadMoreData();
    }

    // 一些util方法，应该都放在util里 start
    public void closeInputMethod(Context context, View view) {

        try {
            //获取输入法的服务
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            //判断是否在激活状态
            if (imm.isActive()) {
                //隐藏输入法!!,
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        } finally {
        }
    }

    private <T> T parserJson(final Class<T> t) {
        T bean = null;
        AssetManager assetManager = ChatRoomActivity.this.getAssets();
        InputStream is = null;
        StringBuffer stringBuffer = null;
        try {
            is = assetManager.open("immock.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            stringBuffer = new StringBuffer();

            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = stringBuffer.toString();
        if (!TextUtils.isEmpty(url)) {
            Gson gson = new Gson();
            bean = gson.fromJson(url, t);
        }
        return bean;
    }

    private boolean keyboardIsShow;

    /**
     * call back
     *
     * @param isShow         true is show else hidden
     * @param keyboardHeight keyboard height
     */
    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        keyboardIsShow = isShow;
        if (isShow) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);//滚动到底部
        } else {

        }
    }
// 一些util方法，应该都放在util里 end

}
