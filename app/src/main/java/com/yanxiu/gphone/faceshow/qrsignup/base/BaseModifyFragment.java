package com.yanxiu.gphone.faceshow.qrsignup.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseModifyFragment extends Fragment {

    private SysUserBean sysUserBean;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public BaseModifyFragment(SysUserBean userBean){
        this.sysUserBean=userBean;
    }



}
