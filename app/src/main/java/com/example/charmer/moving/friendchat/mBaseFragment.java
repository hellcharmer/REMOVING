package com.example.charmer.moving.friendchat;

import android.app.Fragment;
import android.os.Bundle;


/**
 * Created by lenovo on 2016/10/27.
 */

public abstract  class mBaseFragment extends Fragment {

    //找控件
    //界面数据初始化
    //设置事件


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    public abstract  void initView();//找控件
    public abstract  void initEvent();//设置控件的事件
    public abstract  void initData();//设置界面初始值
}
