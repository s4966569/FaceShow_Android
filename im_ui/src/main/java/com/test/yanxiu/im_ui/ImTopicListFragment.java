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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.test.yanxiu.common_base.utils.SharedSingleton;
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
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
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


public class ImTopicListFragment extends FaceShowBaseFragment {
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
        startMqttService();
        setupView(v);
        setupData();
        return v;
    }

    @Override
    public void onDestroyView() {
        stopMqttService();
        super.onDestroyView();
    }

    public void onMsgListActivityReturned() {
        // 如果curTopic中有新msg则，排到最前
        long latestMsgTime = curTopic.latestMsgTime;
        for (DbMsg dbMsg : curTopic.mergedMsgs) {
            if (dbMsg.getSendTime() > latestMsgTime) {
                topics.remove(curTopic);
                topics.add(0, curTopic);
            }
        }

        curTopic = null;
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
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
                LitePal.deleteDatabase(Long.toString(Constants.imId) + "_db");
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
        TopicListAdapter adapter = new TopicListAdapter(getContext(), topics);
        mTopicListRecyclerView.setAdapter(adapter);
        adapter.setmOnItemClickCallback(onDbTopicCallback);
    }

    private void setupData() {
        // 为了不丢消息，上来就启动Mqtt
        updateTopicsFromDb();
        updateTopicsFromHttpWithoutMembers();
    }

    // 1，从DB列表生成
    private void updateTopicsFromDb() {
        DatabaseDealer.useDbForUser(Long.toString(Constants.imId) + "_db");


        topics.addAll(DatabaseDealer.topicsFromDb());
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    // 2，从Http获取用户的topic列表，不包含members，完成后继续从Http获取需要更新的topic的信息
    private void updateTopicsFromHttpWithoutMembers() {
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = Constants.imToken;
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
        List<String> idTopicsNeedUpdateMember = new ArrayList<>(); // 因为可能有新的，所以只能用topicId
        List<DbTopic> topicsNotNeedUpdateMember = new ArrayList<>();

        // 所有不在DB中的，以及所有在DB中但change不等于topicChange的topics
        for (ImTopic imTopic : ret.data.topic) {
            boolean needUpdateMembers = true;
            for (DbTopic dbTopic : topics) {
                if (dbTopic.getTopicId() == imTopic.topicId) {
                    dbTopic.latestMsgId = imTopic.latestMsgId;
                    dbTopic.latestMsgTime = imTopic.latestMsgTime;
                    if (dbTopic.getChange().equals(imTopic.topicChange)) {
                        needUpdateMembers = false;
                        topicsNotNeedUpdateMember.add(dbTopic);
                        break;
                    }

                }
            }

            if (needUpdateMembers) {
                idTopicsNeedUpdateMember.add(Long.toString(imTopic.topicId));
            }
        }

        // 4，对于不需要更新members的topic，直接更新msgs即可
        updateEachTopicMsgs(topicsNotNeedUpdateMember);

        if (idTopicsNeedUpdateMember.size() == 0) {
            return;
        }

        // 组成,分割的字符串
        StringBuilder sb = new StringBuilder();
        String sep = ",";
        for(String topicId : idTopicsNeedUpdateMember){
            sb.append(topicId);
            sb.append(",");
        }
        String strTopicIds = sb.toString();
        strTopicIds = strTopicIds.substring(0, strTopicIds.length() - sep.length());

        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = Constants.imToken;
        getTopicsRequest.topicIds = strTopicIds;
        getTopicsRequest.startRequest(TopicGetTopicsResponse.class, new HttpCallback<TopicGetTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetTopicsResponse ret) {
                // 更新数据库
                List<DbTopic> topicsNeedUpdateMember = new ArrayList<>();

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
                    topicsNeedUpdateMember.add(dbTopic);
                }

                // 更新UI, 需要重新排列么？
                // Collections.sort(topics, topicComparator);
                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

                // 4，对于需要更新members的topic，等待更新完members，再去取msgs
                updateEachTopicMsgs(topicsNeedUpdateMember);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 4，依次更新topic的最新一页数据，并更新数据库，然后更新UI
    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();
    private void updateEachTopicMsgs(List<DbTopic> topics) {
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
                dbTopic.latestMsgId = dbMsg.getMsgId();
                return;
            }
        }

        GetTopicMsgsRequest getTopicMsgsRequest = new GetTopicMsgsRequest();
        getTopicMsgsRequest.imToken = Constants.imToken;
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
                    DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "http", Constants.imId);
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

                binder.getService().setmMqttServiceCallback(new MqttService.MqttServiceCallback() {
                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onConnect() {
                        for (DbTopic dbTopic : topics) {
                            binder.subscribe(Long.toString(dbTopic.getTopicId()));
                        }
                    }
                });
                binder.init();
                binder.connect();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                SrtLogger.log("im mqtt", "mqtt disconnectted");
            }
        }, BIND_AUTO_CREATE);

        EventBus.getDefault().register(this);
    }

    private void stopMqttService() {
        // TBD:cailei 这里需要仔细研究下unbind service时机，直接加上会crash
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", Constants.imId);

        // 如果是当前topic，不在这里更新，而在msgs界面更新
        if ((curTopic != null) && (curTopic.getTopicId() == msg.topicId)) {
            return;
        }

        // mqtt上的实时消息，按照接收顺序写入ui的datalist
        // mqtt不更新latestMsg，只有从http确认的消息才更新latestMsg，所以下次进来还是回去http拉取最新页消息
        for (DbTopic dbTopic : topics) {
            if (dbTopic.getTopicId() == msg.topicId) {
                dbTopic.mergedMsgs.add(0, dbMsg);
                dbTopic.showDot = true;
                break;
            }
        }

        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }
    //endregion

    //region 跳转
    private OnRecyclerViewItemClickCallback<DbTopic> onDbTopicCallback = new OnRecyclerViewItemClickCallback<DbTopic>() {
        @Override
        public void onItemClick(int position, DbTopic dbTopic) {
            SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
            Intent i = new Intent(getActivity(), ImMsgListActivity.class);
            getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
            curTopic = dbTopic;
            dbTopic.showDot = false;
        }
    };
    //endregion
}
