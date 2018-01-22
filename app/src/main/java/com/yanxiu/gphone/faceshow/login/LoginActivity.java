package com.yanxiu.gphone.faceshow.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.constant.Constants;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.NeedModel;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.db.cityModel;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshow.http.login.SignInRequest;
import com.yanxiu.gphone.faceshow.http.login.SignInResponse;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.util.FileUtils;
import com.yanxiu.gphone.faceshow.util.LBSManager;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;
import com.yanxiu.gphone.faceshow.util.anim.AnimUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;


/**
 * 登录页面
 *
 * @author frc
 */
public class LoginActivity extends FaceShowBaseActivity {

    private static final int TO_FORGET_PASSWORD_REQUEST_CODE = 0X01;

    private Unbinder unbinder;
    private Context mContext;
    private PublicLoadLayout rootView;
    @BindView(R.id.edt_account_number)
    EditText edt_account_number;
    @BindView(R.id.edt_account_password)
    EditText edt_account_password;
    @BindView(R.id.img_show_password)
    ImageView img_show_password;
    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;
    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.scroll_root)
    ScrollView scroll_root;

    private boolean isPasswordShow = false;
    private UUID mSignInRequestUUID;
    private String token;
    private String passPort;
    //按钮底部在Y轴的坐标
    int btnY = 0;
    // 需要偏移的距离
    float delta = 0;
    int rootH = 0;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public static void toThisAct(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_login);
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);
        if (SpManager.isFristStartUp()) {
            SpManager.setFristStartUp(false);
        }
        edt_account_number.addTextChangedListener(accountNumberChangedListener);
        edt_account_password.addTextChangedListener(accountPasswordChangedListener);
        if (edt_account_number.getText().length() > 0 && edt_account_password.getText().length() > 0) {
            tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
        } else {
            tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
        }

        ll_login.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                ll_login.getLocationOnScreen(location);
                btnY = location[1] + ll_login.getHeight();
                ll_login.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

        });
        scroll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                scroll_root.getLocationOnScreen(location);
                rootH = location[1] + scroll_root.getHeight();
                scroll_root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        onGlobalLayoutListener = doMonitorSoftKeyboard(rootView, new OnSoftKeyBoardListener() {
            @Override
            public void hasShow(boolean isSoftVisible) {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                if (isSoftVisible) {
                    delta = (float) Math.abs(btnY - r.bottom + (rootH - r.bottom));
                    AnimUtil.up(ll_login, -delta / 3);
                } else {
                    AnimUtil.up(ll_login, 0);
                }
            }
        });
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

