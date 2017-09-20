package com.yanxiu.gphone.faceshow.common.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.io.Serializable;

/**
 * Created by LHZ on 2016/6/28.
 */
public class PdfBean extends BaseBean {
    String name;
    String url;
int record;
    public String getName() {
        return name;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
