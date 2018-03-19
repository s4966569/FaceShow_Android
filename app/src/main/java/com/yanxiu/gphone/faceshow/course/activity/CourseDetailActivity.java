package com.yanxiu.gphone.faceshow.course.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.yanxiu.common_base.utils.ScreenUtils;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.course.GetCourseRequest;
import com.yanxiu.gphone.faceshow.course.GetCourseResponse;
import com.yanxiu.gphone.faceshow.course.bean.CourseBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.course.fragment.CourseResourceFragment;
import com.yanxiu.gphone.faceshow.course.fragment.CourseTaskFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailResponse;
import com.yanxiu.gphone.faceshow.util.DateFormatUtil;
import com.yanxiu.gphone.faceshow.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseDetailActivity extends FaceShowBaseActivity {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.course_title)
    TextView mCourseTitle;
    @BindView(R.id.course_time)
    TextView mCourseTime;
    @BindView(R.id.course_teacher)
    TextView mCourseTeacher;
    @BindView(R.id.course_location)
    TextView mCourseLocation;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.img_left_back)
    ImageView mImgLeftBack;
    @BindView(R.id.tv_see_course)
    TextView mTvSeeCourse;
    private PublicLoadLayout mPublicLoadLayout;
    private UUID mGetCourseRequestUUID;
    private GetCourseResponse.DataBean data;


    /*courseID*/
    private String mCourseid;

    private CourseBean courseBean;
    private CourseDetailBean courseDetailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_course_detail);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
//        getCourse();
        mCourseid = getIntent().getStringExtra("courseId");
        requestData();



    }
    private void toCourseMessage() {
        Intent intent = new Intent(this, CourseMessageActivity.class);
        intent.putExtra("data", courseDetailBean!=null?courseDetailBean.getCourse():null);
        startActivity(intent);
    }
    /**
     * 课程详情页 底部 任务列表和资源列表初始化
     * 需要在 获取 网络请求结果后进行
     * */
    private void viewPagerInit(CourseDetailBean courseDetailBean) {
        Log.i(TAG, "viewPagerInit: "+new Gson().toJson(courseDetailBean.getCourse()));
        /*任务列表*/
        final CourseTaskFragment taskFragment=new CourseTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",courseDetailBean);
        taskFragment.setArguments(bundle);
        /*资源列表*/
        final CourseResourceFragment resourceFragment=new CourseResourceFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("data", courseDetailBean.getCourse());
        resourceFragment.setArguments(bundle1);
        /*viewpager*/
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position==0) {
                    return taskFragment;
                }else if (position==1){
                    return resourceFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return getString(R.string.course_task);
                } else {
                    return getString(R.string.course_resource);
                }
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_1da1f2));
        setUpIndicatorWidth(mTabLayout, 20, 20);
    }
    private List<Fragment> list = new ArrayList<>();


    private void requestData() {
        mPublicLoadLayout.showLoadingView();
        CourseDetailRequest courseDetailRequest = new CourseDetailRequest();
        courseDetailRequest.courseId = mCourseid;
        courseDetailRequest.startRequest(CourseDetailResponse.class, new HttpCallback<CourseDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseDetailResponse ret) {
                mPublicLoadLayout.finish();
             /*TODO: 2018/3/5  获取课程详细信息 成功  设置数据
                ToastUtil.showToast(CourseDetailActivity.this, "请求 课程详细信息成功");*/
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    courseDetailBean=ret.getData();
                    courseBean = ret.getData().getCourse();
                    /*这里获取 课程标题*/
                    setCourseTitleInfo(courseBean);
                     /*对 viewpager 以及 课程任务 和 课程资源列表进行初始化*/
                    viewPagerInit(courseDetailBean);
                } else {
                    mPublicLoadLayout.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();
                mPublicLoadLayout.showNetErrorView();
            }
        });
    }

    private void setCourseTitleInfo(CourseBean courseBean) {
//        CourseBean courseBean=ret.getData().getCourse();
//        Log.i(TAG, "onSuccess: " + new Gson().toJson(courseBean));
        mCourseTitle.setText(courseBean.getCourseName());
        mCourseTeacher.setText(courseBean.getLecturer());
        mCourseLocation.setText(courseBean.getSite());
        mCourseTime.setText(StringUtils.getCourseTime(courseBean.getStartTime())
                + " 至 " + StringUtils.getCourseTime(courseBean.getEndTime()));
    }

    private void getCourse() {
        mPublicLoadLayout.showLoadingView();
        final GetCourseRequest getCourseRequest = new GetCourseRequest();
        getCourseRequest.courseId = getIntent().getStringExtra("courseId");
        mGetCourseRequestUUID = getCourseRequest.startRequest(GetCourseResponse.class, new HttpCallback<GetCourseResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCourseResponse ret) {
                mPublicLoadLayout.finish();
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    data = ret.getData();
                    mCourseTitle.setText(Html.fromHtml(ret.getData().getCourse().getCourseName()));
                    if (ret.getData().getCourse().getLecturerInfos() != null && ret.getData().getCourse().getLecturerInfos() != null && ret.getData().getCourse().getLecturerInfos().size() > 0) {
                        mCourseTeacher.setText(ret.getData().getCourse().getLecturerInfos().get(0).getLecturerName());
                    } else {
                        mCourseTeacher.setText("暂无");
                    }
                    mCourseLocation.setText(TextUtils.isEmpty(ret.getData().getCourse().getSite()) ? getString(R.string.wait_for) : ret.getData().getCourse().getSite());
                    mCourseTime.setText(DateFormatUtil.getCourseTime(ret.getData().getCourse().getStartTime(), ret.getData().getCourse().getEndTime()));
                    CourseResourceFragment courseResourceFragment = new CourseResourceFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("courseId", getCourseRequest.courseId);
                    courseResourceFragment.setArguments(bundle1);
                    CourseTaskFragment courseTaskFragment = new CourseTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", ret.getData());
                    courseTaskFragment.setArguments(bundle);
                    list.add(courseTaskFragment);
                    list.add(courseResourceFragment);
                    mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return list.get(position);
                        }

                        @Override
                        public int getCount() {
                            return list.size();
                        }

                        @Nullable
                        @Override
                        public CharSequence getPageTitle(int position) {
                            if (position == 0) {
                                return getString(R.string.course_task);
                            } else {
                                return getString(R.string.course_resource);
                            }
                        }
                    });
                    mTabLayout.setupWithViewPager(mViewPager);
                    setUpIndicatorWidth(mTabLayout, 20, 20);
                } else {
                    mPublicLoadLayout.showOtherErrorView(ret.getError().getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                mPublicLoadLayout.hiddenOtherErrorView();
            }
        });

    }

    @OnClick({R.id.img_left_back, R.id.tv_see_course})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_back:
                this.finish();
                break;
            case R.id.tv_see_course:
//                Intent intent = new Intent(CourseDetailActivity.this, CourseMessageActivity.class);
//                intent.putExtra("data", data.getCourse());
//                startActivity(intent);
                toCourseMessage();
                break;
            default:
        }
    }

    private void setUpIndicatorWidth(TabLayout tabLayout, int marginLeft, int marginRight) {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(ScreenUtils.dpToPxInt(CourseDetailActivity.this, marginLeft));
                    params.setMarginEnd(ScreenUtils.dpToPxInt(CourseDetailActivity.this, marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetCourseRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseRequestUUID);
        }
    }
}
