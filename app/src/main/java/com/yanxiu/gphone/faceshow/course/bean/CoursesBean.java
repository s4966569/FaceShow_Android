package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程list
 * Created by 戴延枫 on 2017/9/15.
 */

public class CoursesBean extends BaseBean {
    private String date;
    private ArrayList<CourseBean> coursesList = new ArrayList<>();
    private boolean isToday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<CourseBean> getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(ArrayList<CourseBean> coursesList) {
        this.coursesList = coursesList;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
