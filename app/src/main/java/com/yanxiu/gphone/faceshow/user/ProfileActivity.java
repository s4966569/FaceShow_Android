package com.yanxiu.gphone.faceshow.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.util.zxing.camera.CameraManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;

/**
 * Created by lufengqing on 2017/9/15.
 */

public class ProfileActivity extends FaceShowBaseActivity implements OnPermissionCallback {
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
    @BindView(R.id.tv_segment)
    TextView mStageView;
    @BindView(R.id.tv_stduy)
    TextView mSubjectView;
    @BindView(R.id.title_layout_title)
    TextView mTitleView;
    @BindView(R.id.title_layout_left_img)
    ImageView mBackView;


    private PopupWindow picPopupWindow;
    private static final int SELECT_FROM_GALLER = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int CROP_PICTURE = 3;
    private File portraitFile, photoFile; //拍照裁剪后的照片文件，拍照之后存储的照片文件

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

        Glide.with(mContext).load(UserInfo.getInstance().getInfo().getHeadImg()).asBitmap().into(mHeadImgView);
        mNameView.setText(UserInfo.getInstance().getInfo().getUserName());
        mMobileView.setText(UserInfo.getInstance().getInfo().getPhone());
        mSexView.setText(getSex());
        mStageView.setText(UserInfo.getInstance().getInfo().getStageName());
        mSubjectView.setText(UserInfo.getInstance().getInfo().getSubjectName());
    }

    private String getSex(){
        String sex=UserInfo.getInstance().getInfo().getSex();
        String sexTxt="";
        if (!TextUtils.isEmpty(sex)) {
            if (sex.equals("0")) {
                sexTxt="女";
            } else if (sex.equals("1")) {
                sexTxt="男";
            }
        }else {
            sexTxt="未知";
        }
        return sexTxt;
    }

    @OnClick({R.id.person_info, R.id.title_layout_left_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_info://点击修改头像
                showPopWindow();
                break;
            case R.id.title_layout_left_img:
                finish();
                break;
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
        if (CheckCamera(ProfileActivity.this)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/yanxiu/portrait");
            if (!file.exists()) {
                try {
                    //按照指定的路径创建文件夹
                    file.mkdirs();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            photoFile = new File(Environment.getExternalStorageDirectory() + "/yanxiu/portrait/photo.jpg");
            if (!photoFile.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    photoFile.createNewFile();
                } catch (Exception e) {
                }
            }

            Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            infoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(infoIntent, TAKE_PHOTO);
            // 指定调用相机拍照后的照片存储的路径
        } else {
            Toast.makeText(this, "请检查权限", Toast.LENGTH_SHORT).show();
        }
        picPopupWindow.dismiss();
    }

    public boolean CheckCamera(Context context) {
        boolean flag = true;
        CameraManager.init(context);
        CameraManager cameraManager = CameraManager.get();
        SurfaceView scanPreview = new SurfaceView(context);
        SurfaceHolder surfaceHolder = scanPreview.getHolder();
        try {
            cameraManager.openDriver(surfaceHolder);
        } catch (Exception ioe) {
            flag = false;
        }
        cameraManager.closeDriver();
        return flag;
    }

    private void doSelectFromPhotoLib() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_FROM_GALLER);
        picPopupWindow.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 直接从相册获取
                case SELECT_FROM_GALLER:
                    if (data != null)
                        startPhotoZoom(data.getData(), Uri.fromFile(createCroppedImageFile()));
                    break;
                // 调用相机拍照时
                case TAKE_PHOTO:
                    if (photoFile == null) {
                        photoFile = new File(Environment
                                .getExternalStorageDirectory() + "/yanxiu/portrait/photo.jpg");
                    }
                    startPhotoZoom(Uri.fromFile(photoFile), Uri.fromFile(createCroppedImageFile()));
                    break;
                // 取得裁剪后的图片
                case CROP_PICTURE:
                    if (portraitFile != null) {
                        uploadPortrait();
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
//        if(intent.resolveActivity(getPackageManager())==null){
//            ToastMaster.showToast("该手机不支持裁剪");
//        }
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveCroppedImageFileUri);
        startActivityForResult(intent, CROP_PICTURE);
    }

    /**
     * 创建保存裁剪后图片的文件
     *
     * @return
     */
    private File createCroppedImageFile() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/yanxiu/portrait/");
        if (!dir.exists())
            dir.mkdir();
        portraitFile = new File(dir, "photo_cropped.jpg");
        try {
            portraitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "裁剪文件异常", Toast.LENGTH_SHORT).show();
        }
        return portraitFile;
    }

    /**
     * 上传头像
     */
    private void uploadPortrait() {
    //提交上传头像请求
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
        unbinder.unbind();
    }
}