//        String city = FileUtils.getFromAssets(getApplicationContext(), "city.json");
//        String shen = FileUtils.getFromAssets(getApplicationContext(), "shen.json");
//        String difang = FileUtils.getFromAssets(getApplicationContext(), "difang.json");
//        cityModel cityModel = RequestBase.getGson().fromJson(city, cityModel.class);
//        cityModel shenModel = RequestBase.getGson().fromJson(shen, cityModel.class);
//        cityModel difangModel = RequestBase.getGson().fromJson(difang, cityModel.class);
//        for (int i = 0; i < cityModel.getData().size(); i++) {
//            for (int j = 0; j < difangModel.getData().size(); j++) {
//                if (difangModel.getData().get(j).getParentId() == cityModel.getData().get(i).getId()) {
//                    if (cityModel.getData().get(i).getNextLevelData() != null) {
//                        cityModel.getData().get(i).getNextLevelData().add(difangModel.getData().get(j));
//                    } else {
//                        List<com.yanxiu.gphone.faceshow.db.cityModel.DataBean> nextLevelData = new ArrayList<>();
//                        nextLevelData.add(difangModel.getData().get(j));
//                        cityModel.getData().get(i).setNextLevelData(nextLevelData);
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < shenModel.getData().size(); i++) {
//            for (int j = 0; j < cityModel.getData().size(); j++) {
//                if (shenModel.getData().get(i).getId() == cityModel.getData().get(j).getParentId()) {
//                    if (shenModel.getData().get(i).getNextLevelData() != null) {
//                        shenModel.getData().get(i).getNextLevelData().add(cityModel.getData().get(j));
//                    } else {
//                        List<com.yanxiu.gphone.faceshow.db.cityModel.DataBean> nextLevelData = new ArrayList<>();
//                        nextLevelData.add(cityModel.getData().get(j));
//                        shenModel.getData().get(i).setNextLevelData(nextLevelData);
//                    }
//                }
//            }
//        }
//
//        String data = RequestBase.getGson().toJson(shenModel);
//        Log.e("frc",data);
//
//        FileUtils.writeFile(Constants.SDCARD_ROOT_NAME+"/xzz.txt", data,false);


    }

    /***
     * 判断软件盘是否弹出
     * @param v
     * @param listener
     * 备注：在不用的时候记得移除OnGlobalLayoutListener
     * */
    public ViewTreeObserver.OnGlobalLayoutListener doMonitorSoftKeyboard(final View v, final OnSoftKeyBoardListener listener) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // 获取屏幕的可见范围保存在矩形r中
                v.getWindowVisibleDisplayFrame(r);
                int screenHeight = v.getRootView().getHeight();
                //软件盘高度 = 屏幕真实高度 - 屏幕可见范围的高度
                int heightDifference = screenHeight - r.bottom;
                boolean isSoftVisible = heightDifference > (screenHeight / 3);
                if (listener != null) {
                    listener.hasShow(isSoftVisible);
                }
            }
        };
        v.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        return layoutListener;
    }

    interface OnSoftKeyBoardListener {
        void hasShow(boolean isSoftVisible);
    }


    private TextWatcher accountNumberChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0 && edt_account_password.getText().length() > 0) {
                tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
            } else {
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private TextWatcher accountPasswordChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() > 0 && edt_account_number.getText().length() > 0) {
                tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
            } else {
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mSignInRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mSignInRequestUUID);
        }
        if (onGlobalLayoutListener != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    @OnClick({R.id.img_show_password, R.id.tv_sign_in, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_show_password:
                if (isPasswordShow) {
                    edt_account_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_show_password.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_show_password));
                    isPasswordShow = false;
                } else {
                    edt_account_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_show_password.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.selector_hide_password));
                    isPasswordShow = true;
                }
                break;
            case R.id.tv_sign_in:
                if (edt_account_number.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_name_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else if (edt_account_password.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_password_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else {
                    signInRequest();
                }

                break;
            case R.id.tv_forget_password:
                startActivityForResult(new Intent(LoginActivity.this, ForgetPasswordActivity.class), TO_FORGET_PASSWORD_REQUEST_CODE);
                break;
            default:
                break;
        }
    }


    private void signInRequest() {
        rootView.showLoadingView();
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.loginName = edt_account_number.getText().toString();
        signInRequest.password = Utils.MD5Helper(edt_account_password.getText().toString());
        mSignInRequestUUID = signInRequest.startRequest(SignInResponse.class, new HttpCallback<SignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignInResponse ret) {

                if (ret.getCode() == 0) {
                    token = ret.getToken();
                    passPort = ret.getPassport();
                    requestClassData(LoginActivity.this);
                } else {
                    Toast.makeText(mContext, ret.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    rootView.hiddenLoadingView();
                    edt_account_password.setText(null);
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                edt_account_password.setText(null);
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserInfo(final Activity activity, final MainBean data) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.token = token;
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                rootView.finish();
                if (ret.getCode() == 0) {
                    SpManager.saveToken(token);
                    SpManager.savePassPort(passPort);
                    UserInfo.Info info = ret.getData();
                    info.setProjectName(data.getProjectInfo().getProjectName());
                    info.setClassName(data.getClazsInfo().getClazsName());
                    info.setClassId(data.getClazsInfo().getId());
                    SpManager.saveUserInfo(info);
                    PushManager.getInstance().turnOnPush(activity);//开启个推服务
                    //boolean isBind= PushManager.getInstance().bindAlias(activity, String.valueOf(ret.getData().getUserId()));
                    MainActivity.invoke(activity);
                    LoginActivity.this.finish();
                } else {
                    ToastUtil.showToast(activity, ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(FaceShowApplication.getContext(), error.getMessage());
            }
        });
    }

    /**
     * 请求项目班级信息，如果code!=0，那么，不能进入首页，并且弹出toast提示
     */
    private void requestClassData(final Activity activity) {
        MainRequest mainRequest = new MainRequest();
        mainRequest.token = token;
        mainRequest.startRequest(MainResponse.class, new HttpCallback<MainResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainResponse ret) {

                if (ret != null && ret.getCode() == 0) {
                    getUserInfo(activity, ret.getData());
                } else {
                    if (ret != null && ret.getError() != null) {
                        rootView.finish();
                        ToastUtil.showToast(FaceShowApplication.getContext(), ret.getError().getMessage());
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.finish();
                if (error != null) {
                    ToastUtil.showToast(FaceShowApplication.getContext(), error.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_FORGET_PASSWORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    edt_account_number.setText(data.getStringExtra("phoneNumber"));
                    edt_account_password.setText(data.getStringExtra("password"));
                }
            }
        }
    }


}
