package com.example.charmer.moving.friendchat;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lenovo on 2016/10/27.
 */

public class RongImChat {
    public static Activity A;
    private User user;
    private String account;
    private String Token;
   public RongImChat(){}
    public RongImChat(String account) {
        this.account = account;
    }
    public void startChat (String account){

        RequestParams requestParams =new RequestParams(HttpUtils.host4+"getuser");
        requestParams.addQueryStringParameter("choice",1+"");
        requestParams.addQueryStringParameter("account",account);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("result:",result);
                user=new User();
                Gson gson=new Gson();
                Type type=new TypeToken<User>(){}.getType();
                user=gson.fromJson(result,type);
                connectRong();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
               Log.e("errorconn:", ex+"");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    public void connectRong(){
        Token= MainActivity.getToken();
        //链接融云
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserid() + "",user.getUsername(), Uri.parse(HttpUtils.host4 + user.getUserimg())));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("errorr:",errorCode+"");
            }

            @Override
            public void onTokenIncorrect() {
               Log.e("errorr:","tokencuowu");
            }
        });
        RongIM.getInstance().startPrivateChat(A, user.getUserid() + "",user.getUsername());
    }
}
