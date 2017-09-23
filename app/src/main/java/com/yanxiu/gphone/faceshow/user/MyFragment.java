package com.yanxiu.gphone.faceshow.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInNotesActivity;
import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.CornersImageTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 首页 “我的”Fragment
 */
public class MyFragment extends FaceShowBaseFragment {
    private final static String TAG = MyFragment.class.getSimpleName();
    private Unbinder unbinder;
    private PublicLoadLayout rootView;
    @BindView(R.id.person_img)
    ImageView mHeadImgView;
    @BindView(R.id.tv_name)
    TextView mNameView;
    @BindView(R.id.title_layout_title)
    TextView mTitleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData(){
        mTitleView.setText(R.string.my);
        mNameView.setText(UserInfo.getInstance().getInfo().getUserName());
        Glide.with(getContext()).load(UserInfo.getInstance().getInfo().getHeadImg()).asBitmap().into(new CornersImageTarget(getContext(),mHeadImgView,12));
    }

    @OnClick({R.id.person_info, R.id.registration, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_info://点击个人信息
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.registration://点击签到记录
                CheckInNotesActivity.toThisAct(getActivity());
                break;
            case R.id.ll_logout://退出登录
                LoginActivity.toThisAct(getActivity());
                UserInfo.getInstance().setInfo(null);
                SpManager.saveToken("");
                SpManager.loginOut();//设置为登出状态
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
