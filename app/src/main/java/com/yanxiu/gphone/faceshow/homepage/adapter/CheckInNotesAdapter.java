package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInDetailActivity;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;
import com.yanxiu.gphone.faceshow.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到记录的适配
 * Created by frc on 17-9-18.
 */

public class CheckInNotesAdapter extends RecyclerView.Adapter<CheckInNotesAdapter.BaseViewHolder> {
    private final int ITEM_TYPE_CLASS_INFO = 0X01;
    private final int ITEM_TYPE_CHECK_IN_NORMAL = 0X02;
    private final int ITEM_TYPE_CHECK_IN_FOOTER = 0X03;
    private List<GetCheckInNotesResponse.Element> checkInNotesList = new ArrayList<>();
    private int mPositionInList = -1;
    private List<Object> data = new ArrayList<>();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CLASS_INFO) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_layout, parent, false);
            return new ClassInfoViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_CHECK_IN_NORMAL) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_second_layout, parent, false);
            return new CheckInViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in_notes_second_footer_layout, parent, false);
            return new CheckInViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_TYPE_CLASS_INFO) {
            holder.setData(data.get(position));
        } else {
            holder.setData(data.get(position));
            ((CheckInViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPositionInList = position;
                    CheckInDetailActivity.toThisAct(holder.itemView.getContext(), ((GetCheckInNotesResponse.CheckInNotesBean) data.get(position)));
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getClass().getSimpleName().equals(CheckInClassInfo.class.getSimpleName())) {
            return ITEM_TYPE_CLASS_INFO;
        } else {
            if (((GetCheckInNotesResponse.CheckInNotesBean) data.get(position)).isFooter()) {
                return ITEM_TYPE_CHECK_IN_FOOTER;
            } else {
                return ITEM_TYPE_CHECK_IN_NORMAL;
            }
        }
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public void update(List<GetCheckInNotesResponse.Element> list) {
        this.checkInNotesList = list;
        data.clear();
        for (int i = 0; i < checkInNotesList.size(); i++) {
            CheckInClassInfo checkInClassInfo = new CheckInClassInfo(checkInNotesList.get(i).getProjectName(), checkInNotesList.get(i).getClazs());
            data.add(checkInClassInfo);
            for (int j = 0; j < checkInNotesList.get(i).getCheckInNotes().size(); j++) {
                if (j == checkInNotesList.get(i).getCheckInNotes().size() - 1) {
                    checkInNotesList.get(i).getCheckInNotes().get(j).setFooter(true);
                }
                data.add(checkInNotesList.get(i).getCheckInNotes().get(j));
            }
        }
        notifyDataSetChanged();
    }


    String json;

    public void reFreshItem(String json) {
        this.json = json;
        CheckInResponse.DataBean dataB = RequestBase.getGson().fromJson(json, CheckInResponse.DataBean.class);
        GetCheckInNotesResponse.UserSignIn userSignIn = new GetCheckInNotesResponse.UserSignIn();
        userSignIn.setSigninStatus(1);
        userSignIn.setSuccessPrompt(dataB.getSuccessPrompt());
        userSignIn.setSigninTime(dataB.getSigninTime());
        ((GetCheckInNotesResponse.CheckInNotesBean) data.get(mPositionInList)).setUserSignIn(userSignIn);
        notifyItemChanged(mPositionInList);
    }

    private class CheckInViewHolder extends BaseViewHolder {
        private TextView tv_training_statue, tv_training_name, tv_training_check_in_time;
        private ImageView img_training_statue;

        CheckInViewHolder(View itemView) {
            super(itemView);
            tv_training_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            tv_training_statue = (TextView) itemView.findViewById(R.id.tv_training_statue);
            img_training_statue = (ImageView) itemView.findViewById(R.id.img_training_statue);
            tv_training_check_in_time = (TextView) itemView.findViewById(R.id.tv_training_check_in_time);
        }

        @Override
        protected void setData(Object object) {
            GetCheckInNotesResponse.CheckInNotesBean checkInNotesBean = (GetCheckInNotesResponse.CheckInNotesBean) object;
            tv_training_name.setText((checkInNotesBean).getTitle());
            if (checkInNotesBean.getUserSignIn() != null && checkInNotesBean.getUserSignIn().getSigninStatus() == 1) {
                tv_training_statue.setText("已签到");
                tv_training_check_in_time.setVisibility(View.VISIBLE);
                tv_training_check_in_time.setText(DateFormatUtil.translationBetweenTwoFormat(checkInNotesBean.getUserSignIn().getSigninTime(),DateFormatUtil.FORMAT_ONE,DateFormatUtil.FORMAT_SIX));
                tv_training_statue.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_333333));
                img_training_statue.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_check_in_small_success));
            } else {
                tv_training_check_in_time.setVisibility(View.GONE);
                tv_training_statue.setText("未签到");
                tv_training_statue.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_999999));
                img_training_statue.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_check_in_small_error));
            }
        }

    }

    private class ClassInfoViewHolder extends BaseViewHolder {
        private TextView tv_project_name;
        private TextView tv_class_name;
//        private RecyclerView recyclerView_notes;
//        private CheckInNotesSecondAdapter mCheckInNotesSecondAdapter;

        ClassInfoViewHolder(View itemView) {
            super(itemView);
            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            tv_class_name = (TextView) itemView.findViewById(R.id.tv_class_name);
//            recyclerView_notes = (RecyclerView) itemView.findViewById(R.id.recyclerView_notes);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView_notes.setLayoutManager(linearLayoutManager);
//            mCheckInNotesSecondAdapter = new CheckInNotesSecondAdapter();
//            recyclerView_notes.setAdapter(mCheckInNotesSecondAdapter);
        }

        @Override
        protected void setData(Object object) {
            tv_project_name.setText(((CheckInClassInfo) object).getProjectName());
            tv_class_name.setText(((CheckInClassInfo) object).getClazz().getClazsName());
//
//            if (isRefrsh) {
//                isRefrsh = false;
//                mCheckInNotesSecondAdapter.refreshItem(data.getCheckInNotes(), json, mPositionInList);
//                if (mPositionInList != -1) {
//                    recyclerView_notes.scrollToPosition(mPositionInList);
//                }
//            } else {
//                mCheckInNotesSecondAdapter.update(data.getCheckInNotes());
        }

    }


    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        protected abstract void setData(Object object);


    }

    private class CheckInClassInfo {
        private String projectName;
        private GetCheckInNotesResponse.Clazs clazz;

        CheckInClassInfo(String projectName, GetCheckInNotesResponse.Clazs clazz) {
            this.projectName = projectName;
            this.clazz = clazz;
        }

        String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        GetCheckInNotesResponse.Clazs getClazz() {
            return clazz;
        }

        public void setClazz(GetCheckInNotesResponse.Clazs clazz) {
            this.clazz = clazz;
        }
    }
}
