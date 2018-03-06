package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbMember extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private long imId;
    private String name;
    private String avatar;

    private List<DbTopic> topics;   // 表明此用户加入了哪些topic
}
