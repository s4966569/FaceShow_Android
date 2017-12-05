package com.yanxiu.gphone.faceshow.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.igexin.sdk.PushManager;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshow.http.login.SignInRequest;
import com.yanxiu.gphone.faceshow.http.login.SignInResponse;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

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
 * created by frc
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
    private boolean isPasswordShow = false;
    private UUID mSignInRequestUUID;
    private String token;
    private String passPort;

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
//    如果修改状态栏则ｓｃｒｏｌｌＶｉｅｗ失效    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarUtils.setStatusBarFullScreen(LoginActivity.this);
//        }
        unbinder = ButterKnife.bind(this);
        if (SpManager.isFristStartUp())
            SpManager.setFristStartUp(false);

        edt_account_number.addTextChangedListener(accountNumberChangedListener);
        edt_account_password.addTextChangedListener(accountPasswordChangedListener);
        if (edt_account_number.getText().length() > 0 && edt_account_password.getText().length() > 0) {
            tv_sign_in.setBackgroundResource(R.drawable.selector_sign_in_bg);
        } else {
            tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_normal_bg);
        }
        testLsb();
        MWConfiguration config = new MWConfiguration(this);
        config.setLogEnable(true);//打开魔窗Log信息
        MagicWindowSDK.initSDK(config);
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


    private void getUserInfo(final Activity activity) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.token = token;
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                rootView.finish();
                if (ret.getCode() == 0) {
                    SpManager.saveToken(token);
                    SpManager.savePassPort(passPort);
                    String userInfoStr = RequestBase.getGson().toJson(ret.getData());
                    SpManager.saveUserInfo(userInfoStr);
                    UserInfo.getInstance().setInfo(ret.getData());
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
                    getUserInfo(activity);
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

    private void testLsb(){

        // TODO: 2017/12/1 基本功能已经实现 缺乏6.0的动态权限判断
        LocationClientOption locationClientOption =new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setIsNeedLocationDescribe(true);
        locationClientOption.setIsNeedLocationPoiList(true);
        LocationClient locationClient =null;
        MyLocationListener myLocationListener=new MyLocationListener();
        locationClient=new LocationClient(getApplicationContext());
        locationClient.setLocOption(locationClientOption);
        locationClient.registerLocationListener(myLocationListener);
        locationClient.start();
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude =bdLocation.getLatitude();
            double longitude =bdLocation.getLongitude();
            float radius =bdLocation.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType =bdLocation.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode =bdLocation.getLocType();
            Log.e("LBS","latitude:: "+latitude+"  longitude::"+longitude+"  radius::"+radius+"  coorType:"+coorType+"  errorCode::"+errorCode);
            /**获取详细地址信息**/
            String addr = bdLocation.getAddrStr();
            String country = bdLocation.getCountry();    //获取国家
            String province = bdLocation.getProvince();    //获取省份
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            String street = bdLocation.getStreet();    //获取街道信息

            List<Poi> poiList=bdLocation.getPoiList();

            Log.e("LBS","地址:"+country+province+city+district+street);
            Log.e("LBS", "addr: "+addr );
            Log.e("LBS","LocationDescribe: "+bdLocation.getLocationDescribe());
            Log.e("LBS", "poiList:"+poiList.get(0).getName() );
        }
    }
}
