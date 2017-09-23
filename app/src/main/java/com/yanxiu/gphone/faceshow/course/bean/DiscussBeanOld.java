//package com.yanxiu.gphone.faceshow.course.bean;
//
//import com.yanxiu.gphone.faceshow.base.BaseBean;
//
//import java.util.ArrayList;
//
//
///**
// * 讨论
// * Created by 戴延枫 on 2017/9/20.
// */
//
//public class DiscussBeanOld extends BaseBean {
//
//    private String title;//header标题
//    private String count;//回复数量
//    private String iconUrl;//
//    private String name;//
//    private String content;//
//    private String time;//
//    private int laudCount;//点攒数量
//    private boolean isHeader = false;
//    private boolean hasLaud = false;//已经点赞 -- 用来记录已经点攒
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getCount() {
//        return count;
//    }
//
//    public void setCount(String count) {
//        this.count = count;
//    }
//
//    public String getIconUrl() {
//        return iconUrl;
//    }
//
//    public void setIconUrl(String iconUrl) {
//        this.iconUrl = iconUrl;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public int getLaudCount() {
//        return laudCount;
//    }
//
//    public void setLaudCount(int laudCount) {
//        this.laudCount = laudCount;
//    }
//
//    public boolean isHeader() {
//        return isHeader;
//    }
//
//    public void setHeader(boolean header) {
//        isHeader = header;
//    }
//
//    public boolean isHasLaud() {
//        return hasLaud;
//    }
//
//    public void setHasLaud(boolean hasLaud) {
//        this.hasLaud = hasLaud;
//    }
//
//    public static ArrayList<DiscussBeanOld> getMockData() {
//
//        ArrayList list = new ArrayList();
//        for (int i = 0; i < 10; i++) {
//            DiscussBeanOld bean = new DiscussBeanOld();
//            if (i == 0) {
//                bean.setHeader(true);
//                bean.setTitle("这是标题撒旦撒打算阿斯达岁的敖德萨大所多所大");
//                bean.setCount("20");
//
//            } else {
//                bean.setHeader(false);
//                bean.setContent("阿双方均看法快捷键卡啥京东卡圣诞节卡手机的卡三等奖口岸刷机大师空间的4杰威尔胡椒粉圣诞快乐节日快乐考虑到发");
//                bean.setIconUrl("");
//                bean.setLaudCount(1234);
//                bean.setName("关羽");
//                bean.setTime("3分钟前");
//            }
//            list.add(bean);
//        }
//        return list;
//    }
//}
