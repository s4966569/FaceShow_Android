package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.HomeFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.activity.ChooseClassActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.http.main.GetToolsRequest;
import com.yanxiu.gphone.faceshow.http.main.GetToolsResponse;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;
import com.yanxiu.gphone.faceshow.webView.FaceShowWebActivity;


/**
 * 首页 “首页”Fragment
 */
public class HomeFragment extends FaceShowBaseFragment implements View.OnClickListener {
    private final static String TAG = HomeFragment.class.getSimpleName();

    private MainActivity mActivity;
    public MainBean mMainBean;

    private PublicLoadLayout mRootView;

    public HomeFragmentFactory mFragmentFactory;
    public FragmentManager mFragmentManager;

    private final int INDEX_HOME_TAB = 0;//首页tab
    private final int INDEX_NOTICE_TAB = 1;//通知tab
    private final int INDEX_CLASSCIRCLE_TAB = 2;//班级圈tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

    private final int mNavBarViewsCount = 4;
    private View[] mNavBarViews = new View[mNavBarViewsCount];

    private View mCourseArrange_tab;//课程安排tab
    private View mResources_tab;//资源tab
    private View mProjectTask_tab;//项目任务taba
    private View mSchedule_tab;//日程计划tab

    private TextView mTitle;
    private TextView mCheckInEnterTV;//签到入口文字描述
    private TextView mProject_tv;//项目名称
    private TextView mClass_tv;//班级
    private ImageView mCheckInEnterIMG;
    private ImageView mImgTools;

    private ImageView mImgResourceRedDot, mImgProjectTaskRedDot;
    private int lastIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getContext());
        mRootView.setContentView(R.layout.fragment_home);
        mActivity = (MainActivity) getActivity();
        initData();
