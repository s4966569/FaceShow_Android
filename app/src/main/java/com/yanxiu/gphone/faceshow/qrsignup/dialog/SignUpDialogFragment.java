package com.yanxiu.gphone.faceshow.qrsignup.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yanxiu.gphone.faceshow.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpDialogFragment extends DialogFragment {


    public SignUpDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_sign_up_dialog, container, false);
        Button confirmBtn=root.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return root;
    }

}
