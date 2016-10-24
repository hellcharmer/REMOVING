package com.example.charmer.moving.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Friend;
import com.example.charmer.moving.pojo.User;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.Friend_titlebar;
import com.example.charmer.moving.utils.NoScrollViewPager;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static com.example.charmer.moving.MainActivity.getTokenAsyncTask;

/**
 * Created by lenovo on 2016/10/11.
 */
public class Fragment_friend extends Fragment implements View.OnClickListener {

    private NoScrollViewPager vpFriend;
    @InjectView(R.id.iv_search)
    ImageView ivSearch;
    @InjectView(R.id.ll_sreach)
    LinearLayout llSreach;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.tv_jiazai)
    TextView tvJiazai;
    private ObjectAnimator mHeaderAnimator;
    private ObjectAnimator mBottomAnimator;
    private ObjectAnimator llsearch;
    private LinearLayout mBottom_bar;
    private View rl_talk;
    private ImageView iv_talk;
    private ImageView iv_goodfriend;
    private View rl_friend;
    private View rl_qun;
    private View rl_tlz;
    private ImageView iv_talk1;
    private SwipeMenuListView listView;
    private boolean isRunning = false;
    private PagerAdapter pagerAdapter;
    private List<View> views = new ArrayList<View>();
    private final List<Friend> friends=new ArrayList<Friend>();//所有好友信息
    private List<User> usersToken=new ArrayList<User>();//所有user的Token
    CommonAdapter<Friend> goodfriendad;
    private Friend_titlebar fd_friend;
    private Friend_titlebar fd_talk;
    private Friend_titlebar fd_qun;
    private Friend_titlebar fd_tlz;
    private Map<String,Integer> tip=new HashMap<>();
    private List<View> ft=new ArrayList<>();
    private int pv_position;
    private RelativeLayout mHead_bar;
    private SwipeRefreshLayout re_friend;
    private boolean flag=true;
    private User user;
     private String Token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
