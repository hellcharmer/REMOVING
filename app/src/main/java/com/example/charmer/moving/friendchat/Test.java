package com.example.charmer.moving.friendchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.charmer.moving.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Test extends AppCompatActivity {
private List<String> userids=new ArrayList<String>();
    @InjectView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RongImChat.A=this;重要
        CreateQunImpl.B=this;
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.button)
    public void onClick() {
        Toast.makeText(Test.this,"click",Toast.LENGTH_SHORT).show();
        userids.add("1");
        userids.add("2");

        CreateQunImpl createQun=new CreateQunImpl();


    }
}
