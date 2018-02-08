package com.yanxiu.gphone.faceshow.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yanxiu.gphone.faceshow.http.request.DownLoadRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sunpeng on 2018/2/8.
 */

public class DownloadThread extends Thread {
    private String mUrl;
    private String mSavePath;
    private DownLoadRequest.OnDownloadListener mListener;
    private Handler mHandler;

    public DownloadThread(String url, String savePath, DownLoadRequest.OnDownloadListener listener) {
        mUrl = url;
        mSavePath = savePath;
        mListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            File file = new File(mSavePath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            URL url = new URL(mUrl);
            connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);
            int code = connection.getResponseCode();
            // 部分文件是206，全部OK是200
            if (code == 200) {
                int length=connection.getContentLength();
                //开始下载之前
                if (mListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onDownloadStart();
                        }
                    });
                }
                // 已经设置了请求的位置，返回的是设置的位置对应的文件的输入流
                InputStream is = connection.getInputStream();
                BufferedInputStream bis  = new BufferedInputStream(is);
                RandomAccessFile raf = new RandomAccessFile(mSavePath, "rwd");
                raf.seek(0); // 设置文件开始写的位置
                int len = 0;
                int total = 0;
                byte[] buffer = new byte[4096];

                while ((len = bis.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                    total += len;
                    // 正在下载
                    final int progress = (int) (total * 100.00f / length);
                    Log.i("progress",String.valueOf(progress));
                    if (mListener != null) {
                        mListener.onDownloading(progress);
                    }
                }
                bis.close();
                is.close();
                raf.close();
                Log.i("progress","下载完成");
                //下载完成
                if (mListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onDownloadSuccess(mSavePath);
                        }
                    });
                }
            } else {
                //下载失败
                if (file.exists()) {
                    file.delete();
                }
                if (mListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onDownloadFailed();
                        }
                    });
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (mListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onDownloadFailed();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onDownloadFailed();
                    }
                });
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
