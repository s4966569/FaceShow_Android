package com.test.yanxiu.im_ui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_ui.callback.OnNaviLeftBackCallback;

public class ImMsgListActivity extends FragmentActivity {
    private ImTitleLayout mTitleLayout;
    private DbTopic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        setupView();
        setupData();
    }

    private void setupView() {
        mTitleLayout = findViewById(R.id.title_layout);
        mTitleLayout.setOnNaviLeftBackCallback(new OnNaviLeftBackCallback() {
            @Override
            public void onNaviBack() {
                finish();
            }
        });
    }

    private void setupData() {
        topic = SharedSingleton.getInstance().get(Constants.kShareTopic);
    }
}
