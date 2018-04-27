package com.yanxiu.gphone.faceshow.user;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.emoj.EmojiFilter;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseCallback;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.user.request.ModifyUserInfoRequest;
import com.yanxiu.gphone.faceshow.user.response.ModifyUserInfoResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

public class ModifyUserSchoolActivity extends FaceShowBaseActivity implements View.OnClickListener{

    ImageView titleLayoutLeftImg;

    TextView titleLayoutTitle;

    TextView titleLayoutRightTxt;

    EditText edtSchoolName;
    private PopupWindow mCancelPopupWindow;
    private InputMethodManager imm;
    private PublicLoadLayout publicLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(ModifyUserSchoolActivity.this);
        publicLoadLayout.setContentView(R.layout.activity_modify_user_name);
        setContentView(publicLoadLayout);

        titleLayoutLeftImg=findViewById(R.id.title_layout_left_img);
        titleLayoutTitle=findViewById(R.id.title_layout_title);
        titleLayoutRightTxt=findViewById(R.id.title_layout_right_txt);
        edtSchoolName=findViewById(R.id.edt_name);

        titleLayoutRightTxt.setOnClickListener(this);
        titleLayoutLeftImg.setOnClickListener(this);

        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText("修改学校名称");
        titleLayoutRightTxt.setText(R.string.save);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        edtSchoolName.setFilters(new InputFilter[]{new EmojiFilter()});
        edtSchoolName.setText(SpManager.getUserInfo().getSchool());
        if (TextUtils.isEmpty(SpManager.getUserInfo().getSchool())) {
            edtSchoolName.setHint("学校名称");
        }
    }


   @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                saveName(edtSchoolName.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 保存学校名
     *
     * @param schoolName 学校名
     */
    private void saveName(final String schoolName) {
        if (TextUtils.isEmpty(schoolName)) {
            ToastUtil.showToast(getApplicationContext(), "学校名称不能为空");
            return;
        }
        publicLoadLayout.showLoadingView();
        ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
        modifyUserInfoRequest.school = schoolName;
        modifyUserInfoRequest.startRequest(ModifyUserInfoResponse.class, new FaceShowBaseCallback<ModifyUserInfoResponse>() {
            @Override
            protected void onResponse(RequestBase request, ModifyUserInfoResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    UserInfo.Info userInfo = UserInfo.getInstance().getInfo();
                    userInfo.setSchool(schoolName);
                    SpManager.saveUserInfo(userInfo);
                    ToastUtil.showToast(getApplicationContext(), "学校保存成功");
                    ModifyUserSchoolActivity.this.setResult(RESULT_OK);
                    ModifyUserSchoolActivity.this.finish();
                } else {
                    ToastUtil.showToast(getApplicationContext(), response.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), "学校保存失败");
            }
        });

    }

}
