package com.yanxiu.gphone.faceshow.util.talkingdata;

import android.content.Context;

import com.tendcloud.tenddata.TCAgent;

/**
 * @author by frc.
 *         Time : 2017/11/15 14:35.
 *         <p>
 *         Function :打点，点击事件记录
 */
public class EventUpdate {


    /**
     * 点击首页签到
     */
    public static void onHomeSignInButton(Context context) {
    TCAgent.onEvent(context,"点击首页签到");
    }

    /**u
     * 点击课程
     */
    public static void onCourseButton(Context context) {
        TCAgent.onEvent(context,"点击课程");
    }
    /**
     * 点击资源
     */
    public static void onResourceButton(Context context) {
        TCAgent.onEvent(context,"点击资源");
    }
    /**
     * 点击任务
     */
    public static void onTaskButton(Context context) {
        TCAgent.onEvent(context,"点击任务");
    }
    /**
     * 点击日程
     */
    public static void onScheduleButton(Context context) {
        TCAgent.onEvent(context,"点击日程");
    }

    /**
     * 查看课程详情
     */
    public static void onCourseDetailButton(Context context){
        TCAgent.onEvent(context,"查看课程详情");
    }

    /**
     * 点击签到详情页中的签到按钮
     */
    public static void onSignInButtonInSignInDetailPage(Context context){
        TCAgent.onEvent(context,"点击签到详情页中的签到按钮");
    }

    /**
     * 点击修改学段学科
     */
    public static void onChooseStageSubjectButton(Context context){
        TCAgent.onEvent(context,"点击修改学段学科");
    }



}
