package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.user.ModifyUserSubjectAdapter;
import com.yanxiu.gphone.faceshow.user.StageSubjectModel;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

/**
 * A simple {@link Fragment} subclass.
 * 设置学段学科
 * 首先设置学段
 */
public class ModifyStageFragment extends Fragment {


    private PublicLoadLayout publicLoadLayout;

    public ModifyStageFragment() {
        // Required empty public constructor
    }

    private SysUserBean userBean;

    private StageSubjectModel mStageSubjectModel;
    private int mSelectedPosition = 0;
    private boolean canSelected = false;
    RecyclerView mRecyclerViewChooseStage;

    public void setUserBean(SysUserBean userBean) {
        this.userBean = userBean;
    }

    private View root;

    public int getmSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.activity_modify_user_stage_and_sbuject, null);
            publicLoadLayout = new PublicLoadLayout(getActivity());
            publicLoadLayout.setContentView(root);

        }
        viewInit(root);
        initRecyclerView();
        return publicLoadLayout;
    }

    private void initRecyclerView() {
        mStageSubjectModel = RequestBase.getGson().fromJson(FileUtil.getDataFromAssets(getActivity(), "stageSubject.json"), StageSubjectModel.class);
        ModifyUserSubjectAdapter modifyUserStageAdapter = new ModifyUserSubjectAdapter(mStageSubjectModel.getData(), mIRecyclerViewItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewChooseStage.setLayoutManager(linearLayoutManager);
        mRecyclerViewChooseStage.setAdapter(modifyUserStageAdapter);
        /*初始化 选择的显示*/
        for (StageSubjectModel.DataBean dataBean : mStageSubjectModel.getData()) {
            if (dataBean.getId().equals(userBean.getStage()+"")) {
                modifyUserStageAdapter.setDefaultSelectPosition(mStageSubjectModel.getData().indexOf(dataBean));
                break;
            }
        }
    }

    /**
     * 选择监听
     * 只有选择了学段之后才能选择 科目
     */
    private IRecyclerViewItemClick mIRecyclerViewItemClickListener = new IRecyclerViewItemClick() {
        @Override
        public void onItemClick(View view, int position) {
            enableNextStepBtn();
            mSelectedPosition = position;
            /*获取 stage的ID*/
            String stageId=mStageSubjectModel.getData().get(mSelectedPosition).getId();
            userBean.setStage(Integer.valueOf(stageId));
            userBean.setStageName(mStageSubjectModel.getData().get(mSelectedPosition).getName());
            canSelected = true;
        }
    };
    ImageView backView;
    TextView titleTxt;
    TextView rightTxt;

    private void viewInit(View root) {
        backView = root.findViewById(R.id.title_layout_left_img);
        titleTxt = root.findViewById(R.id.title_layout_title);
        rightTxt = root.findViewById(R.id.title_layout_right_txt);

        backView.setVisibility(View.VISIBLE);
        rightTxt.setVisibility(View.VISIBLE);

        rightTxt.setText("下一步");
        titleTxt.setText("选择学段");
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });

        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }
            }
        });

        mRecyclerViewChooseStage = root.findViewById(R.id.recyclerView_choose_stage);
        disableNextStepBtn();
    }

    private ModifySysInfoCallback modifySysInfoCallback;

    /**
     * 开启下一步
     */
    public void enableNextStepBtn() {
        rightTxt.setEnabled(true);
        rightTxt.setTextColor(getActivity().getResources().getColor(R.color.color_1da1f2));
    }

    /**
     * 关闭下一步
     */
    public void disableNextStepBtn() {
        rightTxt.setEnabled(false);
        rightTxt.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
    }


    public void setModifySysInfoCallback(ModifySysInfoCallback modifySysInfoCallback) {
        this.modifySysInfoCallback = modifySysInfoCallback;
    }

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

}
