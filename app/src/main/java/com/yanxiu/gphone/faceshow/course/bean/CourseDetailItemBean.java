package com.yanxiu.gphone.faceshow.course.bean;

import java.util.ArrayList;


/**
 * 课程详情的每个item的数据。（把server数据转成自己的格式）
 * Created by 戴延枫 on 2017/9/18.
 */

public class CourseDetailItemBean extends CourseBean {
    public static final int header = 1;
    public static final int lecturer = 2;//讲师
    public static final int attachment = 3;//附件
    public static final int interact = 4;//互动（投票等）

    /**
     * 自己的数据类型
     */
    private int myDataType;

    public int getMyDataType() {
        return myDataType;
    }

    public void setMyDataType(int myDataType) {
        this.myDataType = myDataType;
    }
}
