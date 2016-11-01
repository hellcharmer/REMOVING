package com.example.charmer.moving.MyApplicition;

import android.app.Application;
import android.content.Context;

import com.example.charmer.moving.LoginActivity;
import com.example.charmer.moving.pojo.User;

import org.xutils.BuildConfig;
import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * Created by Charmer on 2016/9/13.
 */
public class MyApplication extends Application{
     static Integer userid= LoginActivity.getUserid();
    static String useraccount=LoginActivity.getUseraccount();
    public static User getUser() {
        return user;
    }
   public static User user =new User(userid,useraccount);//设置一个默认用户

    public  void setUser(User user) {
        this.user = user;
    }
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        context = getApplicationContext();
        RongIM.init(this);
    }
    public static Context getContext(){return context;}
}
