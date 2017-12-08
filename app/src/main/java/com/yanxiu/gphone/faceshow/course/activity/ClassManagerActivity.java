package com.yanxiu.gphone.faceshow.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 无班级 异常页面
 */
public class ClassManagerActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_txt)
    TextView titleLayoutLeftTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manager);
        ButterKnife.bind(this);
        titleLayoutLeftTxt.setText(R.string.back);
        titleLayoutLeftTxt.setVisibility(View.VISIBLE);
        titleLayoutLeftTxt.setTextColor(ContextCompat.getColor(this,R.color.color_1da1f2));
    }

    @OnClick(R.id.title_layout_left_txt)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }
}
