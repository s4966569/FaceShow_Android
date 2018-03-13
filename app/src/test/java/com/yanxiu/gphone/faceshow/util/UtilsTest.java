package com.yanxiu.gphone.faceshow.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by srt on 2018/3/13.
 */
public class UtilsTest {



    @Test
    public void isContainBlank() throws Exception {

        String[] testData1={" abc",""," ","a b","1231 "};

        String[] testData2={"abc","1","12312312","ab","1231"};
        for (String datum : testData1) {
            Assert.assertTrue(Utils.isContainBlank(datum));
        }

        for (String datum : testData2) {
            Assert.assertFalse(Utils.isContainBlank(datum));
        }



    }

}