package com.debasish.demomap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.debasish.demomap.ui.activity.home.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class LocationInfoDialog extends Dialog {

    @BindViews({R.id.message,R.id.mobile,R.id.amount,R.id.textbal})
    List<TextView> textViews;
    @BindView(R.id.okbtn)
    Button okbtn;
    private Activity activity;
    private ArrayList<String> stringArrayList;

    public LocationInfoDialog(Activity activity, ArrayList<String> stringArrayList) {
        super(activity);
        this.activity = activity;
        this.stringArrayList = stringArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.info_dialog);
            ButterKnife.bind(this);
            textViews.get(0).setText("Address"+stringArrayList.get(0));
        textViews.get(1).setText(stringArrayList.get(1)+""+stringArrayList.get(2));
        textViews.get(2).setText(stringArrayList.get(6));
        textViews.get(3).setText( stringArrayList.get(7));
            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                    dismiss();
                    activity.finish();

                }
            });

    }




}
