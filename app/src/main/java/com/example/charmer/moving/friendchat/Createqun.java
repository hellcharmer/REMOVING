package com.example.charmer.moving.friendchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Friend;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Createqun extends AppCompatActivity {
    private ObjectAnimator mBottomAnimator;
    private boolean isRunning = false;
    CommonAdapter<Friend> mfda;
    private Map<Integer,Boolean> check=new HashMap<Integer, Boolean>();
   private List<Friend> fdls=new ArrayList<Friend>();
    private ListView lv_create;
    private RelativeLayout bottom;
    private List<String> ids=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createqun);
        lv_create = ((ListView) findViewById(R.id.lv_create));
        bottom = ((RelativeLayout) findViewById(R.id.bottom));
        initData();
        initView();
        initEvt();
    }
    private void initView() {
    }
    private void initData() {
        getFriendData();

    }


    private void initEvt() {
        //listvew 的滑动
        lv_create.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;
            private int direction;//0表示向上，1表示向下

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndY = event.getY();
                        float v1 = mEndY - mStartY;

                        if (v1 > 3 && !isRunning && direction == 1) {
                            direction = 0;
                            showBar();
                            mStartY = mEndY;
                            return false;
                        } else if (v1 < -3 && !isRunning && direction == 0) {
                            direction = 1;
                            hideBar();
                            mStartY = mEndY;
                            return false;
                        }
                        mStartY = mEndY;

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });


        //lv的item点击
        lv_create.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RelativeLayout dan=((RelativeLayout) view.findViewById(R.id.dan));
                if (check.get(position)){
                              dan.setBackgroundResource(R.drawable.libule);
                             check.put(position,false);
                             ids.add(fdls.get(position).getFriendid()+"");
                             Log.i("userid",fdls.get(position).getFriendid()+"");
                         }else{
                             dan.setBackgroundResource(R.drawable.greybg);
                             check.put(position,true);
                             ids.remove(fdls.get(position).getFriendid()+"");
                         }
            }
        });
        //跳转到第二步
       bottom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String userid=new String();
               for (int i=0;i<ids.size();i++){
                   userid=userid+ids.get(i)+",";
               }
               userid= MainActivity.getUser().getUserid()+","+userid;
               Log.i("userid1",userid);
               Intent intent=new Intent(Createqun.this,Creatqun2.class);
               intent.putExtra("ids",userid);
               startActivity(intent);
           }
       });
    }
    //获取用户好友
    private void getFriendData() {
        RequestParams requestParams = new RequestParams(HttpUtils.host4 + "friendservlet");
        requestParams.addQueryStringParameter("choice",6+ "");
        //获取用户ID
        requestParams.addQueryStringParameter("userid", MainActivity.getUser().getUserid() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                 fdls.clear();
                Log.i("createqun",result);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Friend>>(){}.getType();
                List<Friend> newfdls=gson.fromJson(result,type);
                fdls.addAll(newfdls);
                mfda=new Myfdad(Createqun.this,fdls,R.layout.creatqun);
                lv_create.setAdapter(mfda);
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
        class Myfdad extends CommonAdapter<Friend> {

            public Myfdad(Context context, List<Friend> lists, int layoutId) {
                super(context, lists, layoutId);
                for (int i=0;i<lists.size();i++)
                {check.put(i,false);
                }
            }
            @Override
            public void convert(ViewHolder viewHolder, final Friend friend, final int position) {
                      Log.i("friend","好友数据");
               ImageView iv3= ((ImageView) viewHolder.getViewById(R.id.iv3));
                xUtilsImageUtils.display(iv3, HttpUtils.hostpc + friend.getUser().getUserimg(), true);
                TextView tv3=  ((TextView) viewHolder.getViewById(R.id.tv3));
                tv3.setText(friend.getUser().getUsername());


            }
        }
    //lisiview拖动隐藏bar
    public void hideBar() {
        mBottomAnimator = ObjectAnimator.ofFloat(bottom, "translationY", bottom.getHeight());
        mBottomAnimator.setDuration(400).start();
        mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
            }
        });
    }
    //list拖动显示bottom
    public void showBar() {
        mBottomAnimator = ObjectAnimator.ofFloat(bottom, "translationY", 0);
        mBottomAnimator.setDuration(400).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
