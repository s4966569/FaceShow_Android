package com.yanxiu.gphone.faceshow.webView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.main.GetToolsResponse;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用了三方架包 AgentWeb
 * @author frc
 */
public class FaceShowWebActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;
    GetToolsResponse data;
    AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_show_web_layout);
        ButterKnife.bind(this);
        data = (GetToolsResponse) getIntent().getSerializableExtra("data");
        String url = data.getData().getTools().get(0).getEventObj().getContent() + "?classId=" + SpManager.getUserInfo().getClassId() + "&token=" + SpManager.getToken();

        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLlRoot, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        mTitleLayoutTitle.setText(title);
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.title_layout_left_img)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
