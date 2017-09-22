package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;

/**
 * 投票封装类
 */
public class VoteBeanOld extends BaseBean {
    public static final int TYPE_SINGLE = 0x000;
    public static final int TYPE_MULTI = 0x001;
    public static final int TYPE_TEXT = 0x002;
    private int type;
    private String title;
    private String feedBackText;//反馈信息
    private int personCount;//参与人数
    private String feedBackTime;//回复时间
    private ArrayList<String> chooseList = new ArrayList();
    private ArrayList<String> answerList = new ArrayList<>();//保存选项结果
    private ArrayList<VoteResultBean> resultList = new ArrayList<>();//保存选项结果

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

    public ArrayList<VoteResultBean> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<VoteResultBean> resultList) {
        this.resultList = resultList;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public String getFeedBackTime() {
        return feedBackTime;
    }

    public void setFeedBackTime(String feedBackTime) {
        this.feedBackTime = feedBackTime;
    }


    public static class VoteResultBean {
        private String chooseContent;//选项的文字
        private int totalCount;//总的投票数量
        private int count;//投票数量

        public String getChooseContent() {
            return chooseContent;
        }

        public void setChooseContent(String chooseContent) {
            this.chooseContent = chooseContent;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static ArrayList<VoteBeanOld> getMockData() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < 10; i++) {
            VoteBeanOld bean = new VoteBeanOld();
            if (i % 2 == 0) {
                bean.setType(TYPE_SINGLE);
                bean.getChooseList().add("满意");
                bean.getChooseList().add("不满意");
                bean.getChooseList().add("满意2");
                bean.getChooseList().add("满意3");
                bean.getAnswerList().add(String.valueOf(1));
                for (int j = 0; j < 4; j++) {
                    VoteResultBean vrb = new VoteResultBean();
                    vrb.setCount(10);
                    vrb.setTotalCount(100);
                    vrb.setChooseContent("满意");
                    bean.getResultList().add(vrb);
                }
            } else if (i % 3 == 0) {
                bean.setType(TYPE_MULTI);
                bean.getChooseList().add("满意");
                bean.getChooseList().add("不满意");
                bean.getChooseList().add("满意2");
                bean.getChooseList().add("满意3");
                for (int k = 0; k < 3; k++) {
                    bean.getAnswerList().add(String.valueOf(k));
                }

                for (int j = 0; j < 3; j++) {
                    VoteResultBean vrb = new VoteResultBean();
                    vrb.setCount(30);
                    vrb.setTotalCount(100);
                    vrb.setChooseContent("满意");
                    bean.getResultList().add(vrb);
                }

            } else {
                bean.setType(TYPE_TEXT);
                bean.setPersonCount(5);
                bean.setFeedBackTime("2017年9月20日10:05:40");
                bean.setFeedBackText("asdasdasdsdadsd");
            }
            bean.setTitle(i + "、这是标题");
            list.add(bean);
        }
        return list;
    }
}
