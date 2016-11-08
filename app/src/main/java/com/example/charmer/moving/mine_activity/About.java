package com.example.charmer.moving.mine_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.charmer.moving.R;
import com.example.charmer.moving.utils.StatusBarCompat;

public class About extends AppCompatActivity {

    private RelativeLayout finishthis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        initView();
    }

    private void initView() {
        finishthis = (RelativeLayout) findViewById(R.id.finishthis);
        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

}
