package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-8.
 */

public class GetCourseRequest extends FaceShowBaseRequest {
    private String method = "app.manage.course.getCourse";
    public String courseId;

    @Override
    protected String urlPath() {
        return null;
    }
}
