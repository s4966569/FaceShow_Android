package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/19 12:08.
 * Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener {

    public static final String TYPE_TEXT="text";
    public static final String TYPE_IMAGE="image";

    private static final String KEY_TYPE="key_type";
    private static final String KEY_IMAGE="key_image";

    private Context mContext;
    private PublicLoadLayout rootView;
    private String mType;
    private ArrayList<String> mImagePaths;
    private EditText mContentView;
    private ImageView mPictureView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private TextView mBackView;

    public static void LuanchActivity(Context context, String type, ArrayList<String> imgPaths){
        Intent intent=new Intent(context,SendClassCircleActivity.class);
        intent.putExtra(KEY_TYPE,type);
        if (imgPaths!=null&&imgPaths.size()>0){
            intent.putStringArrayListExtra(KEY_IMAGE,imgPaths);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=SendClassCircleActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_send_classcircle);
        setContentView(rootView);
        mType=getIntent().getStringExtra(KEY_TYPE);
        if (mType.equals(TYPE_IMAGE)){
            mImagePaths=getIntent().getStringArrayListExtra(KEY_IMAGE);
        }
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.title_layout_left_txt);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = (TextView) findViewById(R.id.title_layout_title);
        mFunctionView = (TextView) findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);

        mContentView= (EditText) findViewById(R.id.et_content);
        mPictureView= (ImageView) findViewById(R.id.iv_picture);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mFunctionView.setOnClickListener(this);
    }

    private void initData() {
        mTitleView.setText(R.string.classcircle);
        mBackView.setText(R.string.cancle);
        mFunctionView.setText(R.string.send);

        if (mType.equals(TYPE_TEXT)){
            mPictureView.setVisibility(View.GONE);
        }else {
            mPictureView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mImagePaths.get(0)).into(mPictureView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_layout_left_txt:
                this.finish();
                break;
            case R.id.title_layout_signIn:
                if (mType.equals(TYPE_IMAGE)){

                }
                break;
        }
    }


}
