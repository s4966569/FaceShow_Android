package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifySubjectFragment extends Fragment {

    private PublicLoadLayout publicLoadLayout;
    private View root;
    private RecyclerView mRecyclerViewChooseStage;
    private SysUserBean userBean;
    private StageSubjectModel mStageSubjectModel;
    private TextView rightTxt;
    private int mSelectedPosition=0;

    private boolean isReselected=false;

    public void setReselected(boolean reselected) {
        isReselected = reselected;
    }

    public void setmSelectedPosition(int mSelectedPosition) {
        this.mSelectedPosition = mSelectedPosition;
    }

    public ModifySubjectFragment() {
        // Required empty public constructor
    }
    public void setUserBean(SysUserBean userBean) {
        this.userBean = userBean;
    }

    public void setTitleText(String title){
        stageText=title;
    }

    private String stageText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root==null) {
            root=inflater.inflate(R.layout.activity_modify_user_stage_and_sbuject, null);
            publicLoadLayout = new PublicLoadLayout(getActivity());
            publicLoadLayout.setContentView(root);
        }
        viewInit(root);
        return publicLoadLayout;
    }


    private void recyclerInit(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewChooseStage.setLayoutManager(linearLayoutManager);
        mStageSubjectModel = RequestBase.getGson().fromJson(FileUtil.getDataFromAssets(getActivity(), "stageSubject.json"), StageSubjectModel.class);
        ModifyUserSubjectAdapter modifyUserSubjectAdapter = new ModifyUserSubjectAdapter(mStageSubjectModel.getData().get(mSelectedPosition).getSub(),mIRecyclerViewItemClick);
        mRecyclerViewChooseStage.setAdapter(modifyUserSubjectAdapter);

         /*初始化 选择的显示*/
        /*如果重新选择了 学段 放弃 默认选项*/
        if (!isReselected) {
            List<StageSubjectModel.DataBean> subList= mStageSubjectModel.getData().get(mSelectedPosition).getSub();
            for (StageSubjectModel.DataBean dataBean :subList) {
                if (dataBean.getId().equals(userBean.getSubject()+"")||dataBean.getName().equals(userBean.getSubjectName())) {
                    modifyUserSubjectAdapter.setDefaultSelectPosition(subList.indexOf(dataBean));
                    modifyUserSubjectAdapter.notifyDataSetChanged();
                    enableNextStepBtn();
                    break;
                }
            }
        }

    }
    private IRecyclerViewItemClick mIRecyclerViewItemClick= new IRecyclerViewItemClick() {
        @Override
        public void onItemClick(View view, int postion) {
            String subjectId=mStageSubjectModel.getData().get(mSelectedPosition).getSub().get(postion).getId();
            userBean.setSubject(Integer.valueOf(subjectId));
            userBean.setSubjectName(mStageSubjectModel.getData().get(mSelectedPosition).getSub().get(postion).getName());
        }
    };

    private void viewInit(View root ){
        ImageView backView=root.findViewById(R.id.title_layout_left_img);
        TextView titleTxt=root.findViewById(R.id.title_layout_title);
        rightTxt=root.findViewById(R.id.title_layout_right_txt);
        final EditText editText=root.findViewById(R.id.edt_name);

        backView.setVisibility(View.VISIBLE);
        rightTxt.setVisibility(View.VISIBLE);

        rightTxt.setText("保存");

        if (TextUtils.isEmpty(stageText)) {
            titleTxt.setText("选择学段");
        }else {
            titleTxt.setText(stageText);
        }

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
        mRecyclerViewChooseStage=root.findViewById(R.id.recyclerView_choose_stage);
        recyclerInit();
    }
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
    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }
}
