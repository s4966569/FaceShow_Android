package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.user.MyFragment;

import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceHoldFragment extends FaceShowBaseFragment {
    private final static String TAG = MyFragment.class.getSimpleName();
    private Unbinder unbinder;
    private PublicLoadLayout rootView;
    private TextView mTitleView;


    private ImageView titleLeftImageView;
    public PlaceHoldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_hold, container, false);

        /*左侧 菜单按钮*/
        titleLeftImageView=view.findViewById(R.id.title_layout_left_img);
        titleLeftImageView.setVisibility(View.VISIBLE);
        titleLeftImageView.setImageResource(R.drawable.main_leftdrawe);
        mTitleView=view.findViewById(R.id.title_layout_title);
        mTitleView.setText("聊聊");

        titleLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).openLeftDrawer();
            }
        });
        return view;
    }

}
