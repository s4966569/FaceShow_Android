package com.test.yanxiu.im_ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.http.ImRequestBase;
import com.test.yanxiu.im_core.http.ImResponseBase;
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
import com.test.yanxiu.im_core.http.TopicCreateTopicRequest;
import com.test.yanxiu.im_core.http.TopicCreateTopicResponse;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends FaceShowBaseFragment {


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        test();
        //test01();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
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
}
