package com.test.yanxiu.im_core.dealer;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cailei on 06/03/2018.
 */
// litepal已知问题列表
// 1, 只能两表关联，且不支持多个外键（会重名）
// 2, ClusterQuery中每种类型的只能有一次，连续.where两次，则覆盖

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
        m11.setMsgId(1);

        DbMyMsg my1 = new DbMyMsg();
        my1.setReqId("my1");
        my1.setTopicId(t1.getTopicId());
        my1.setSenderId(memberM.getImId());
        my1.setSendTime(2);
        my1.setMsgId(1);

        DbMsg m12 = new DbMsg();
        m12.setReqId("m12");
        m12.setTopicId(t1.getTopicId());
        m12.setSenderId(memberA.getImId());
        m12.setSendTime(5);
        m12.setMsgId(4);

        DbMyMsg my2 = new DbMyMsg();
        my2.setReqId("my2");
        my2.setTopicId(t1.getTopicId());
        my2.setSenderId(memberM.getImId());
        my2.setSendTime(9);
        my2.setMsgId(4);

        // 设置 topic 2
        DbTopic t2 = new DbTopic();
        t2.setTopicId(2);

        DbMsg m21 = new DbMsg();
        m21.setReqId("m21");
        m21.setTopicId(t2.getTopicId());
        m21.setSenderId(memberB.getImId());
        m21.setSendTime(3);
        m21.setMsgId(2);

        // 设置 topic 3
        DbTopic t3 = new DbTopic();
        t3.setTopicId(3);

        DbMsg m31 = new DbMsg();
        m31.setReqId("m31");
        m31.setTopicId(t3.getTopicId());
        m31.setSenderId(memberA.getImId());
        m31.setSendTime(4);
        m31.setMsgId(3);

        DbMsg m32 = new DbMsg();
        m32.setReqId("m32");
        m32.setTopicId(t3.getTopicId());
        m32.setSenderId(memberB.getImId());
        m32.setSendTime(6);
        m32.setMsgId(6);

        DbMyMsg my3 = new DbMyMsg();
        my3.setReqId("my3");
        my3.setTopicId(t3.getTopicId());
        my3.setSenderId(memberM.getImId());
        my3.setSendTime(7);
        my3.setMsgId(6);

        DbMsg m33 = new DbMsg();
        m33.setReqId("m33");
        m33.setTopicId(t3.getTopicId());
        m33.setSenderId(memberC.getImId());
        m33.setSendTime(8);
        m33.setMsgId(8);

        memberA.save();
        memberB.save();
        memberC.save();
        memberM.save();

        t1.save();
        m11.save();
        my1.save();
        m12.save();
        my2.save();

        t2.save();
        m21.save();

        t3.save();
        m31.save();
        m32.save();
        my3.save();
        m33.save();
    }

    /**
     * 获取此topic的从startMsgId开始的DbMsg中count条数据以及DbMyMsg中相关的数据，msgId值大的在前
     * @param startMsgId : 最大的msgId. 传-1为从最近一条msg开始
     * @param count : DbMsg中的消息数
     * @return 返回merge后的数组，如果数据足够，数组size() >= count，如果数组size()小于count值，则表明已经取完所有数据
     */
    public static List<DbMsg> getTopicMsgs(long topicId, long startMsgId, int count) {
        ClusterQuery query = null;
        if (startMsgId == -1) {
            query = DataSupport.
                    where("topicId = ?", Long.toString(topicId))
                    .limit(count)
                    .order("msgid desc");   // server按照msgId插入，这里也可以用sendtime
        } else {
            query = DataSupport.
                    where("topicId = ? and msgid <= ?",
                            Long.toString(topicId),
                            Long.toString(startMsgId))
                    .limit(count)
                    .order("msgid desc");   // server按照msgId插入，这里也可以用sendtime
        }

        List<DbMsg> otherMsgs = query.find(DbMsg.class);

        if (otherMsgs.size() > 0) {
            long myStartId = otherMsgs.get(0).getMsgId();                   // 大的
            long myEndId = otherMsgs.get(otherMsgs.size() - 1).getMsgId();  // 小的
            List<DbMyMsg> myMsgs = DataSupport
                    .where("topicId = ? and msgid <= ? and msgid >= ?",
                            Long.toString(topicId),
                            Long.toString(myStartId),
                            Long.toString(myEndId))
                    .order("sendtime desc")
                    .find(DbMyMsg.class);

            if (myMsgs.size() > 0) {
                // 需要merge进总queue
                merge(otherMsgs, myMsgs);
            }
        }
        return otherMsgs;
    }

    private static void merge(List<DbMsg> otherMsgs, List<DbMyMsg> myMsgs) {
        int i = 0, j = 0;
        while (i < otherMsgs.size() && j < myMsgs.size()) {
            DbMsg otherMsg = otherMsgs.get(i);
            DbMyMsg myMsg = myMsgs.get(j);
            if (otherMsg.getMsgId() > myMsg.getMsgId()) {
                i++;
            } else {
                // 倒叙则先插 本地send的msg，然后是网上获得的
                otherMsgs.add(i, myMsg);
                i++;
                j++;
            }
        }

        while (j < myMsgs.size()) {
            DbMyMsg myMsg = myMsgs.get(j);
            otherMsgs.add(myMsg);
            j++;
        }
    }

    /**
     * 从数据库重建Topic List，每条topic带最新一页10条msgs，且topic list按照topic里最新的msg排序
     */
    public static List<DbTopic> topicsFromDb() {
        List<DbTopic> topics = DataSupport.findAll(DbTopic.class, true);
        for (DbTopic topic : topics) {
            List<DbMsg> msgs = getTopicMsgs(topic.getTopicId(), -1, 10);
            topic.mergedMsgs = msgs;
        }

        Collections.sort(topics, new Comparator<DbTopic>() {
            @Override
            public int compare(DbTopic t1, DbTopic t2) {
                long t1Time = t1.mergedMsgs.get(0).getSendTime();
                long t2Time = t2.mergedMsgs.get(0).getSendTime();
                if (t1Time < t2Time) {
                    return 1;
                }
                if (t1Time > t2Time) {
                    return -1;
                }
                return 0;
            }
        });

        return topics;
    }
}
