package com.test.yanxiu.im_ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.http.ImRequestBase;
import com.test.yanxiu.im_core.http.ImResponseBase;
import com.test.yanxiu.im_core.http.PolicyConfigRequest;
import com.test.yanxiu.im_core.http.PolicyConfigResponse;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends FaceShowBaseFragment {


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        test();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    private void test() {
        PolicyConfigRequest r = new PolicyConfigRequest();
        r.startRequest(PolicyConfigResponse.class, new HttpCallback<PolicyConfigResponse>() {
            @Override
            public void onSuccess(RequestBase request, PolicyConfigResponse ret) {
                Log.e("Tag", ret.toString());
            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }
}
