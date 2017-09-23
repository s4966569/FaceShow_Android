package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到异常页面
 * created bu frc
 */
public class CheckInErrorActivity extends FaceShowBaseActivity {

    public static final String QR_STATUE = "error_statue";
    public static final String QR_EXPIRED = "check_in_qr_expired";//签到已过期
    public static final String HAS_NOT_START = "check_in_has_not_start";//签到未开始
    public static final String QR_INVALID = "check_in_qr_invalid";//无效的二维码
    public static final String QR_NOT_IN_CLASS = "not_in_class";//您不是班级成员
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_error_statue)
    TextView tvErrorStatue;
    @BindView(R.id.tv_error_prompt)
    TextView tvErrorPrompt;
    @BindView(R.id.tv_check_in_again)
    TextView tvCheckInAgain;

    private String mQrStatue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_error);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
        mQrStatue = getIntent().getStringExtra(QR_STATUE);
        tvErrorStatue.setText(mQrStatue);
        tvErrorPrompt.setText(R.string.please_scan_correct_check_in_qr);
//        if (mQrStatue.equals(QR_EXPIRED)) {
//            tvErrorStatue.setText(R.string.check_in_qr_already_expired);
//            tvErrorPrompt.setText(R.string.please_scan_new_check_in_qr);
//        } else if (mQrStatue.equals(QR_INVALID)) {
//            tvErrorStatue.setText(R.string.check_in_qr_already_invalid);
//            tvErrorPrompt.setText("");
//        } else if (mQrStatue.equals(HAS_NOT_START)) {
//            tvErrorStatue.setText(R.string.check_in_qr_has_not_start);
//            tvErrorPrompt.setText(R.string.please_scan_correct_check_in_qr);
//        } else if (mQrStatue.equals(QR_NOT_IN_CLASS)) {
//            tvErrorStatue.setText(R.string.check_in_not_in_class);
//            tvErrorPrompt.setText("");
//        }

    }

    @OnClick({R.id.img_left, R.id.tv_check_in_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.tv_check_in_again:
                QRCodeCheckInActivity.toThisAct(this);
                this.finish();
                break;
        }
    }
}
