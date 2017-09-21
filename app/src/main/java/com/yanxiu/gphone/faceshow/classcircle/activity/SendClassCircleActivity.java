package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.UUID;

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

    private UUID mSendDataRequest;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendDataRequest!=null){
            RequestBase.cancelRequestWithUUID(mSendDataRequest);
            mSendDataRequest=null;
        }
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
                String content=mContentView.getText().toString();
                if (mType.equals(TYPE_IMAGE)){
                    uploadImg();
                }else {
                    uploadData(content,"");
                }
                break;
        }
    }

    private void uploadImg(){

    }

    private void uploadData(String content,String resourceIds){
        SendClassCircleRequest sendClassCircleRequest=new SendClassCircleRequest();
        sendClassCircleRequest.content=content;
        sendClassCircleRequest.resourceIds=resourceIds;
        mSendDataRequest=sendClassCircleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                ToastUtil.showToast(mContext,R.string.send_success);
                mSendDataRequest=null;
                SendClassCircleActivity.this.finish();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mSendDataRequest=null;
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }
}
