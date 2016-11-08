package com.example.charmer.moving;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Create by asus on 2016/11/3.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences("sp_mobile", Context.MODE_PRIVATE);
                        String mobile =sharedPreferences.getString("number", "");
                        String userId =sharedPreferences.getString("userId", "");
                        String useraccount =sharedPreferences.getString("useraccount", "");
                        System.out.println(mobile+"=============="+userId+"=-=-=-=-=-=-="+useraccount);
                        if(mobile.length()>0){
                            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
                            intent.putExtra("userId",userId);
                            intent.putExtra("useraccount",useraccount);
                            startActivity(intent);
                        }else {
                            Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }, 2000);

    }
}
