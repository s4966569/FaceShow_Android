package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInDetailActivity;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录二级适配
 * Created by frc on 17-9-18.
 */

class CheckInNotesSecondAdapter extends RecyclerView.Adapter<CheckInNotesSecondAdapter.ViewHolder> {
    private List<GetCheckInNotesResponse.CheckInNotesBean> checkInNotes = new ArrayList<>();

    private final int NORMAL = 0x001;
    private final int FOOTER = 0x002;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == NORMAL) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_second_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_second_footer_layout, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setData(checkInNotes.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInDetailActivity.toThisAct(holder.itemView.getContext(), checkInNotes.get(position));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return FOOTER;
        } else {
            return NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return checkInNotes.size();
    }

    void update(List<GetCheckInNotesResponse.CheckInNotesBean> checkInNotes) {
        this.checkInNotes = checkInNotes;
        notifyDataSetChanged();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_training_statue, tv_training_name, tv_training_check_in_time;
        private ImageView img_training_statue;

        ViewHolder(View itemView) {
            super(itemView);
            tv_training_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            tv_training_statue = (TextView) itemView.findViewById(R.id.tv_training_statue);
            img_training_statue = (ImageView) itemView.findViewById(R.id.img_training_statue);
            tv_training_check_in_time = (TextView) itemView.findViewById(R.id.tv_training_check_in_time);
        }

        public void setData(GetCheckInNotesResponse.CheckInNotesBean checkInNotesBean) {
            tv_training_name.setText(checkInNotesBean.getTitle());

            if (checkInNotesBean.getUserSignIn() != null && checkInNotesBean.getUserSignIn().getSigninStatus() == 1) {
                tv_training_statue.setText("已签到");
                tv_training_check_in_time.setVisibility(View.VISIBLE);
                tv_training_check_in_time.setText(checkInNotesBean.getUserSignIn().getSigninTime());
                tv_training_statue.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_333333));
                img_training_statue.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_check_in_small_success));
            } else {
                tv_training_check_in_time.setVisibility(View.GONE);
                tv_training_statue.setText("未签到");
                tv_training_statue.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_999999));
                img_training_statue.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_check_in_small_error));
            }
        }
    }
}
