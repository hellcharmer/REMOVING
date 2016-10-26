package com.example.charmer.moving;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.contantData.Constant;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.fragment.Fragment_dynamic;
import com.example.charmer.moving.fragment.Fragment_friend;
import com.example.charmer.moving.fragment.Fragment_home;
import com.example.charmer.moving.fragment.Fragment_mine;
import com.example.charmer.moving.fragment.Fragment_service;
import com.example.charmer.moving.home_activity.Publish_articles;
import com.example.charmer.moving.pojo.User;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private boolean clicked = false;// 记录加号按钮的点击状态，默认为没有点击

    private RelativeLayout plus_rl;
    private ImageView plus_im;
    private ImageView plus_yuan;
    private ImageView iv_fabuhuodong;
    private ImageView iv_write;
    private TextView dishui_tv, guoshui_tv;
    //lzy的改动
    private ImageView iv_huan;
    private Integer j;
    private Button btn_friends;
    private List<User> usersToken=new ArrayList<User>();//所有user的Token
    public static String Token;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //handleMessage界面更新
                case 0:
                    iv_huan.setVisibility(View.GONE);
                    btn_friends.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    //lzy的改动
    private Animation rotate_anticlockwise, rotate_clockwise, scale_max,
            scale_min, alpha_button;
    android.support.v4.app.Fragment[] fragments;
    Fragment_home fragment_home;//主页
    Fragment_mine fragment_mine;
    Fragment_service fragment_service;
    Fragment_dynamic fragment_info;
    Fragment_friend fragment_friends;
    //按钮的数组，一开始第一个按钮被选中
    Button[] tabs;

    Drawable startDra;
    int oldIndex;//用户看到的item
    int newIndex;//用户即将看到的item



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//       getusertoken();
          yiburenwu2();
        //BP.init();
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));

        initData();
        initView();
        setListeners();
        //获取屏幕高度和宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constant.displayWidth = displayMetrics.widthPixels;
        Constant.displayHeight = displayMetrics.heightPixels;
        //初始化模糊界面
        plus_rl.setClickable(false);
        //初始化fragment
        fragment_home = new Fragment_home();
        fragment_mine = new Fragment_mine();
        fragment_service = new Fragment_service();
        fragment_info = new Fragment_dynamic();
        fragment_friends = new Fragment_friend();

        //所有fragment的数组
        fragments = new Fragment[]{fragment_home, fragment_info, fragment_service, fragment_friends, fragment_mine};


        //设置按钮的数组
        tabs = new Button[5];
        tabs[0] = (Button) findViewById(R.id.home);//主页的button
        tabs[1] = (Button) findViewById(R.id.communication);//主页的button
        tabs[2] = (Button) findViewById(R.id.exercise);//主页的button
        tabs[3] = (Button) findViewById(R.id.friends);//主页的button
        tabs[4] = (Button) findViewById(R.id.mine);//主页的button
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, fragments[0]).commit();
        tabs[0].setSelected(true);
        plus_yuan.setVisibility(View.VISIBLE);
        plus_im.setVisibility(View.VISIBLE);
    }


    //按钮的点击事件:选中不同的按钮，不同的fragment显示
    public void onTabClicked(View view) {

        //点击按钮时，表示选中不同的项
        switch (view.getId()) {
            case R.id.home:
                newIndex = 0;//选中第一项
                plus_yuan.setVisibility(View.VISIBLE);
                plus_im.setVisibility(View.VISIBLE);
                break;

            case R.id.communication:
                newIndex = 1;//选中第二项
                plus_yuan.setVisibility(View.GONE);
                plus_im.setVisibility(View.GONE);
                break;
            case R.id.exercise:
                newIndex = 2;//选中第三项
                plus_yuan.setVisibility(View.GONE);
                plus_im.setVisibility(View.GONE);
                break;
            case R.id.friends:
                newIndex = 3;//选中第四项
                plus_yuan.setVisibility(View.GONE);
                plus_im.setVisibility(View.GONE);
                break;
            case R.id.mine:
                newIndex = 4;//选中第五项
                plus_yuan.setVisibility(View.GONE);
                plus_im.setVisibility(View.GONE);
                break;

        }


        switchFragment();


    }

    public void switchFragment() {
        FragmentTransaction transaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {

            transaction = getSupportFragmentManager().beginTransaction();

            transaction.hide(fragments[oldIndex]);//隐藏当前显示项

            switch (oldIndex) {
                case 0:
                    startDra = getResources().getDrawable(R.drawable.home);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[oldIndex].setCompoundDrawables(null, startDra, null, null);


                    break;
                case 1:
                    startDra = getResources().getDrawable(R.drawable.communication);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[oldIndex].setCompoundDrawables(null, startDra, null, null);


                    break;
                case 2:
                    startDra = getResources().getDrawable(R.drawable.exercises);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[oldIndex].setCompoundDrawables(null, startDra, null, null);

                    break;
                case 3:
                    startDra = getResources().getDrawable(R.drawable.friends);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[oldIndex].setCompoundDrawables(null, startDra, null, null);
                    break;
                case 4:
                    startDra = getResources().getDrawable(R.drawable.mine);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[oldIndex].setCompoundDrawables(null, startDra, null, null);
                    break;
            }

            //如果选中项没有加过，则添加
            if (!fragments[newIndex].isAdded()) {
                //添加fragment
                transaction.add(R.id.frame_content, fragments[newIndex]);
            }
            //显示当前选择项
            transaction.show(fragments[newIndex]).commit();

            switch (newIndex) {
                case 0:
                    startDra = getResources().getDrawable(R.drawable.home_select);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[newIndex].setCompoundDrawables(null, startDra, null, null);


                    break;
                case 1:
                    startDra = getResources().getDrawable(R.drawable.communication_selected);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[newIndex].setCompoundDrawables(null, startDra, null, null);


                    break;
                case 2:
                    startDra = getResources().getDrawable(R.drawable.exercises_selected);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[newIndex].setCompoundDrawables(null, startDra, null, null);

                    break;
                case 3:
                    startDra = getResources().getDrawable(R.drawable.friends_selected);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[newIndex].setCompoundDrawables(null, startDra, null, null);

                    break;
                case 4:
                    startDra = getResources().getDrawable(R.drawable.mine_selected);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    tabs[newIndex].setCompoundDrawables(null, startDra, null, null);

                    break;
            }

        }
        //之前选中的项，取消选中
        tabs[oldIndex].setSelected(false);
        //当前选择项，按钮被选中
        tabs[newIndex].setSelected(true);


        //当前选择项变为选中项
        oldIndex = newIndex;

    }

    private void setListeners() {
        // TODO Auto-generated method stub
        plus_yuan.setOnClickListener(new View.OnClickListener() {
            // 监听加号按钮的点击
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clicked = !clicked;
                // 两个按钮的显示隐藏
                dishui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);
                guoshui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);

                iv_fabuhuodong.setVisibility(clicked ? View.VISIBLE : View.GONE);
                iv_write.setVisibility(clicked ? View.VISIBLE : View.GONE);
                // 加号旋转
                plus_im.startAnimation(clicked ? rotate_anticlockwise
                        : rotate_clockwise);
                // 按钮显示隐藏效果
                dishui_tv.startAnimation(clicked ? scale_max : scale_min);
                guoshui_tv.startAnimation(clicked ? scale_max : scale_min);
                iv_fabuhuodong.startAnimation(clicked ? scale_max : scale_min);
                iv_write.startAnimation(clicked ? scale_max : scale_min);
                // 背景色的改变
                plus_rl.setBackgroundColor(clicked ? Color
                        .parseColor("#aaffffff") : Color.TRANSPARENT);
                // 背景是否可点击，用于控制Framelayout层下面的视图是否可点击
                plus_rl.setClickable(clicked);
                iv_write.setClickable(clicked? true : false);
                dishui_tv.setClickable(clicked? true : false);
                guoshui_tv.setClickable(clicked? true : false);
                iv_fabuhuodong.setClickable(clicked? true : false);


            }
        });
        plus_rl.setOnClickListener(new View.OnClickListener() {
            // 监听加号按钮的点击
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clicked = !clicked;
                // 两个按钮的显示隐藏
                dishui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);
                guoshui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);

                iv_fabuhuodong.setVisibility(clicked ? View.VISIBLE : View.GONE);
                iv_write.setVisibility(clicked ? View.VISIBLE : View.GONE);
                // 加号旋转
                plus_im.startAnimation(clicked ? rotate_anticlockwise
                        : rotate_clockwise);
                // 按钮显示隐藏效果
                dishui_tv.startAnimation(clicked ? scale_max : scale_min);
                guoshui_tv.startAnimation(clicked ? scale_max : scale_min);
                iv_fabuhuodong.startAnimation(clicked ? scale_max : scale_min);
                iv_write.startAnimation(clicked ? scale_max : scale_min);
                // 背景色的改变
                plus_rl.setBackgroundColor(clicked ? Color
                        .parseColor("#aaffffff") : Color.TRANSPARENT);
                // 背景是否可点击，用于控制Framelayout层下面的视图是否可点击
                plus_rl.setClickable(clicked);
                iv_write.setClickable(clicked? true : false);
                dishui_tv.setClickable(clicked? true : false);
                guoshui_tv.setClickable(clicked? true : false);
                iv_fabuhuodong.setClickable(clicked? true : false);
            }
        });

        dishui_tv.setOnClickListener(onClickListener_w);
        guoshui_tv.setOnClickListener(onClickListener);
        iv_write.setOnClickListener(onClickListener_w);
        iv_fabuhuodong.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener_w = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            clicked = !clicked;
            // 两个按钮的显示隐藏
            dishui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);
            guoshui_tv.setVisibility(clicked ? View.VISIBLE : View.GONE);

            iv_fabuhuodong.setVisibility(clicked ? View.VISIBLE : View.GONE);
            iv_write.setVisibility(clicked ? View.VISIBLE : View.GONE);
            // 加号旋转
            plus_im.startAnimation(clicked ? rotate_anticlockwise
                    : rotate_clockwise);
            // 按钮显示隐藏效果
            dishui_tv.startAnimation(clicked ? scale_max : scale_min);
            guoshui_tv.startAnimation(clicked ? scale_max : scale_min);
            iv_fabuhuodong.startAnimation(clicked ? scale_max : scale_min);
            iv_write.startAnimation(clicked ? scale_max : scale_min);
            // 背景色的改变
            plus_rl.setBackgroundColor(clicked ? Color
                    .parseColor("#aaffffff") : Color.TRANSPARENT);
            // 背景是否可点击，用于控制Framelayout层下面的视图是否可点击
            plus_rl.setClickable(clicked);
            iv_write.setClickable(clicked? true : false);
            dishui_tv.setClickable(clicked? true : false);
            guoshui_tv.setClickable(clicked? true : false);
            iv_fabuhuodong.setClickable(clicked? true : false);
            v.startAnimation(alpha_button);
            plus_im.performClick();
            Intent intent =new Intent(MainActivity.this,Publish_articles.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            v.startAnimation(alpha_button);
            plus_im.performClick();
        }
    };

    private void initData() {
        // TODO Auto-generated method stub
        rotate_anticlockwise = AnimationUtils.loadAnimation(this,
                R.anim.rotate_anticlockwise);
        rotate_clockwise = AnimationUtils.loadAnimation(this,
                R.anim.rotate_clockwise);
        scale_max = AnimationUtils.loadAnimation(this, R.anim.scale_max);
        scale_min = AnimationUtils.loadAnimation(this, R.anim.scale_min);
        alpha_button = AnimationUtils.loadAnimation(this, R.anim.alpha_button);
    }

    private void initView() {
        // TODO Auto-generated method stub
        //lzy的改动
        iv_huan = ((ImageView) findViewById(R.id.iv_huan));
        btn_friends = ((Button) findViewById(R.id.friends));
        //
        plus_rl = (RelativeLayout) findViewById(R.id.plus_rl);
        plus_yuan = (ImageView) findViewById(R.id.plus_yuan);
        plus_im = (ImageView) findViewById(R.id.plus_im);
        iv_write = (ImageView) findViewById(R.id.iv_write);
        iv_fabuhuodong = (ImageView) findViewById(R.id.iv_fabuhuodong);
        dishui_tv = (TextView) findViewById(R.id.dishui_tv);
        guoshui_tv = (TextView) findViewById(R.id.guoshui_tv);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (dishui_tv.getVisibility() == View.VISIBLE
                && keyCode == KeyEvent.KEYCODE_BACK) {
            plus_im.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //异步任务尝试2
    public void yiburenwu2(){
//         getTokenAsyncTask=new GetTokenAsyncTask(btn_friends);
//        getTokenAsyncTask.execute();
        //handler更新视图
        new Thread(new Runnable() {
            @Override
            public void run() {
                j= MyApplication.getUser().getUserid();
                RequestParams requestParams2=new RequestParams(HttpUtils.host4+"getalluserstoken");
                x.http().get(requestParams2, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson=new Gson();
                        Type type=new TypeToken<List<User>>(){}.getType();
                        Log.i("TokenSuccess",result);
                        List<User> newusersToken=gson.fromJson(result,type);
                        usersToken.addAll(newusersToken);
                        //赋予当前用户token
                        for (int i=0;i<usersToken.size();i++){
                            if(j==usersToken.get(i).getUserid()){
                                Token=usersToken.get(i).getUsertoken();
                                Log.i("Tooooo3","kennn"+Token);
                                setToken(Token);
                            }
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
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
        }).start();

    }
    public  static String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}