package com.yanxiu.gphone.faceshow.qrsignup.activity;

import android.os.Bundle;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyNameFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyPhoneFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifySexFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyStageFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifySysInfoCallback;

/**
 * 本来想复用 用户信息的设置界面
 * 但请求方式不同
 */
public class ModifySysUserActivity extends FaceShowBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_sys_user);

        setModifyPage();

    }

    private void setModifyPage() {
        int page = getIntent().getIntExtra("page", 0);
        switch (page) {
            case 1:
                ModifyNameFragment modifyNameFragment=new ModifyNameFragment();
                modifyNameFragment.setModifySysInfoCallback(new ModifySysInfoCallback() {
                    @Override
                    public void onModifyed(String content) {

                    }
                });
                modifyNameFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                    @Override
                    public void onLeftComponentClick() {

                    }

                    @Override
                    public void onRightComponentClick() {

                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,modifyNameFragment).commit();
                break;
            case 2:
                ModifyPhoneFragment modifyPhoneFragment=new ModifyPhoneFragment();
                modifyPhoneFragment.setModifySysInfoCallback(new ModifySysInfoCallback() {
                    @Override
                    public void onModifyed(String content) {

                    }
                });
                modifyPhoneFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                    @Override
                    public void onLeftComponentClick() {

                    }

                    @Override
                    public void onRightComponentClick() {

                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,modifyPhoneFragment).commit();
                break;
            case 3:
                ModifySexFragment modifySexFragment=new ModifySexFragment();
                modifySexFragment.setModifySysInfoCallback(new ModifySysInfoCallback() {
                    @Override
                    public void onModifyed(String content) {

                    }
                });
                modifySexFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                    @Override
                    public void onLeftComponentClick() {

                    }

                    @Override
                    public void onRightComponentClick() {

                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,modifySexFragment).commit();
                break;
            case 4:
                ModifyStageFragment modifyStageFragment=new ModifyStageFragment();
                modifyStageFragment.setModifySysInfoCallback(new ModifySysInfoCallback() {
                    @Override
                    public void onModifyed(String content) {

                    }
                });
                modifyStageFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                    @Override
                    public void onLeftComponentClick() {

                    }

                    @Override
                    public void onRightComponentClick() {

                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,modifyStageFragment).commit();
                break;
            default:
                break;
        }
    }


}
