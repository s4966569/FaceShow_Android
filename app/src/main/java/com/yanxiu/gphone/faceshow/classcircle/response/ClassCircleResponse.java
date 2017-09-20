package com.yanxiu.gphone.faceshow.classcircle.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:09.
 * Function :
 */
public class ClassCircleResponse extends FaceShowBaseResponse{


    public Data data;


    public class Data{

        public ArrayList<Moments> moments;

        public class Moments{
            public String id;
            /**
             * 相册图片列表
             * */
            public ArrayList<Album> album;
            public String claszId;
            /**
             * 评论数量，包括一级评论和回复
             * */
            public String commentedNum;
            public ArrayList<Comments> comments;
            public String content;
            /**
             * 点赞数量
             * */
            public String likedNum;
            /**
             * 点赞
             * */
            public ArrayList<Likes> likes;
            public String publishTime;
            public String publishTimeDesc;
            public Publisher publisher;
            /**
             * 阅读数量
             * */
            public String readedNum;

            public class Publisher{
                public String avatar;
                public String realName;
                public String userId;
            }

            public class Likes{
                public String claszId;
                public String createTime;
                public String id;
                public String momentId;
                public Publisher publisher;

                public class Publisher{
                    public String avatar;
                    public String realName;
                    public String userId;
                }
            }

            public class Comments{
                public String claszId;
                /**
                 * 内容
                 * */
                public String content;
                public String createTime;
                public String id;
                /**
                 * 1为评论，2为回复
                 * */
                public String level;
                public String momentId;
                public String parentId;
                /**
                 * 评论人
                 * */
                public Publisher publisher;
                /**
                 * 被评论人
                 * */
                public Publisher toUser;

                public class Publisher{
                    /**
                     * 头像
                     * */
                    public String avatar;
                    public String realName;
                    public String userId;
                }
            }

            public class Album{
                public String id;
                public String momentId;
                /**
                 * 图片详细信息
                 * */
                public Attachment attachment;

                public class Attachment{
                    public String downloadUrl;
                    public String previewUrl;
                    public String resId;
                    public String resName;
                    public String resType;
                }
            }
        }
    }
}
