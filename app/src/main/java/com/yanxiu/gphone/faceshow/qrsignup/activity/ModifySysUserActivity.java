package com.yanxiu.gphone.faceshow.qrsignup.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyNameFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyPhoneFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifySchoolFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifySexFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifyStageFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.ModifySubjectFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetProfileFragment;

/**
 * 扫码注册用户 信息设置界面
 */
public class ModifySysUserActivity extends FaceShowBaseActivity {
    private final String TAG = getClass().getSimpleName();

    /**
     * 以注册的用户信息
     */
    private SysUserBean registedUserBean;

    /**
     * 用户信息 展示界面
     */
    private SetProfileFragment profileFragment;
    /**
     * 修改信息界面
     * 懒加载 设置界面
     */
    private ModifyNameFragment modifyNameFragment;
    private ModifySchoolFragment modifySchoolFragment;
    private ModifyPhoneFragment modifyPhoneFragment;
    private ModifySexFragment modifySexFragment;
    private ModifyStageFragment modifyStageFragment;
    private ModifySubjectFragment modifySubjectFragment;

    private String targetClazsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_sys_user);


        Bundle bundle = getIntent().getBundleExtra("data");
        /*获取传递过来的用户信息 */
        registedUserBean = (SysUserBean) bundle.getSerializable("user");
        int userType = bundle.getInt("type");
        /*获取传递过来的班级信息 */
        targetClazsName=bundle.getString("className");
        /*初始化 fragment*/
        fragmentInit(userType);
        listenerInit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment).commit();
    }

    private void fragmentInit(int userType) {
    /*fragment 初始化*/
        profileFragment = new SetProfileFragment();

        profileFragment.setSysUserBean(registedUserBean);
//        Log.i(TAG, "onCreate: type : " + userType);
        if (userType == 0) {
            profileFragment.hideNotice();
        } else {
            profileFragment.showNotice();
        }
    }

    private void listenerInit() {
        /*toolbar 点击监听*/
        profileFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                ModifySysUserActivity.this.finish();
            }

            @Override
            public void onRightComponentClick() {
                /*点击了保存 并且成功*/
            }
        });

        /*profile Item 点击监听 */
        profileFragment.setItemClickListener(new SetProfileFragment.ProfileItemClickListener() {
            @Override
            public void onNameItemClicked() {
                /*当点击了 姓名 item*/
                modifyName();
            }

            @Override
            public void onPhoneItemClicked() {
                /*点击了 电话*/
                modifyPhone();
            }

            @Override
            public void onSexItemClicked() {
                /*点击了性别*/
                modifyGender();
            }

            @Override
            public void onStageItemClicked() {
                /*点击了 学段与学科*/
                modifyStageAndSubj();
            }

            @Override
            public void onSchoolItemClicked() {
                /*点击了编辑学校名称*/
                modifySchool();
            }
        });
    }

    /**
     * 设置学校
     */
    private void modifySchool() {
        isProfilePage = false;
        if (modifySchoolFragment == null) {
            modifySchoolFragment = new ModifySchoolFragment();
            modifySchoolFragment.setUserBean(registedUserBean);
            modifySchoolFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                @Override
                public void onLeftComponentClick() {
                    /*点击返回 返回到 信息展示*/
                    backToProfileFragment();
                }

                @Override
                public void onRightComponentClick() {
                    /*点击保存 保存后返回到信息展示*/
                    backToProfileFragment();
                }
            });
        }
        /*切换到 设置姓名fragment*/
        transaction(modifySchoolFragment);
    }


    /**
     * 设置姓名
     */
    private void modifyName() {
        isProfilePage = false;
        if (modifyNameFragment == null) {
            modifyNameFragment = new ModifyNameFragment();
            modifyNameFragment.setUserBean(registedUserBean);
            modifyNameFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                @Override
                public void onLeftComponentClick() {
                    /*点击返回 返回到 信息展示*/
                    backToProfileFragment();
                }

                @Override
                public void onRightComponentClick() {
                    /*点击保存 保存后返回到信息展示*/
                    backToProfileFragment();
                }
            });
        }
        /*切换到 设置姓名fragment*/
        transaction(modifyNameFragment);
    }

    /**
     * 设置 学段
     */
    private void modifyStageAndSubj() {
        isProfilePage = false;
                /*学段 学科 有两段选择*/
        if (modifyStageFragment == null) {
            modifyStageFragment = new ModifyStageFragment();
            modifyStageFragment.setUserBean(registedUserBean);
            modifyStageFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                @Override
                public void onLeftComponentClick() {
                    backToProfileFragment();
                }

                @Override
                public void onRightComponentClick() {
                    /*在学段界面点击了下一步 进入 学科设置界面*/
                    toModifySubject();
                }
            });
        }
        transaction(modifyStageFragment);
    }

    /**
     * 设置性别
     */
    private void modifyGender() {
        showGeneralPopWindow();
    }

    private void showGeneralPopWindow() {
        final PopupWindow genderPopWindow;
        View contentView = getLayoutInflater().inflate(R.layout.popwindow_select_gender, null);
        genderPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        genderPopWindow.setAnimationStyle(R.style.pop_anim);
        genderPopWindow.setFocusable(true);
        genderPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        genderPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        TextView genderMale = contentView.findViewById(R.id.gender_male);
        TextView genderFemale = contentView.findViewById(R.id.gender_female);
        TextView cancleBtn = contentView.findViewById(R.id.tv_cancel);
//        点击 性别女
        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registedUserBean.setSex(0);
                registedUserBean.setSexName("女");
                genderPopWindow.dismiss();
            }
        });
