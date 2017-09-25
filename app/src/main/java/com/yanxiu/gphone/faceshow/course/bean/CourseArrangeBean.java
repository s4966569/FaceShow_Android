package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程安排
 * Created by 戴延枫 on 2017/9/15.
 */

public class CourseArrangeBean extends BaseBean {
    private ArrayList<CoursesBean> courses = new ArrayList<>();

//    public ArrayList<CoursesBean> getCourses() {
//        return courses;
//    }

    public void setCourses(ArrayList<CoursesBean> courses) {
        this.courses = courses;
    }

    /**
     * 把server的数据转化为可用数据
     * dyf
     *
     * @return
     */
    public ArrayList<CourseBean> getCourses() {
        ArrayList<CourseBean> list = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            CoursesBean cb = courses.get(i);
            CourseBean headerbean = new CourseBean();
            headerbean.setDate(cb.getDate());
            headerbean.setToday(cb.isToday());
            list.add(headerbean);
            list.addAll(cb.getCoursesList());
        }
        return list;
    }
}
