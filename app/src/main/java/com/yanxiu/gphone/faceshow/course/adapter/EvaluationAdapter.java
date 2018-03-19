package com.yanxiu.gphone.faceshow.course.adapter;

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
import com.yanxiu.gphone.faceshow.course.bean.EvaluationBean;
import com.yanxiu.gphone.faceshow.course.bean.QusetionBean;
import com.yanxiu.gphone.faceshow.customview.ChooseLayout;

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.course.bean.EvaluationBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.EvaluationBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.course.bean.EvaluationBean.TYPE_TEXT;


/**
 * Created by 戴延枫
 * 评价adapter
 */

public class EvaluationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChooseLayout.onItemClickListener {

    private Context mContext;

    private ArrayList<QusetionBean> mList;
    private EvaluationBean mVoteBean;

    private CanSubmitListener mListener;
    private boolean mIsAnswer;
    private String mTitle;
    private String mDescription;


    public EvaluationAdapter(Context context, CanSubmitListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(EvaluationBean evaluationBean) {
        mVoteBean = evaluationBean;
        mIsAnswer = mVoteBean.isAnswer();
        mList = mVoteBean.getQuestionGroup().getQuestions();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
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
        final QusetionBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_SINGLE:
            case TYPE_MULTI:
                ChooseViewHolder holder1 = (ChooseViewHolder) holder;
                holder1.evaluation_title.setText(position + 1 + "、" + data.getTitle() + "(" + data.getQuestionTypeName() + ")");
                holder1.chooseLayout.setChooseType(getItemViewType(position));
                holder1.chooseLayout.setIsClick(!mIsAnswer);
                holder1.chooseLayout.setData(data.getVoteInfo());
                if (mIsAnswer) {
                    holder1.chooseLayout.setSaveChooceResultList(data.getUserAnswer().getQuestionAnswers());
                } else {
                    holder1.chooseLayout.setSaveChooceResultList(data.getAnswerList());
                }

                holder1.chooseLayout.setSelectItemListener(this);
                if (position >= (mList.size() - 1))
                    holder1.line.setVisibility(View.GONE);

                if (position == 0 && !TextUtils.isEmpty(mTitle)) {
                    holder1.all_title_layout.setVisibility(View.VISIBLE);
                    holder1.all_title.setText(mTitle);
                    if (!TextUtils.isEmpty(mDescription)) {
                        holder1.all_title_description.setVisibility(View.VISIBLE);
                        holder1.all_title_description.setText(mDescription);
                    }else {
                        holder1.all_title_description.setVisibility(View.GONE);
                    }
                } else {
                    holder1.all_title_layout.setVisibility(View.GONE);
                }

                break;
            case TYPE_TEXT:
                TextViewHolder holder2 = (TextViewHolder) holder;
                holder2.evaluation_title.setText(position + 1 + "、" + data.getTitle());
//                holder2.evalution_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        return false;
//                    }
//                });
                if (mIsAnswer && data.getUserAnswer() != null && data.getUserAnswer().getQuestionAnswers() != null
                        && !TextUtils.isEmpty(data.getUserAnswer().getQuestionAnswers().get(0))) {
                    holder2.evalution_editText.setText(data.getUserAnswer().getQuestionAnswers().get(0));
                    holder2.evalution_editText.setEnabled(false);
                }
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
                        QusetionBean bean = mList.get(position);
                        bean.setFeedBackText(s.toString());
                    }
                });
                if (position >= (mList.size() - 1))
                    holder2.line.setVisibility(View.GONE);

                if (position == 0 && !TextUtils.isEmpty(mTitle)) {
                    holder2.all_title_layout.setVisibility(View.VISIBLE);
                    holder2.all_title.setText(mTitle);
                   if(! TextUtils.isEmpty(mDescription)) {
                       holder2.all_title_description.setVisibility(View.VISIBLE);
                       holder2.all_title_description.setText(mDescription);
                   }else {
                       holder2.all_title_description.setVisibility(View.GONE);
                   }
                } else {
                    holder2.all_title_layout.setVisibility(View.GONE);
                }
                break;
                default:break;
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
            QusetionBean bean = mList.get(i);
            if (bean.getQuestionType() == TYPE_TEXT && TextUtils.isEmpty(bean.getFeedBackText())) {
                allChoose = false;
                break;
            }
            if ((bean.getQuestionType() == TYPE_SINGLE || bean.getQuestionType() == TYPE_MULTI) && bean.getAnswerList().isEmpty()) {
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
        private View line;
        private View all_title_layout;
        private TextView all_title;
        private TextView all_title_description;


        public ChooseViewHolder(View itemView) {
            super(itemView);
            evaluation_title = (TextView) itemView.findViewById(R.id.evaluation_title);
            chooseLayout = (ChooseLayout) itemView.findViewById(R.id.chooseLayout);
            line = itemView.findViewById(R.id.evalution_choose_layout_line);
            all_title_layout = itemView.findViewById(R.id.all_title_layout);
            all_title = (TextView) itemView.findViewById(R.id.all_title);
            all_title_description = (TextView) itemView.findViewById(R.id.all_title_description);
        }
    }

    /**
     * 评价文本
     */
    class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView evaluation_title;
        private EditText evalution_editText;
        private View line;
        private View all_title_layout;
        private TextView all_title;
        private TextView all_title_description;

        public TextViewHolder(View itemView) {
            super(itemView);
            evaluation_title = (TextView) itemView.findViewById(R.id.evaluation_title);
            evalution_editText = (EditText) itemView.findViewById(R.id.evalution_editText);
            line = itemView.findViewById(R.id.evalution_text_layout_line);
            all_title_layout = itemView.findViewById(R.id.all_title_layout);
            all_title = (TextView) itemView.findViewById(R.id.all_title);
            all_title_description = (TextView) itemView.findViewById(R.id.all_title_description);
        }
    }

    public interface CanSubmitListener {
        void canSubmit(boolean is);
    }
}


