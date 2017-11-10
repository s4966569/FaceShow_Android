package com.yanxiu.gphone.faceshow.getui;

import java.io.Serializable;

/**
 * 个推消息透传返回的数据结构
 * Created by frc on 17-10-23.
 */

public class GTPayloadBean implements Serializable {


    /**
     * appId : 1
     * content : 啊啊啊啊啊
     * extendInfo : {"extendInfoOne":"One"}
     * extendOne : clazId
     * id : 0
     * objectId : noticeId
     * title :
     * type : 2
     * userId : 23246747
     */

    private int appId;
    private String content;
    private ExtendInfoBean extendInfo;
    private String extendOne;
    private int id;
    private String objectId;
    private String title;
    private int type;
    private int userId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ExtendInfoBean getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(ExtendInfoBean extendInfo) {
        this.extendInfo = extendInfo;
    }

    public String getExtendOne() {
        return extendOne;
    }

    public void setExtendOne(String extendOne) {
        this.extendOne = extendOne;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static class ExtendInfoBean {
        /**
         * extendInfoOne : One
         */

        private String extendInfoOne;

        public String getExtendInfoOne() {
            return extendInfoOne;
        }

        public void setExtendInfoOne(String extendInfoOne) {
            this.extendInfoOne = extendInfoOne;
        }
    }
}
