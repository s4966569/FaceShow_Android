package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
public class ModifyNameFragment extends Fragment {
    private final String TAG=getClass().getSimpleName();

    public ModifyNameFragment() {
        // Required empty public constructor
    }

    private SysUserBean userBean;
    private PublicLoadLayout publicLoadLayout;

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
        backView.setVisibility(View.VISIBLE);
        TextView titleTxt = root.findViewById(R.id.title_layout_title);
        TextView rightTxt = root.findViewById(R.id.title_layout_right_txt);
        rightTxt.setVisibility(View.VISIBLE);

        final EditText editText = root.findViewById(R.id.edt_name);

        rightTxt.setText("保存");
        titleTxt.setText("编辑姓名");
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/3/13 收起软键盘
                if (toolbarActionCallback != null) {
                    hideSoftInput(editText);
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });

        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*点击保存 首先判断 gettext是否为空 为空说明没有编辑 采用hint 进行保存 */
                if (TextUtils.isEmpty(editText.getText())) {
                    if(TextUtils.isEmpty(editText.getHint())){
                        /*如果text 与 hint都为空 保存空*/
                        saveName("");
                    }else {
                        /*如果text 为空 hint 不为空 保存 hint*/
                        saveName(editText.getHint().toString());
                    }
                }else {
                    /*text 不为空 保存 text*/
                    saveName(editText.getText().toString());
                }


//                String nameStr=TextUtils.isEmpty(editText.getText().toString())
//                        ?editText.getHint().toString():editText.getText().toString();
//                saveName(nameStr);
                if (toolbarActionCallback != null) {
                    hideSoftInput(editText);
                    toolbarActionCallback.onRightComponentClick();
                }
            }
        });
        editText.setHint(userBean.getRealName());
    }
    private void hideSoftInput(EditText editText) {
        InputMethodManager inputMethodManager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    private void saveName(String name) {
        userBean.setRealName(name);
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
