package com.yanxiu.gphone.faceshow.classcircle.response;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:09.
 * Function :
 */
public class ClassCircleMock {

    public String name;
    public String headimg;
    public String content;
    public String time;
    public List<String> imgUrls;
    public List<ThumbUp> thumbs;
    public List<Comment> comments;

    public class ThumbUp{
        public String userId;
        public String userName;
    }

    public class Comment{
        public String userId;
        public String userName;
        public String toUserId;
        public String toUserName;
        public String content;
    }
}
