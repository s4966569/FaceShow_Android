package com.yanxiu.gphone.faceshow.course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.activity.SpecialistIntroductionActivity;
import com.yanxiu.gphone.faceshow.course.adapter.LectureInfoAdapter;
import com.yanxiu.gphone.faceshow.course.bean.CourseBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.recyclerview.RecyclerViewItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc  on 17-11-9.
 */

public class LectureInfoListFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setErrorLayoutFullScreen();
        mPublicLoadLayout.setContentView(R.layout.fragment_lecture_info_list_layout);
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);

//        GetCourseResponse.CourseBean course = getArguments() != null ? (GetCourseResponse.CourseBean) getArguments().get("data") : null;
        final CourseBean course = getArguments() != null ? (CourseBean) getArguments().get("data") : null;
        Log.i(TAG, "onCreateView: "+new Gson().toJson(course));
        if (course != null && course.getLecturerInfos() != null && course.getLecturerInfos().size() > 0) {
            LectureInfoAdapter lectureInfoAdapter = new LectureInfoAdapter(course.getLecturerInfos());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(lectureInfoAdapter);
            lectureInfoAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    LecturerInfosBean lecturerInfosBean = course.getLecturerInfos().get(position);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("info",lecturerInfosBean);
                    Intent intent=new Intent(getActivity(), SpecialistIntroductionActivity.class);
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                }
            });


        } else {
            mPublicLoadLayout.showOtherErrorView(getString(R.string.no_lecture));
        }

        return mPublicLoadLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
