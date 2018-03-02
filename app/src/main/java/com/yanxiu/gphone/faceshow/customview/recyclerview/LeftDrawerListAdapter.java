package com.yanxiu.gphone.faceshow.customview.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.ChooseClassActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.YXPictureManager;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;

/**
 * adapter for LeftDrawerRecyclerView in MainActivity
 * Created by frc on 17-10-26.
 */

public class LeftDrawerListAdapter extends BaseRecyclerViewAdapter {
    private Context mContext;
    private final int TYPE_HEAD = 0X01;
    private final int TYPE_NORMAL = 0X02;

    private int[] itemIconArray = new int[]{
            R.drawable.selector_left_drawer_image_main,
            R.drawable.selector_left_drawer_image_user,
            R.drawable.selector_left_drawer_image_main,//这里是 签到图标
            R.drawable.selector_left_drawer_image_main};//这里是 意见反馈图标
    //            R.drawable.ic_person_black,
//            R.drawable.ic_settings_black, R.drawable.ic_exit_to_app_black};
    private String[] itemNameArray = null;

    private CourseArrangeBean mCourseData;

        public LeftDrawerListAdapter(Context context, CourseArrangeBean courseData) {
        mContext = context;
        mCourseData = courseData;
        itemNameArray = context.getResources().getStringArray(R.array.left_drawer_item_names);
    }
    public LeftDrawerListAdapter(Context context) {
        mContext = context;
//        mCourseData = courseData;
        itemNameArray = context.getResources().getStringArray(R.array.left_drawer_item_names);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_head_layout, parent, false));
        } else if (viewType == TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_normal_layout, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            final HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            if (UserInfo.getInstance().getInfo() != null) {
                if (UserInfo.getInstance().getInfo().getAvatar() != null) {
                    YXPictureManager.getInstance().showRoundPic(mContext, UserInfo.getInstance().getInfo().getAvatar(), headViewHolder.user_icon, 6, R.drawable.discuss_user_default_icon);
                }
                if (UserInfo.getInstance().getInfo().getRealName() != null) {
                    headViewHolder.user_name.setText(UserInfo.getInstance().getInfo().getRealName());
                }
            }
            /**
             * 这里对 用户当前所在的项目 和班级 进行文字设置
             * */
            try {
                String prjName=SpManager.getUserInfo().getProjectName();
                String clazzName=SpManager.getUserInfo().getClassName();
                if (prjName==null||prjName.isEmpty()) {
                    headViewHolder.project_name.setText("暂未设置项目");
                }else {
                    headViewHolder.project_name.setText(SpManager.getUserInfo().getProjectName());
                }
                if (clazzName==null||clazzName.isEmpty()) {
                    headViewHolder.class_name.setText("暂未设置班级");
                }else {
                    headViewHolder.class_name.setText(SpManager.getUserInfo().getClassName());
                }

                if (prjName==null||prjName.isEmpty()||clazzName==null||clazzName.isEmpty()) {
                    headViewHolder.changeClass_button.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            /*顶部view 的点击监听*/
            headViewHolder.changeClass_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headerViewClickListener != null) {
                        headerViewClickListener.onHeaderButtonClicked();
                    }

                }
            });

        } else {
            ((NormalViewHolder) holder).itemName.setText(itemNameArray[position - 1]);
            ((NormalViewHolder) holder).itemIcon.setImageDrawable(ContextCompat.getDrawable(mContext, itemIconArray[position - 1]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewItemClickListener != null) {
                        Log.i(getClass().getSimpleName(), "onClick: position is  "+position);
                        recyclerViewItemClickListener.onItemClick(v, holder.getAdapterPosition() - 1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return itemIconArray.length + 1;
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {

        public ImageView user_icon;
        public TextView user_name;
        public TextView project_name;
        public TextView class_name;
        public TextView changeClass_button;

        HeadViewHolder(View itemView) {
            super(itemView);
            user_icon = (ImageView) itemView.findViewById(R.id.user_icon);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            project_name = (TextView) itemView.findViewById(R.id.project_name);
            class_name = (TextView) itemView.findViewById(R.id.class_name);
            changeClass_button = (TextView) itemView.findViewById(R.id.changeClass_button);
        }
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIcon;
        private TextView itemName;

        NormalViewHolder(View itemView) {
            super(itemView);
            itemIcon = (ImageView) itemView.findViewById(R.id.list_item_icon);
            itemName = (TextView) itemView.findViewById(R.id.list_item_name);
        }
    }
    /*抽屉 顶部布局的点击监听*/
    private HeaderViewClickListener headerViewClickListener;

    public void setHeaderViewClickListener(HeaderViewClickListener headerViewClickListener) {
        this.headerViewClickListener = headerViewClickListener;
    }

    public interface HeaderViewClickListener{
            void onHeaderImgClicked();
            void onHeaderButtonClicked();
    }
}
