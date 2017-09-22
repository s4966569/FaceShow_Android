package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.MultiUploadBean;
import com.yanxiu.gphone.faceshow.classcircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.http.request.UpLoadRequest;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/19 12:08.
 * Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener, TextWatcher {

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
    private String mResourceIds="";

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
        UpLoadRequest.getInstense().cancle();
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
        mContentView.addTextChangedListener(this);
    }

    private void initData() {
        mTitleView.setText(R.string.classcircle);
        mBackView.setText(R.string.cancle);
        mFunctionView.setText(R.string.send);
        mFunctionView.setEnabled(false);
        mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));

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
            case R.id.title_layout_right_txt:
                String content=mContentView.getText().toString();
                rootView.showLoadingView();
                if (mType.equals(TYPE_IMAGE)){
                    uploadImg(content);
                }else {
                    uploadData(content,mResourceIds);
                }
                break;
        }
    }

    private void uploadImg(final String content){
        UpLoadRequest.getInstense().setConstantParams(new UpLoadRequest.findConstantParams() {
            @NonNull
            @Override
            public String findUpdataUrl() {
                String url="/multiUpload";
                String token="ce0d56d0d8a214fb157be3850476ecb5";
                return UrlRepository.getInstance().getUploadServer()+url+"?token="+token;
            }

            @Override
            public int findFileNumber() {
                return mImagePaths.size();
            }

            @Nullable
            @Override
            public Map<String, String> findParams() {
                return null;
            }
        }).setImgPath(new UpLoadRequest.findImgPath() {
            @NonNull
            @Override
            public String getImgPath(int position) {
                return mImagePaths.get(0);
            }
        }).setListener(new UpLoadRequest.onUpLoadlistener() {
            @Override
            public void onUpLoadStart(int position, Object tag) {
            }

            @Override
            public void onUpLoadSuccess(int position, Object tag, String jsonString) {
                Gson gson=new Gson();
                MultiUploadBean uploadBean=gson.fromJson(jsonString,MultiUploadBean.class);
                if (uploadBean!=null){
                    mResourceIds=uploadBean.tplData.data.get(0).uniqueKey;
                    mType=TYPE_TEXT;
                    uploadData(content,uploadBean.tplData.data.get(0).uniqueKey);
                }else {
                    ToastUtil.showToast(mContext,"网络异常请稍后再试");
                }
            }

            @Override
            public void onUpLoadFailed(int position, Object tag, String failMsg) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,failMsg);
            }

            @Override
            public void onError(String errorMsg) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,errorMsg);
            }
        });
    }

    private void uploadData(String content,String resourceIds){
        SendClassCircleRequest sendClassCircleRequest=new SendClassCircleRequest();
        sendClassCircleRequest.clazsId="7";
        sendClassCircleRequest.content=content;
        sendClassCircleRequest.resourceIds=resourceIds;
        mSendDataRequest=sendClassCircleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,R.string.send_success);
                mSendDataRequest=null;
                EventBus.getDefault().post(new RefreshClassCircle());
                SendClassCircleActivity.this.finish();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mSendDataRequest=null;
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()==0){
            mFunctionView.setEnabled(false);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
        }else {
            mFunctionView.setEnabled(true);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext,R.color.color_1da1f2));
        }
    }
}
