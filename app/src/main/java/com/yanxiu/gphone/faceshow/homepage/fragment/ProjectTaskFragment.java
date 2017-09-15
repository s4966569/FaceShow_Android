package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;


/**
 * 首页tab里 “项目任务”Fragment
 */
public class ProjectTaskFragment extends FaceShowBaseFragment {
    private final static String TAG = ProjectTaskFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_task, container, false);
        return view;
    }
}
