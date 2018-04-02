package com.yanxiu.gphone.faceshow.homepage.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.adapter.ClassManagerListAdapter;
import com.yanxiu.gphone.faceshow.http.course.GetStudentClazsesResponse;
import com.yanxiu.gphone.faceshow.http.course.GetSudentClazsesRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc 选择班级页
 */
public class ChooseClassActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView mTitleLayoutRightTxt;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_layout_left_img)
    ImageView mLeftImage;
    private ClassManagerListAdapter mClassManagerListAdapter;
    private PublicLoadLayout mRootView;
    private UUID mUUID;
    private int mSelcetPosition = 0;
    private List<GetStudentClazsesResponse.ClazsInfosBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_choose_class);
        setContentView(mRootView);
        ButterKnife.bind(this);
        initTitle();
        initRecyclerView();
        getClassListData();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.hiddenNetErrorView();
                mRootView.hiddenOtherErrorView();
                getClassListData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mClassManagerListAdapter = new ClassManagerListAdapter();
        mRecyclerView.setAdapter(mClassManagerListAdapter);
        mClassManagerListAdapter.setIRecyclerViewItemClick(new IRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int postion) {
                mSelcetPosition = postion;
            }
        });
    }

    private void initTitle() {
        mLeftImage.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText("选择班级");
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        mTitleLayoutRightTxt.setText(R.string.sure);
        mTitleLayoutRightTxt.setVisibility(View.VISIBLE);
    }

    private void getClassListData() {
        mRootView.showLoadingView();
        GetSudentClazsesRequest getSudentClazsesRequest = new GetSudentClazsesRequest();
        mUUID = getSudentClazsesRequest.startRequest(GetStudentClazsesResponse.class, new HttpCallback<GetStudentClazsesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetStudentClazsesResponse ret) {
                mRootView.finish();
                mUUID = null;
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().getClazsInfos() != null && ret.getData().getClazsInfos().size() > 0) {
                        if (data != null) {
                            data.clear();
                            data = ret.getData().getClazsInfos();
                        }
                        for (int i = 0; i < data.size(); i++) {
                            if (SpManager.getUserInfo().getClassId().equals(String.valueOf(data.get(i).getId()))) {
                                mSelcetPosition = i;
                            }
                        }
                        mClassManagerListAdapter.update(ret.getData().getClazsInfos(), mSelcetPosition);
                    } else {
                        mRootView.showOtherErrorView("暂无班级");
                    }
                } else {
                    mRootView.showOtherErrorView(ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
                mUUID = null;
                mRootView.showNetErrorView();
//                mRootView.showOtherErrorView(error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUUID != null) {
            RequestBase.cancelRequestWithUUID(mUUID);
        }
    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                if (data != null&&data.size()>0) {
                    UserInfo.Info info = SpManager.getUserInfo();
                    info.setClassId(String.valueOf(data.get(mSelcetPosition).getId()));
                    info.setClassName(data.get(mSelcetPosition).getClazsName());
                    info.setProjectName(data.get(mSelcetPosition).getProjectName());
                    SpManager.saveUserInfo(info);
                    this.setResult(RESULT_OK);
                    this.finish();
                }else {
                    this.setResult(RESULT_CANCELED);
                    this.finish();
                }
                break;
            default:
        }
    }
}
