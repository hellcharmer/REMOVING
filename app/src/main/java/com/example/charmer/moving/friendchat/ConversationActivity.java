package com.example.charmer.moving.friendchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.charmer.moving.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Toast.makeText(ConversationActivity.this, "长按", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, io.rong.imlib.model.Message message) {
                return false;
            }



            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, io.rong.imlib.model.Message message) {
                return false;
            }

        });
    }
}
