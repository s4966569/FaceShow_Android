package com.yanxiu.gphone.faceshow.homepage.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程安排
 * Created by 戴延枫 on 2017/9/15.
 */

public class CourseArrangeBean extends BaseBean {
    private String courseDate;//日期--分割标题
    private String courseName;//课程名称
    private String time;//时间
    private String teacher;//授课专家
    private String location;//地点

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static ArrayList<CourseArrangeBean> getMockData() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            CourseArrangeBean bean = new CourseArrangeBean();
            if (i % 3 == 0) {
                bean.setCourseDate("1");
            } else {
                bean.setCourseName("按揭客户达数据库里");
                bean.setLocation("第三会议室");
                bean.setTeacher("著名僵尸：李老师");
                bean.setTime("上午 12.00");
            }

            list.add(bean);
        }
        return list;
    }
}
