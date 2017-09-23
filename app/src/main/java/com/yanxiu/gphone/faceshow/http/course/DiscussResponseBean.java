package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.course.bean.DiscussBean;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/9/23.
 */

public class DiscussResponseBean extends BaseBean {
    private int totalElements;
    private String callbackParam;
    private String callbackValue;
    private ArrayList<DiscussBean> elements = new ArrayList();

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public String getCallbackParam() {
        return callbackParam;
    }

    public void setCallbackParam(String callbackParam) {
        this.callbackParam = callbackParam;
    }

    public String getCallbackValue() {
        return callbackValue;
    }

    public void setCallbackValue(String callbackValue) {
        this.callbackValue = callbackValue;
    }

    public ArrayList<DiscussBean> getElements() {
        return elements;
    }

    public void setElements(ArrayList<DiscussBean> elements) {
        this.elements = elements;
    }

    /**
     * 将数据转化为自己的带有header的数据
     * 注释：如果是loadMore，请调用getElements()获取分页数据
     *
     * @return
     */
    public ArrayList<DiscussBean> getDataWithHeader(String discussTitle) {
        ArrayList<DiscussBean> list = new ArrayList();
        DiscussBean header = new DiscussBean();
        header.setHeader(true);
        header.setTitle(discussTitle);
        header.setTotalElements(totalElements);
        list.add(header);
        list.addAll(elements);
        return list;
    }

}
