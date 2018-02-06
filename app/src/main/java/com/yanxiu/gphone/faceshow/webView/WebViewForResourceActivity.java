package com.yanxiu.gphone.faceshow.webView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 *         用来显示资源的WebView
 */
public class WebViewForResourceActivity extends FaceShowBaseActivity {

    //    @BindView(R.id.title_layout_left_img)
//    ImageView titleLayoutLeftImg;
//    @BindView(R.id.title_layout_title)
//    TextView titleLayoutTitle;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    AgentWeb mAgentWeb;

    public static void lunch(Context context, String url) {
        Intent intent = new Intent(context, WebViewForResourceActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_for_resource);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llRoot, new RelativeLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
//                        titleLayoutTitle.setVisibility(View.VISIBLE);
//                        titleLayoutTitle.setText(title);
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url);
        mAgentWeb.getAgentWebSettings().getWebSettings().setMediaPlaybackRequiresUserGesture(false);
    }

//    @OnClick(R.id.title_layout_left_img)
//    public void onViewClicked() {
//
//    }

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
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
