package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.request.GetResIdRequest;
import com.yanxiu.gphone.faceshow.classcircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.GetResIdResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.MultiUploadBean;
import com.yanxiu.gphone.faceshow.classcircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshow.classcircle.response.UploadResResponse;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.UploadFileByHttp;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.http.request.UpLoadRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.FileUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTitleView.getWindowToken(), 0);
    }

    private void uploadImg(final String content){

        File file=new File(mImagePaths.get(0));
        final Map<String, String> map = new HashMap<>();
        map.put("userId", UserInfo.getInstance().getInfo().getUserId()+"");
        map.put("name", file.getName());
        map.put("lastModifiedDate", String.valueOf(System.currentTimeMillis()));
        map.put("size", String.valueOf(FileUtils.getFileSize(file.getPath())));
        map.put("md5", FileUtil.MD5Helper(UserInfo.getInstance().getInfo().getUserId() +
                file.getName() + "jpg" + String.valueOf(System.currentTimeMillis())
                + String.valueOf(FileUtils.getFileSize(file.getPath()))));
        map.put("type", "image/jpg");
        map.put("chunkSize", String.valueOf(FileUtils.getFileSize(file.getPath())));

        UploadFileByHttp uploadFileByHttp = new UploadFileByHttp();
        try {
            uploadFileByHttp.uploadForm(map, "file", file, file.getName(), "http://newupload.yanxiu.com/fileUpload", new UploadFileByHttp.UpLoadFileByHttpCallBack() {
                @Override
                public void onSuccess(String responseStr) {
                    rootView.hiddenLoadingView();
                    Log.d("cwq",responseStr);
                    UploadResResponse resResponse = RequestBase.getGson().fromJson(responseStr, UploadResResponse.class);
                    GetResId(resResponse.name, resResponse.md5,content);
                }

                @Override
                public void onFail(String errorMessage) {
                    rootView.hiddenLoadingView();
                    Log.d("cwq",errorMessage);
//                    loadingView.dismiss();
//                    ToastMaster.showToast(errorMessage);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




//        UpLoadRequest.getInstense().setConstantParams(new UpLoadRequest.findConstantParams() {
//            @NonNull
//            @Override
//            public String findUpdataUrl() {
//                String url="http://newupload.yanxiu.com/fileUpload";
//                String token= SpManager.getToken();
//                File file=new File(mImagePaths.get(0));
//                String parmas="";
//                parmas=parmas+"token="+token;
//                parmas=parmas+"&+"+"userId="+UserInfo.getInstance().getInfo().getUserId();
//                parmas=parmas+"&+"+"name="+file.getName();
//                parmas=parmas+"&+"+"lastModifiedDate="+String.valueOf(System.currentTimeMillis());
//                parmas=parmas+"&+"+"size="+String.valueOf(file.length());
//                parmas=parmas+"&+"+"md5="+ FileUtil.MD5Helper(UserInfo.getInstance().getInfo().getUserId() +
//                        file.getName() + "jpg" + String.valueOf(System.currentTimeMillis())
//                        + String.valueOf(file.length()));
//                parmas=parmas+"&+"+"type="+"image/jpg";
//                parmas=parmas+"&+"+"chunkSize="+String.valueOf(file.length());
//                return url+"?"+parmas;
//            }
//
//            @Override
//            public int findFileNumber() {
//                return mImagePaths.size();
//            }
//
//            @Nullable
//            @Override
//            public Map<String, String> findParams() {
//                return null;
//            }
//        }).setImgPath(new UpLoadRequest.findImgPath() {
//            @NonNull
//            @Override
//            public String getImgPath(int position) {
//                return mImagePaths.get(0);
//            }
//        }).setListener(new UpLoadRequest.onUpLoadlistener() {
//            @Override
//            public void onUpLoadStart(int position, Object tag) {
//            }
//
//            @Override
//            public void onUpLoadSuccess(int position, Object tag, String jsonString) {
//                Gson gson=new Gson();
//                MultiUploadBean uploadBean=gson.fromJson(jsonString,MultiUploadBean.class);
//                if (uploadBean!=null){
//                    mResourceIds=uploadBean.tplData.data.get(0).uniqueKey;
//                    mType=TYPE_TEXT;
//                    uploadData(content,uploadBean.tplData.data.get(0).uniqueKey);
//                }else {
//                    ToastUtil.showToast(mContext,"网络异常请稍后再试");
//                }
//            }
//
//            @Override
//            public void onUpLoadFailed(int position, Object tag, String failMsg) {
//                rootView.hiddenLoadingView();
//                ToastUtil.showToast(mContext,failMsg);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                rootView.hiddenLoadingView();
//                ToastUtil.showToast(mContext,errorMsg);
//            }
//        });
    }

    private void GetResId(String fileName, String md5, final String content) {
        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("client_type", "app");
//        cookies.put("passport", UserStore.sharedInstance().getUserModel().phoneNumber);
        GetResIdRequest getResIdRequest = new GetResIdRequest();
        getResIdRequest.filename = fileName;
        getResIdRequest.md5 = md5;
        getResIdRequest.cookies = cookies;
        GetResIdRequest.Reserve reserve = new GetResIdRequest.Reserve();
        getResIdRequest.reserve = RequestBase.getGson().toJson(reserve);
        getResIdRequest.startRequest(GetResIdResponse.class, new HttpCallback<GetResIdResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetResIdResponse ret) {
                Log.d("cwq",ret.result.resid);
                uploadData(content,ret.result.resid);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext,error.getMessage());
            }
        });

    }



    private void uploadData(String content,String resourceIds){
        SendClassCircleRequest sendClassCircleRequest=new SendClassCircleRequest();
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
