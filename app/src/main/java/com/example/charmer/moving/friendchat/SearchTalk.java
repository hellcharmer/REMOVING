package com.example.charmer.moving.friendchat;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.charmer.moving.R;

public class SearchTalk extends AppCompatActivity {


    private RelativeLayout rl_search;
    private EditText rdsearsr;
    private FrameLayout frsearch;
    private ImageView iv_cancell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_talk);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//为什么不弹出
        initView();
        initData();
        initEvt();

    }

    private void initView() {
        iv_cancell = ((ImageView) findViewById(R.id.iv_cancel1));
        rdsearsr = ((EditText) findViewById(R.id.edsearchsr));
        rl_search = ((RelativeLayout) findViewById(R.id.rl_search));
        frsearch = ((FrameLayout) findViewById(R.id.frsearch));
    }
    private void initData() {

    }
    private void initEvt() {
        iv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdsearsr.setText("");
            }
        });
        frsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_cancell.setVisibility(View.GONE);
                rdsearsr.setFocusable(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        rdsearsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_cancell.setVisibility(View.VISIBLE);
                rdsearsr.setFocusable(true);
                rdsearsr.setFocusableInTouchMode(true);
                rdsearsr.requestFocus();
                rdsearsr.requestFocusFromTouch();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        this.getFragmentManager().beginTransaction().replace(R.id.frsearch,fragment).commit();
    }
}
