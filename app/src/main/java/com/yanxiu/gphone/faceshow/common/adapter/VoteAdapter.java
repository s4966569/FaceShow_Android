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

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.common.bean.VoteBean.TYPE_TEXT;


/**
 * Created by 戴延枫
 * 评价adapter
 */

public class VoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChooseLayout.onItemClickListener {

    private Context mContext;

    private ArrayList<VoteBean> mList;

    private CanSubmitListener mListener;
    private boolean mOnlyLook;

    public VoteAdapter(Context context, CanSubmitListener listener) {
        mContext = context;
        mListener = listener;
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
                View multi = inflater.inflate(R.layout.evaluation_choose_layout, parent, false);
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
                holder1.evaluation_title.setText(data.getTitle());
                holder1.chooseLayout.setChooseType(getItemViewType(position));
                holder1.chooseLayout.setData(data.getChooseList());
                holder1.chooseLayout.setSaveChooceResultList(data.getAnswerList());
                holder1.chooseLayout.setSelectItemListener(this);
                break;
            case TYPE_TEXT:
                TextViewHolder holder2 = (TextViewHolder) holder;
                holder2.evaluation_title.setText(data.getTitle());
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

//            for (int j = 0; j < bean.getAnswerList().size(); j++) {
//                Log.e("dyf", "第: " + i + "个数据 =" + bean.getAnswerList().get(j));
//            }
//            Log.i("dyf", "第: " + i + "个反馈 =" + bean.getFeedBackText());
        }
        mListener.canSubmit(allChoose);
    }

    /**
     * 评价单双选
     */
    class ChooseViewHolder extends RecyclerView.ViewHolder {
        private TextView evaluation_title;
        private ChooseLayout chooseLayout;


        public ChooseViewHolder(View itemView) {
            super(itemView);
            evaluation_title = (TextView) itemView.findViewById(R.id.evaluation_title);
            chooseLayout = (ChooseLayout) itemView.findViewById(R.id.chooseLayout);
        }
    }

    /**
     * 评价文本
     */
    class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView evaluation_title;
        private EditText evalution_editText;

        public TextViewHolder(View itemView) {
            super(itemView);
            evaluation_title = (TextView) itemView.findViewById(R.id.evaluation_title);
            evalution_editText = (EditText) itemView.findViewById(R.id.evalution_editText);
        }
    }

    public interface CanSubmitListener {
        void canSubmit(boolean is);
    }
}


