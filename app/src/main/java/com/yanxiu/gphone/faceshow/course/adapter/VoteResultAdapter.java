package com.yanxiu.gphone.faceshow.course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.QusetionBean;
import com.yanxiu.gphone.faceshow.course.bean.VoteBean;
import com.yanxiu.gphone.faceshow.customview.VoteRuseltLayout;

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_TEXT;


/**
 * Created by 戴延枫
 * 评价adapter
 */

public class VoteResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private VoteBean mVoteBean;
    private ArrayList<QusetionBean> mList;

    public VoteResultAdapter(Context context) {
        mContext = context;
    }

    public void setData(VoteBean voteBean) {
        mVoteBean = voteBean;
        mList = mVoteBean.getQuestionGroup().getQuestions();
    }

    @Override
    public int getItemViewType(int position) {
        QusetionBean bean = mList.get(position);
        return bean.getQuestionType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_SINGLE:
            case TYPE_MULTI:
                View multi = inflater.inflate(R.layout.vote_result__layout, parent, false);
                viewHolder = new ChooseViewHolder(multi);
                break;
            case TYPE_TEXT:
                View item3 = inflater.inflate(R.layout.vote_text_layout, parent, false);
                viewHolder = new TextViewHolder(item3);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final QusetionBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_SINGLE:
            case TYPE_MULTI:
                ChooseViewHolder holder1 = (ChooseViewHolder) holder;
                holder1.voteResult_title.setText(position + 1 + "、" + data.getTitle() + "(" + data.getQuestionTypeName() + ")");
                holder1.voteResult_Layout.setData(data.getVoteInfo());
                break;
            case TYPE_TEXT:
                TextViewHolder holder2 = (TextViewHolder) holder;
                holder2.voteResult_title.setText(position + 1 + "、" + data.getTitle());
                holder2.voteResult_personnumber.setText("参与人数:" + data.getAnswerUserNum());
//                String text = "我的回复:"+"/n"+data.getFeedBackTime()+"/n"+;
                holder2.voteResult_time.setText(data.getCreateTime());
                String text = "";
                try {
                    text = data.getUserAnswer().getQuestionAnswers().get(0);
                } catch (Exception e) {

                }
                holder2.voteResult_editText.setText(text);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    @Override
//    public void onChooseItemClick(int position, boolean isSelected) {
//        boolean allChoose = true;
//        for (int i = 0; i < getItemCount(); i++) {
//            VoteBean bean = mList.get(i);
//            if (bean.getType() == TYPE_TEXT && TextUtils.isEmpty(bean.getFeedBackText())) {
//                allChoose = false;
//                break;
//            }
//            if ((bean.getType() == TYPE_SINGLE || bean.getType() == TYPE_MULTI) && bean.getAnswerList().isEmpty()) {
//                allChoose = false;
//                break;
//            }
//
//        }
//    }

    /**
     * 评价单双选
     */
    class ChooseViewHolder extends RecyclerView.ViewHolder {
        private TextView voteResult_title;
        private VoteRuseltLayout voteResult_Layout;


        public ChooseViewHolder(View itemView) {
            super(itemView);
            voteResult_title = (TextView) itemView.findViewById(R.id.voteResult_title);
            voteResult_Layout = (VoteRuseltLayout) itemView.findViewById(R.id.voteResult_Layout);
        }
    }

    /**
     * 评价文本
     */
    class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView voteResult_title;
        private TextView voteResult_personnumber;
        private TextView voteResult_editText;
        private TextView voteResult_time;

        public TextViewHolder(View itemView) {
            super(itemView);
            voteResult_title = (TextView) itemView.findViewById(R.id.voteResult_title);
            voteResult_time = (TextView) itemView.findViewById(R.id.voteResult_time);
            voteResult_personnumber = (TextView) itemView.findViewById(R.id.voteResult_personnumber);
            voteResult_editText = (TextView) itemView.findViewById(R.id.voteResult_editText);
        }
    }

}


