package com.test.yanxiu.im_ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.dealer.MqttProtobufDealer;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.im_core.mqtt.MqttService;
import com.test.yanxiu.im_ui.activity.ChatRoomActivity;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.test.yanxiu.im_core.dealer.DatabaseDealer.topicComparator;


public class ImTopicListFragment extends FaceShowBaseFragment implements TopicListAdapter.OnRecyclerViewItemClickListener {
    private ImTitleLayout mTitleLayout;
    private ImageView mNaviLeftImageView;
    private TextView mNaviRightTextView;
    private RecyclerView mTopicListRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<DbTopic> topics = new ArrayList<>();
    public ImTopicListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_list, container, false);
        setupView(v);
        setupData();
        return v;
    }

    @Override
    public void onDestroyView() {
        stopMqttService();
        super.onDestroyView();
    }

    private void setupView(View v) {
        mTitleLayout = v.findViewById(R.id.title_layout);

        // set title
        mTitleLayout.setTitle("聊聊");

        // set navi left
        mNaviLeftImageView = v.findViewById(R.id.navi_left_imageview);
        mNaviLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LitePal.deleteDatabase("db_cailei");
            }
        });
        mTitleLayout.setLeftView(mNaviLeftImageView);

        // set navi right
        mNaviRightTextView = v.findViewById(R.id.navi_right_textview);
        mNaviRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topics != null) {
                    topics.clear();
                    setupData();
                }
            }
        });
        mTitleLayout.setRightView(mNaviRightTextView);

        // set topic list
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mTopicListRecyclerView = v.findViewById(R.id.topic_list_recyclerview);
        mTopicListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        TopicListAdapter adapter = new TopicListAdapter(getContext(), topics,this);
        mTopicListRecyclerView.setAdapter(adapter);
    }

    private void setupData() {
        // 为了不丢消息，上来就启动Mqtt
        startMqttService();

        updateTopicsFromDb();
        updateTopicsFromHttpWithoutMembers();
    }

    private String imToken = "fb1a05461324976e55786c2c519a8ccc";
    private long imId = 9;
    // 1，从DB列表生成
    private void updateTopicsFromDb() {
        DatabaseDealer.useDbForUser("cailei");
        topics.addAll(DatabaseDealer.topicsFromDb());
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    // 2，从Http获取用户的topic列表，不包含members，完成后继续从Http获取需要更新的topic的信息
    private void updateTopicsFromHttpWithoutMembers() {
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = imToken;
        getMemberTopicsRequest.bizId = null;
        getMemberTopicsRequest.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                // 3
                updateTopicsFromHttpAddMembers(ret);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 3，从Http获取需要更新的topic的信息，完成后写入DB，更新UI
    private void updateTopicsFromHttpAddMembers(TopicGetMemberTopicsResponse ret) {
        List<String> ids = new ArrayList<>();
        // 所有不在DB中的，以及所有在DB中但change不等于topicChange的topics
        for (ImTopic imTopic : ret.data.topic) {
            boolean needUpdate = true;
            for (DbTopic dbTopic : topics) {
                if ((dbTopic.getTopicId() == imTopic.topicId) &&
                        dbTopic.getChange().equals(imTopic.topicChange))
                {
                    needUpdate = false;
                    break;
                }
            }

            if (needUpdate) {
                ids.add(Long.toString(imTopic.topicId));
            }
        }

        if (ids.size() == 0) {
            return;
        }

        // 组成,分割的字符串
        StringBuilder sb = new StringBuilder();
        String sep = ",";
        for(String topicId : ids){
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
                // 更新数据库
                for (ImTopic imTopic : ret.data.topic) {
                    DbTopic dbTopic = DatabaseDealer.updateDbTopicWithImTopic(imTopic);
                    dbTopic.latestMsgTime = imTopic.latestMsgTime;
                    dbTopic.latestMsgId = imTopic.latestMsgId;

                    // 更新uiTopics
                    for(Iterator<DbTopic> i = topics.iterator(); i.hasNext();) {
                        DbTopic uiTopic = i.next();
                        if (uiTopic.getTopicId()  == dbTopic.getTopicId()) {
                            i.remove();
                        }
                    }

                    topics.add(dbTopic);
                }

                // 更新UI
                Collections.sort(topics, topicComparator);
                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

                // 4
                updateEachTopicMsgs();
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 4，依次更新topic的最新一页数据，并更新数据库，然后更新UI
    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();
    private void updateEachTopicMsgs() {
        totalRetryTimes = 10;
        for (final DbTopic dbTopic : topics) {
            doGetTopicMsgsRequest(dbTopic);
        }
    };

    private void doGetTopicMsgsRequest(final DbTopic dbTopic) {
        if ((dbTopic.mergedMsgs != null) && (dbTopic.mergedMsgs.size() > 0)) {
            DbMsg dbMsg = dbTopic.mergedMsgs.get(0);
            if (dbMsg.getMsgId() >= dbTopic.latestMsgId) {
                // 数据库中已有最新的msg，不用更新
                // TBD:cailei 这里可以每次更新下最后一页
                dbTopic.latestMsgId = dbMsg.getMsgId();
                return;
            }
        }

        GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
        getTopicMsgsRequest.imToken = imToken;
        getTopicMsgsRequest.topicId = Long.toString(dbTopic.getTopicId());
        getTopicMsgsRequest.startId = Long.toString(dbTopic.latestMsgId);
        getTopicMsgsRequest.order = "desc";
        rqHelper.addRequest(getTopicMsgsRequest, GetTopicMsgsResponse.class, new HttpCallback<GetTopicMsgsResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetTopicMsgsResponse ret) {
                // 有新消息，UI上应该显示红点
                dbTopic.showDot = true;

                // 用最新一页，取代之前的mergedMsgs，来自mqtt的消息不应该删除
                for(Iterator<DbMsg> i = dbTopic.mergedMsgs.iterator(); i.hasNext();) {
                    DbMsg uiMsg = i.next();
                    if (uiMsg.getFrom().equals("mqtt")) {
                        continue;
                    }
                    i.remove();
                }

                for (ImMsg msg : ret.data.topicMsg) {
                    DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "http", imId);
                    dbTopic.mergedMsgs.add(dbMsg);

                    if (dbMsg.getMsgId() > dbTopic.latestMsgId) {
                        dbTopic.latestMsgId = dbMsg.getMsgId();
                    }
                }

                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                // 重试
                if (totalRetryTimes-- <= 0) {
                    return;
                }
                doGetTopicMsgsRequest(dbTopic);
            }
        });
    }

    //region MQTT
    private MqttService.MqttBinder binder = null;
    private DbTopic curTopic = null;    // 当前
    private void startMqttService() {
        Intent intent = new Intent(getActivity(), MqttService.class);
        getActivity().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                SrtLogger.log("im mqtt", "mqtt connectted");
                binder = (MqttService.MqttBinder) iBinder;

                binder.init();
                binder.connect();
                //binder.subscribe("16");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                SrtLogger.log("im mqtt", "mqtt disconnectted");
            }
        }, BIND_AUTO_CREATE);

        EventBus.getDefault().register(this);
    }

    private void stopMqttService() {
        getActivity().unbindService(new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                SrtLogger.log("im mqtt", "mqtt disconnectted");
            }
        });
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", imId);

        // mqtt上的实时消息，按照接收顺序写入ui的datalist
        // mqtt不更新latestMsg，只有从http确认的消息才更新latestMsg，所以下次进来还是回去http拉取最新页消息
        for (DbTopic dbTopic : topics) {
            if (dbTopic.getTopicId() == msg.topicId) {
                dbTopic.mergedMsgs.add(dbMsg);
                break;
            }
        }

        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    //endregion

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
        ChatRoomActivity.invoke(getActivity());
    }
}
