package com.example.charmer.moving.friendchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.charmer.moving.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Test extends AppCompatActivity {

    @InjectView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.button)
    public void onClick() {
        Toast.makeText(Test.this,"click",Toast.LENGTH_SHORT).show();

        RongImChat rong=new RongImChat();
        rong.startChat("18862603077");
    }
}
