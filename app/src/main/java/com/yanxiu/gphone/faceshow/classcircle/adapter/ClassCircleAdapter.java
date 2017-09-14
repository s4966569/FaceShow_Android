package com.yanxiu.gphone.faceshow.classcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 16:07.
 * Function :
 */
public class ClassCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TITLE=0x0000;
    private static final int TYPE_DEFAULT=0x0001;

    private List<ClassCircleMock> mData=new ArrayList<>();

    public void setData(List<ClassCircleMock> list){
        if (list==null){
            return;
        }
        this.mData.clear();
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addData(List<ClassCircleMock> list){
        if (list==null){
            return;
        }
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clear(){
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_TITLE;
        }
        return TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType==TYPE_TITLE){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_title,parent,false);
            return new TitleViewHolder(view);
        }else {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_class_circle_item,parent,false);
            return new ClassCircleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder){

        }else if (holder instanceof ClassCircleViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size()+1:1;
    }

    class ClassCircleViewHolder extends RecyclerView.ViewHolder{

        public ClassCircleViewHolder(View itemView) {
            super(itemView);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{

        public TitleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
