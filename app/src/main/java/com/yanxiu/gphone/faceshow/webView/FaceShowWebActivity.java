package com.yanxiu.gphone.faceshow.webView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.just.agentweb.AgentWeb;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.main.GetToolsResponse;
import com.test.yanxiu.common_base.utils.ScreenUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用了三方架包 AgentWeb
 *
 * @author frc
 */
public class FaceShowWebActivity extends FaceShowBaseActivity {

    @BindView(R.id.ll_root)
    RelativeLayout mLlRoot;
    GetToolsResponse data;
    AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_face_show_web_layout);
        ButterKnife.bind(this);
        data = (GetToolsResponse) getIntent().getSerializableExtra("data");

        String url = data.getData().getTools().get(0).getEventObj().getContent();
        if (url.contains("?")) {
            url = url + "&classId=" + SpManager.getUserInfo().getClassId() + "&token=" + SpManager.getToken() + "&_=" + System.currentTimeMillis();
        } else {
            url = url + "?classId=" + SpManager.getUserInfo().getClassId() + "&token=" + SpManager.getToken() + "&_=" + System.currentTimeMillis();
        }

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLlRoot, new RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .createAgentWeb()
                .ready()
                .go(url);


        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDomStorageEnabled(true);

// 缓存白屏
        String appCachePath = getApplicationContext().getCacheDir()
                .getAbsolutePath() + "/webcache";
// 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabasePath(appCachePath);


        ImageView backImage = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) ScreenUtils.dpToPx(this, 30), (int) ScreenUtils.dpToPx(this, 30));
        layoutParams.setMargins((int) ScreenUtils.dpToPx(this, 5), (int) ScreenUtils.dpToPx(this, 10), 0, 0);
        backImage.setLayoutParams(layoutParams);
        backImage.setBackgroundResource(R.drawable.selector_back);
        mLlRoot.addView(backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceShowWebActivity.this.finish();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        mAgentWeb.clearWebCache();
        mAgentWeb.destroyAndKill();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
