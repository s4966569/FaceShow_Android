package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshow.classcircle.request.GetResIdRequest;
import com.yanxiu.gphone.faceshow.classcircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.GetResIdResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.RefreshClassCircle;
import com.yanxiu.gphone.faceshow.classcircle.response.UploadResResponse;
import com.yanxiu.gphone.faceshow.common.activity.PhotoActivity;
import com.yanxiu.gphone.faceshow.common.bean.PhotoDeleteBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.UploadFileByHttp;
import com.yanxiu.gphone.faceshow.http.request.UpLoadRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.user.ModifyUserNameActivity;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.FileUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/19 12:08.
 * Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener, TextWatcher {

    private static final int REQUEST_CODE_ALBUM = 0x000;
    private static final int REQUEST_CODE_CAMERA = 0x001;

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_IMAGE = "key_image";

    private Context mContext;
    private PublicLoadLayout rootView;
    private String mType;
    private ArrayList<String> mImagePaths;
    private EditText mContentView;
    private LinearLayout mShowPictureView;
    private ImageView mPictureView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private TextView mBackView;
    private ImageView mDeleteView;
    private String mResourceIds = "";
    private String mCameraPath;
    private InputMethodManager imm;
    private ClassCircleDialog mClassCircleDialog;
    private PopupWindow mCancelPopupWindow;
    private UUID mSendDataRequest;

    public static void LuanchActivity(Context context, String type, ArrayList<String> imgPaths) {
        Intent intent = new Intent(context, SendClassCircleActivity.class);
        intent.putExtra(KEY_TYPE, type);
        if (imgPaths != null && imgPaths.size() > 0) {
            intent.putStringArrayListExtra(KEY_IMAGE, imgPaths);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SendClassCircleActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_send_classcircle);
        setContentView(rootView);
        EventBus.getDefault().register(mContext);
        mType = getIntent().getStringExtra(KEY_TYPE);
        if (mType.equals(TYPE_IMAGE)) {
            mImagePaths = getIntent().getStringArrayListExtra(KEY_IMAGE);
        }
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpLoadRequest.getInstense().cancle();
        EventBus.getDefault().unregister(mContext);
        if (mSendDataRequest != null) {
            RequestBase.cancelRequestWithUUID(mSendDataRequest);
            mSendDataRequest = null;
        }
    }

    private void initView() {
        mBackView = (TextView) findViewById(R.id.title_layout_left_txt);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView = (TextView) findViewById(R.id.title_layout_title);
        mFunctionView = (TextView) findViewById(R.id.title_layout_right_txt);
        mFunctionView.setVisibility(View.VISIBLE);

        mContentView = (EditText) findViewById(R.id.et_content);
        mPictureView = (ImageView) findViewById(R.id.iv_picture);
        mShowPictureView = (LinearLayout) findViewById(R.id.ll_picture);
        mDeleteView = (ImageView) findViewById(R.id.iv_delete);
    }

    private void listener() {
        mBackView.setOnClickListener(this);
        mFunctionView.setOnClickListener(this);
        mContentView.addTextChangedListener(this);
        mDeleteView.setOnClickListener(this);
        mPictureView.setOnClickListener(this);
    }

    private void initData() {
        mTitleView.setText(R.string.classcircle);
        mBackView.setText(R.string.cancle);
        mFunctionView.setText(R.string.send);
        mFunctionView.setEnabled(false);
        mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));

        if (mType.equals(TYPE_TEXT)) {
            mShowPictureView.setVisibility(View.GONE);
        } else {
            mShowPictureView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mImagePaths.get(0)).centerCrop().into(mPictureView);
        }
        mContentView.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_left_txt:
                exitDialog();
                break;
            case R.id.title_layout_right_txt:
                String content = mContentView.getText().toString();

                if (mType.equals(TYPE_IMAGE)) {
                    rootView.showLoadingView();
                    uploadImg(content);
                } else {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToast(getApplicationContext(), "请输入要发布的内容");
                    } else {
                        rootView.showLoadingView();
                        uploadData(content, mResourceIds);
                    }
                }
                break;
            case R.id.iv_delete:
                if (mImagePaths != null && !mImagePaths.isEmpty()) {
                    mImagePaths.clear();
                    mType = TYPE_TEXT;
                    mDeleteView.setVisibility(View.GONE);
                    Glide.with(mContext).load(R.drawable.class_circle_add_picture).into(mPictureView);
                }
                break;
            case R.id.iv_picture:
                if (mImagePaths != null && !mImagePaths.isEmpty()) {
                    PhotoActivity.LaunchActivity(mContext, mImagePaths, 0, mContext.hashCode(), PhotoActivity.DELETE_CAN);
                } else {
                    showDialog();
                }
                break;
            default:
                break;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTitleView.getWindowToken(), 0);
    }

    private void showDialog() {
        if (mClassCircleDialog == null) {
            mClassCircleDialog = new ClassCircleDialog(mContext);
            mClassCircleDialog.setClickListener(new ClassCircleDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    FaceShowBaseActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(mContext, R.string.no_storage_permissions);
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    FaceShowBaseActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            mCameraPath = FileUtil.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            File file = new File(mCameraPath);
                            if (file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(mContext, R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    public void onEventMainThread(PhotoDeleteBean bean) {
        if (bean != null && bean.formId == mContext.hashCode()) {
            mImagePaths.remove(bean.deleteId);
            mType = TYPE_TEXT;
            mDeleteView.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.class_circle_add_picture).fitCenter().into(mPictureView);
        }
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(uri,mCropPath);
                    String path = FileUtil.getRealFilePath(mContext, uri);
                    mImagePaths.add(path);
                    mType = TYPE_IMAGE;
                    Glide.with(mContext).load(mImagePaths.get(0)).centerCrop().into(mPictureView);
                    mDeleteView.setVisibility(View.VISIBLE);
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (!TextUtils.isEmpty(mCameraPath)) {
//                    mCropPath=FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
//                    startCropImg(Uri.fromFile(new File(mCameraPath)),mCropPath);
                    try {
                        new FileInputStream(new File(mCameraPath));
                        mImagePaths.add(mCameraPath);
                        mType = TYPE_IMAGE;
                        Glide.with(mContext).load(mImagePaths.get(0)).centerCrop().into(mPictureView);
                        mDeleteView.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void uploadImg(final String content) {
        File file = new File(mImagePaths.get(0));
        final Map<String, String> map = new HashMap<>();
        map.put("userId", UserInfo.getInstance().getInfo().getUserId() + "");
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
                    UploadResResponse resResponse = RequestBase.getGson().fromJson(responseStr, UploadResResponse.class);
                    GetResId(resResponse.name, resResponse.md5, content);
                }

                @Override
                public void onFail(String errorMessage) {
                    ToastUtil.showToast(mContext, getString(R.string.send_class_circle_fail));
                    rootView.hiddenLoadingView();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GetResId(String fileName, String md5, final String content) {
        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("client_type", "app");
        cookies.put("passport", SpManager.getPassport());
        GetResIdRequest getResIdRequest = new GetResIdRequest();
        getResIdRequest.filename = fileName;
        getResIdRequest.md5 = md5;
        getResIdRequest.cookies = cookies;
        GetResIdRequest.Reserve reserve = new GetResIdRequest.Reserve();
        reserve.title = fileName;
        getResIdRequest.reserve = RequestBase.getGson().toJson(reserve);
        getResIdRequest.startRequest(GetResIdResponse.class, new HttpCallback<GetResIdResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetResIdResponse ret) {
                mResourceIds = ret.result.resid;
                mType = TYPE_TEXT;
                uploadData(content, ret.result.resid);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, error.getMessage());
            }
        });

    }

    private void uploadData(String content, String resourceIds) {
        SendClassCircleRequest sendClassCircleRequest = new SendClassCircleRequest();
        sendClassCircleRequest.content = content;
        sendClassCircleRequest.resourceIds = resourceIds;
        mSendDataRequest = sendClassCircleRequest.startRequest(ClassCircleResponse.class, new HttpCallback<ClassCircleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ClassCircleResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.data != null) {
                    ToastUtil.showToast(mContext, R.string.send_success);
                    mSendDataRequest = null;
                    EventBus.getDefault().post(new RefreshClassCircle());
                    SendClassCircleActivity.this.finish();
                } else {
                    ToastUtil.showToast(mContext, ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                mSendDataRequest = null;
                ToastUtil.showToast(mContext, error.getMessage());
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
        if (TextUtils.isEmpty(s) && (mImagePaths == null || mImagePaths.size() == 0)) {
            mFunctionView.setEnabled(false);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
        } else {
            mFunctionView.setEnabled(true);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_1da1f2));
        }
    }


    private void exitDialog() {
        if (imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mBackView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        showCancelPopupWindow(this);
    }

    private void showCancelPopupWindow(Activity context) {
        if (mCancelPopupWindow == null) {
            View pop = LayoutInflater.from(context).inflate(R.layout.pop_ask_cancel_layout, null);
            (pop.findViewById(R.id.tv_pop_sure)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.tv_cancel)).setOnClickListener(popupWindowClickListener);
            mCancelPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mCancelPopupWindow.setAnimationStyle(R.style.pop_anim);
            mCancelPopupWindow.setFocusable(true);
            mCancelPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mCancelPopupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow() {
        if (mCancelPopupWindow != null) {
            mCancelPopupWindow.dismiss();
        }
    }

    View.OnClickListener popupWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_sure:
                    dismissPopupWindow();
                    SendClassCircleActivity.this.finish();
                    break;
                case R.id.tv_cancel:
                    dismissPopupWindow();
                    break;
                default:
                    break;

            }

        }
    };
}
