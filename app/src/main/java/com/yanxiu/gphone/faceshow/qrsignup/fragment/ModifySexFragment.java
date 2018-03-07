package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ModifySexFragment extends Fragment implements View.OnClickListener {


    public ModifySexFragment() {
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
            root = inflater.inflate(R.layout.activity_modify_user_sex, null);
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
        titleTxt.setText("设置性别");
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
        root.findViewById(R.id.rl_boy).setOnClickListener(this);
        root.findViewById(R.id.rl_girl).setOnClickListener(this);
    }

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_boy:
                root.findViewById(R.id.boy_click).setVisibility(View.VISIBLE);
                root.findViewById(R.id.girl_click).setVisibility(View.INVISIBLE);
                userBean.setSex(0);
                userBean.setSexName("男");
                break;
            case R.id.rl_girl:
                root.findViewById(R.id.boy_click).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.girl_click).setVisibility(View.VISIBLE);
                userBean.setSex(1);
                userBean.setSexName("女");
                break;
            default:
                break;
        }
    }
}
