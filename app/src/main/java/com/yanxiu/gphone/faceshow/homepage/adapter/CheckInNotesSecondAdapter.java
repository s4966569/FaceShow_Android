package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInDetailActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.QRCodeCheckInActivity;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录二级适配
 * Created by frc on 17-9-18.
 */

public class CheckInNotesSecondAdapter extends RecyclerView.Adapter<CheckInNotesSecondAdapter.ViewHolder> {
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
                if (checkInNotes.get(position).getCheckInStatue().equals("0")) {
                    CheckInDetailActivity.toThisAct(holder.itemView.getContext());
                } else {
                    QRCodeCheckInActivity.toThisAct(holder.itemView.getContext());
                }
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

    public void update(List<GetCheckInNotesResponse.CheckInNotesBean> checkInNotes) {
        this.checkInNotes = checkInNotes;
        notifyDataSetChanged();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_training_statue, tv_training_name;
        private ImageView img_training_statue;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_training_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            tv_training_statue = (TextView) itemView.findViewById(R.id.tv_training_statue);
            img_training_statue = (ImageView) itemView.findViewById(R.id.img_training_statue);
        }

        public void setData(GetCheckInNotesResponse.CheckInNotesBean checkInNotesBean) {
            tv_training_name.setText(checkInNotesBean.getTrainingName());

            if (checkInNotesBean.getCheckInStatue().equals("0")) {
                tv_training_statue.setText("已签到");
            } else {
                tv_training_statue.setText("未签到");
            }
        }
    }
}
