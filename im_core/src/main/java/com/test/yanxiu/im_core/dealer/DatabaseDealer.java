package com.test.yanxiu.im_core.dealer;

import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

/**
 * Created by cailei on 06/03/2018.
 */

public class DatabaseDealer {
    // 每个用户用自己不同的数据库，db_<userId>作为数据库名
    public static void useDbForUser(String userId) {
        if (userId == null) {
            return;
        }

        LitePalDB db = new LitePalDB("db_" + userId, 1);
        db.addClassName(DbMember.class.getName());
        db.addClassName(DbTopic.class.getName());
        db.addClassName(DbMsg.class.getName());
        LitePal.use(db);
    }
}
