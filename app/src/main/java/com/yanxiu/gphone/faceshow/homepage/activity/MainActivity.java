package com.yanxiu.gphone.faceshow.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.util.ActivityManger;

public class MainActivity extends FaceShowBaseActivity implements View.OnClickListener {

    private final int mNavBarViewsCount = 4;
    private View[] mNavBarViews = new View[mNavBarViewsCount];
    private ImageView[] mNavIconViews = new ImageView[mNavBarViewsCount];
    private TextView[] mNavTextViews = new TextView[mNavBarViewsCount];
    private int mNormalNavTxtColor, mSelNavTxtColor;

    private final int INDEX_HOME_TAB = 0;//首页tab
    private final int INDEX_NOTICE_TAB = 1;//通知tab
    private final int INDEX_CLASSCIRCLE_TAB = 2;//班级圈tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

    public NaviFragmentFactory mNaviFragmentFactory;
    public FragmentManager mFragmentManager;

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
        mFragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        initBottomBar();
        showCurrentFragment(0);
    }

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_336600);
        mNormalNavTxtColor = getResources().getColor(R.color.color_999999);
        mNavBarViews[0] = findViewById(R.id.navi_1);
        mNavBarViews[1] = findViewById(R.id.navi_2);
        mNavBarViews[2] = findViewById(R.id.navi_3);
        mNavBarViews[3] = findViewById(R.id.navi_4);
        for (int i = 0; i < mNavBarViews.length; i++) {
            mNavBarViews[i].setOnClickListener(this);
            mNavIconViews[i] = (ImageView) mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = (TextView) mNavBarViews[i].findViewById(R.id.nav_txt);
        }
        mNavIconViews[0].setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.navi_1:
                curItem = INDEX_HOME_TAB;
                mNavIconViews[0].setEnabled(false);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_2:
                curItem = INDEX_NOTICE_TAB;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(false);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_3:
                curItem = INDEX_CLASSCIRCLE_TAB;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_4:
                curItem = INDEX_MY;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(false);
                break;
            default:
                break;
        }
        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }

    private void checkBottomBar(int index) {
        if (index >= 0 && index < mNavBarViews.length) {
            for (int i = 0; i < mNavBarViews.length; i++) {
                if (i == index) {
                    mNavTextViews[index].setTextColor(mSelNavTxtColor);
                } else {
                    mNavTextViews[i].setTextColor(mNormalNavTxtColor);
                }
            }
        }
    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        mLastSelectIndex = index;
        checkBottomBar(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mNaviFragmentFactory.hideAndShowFragment(mFragmentManager, index);
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

    /**
     * 跳转MainActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
