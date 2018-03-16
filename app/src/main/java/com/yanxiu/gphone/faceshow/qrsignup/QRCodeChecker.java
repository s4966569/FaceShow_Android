package com.yanxiu.gphone.faceshow.qrsignup;

import android.text.TextUtils;
import android.util.Log;

import com.test.yanxiu.common_base.utils.UrlRepository;


/**
 * Created by srt on 2018/3/5.
 * 负责处理扫描到的二维码
 * 判断二维码类型
 */

public class QRCodeChecker {
    private final String TAG=getClass().getSimpleName();
    /**
     * 检查是否是 签到二维码 仅对内容关键字段进行检查
     * @param codeStr 二维码扫描结果
     *
     * */
    public boolean isCheckInCode(String codeStr) {
        if (strBaseCheck(codeStr)) {
            /*目前根据 base url 与 method 部分来判断
              首先判断是否是 APP 的 服务器地址，然后判断method是否是 相应的字段内容*/
            return codeStr.startsWith(
                    UrlRepository.getInstance().getServer()+"?method=interact.userSignIn");
        }
        return false;
    }

    /**
     * 是研修server的二维码
     * */
    private boolean isYanxiuServer(String codeStr){
        return codeStr.startsWith(UrlRepository.getInstance().getServer());
    }


    /**
     * 检查是否是 HTML5 宣传界面
     * APP 不存在 HTML5 二维码使用
     *
     * */
    public boolean isHTML5Page(String codeStr) {
        if (strBaseCheck(codeStr)) {
            return codeStr.startsWith("http://2bai.co");
        }
        return false;
    }
    /**
     * 检查 是否是 APP 下载二维码
     * APP 不存在 下载二维码的使用
     *
     * */
    public boolean isDownloadCode(String codeStr) {
        if (strBaseCheck(codeStr)) {
            return isYanxiuServer(codeStr);
        }
        return false;
    }
    /**
     * 检查是否是 班级二维码
     * */
    public boolean isClazzCode(String codeStr) {
        if (strBaseCheck(codeStr)) {
            /*为了测试流程的顺畅 没有 检查逻辑*/
            return isYanxiuServer(codeStr);
        }
        return false;
    }
    /**
     * 对字符串进行基本的检查
     * 是否为空 是否符合 二维码形式
     *
     * */
    private boolean strBaseCheck(String str){
        /*空检查*/
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        /**/
        /*所有检查通过 确认可以进行下一步检查*/
        return true;
    }


    /**
     * 二维码 信息提取
     * 提取 calzsId
     * */
    public int getClazsIdFromQR(String result){
        if (TextUtils.isEmpty(result)) {
            return -1;
        }else {
            int position=result.indexOf("clazsId=");
            if (result.contains("clazsId")&&position>0&&result.length()>position+8) {
                result=result.substring(position+8,result.length());
                Log.i(TAG, "getClazsIdFromQR: "+ result);
            }else {
                return -1;
            }
            return Integer.valueOf(result);
        }
    }


}
