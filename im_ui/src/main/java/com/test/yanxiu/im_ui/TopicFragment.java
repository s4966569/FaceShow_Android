package com.test.yanxiu.im_ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

}
