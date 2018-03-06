package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbMsg extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    public long msgId;
    public DbTopic topic;       // 此msg所属的topic
    public DbMember sender;     // 此msg的owner
    public long sendTime;       // msg的发送时间

    public int contentType;     // 目前为txt，未来支持pic
    public String msg;         // txt怎插入msg，pic插入
    public String thumbnail;
    public String viewUrl;
}
