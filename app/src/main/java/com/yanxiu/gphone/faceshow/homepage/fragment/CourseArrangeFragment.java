package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;


/**
 * 首页tab里 “课程安排”Fragment
 */
public class CourseArrangeFragment extends FaceShowBaseFragment {
    private final static String TAG = CourseArrangeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_my, container, false);
        return view;
    }
}