//       Token= MainActivity.getToken();
//        Log.i("Tooooo2","kenn"+Token);
        tip.put("篮球",R.drawable.basketball);
        tip.put("游泳",R.drawable.swim);
        tip.put("乒乓球",R.drawable.pingp);
        tip.put("网球",R.drawable.vallyball);
        tip.put("足球",R.drawable.football);
        tip.put("钓鱼",R.drawable.fish);
        vpFriend = (NoScrollViewPager) view.findViewById(R.id.vp_friend);
        vpFriend.setNoScroll(true);
        mBottom_bar = (LinearLayout) getActivity().findViewById(R.id.main_bottom);
        mHead_bar = ((RelativeLayout) view.findViewById(R.id.rl_toptitle));
        initdata();
        initview(view);
        initEnt();
        ButterKnife.inject(this, view);
        return view;
    }

    private void initdata() {
        //所有user的token后台处理
        //已在MainActivity中注册所有Id并获得usertoken
        //尝试异步任务
//        GetTokenAsyncTask getTokenAsyncTask=new GetTokenAsyncTask();
//        getTokenAsyncTask.execute();
        //第四次异步任务终于成功哇哈哈哈哈哈哈！！！！！时间优化4倍
        Token=getTokenAsyncTask.getToken();
        Log.i("Tooooo5","kenn"+Token);
        //对好友的信息处理
        getFriendData();
        //容云用户提供头像
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                for (Friend i:friends){
                    if (i.getFriendid().equals(s)) {
                        Log.i("提供了", "用户" + s);
                        return new UserInfo(i.getFriendid()+"", i.getName(), Uri.parse(HttpUtils.host4 + i.getUser().getUserimg()));
                    }
                }
                return null;
            }
        },true);
        //链接融云

        Rong();

    }

    private void initview(View view) {
        //第三方控件侧滑
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        //每个标题
        fd_talk = ((Friend_titlebar) view.findViewById(R.id.fd_talk));
        fd_friend = ((Friend_titlebar) view.findViewById(R.id.fd_friend));
        fd_qun = ((Friend_titlebar) view.findViewById(R.id.fd_qun));
        fd_tlz = ((Friend_titlebar) view.findViewById(R.id.fd_tlz));
        ft.add(fd_talk);
        ft.add(fd_friend);
        ft.add(fd_qun);
        ft.add(fd_tlz);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v1 = layoutInflater.inflate(R.layout.activity_friend_talk, null);
        View v2 = layoutInflater.inflate(R.layout.activity_friend_goodfriend, null);
        View v3 = layoutInflater.inflate(R.layout.activity_friend_qun, null);
        View v4 = layoutInflater.inflate(R.layout.activity_friend_tlzs, null);
        //刷新处理
        re_friend = ((SwipeRefreshLayout) v2.findViewById(R.id.re_friend));
        //设置下拉刷新加载圈的颜色
        re_friend.setColorSchemeColors(getResources().getColor(R.color.refreshcolor));
        //设置下拉加载圈出现距离顶部的位置
        re_friend.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        //设置下拉加载圈转动时距离顶部的位置
        re_friend.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        //加载圈事件
        refreshData();
        //好友listview处理
        listView = ((SwipeMenuListView) v2.findViewById(R.id.lv_goodfriends));
        listView.setMenuCreator(creator);

        views.add(v1);
        views.add(v2);
        views.add(v3);
        views.add(v4);
        //vp适配器打气
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        };
        vpFriend.setAdapter(pagerAdapter);
    }

    private void initEnt() {
        //好友删除按钮
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                       //delete
                        Integer friendid=friends.get(position).getFriendid();
                        RequestParams requestParams = new RequestParams(HttpUtils.host4 + "deletefd");
                        //获取用户ID
                        requestParams.addQueryStringParameter("friendid",friendid+"");
                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
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
                        friends.remove(position);
                        goodfriendad.notifyDataSetChanged();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted())
                                {
                                    try
                                    {
                                        Thread.sleep(100);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        Thread.currentThread().interrupt();
                                    }
                                    //使用postInvalidate可以直接在线程中更新界面
                                    listView.postInvalidate();
                                }
                            }
                        }).start();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        //listview滑动监听隐藏
        listView.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;
            private int direction;//0表示向上，1表示向下
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndY = event.getY();
                        float v1 = mEndY - mStartY;

                        if (v1 > 3 && !isRunning&& direction == 1) {
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

        //容云点击聊天按钮
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (RongIM.getInstance() != null)
                {
       RongIM.getInstance().startPrivateChat(getActivity(),friends.get(position-1).getFriendid()+"", friends.get(position-1).getName());}
            }
        });
        //会话标题按钮
        fd_talk.setImgLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"个人信息",Toast.LENGTH_SHORT).show();
            }
        });
       rightclick(fd_talk,1);
       rightclick(fd_friend,2);
        rightclick(fd_qun,3);
        rightclick(fd_tlz,0);
        leftclick(fd_friend,0);
        leftclick(fd_qun,1);
        leftclick(fd_tlz,2);
        //vp滑动事件
        vpFriend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            String str[]=new String[]{"会话","好友","群","讨论组"};
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(),str[position],Toast.LENGTH_SHORT).show();
                ft.get(position).setVisibility(View.VISIBLE);
                ft.get(pv_position).setVisibility(View.GONE);
                pv_position=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {

    }
    //好友列表的填充
    class FriendAdapter extends CommonAdapter<Friend> {

        public FriendAdapter(Context context, List<Friend> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Friend friend, int position) {
            Log.i("daqi","打气了");
           TextView tv_username= ((TextView) viewHolder.getViewById(R.id.tv_username));
            tv_username.setText(friend.getUser().getUsername());
           TextView tv_nowact= ((TextView) viewHolder.getViewById(R.id.tv_nowact));
           tv_nowact.setText(friend.getContent());
            ImageView iv_toux=((ImageView) viewHolder.getViewById(R.id.iv_toux));
            xUtilsImageUtils.display(iv_toux,HttpUtils.host4+friend.getUser().getUserimg(),true);
            ImageView i_tip=((ImageView) viewHolder.getViewById(R.id.i_tip));
            i_tip.setImageResource(tip.get(friend.getTitle()));
        }
    }

    //对好友的信息处理
    public void getFriendData() {
        RequestParams requestParams = new RequestParams(HttpUtils.host4 + "tofriendmain");
        //获取用户ID
        requestParams.addQueryStringParameter("userid", 1 + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                friends.clear();
                progressbar.setVisibility(View.GONE);
                tvJiazai.setVisibility(View.GONE);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Friend>>(){}.getType();
                Log.i("getFrienddata",result);
                List<Friend> newfds=gson.fromJson(result,type);
                friends.addAll(newfds);
                goodfriendad=new FriendAdapter(getActivity(),friends,R.layout.activity_friend_goodfriend_layout);
               listView.setAdapter(goodfriendad);
                if (flag){
                View view1=View.inflate(getContext(),R.layout.friendnumber,null);
                TextView fdnum=((TextView) view1.findViewById(R.id.friendnumber));
                fdnum.setText(friends.size()+"");
                listView.addFooterView(view1);
                View view2=View.inflate(getContext(),R.layout.friend_bugdeal,null);
                listView.addHeaderView(view2);}
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("===================================");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    //左右图片点击方法
    public void leftclick(Friend_titlebar ft, final Integer i){
        ft.setImgLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFriend.setCurrentItem(i);
            }
        });
    }
    public void rightclick(Friend_titlebar ft, final Integer i){
        ft.setImgRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFriend.setCurrentItem(i);
            }
        });
    }
    //dp2xp
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    //lisiview拖动隐藏bar
    public void hideBar() {
        mHeaderAnimator = ObjectAnimator.ofFloat(mHead_bar, "translationY", -mHead_bar.getHeight());
        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar, "translationY", mBottom_bar.getHeight());
        llsearch = ObjectAnimator.ofFloat(llSreach, "translationY", -llSreach.getHeight());
        mHeaderAnimator.setDuration(500).start();
        mBottomAnimator.setDuration(400).start();
        llsearch.setDuration(500).start();

        mHeaderAnimator.addListener(new AnimatorListenerAdapter() {
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

    public void showBar() {
        mHeaderAnimator = ObjectAnimator.ofFloat(mHead_bar, "translationY", 0);
        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar,"translationY", 0);
        llsearch = ObjectAnimator.ofFloat(llSreach,"translationY", 0);

        mHeaderAnimator.setDuration(300).start();
        mBottomAnimator.setDuration(400).start();
        llsearch.setDuration(300).start();

    }
    //加载圈的处理
    private void refreshData() {
        re_friend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag=false;
                re_friend.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFriendData();
                        re_friend.setRefreshing(false);
                    }
                },1000);
            }
        });
    }

    //会话列表链接容云
    private void Rong(){


            //        //异步回调刷新数据??有用？？
            RequestParams requestParams1 =new RequestParams(HttpUtils.host4+"getuser");
            requestParams1.addQueryStringParameter("userid",+((MyApplication)getActivity().getApplication()).getUser().getUserid()+"");
            x.http().get(requestParams1, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("UUUSSSSIIIMMMGG",result);
                    Gson gson=new Gson();
                    Type type=new TypeToken<User>(){}.getType();
                    user=gson.fromJson(result,type);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(((MyApplication)getActivity().getApplication()).getUser().getUserid()+"", user.getUsername(), Uri.parse(HttpUtils.host4+user.getUserimg())));
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

            /**
             * 连接
             */
            RongIM.connect(Token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.i("Tooooo1","kenn"+Token);
                    Log.e("framgment","tokenINcorrect");
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("framgment1", "onSuccess: "+s );
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("framgmente", "onError: "+errorCode.getValue() );
                }
            });
        }

    //获得所有用户token


}
