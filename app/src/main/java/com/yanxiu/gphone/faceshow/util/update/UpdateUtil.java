package com.yanxiu.gphone.faceshow.util.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.constant.Constants;
import com.yanxiu.gphone.faceshow.customview.dialog.UpdateDialog;
import com.yanxiu.gphone.faceshow.http.request.DownLoadRequest;
import com.yanxiu.gphone.faceshow.http.request.InitializeRequest;
import com.yanxiu.gphone.faceshow.http.response.InitializeReponse;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.service.UpdateService;
import com.yanxiu.gphone.faceshow.util.ActivityManger;
import com.yanxiu.gphone.faceshow.util.DownloadThread;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.Logger;
import com.yanxiu.gphone.faceshow.util.SystemUtil;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 升级工具类
 * Created by Canghaixiao.
 * Time : 2017/6/7 16:36.
 * Function :
 */
public class UpdateUtil {

    private static final String TAG = "updata";

    private static InitializeRequest mInitializeRequest;
    //     private static UpdateDialog mUpdateDialog;
    private static UpdateDialog mUpdateDialog;
    private static NotificationManager mNotificationManager;
    private static Notification mNotification;
    public static final int NOTIFICATION_ID = 0x11;
    private static NotificationCompat.Builder mNotificationBuilder;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static List<com.yanxiu.gphone.faceshow.http.response.InitializeReponse.Data> mData;
    private static boolean mIsUpgrade;

    public interface OnUpgradeCallBack {
        void onExit();

        void onDownloadApk(boolean isSuccess);

        void onInstallApk(boolean isSuccess);

        void onCancle();
    }

    public interface OnUpdateFinishCallBack {
        void onUpdateFinish(String state);
    }

    public static void Initialize(final Context context, final boolean isFromUser, final OnUpdateFinishCallBack onUpdateFinishCallBack) {
//         if (!isFromUser && Constants.UPDATA_TYPE == 1) {
//             return;
//         }
        Constants.UPDATA_TYPE = 1;
        updateCancel();
        String channel = SystemUtil.getChannelName();
        mInitializeRequest = new InitializeRequest();
        mInitializeRequest.channel = channel;
        mInitializeRequest.startRequest(InitializeReponse.class, new HttpCallback<InitializeReponse>() {
            @Override
            public void onSuccess(RequestBase request, InitializeReponse ret) {
                if (ret.data != null && ret.data.size() > 0) {
                    mData = ret.data;
                    mIsUpgrade = checkIsShouldUpdate(Constants.versionName, ret.data.get(0).version);
                    if (mIsUpgrade) {

                    } else {
                        if (isFromUser) {
                            ToastUtil.showToast(context, R.string.update_new);
                        }
                    }
                } else {
                    if (isFromUser) {
                        ToastUtil.showToast(context, R.string.update_new);
                    }
                }
                if (onUpdateFinishCallBack != null)
                    onUpdateFinishCallBack.onUpdateFinish("onSuccess");
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (isFromUser) {
                    ToastUtil.showToast(context, error.getMessage());
                }
                if (onUpdateFinishCallBack != null)
                    onUpdateFinishCallBack.onUpdateFinish("onFail");
            }
        });
    }

    public static void checkUpdate(final Context context) {
        if (mIsUpgrade) {
            if (!TextUtils.isEmpty(mData.get(0).fileURL)) {
                String[] str = mData.get(0).fileURL.split("\\.");
                if (str.length > 1 && "apk".equals(str[str.length - 1])) {
//                                 if (true){
                    showUpdateDialog(context, mData.get(0), new OnUpgradeCallBack() {
                        @Override
                        public void onExit() {
                            mIsUpgrade = false;
                            mData = null;
                            ActivityManger.destoryAll();
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            System.exit(-1);
                        }

                        @Override
                        public void onDownloadApk(boolean isSuccess) {
                            mIsUpgrade = false;
                            mData = null;
                        }

                        @Override
                        public void onInstallApk(boolean isSuccess) {
                            mIsUpgrade = false;
                            mData = null;
                        }

                        @Override
                        public void onCancle() {
                            mIsUpgrade = false;
                            mData = null;
                        }
                    });
                } else {
//                                 ToastManager.showMsgOnDebug("这是版本更新接口提示,测试同学,你们辛苦了,请按照正常的作业流程走,不要乱跑,谢谢合作,此条消息只在debug环境下出现");
                }
            } else {
//                             ToastManager.showMsgOnDebug("这是版本更新接口提示,测试同学,你们辛苦了,请按照正常的作业流程走,不要乱跑,谢谢合作,此条消息只在debug环境下出现");
            }
        }

    }

    private static class MyPermission implements OnPermissionCallback {

        Context context;
        InitializeReponse.Data data;
        OnUpgradeCallBack callBack;

        public MyPermission(Context context, InitializeReponse.Data data, OnUpgradeCallBack callBack) {
            this.context = context;
            this.data = data;
            this.callBack = callBack;
        }

        @Override
        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationBuilder = new NotificationCompat.Builder(context);
//            mNotification = mNotificationBuilder.build();
            downloadApk(context, data.fileURL, callBack);
        }

        @Override
        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {

        }
    }

    private static void showUpdateDialog(final Context context, final InitializeReponse.Data data, final OnUpgradeCallBack callBack) {
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }
        mUpdateDialog = new UpdateDialog(context, data.upgradetype, new UpdateDialog.UpdateDialogCallBack() {
            @Override
            public void update() {
                FaceShowBaseActivity.requestWriteAndReadPermission(new MyPermission(context, data, callBack));
            }

            @Override
            public void cancel() {
                if (callBack != null) {
                    callBack.onCancle();
                }
            }

            @Override
            public void exit() {
                if (callBack != null) {
                    callBack.onExit();
                }
            }
        });
