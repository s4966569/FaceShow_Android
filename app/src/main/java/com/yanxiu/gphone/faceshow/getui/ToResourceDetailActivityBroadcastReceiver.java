package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * 跳转到资源详情页面的广播接收者
 * Created by frc on 2017/10/19.
 */

public class ToResourceDetailActivityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        getResourceDetail(context, intent.getStringExtra("objectId"));

    }

    private void getResourceDetail(final Context context, String resId) {
        ResourceDetailRequest resourceDetailRequest = new ResourceDetailRequest();
        resourceDetailRequest.resId = resId;
        resourceDetailRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                if (ret!=null){
                    if (ret.getCode()==0&&ret.getData()!=null){
                        setIntent(context, ret.getData());
                    }else if (ret.getError()!=null&&!TextUtils.isEmpty(ret.getError().getMessage())){
                        ToastUtil.showToast(context,ret.getError().getMessage());
                    }else {
                        ToastUtil.showToast(context, "数据异常");
                    }

                }else {
                    ToastUtil.showToast(context, "数据异常");
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(context, "网络异常");
            }
        });
    }


    public void setIntent(Context context, ResourceDetailResponse.ResourceDetailBean data) {
        if (TextUtils.equals(data.getType(), "1") && !TextUtils.isEmpty(data.getUrl())) {
//            WebViewActivity.loadThisAct(context, data.getUrl(), data.getResName());
            Intent resourceDetailIntent = new Intent(context, WebViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            resourceDetailIntent.putExtra("url", data.getUrl());
            resourceDetailIntent.putExtra("title", data.getResName());
            context.startActivity(resourceDetailIntent);
        } else if (TextUtils.equals(data.getType(), "0")) {
            AttachmentInfosBean attachmentInfosBean = data.getAi();
            if (attachmentInfosBean != null && attachmentInfosBean.getResType() != null) {
                if (attachmentInfosBean.getResType().equals(AttachmentInfosBean.EXCEL) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PDF)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PPT) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.TEXT)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.WORD)) {
                    Intent intent;
                    PdfBean pdfbean = new PdfBean();
                    pdfbean.setName(attachmentInfosBean.getResName());
                    pdfbean.setUrl(attachmentInfosBean.getPreviewUrl());
                    pdfbean.setRecord(0);

                    intent = new Intent(context, PDFViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pdfbean", pdfbean);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    return;
                }
            } else {
                ToastUtil.showToast(context, "数据异常");
            }
        } else {
            ToastUtil.showToast(context, "数据异常");
        }
    }
}
