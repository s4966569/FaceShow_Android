package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
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
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.FileUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.imagePicker.GlideImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * @author frc
 *         Time : 2018/1/12
 *         Function :
 */
public class SendClassCircleActivity extends FaceShowBaseActivity implements View.OnClickListener, TextWatcher {


    private static final int IMAGE_PICKER = 0x03;
    private static final int REQUEST_CODE_SELECT = 0x04;

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_IMAGE = "key_image";

    private Context mContext;
    private PublicLoadLayout rootView;
    private String mType;
    private ArrayList<String> mImagePaths;
    private EditText mContentView;
    private TextView mTitleView;
    private TextView mFunctionView;
    private TextView mBackView;
    private String mResourceIds = "";
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
        setImagePicker();
    }

    private void setImagePicker() {
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(glideImageLoader);
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(true);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(true);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
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
        mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));

        if (mType.equals(TYPE_TEXT)) {
//            mShowPictureView.setVisibility(View.GONE);
        } else {
//            mShowPictureView.setVisibility(View.VISIBLE);
//            if (mImagePaths != null) {
//                Glide.with(mContext).load(mImagePaths.get(0)).centerCrop().into(mPictureView);
//            } else {
//                mDeleteView.setVisibility(View.GONE);
//                Glide.with(mContext).load(R.drawable.class_circle_add_picture).into(mPictureView);
//            }
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
            default:
                break;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mTitleView.getWindowToken(), 0);
        }
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
                            Intent intent = new Intent(SendClassCircleActivity.this, ImageGridActivity.class);
                            startActivityForResult(intent, IMAGE_PICKER);
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

                            Intent intent = new Intent(SendClassCircleActivity.this, ImageGridActivity.class);
                            // 是否是直接打开相机
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                            startActivityForResult(intent, REQUEST_CODE_SELECT);
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

    /**
     * 删除图片回调
     *
     * @param bean
     */
    public void onEventMainThread(PhotoDeleteBean bean) {

        if (bean != null && bean.formId == mContext.hashCode()) {
            mImagePaths.remove(bean.deleteId);
            mType = TYPE_TEXT;
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
            case IMAGE_PICKER:
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e("imagPicker", "image count:  " + images.size());

            case REQUEST_CODE_SELECT:
                ArrayList<ImageItem> sel = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.e("imagPicker", "image count:  " + sel.size());
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