//        mUpdateDialog.setTitles(data.title, data.version);
//        mUpdateDialog.setContent(data.content);
        mUpdateDialog.setTitleText(data.title);
        mUpdateDialog.setContentText(data.content);
        mUpdateDialog.show();
    }

    private static void downloadApk(final Context context, String fileURL, final OnUpgradeCallBack callBack) {

        DownloadThread downloadThread = new DownloadThread(fileURL, FileUtil.getExternalStorageAbsolutePath("faceshow.apk"), new DownLoadRequest.OnDownloadListener() {
            private long preMillis = System.currentTimeMillis();

            @Override
            public void onDownloadSuccess(String saveDir) {
//                mNotificationManager.cancel(NOTIFICATION_ID);
//                mNotification.icon = R.drawable.notification01;
//                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading_success);
//                mNotification.contentView.setProgressBar(R.id.progress_value, 100, 100, false);
//                mNotification.contentView.setViewVisibility(R.id.progress_value, View.GONE);
                mUpdateDialog.setProgress(100);
                mUpdateDialog.dismiss();
                if (callBack != null) {
                    callBack.onDownloadApk(true);
                    installApk(context, saveDir);
                }
            }

            @Override
            public void onDownloadStart() {
//                Intent intent = new Intent();
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_updata_layout);
//                remoteViews.setTextViewText(R.id.app_name, context.getResources().getString(R.string.app_name));
//
//                remoteViews.setTextViewText(R.id.progress_text, "0%");
//                remoteViews.setProgressBar(R.id.progress_value, 100, 0, false);
////                mNotification.contentView = remoteViews;
//                mNotification = mNotificationBuilder.setSmallIcon(R.drawable.notification04)
//                        .setContentTitle("正在下载")
//                        .setContent(remoteViews)
//                        .setOngoing(true)
//                        .setContentIntent(pendingIntent).build();
                mUpdateDialog.setProgress(0);

//                mNotification.contentView.setInt(R.id.app_name,"setTextColor", isDarkNotificationTheme(context)==true?Color.WHITE:Color.BLACK);
//                        setInt(R.id.share_content, "setTextColor", isDarkNotificationTheme(context)==true?Color.WHITE:Color.BLACK);

//                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading);
//                mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//                mNotification.icon = R.drawable.notification04;
//
//                mNotification.contentIntent = pendingIntent;
//                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
//                remoteViews.setInt(R.id.app_name, "setTextColor", isDarkNotificationTheme(context) == true ? Color.WHITE : Color.BLACK);
//                remoteViews.setInt(R.id.progress_text, "setTextColor", isDarkNotificationTheme(context) == true ? Color.WHITE : Color.BLACK);
            }

            @Override
            public void onDownloading(int progress) {
                if (System.currentTimeMillis() - preMillis < 500 && progress != 100) {
                    return;
                }
//                if (mNotification == null || mNotification.contentView == null)
//                    return;
//                mUpdateDialog.setProgress(progress);
//                mNotification.contentView.setProgressBar(R.id.progress_value, 100, (int) progress, false);
//                mNotification.contentView.setTextViewText(R.id.progress_text, (int) progress + "%");
//                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                preMillis = System.currentTimeMillis();
                mUpdateDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {
                Logger.d(TAG, "onfail");
//                mNotificationManager.cancel(NOTIFICATION_ID);
//                mNotification.icon = R.drawable.notification01;
//                mNotification.tickerText = context.getResources().getString(R.string.update_asynctask_downloading_fail);
                if (callBack != null) {
                    callBack.onDownloadApk(false);
                }
                ToastUtil.showToast(context, R.string.update_asynctask_downloading_fail);
                mUpdateDialog.dismiss();
            }
        });

        downloadThread.start();
    }

    private static void installApk(Context context, String filePath) {
//         Intent intent = new Intent();
//         intent.setAction(Intent.ACTION_VIEW);
//         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//         SpManager.setFristStartUp(true);
//         String type = "application/vnd.android.package-archive";
//         File file = new File(filePath);
//         intent.setDataAndType(Uri.fromFile(file), type);
//         context.startActivity(intent);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        SpManager.setFristStartUp(true);
        String type = "application/vnd.android.package-archive";
        File file = new File(filePath);
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, "com.yanxiu.gphone.faceshow.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    private static boolean checkIsShouldUpdate(String nowVersion, String serverVersion) {
        if (!isEmpty(nowVersion) && !isEmpty(serverVersion)) {
            if (!nowVersion.equals(serverVersion)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (null != str) {
            if (str.length() > 4) {
                return false;
            }
        }
        return null == str || "".equals(str) || "NULL"
                .equals(str.toUpperCase());
    }

    private static void updateCancel() {
        if (mInitializeRequest != null) {
            mInitializeRequest.cancelRequest();
            mInitializeRequest = null;
        }
    }


    public static boolean isDarkNotificationTheme(Context context) {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context));
    }

    /**
     * 获取通知栏颜色
     *
     * @param context
     * @return
     */
    public static int getNotificationColor(Context context) {
        int layoutId = mNotification.contentView.getLayoutId();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
        if (viewGroup.findViewById(android.R.id.title) != null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }


    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }
}
