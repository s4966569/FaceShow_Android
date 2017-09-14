package com.yanxiu.gphone.faceshow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.util.ActivityManger;

public class MainActivity extends FaceShowBaseActivity implements View.OnClickListener {

    private View mBottomNaviLayout;
    private View[] mNavBarViews = new View[4];
    private ImageView[] mNavIconViews = new ImageView[4];
    private TextView[] mNavTextViews = new TextView[4];
    private int mNormalNavTxtColor, mSelNavTxtColor;

    private final int INDEX_1 = 0;//1tab
    private final int INDEX_2 = 1;//2tab
    private final int INDEX_3 = 2;//3tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

//    public NaviFragmentFactory mNaviFragmentFactory;
//    public FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initView() {
        mBottomNaviLayout = findViewById(R.id.navi_switcher);
//        mFragmentManager = getSupportFragmentManager();
//        mNaviFragmentFactory = new NaviFragmentFactory();
        initBottomBar();
        showCurrentFragment(0);
    }

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_336600);
        mNormalNavTxtColor = getResources().getColor(R.color.color_999999);
        mNavBarViews[0] = findViewById(R.id.navi_1);
        mNavBarViews[1] = findViewById(R.id.navi_2);
        mNavBarViews[2] = findViewById(R.id.navi_3);
        mNavBarViews[3] = findViewById(R.id.navi_my);
        for (int i = 0; i < 4; i++) {
            mNavBarViews[i].setOnClickListener(this);
            mNavIconViews[i] = (ImageView) mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = (TextView) mNavBarViews[i].findViewById(R.id.nav_txt);
        }
        mNavIconViews[0].setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_1;
        switch (view.getId()) {
            case R.id.navi_1:
                curItem = INDEX_1;
                mNavIconViews[0].setEnabled(false);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_2:
                curItem = INDEX_2;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(false);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_3:
                curItem = INDEX_3;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_my:
                curItem = INDEX_MY;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(false);
                break;
            default:
                break;
        }
//        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
        showCurrentFragment(curItem);
//        }
    }

    private void checkBottomBarProcess(int index) {
        if (index >= 0 && index < 4) {
            resetBottomBar();
            mNavTextViews[index].setTextColor(mSelNavTxtColor);

        }
    }

    private void resetBottomBar() {
        for (int i = 0; i < 4; i++) {
            mNavTextViews[i].setTextColor(mNormalNavTxtColor);
        }
    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        mLastSelectIndex = index;
        checkBottomBarProcess(index);
//        if (mNaviFragmentFactory == null) {
//            mNaviFragmentFactory = new NaviFragmentFactory();
//        }
//        if (mFragmentManager == null) {
//            mFragmentManager = getSupportFragmentManager();
//        }
//        mNaviFragmentFactory.hideAndShowFragment(mFragmentManager, index);
    }

    /**
     * 退出间隔时间戳
     */
    private long mBackTimestamp = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - mBackTimestamp <= 2000) {
                //Todo 退出程序
                ActivityManger.destoryAll();
            } else {
                mBackTimestamp = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
