package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.LoadingView;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInRequest;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.zxing.camera.CameraManager;
import com.yanxiu.gphone.faceshow.util.zxing.decoding.CaptureActivityHandler;
import com.yanxiu.gphone.faceshow.util.zxing.decoding.InactivityTimer;
import com.yanxiu.gphone.faceshow.util.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码扫描签到页面
 */
public class QRCodeCheckInActivity extends ZXingBaseActivity implements
        SurfaceHolder.Callback {

    @BindView(R.id.img_left)
    ImageView img_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.preview_view)
    SurfaceView preview_view;
    @BindView(R.id.viewfinder_view)
    ViewfinderView viewfinder_view;

    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Dialog mDialog;


    public static void toThisAct(Context activity) {
        Intent intent = new Intent(activity
                , QRCodeCheckInActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inactivityTimer = new InactivityTimer(this);
        setContentView(R.layout.activity_arcode_check_in);
        ButterKnife.bind(this);

        final CameraManager[] cameraManager = {null};
        requestCameraPermission(new OnPermissionCallback() {
            @Override
            public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                CameraManager.init(QRCodeCheckInActivity.this);
                cameraManager[0] = CameraManager.get();
                SurfaceView scanPreview = new SurfaceView(QRCodeCheckInActivity.this);
                SurfaceHolder surfaceHolder = scanPreview.getHolder();
                try {
                    cameraManager[0].openDriver(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cameraManager[0].closeDriver();
            }

            @Override
            public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {

            }
        });
//        try {
//            CameraManager.init(this);
//            cameraManager = CameraManager.get();
//            SurfaceView scanPreview = new SurfaceView(this);
//            SurfaceHolder surfaceHolder = scanPreview.getHolder();
//            cameraManager.openDriver(surfaceHolder);
//        } catch (Exception e) {
//            mDialog = CreateDialog();
//            mDialog.setCancelable(false);
//            mDialog.show();
//        }

        tv_title.setText(R.string.check_in);
        hasSurface = false;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    private Dialog CreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("无法获取摄像头数据，请检查是否已经打开摄像头权限。");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                QRCodeCheckInActivity.this.finish();
            }
        });

        return builder.create();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(QRCodeCheckInActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
            CameraManager.get().closeDriver();
        }
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    /**
     * @param result
     * @param barcode
     */
    @Override
    public void handleDecode(Result result, Bitmap barcode) {

        String resultString = result.getText();
        // TODO: 17-9-19 此处需要个网络请求
        if (resultString != null) {
            CheckInRequest checkInRequest = new CheckInRequest();
            checkInRequest.scanString = resultString;
            checkInRequest.startRequest(CheckInResponse.class, new HttpCallback<CheckInResponse>() {
                @Override
                public void onSuccess(RequestBase request, CheckInResponse ret) {
                    if (ret.getCode() == 0) {
                        // TODO: 17-9-15 签到成功
                        startActivity(new Intent(QRCodeCheckInActivity.this, CheckInSuccessActivity.class));
                        QRCodeCheckInActivity.this.finish();
                    } else {
                        ToastUtil.showToast(QRCodeCheckInActivity.this, ret.getError().getMessage());
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    ToastUtil.showToast(QRCodeCheckInActivity.this, error.getMessage());
                }
            });


        } else {
            Intent intent = new Intent(QRCodeCheckInActivity.this, CheckInErrorActivity.class);
            intent.putExtra(CheckInErrorActivity.QR_STATUE, CheckInErrorActivity.QR_EXPIRED);
            startActivity(intent);
            QRCodeCheckInActivity.this.finish();
            // TODO: 17-9-15 签到失败
            //Toast.makeText(this, "二维码扫描失败", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void drawViewfinder() {
        viewfinder_view.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }

    };


    @Override
    public ViewfinderView getViewfinderView() {
        return viewfinder_view;

    }

    @OnClick(R.id.img_left)
    public void onViewClicked() {
        this.finish();
    }
}
