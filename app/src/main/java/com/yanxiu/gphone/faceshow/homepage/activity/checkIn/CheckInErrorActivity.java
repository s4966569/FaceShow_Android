package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到异常页面
 * created bu frc
 */
public class CheckInErrorActivity extends FaceShowBaseActivity {

    public static final String QR_STATUE = "error_statue";
    public static final int QR_EXPIRED = 210413;//签到已过期
    public static final int HAS_NOT_START = 210412;//签到未开始
    public static final int QR_INVALID = 210411;//签到不存在或已停用
    public static final int QR_NOT_IN_CLASS = 210305;//您不是班级成员
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

    private CheckInResponse.Error mQrStatue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_error);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
        mQrStatue = (CheckInResponse.Error) getIntent().getSerializableExtra(QR_STATUE);
        if (mQrStatue != null) {
            tvErrorStatue.setText(mQrStatue.getMessage());
            switch (mQrStatue.getCode()) {
                case QR_INVALID:
                    tvErrorStatue.setText(mQrStatue.getMessage());
                    tvErrorPrompt.setText("");
                    break;
                case HAS_NOT_START:
                    tvErrorStatue.setText(mQrStatue.getMessage());
                    tvErrorPrompt.setText(getString(R.string.check_in_plan, StringUtils.getCourseTime(mQrStatue.getData().getStartTime()), StringUtils.getCourseTime(mQrStatue.getData().getEndTime())));
                    break;
                case QR_EXPIRED:
                    tvErrorStatue.setText(mQrStatue.getMessage());
                    tvErrorPrompt.setText(getString(R.string.check_in_plan, StringUtils.getCourseTime(mQrStatue.getData().getStartTime()), StringUtils.getCourseTime(mQrStatue.getData().getEndTime())));
                    tvCheckInAgain.setVisibility(View.GONE);
                    break;
                case QR_NOT_IN_CLASS:
                    tvErrorStatue.setText(mQrStatue.getMessage());
                    tvErrorPrompt.setText("");
                    break;
                default:
                    tvErrorStatue.setText(R.string.check_in_qr_already_invalid);
                    tvErrorPrompt.setText(R.string.please_scan_new_check_in_qr);
                    break;
            }
        } else {
            tvErrorStatue.setText(R.string.check_in_fail);
            tvErrorPrompt.setText("");
        }
    }

    @OnClick({R.id.img_left, R.id.tv_check_in_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.tv_check_in_again:
                CheckInByQRActivity.toThisAct(this);
                this.finish();
                break;
        }
    }
}
