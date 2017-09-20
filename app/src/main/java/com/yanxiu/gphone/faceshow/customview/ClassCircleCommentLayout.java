package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleCommentLayout extends RelativeLayout {

    private CommentAdapter adapter;

    public ClassCircleCommentLayout(Context context) {
        this(context,null);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_class_circle_comment,this);
        ListView listView= (ListView) findViewById(R.id.un_listview);
        adapter=new CommentAdapter(context);
        listView.setAdapter(adapter);
    }

    public void setData(ArrayList<ClassCircleResponse.Data.Moments.Comments> list){
        if (list==null||list.size()==0){
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        adapter.setData(list);
    }

    private class CommentAdapter extends BaseAdapter{

        private Context mContext;
        private ArrayList<ClassCircleResponse.Data.Moments.Comments> mData=new ArrayList<>();

        CommentAdapter(Context context){
            this.mContext=context;
        }

        public void setData(ArrayList<ClassCircleResponse.Data.Moments.Comments> list){
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty();
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(position);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public int getCount() {
            return mData!=null?mData.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassCircleResponse.Data.Moments.Comments comment=mData.get(position);
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adapter_classcircle_comment_item,null);
            TextView textView= (TextView) convertView.findViewById(R.id.tv_comment);
            String text;
            if (comment.level.equals("1")){
                text=comment.publisher.realName+": "+comment.content;
            }else {
                text=comment.publisher.realName+"对"+comment.toUser.realName+"说"+": "+comment.content;
            }
            textView.setText(text);
            return convertView;
        }

    }
}
