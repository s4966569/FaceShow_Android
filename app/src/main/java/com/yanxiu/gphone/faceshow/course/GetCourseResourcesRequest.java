package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-9.
 */

public class GetCourseResourcesRequest extends FaceShowBaseRequest {
    private String method = "app.manage.resource.courseResources";
    public String courseId;

    @Override
    protected String urlPath() {
        return null;
    }
}
