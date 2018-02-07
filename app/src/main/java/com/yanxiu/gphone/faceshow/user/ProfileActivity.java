package com.yanxiu.gphone.faceshow.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.response.HeadimgUploadBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.http.request.UpLoadRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.user.request.UpdataUserMessageRequest;
import com.yanxiu.gphone.faceshow.util.CornersImageTarget;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.FileUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/9/15.
 */

public class ProfileActivity extends FaceShowBaseActivity implements OnPermissionCallback {
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    private Unbinder unbinder;
    private Context mContext;
    private PublicLoadLayout rootView;
    @BindView(R.id.person_img)
    ImageView mHeadImgView;
    @BindView(R.id.tv_name)
    TextView mNameView;
    @BindView(R.id.tv_phone)
    TextView mMobileView;
    @BindView(R.id.tv_gender)
    TextView mSexView;
    @BindView(R.id.tv_stage_subject)
    TextView mStageSubjectView;
    @BindView(R.id.title_layout_title)
    TextView mTitleView;
    @BindView(R.id.title_layout_left_img)
    ImageView mBackView;
    @BindView(R.id.rl_stage_subject)
    RelativeLayout mRlStageSubejct;

    private PopupWindow picPopupWindow;
    private static final int SELECT_FROM_GALLER = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PICTURE = 3;
    private static final int MODIFY_NAME = 4;
    private static final int MODIFY_SEX = 5;
    private static final int MODIFY_STAGE_SUBJECT = 6;
    private File portraitFile, photoFile; //拍照裁剪后的照片文件，拍照之后存储的照片文件
    private String path;
    private String crop_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_profile);
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);
        mTitleView.setText(R.string.my);
        initData();
    }


    private void initData() {
        mBackView.setVisibility(View.VISIBLE);

        setHeadimg();
        mNameView.setText(UserInfo.getInstance().getInfo().getRealName());
        mMobileView.setText(UserInfo.getInstance().getInfo().getMobilePhone());
        mSexView.setText(getSex());
        String stageName = TextUtils.isEmpty(UserInfo.getInstance().getInfo().getStageName()) ? "暂无" : UserInfo.getInstance().getInfo().getStageName();
        String subjectName = TextUtils.isEmpty(UserInfo.getInstance().getInfo().getSubjectName()) ? "暂无" : UserInfo.getInstance().getInfo().getSubjectName();
        if (stageName.equals("暂无")) {
            mStageSubjectView.setText("暂无");
        } else if (subjectName.equals("暂无")) {
            mStageSubjectView.setText(stageName);
        } else {
            mStageSubjectView.setText(stageName +"-"+ subjectName);
        }
    }

    private void setHeadimg() {
        Glide.with(mContext).load(UserInfo.getInstance().getInfo().getAvatar()).asBitmap().placeholder(R.drawable.person_img).centerCrop().into(new CornersImageTarget(mContext, mHeadImgView, 12));
    }

    private String getSex() {
        String sex = UserInfo.getInstance().getInfo().getSexName();
        return sex;
    }

    @OnClick({R.id.person_info, R.id.title_layout_left_img, R.id.rl_name, R.id.rl_sex, R.id.rl_stage_subject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //点击修改头像
            case R.id.person_info:
                showPopWindow();
                break;
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.rl_name:
                //修改用户名
                startActivityForResult(new Intent(this, ModifyUserNameActivity.class), MODIFY_NAME);
                break;
            case R.id.rl_sex:
                //修改用户性别
                startActivityForResult(new Intent(this, ModifyUserSexActivity.class), MODIFY_SEX);
                break;
            case R.id.rl_stage_subject:
                //修改用户学段学科
                EventUpdate.onChooseStageSubjectButton(this);
                startActivityForResult(new Intent(this, ModifyUserStageActivity.class), MODIFY_STAGE_SUBJECT);
                break;
            default:

        }
    }

    /**
     * 选择头像的PopWindow
     */
    private void showPopWindow() {
        if (picPopupWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.popwindow_select_picture, null);
            picPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            picPopupWindow.setAnimationStyle(R.style.pop_anim);
            picPopupWindow.setFocusable(true);
            picPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            setOnItemClickListener(contentView);
        }
        picPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 为选择图像的PopWindow设置点击事件
     *
     * @param contentView PopWindow的contentView
     */
    private void setOnItemClickListener(View contentView) {
        View pop_background = contentView.findViewById(R.id.pop_background);
        TextView tv_take_photo = (TextView) contentView.findViewById(R.id.tv_take_photo);
        TextView tv_select_from_gallery = (TextView) contentView.findViewById(R.id.tv_select_from_gallery);
        TextView tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);

        //点击背景消失
        pop_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPopupWindow.dismiss();
            }
        });
        //拍照
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTakePhotoPermission();
            }
        });
        //从相册选择
        tv_select_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelectFromPhotoLibPermission();
            }
        });
        //取消
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPopupWindow.dismiss();
            }
        });
    }

    // 权限相关
    private void checkTakePhotoPermission() {
        FaceShowBaseActivity.requestCameraPermission(ProfileActivity.this);
    }

    private void checkSelectFromPhotoLibPermission() {
        FaceShowBaseActivity.requestWriteAndReadPermission(ProfileActivity.this);
    }

    private void doTakePhoto() {
        FaceShowBaseActivity.requestCameraPermission(new OnPermissionCallback() {
            @Override
            public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                mCameraPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                File file = new File(mCameraPath);
                if (file.exists()) {
                    try {
                        //在指定的文件夹中创建文件
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                infoIntent.addCategory(Intent.CATEGORY_DEFAULT);
                Uri uri = Uri.fromFile(file);
                infoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(infoIntent, TAKE_PHOTO);
                // 指定调用相机拍照后的照片存储的路径
            }

            @Override
            public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                Toast.makeText(ProfileActivity.this, "请检查权限", Toast.LENGTH_SHORT).show();
            }
        });
        picPopupWindow.dismiss();
    }

    private void doSelectFromPhotoLib() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_FROM_GALLER);
        picPopupWindow.dismiss();
    }

    private String mCropPath;
    private String mCameraPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 直接从相册获取
                case SELECT_FROM_GALLER:
                    if (data != null) {
                        Uri uri = data.getData();
                        mCropPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                        startPhotoZoom(uri, Uri.fromFile(new File(mCropPath)));
                    }
                    break;
                // 调用相机拍照时
                case TAKE_PHOTO:
                    if (!TextUtils.isEmpty(mCameraPath)) {
                        mCropPath = FileUtils.getImageCatchPath(System.currentTimeMillis() + ".jpg");
                        try {
                            new FileInputStream(new File(mCameraPath));
                            Uri imageUri;
                            if (Build.VERSION.SDK_INT < 24) {
                                imageUri = Uri.fromFile(new File(mCameraPath));
                            } else {
                                imageUri = FileProvider.getUriForFile(ProfileActivity.this, "com.yanxiu.gphone.faceshow.fileprovider", new File(mCameraPath));
                            }
                            startPhotoZoom(imageUri, Uri.fromFile(new File(mCropPath)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                // 取得裁剪后的图片
                case CROP_PICTURE:
                    if (!TextUtils.isEmpty(mCropPath)) {
                        if (new File(mCropPath).exists()) {
                            uploadPortrait(mCropPath);
                        }
                    }
                    break;
                case MODIFY_NAME:
                    mNameView.setText(UserInfo.getInstance().getInfo().getRealName());
                    break;
                case MODIFY_SEX:
                    mSexView.setText(getSex());
                    break;
                case MODIFY_STAGE_SUBJECT:
                    if (data != null) {
                        mStageSubjectView.setText(data.getStringExtra("stageSubjectName"));
                    }
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, Uri saveCroppedImageFileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        //输出图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveCroppedImageFileUri);
        startActivityForResult(intent, CROP_PICTURE);
    }


    /**
     * 上传头像
     */
    private void uploadPortrait(final String path) {
        //提交上传头像请求
        UpLoadRequest.getInstense().setConstantParams(new UpLoadRequest.findConstantParams() {
            @NonNull
            @Override
            public String findUpdataUrl() {
                String token = SpManager.getToken();
                return UrlRepository.getInstance().getUploadServer() + "?token=" + token + "&width=110&height=110";
            }

            @Override
            public int findFileNumber() {
                return 1;
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
                return path;
            }
        }).setProgressListener(new UpLoadRequest.onProgressListener() {
            @Override
            public void onRequestStart() {

            }

            @Override
            public void onProgress(int index, int position) {

            }

            @Override
            public void onRequestEnd() {

            }
        }).setListener(new UpLoadRequest.onUpLoadlistener() {
            @Override
            public void onUpLoadStart(int position, Object tag) {
                rootView.showLoadingView();
            }

            @Override
            public void onUpLoadSuccess(int position, Object tag, String jsonString) {
                rootView.hiddenLoadingView();
                try {
                    Gson gson = new Gson();
                    HeadimgUploadBean uploadBean = gson.fromJson(jsonString, HeadimgUploadBean.class);
                    if (uploadBean != null && uploadBean.tplData != null && uploadBean.tplData.data != null && uploadBean.tplData.data.size() > 0) {
                        updataHeadimg(uploadBean.tplData.data.get(0).url);
                    } else {
                        ToastUtil.showToast(mContext, "头像上传失败");
                    }
                } catch (Exception e) {
                    ToastUtil.showToast(mContext, "头像上传失败");
                }
            }

            @Override
            public void onUpLoadFailed(int position, Object tag, String failMsg) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, "头像上传失败");
            }

            @Override
            public void onError(String errorMsg) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, "头像上传失败");
            }
        });

    }

    private void updataHeadimg(final String url) {
        UpdataUserMessageRequest messageRequest = new UpdataUserMessageRequest();
        messageRequest.avatar = url;
        messageRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                rootView.hiddenLoadingView();
                UserInfo.getInstance().getInfo().setAvatar(url);//保留次行代 兼容之前人写的代码
                UserInfo.Info info = SpManager.getUserInfo();
                info.setAvatar(url);
                SpManager.saveUserInfo(info);
                setHeadimg();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(mContext, "头像上传失败");
            }
        });
    }

    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                doTakePhoto();
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                doSelectFromPhotoLib();
            }
        }
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "没有拍照权限", Toast.LENGTH_SHORT).show();
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "没有存储权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UpLoadRequest.getInstense().cancle();
        unbinder.unbind();
    }


}
