package com.example.firebasepushsdk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class ViewPushActivity extends AppCompatActivity {

    public TextView tvInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_push_default);
        tvInfo = (TextView) findViewById(R.id.tvInfoPush);
        Bundle bundle = getIntent().getBundleExtra("data_push");
        Date currentTime = Calendar.getInstance().getTime();
        if (bundle != null) {
            tvInfo.setText(bundle.getString("title")
                    + "\n" + bundle.getString("body"));
                    /*+ "\n" + bundle.getLong("time")
                    + "\n" + currentTime.getTime());*/
        }
    }
}
