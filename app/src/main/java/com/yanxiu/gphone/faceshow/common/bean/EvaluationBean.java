package com.yanxiu.gphone.faceshow.common.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;

/**
 * 评价封装类
 */
public class EvaluationBean extends BaseBean {
    public static final int TYPE_SINGLE = 0x000;
    public static final int TYPE_MULTI = 0x001;
    public static final int TYPE_TEXT = 0x002;
    private int type;
    private String title;
    private String feedBackText;//反馈信息
    private ArrayList<String> chooseList = new ArrayList();
    private ArrayList<String> answerList = new ArrayList<>();//保存选项结果

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getChooseList() {
        return chooseList;
    }

    public void setChooseList(ArrayList<String> chooseList) {
        this.chooseList = chooseList;
    }

    public ArrayList<String> getAnswerList() {
        if (answerList == null) {
            answerList = new ArrayList<>(chooseList.size());
        }
        return answerList;
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }

    public String getFeedBackText() {
        return feedBackText;
    }

    public void setFeedBackText(String feedBackText) {
        this.feedBackText = feedBackText;
    }

    public static ArrayList<EvaluationBean> getMockData() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < 10; i++) {
            EvaluationBean bean = new EvaluationBean();
            if (i % 2 == 0) {
                bean.setType(TYPE_SINGLE);
                bean.getChooseList().add("满意");
                bean.getChooseList().add("不满意");
                bean.getChooseList().add("满意2");
                bean.getChooseList().add("满意3");
            } else if (i % 3 == 0) {
                bean.setType(TYPE_MULTI);
                bean.getChooseList().add("满意");
                bean.getChooseList().add("不满意");
                bean.getChooseList().add("满意2");
                bean.getChooseList().add("满意3");
            } else {
                bean.setType(TYPE_TEXT);
            }
            bean.setTitle(i + "、这是标题");
            list.add(bean);
        }
        return list;
    }
}
