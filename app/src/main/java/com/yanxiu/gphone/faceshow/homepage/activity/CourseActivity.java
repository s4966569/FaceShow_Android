package com.yanxiu.gphone.faceshow.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.util.ActivityManger;

/**
 * 课程
 */
public class CourseActivity extends FaceShowBaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        initView();
    }


    private void initView() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navi_1:
                break;
            default:
                break;
        }
    }


    /**
     * 跳转CourseActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, CourseActivity.class);
        activity.startActivity(intent);
    }
}