//        点击性别男
        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registedUserBean.setSex(1);
                registedUserBean.setSexName("男");
                genderPopWindow.dismiss();
            }
        });
//        点击取消
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderPopWindow.dismiss();
            }
        });

    }

    /**
     * 设置电话号
     */
    private void modifyPhone() {
        isProfilePage = false;
        if (modifyPhoneFragment == null) {
            modifyPhoneFragment = new ModifyPhoneFragment();
            modifyPhoneFragment.setUserBean(registedUserBean);
            modifyPhoneFragment.setToolbarActionCallback(new ToolbarActionCallback() {
                @Override
                public void onLeftComponentClick() {
                    backToProfileFragment();
                }

                @Override
                public void onRightComponentClick() {
                    backToProfileFragment();
                }
            });
        }
        transaction(modifyPhoneFragment);

    }


    /**
     * 设置 学科
     */
    private void toModifySubject() {
        modifySubjectFragment = new ModifySubjectFragment();
        modifySubjectFragment.setmSelectedPosition(modifyStageFragment.getmSelectedPosition());
        modifySubjectFragment.setReselected(modifyStageFragment.isReSelected);
        modifySubjectFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                /*返回到 学段设置 这里的返回动画 需要注意一下*/
//                transaction(modifyStageFragment);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.qr_fragment_slide_in_left, R.anim.qr_fragment_slide_out_right)
                        .replace(R.id.fragment_container, modifyStageFragment)
                        .commit();
            }

            @Override
            public void onRightComponentClick() {
                /*点击保存 返回到 信息展示*/
                backToProfileFragment();
            }
        });
        modifySubjectFragment.setTitleText(modifyStageFragment.getStageText());
        modifySubjectFragment.setUserBean(registedUserBean);

        transaction(modifySubjectFragment);
    }

    /**
     * 切换fragment
     */
    private void transaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.qr_fragment_slide_in_right, R.anim.qr_fragment_slide_out_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private boolean isProfilePage = true;

    /**
     * 返回到 用户信息
     */
    private void backToProfileFragment() {
        isProfilePage = true;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.qr_fragment_slide_in_left, R.anim.qr_fragment_slide_out_right)
                .replace(R.id.fragment_container, profileFragment).commit();
    }


    /**
     * 处理一下 在设置界面的时候先返回到 信息展示界面
     */
    @Override
    public void onBackPressed() {
        if (isProfilePage) {
            super.onBackPressed();
        } else {
            backToProfileFragment();
        }
    }
}
