package com.example.charmer.moving.MyApplicition;

import android.app.Application;
import android.content.Context;

import com.example.charmer.moving.BuildConfig;
import com.example.charmer.moving.pojo.User;

import org.xutils.x;

/**
 * Created by Charmer on 2016/9/13.
 */
public class MyApplication extends Application{

    private User user =new User(1);//设置一个默认用户
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        context = getApplicationContext();

    }
    public static Context getContext() {
        return context;
    }

}
