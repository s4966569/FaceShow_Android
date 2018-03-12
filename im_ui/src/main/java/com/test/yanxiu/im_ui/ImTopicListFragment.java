package com.test.yanxiu.im_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.RequestQueueHelper;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_core.http.GetTopicMsgsRequest;
import com.test.yanxiu.im_core.http.GetTopicMsgsResponse;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetMemberTopicsResponse;
import com.test.yanxiu.im_core.http.TopicGetTopicsRequest;
import com.test.yanxiu.im_core.http.TopicGetTopicsResponse;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.http.common.ImTopic;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.test.yanxiu.im_core.dealer.DatabaseDealer.topicComparator;


public class ImTopicListFragment extends FaceShowBaseFragment {
    private ImTitleLayout mTitleLayout;
    private ImageView mNaviLeftImageView;
    private TextView mNaviRightTextView;
    private RecyclerView mTopicListRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<DbTopic> topics;
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

    private void setupView(View v) {
        mTitleLayout = v.findViewById(R.id.title_layout);

        // set title
        mTitleLayout.setTitle("聊聊");

        // set navi left
        mNaviLeftImageView = v.findViewById(R.id.navi_left_imageview);
        mNaviLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTitleLayout.setLeftView(mNaviLeftImageView);

        // set navi right
        mNaviRightTextView = v.findViewById(R.id.navi_right_textview);
        mNaviRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    }

    private void setupData() {
        updateTopicsFromDb();
        updateTopicsFromHttpWithoutMembers();
    }

    private String imToken = "fb1a05461324976e55786c2c519a8ccc";
    // 1，从DB列表生成
    private void updateTopicsFromDb() {
        DatabaseDealer.useDbForUser("cailei");
        topics = DatabaseDealer.topicsFromDb();
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
                        (dbTopic.getChange() == imTopic.topicChange))
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
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }

    // 4，依次更新topic的最新一页数据，并更新数据库
    private int totalRetryTimes;
    private RequestQueueHelper rqHelper = new RequestQueueHelper();
    private void updateTopicMsgs() {
        totalRetryTimes = 10;
        for (final DbTopic dbTopic : topics) {
            doGetTopicMsgsRequest(dbTopic);
        }
    };

    private void doGetTopicMsgsRequest(final DbTopic dbTopic) {
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
                    doGetTopicMsgsRequest(dbTopic);
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


}
