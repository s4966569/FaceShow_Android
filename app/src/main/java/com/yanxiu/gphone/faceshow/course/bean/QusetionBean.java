package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_SINGLE;

/**
 * 投票封装类--每一个题目
 */
public class QusetionBean extends BaseBean {
    private String id;
    private String title;
    private String description;
    private int questionType;
    private String questionData;
    private String answerNum;
    private String answerUserNum;
    private String groupId;
    private String questionStatus;
    private String bizId;
    private String bizSource;
    private String createTime;
    private String questionTypeName;
    private VoteInfoBean voteInfo = new VoteInfoBean();
    private UserAnswerBean userAnswer = new UserAnswerBean();

    private ArrayList<String> answerList = new ArrayList<>();//保存选项结果

    private String feedBackText;//反馈信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestionData() {
        return questionData;
    }

    public void setQuestionData(String questionData) {
        this.questionData = questionData;
    }

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public String getAnswerUserNum() {
        return answerUserNum;
    }

    public void setAnswerUserNum(String answerUserNum) {
        this.answerUserNum = answerUserNum;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizSource() {
        return bizSource;
    }

    public void setBizSource(String bizSource) {
        this.bizSource = bizSource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public VoteInfoBean getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(VoteInfoBean voteInfo) {
        this.voteInfo = voteInfo;
    }

    /**
     * 提交答案时，上传的是选项的id，但是我们的逻辑是index，所以，把server的id转为index
     *
     * @return
     */
    public UserAnswerBean getUserAnswer() {
        if (getQuestionType() == TYPE_SINGLE || getQuestionType() == TYPE_MULTI) {
            ArrayList<String> answerList = userAnswer.getQuestionAnswers();//里面是id
            ArrayList<VoteItemBean> voteItem = voteInfo.getVoteItems();
            for (int i = 0; i < answerList.size(); i++) {
                String id = answerList.get(i);
                for (int j = 0; j < voteItem.size(); j++) {
                    if (id.equals(voteItem.get(j).getItemId())) {
//                    result.add(voteItem.get(j).getItemId());
                        answerList.set(i, String.valueOf(j));
                    }
                }
            }
        }

        return userAnswer;
    }

    public void setUserAnswer(UserAnswerBean userAnswer) {
        this.userAnswer = userAnswer;
    }

    public ArrayList<String> getAnswerList() {
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
}
