package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyPhoneFragment extends Fragment {


    private PublicLoadLayout publicLoadLayout;

    public ModifyPhoneFragment() {
        // Required empty public constructor
    }

    private SysUserBean userBean;

    public void setUserBean(SysUserBean userBean) {
        this.userBean = userBean;
    }

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.activity_modify_user_name, null);
            publicLoadLayout = new PublicLoadLayout(getActivity());
            publicLoadLayout.setContentView(root);
        }
        viewInit(root);
        return publicLoadLayout;
    }

    private void viewInit(View root) {
        ImageView backView = root.findViewById(R.id.title_layout_left_img);
        TextView titleTxt = root.findViewById(R.id.title_layout_title);
        TextView rightTxt = root.findViewById(R.id.title_layout_right_txt);
        final EditText editText = root.findViewById(R.id.edt_name);
        backView.setVisibility(View.VISIBLE);
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("保存");
        titleTxt.setText("编辑电话");
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

                 /*点击保存 首先判断 gettext是否为空 为空说明没有编辑 采用hint 进行保存 */
                String phoneStr = TextUtils.isEmpty(editText.getText().toString())
                        ? editText.getHint().toString() : editText.getText().toString();


                userBean.setMobilePhone(phoneStr);
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }

            }
        });

        editText.setHint(userBean.getMobilePhone());

    }

    private ModifySysInfoCallback modifySysInfoCallback;

    public void setModifySysInfoCallback(ModifySysInfoCallback modifySysInfoCallback) {
        this.modifySysInfoCallback = modifySysInfoCallback;
    }

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }
}
