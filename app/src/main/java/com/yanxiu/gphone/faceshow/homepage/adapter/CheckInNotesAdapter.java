package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录的适配
 * Created by frc on 17-9-18.
 */

public class CheckInNotesAdapter extends RecyclerView.Adapter<CheckInNotesAdapter.ViewHolder> {
    private List<GetCheckInNotesResponse.DataBean> checkInNotesList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(checkInNotesList.get(position));
    }


    @Override
    public int getItemCount() {
        return checkInNotesList.size();
    }

    public void update(List<GetCheckInNotesResponse.DataBean> list) {
        this.checkInNotesList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_project_name;
        private TextView tv_class_name;
        private RecyclerView recyclerView_notes;
        private CheckInNotesSecondAdapter mCheckInNotesSecondAdapter;

        ViewHolder(View itemView) {
            super(itemView);
            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            tv_class_name = (TextView) itemView.findViewById(R.id.tv_class_name);
            recyclerView_notes = (RecyclerView) itemView.findViewById(R.id.recyclerView_notes);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView_notes.setLayoutManager(linearLayoutManager);
            mCheckInNotesSecondAdapter = new CheckInNotesSecondAdapter();
            recyclerView_notes.setAdapter(mCheckInNotesSecondAdapter);

        }

        public void setData(GetCheckInNotesResponse.DataBean data) {
            tv_project_name.setText(data.getProjectName());
            tv_class_name.setText(data.getClassName());
            mCheckInNotesSecondAdapter.update(data.getCheckInNotes());

        }
    }
}
