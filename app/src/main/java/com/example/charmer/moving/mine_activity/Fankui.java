package com.example.charmer.moving.mine_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.R;
import com.example.charmer.moving.utils.StatusBarCompat;


public class Fankui extends AppCompatActivity {
    private RelativeLayout finishthis;
    private TextView send_advice;
    private EditText et_write_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        et_write_comment = (EditText) findViewById(R.id.et_write_comment);
        send_advice = (TextView) findViewById(R.id.send_advice);
        finishthis = (RelativeLayout) findViewById(R.id.finishthis);

        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });
        send_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=et_write_comment.getText().toString();
//                sendAdvice(str);
                Toast.makeText(Fankui.this,"感谢你的意见",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    private void sendAdvice(String advice) {
//        RequestParams params = new RequestParams(HttpUtils.hoster + " ");
//        params.addQueryStringParameter("advice", advice);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Intent intent = new Intent(Fankui.this, Fragment_mine.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(Fankui.this,"发送失败",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }





}
