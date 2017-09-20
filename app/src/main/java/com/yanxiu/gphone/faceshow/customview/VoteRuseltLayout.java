package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.bean.VoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 投票结果控件
 * dyf
 */
public class VoteRuseltLayout extends LinearLayout implements View.OnClickListener {

//    public static final int TYPE_SINGLE = 0x000;
//    public static final int TYPE_MULTI = 0x001;

    private Context mContext;
    //    private onItemClickListener mOnItemClickListener;
//    private int mChooseType = TYPE_SINGLE;
//    private boolean mIsClick = true;
//
//    private ArrayList<VoteBean> mAnswerList;//保存选项结果


//    public interface onItemClickListener {
//        void onChooseItemClick(int position, boolean isSelected);
//    }

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

    public void setData(List<VoteBean.VoteResultBean> list) {
        addChildView(list);
    }

//    /**
//     * 选择结果，保存到这个list里（该list，就是bean里的list）
//     *
//     * @param list
//     */
//    public void setSaveChooceResultList(ArrayList<String> list) {
//        mAnswerList = list;
//        for (int i = 0; i < mAnswerList.size(); i++) {
//            //adapter复用时，恢复数据
//            int position = Integer.parseInt(mAnswerList.get(i));
//            setSelect(position);
//        }
//    }


    private void addChildView(final List<VoteBean.VoteResultBean> list) {
        this.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            VoteBean.VoteResultBean bean = list.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_vote_result_item, this, false);
            final ViewHolder holder = new ViewHolder();
            holder.position = i;
            holder.mVote_title = (TextView) view.findViewById(R.id.vote_title);
            holder.mVoteResult_count = (TextView) view.findViewById(R.id.voteResult_count);
            holder.mVoteResult_progress = (VoteProgressView) view.findViewById(R.id.voteResult_progress);
            holder.mVoteResult_progress.setMaxCount(bean.getTotalCount());
            holder.mVoteResult_progress.updateProgress(bean.getCount());

            holder.mVote_title.setText(bean.getChooseContent()+"");
            holder.mVoteResult_count.setText(bean.getCount()+"");
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
