package com.yanxiu.gphone.faceshow.util.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  frc on 2017/12/18.
 */

public abstract class AbstractFrcRecyclerViewAdapter<D,VH extends AbstractFrcRecyclerViewAdapter.BaseViewHolder>extends RecyclerView.Adapter<VH>{
    private List<D> data;
    private int layoutResId;
    protected abstract void bindTheData(VH holder,D data,int position);
    private IRecyclerViewItemClickListener mIrecyclerViewItemClickListener;

    public AbstractFrcRecyclerViewAdapter(List<D> data, int layoutResId, IRecyclerViewItemClickListener iRecyclerViewItemClickListener) {
        this.data = data == null ? new ArrayList<D>() : data;
        if (layoutResId!=0){
            this.layoutResId =layoutResId;
        }else {
            throw  new NullPointerException("请设置Item资源id");
        }
        mIrecyclerViewItemClickListener = iRecyclerViewItemClickListener;
    }



    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(layoutResId,parent,false);
        return (VH) new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        bindTheData(holder,data.get(position),position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIrecyclerViewItemClickListener !=null){
                    mIrecyclerViewItemClickListener.onItemClick(view,holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
        public void setData(Object o){

        }
    }

    public interface IRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }

}
