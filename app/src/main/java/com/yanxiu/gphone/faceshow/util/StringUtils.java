package com.yanxiu.gphone.faceshow.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by frc on 16-6-20.
 */
public class StringUtils {
    final static int BUFFER_SIZE = 4096;
    public final static String GB2312 = "gb2312";
    public final static String UTF_8 = "UTF-8";

    /**
     * 获取当前版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();// 得到包管理对象
            PackageInfo info = manager
                    .getPackageInfo(context.getPackageName(), 0);// 获取指定包的信息
            return info.versionName;// 获取版本
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unkonwn";
        }
    }

    /**
     * 把InputStream对象转换成String
     *
     * @param is
     * @return
     */
    public static String converStreamToString(InputStream is) {

        if (is == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();

        int count = 0;
        while (count < 3) {

            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                break;
            } catch (IOException e) {
                count++;
            }
        }

        return buffer.toString();
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     */
    public static String converStreamToString(InputStream in, String encoding) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            data = null;
            return new String(outStream.toByteArray(), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String timeString(int time) {
        time = time / 1000;
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String intTimeString(int time) {
        int miao = time % 60;
        int fen = time / 60;
        int hour = 0;
        if (fen >= 60) {
            hour = fen / 60;
            fen = fen % 60;
        }
        String timeString = "";
        String miaoString = "";
        String fenString = "";
        String hourString = "";
        if (miao < 10) {
            miaoString = "0" + miao;
        } else {
            miaoString = miao + "";
        }
        if (fen < 10) {
            fenString = "0" + fen;
        } else {
            fenString = fen + "";
        }
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }
        if (hour != 0) {
            timeString = hourString + ":" + fenString + ":" + miaoString;
        } else {
            timeString = fenString + ":" + miaoString;
        }
        return timeString;
    }

    public static boolean isEmpty(String str) {
        if (null != str) {
            if (str.length() > 4) {
                return false;
            }
        }
        return null == str || "".equals(str) || "NULL"
                .equals(str.toUpperCase());
    }

    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str);
    }

    public static boolean isEmptyArray(Object[] array, int len) {
        return null == array || array.length < len;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getDateTime(long value) {
        System.out.println(value);
        Date date = new Date(value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(format.format(date));

        return format.format(date);
    }

    // Convert Unix timestamp to normal date style
    public static String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date(timestamp));
        return date;
    }

    public static double getM(double b) {
        double m;
        double kb;
        kb = b / 1024.0;
        m = kb / 1024.0;
        return m;
    }

    public static String getImage260_360Url(String originalUrl) {
        int dotIndex = originalUrl.lastIndexOf(".");
        String sizeUrlExe = originalUrl
                .substring(dotIndex, originalUrl.length());
        String sizeUrlHead = originalUrl.substring(0, dotIndex);
        String sizeNewUrl = sizeUrlHead + "_260_360" + sizeUrlExe;
        return sizeNewUrl;
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }
}
