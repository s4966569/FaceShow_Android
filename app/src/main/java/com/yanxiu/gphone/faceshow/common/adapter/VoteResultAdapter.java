package com.yanxiu.gphone.faceshow.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.bean.VoteBean;
import com.yanxiu.gphone.faceshow.customview.ChooseLayout;
import com.yanxiu.gphone.faceshow.customview.VoteRuseltLayout;

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_TEXT;


/**
 * Created by 戴延枫
 * 评价adapter
 */

public class VoteResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VoteRuseltLayout.onItemClickListener {

    private Context mContext;

    private ArrayList<VoteBean> mList;

    private boolean mOnlyLook;

    public VoteResultAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<VoteBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        VoteBean bean = mList.get(position);
        return bean.getType();
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
                View item3 = inflater.inflate(R.layout.evaluation_text_layout, parent, false);
                viewHolder = new TextViewHolder(item3);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final VoteBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_SINGLE:
            case TYPE_MULTI:
                ChooseViewHolder holder1 = (ChooseViewHolder) holder;
                holder1.voteResult_title.setText(data.getTitle());
                holder1.voteResult_Layout.setChooseType(getItemViewType(position));
                holder1.voteResult_Layout.setData(data.getChooseList());
                holder1.voteResult_Layout.setSaveChooceResultList(data.getAnswerList());
                holder1.voteResult_Layout.setSelectItemListener(this);
                break;
            case TYPE_TEXT:
                TextViewHolder holder2 = (TextViewHolder) holder;
                holder2.voteResult_title.setText(data.getTitle());
                holder2.evalution_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        com.yanxiu.gphone.faceshow.util.Logger.e("dyf", s.toString());
                        VoteBean bean = mList.get(position);
                        bean.setFeedBackText(s.toString());
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onChooseItemClick(int position, boolean isSelected) {
        boolean allChoose = true;
        for (int i = 0; i < getItemCount(); i++) {
            VoteBean bean = mList.get(i);
            if (bean.getType() == TYPE_TEXT && TextUtils.isEmpty(bean.getFeedBackText())) {
                allChoose = false;
                break;
            }
            if ((bean.getType() == TYPE_SINGLE || bean.getType() == TYPE_MULTI) && bean.getAnswerList().isEmpty()) {
                allChoose = false;
                break;
            }

        }
    }

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
        private EditText evalution_editText;

        public TextViewHolder(View itemView) {
            super(itemView);
            voteResult_title = (TextView) itemView.findViewById(R.id.voteResult_title);
            evalution_editText = (EditText) itemView.findViewById(R.id.evalution_editText);
        }
    }

}


