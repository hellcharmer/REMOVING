package com.example.charmer.moving.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.charmer.moving.Homepage;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.mine_activity.About;
import com.example.charmer.moving.mine_activity.Articles;
import com.example.charmer.moving.mine_activity.Fankui;
import com.example.charmer.moving.mine_activity.Personal_information;
import com.example.charmer.moving.mine_activity.Shoucang;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.relevantexercise.ManagerexeActivity;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Charmer on 2016/9/19.
 */
public class Fragment_mine extends Fragment implements View.OnClickListener {
    View view;
    View view_qrcode;
    private TextView tv_user_name;
    private ImageView iv_head;
    private RelativeLayout ib_qrcode;
    private ImageView articles;
    private ImageView iv_enter1;
    private RelativeLayout person_rl_articles;
    private ImageView iv_dynamic;
    private ImageView iv_enter2;
    private RelativeLayout person_rl_dynamic;
    private ImageView iv_enter3;
    private ImageView iv_exercise;
    private RelativeLayout person_rl_exercise;
    private ImageView shoucang;
    private ImageView iv_enter4;
    private RelativeLayout person_rl_shoucang;
    private ImageView feedback;
    private ImageView iv_enter5;
    private RelativeLayout person_rl_fankui;
    private ImageView about;
    private ImageView iv_enter6;
    private RelativeLayout person_rl_about;
    public static RelativeLayout rl_qrcode;
    private ImageView myqrcode;
    private TextView edit_personalinfo;
    String useraccount;
    String userId;
    String userimg;
    String username;
    String userqrcode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);

        initView();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sp_mobile", Context.MODE_PRIVATE);
        userimg = sharedPreferences.getString("userimg", "");
        username = sharedPreferences.getString("username", "");
        userId = sharedPreferences.getString("userId", "");
        useraccount = sharedPreferences.getString("useraccount", "");
        userqrcode = sharedPreferences.getString("myqrcode", "");
        tv_user_name.setText(username);
        File cacheDir = new File("/data/data/com.example.charmer.moving/QRcodepicture/"+myqrcode);
        File cacheDir1 = new File("/data/data/com.example.charmer.moving/myImg/"+userimg);

        if (cacheDir.exists()&&cacheDir1.exists()) {
            String head = "/data/data/com.example.charmer.moving/myImg/"+userimg;
            String qrcode = "/data/data/com.example.charmer.moving/QRcodepicture/"+userqrcode;
            System.out.println("123445678876246543265432=========");
            Bitmap bm = BitmapFactory.decodeFile(head);
            Bitmap bm1 = BitmapFactory.decodeFile(qrcode);
            iv_head.setImageBitmap(bm);
            myqrcode.setImageBitmap(bm1);

        } else {
            //从网络拿数据
            System.out.println("------------22222222");
            getInfo();
        }
    }

    private void getInfo() {
        RequestParams params = new RequestParams(HttpUtils.hoster + "getpersonalinfo");//upload 是你要访问的servlet
        params.addQueryStringParameter("user",useraccount);
        params.addQueryStringParameter("state","5");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                final VariableExercise.DataSummary dataSummary=gson.fromJson(result,VariableExercise.DataSummary.class);
                //网络图片下载到本地
                System.out.println("=============33333333"+dataSummary.userImg);
                Glide.with(getActivity()).load(HttpUtils.hoster+"upload/"+dataSummary.userImg).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv_head.setImageBitmap(resource);
                        saveBitmaptofile_Img(resource,dataSummary.userImg);
                    }
                }); //方法中设置asBitmap可以设置回调类型
                Glide.with(getActivity()).load(HttpUtils.hoster+"qrcode/"+dataSummary.QRcode).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        myqrcode.setImageBitmap(resource);
                        saveBitmaptofile_Qrcode(resource,dataSummary.QRcode);
                    }
                }); //方法中设置asBitmap可以设置回调类型
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

    private void initView() {


        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_user_name.setOnClickListener(this);
        iv_head = (ImageView) view.findViewById(R.id.iv_head);
        iv_head.setOnClickListener(this);
        ib_qrcode = (RelativeLayout) view.findViewById(R.id.ib_qrcode);
        ib_qrcode.setOnClickListener(this);
        articles = (ImageView) view.findViewById(R.id.articles);
        articles.setOnClickListener(this);
        iv_enter1 = (ImageView) view.findViewById(R.id.iv_enter1);
        iv_enter1.setOnClickListener(this);
        person_rl_articles = (RelativeLayout) view.findViewById(R.id.person_rl_articles);
        person_rl_articles.setOnClickListener(this);
        iv_dynamic = (ImageView) view.findViewById(R.id.iv_dynamic);
        iv_dynamic.setOnClickListener(this);
        iv_enter2 = (ImageView) view.findViewById(R.id.iv_enter2);
        iv_enter2.setOnClickListener(this);
        person_rl_dynamic = (RelativeLayout) view.findViewById(R.id.person_rl_dynamic);
        person_rl_dynamic.setOnClickListener(this);
        iv_enter3 = (ImageView) view.findViewById(R.id.iv_enter3);
        iv_enter3.setOnClickListener(this);
        iv_exercise = (ImageView) view.findViewById(R.id.iv_exercise);
        iv_exercise.setOnClickListener(this);
        person_rl_exercise = (RelativeLayout) view.findViewById(R.id.person_rl_exercise);
        person_rl_exercise.setOnClickListener(this);
        shoucang = (ImageView) view.findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        iv_enter4 = (ImageView) view.findViewById(R.id.iv_enter4);
        iv_enter4.setOnClickListener(this);
        person_rl_shoucang = (RelativeLayout) view.findViewById(R.id.person_rl_shoucang);
        person_rl_shoucang.setOnClickListener(this);
        feedback = (ImageView) view.findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
        iv_enter5 = (ImageView) view.findViewById(R.id.iv_enter5);
        iv_enter5.setOnClickListener(this);
        person_rl_fankui = (RelativeLayout) view.findViewById(R.id.person_rl_fankui);
        person_rl_fankui.setOnClickListener(this);
        about = (ImageView) view.findViewById(R.id.about);
        about.setOnClickListener(this);
        iv_enter6 = (ImageView) view.findViewById(R.id.iv_enter6);
        iv_enter6.setOnClickListener(this);
        person_rl_about = (RelativeLayout) view.findViewById(R.id.person_rl_about);
        person_rl_about.setOnClickListener(this);

        rl_qrcode = (RelativeLayout) view.findViewById(R.id.rl_qrcode);
        rl_qrcode.setOnClickListener(this);
        myqrcode = (ImageView) view.findViewById(R.id.myqrcode);

        edit_personalinfo = (TextView) view.findViewById(R.id.edit_personalinfo);
        edit_personalinfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_qrcode:

                rl_qrcode.setVisibility(View.VISIBLE);
                rl_qrcode.setBackgroundColor(Color.parseColor("#aa000000"));
                break;

            case R.id.rl_qrcode:
                rl_qrcode.setVisibility(View.GONE);
                break;
            case R.id.iv_head:
                Intent intent = new Intent(getActivity(), Homepage.class);
                intent.putExtra("user",useraccount);
                startActivity(intent);
                break;
            case R.id.edit_personalinfo:
                Intent intent_personalinfo = new Intent(getActivity(), Personal_information.class);
                startActivity(intent_personalinfo);
                break;
            case R.id.person_rl_articles:
                Intent intent_articles = new Intent(getActivity(),Articles.class);
                startActivity(intent_articles);
                break;
            case R.id.person_rl_dynamic:

                break;
            case R.id.person_rl_exercise:
                Intent intent_exercise = new Intent(getActivity(),ManagerexeActivity.class);
                startActivity(intent_exercise);
                break;
            case R.id.person_rl_shoucang:
                Intent intent_shoucang = new Intent(getActivity(),Shoucang.class);
                startActivity(intent_shoucang);
                break;
            case R.id.person_rl_fankui:
                Intent intent_fankui = new Intent(getActivity(), Fankui.class);
                startActivity(intent_fankui);
                break;
            case R.id.person_rl_about:
                Intent intent_about = new Intent(getActivity(), About.class);
                startActivity(intent_about);
                break;
        }
    }

    static boolean saveBitmaptofile_Img(Bitmap bmp,String mobile){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            File cacheDir = new File("/data/data/com.example.charmer.moving/myImg/");//设置目录参数
            if(cacheDir.exists()){

            }else{
                cacheDir.mkdirs();//新建目录
            }

            stream = new FileOutputStream("/data/data/com.example.charmer.moving/myImg/"+mobile);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }
    static boolean saveBitmaptofile_Qrcode(Bitmap bmp,String mobile){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            File cacheDir = new File("/data/data/com.example.charmer.moving/QRcodepicture/");//设置目录参数
            if(cacheDir.exists()){

            }else{
                cacheDir.mkdirs();//新建目录
            }

            stream = new FileOutputStream("/data/data/com.example.charmer.moving/QRcodepicture/"+mobile);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

}
