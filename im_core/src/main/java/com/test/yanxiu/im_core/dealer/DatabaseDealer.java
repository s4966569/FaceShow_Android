package com.test.yanxiu.im_core.dealer;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */

public class DatabaseDealer {
    // 每个用户用自己不同的数据库，db_<userId>作为数据库名
    public static void useDbForUser(String userId) {
        if (userId == null) {
            Logger.e("error: create db for " + userId);
            return;
        }

        LitePalDB db = new LitePalDB("db_" + userId, 1);
        db.addClassName(DbMember.class.getName());
        db.addClassName(DbTopic.class.getName());
        db.addClassName(DbMsg.class.getName());
        db.addClassName(DbMyMsg.class.getName());
        LitePal.use(db);
    }


    public static void genMockData() {
        DbMember memberA = new DbMember();
        memberA.setImId(111111);

        DbMember memberB = new DbMember();
        memberB.setImId(222222);

        DbMember memberC = new DbMember();
        memberC.setImId(333333);

        DbMember memberM = new DbMember();
        memberM.setImId(454545);

        // 设置 topic 1
        DbTopic t1 = new DbTopic();
        t1.setTopicId(1);

        t1.getMembers().add(memberA);
        t1.getMembers().add(memberM);

        DbMsg m11 = new DbMsg();
        m11.setReqId("m11");
        m11.setTopicId(t1.getTopicId());
        m11.setSenderId(memberA.getImId());
        m11.setSendTime(1);

        DbMyMsg my1 = new DbMyMsg();
        my1.setReqId("my1");
        my1.setTopicId(t1.getTopicId());
        my1.setSenderId(memberM.getImId());
        my1.setSendTime(2);

        DbMsg m12 = new DbMsg();
        m12.setReqId("m12");
        m12.setTopicId(t1.getTopicId());
        m12.setSenderId(memberA.getImId());
        m12.setSendTime(5);

        DbMyMsg my2 = new DbMyMsg();
        my2.setReqId("my2");
        my2.setTopicId(t1.getTopicId());
        my2.setSenderId(memberM.getImId());
        my2.setSendTime(9);


        memberA.save();
        memberB.save();
        memberC.save();
        memberM.save();
        t1.save();
        m11.save();
        my1.save();
        m12.save();
        my2.save();

//        //t1.setFirstMsg(m11);
//        t1.save();
//        t2.save();
//        t3.save();
//        m11.save();
//        m12.save();
//        m21.save();
//        m31.save();
//        m32.save();
//        m33.save();
    }

    public static List<DbTopic> topicsFromDb() {
        List<DbMyMsg> myMsgs = DataSupport.findAll(DbMyMsg.class, true);
        List<DbMsg> msgs = DataSupport.findAll(DbMsg.class, true);


        List<DbTopic> topics = DataSupport.findAll(DbTopic.class, true);
//        List<DbTopic> topics = DataSupport.select("topicid", "dbmsg_id").find(DbTopic.class, true);
//        Collections.sort(topics, new Comparator<DbTopic>() {
//            @Override
//            public int compare(DbTopic a, DbTopic b) {
//                if (a.getLastMsg().getSendTime() > b.getLastMsg().getSendTime()) {
//                    return 1;
//                }
//
//                if (a.getLastMsg().getSendTime() < b.getLastMsg().getSendTime()) {
//                    return -1;
//                }
//
//                return 0;
//            }
//        });

        // 排序后应该为 T3, T1, T2
        return topics;
    }
}
