package com.test.yanxiu.im_ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.db.DbTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        mTitleLayout = v.findViewById(R.id.title_layout);

        // set title
        mTitleLayout.setTitle("聊聊");

        // set left
        mNaviLeftImageView = v.findViewById(R.id.navi_left_imageview);
        mNaviLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTitleLayout.setLeftView(mNaviLeftImageView);

        // set right
        mNaviRightTextView = v.findViewById(R.id.navi_right_textview);
        mNaviRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTitleLayout.setRightView(mNaviRightTextView);

        // set list
        DbTopic t1 = new DbTopic();
        t1.setName("1");
        DbTopic t2 = new DbTopic();
        t2.setName("2");
        DbTopic t3 = new DbTopic();
        t3.setName("3");
        topics = new ArrayList<>(Arrays.asList(t1, t2, t3));


        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mTopicListRecyclerView = v.findViewById(R.id.topic_list_recyclerview);
        mTopicListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        TopicListAdapter adapter = new TopicListAdapter(getContext(), topics);
        mTopicListRecyclerView.setAdapter(adapter);

        return v;
    }


}
