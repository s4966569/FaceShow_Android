package com.test.yanxiu.im_ui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.stetho.inspector.protocol.module.Database;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class ImMsgListActivity extends FragmentActivity {
    private DbTopic topic;

    private ImTitleLayout mTitleLayout;
    private RecyclerView mMsgListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        topic = SharedSingleton.getInstance().get(Constants.kShareTopic);
        setupData();
        setupView();
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
        MsgListAdapter adapter = new MsgListAdapter(this, topic.mergedMsgs);
        mMsgListRecyclerView.setAdapter(adapter);
    }

    private void setupData() {
    }
}
