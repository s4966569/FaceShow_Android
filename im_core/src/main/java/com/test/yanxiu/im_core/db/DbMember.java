package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbMember extends DataSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private long imId;
    private String name;
    private String avatar;

    private List<DbTopic> topics;   // 表明此用户加入了哪些topic

    //region getter setter
    public long getImId() {
        return imId;
    }

    public void setImId(long imId) {
        this.imId = imId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<DbTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<DbTopic> topics) {
        this.topics = topics;
    }
    //endregion
}