//        initView();
        intListener();

        return mRootView;
    }

    /**
     * 目前仅年会信息
     */
    private void getToolsData() {
        GetToolsRequest getToolsRequest = new GetToolsRequest();
        getToolsRequest.clazsId = SpManager.getUserInfo().getClassId();
        getToolsRequest.startRequest(GetToolsResponse.class, new HttpCallback<GetToolsResponse>() {
            @Override
            public void onSuccess(RequestBase request, final GetToolsResponse ret) {
                if (ret != null && ret.getData() != null && ret.getData().getTools() != null && ret.getData().getTools().size() > 0) {
                    mImgTools.setVisibility(View.VISIBLE);
//                    mImgTools.setText(ret.getData().getTools().get(0).getName());
                    mImgTools.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), FaceShowWebActivity.class);
                            intent.putExtra("data",ret);
                            startActivity(intent);
//                            ToastUtil.showToast(getContext(), ret.getData().getTools().get(0).getEventObj().getContent());
                        }
                    });
                } else {
                    mImgTools.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mImgTools.setVisibility(View.GONE);
            }
        });
    }

    public void toRefresh() {
        if (TextUtils.isEmpty(SpManager.getUserInfo().getClassId())) {
            requestData();
        } else {
            initView();
        }
        getToolsData();
    }

    private void requestData() {
        MainRequest mainRequest = new MainRequest();
        mainRequest.startRequest(MainResponse.class, new HttpCallback<MainResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0 && ret.getData() != null && ret.getData().getClazsInfo() != null
                        && ret.getData().getProjectInfo() != null) {
                    UserInfo.Info info = SpManager.getUserInfo();
                    info.setClassId(String.valueOf(ret.getData().getClazsInfo().getId()));
                    info.setClassName(ret.getData().getClazsInfo().getClazsName());
                    info.setProjectName(ret.getData().getProjectInfo().getProjectName());
                    SpManager.saveUserInfo(info);

                    mMainBean = ret.getData();
                    initView();
                } else {
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

    }

    private void initData() {
//        mMainBean = mActivity.mMainData;
        toRefresh();
    }

    public final static int CHOOSE_CLASS = 0X003;

    private void initView() {
        mTitle = (TextView) mRootView.findViewById(R.id.title_layout_title);
        mTitle.setText("研修宝");
        mCheckInEnterTV = (TextView) mRootView.findViewById(R.id.title_layout_signIn);
        mCheckInEnterTV.setVisibility(View.VISIBLE);
        mCheckInEnterIMG = (ImageView) mRootView.findViewById(R.id.title_layout_right_img);
        mCheckInEnterIMG.setImageResource(R.drawable.scan_selector);
        mCheckInEnterIMG.setVisibility(View.VISIBLE);
        TextView textView = (TextView) mRootView.findViewById(R.id.title_layout_left_txt);
        textView.setText(R.string.choose_class);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_1da1f2));
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(HomeFragment.this.getActivity(), ChooseClassActivity.class), CHOOSE_CLASS);
            }
        });
        mProject_tv = (TextView) mRootView.findViewById(R.id.project_tv);
        mClass_tv = (TextView) mRootView.findViewById(R.id.class_tv);
        mImgProjectTaskRedDot = (ImageView) mRootView.findViewById(R.id.img_project_task_red_dot);
        mImgResourceRedDot = (ImageView) mRootView.findViewById(R.id.img_resource_red_dot);
        mImgTools = (ImageView) mRootView.findViewById(R.id.img_tools);

        if (mMainBean != null && mMainBean.getClazsInfo() != null && mMainBean.getProjectInfo() != null) {
            mProject_tv.setText(mMainBean.getProjectInfo().getProjectName());
            mClass_tv.setText(mMainBean.getClazsInfo().getClazsName());
        } else {
            mProject_tv.setText(SpManager.getUserInfo().getProjectName());
            mClass_tv.setText(SpManager.getUserInfo().getClassName());
        }
        initTabBar();
        mFragmentManager = getChildFragmentManager();
        if (mFragmentFactory == null) {
            mFragmentFactory = new HomeFragmentFactory();
        } else {
            if (mFragmentFactory.getProjectTaskFragment() != null) {
                mFragmentFactory.getProjectTaskFragment().refreshData();
            }
            if (mFragmentFactory.getResourcesFragment() != null) {
                mFragmentFactory.getResourcesFragment().refreshData();
            }
            if (mFragmentFactory.getCourseArrangeFragment() != null) {
                mFragmentFactory.getCourseArrangeFragment().refreshData();
            }
            if (mFragmentFactory.getScheduleFragment() != null) {
                mFragmentFactory.getScheduleFragment().refreshData();
            }
        }
        showCurrentFragment(lastIndex);
    }

    private void initTabBar() {
        mCourseArrange_tab = mRootView.findViewById(R.id.courseArrange_tab);
        mResources_tab = mRootView.findViewById(R.id.resources_tab);
        mProjectTask_tab = mRootView.findViewById(R.id.projectTask_tab);
        mSchedule_tab = mRootView.findViewById(R.id.schedule_tab);

        mNavBarViews[0] = mCourseArrange_tab;
        mNavBarViews[1] = mResources_tab;
        mNavBarViews[2] = mProjectTask_tab;
        mNavBarViews[3] = mSchedule_tab;


    }

    public void showResourceRedDot() {

        ResourcesFragment resourcesFragment = mFragmentFactory.getResourcesFragment();
        //如果当前页在资源页就不显示红点

        if (resourcesFragment != null) {
            if (mFragmentFactory.getCurrentItem() != 1) {
                mImgResourceRedDot.setVisibility(View.VISIBLE);
            } else {
                mImgResourceRedDot.setVisibility(View.GONE);
            }
            resourcesFragment.refreshData();
        } else {
            mImgResourceRedDot.setVisibility(View.VISIBLE);
        }

    }

    public void showTaskRedDot() {
        ProjectTaskFragment projectTaskFragment = mFragmentFactory.getProjectTaskFragment();
        if (projectTaskFragment != null) {
            if (mImgProjectTaskRedDot.getVisibility() == View.GONE) {
                projectTaskFragment.refreshData();
            }
        }
        mImgProjectTaskRedDot.setVisibility(View.VISIBLE);
    }

    public void hideTaskRedDot() {
        mImgProjectTaskRedDot.setVisibility(View.GONE);
    }

    public void hideResourceRedDot() {
        mImgResourceRedDot.setVisibility(View.GONE);
    }


    private void intListener() {
        mCourseArrange_tab.setOnClickListener(this);
        mResources_tab.setOnClickListener(this);
        mProjectTask_tab.setOnClickListener(this);
        mSchedule_tab.setOnClickListener(this);
        mCheckInEnterTV.setOnClickListener(this);
        mCheckInEnterIMG.setOnClickListener(this);
        for (int i = 0; i < mNavBarViews.length; i++) {
            mNavBarViews[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.courseArrange_tab:
                curItem = INDEX_HOME_TAB;
                mNavBarViews[0].setEnabled(false);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(true);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onCourseButton(getContext());
                break;
            case R.id.resources_tab:
                curItem = INDEX_NOTICE_TAB;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(false);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(true);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onResourceButton(getContext());
                mImgResourceRedDot.setVisibility(View.GONE);
                break;
            case R.id.projectTask_tab:
                curItem = INDEX_CLASSCIRCLE_TAB;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(false);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(true);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onTaskButton(getContext());
                break;
            case R.id.schedule_tab:
                curItem = INDEX_MY;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(false);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(true);
                EventUpdate.onScheduleButton(getContext());
                break;
            case R.id.title_layout_signIn:
            case R.id.title_layout_right_img:
                CheckInByQRActivity.toThisAct(getActivity());
                EventUpdate.onHomeSignInButton(getContext());
                return;
            default:
                break;
        }
        if (lastIndex != curItem) {
            showCurrentFragment(curItem);
        }
        lastIndex = curItem;
    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        if (index == 0) {
            //默认选项
            mNavBarViews[0].setEnabled(false);
            mNavBarViews[0].setSelected(true);
        }
        mLastSelectIndex = index;
        if (mFragmentFactory == null) {
            mFragmentFactory = new HomeFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getChildFragmentManager();
        }
        mFragmentFactory.hideAndShowFragment(mFragmentManager, index);
    }
}
