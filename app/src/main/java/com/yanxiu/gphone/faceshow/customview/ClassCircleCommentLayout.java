package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleCommentLayout extends RelativeLayout {

    private ListView mListView;
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
        mListView= (ListView) findViewById(R.id.un_listview);
        adapter=new CommentAdapter(context);
        mListView.setAdapter(adapter);
    }

    public void setData(ArrayList<ClassCircleMock.Comment> list){
        if (list==null||list.size()==0){
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        adapter.setData(list);
    }

    private class CommentAdapter extends BaseAdapter{

        private Context mContext;
        private ArrayList<ClassCircleMock.Comment> mData=new ArrayList<>();

        CommentAdapter(Context context){
            this.mContext=context;
        }

        public void setData(ArrayList<ClassCircleMock.Comment> list){
            mData.addAll(list);
            notifyDataSetChanged();
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
            ClassCircleMock.Comment comment=mData.get(position);
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adapter_classcircle_comment_item,null);
            TextView textView= (TextView) convertView.findViewById(R.id.tv_comment);
            String text="";
            if (TextUtils.isEmpty(comment.toUserName)){
                text=comment.userName+": "+comment.content;
            }else {
                text=comment.userName+"对"+comment.toUserName+"说"+": "+comment.content;
            }
            textView.setText(text);
            return convertView;
        }

    }
}
