package com.example.charmer.moving.friendchat;

import android.app.Activity;

import com.example.charmer.moving.contantData.HttpUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by lenovo on 2016/11/3.
 */

public class PostFriend {
    private static Activity C;
    public PostFriend() {
    }

    //请求人id，被请求人id，被请求人名字
    public void postfriend(Integer userid,Integer getid,String getname){
        RequestParams params=new RequestParams(HttpUtils.host4+"friendservlet");
        params.addQueryStringParameter("choice",4+"");
        params.addQueryStringParameter("userid",userid+"");
        params.addQueryStringParameter("getid",getid+"");
        params.addQueryStringParameter("getname",getname+"");
        params.addQueryStringParameter("state",0+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
