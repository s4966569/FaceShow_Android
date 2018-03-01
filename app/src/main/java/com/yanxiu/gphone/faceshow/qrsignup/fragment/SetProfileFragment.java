package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.recycleradapter.ProfileRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * 完善用户信息界面
 */
public class SetProfileFragment extends FaceShowBaseFragment {

    private View fragmentRootView;
    private PublicLoadLayout mRootView;
    private ImageView titleLeftImage;
    private TextView titleRightText;
    private TextView titleTextView;
    private ToolbarActionCallback toolbarActionCallback;


    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private RecyclerView profileRecyclerView;



    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView=new PublicLoadLayout(getActivity());
//            mRootView.setContentView(R.layout.fragment_checkphone_layout);
            fragmentRootView=inflater.inflate(R.layout.fragment_setprofile_layout,null);
            mRootView.setContentView(fragmentRootView);
        }
        profileRecyclerAdapter=new ProfileRecyclerAdapter(getActivity());
        toolbarInit(fragmentRootView.findViewById(R.id.setprofile_titelbar));
        viewInit(fragmentRootView);
        return mRootView;
    }

    private void viewInit(View root){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        profileRecyclerView= (RecyclerView) root.findViewById(R.id.profile_recyclerview);
        profileRecyclerView.setLayoutManager(layoutManager);
        profileRecyclerView.setAdapter(profileRecyclerAdapter);
    }

    /**
     * 对当前界面进行toolbar 设置
     * */
    private void toolbarInit(View root){
        titleLeftImage= (ImageView) root.findViewById(R.id.title_layout_left_img);
        titleRightText= (TextView) root.findViewById(R.id.title_layout_right_txt);
        titleTextView= (TextView) root.findViewById(R.id.title_layout_title);

        titleRightText.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleTextView.setText("设置密码");
        titleRightText.setText("保存");
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });
        titleRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }
            }
        });
    }

}
