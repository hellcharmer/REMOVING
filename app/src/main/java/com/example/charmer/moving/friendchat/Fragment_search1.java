package com.example.charmer.moving.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.charmer.moving.R;

/**
 * Created by lenovo on 2016/10/27.
 */

public class Fragment_search1 extends mBaseFragment{

    private ImageView chuangjian;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search1, null);
        return v;
    }

    @Override
    public void initView() {
        chuangjian = ((ImageView) getView().findViewById(R.id.创建));

    }

    @Override
    public void initEvent() {
       chuangjian.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(getActivity(),"创群啦≥▽≤",Toast.LENGTH_SHORT).show();
               Intent intent=new Intent(getActivity(),Createqun.class);
               startActivity(intent);
           }
       });
    }

    @Override
    public void initData() {

    }


}
