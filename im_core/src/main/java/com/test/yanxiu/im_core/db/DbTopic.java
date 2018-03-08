package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbTopic extends DataSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private long topicId;
    private String name;
    private String type;

    private List<DbMember> members = new ArrayList<>();

    // 只为UI显示用，不做数据库存储用
    @Column(ignore = true)
    public long latestMsgId;
    @Column(ignore = true)
    public List<DbMsg> mergedMsgs;

    //region getter setter
    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DbMember> getMembers() {
        return members;
    }

    public void setMembers(List<DbMember> members) {
        this.members = members;
    }
    //endregion
}
