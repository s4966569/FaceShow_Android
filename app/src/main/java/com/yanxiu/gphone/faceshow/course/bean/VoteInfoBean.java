package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;

/**
 * 投票的一个题目的信息
 */
public class VoteInfoBean extends BaseBean {

    private ArrayList<VoteItemBean> voteItems = new ArrayList();
    private int maxSelectNum;

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public ArrayList<VoteItemBean> getVoteItems() {
        return voteItems;
    }

    public void setVoteItems(ArrayList<VoteItemBean> voteItems) {
        this.voteItems = voteItems;
    }
}
