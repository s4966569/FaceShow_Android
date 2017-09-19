package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 首页tab里 “日程计划”Fragment
 */
public class ScheduleFragment extends FaceShowBaseFragment {
    private final static String TAG = ScheduleFragment.class.getSimpleName();

    @BindView(R.id.schedule_name)
    TextView schedule_name;
    @BindView(R.id.schedule_img)
    ImageView schedule_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.schedule_img})
    public void onViewClicked(View view) {

    }

    private void openPicture() {
    }
}
