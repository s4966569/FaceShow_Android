package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyStageFragment extends Fragment {


    public ModifyStageFragment() {
        // Required empty public constructor
    }


    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root==null) {
            root=inflater.inflate(R.layout.activity_modify_user_name, null);
        }
        viewInit(root);
        return root;
    }

    private void viewInit(View root ){
        ImageView backView=root.findViewById(R.id.title_layout_left_img);
        TextView titleTxt=root.findViewById(R.id.title_layout_title);
        TextView rightTxt=root.findViewById(R.id.title_layout_right_txt);
        final EditText editText=root.findViewById(R.id.edt_name);

        rightTxt.setText("保存");
        titleTxt.setText("编辑姓名");
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });

        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifySysInfoCallback != null) {
                    modifySysInfoCallback.onModifyed(editText.getText().toString());
                }
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }

            }
        });

    }

    private ModifySysInfoCallback modifySysInfoCallback;

    public void setModifySysInfoCallback(ModifySysInfoCallback modifySysInfoCallback) {
        this.modifySysInfoCallback = modifySysInfoCallback;
    }

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

}
