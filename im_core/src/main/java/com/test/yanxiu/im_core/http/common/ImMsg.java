package com.test.yanxiu.im_core.http.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cailei on 05/03/2018.
 */

public class ImMsg {
    @SerializedName("id")
    public long msgId;
    public long topicId;
    public long senderId;
    public int contentType;
    public long sendTime;
    public ContentData contentData;

    public class ContentData {
        public String msg;
        public String thumbnail;
        public String viewUrl;
    }
}
