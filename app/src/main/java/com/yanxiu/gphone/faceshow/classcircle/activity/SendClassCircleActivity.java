package com.yanxiu.gphone.faceshow.classcircle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.adapter.SelectedImageListAdapter;
import com.yanxiu.gphone.faceshow.classcircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshow.classcircle.request.GetQiNiuTokenRequest;
import com.yanxiu.gphone.faceshow.classcircle.request.GetResIdRequest;
import com.yanxiu.gphone.faceshow.classcircle.request.SendClassCircleRequest;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.classcircle.response.GetQiNiuTokenResponse;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private ImageItem mAddPicItem;

    private UUID mGetSendDataRequestUUID;

    /**
     * 用来展示已经选中的图片
     */
    private RecyclerView mImageSelectedRecyclerView;
    private SelectedImageListAdapter mSelectedImageListAdapter;
    private List<ImageItem> mSelectedImageList;

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
        } else {
            mType = TYPE_TEXT;
        }
        initView();
        listener();
        initData();
        setImagePicker();
    }

    ImagePicker imagePicker;

    private void setImagePicker() {
        GlideImageLoader glideImageLoader = new GlideImageLoader();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(glideImageLoader);
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //是否按矩形区域保存
//        imagePicker.setSaveRectangle(true);
        //选中数量限制
        imagePicker.setSelectLimit(9);
        //裁剪框的形状
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
//        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
//        imagePicker.setOutPutY(1000);
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
        initRecyclerView();

    }

    private void initRecyclerView() {
        mImageSelectedRecyclerView = (RecyclerView) findViewById(R.id.selected_images_recycler_view);
        if (mSelectedImageList == null) {
            mSelectedImageList = new ArrayList<>();
        }
        mSelectedImageList.clear();
        mSelectedImageList.add(mAddPicItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mImageSelectedRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectedImageListAdapter = new SelectedImageListAdapter((ArrayList<ImageItem>) mSelectedImageList);
        mImageSelectedRecyclerView.setAdapter(mSelectedImageListAdapter);
        mSelectedImageListAdapter.addPicClickListener(new SelectedImageListAdapter.PicClickListener() {
            @Override
            public void addPic() {
                if (mImagePaths != null && mImagePaths.size() >= 9) {
                    ToastUtil.showToast(getApplicationContext(), "一次最多上传9张图片");
                } else {
                    imagePicker.setSelectLimit(9 - (mImagePaths != null ? mImagePaths.size() : 0));
                    showDialog();
                }
            }

            @Override
            public void showBigPic(int position) {
                PhotoActivity.LaunchActivity(mContext, mImagePaths, position, mContext.hashCode(), PhotoActivity.DELETE_CAN);
            }
        });
        mSelectedImageListAdapter.setDataRemoveListener(new SelectedImageListAdapter.DataRemoveListener() {
            @Override
            public void remove(int pos) {
                mSelectedImageListAdapter.notifyItemRemoved(pos);
                mImagePaths.remove(pos);
                if (pos != mSelectedImageList.size()) {
                    mSelectedImageListAdapter.notifyItemRangeChanged(pos, mSelectedImageList.size() - pos);
                }
                if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
                    openPublishButton(false);
                } else {
                    openPublishButton(true);
                }
            }
        });
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
        mContentView.setText("");

        //生成添加图片item的数据
        mAddPicItem = new ImageItem();
        mAddPicItem.path = String.valueOf(ContextCompat.getDrawable(this, R.drawable.class_circle_add_picture));
        mAddPicItem.name = "添加图片";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_left_txt:
                onBackPressed();
                break;
            case R.id.title_layout_right_txt:
                String content = mContentView.getText().toString();
                if (mImagePaths != null && mImagePaths.size() > 0) {
                    rootView.showLoadingView();
                    getQiNiuToken();
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
            mSelectedImageList.remove(bean.deleteId);
            mSelectedImageListAdapter.update(mSelectedImageList);
            if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
                openPublishButton(false);
            } else {
                openPublishButton(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mCanPublish) {
            exitDialog();
        }else {
            this.finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER:
                createSelectedImagesList(data);
                break;
            case REQUEST_CODE_SELECT:
                createSelectedImagesList(data);
            default:
                break;
        }

    }

    private boolean mCanPublish;

    /**
     * 开发发布按钮
     *
     * @param bool 是否开启
     */
    private void openPublishButton(boolean bool) {
        if (bool) {
            mFunctionView.setEnabled(true);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_1da1f2));
        } else {
            mFunctionView.setEnabled(false);
            mFunctionView.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
        }
        mCanPublish = bool;
    }

    private void createSelectedImagesList(Intent data) {
        ArrayList<ImageItem> images = null;
        try {
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        } catch (Exception e) {

        }
        if (images == null) {
            return;
        }

        if (mSelectedImageList != null && mSelectedImageList.size() > 0) {
            mSelectedImageList.remove(mSelectedImageList.size() - 1);
        }
        for (int i = 0; i < images.size(); i++) {
            reSizeBitmap(images.get(i).path);
        }
        mSelectedImageList.addAll(images);
        mSelectedImageList.add(mAddPicItem);
        mSelectedImageListAdapter.update(mSelectedImageList);
        if (mImagePaths != null) {
            mImagePaths.clear();
        } else {
            mImagePaths = new ArrayList<>();
        }
        for (int i = 0; i < mSelectedImageList.size(); i++) {
            if (i < mSelectedImageList.size() - 1) {
                mImagePaths.add(mSelectedImageList.get(i).path);
            }
        }
        if (mImagePaths.size() == 0 && TextUtils.isEmpty(mContentView.getText().toString())) {
            openPublishButton(false);
        } else {
            openPublishButton(true);
        }

    }

    /**
     * 压缩下图片
     *
     * @param filePath
     */
    private void reSizeBitmap(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中


        Bitmap mBitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        if (mImagePaths == null || mImagePaths.size() == 0) {
            if (TextUtils.isEmpty(s)) {
                openPublishButton(false);
            } else {
                openPublishButton(true);
            }
        } else {
            openPublishButton(true);
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

    /**
     * 获取七牛token
     */
    private void getQiNiuToken() {
        GetQiNiuTokenRequest getQiNiuTokenRequest = new GetQiNiuTokenRequest();
        getQiNiuTokenRequest.from = "100";
        getQiNiuTokenRequest.dtype = "app";
        mGetSendDataRequestUUID = getQiNiuTokenRequest.startRequest(GetQiNiuTokenResponse.class, new HttpCallback<GetQiNiuTokenResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetQiNiuTokenResponse ret) {
                if (ret != null) {
                    if (ret.getCode() == 0) {
                        uploadPicListByQiNiu(ret.getData().getToken(), mImagePaths);
                    } else {
                        rootView.hiddenLoadingView();
                        ToastUtil.showToast(getApplicationContext(), ret.getError() != null ? ret.getError().getMessage() : getString(R.string.get_qiniu_token_error));
                    }

                } else {
                    rootView.hiddenLoadingView();
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.get_qiniu_token_error));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), error.getMessage());
            }
        });
    }

    UploadManager uploadManager = null;
    Configuration config = new Configuration.Builder()
            .chunkSize(2 * 1024 * 1024)        // 分片上传时，每片的大小。 默认256K
            .putThreshhold(4 * 1024 * 1024)   // 启用分片上传阀值。默认512K
            .connectTimeout(10)           // 链接超时。默认10秒
            .responseTimeout(60)          // 服务器响应超时。默认60秒
            .build();

    /**
     * 通过七牛云上传图片
     *
     * @param token 七牛上传证书
     */
    private void uploadPicListByQiNiu(String token, @NonNull List<String> fileList) {
        if (uploadManager == null) {
            uploadManager = new UploadManager(config);
        }
        if (fileList != null && fileList.size() > 0) {
            uploadPicByQiNiu(fileList, -1, null, token);
        }
    }

    public class QiNiuResponse {


        private String hash;
        private String key;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private void uploadPicByQiNiu(final List<String> filePathList, int position, String key, final String token) {
        position++;
        if (filePathList.size() <= position) {
            uploadData(mContentView.getText().toString(), mResourceIds);
        } else {
            final int finalPosition = position;
            uploadManager.put(filePathList.get(finalPosition), null, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject res) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        try {
                            mResourceIds = mResourceIds + (TextUtils.isEmpty(mResourceIds) ? "" : ",") + res.getString("key") + "|" + FileUtils.getFileType(filePathList.get(finalPosition));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        uploadPicByQiNiu(filePathList, finalPosition, null, token);

                    } else {
                        mResourceIds = "";
                        rootView.hiddenLoadingView();
                    }

                }
            }, new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String s, double v) {
                }
            }, null));
        }
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
