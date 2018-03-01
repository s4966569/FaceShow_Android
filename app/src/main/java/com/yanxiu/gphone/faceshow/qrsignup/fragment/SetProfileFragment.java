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
 * 完善用户信息界面
 */
public class SetProfileFragment extends FaceShowBaseFragment {

    private View rootView;
    private ImageView titleLeftImage;
    private ImageView titleRightImage;
    private TextView titleTextView;

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {

        }
        return rootView;
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
        titleTextView.setText("完善资料");
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
