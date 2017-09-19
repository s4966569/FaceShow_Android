package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程安排
 * Created by 戴延枫 on 2017/9/18.
 */

public class CourseDetailBean extends BaseBean {
    private String courseDate;//日期--分割标题
    private String courseName;//课程名称
    private String time;//时间
    private String teacher;//授课专家
    private String location;//地点
    private String course_detail;//课程简介
    private boolean isHeader = false;
    private ArrayList<CourseDetailBeanItem> courseItem = new ArrayList<>();

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

    public String getCourse_detail() {
        return course_detail;
    }

    public void setCourse_detail(String course_detail) {
        this.course_detail = course_detail;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public ArrayList<CourseDetailBeanItem> getCourseItem() {
        return courseItem;
    }

    public void setCourseItem(ArrayList<CourseDetailBeanItem> item) {
        this.courseItem = item;
    }

    public static class CourseDetailBeanItem extends BaseBean {
        private String imgUrl;//icomurl
        private String course_detail_item_txt;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCourse_detail_item_txt() {
            return course_detail_item_txt;
        }

        public void setCourse_detail_item_txt(String text) {
            this.course_detail_item_txt = text;
        }

        private String courseDate;//日期--分割标题
        private String courseName;//课程名称
        private String time;//时间
        private String teacher;//授课专家
        private String location;//地点
        private String course_detail;//课程简介
        private boolean isHeader = false;

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

        public String getCourse_detail() {
            return course_detail;
        }

        public void setCourse_detail(String course_detail) {
            this.course_detail = course_detail;
        }

        public boolean isHeader() {
            return isHeader;
        }

        public void setHeader(boolean header) {
            isHeader = header;
        }
    }

    public static CourseDetailBean getMockData() {

        CourseDetailBean bean = new CourseDetailBean();
        for (int i = 0; i < 10; i++) {

            CourseDetailBeanItem cb = new CourseDetailBeanItem();
            if (i == 0) {
                cb.setHeader(true);
                cb.setCourseDate("2017年9月18日15:41:57");
                cb.setCourseName("按揭客户达数据库里");
                cb.setLocation("第三会议室");
                cb.setTeacher("著名僵尸：李老师");
                cb.setTime("上午 12.00");
                cb.setCourse_detail("阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的" +
                        "阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的阿斯顿奇偶及发神经的");
                bean.getCourseItem().add(cb);
            } else {
                cb.setHeader(false);

                cb.setImgUrl("http://www.baidu.com");
                cb.setCourse_detail_item_txt("666666666666666");
                bean.getCourseItem().add(cb);
            }
        }
        return bean;
    }
}
