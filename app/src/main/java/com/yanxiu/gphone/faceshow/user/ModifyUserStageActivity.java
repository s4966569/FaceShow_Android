package com.yanxiu.gphone.faceshow.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc 选择学段
 */
public class ModifyUserStageActivity extends FaceShowBaseActivity {

    private static final int REQUEST_CODE_CHOOSE_SUBJECT = 1;

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.recyclerView_choose_stage)
    RecyclerView mRecyclerViewChooseStage;
    @BindView(R.id.title_layout_right_txt)
    TextView mTitleLayoutRightText;

    private StageSubjectModel mStageSubjectModel;
    private int mSelectedPosition = 0;
    private boolean canSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_stage_and_sbuject);
        ButterKnife.bind(this);
        initTitle();
        initRecyclerView();

    }

    private void initRecyclerView() {
        mStageSubjectModel = RequestBase.getGson().fromJson(FileUtil.getDataFromAssets(this, "stageSubject.json"), StageSubjectModel.class);
        ModifyUserSubjectAdapter modifyUserStageAdapter = new ModifyUserSubjectAdapter(mStageSubjectModel.getData(), mIRecyclerViewItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewChooseStage.setLayoutManager(linearLayoutManager);
        mRecyclerViewChooseStage.setAdapter(modifyUserStageAdapter);
    }

    private IRecyclerViewItemClick mIRecyclerViewItemClickListener = new IRecyclerViewItemClick() {
        @Override
        public void onItemClick(View view, int position) {
            mSelectedPosition = position;
            mTitleLayoutRightText.setTextColor(ContextCompat.getColor(ModifyUserStageActivity.this, R.color.color_1da1f2));
            canSelected = true;
        }
    };


    private void initTitle() {
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.choose_stage);
        mTitleLayoutRightText.setText("下一步");
        mTitleLayoutRightText.setTextColor(ContextCompat.getColor(ModifyUserStageActivity.this, R.color.color_999999));
        mTitleLayoutRightText.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                if (canSelected) {
                    Intent intent = new Intent(ModifyUserStageActivity.this, ModifyUserSubjectActivity.class);
                    intent.putExtra("data", mStageSubjectModel.getData().get(mSelectedPosition));
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_SUBJECT);
                }else {
                    ToastUtil.showToast(ModifyUserStageActivity.this,"请先选择学段");
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CHOOSE_SUBJECT == requestCode) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    this.setResult(RESULT_OK, data);
                    this.finish();
                }
            }
        }
    }


}
