package com.yanxiu.gphone.faceshow.classcircle.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:53.
 * Function :
 */
public class CommentResponse extends FaceShowBaseResponse {

    public Data data;

    public class Data{
        public String id;
        public String clazsId;
        public String momentId;
        public String parentId;
        public String content;
        /**
         * 1为评论，2为回复
         * */
        public String level;
        public String createTime;
        /**
         * 评论人
         * */
        public Publisher publisher;
        /**
         * 被评论人
         * */
        public Publisher toUser;
    }

}
