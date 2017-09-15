package com.yanxiu.gphone.faceshow.classcircle.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;
import com.yanxiu.gphone.faceshow.customview.ClassCircleCommentLayout;
import com.yanxiu.gphone.faceshow.customview.ClassCircleThumbView;
import com.yanxiu.gphone.faceshow.customview.UnMoveGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:07.
 * Function :
 */
public class ClassCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onCommentClickListener {
        void commentClick(int position, ClassCircleMock mock, ClassCircleMock.Comment comment, boolean isCommentMaster);
        void commentFinish();
    }

    private static final int TYPE_TITLE = 0x0000;
    private static final int TYPE_DEFAULT = 0x0001;
    private static final int ANIM_OPEN = 0x0002;
    private static final int ANIM_CLOSE = 0x0003;
    private static final int ANIM_DURATION = 200;
    private static final int ANIM_POSITION_DEFAULT = -1;

    private Context mContext;
    private List<ClassCircleMock> mData = new ArrayList<>();
    private int animPosition = ANIM_POSITION_DEFAULT;
    private onCommentClickListener mCommentClickListener;

    public ClassCircleAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ClassCircleMock> list) {
        if (list == null) {
            return;
        }
        this.mData.clear();
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addData(List<ClassCircleMock> list) {
        if (list == null) {
            return;
        }
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    public void setCommentClickListener(onCommentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        }
        return TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_title, parent, false);
            return new TitleViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_item, parent, false);
            return new ClassCircleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
        } else if (holder instanceof ClassCircleViewHolder) {
            final ClassCircleViewHolder classCircleViewHolder = (ClassCircleViewHolder) holder;
            final ClassCircleMock circleMock = mData.get(position - 1);

            Glide.with(mContext).load(circleMock.headimg).into(classCircleViewHolder.mHeadImgView);
            classCircleViewHolder.mNameView.setText(circleMock.name);
            classCircleViewHolder.mContentView.setText(circleMock.content);
            classCircleViewHolder.mTimeView.setText(circleMock.time);
            if (animPosition!=position) {
                classCircleViewHolder.mAnimLayout.setVisibility(View.INVISIBLE);
            }
            classCircleViewHolder.mAnimLayout.setEnabled(false);
            classCircleViewHolder.mFunctionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewAnim(classCircleViewHolder.getLayoutPosition(), classCircleViewHolder.mAnimLayout);
                }
            });
            classCircleViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (animPosition != ANIM_POSITION_DEFAULT) {
                        notifyItemChanged(animPosition);
                        animPosition = ANIM_POSITION_DEFAULT;
                    }
                    if (mCommentClickListener != null) {
                        mCommentClickListener.commentFinish();
                    }
                    return false;
                }
            });
            classCircleViewHolder.mCommentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCommentClickListener != null) {
                        mCommentClickListener.commentClick(classCircleViewHolder.getLayoutPosition(), circleMock, null, true);
                    }
//                    classCircleViewHolder.mAnimLayout.setVisibility(View.GONE);
//                    animPosition=ANIM_POSITION_DEFAULT;
                }
            });
            classCircleViewHolder.mThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点赞", Toast.LENGTH_SHORT).show();
                    classCircleViewHolder.mAnimLayout.setVisibility(View.GONE);
                    animPosition = ANIM_POSITION_DEFAULT;
                }
            });
        }
    }

    private void setViewAnim(int position, View view) {
        int width = view.getWidth();
        float scaleStart;
        float scaleEnd;
        int translationStart;
        final int translationEnd;
        final int animType;
        if (animPosition == ANIM_POSITION_DEFAULT) {
            scaleStart = 0f;
            scaleEnd = 1f;
            translationStart = width / 2;
            translationEnd = 0;
            animType = ANIM_OPEN;
            animPosition = position;
        } else if (animPosition == position) {
            scaleStart = 1f;
            scaleEnd = 0f;
            translationStart = 0;
            translationEnd = width / 2;
            animType = ANIM_CLOSE;
            animPosition = ANIM_POSITION_DEFAULT;
        } else {
            notifyItemChanged(animPosition);
            scaleStart = 0f;
            scaleEnd = 1f;
            translationStart = width / 2;
            translationEnd = 0;
            animType = ANIM_OPEN;
            animPosition = position;
        }

        ViewCompat.setScaleX(view, scaleStart);
        ViewCompat.setTranslationX(view, translationStart);
        ViewCompat.animate(view).setDuration(ANIM_DURATION).translationX(translationEnd).scaleX(scaleEnd).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setEnabled(true);
                if (animType == ANIM_OPEN) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                if (animType == ANIM_CLOSE) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                if (animType == ANIM_CLOSE) {
                    view.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1 : 1;
    }

    class ClassCircleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_head_img)
        ImageView mHeadImgView;
        @BindView(R.id.tv_name)
        TextView mNameView;
        @BindView(R.id.tv_content)
        TextView mContentView;
        @BindView(R.id.tv_time)
        TextView mTimeView;
        @BindView(R.id.iv_function)
        ImageView mFunctionView;
        @BindView(R.id.gv_imgs)
        UnMoveGridView mUnMoveGridView;
        @BindView(R.id.cc_thumb)
        ClassCircleThumbView mCircleThumbView;
        @BindView(R.id.cc_comments)
        ClassCircleCommentLayout mCircleCommentLayout;
        @BindView(R.id.ll_anim)
        LinearLayout mAnimLayout;
        @BindView(R.id.tv_thumb)
        TextView mThumbView;
        @BindView(R.id.tv_comment)
        TextView mCommentView;

        ClassCircleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        TitleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
