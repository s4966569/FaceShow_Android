package com.test.yanxiu.im_core.http;

import com.test.yanxiu.im_core.http.common.ImTopic;

import java.util.List;

/**
 * Created by cailei on 05/03/2018.
 */

public class TopicCreateTopicResponse extends ImResponseBase {
    public Data data;

    public class Data {
        public long imEvent;
        public String reqId;
        // 还有一堆不确定干什么用，暂时没写
        public List<ImTopic> topic;
        public String topicMsge;    // 没啥用?
    }
}
