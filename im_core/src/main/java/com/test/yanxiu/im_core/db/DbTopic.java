package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbTopic extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private long topicId;
    private String name;
    private String type;

    private DbMsg lastMsg;          // 最后一条消息
    private List<DbMsg> msgs;       // 属于此Topic的所有Msg，分页

    public List<DbMember> members;  // 加入此Topic的所有Member，不分页
}
