package com.yanxiu.gphone.faceshow.classcircle.mock;

import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 17:49.
 * Function :
 */
public class MockUtil {

    public static List<ClassCircleMock> getClassCircleMockList(){
        List<ClassCircleMock> mockList=new ArrayList<>();
        for (int i=0;i<10;i++){
            ClassCircleMock circleMock=new ClassCircleMock();
            circleMock.time="2014";
            circleMock.content="asfuf";
            circleMock.headimg="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1505382784&di=e413dd155c634e81a9455492c2469dd1&src=http://image.xinmin.cn/2012/12/15/20121215093851856906.jpg";
            circleMock.name="asd";
            circleMock.userId="12345676";
            List<ClassCircleMock.Comment> comments=new ArrayList<>();
            for (int j=0;j<5;j++){
                ClassCircleMock.Comment comment=circleMock.new Comment();
                comment.userId="1234";
                comment.userName="哈哈";
                comment.toUserId="4321";
                comment.toUserName="asssss";
                comment.content="ahsgfug";
                comments.add(comment);
            }
            circleMock.comments=comments;
            List<ClassCircleMock.ThumbUp> thumbUps=new ArrayList<>();
            for (int k=0;k<5;k++){
                ClassCircleMock.ThumbUp thumbUp=circleMock.new ThumbUp();
                thumbUp.userId="789";
                thumbUp.userName="kuaff";
                thumbUps.add(thumbUp);
            }
            circleMock.thumbs=thumbUps;
            List<String> imgUrls=new ArrayList<>();
            for (int l=0;l<9;l++){
                imgUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505393189408&di=601c56ea0f6bd20c71910e737768c8a7&imgtype=0&src=http%3A%2F%2Fuploads.gz2010.cn%2Fallimg%2F160204%2F0FA2MH-1.jpg");
            }
            circleMock.imgUrls=imgUrls;

            mockList.add(circleMock);
        }
        return mockList;
    }

}
