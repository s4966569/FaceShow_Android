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

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.activity.CourseDiscussActivity;
import com.yanxiu.gphone.faceshow.course.activity.EvaluationActivity;
import com.yanxiu.gphone.faceshow.course.activity.VoteActivity;
import com.yanxiu.gphone.faceshow.course.adapter.CourseTaskAdapter;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.recyclerview.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc on 17-11-8.
 *
 * task 部分 三项  投票、问卷、讨论
 *
 *
 */

public class CourseTaskFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    CourseDetailBean data;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setErrorLayoutFullScreen();
        mPublicLoadLayout.setContentView(inflater.inflate(R.layout.fragment_course_task_layout, container, false));
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);
        data= (CourseDetailBean) getArguments().get("data");
//        data = (getArguments() != null ? (CourseDetailBean) getArguments().get("data") : null);

        if (data == null) {
            Log.i(TAG, "onCreateView: data is null");
            mPublicLoadLayout.showOtherErrorView("暂无课程任务");
        } else {
            Log.i(TAG, "onCreateView: data not null ");
            if (data.getCourse() != null && data.getInteractSteps().size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                CourseTaskAdapter courseTaskAdapter = new CourseTaskAdapter(data.getInteractSteps(),getActivity());
                mRecyclerView.setAdapter(courseTaskAdapter);
                courseTaskAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        InteractStepsBean interactStep = data.getInteractSteps().get(position);
                        Intent intent = null;

                        /*判断 interact 类型 并设定相应的intent 目标activity*/
                        intent = checkInteractStepType(interactStep, intent);
//                        switch (interactStep.getInteractType()) {
//                            case 6:
//                                intent = new Intent(getContext(), CheckInDetailActivity.class);
//                                break;
//                            case 5:
////                                intent = new Intent(getContext(), QuestionnaireActivity.class);
//                                break;
//                            case 4:
//                                intent = new Intent(getContext(), CourseCommentActivity.class);
//                                break;
//                            case 3:
//                                intent = new Intent(getContext(), VoteActivity.class);
//                            default:
//                        }
//                        if (intent == null) {
//                            ToastUtil.showToast(getContext(), "未知类型");
//                            return;
//                        }
//                        intent.putExtra("stepId", String.valueOf(interactStep.getStepId()));
//                        if (getActivity() != null) {
//                            getActivity().startActivity(intent);
//                        }
                    }
                });
            } else {
                mPublicLoadLayout.showOtherErrorView("暂无课程任务");
            }
        }
        return mPublicLoadLayout;
    }

    private Intent checkInteractStepType(InteractStepsBean interactStep, Intent intent) {
        String type = interactStep.getInteractType();
        if (InteractStepsBean.VOTE.equals(type)) {
            VoteActivity.invoke(getActivity(),interactStep.getStepId());
//            intent = new Intent(getContext(), VoteActivity.class);
        } else if (InteractStepsBean.DISCUSS.equals(type)) {
            CourseDiscussActivity.invoke(getActivity(),interactStep);
//            intent = new Intent(getContext(), CourseCommentActivity.class);
        } else if (InteractStepsBean.QUESTIONNAIRES.equals(type)) {
            EvaluationActivity.invoke(getActivity(),interactStep.getStepId());
//                            intent = new Intent(getContext(), QuestionnaireActivity.class);
        } else if (InteractStepsBean.CHECK_IN.equals(type)) {
            CheckInByQRActivity.toThisAct(getActivity());
//            intent = new Intent(getContext(), CheckInDetailActivity.class);
        } else {

        }
        return intent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
