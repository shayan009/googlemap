package com.debasish.demomap.util;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.debasish.demomap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RToast extends Toast {

    @BindView(R.id.tvText)
    AppCompatTextView tvText;

    private Context context;

    public RToast(Context context) {
        super(context);
        this.context = context;
    }

    public void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        setView(view);
        ButterKnife.bind(this, view);
    }

    public RToast message(String message) {
        tvText.setText(message);
        return this;
    }

    public RToast message(int stringResId) {
        tvText.setText(context.getString(stringResId));
        return this;
    }
}
