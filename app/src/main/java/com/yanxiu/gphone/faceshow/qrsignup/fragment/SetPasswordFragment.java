package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;

/**
 * A simple {@link Fragment} subclass.
 * 设置用户密码界面
 * 当前 为 扫描注册的流程中一步
 * 后期可以复用
 */
public class SetPasswordFragment extends FaceShowBaseFragment {
    private View rootView;
    private ImageView titleLeftImage;
    private ImageView titleRightImage;
    private TextView titleTextView;

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    /**
     * 对当前界面进行toolbar 设置
     * */
    private void toolbarInit(View root){
        titleLeftImage= (ImageView) root.findViewById(R.id.title_layout_left_img);
        titleRightImage= (ImageView) root.findViewById(R.id.title_layout_right_img);
        titleTextView= (TextView) root.findViewById(R.id.title_layout_title);

        titleRightImage.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleTextView.setText("设置密码");
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });
        titleRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }
            }
        });
    }

}
