package com.test.yanxiu.im_ui.contacts.view;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;

/**
 * Created by frc on 2018/3/13.
 */

public class FSSearchView  extends SearchView {

    OnQueryTextListener onQueryTextListener;

    public FSSearchView(Context context) {
        super(context);
    }

    public FSSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FSSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
    }

    public interface OnQueryTextListener {
        void queryTextChanged(String query);
    }
}
