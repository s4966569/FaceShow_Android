package com.test.yanxiu.im_ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMember;
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
import com.test.yanxiu.im_ui.contacts.ContactsActivity;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;


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

    public MqttService.MqttBinder getBinder() {
        return binder;
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
        for (DbTopic topic : msgShownTopics) {
            // 最多保留20条
            topic.setShowDot(false);
            topic.mergedMsgs = topic.mergedMsgs.subList(0, Math.min(DatabaseDealer.pagesize, topic.mergedMsgs.size()));

            // 有新消息则要放置到最前
            long latestMsgTime = topic.latestMsgTime;

            // 因为mqtt新增topic，和http新增topic，不确定哪个先返回，所以需要用msg activity里的topic替换掉topic fragment的
            for (DbTopic dbTopic : topics) {
                if (dbTopic.getTopicId() == topic.getTopicId()) {
                    topics.set(topics.indexOf(dbTopic), topic);
                }
            }


            for (DbMsg msg : topic.mergedMsgs) {
                if (msg.getSendTime() > latestMsgTime) {
                    // 放置到最前
                    topics.remove(topic);
                    topics.add(0, topic);
                }
            }
        }

        rearrangeTopics(); // 重新排列群聊、私聊

        curTopic = null;
        msgShownTopics.clear();
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
                if (titleActionCallback != null) {
                    titleActionCallback.onLeftImgClicked();
                }
                LitePal.deleteDatabase(Long.toString(Constants.imId) + "_db");
            }
        });
        mTitleLayout.setLeftView(mNaviLeftImageView);

        // set navi right
        mNaviRightTextView = v.findViewById(R.id.navi_right_textview);
        mNaviRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImTopicListFragment.this.getContext(), ContactsActivity.class);
                getActivity().startActivityForResult(intent, Constants.IM_REQUEST_CODE_CONTACT);
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
        updateTopicsFromDb();
        // 为了重连等机制，放到mqtt connected以后做http拉取
        // updateTopicsFromHttpWithoutMembers();
    }

    // 1，从DB列表生成
    private void updateTopicsFromDb() {
        DatabaseDealer.useDbForUser(Long.toString(Constants.imId) + "_db");


        topics.addAll(DatabaseDealer.topicsFromDb());

        rearrangeTopics();
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }


    // 2，从Http获取用户的topic列表，不包含members，完成后继续从Http获取需要更新的topic的信息
    private void updateTopicsFromHttpWithoutMembers() {
        TopicGetMemberTopicsRequest getMemberTopicsRequest = new TopicGetMemberTopicsRequest();
        getMemberTopicsRequest.imToken = Constants.imToken;
        getMemberTopicsRequest.startRequest(TopicGetMemberTopicsResponse.class, new HttpCallback<TopicGetMemberTopicsResponse>() {
            @Override
            public void onSuccess(RequestBase request, TopicGetMemberTopicsResponse ret) {
                // 3
                for (ImTopic imTopic : ret.data.topic) {
                    binder.subscribeTopic(Long.toString(imTopic.topicId));
                }

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

        /*
        // 组成,分割的字符串
        StringBuilder sb = new StringBuilder();
        String sep = ",";
        for(String topicId : idTopicsNeedUpdateMember){
            sb.append(topicId);
            sb.append(",");
        }
        String strTopicIds = sb.toString();
        strTopicIds = strTopicIds.substring(0, strTopicIds.length() - sep.length());

        // 抽出方法，与mqtt公用
        updateTopicsWithMembers(strTopicIds);
        */

        for(String topicId : idTopicsNeedUpdateMember){
            // 由于server限制，改成一个个取
            updateTopicsWithMembers(topicId);
        }
    }

    // 4，依次更新topic的最新一页数据，并更新数据库，然后更新UI
    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();
    private void updateEachTopicMsgs(List<DbTopic> topics) {
        totalRetryTimes = 10;
        for (final DbTopic dbTopic : topics) {
            if (dbTopic.mergedMsgs.size() == 0) {
                // 获得了members，消息需要重新获得
                doGetTopicMsgsRequest(dbTopic);
            } else {
                // 没有更新members的，但数据库中最新消息已经过期的需要重新获取
                // 对于已经有最新消息在数据库的
                long dbLastMsgId = DatabaseDealer.getLatestMsgIdForTopic(dbTopic.getTopicId());
                if (dbTopic.latestMsgId > dbLastMsgId) {
                    doGetTopicMsgsRequest(dbTopic);
                }
            }
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
                // 有新消息，UI上应该显示红点       -- 这里 1290
                dbTopic.setShowDot(true);
                dbTopic.save();

                // 用最新一页，取代之前的mergedMsgs，来自mqtt的消息不应该删除
                for(Iterator<DbMsg> i = dbTopic.mergedMsgs.iterator(); i.hasNext();) {
                    DbMsg uiMsg = i.next();
                    if (uiMsg.getFrom().equals("mqtt")) {
                        // 数据库中记录的来自mqtt的消息
                        for (ImMsg imMsg : ret.data.topicMsg) {
                            //如果 http请求中包含此条消息
                            if (imMsg.reqId.equals(uiMsg.getReqId())) {
                                //对数据库内容进行 更新 将消息来源更新为 http
                                DatabaseDealer.updateDbMsgWithImMsg(imMsg,"http",Constants.imId);
                                //在UImsg 列表中将其删除
                                i.remove();
                            }
                        }
                        // TODO: 2018/3/28  如果 请求的列表中不包含上次的mqtt 信息，
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
                //判断获取的消息数量是否为0 或空  此时 不显示红点
                if (ret.data.topicMsg==null||ret.data.topicMsg.size()==0) {
                    dbTopic.setShowDot(false);
                    dbTopic.save();
                }

                rearrangeTopics();
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

    public ServiceConnection mqttServiceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SrtLogger.log("im mqtt", "service connectted");
            binder = (MqttService.MqttBinder) iBinder;

            binder.getService().setmMqttServiceCallback(new MqttService.MqttServiceCallback() {
                @Override
                public void onDisconnect() {
                    // 每30秒重试一次
                    if (reconnectTimer != null) {
                        reconnectTimer.cancel();
                        reconnectTimer.purge();
                        reconnectTimer = null;
                    }

                    reconnectTimer = new Timer();
                    reconnectTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 重连必须重新给一个clientId，否则直接失败
                            binder.init();
                            binder.connect();
                        }
                    }, 30 *1000);
                }

                @Override
                public void onConnect() {
                    if (reconnectTimer != null) {
                        reconnectTimer.cancel();
                        reconnectTimer.purge();
                        reconnectTimer = null;
                    }

                    // 为统一处理，移到此处
                    updateTopicsFromHttpWithoutMembers();

                    binder.subscribeMember(Constants.imId);

                    for (DbTopic dbTopic : topics) {
                        binder.subscribeTopic(Long.toString(dbTopic.getTopicId()));
                    }
                }
            });

            binder.init();
            binder.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SrtLogger.log("im mqtt", "service disconnectted");

            if (reconnectTimer != null) {
                reconnectTimer.cancel();
                reconnectTimer.purge();
                reconnectTimer = null;
            }
        }
    };
    //region MQTT
    private Timer reconnectTimer = new Timer();
    private MqttService.MqttBinder binder = null;
    private DbTopic curTopic = null;                                    // 当前界面上开启msgs的topic，curTopic为空说明是新建私聊
    private ArrayList<DbTopic> msgShownTopics = new ArrayList<>();      // 因为需要可以从群聊点击头像进入私聊，多级msgs界面

    private void startMqttService() {
        Intent intent = new Intent(getActivity(), MqttService.class);
        //mqttServiceConnection =
        getActivity().bindService(intent, mqttServiceConnection, BIND_AUTO_CREATE);

        EventBus.getDefault().register(this);
    }

    private void stopMqttService() {
        // 已经在MqttService的unbind中处理
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.NewMsgEvent event) {
        Log.i(TAG, "onMqttMsg: new msg");
        ImMsg msg = event.msg;
        DbMsg dbMsg = DatabaseDealer.updateDbMsgWithImMsg(msg, "mqtt", Constants.imId);

        // mqtt上的实时消息，按照接收顺序写入ui的datalist
        // mqtt不更新latestMsg，只有从http确认的消息才更新latestMsg，所以下次进来还是回去http拉取最新页消息
        for (DbTopic dbTopic : topics) {
            if (dbTopic.getTopicId() == msg.topicId) {
                //dbTopic.mergedMsgs.add(0, dbMsg);
                DatabaseDealer.pendingMsgToTopic(dbMsg, dbTopic);
                dbTopic.setShowDot(true);
                dbTopic.save();
                break;
            }
        }
        rearrangeTopics();
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Subscribe
    public void onMqttMsg(MqttProtobufDealer.TopicChangeEvent event) {
        // 目前只处理AddTo
        Log.i(TAG, "onMqttMsg: topic change ");
        if (event.type == MqttProtobufDealer.TopicChange.AddTo) {
            binder.subscribeTopic(Long.toString(event.topicId));
            updateTopicsWithMembers(Long.toString(event.topicId));
        }
        rearrangeTopics();
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    // http, mqtt 公用
    private void updateTopicsWithMembers(String topicIds) {
        TopicGetTopicsRequest getTopicsRequest = new TopicGetTopicsRequest();
        getTopicsRequest.imToken = Constants.imToken;
        getTopicsRequest.topicIds = topicIds;
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
                rearrangeTopics();
                mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

                // 4，对于需要更新members的topic，等待更新完members，再去取msgs
                updateEachTopicMsgs(topicsNeedUpdateMember);
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }
    //endregion

    //region 跳转
    private OnRecyclerViewItemClickCallback<DbTopic> onDbTopicCallback = new OnRecyclerViewItemClickCallback<DbTopic>() {
        @Override
        public void onItemClick(int position, DbTopic dbTopic) {
            SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
            Intent i = new Intent(getActivity(), ImMsgListActivity.class);

            DbMember member = null;
            if (DatabaseDealer.isMockTopic(dbTopic)) {
                // 私聊但topic没有创建成功
                for (DbMember m : dbTopic.getMembers()) {
                    if (m.getImId() != Constants.imId) {
                        member = m;
                    }
                }
                i.putExtra(Constants.kCreateTopicMemberId, member.getImId());
                i.putExtra(Constants.kCreateTopicMemberName, member.getName());
            }

            getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
            dbTopic.setShowDot(false);
            dbTopic.save();

            curTopic = dbTopic;
            msgShownTopics.add(dbTopic);
        }
    };


    @Subscribe
    public void onChatWithContact(DbMember contact) {
        final DbMember member = contact;
        long memberId = member.getImId();
        boolean privateChatExist = false;

        for (DbTopic topic : topics) {
            if (topic.getType().equals("1")) {
                for (DbMember dbMember : topic.getMembers()) {
                    if (dbMember.getImId() == memberId) {
                        privateChatExist = true;
                        break;
                    }
                }
                if (privateChatExist) {
                    curTopic = topic;
                    break;
                }
            }
        }

        if (privateChatExist) {
            SharedSingleton.getInstance().set(Constants.kShareTopic, curTopic);
            Intent i = new Intent(getActivity(), ImMsgListActivity.class);
            getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
            curTopic.setShowDot(false);
            curTopic.save();
            msgShownTopics.add(curTopic);
            return;
        }

        curTopic = null;
        SharedSingleton.getInstance().set(Constants.kShareTopic, null);
        Intent i = new Intent(getActivity(), ImMsgListActivity.class);
        i.putExtra(Constants.kCreateTopicMemberId, memberId);
        i.putExtra(Constants.kCreateTopicMemberName, member.getName());
        getActivity().startActivityForResult(i, Constants.IM_REQUEST_CODE_MSGLIST);
    }

    @Subscribe
    public void onMockTopicRemoved(ImMsgListActivity.MockTopicRemovedEvent event) {
        for(Iterator<DbTopic> i = topics.iterator(); i.hasNext();) {
            DbTopic uiTopic = i.next();
            if (uiTopic.getTopicId()  == event.dbTopic.getTopicId()) {
                i.remove();
                DatabaseDealer.removeMockTopic(uiTopic);
                msgShownTopics.remove(event.dbTopic);
            }
        }
    }

    @Subscribe
    public void onNewTopicCreated(ImMsgListActivity.NewTopicCreatedEvent event) {
        // 新建topic成功，curTopic由null转为实际生成的topic
        DbTopic dbTopic = event.dbTopic;
        dbTopic.setShowDot(false);
        dbTopic.save();

        curTopic = dbTopic;
        SharedSingleton.getInstance().set(Constants.kShareTopic, dbTopic);
        msgShownTopics.add(curTopic);

        // 更新uiTopics
        for(Iterator<DbTopic> i = topics.iterator(); i.hasNext();) {
            DbTopic uiTopic = i.next();
            if (uiTopic.getTopicId()  == dbTopic.getTopicId()) {
                i.remove();
            }
        }
        topics.add(dbTopic);
        rearrangeTopics();
        mTopicListRecyclerView.getAdapter().notifyDataSetChanged();

        binder.subscribeTopic(Long.toString(dbTopic.getTopicId()));
    }
    //endregion

    private void rearrangeTopics() {
        //首先按照 最新消息时间进行排序
        Collections.sort(topics,DatabaseDealer.topicComparator);

        // 只区分开群聊、私聊，不改变以前里面的顺序
        List<DbTopic> privateTopics = new ArrayList<>();
        for(Iterator<DbTopic> i = topics.iterator(); i.hasNext();) {
            DbTopic topic = i.next();
            if (topic.getType().equals("1")) {
                // 私聊
                i.remove();
                privateTopics.add(topic);
            }
        }

        topics.addAll(privateTopics);
    }


    private TitleActionCallback titleActionCallback;

    public void setTitleActionCallback(TitleActionCallback titleActionCallback) {
        this.titleActionCallback = titleActionCallback;
    }

    public interface TitleActionCallback{

        void onLeftImgClicked();

    }
}
