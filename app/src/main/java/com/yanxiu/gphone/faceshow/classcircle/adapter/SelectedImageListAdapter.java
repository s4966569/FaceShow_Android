package com.yanxiu.gphone.faceshow.classcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author frc on 2018/1/12.
 */

public class SelectedImageListAdapter extends RecyclerView.Adapter {
    private List<ImageItem> data;

    public SelectedImageListAdapter(ArrayList<ImageItem> data) {
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
