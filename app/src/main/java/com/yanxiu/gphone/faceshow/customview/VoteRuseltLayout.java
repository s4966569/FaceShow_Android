package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.VoteInfoBean;
import com.yanxiu.gphone.faceshow.course.bean.VoteItemBean;

import java.util.ArrayList;

/**
 * 投票结果控件
 * dyf
 */
public class VoteRuseltLayout extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private VoteInfoBean mData;

    private final String[] mEms = new String[]{" A.", " B.", " C.", " D.", " E.", " F.", " G.", " H.", " I.", " J.", " K.", " L.", " M.", " N.",
            " O.", " P.", " Q.", " R.", " S.", " T.", " U.", " V.", " W.", " X.", " Y.", " Z."};

    public VoteRuseltLayout(Context context) {
        super(context);
        init(context);
    }

    public VoteRuseltLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteRuseltLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        if (isInEditMode()) {
            return;
        }
        this.setOrientation(LinearLayout.VERTICAL);
    }

    private ArrayList<String> myAnswers;
    public void setUserAnswer(ArrayList<String> answers){
        myAnswers=answers;
    }
    public void setData(VoteInfoBean data) {
        mData = data;
        addChildView(data);
    }

    private void addChildView(final VoteInfoBean data) {
        this.removeAllViews();
        ArrayList<VoteItemBean> list = data.getVoteItems();
        for (int i = 0; i < list.size(); i++) {
            VoteItemBean bean = list.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_vote_result_item, this, false);
            final ViewHolder holder = new ViewHolder();
            holder.position = i;
            holder.mVote_title = (TextView) view.findViewById(R.id.vote_title);
            holder.mVoteResult_count = (TextView) view.findViewById(R.id.voteResult_count);
            //高亮设置  需要 用户选择信息  对比的是 数组的index  不是itemid
            if (myAnswers != null) {
                //用户的答案中是否包含当前项
                for (int j = 0; j < myAnswers.size(); j++) {
                    if (i==Integer.valueOf(myAnswers.get(j))) {
                        holder.mVote_title.setTextColor(getResources().getColor(R.color.color_1da1f2));
                        holder.mVoteResult_count.setTextColor(getResources().getColor(R.color.color_1da1f2));
                        break;
                    }
                }
            }


            holder.mVoteResult_progress = (VoteProgressView) view.findViewById(R.id.voteResult_progress);
//            holder.mVoteResult_progress.setMaxCount(100);
//            holder.mVoteResult_progress.updateProgress((int)(100*Float.valueOf(bean.getPercent())));
            holder.mVoteResult_progress.setPercent(Float.valueOf(bean.getPercent()));

            holder.mVote_title.setText(mEms[i]+bean.getItemName());
            holder.mVoteResult_count.setText(bean.getSelectedNum());
            this.addView(view);
        }
    }

    public class ViewHolder {
        public int position;
        public boolean mSelect = false;
        public TextView mVote_title;
        public TextView mVoteResult_count;
        public VoteProgressView mVoteResult_progress;
    }

//    public void setSelect(int position) {
//        setSelect(position, false);
//    }
//
//    private void setSelect(int position, boolean isCallBack) {
//        int count = this.getChildCount();
//        if (position >= count) {
//            return;
//        }
//        for (int i = 0; i < count; i++) {
//            View chileView = this.getChildAt(i);
//            ViewHolder holder = (ViewHolder) chileView.getTag();
//            if (i == position) {
//                if (holder.mSelect) {
//                    setItemUnSelect(holder);
//                    if (isCallBack) {
//                        if (mChooseType == TYPE_SINGLE) {
//                            mAnswerList.clear();
//                        }
//                        if (mAnswerList.contains(String.valueOf(position))) { //已有，删除
//                            mAnswerList.remove(String.valueOf(position));
//                        }
////                        onClick(i, false);
//                    }
//                } else {
//                    setItemSelect(holder);
//                    if (isCallBack) {
//                        if (mChooseType == TYPE_SINGLE) {
//                            mAnswerList.clear();
//                        }
//                        if (!mAnswerList.contains(String.valueOf(position))) { //没有，添加
//                            mAnswerList.add(String.valueOf(position));
//                        }
////                        onClick(i, true);
//                    }
//                }
//            } else {
//                if (mChooseType == TYPE_SINGLE) {
//                    setItemUnSelect(holder);
//                }
//            }
//        }
//    }
//
//    private void setItemUnSelect(ViewHolder holder) {
//        holder.mSelect = false;
//        if (mChooseType == TYPE_MULTI) {
//            ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.multi_unselect));
//        } else {
//            ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.single_unselect));
//        }
//        holder.mVote_title.setTextColor(getResources().getColor(R.color.color_666666));
//    }
//
//    private void setItemSelect(ViewHolder holder) {
//        holder.mSelect = true;
//        if (mChooseType == TYPE_MULTI) {
//            ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.multi_select));
//        } else {
//            ViewCompat.setBackground(holder.mQuestionSelectView, ContextCompat.getDrawable(mContext, R.drawable.single_select));
//        }
//        holder.mVote_title.setTextColor(getResources().getColor(R.color.color_1da1f2));
//    }

/*    private void onClick(int position, boolean isSelected) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onChooseItemClick(position, isSelected);
        }
    }*/

    @Override
    public void onClick(View v) {
//        if (mIsClick) {
//            ViewHolder holder = (ViewHolder) v.getTag();
//            setSelect(holder.position, true);
//        }
    }
}
