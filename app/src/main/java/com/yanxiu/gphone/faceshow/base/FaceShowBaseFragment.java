package com.yanxiu.gphone.faceshow.base;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.yanxiu.gphone.faceshow.constant.Constants;

/**
 * Created by 戴延枫 on 2017/9/13.
 */
public class FaceShowBaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        Log.i(Constants.TAG, this.getClass().getName());
    }
}
