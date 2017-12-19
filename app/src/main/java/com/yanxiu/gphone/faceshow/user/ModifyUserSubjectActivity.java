package com.yanxiu.gphone.faceshow.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseCallback;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.user.request.ModifyUserInfoRequest;
import com.yanxiu.gphone.faceshow.user.response.ModifyUserInfoResponse;
import com.yanxiu.gphone.faceshow.util.ActivityManger;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyUserSubjectActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView mTitleLayoutRightTxt;
    @BindView(R.id.recyclerView_choose_subject)
    RecyclerView mRecyclerViewChooseSubject;

    private StageSubjectModel.DataBean mData;
    private int mSelectedPosition =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_subject);
        ButterKnife.bind(this);
        initTitle();
        mData= (StageSubjectModel.DataBean) getIntent().getSerializableExtra("data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewChooseSubject.setLayoutManager(linearLayoutManager);
        ModifyUserSubjectAdapter modifyUserSubjectAdapter = new ModifyUserSubjectAdapter(mData.getSub(),mIRecyclerViewItemClick);
        mRecyclerViewChooseSubject.setAdapter(modifyUserSubjectAdapter);

    }
    private IRecyclerViewItemClick mIRecyclerViewItemClick= new IRecyclerViewItemClick() {
        @Override
        public void onItemClick(View view, int postion) {
            mSelectedPosition =postion;
        }
    };


    private void initTitle() {
        mTitleLayoutRightTxt.setText(R.string.sure);
        mTitleLayoutRightTxt.setVisibility(View.VISIBLE);
        mTitleLayoutRightTxt.setTextColor(ContextCompat.getColor(this, R.color.color_1da1f2));
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.choose_subject);
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                saveStageSubject(mData.getId(),mData.getSub().get(mSelectedPosition).getId());

                break;
            default:
                break;
        }
    }
    private PublicLoadLayout publicLoadLayout;

    /**
     * 保存用户学段学科
     *
     */
    private void saveStageSubject(final String stageId, final String subjectId) {
        if (publicLoadLayout==null)
        {
            publicLoadLayout = new PublicLoadLayout(this);
        }
        publicLoadLayout.showLoadingView();
        ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
        modifyUserInfoRequest.stage = stageId;
        modifyUserInfoRequest.subject =subjectId;
        modifyUserInfoRequest.startRequest(ModifyUserInfoResponse.class, new FaceShowBaseCallback<ModifyUserInfoResponse>() {
            @Override
            protected void onResponse(RequestBase request, ModifyUserInfoResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    UserInfo.Info userInfo = UserInfo.getInstance().getInfo();
                    userInfo.setStage(Integer.valueOf(stageId));
                    userInfo.setSubject(Integer.valueOf(subjectId));
                    SpManager.saveUserInfo(userInfo);
                    ToastUtil.showToast(getApplicationContext(), "学段学科保存成功");
                    Intent intent = new Intent();
                    intent.putExtra("stageSubjectName", mData.getName()+mData.getSub().get(mSelectedPosition).getName());
                    setResult(RESULT_OK,intent);
                    ModifyUserSubjectActivity.this.finish();

                } else {
                    ToastUtil.showToast(getApplicationContext(), "学段学科保存失败");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), "学段学科保存失败");
            }
        });

    }
}
