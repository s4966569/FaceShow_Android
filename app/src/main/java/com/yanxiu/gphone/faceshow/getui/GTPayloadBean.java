package com.yanxiu.gphone.faceshow.getui;

import java.io.Serializable;

/**
 * 个推消息透传返回的数据结构
 * Created by frc on 17-10-23.
 */

public class GTPayloadBean implements Serializable {

    /**
     * content : 收到一条新通知，请及时查看
     * extendOne : clazId
     * extendInfo : {"extendInfoOne":"One"}
     * objectId : 1
     * title :
     * type : 101
     * userId : 1111111
     */

    private String content;
    private String extendOne;
    private ExtendInfoBean extendInfo;
    private String objectId;
    private String title;
    private int type;
    private int userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtendOne() {
        return extendOne;
    }

    public void setExtendOne(String extendOne) {
        this.extendOne = extendOne;
    }

    public ExtendInfoBean getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(ExtendInfoBean extendInfo) {
        this.extendInfo = extendInfo;
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
