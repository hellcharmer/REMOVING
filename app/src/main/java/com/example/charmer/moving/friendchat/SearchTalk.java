package com.example.charmer.moving.friendchat;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchTalk extends AppCompatActivity  {
    @InjectView(R.id.finish)
    TextView finish;
    @InjectView(R.id.tvpeople)
    TextView tvpeople;
    @InjectView(R.id.llsearchp)
    RelativeLayout llsearchp;
    @InjectView(R.id.tvqun)
    TextView tvqun;
    @InjectView(R.id.rlsearch2)
    RelativeLayout rlsearch2;
    private RelativeLayout rl_search;
    private EditText rdsearsr;
    private FrameLayout frsearch;
    private ImageView iv_cancell;
    public static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_talk);
        ButterKnife.inject(this);
        initView();
        initData();
        initEvt();

    }

    private void initView() {
        switchFragment(new Fragment_search1());
        iv_cancell = ((ImageView) findViewById(R.id.iv_cancel1));
        rdsearsr = ((EditText) findViewById(R.id.edsearchsr));
        rl_search = ((RelativeLayout) findViewById(R.id.rl_search));
        frsearch = ((FrameLayout) findViewById(R.id.frsearch));

    }

    private void initData() {
        rdsearsr.addTextChangedListener(textWatcher);
    }

    private void initEvt() {
        //群搜框
        rlsearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsearchp.setVisibility(View.GONE);
                rlsearch2.setVisibility(View.GONE);
                switchFragment(new Fragment_search3());
                Toast.makeText(SearchTalk.this,"找组织(⊙＿⊙)",Toast.LENGTH_SHORT).show();
            }
        });
        //大取消
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //取消小按钮
        iv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdsearsr.setText("");
                tvpeople.setText("");
                tvqun.setText("");
                switchFragment(new Fragment_search1());
            }
        });
        //fragment
        frsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_cancell.setVisibility(View.GONE);
                llsearchp.setVisibility(View.GONE);
                rlsearch2.setVisibility(View.GONE);
                rdsearsr.setFocusable(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        //搜索框
        rdsearsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsearchp.setVisibility(View.VISIBLE);
                rlsearch2.setVisibility(View.VISIBLE);
                iv_cancell.setVisibility(View.VISIBLE);
                rdsearsr.setFocusable(true);
                rdsearsr.setFocusableInTouchMode(true);
                rdsearsr.requestFocus();
                rdsearsr.requestFocusFromTouch();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

            }
        });
        //人搜索框
        llsearchp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsearchp.setVisibility(View.GONE);
                rlsearch2.setVisibility(View.GONE);
                switchFragment(new Fragment_search2());
                Toast.makeText(SearchTalk.this,"找人去(⊙＿⊙)",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void switchFragment(Fragment fragment) {
        this.getFragmentManager().beginTransaction().replace(R.id.frsearch, fragment).commit();
    }
   //搜索文本框变化
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            llsearchp.setVisibility(View.VISIBLE);
            rlsearch2.setVisibility(View.VISIBLE);
            Log.i("onTextChanged",rdsearsr.getText().toString());
            tvpeople.setText(rdsearsr.getText().toString());
            tvqun.setText(rdsearsr.getText().toString());
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            Log.i("beforeTextChanged",rdsearsr.getText().toString());
        }
        @Override
        public void afterTextChanged(Editable s) {
            Log.i("afterTextChanged",rdsearsr.getText().toString());
               setContent(rdsearsr.getText().toString().trim());
        }
    };

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        SearchTalk.content = content;
    }
}
