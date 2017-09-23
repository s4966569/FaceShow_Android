package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/23 15:04.
 * Function :
 */
public class CourseDiscussLikeRequest extends FaceShowBaseRequest {

    public String method="interact.likeCommentRecord";
    public String commentRecordId;

    @Override
    protected String urlPath() {
        return null;
    }
}
