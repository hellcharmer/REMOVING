package com.example.charmer.moving.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by lenovo on 2016/10/9.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }
    public abstract  void initView();//找控件
    public abstract  void initEvent();//设置控件的事件
    public abstract  void initData();//设置界面初始值
}
