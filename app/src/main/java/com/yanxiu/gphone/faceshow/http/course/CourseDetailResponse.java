package com.yanxiu.gphone.faceshow.http.course;

import android.text.TextUtils;

import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.ArrayList;

/**
 * 课程详情
 */

public class CourseDetailResponse extends FaceShowBaseResponse {
    private CourseDetailBean data = new CourseDetailBean();


//    public CourseDetailBean getData() {
//        return data;
//    }

    public void setData(CourseDetailBean data) {
        this.data = data;
    }


    /**
     * 把server的数据转化为可用数据
     * dyf
     *
     * @return
     */
    public ArrayList<CourseDetailItemBean> getCourseDetailData() {
        ArrayList<CourseDetailItemBean> list = new ArrayList<>();
        CourseDetailItemBean header = new CourseDetailItemBean();
        header.setMyDataType(CourseDetailItemBean.header);

        header.setId(data.getCourse().getId());
        header.setSubscriberId(data.getCourse().getSubscriberId());
        header.setSubscriberType(data.getCourse().getSubscriberType());
        header.setCourseName(data.getCourse().getCourseName());
        if (TextUtils.isEmpty(data.getCourse().getLecturer())) {
            header.setLecturer(getTeacher());
        } else {
            header.setLecturer(data.getCourse().getLecturer());
        }

        header.setSite(data.getCourse().getSite());
        header.setStartTime(data.getCourse().getStartTime());
        header.setEndTime(data.getCourse().getEndTime());
        header.setBriefing(data.getCourse().getBriefing());
        header.setAttachments(data.getCourse().getAttachments());
        header.setCourseStatus(data.getCourse().getCourseStatus());
        list.add(header);

        if (data.getCourse().getLecturerInfos() != null) {
            for (int i = 0; i < data.getCourse().getLecturerInfos().size(); i++) {
                LecturerInfosBean lBean = data.getCourse().getLecturerInfos().get(i);
                lBean.setMyDataType(CourseDetailItemBean.lecturer);
                list.add(lBean);
            }
        }
        if (data.getCourse().getAttachmentInfos() != null) {
            for (int i = 0; i < data.getCourse().getAttachmentInfos().size(); i++) {
                AttachmentInfosBean aBean = data.getCourse().getAttachmentInfos().get(i);
                aBean.setMyDataType(CourseDetailItemBean.attachment);
                list.add(aBean);
            }
        }
        if (data.getInteractSteps() != null) {
            for (int i = 0; i < data.getInteractSteps().size(); i++) {
                InteractStepsBean iBean = data.getInteractSteps().get(i);
                iBean.setMyDataType(CourseDetailItemBean.interact);
                list.add(iBean);
            }
        }

        return list;
    }

    private String getTeacher() {
        StringBuffer result = new StringBuffer();
        if (data.getCourse().getLecturerInfos() != null) {
            for (int i = 0; i < data.getCourse().getLecturerInfos().size(); i++) {
                LecturerInfosBean lBean = data.getCourse().getLecturerInfos().get(i);
                result.append(lBean.getLecturerName());
                if (i < data.getCourse().getLecturerInfos().size() - 1) {
                    result.append(",");
                }
            }
        }
        return result.toString();
    }
}
