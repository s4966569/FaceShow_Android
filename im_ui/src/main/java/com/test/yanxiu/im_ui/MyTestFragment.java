package com.test.yanxiu.im_ui;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.LoginAppRequest;
import com.test.yanxiu.im_core.http.LoginAppResponse;
import com.test.yanxiu.im_core.http.MemberGetMembersRequest;
import com.test.yanxiu.im_core.http.MemberGetMembersResponse;
import com.test.yanxiu.im_core.http.OnlineHeartbeatRequest;
import com.test.yanxiu.im_core.http.OnlineHeartbeatResponse;
import com.test.yanxiu.im_core.http.PolicyConfigRequest;
import com.test.yanxiu.im_core.http.PolicyConfigResponse;
import com.test.yanxiu.im_core.http.PolicyMqttServerRequest;
import com.test.yanxiu.im_core.http.PolicyMqttServerResponse;
import com.test.yanxiu.im_core.http.SaveTextMsgRequest;
import com.test.yanxiu.im_core.http.SaveTextMsgResponse;
import com.test.yanxiu.im_core.http.TopicCreateTopicRequest;
import com.test.yanxiu.im_core.http.TopicCreateTopicResponse;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_core.mqtt.MqttService;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class MyTestFragment extends FaceShowBaseFragment {
    Button btn;

    public MyTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //test();
        //test01();
        //testMqtt();
        testRequestRety();

        testDb();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_test, container, false);
        btn = v.findViewById(R.id.send_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTextMsgRequest r = new SaveTextMsgRequest();
                r.imToken = "fb1a05461324976e55786c2c519a8ccc";
                r.topicId = "16";
                r.msg = "from android";
                r.startRequest(SaveTextMsgResponse.class, new HttpCallback<SaveTextMsgResponse>() {
                    @Override
                    public void onSuccess(RequestBase request, SaveTextMsgResponse ret) {
                        Log.d("Tag", "success");
                    }

                    @Override
                    public void onFail(RequestBase request, Error error) {
                        Log.d("Tag", "fail");
                    }
                });
            }
        });
        return v;
    }

    private void test() {
        // 1.1 获取通用配置
        PolicyConfigRequest r = new PolicyConfigRequest();
        r.startRequest(PolicyConfigResponse.class, new HttpCallback<PolicyConfigResponse>() {
            @Override
            public void onSuccess(RequestBase request, PolicyConfigResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 1.2 获取mqtt服务器配置
        PolicyMqttServerRequest r1 = new PolicyMqttServerRequest();
        r1.startRequest(PolicyMqttServerResponse.class, new HttpCallback<PolicyMqttServerResponse>() {
            @Override
            public void onSuccess(RequestBase request, PolicyMqttServerResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 1.1 app登录接口
        LoginAppRequest r2 = new LoginAppRequest();
        r2.bizToken = "ce0d56d0d8a214fb157be3850476ecb5";
        r2.startRequest(LoginAppResponse.class, new HttpCallback<LoginAppResponse>() {
            @Override
            public void onSuccess(RequestBase request, LoginAppResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 2.2 获取im用户信息
        MemberGetMembersRequest r5 = new MemberGetMembersRequest();
        r5.imToken = "755f55fdf917fc7c61e62946ca7acbd1";
        r5.imMemberIds = "7,25";
        r5.startRequest(MemberGetMembersResponse.class, new HttpCallback<MemberGetMembersResponse>() {
            @Override
            public void onSuccess(RequestBase request, MemberGetMembersResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 2.3 im当前用户心跳
        OnlineHeartbeatRequest r4 = new OnlineHeartbeatRequest();
        r4.onlineSeconds = "600";
        r4.startRequest(OnlineHeartbeatResponse.class, new HttpCallback<OnlineHeartbeatResponse>() {
            @Override
            public void onSuccess(RequestBase request, OnlineHeartbeatResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 1.1 创建主题
        TopicCreateTopicRequest r6 = new TopicCreateTopicRequest();
        r6.topicType = "1";
        r6.imToken = "fb1a05461324976e55786c2c519a8ccc";
        r6.imMemberIds = "9,29";
        r6.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 1.5 获取多个主题
        TopicGetTopicsRequest r7 = new TopicGetTopicsRequest();
        r7.imToken = "fb1a05461324976e55786c2c519a8ccc";
        r7.topicIds = "16";
        r7.startRequest(TopicCreateTopicResponse.class, new HttpCallback<TopicCreateTopicResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicCreateTopicResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });

        // 1.6 获取当前用户的主题列表
        TopicGetMemberTopicsRequest r8 = new TopicGetMemberTopicsRequest();
        r8.imToken = "fb1a05461324976e55786c2c519a8ccc";
        r8.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    private void test01() {
        final RequestQueueHelper q = new RequestQueueHelper();

        MemberGetMembersRequest r1 = new MemberGetMembersRequest();
        r1.imToken = "755f55fdf917fc7c61e62946ca7acbd1";
        r1.imMemberIds = "7";

        LoginAppRequest r2 = new LoginAppRequest();
        r2.bizToken = "ce0d56d0d8a214fb157be3850476ecb5";

        q.addRequest(r1, MemberGetMembersResponse.class, new HttpCallback<MemberGetMembersResponse>() {
            @Override
            public void onSuccess(RequestBase request, MemberGetMembersResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });


        q.addRequest(r2, LoginAppResponse.class, new HttpCallback<LoginAppResponse>() {
            @Override
            public void onSuccess(RequestBase request, LoginAppResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    private MqttService.MqttBinder binder = null;
    private void testMqtt() {
        Intent intent = new Intent(getActivity(), MqttService.class);
        getActivity().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d("Tag", "MqttService connected");
                binder = (MqttService.MqttBinder) iBinder;

                binder.init();
                binder.connect();
                //binder.subscribe("16");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("Tag", "MqttService disconnected");
            }
        }, BIND_AUTO_CREATE);

        // TBD:cailei 什么时候unbind service

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg);
        // 找出对应的topic，加入并更新UI
        for (DbTopic topic : topics) {
            if (topic.getTopicId() == msg.topicId) {
                topic.mergedMsgs.add(dbMsg);
            }

            if (dbMsg.getMsgId() > topic.latestMsgId) {
                topic.latestMsgId = dbMsg.getMsgId();
            }
        }

        // TBD:cailei 更新topic对应列的UI
    }

    private void testDb() {
        DatabaseDealer.useDbForUser("cailei");
        DatabaseDealer.genMockData();

        // 验证msgs合并
        List<DbMsg> msgs1 = DatabaseDealer.getTopicMsgs(1, 1, 10);
        List<DbMsg> msgs2 = DatabaseDealer.getTopicMsgs(1, 4, 10);
        List<DbMsg> msgs3 = DatabaseDealer.getTopicMsgs(1, 4, 1);


        // 验证topics最近更新，顺序正确
        List<DbTopic> topics = DatabaseDealer.topicsFromDb();


        List<String> arr = new ArrayList<>();

        Logger.d("done");
    }

    private String imToken = "fb1a05461324976e55786c2c519a8ccc";
    private long imId = 9;
    private List<DbTopic> topics;       // UI根据此数据结构显示
    private void updateTopicList() {
        // 1, db : 从数据库重建topic list
        topics = DatabaseDealer.topicsFromDb();

        // 2, http : 获取当前用户的topic列表
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = imToken;
        getMemberTopicsRequest.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                // 只请求DB中没有的
                List<String> ids = new ArrayList<>();
                for (ImTopic topic : ret.data.topic) {
                    ids.add(Long.toString(topic.topicId));
                }
                for (DbTopic topic : topics) {
                    ids.remove(Long.toString(topic.getTopicId()));
                }

                // 3, http : 获取topics中的members信息
                updateTopicsList_updateTopicsMembers(ids);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 3, http : 获取topics中的members信息
    private void updateTopicsList_updateTopicsMembers(List<String> topicIds) {
        if ((topicIds == null) || (topicIds.size() <=0)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        String sep = ",";
        for(String topicId : topicIds){
            sb.append(topicId);
            sb.append(",");
        }
        String strTopicIds = sb.toString();
        strTopicIds = strTopicIds.substring(0, strTopicIds.length() - sep.length());

        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = imToken;
        getTopicsRequest.topicIds = strTopicIds;
        getTopicsRequest.startRequest(TopicGetTopicsResponse.class, new HttpCallback<TopicGetTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetTopicsResponse ret) {
                // 4, 更新数据库topic下的members
                for (ImTopic topic : ret.data.topic) {
                    updateTopicMembersDb(topic);
                }



                // 5, 依次请求更新topic的msgs

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 4, 更新数据库topic下的members
    private void updateTopicMembersDb(ImTopic topic) {
        DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(topic);
        // 顺便加入topic列表
        topics.add(dbTopic);
    }

    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();

    // 5, 依次请求更新topic的msgs
    private void updateTopicMsgsForAllTopics() {
        totalRetryTimes = 10;
        for (final DbTopic dbTopic : topics) {
            doRequest(dbTopic);
        }
    }

    private void doRequest(final DbTopic dbTopic) {
        GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
        getTopicMsgsRequest.imToken = imToken;
        getTopicMsgsRequest.topicId = Long.toString(dbTopic.getTopicId());
        getTopicMsgsRequest.startId = Long.toString(dbTopic.latestMsgId);
        rqHelper.addRequest(getTopicMsgsRequest, GetTopicMsgsResponse.class, new HttpCallback<GetTopicMsgsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTopicMsgsResponse ret) {
                // 更新DB，然后通知更新UI
                for (ImMsg msg : ret.data.topicMsg) {
                    DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg);
                    if (dbMsg == null) continue;    // 我发的消息
                    dbTopic.mergedMsgs.add(dbMsg);
                    if (dbMsg.getMsgId() > dbTopic.latestMsgId) {
                        dbTopic.latestMsgId = dbMsg.getMsgId();
                    }
                }

                // 还是不能排序，会打乱之前的顺序导致跳动，在逻辑完善前，先到先显示吧
                // Collections.sort(dbTopic.mergedMsgs, DatabaseDealer.msgComparator);

                // TBD:cailei 通知UI刷新

                // 如果还有更多页，请求更多
                if (ret.data.topicMsg.size() >= DatabaseDealer.pagesize)
                doRequest(dbTopic);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (totalRetryTimes-- <= 0) {
                    return;
                }
                doRequest(dbTopic);
            }
        });
    }

    private void testRequestRety() {
        DbTopic dbTopic = new DbTopic();
        dbTopic.setTopicId(16);
        dbTopic.latestMsgId = 0;
        totalRetryTimes = 10;

        doRequest(dbTopic);
    }
}


