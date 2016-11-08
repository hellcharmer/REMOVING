package com.example.charmer.moving.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.friendchat.PostAcyivity;
import com.example.charmer.moving.friendchat.SearchTalk;
import com.example.charmer.moving.pojo.Friend;
import com.example.charmer.moving.pojo.Qun;
import com.example.charmer.moving.pojo.TLZ;
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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lenovo on 2016/10/11.
 */
public class Fragment_friend extends Fragment implements View.OnClickListener {

    @InjectView(R.id.edsearch)
    EditText edsearch;
    private NoScrollViewPager vpFriend;
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
    private ListView qunlistView;
    private boolean isRunning = false;
    private PagerAdapter pagerAdapter;
    private List<View> views = new ArrayList<View>();
    private final List<Friend> friends = new ArrayList<Friend>();//所有好友信息
    private  List<Qun> quns=new ArrayList<Qun>();//群信息
    private  List<TLZ> tlzs=new ArrayList<TLZ>();//讨论组信息
    private List<User> usersToken = new ArrayList<User>();//所有user的Token
    CommonAdapter<Friend> goodfriendad;
    CommonAdapter<Qun> qunad;
    CommonAdapter<TLZ> tlzad;
    private Friend_titlebar fd_friend;
    private Friend_titlebar fd_talk;
    private Friend_titlebar fd_qun;
    private Friend_titlebar fd_tlz;
    private Map<String, Integer> tip = new HashMap<>();
    private List<View> ft = new ArrayList<>();
    private int pv_position;
    private RelativeLayout mHead_bar;
    private SwipeRefreshLayout re_friend;
    private boolean flag = true;
    private boolean flag1=false;
    private User user;
    private String Token;
    private RelativeLayout friendbg;
    private SwipeRefreshLayout re_qun;
    private ListView tlzlistview;
    private SwipeRefreshLayout re_tlz;
    private ProgressBar progress2;
    private ImageView userimg;
    private ImageView no_yuan;
    private ProgressBar progress4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
//       Token= MainActivity.getToken();
//        Log.i("Tooooo2","kenn"+Token);
        tip.put("篮球", R.drawable.basketball);
        tip.put("游泳", R.drawable.swim);
        tip.put("乒乓球", R.drawable.pingp);
        tip.put("网球", R.drawable.vallyball);
        tip.put("足球", R.drawable.football);
        tip.put("钓鱼", R.drawable.fish);
        vpFriend = (NoScrollViewPager) view.findViewById(R.id.vp_friend);
        vpFriend.setNoScroll(true);
        mBottom_bar = (LinearLayout) getActivity().findViewById(R.id.main_bottom);
        mHead_bar = ((RelativeLayout) view.findViewById(R.id.rl_toptitle));
        initdata();
        initview(view);
        ButterKnife.inject(this, view);
        initEnt();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getQunData();
    }

    private void initdata() {
        //所有user的token后台处理
        //已在MainActivity中注册所有Id并获得usertoken
        //尝试异步任务
        //第四次异步任务终于成功哇哈哈哈哈哈哈！！！！！时间优化4倍
        Token = MainActivity.getToken();
        /**
         * 连接
         */
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.i("Tooooo1", "kenn" + Token);
                Log.e("framgment", "tokenINcorrect");
            }

            @Override
            public void onSuccess(String s) {
                Log.e("framgment1", "onSuccess: " + s);
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String s) {
                        for (Friend i : friends) {
                            if (i.getFriendid().equals(s)) {
                                Log.i("提供了", "用户" + s);
                                return new UserInfo(i.getFriendid() + "", i.getName(), Uri.parse(HttpUtils.hostpc+ i.getUser().getUserimg()));
                            }
                        }
                        return null;
                    }
                }, true);
                //容云用户提供头像
                Rong();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("framgmente", "onError: " + errorCode.getValue());
            }
        });
        Log.i("Tooooo5", "kenn" + Token);
        //对好友的信息处理
        getFriendData();
        //群
        getQunData();
        //讨论组
        getTlzData();

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
                openItem.setTitle("phone");
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
        no_yuan = ((ImageView) view.findViewById(R.id.hongyuan));
        //主界面
        friendbg = ((RelativeLayout) view.findViewById(R.id.friendbg));
        //每个标题
        fd_talk = ((Friend_titlebar) view.findViewById(R.id.fd_talk));
        //自己的头像
        userimg = ((ImageView) fd_talk.findViewById(R.id.left));
        getUserimg();
        //判断是否有申请信息
        ifpost();
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
        progress2 = ((ProgressBar) v3.findViewById(R.id.progress2));
        progress4 = ((ProgressBar) v4.findViewById(R.id.progress4));
        //讨论组listview处理
        tlzlistview = ((ListView) v4.findViewById(R.id.lv_tlzs));
        //群listview处理
        qunlistView=((ListView) v3.findViewById(R.id.lv_qun));
        //好友listview处理
        listView = ((SwipeMenuListView) v2.findViewById(R.id.lv_goodfriends));
        listView.setMenuCreator(creator);
        //好友刷新处理
        re_friend = ((SwipeRefreshLayout) v2.findViewById(R.id.re_friend));
        //设置下拉刷新加载圈的颜色
        re_friend.setColorSchemeColors(getResources().getColor(R.color.colorSkybule));
        //设置下拉加载圈出现距离顶部的位置
        re_friend.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        //设置下拉加载圈转动时距离顶部的位置
        re_friend.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        //群刷新处理
        re_qun = ((SwipeRefreshLayout) v3.findViewById(R.id.re_qun));
        re_qun.setColorSchemeColors(getResources().getColor(R.color.colorSkybule));
        re_qun.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        re_qun.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        //讨论组刷新
        re_tlz = ((SwipeRefreshLayout) v4.findViewById(R.id.re_tlz));
        re_tlz.setColorSchemeColors(getResources().getColor(R.color.colorSkybule));
        re_tlz.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        re_tlz.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.activity_fragment));
        //加载圈事件
        refreshData();
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
        //软键盘不弹出
        edsearch.setInputType(InputType.TYPE_NULL);
        //edsear点击事件
        edsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchTalk.class);
                startActivity(intent);
            }
        });
        //好友删除按钮
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                      //phone
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+friends.get(position).getUser().getUserphone()));
                        startActivity(intent);
                        break;
                    case 1:
                        //delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("删除");
                        builder.setMessage("抛弃他？");

                        builder.setIcon(R.drawable.qun);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"好心人",Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer friendid = friends.get(position).getFriendid();
                                RequestParams requestParams = new RequestParams(HttpUtils.host4 + "friendservlet");
                                //获取用户ID
                                requestParams.addQueryStringParameter("choice", 1 + "");
                                requestParams.addQueryStringParameter("friendid", friendid + "");
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
                                        while (!Thread.currentThread().isInterrupted()) {
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                            //使用postInvalidate可以直接在线程中更新界面
                                            listView.postInvalidate();
                                        }
                                    }
                                }).start();

                                Toast.makeText(getActivity(),"他已被你狠心抛弃",Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setNeutralButton("再说", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"好的",Toast.LENGTH_SHORT).show();

                            }
                        });

                        builder.show();

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
        //群滑动监听
        qunlistView.setOnTouchListener(new View.OnTouchListener() {
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
        //讨论组滑动
        tlzlistview.setOnTouchListener(new View.OnTouchListener() {
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
        //容云点击聊天按钮单聊
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(getActivity(), friends.get(position - 1).getFriendid() + "", friends.get(position - 1).getName());
                }
            }
        });

        //群聊
        qunlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(RongIM.getInstance()!=null){
                    RongIM.getInstance().startGroupChat(getActivity(),quns.get(position).getQunid()+"", quns.get(position).getQunname());
                }
            }
        });
        //讨论组聊天
        tlzlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(RongIM.getInstance()!=null){
                    RongIM.getInstance().startDiscussionChat(getActivity(), tlzs.get(position).getTlzid(),tlzs.get(position).getTlzname());
                }
            }
        });
        //会话标题按钮

        fd_talk.setImgLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_yuan.setVisibility(View.GONE);
                Intent intent=new Intent(getActivity(), PostAcyivity.class);
                startActivity(intent);

            }
        });
        rightclick(fd_talk, 1);
        rightclick(fd_friend, 2);
        rightclick(fd_qun, 3);
        rightclick(fd_tlz, 0);
        leftclick(fd_friend, 0);
        leftclick(fd_qun, 1);
        leftclick(fd_tlz, 2);
        //vp滑动事件
        vpFriend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            String str[] = new String[]{"会话", "好友", "群", "讨论组"};

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(), str[position], Toast.LENGTH_SHORT).show();
                ft.get(position).setVisibility(View.VISIBLE);
                ft.get(pv_position).setVisibility(View.GONE);
                pv_position = position;
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
    //各种控件的点击事件
    @Override
    public void onClick(View v) {
      switch (v.getId()){

      }
    }
    //好友列表的填充
    class FriendAdapter extends CommonAdapter<Friend> {

        public FriendAdapter(Context context, List<Friend> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Friend friend, int position) {

            final String str = friend.getUser().getUserimg();

            Log.i("daqi", "打气了");
            TextView tv_username = ((TextView) viewHolder.getViewById(R.id.tv_username));
            tv_username.setText(friend.getUser().getUsername());
            TextView tv_nowact = ((TextView) viewHolder.getViewById(R.id.tv_nowact));
            if(friend.getContent()==null){
                tv_nowact.setText("暂无");
            }else{
            tv_nowact.setText(friend.getContent());}
            ImageView iv_toux = ((ImageView) viewHolder.getViewById(R.id.iv_toux));
            xUtilsImageUtils.display(iv_toux, HttpUtils.host4+"upload/" + str, true);
            ImageView i_tip = ((ImageView) viewHolder.getViewById(R.id.i_tip));
            System.out.println();
            if (friend.getTitle()!=null){
                if (friend.getTitle().isEmpty()){
                    i_tip.setImageResource(R.drawable.wu);
                }
                else {
                    i_tip.setImageResource(tip.get(friend.getTitle()));
                }
            }
            if (friend.getTitle()==null){
                i_tip.setImageResource(R.drawable.wu);
            }
        }
    }
    //群列表的填充
  class QunAdapter extends CommonAdapter<Qun> {
        public QunAdapter(Context context, List<Qun> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Qun qun, int position) {
            ImageView qunimg=((ImageView) viewHolder.getViewById(R.id.qunimg));
            xUtilsImageUtils.display(qunimg, qun.getQunimg(),false);
            TextView qun_name=((TextView) viewHolder.getViewById(R.id.qun_name));
            qun_name.setText(qun.getQunname());
        }
    }
    //讨论组列表的填充
    class TlzAdapter extends CommonAdapter<TLZ>{

        public TlzAdapter(Context context, List<TLZ> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, TLZ tlz, int position) {
           TextView tv_tlzname= ((TextView) viewHolder.getViewById(R.id.tv_tlzname));
            tv_tlzname.setText(tlz.getTlzname());
            String userid=tlz.getTlzusers();
            String[] userids=userid.split(",");
            for(int i=0;i<userids.length;i++){
                Log.i("convertids",userids[i]);
            }
        }
    }
    //对好友的信息处理
    public void getFriendData() {
        RequestParams requestParams = new RequestParams(HttpUtils.host4 + "friendservlet");
        requestParams.addQueryStringParameter("choice", 6+ "");
        //获取用户ID
        requestParams.addQueryStringParameter("userid", MainActivity.getUser().getUserid() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("[]")){
                    Toast.makeText(getActivity(),"没有朋友呢（╯＾╰〉",Toast.LENGTH_SHORT).show();
                }
                friends.clear();
                progressbar.setVisibility(View.GONE);
                tvJiazai.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Friend>>() {
                }.getType();
                Log.i("getFrienddata", result);
                List<Friend> newfds = gson.fromJson(result, type);
                friends.addAll(newfds);
                goodfriendad = new FriendAdapter(getActivity(), friends, R.layout.activity_friend_goodfriend_layout);
                listView.setAdapter(goodfriendad);
                if (flag) {
                    View view1 = View.inflate(getContext(), R.layout.friendnumber, null);
                    TextView fdnum = ((TextView) view1.findViewById(R.id.friendnumber));
                    fdnum.setText(friends.size() + "");
                    listView.addFooterView(view1);
                    View view2 = View.inflate(getContext(), R.layout.friend_bugdeal, null);
                    listView.addHeaderView(view2);
                }
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
    //对群组信息的处理
     public void getQunData(){
     RequestParams params=new RequestParams(HttpUtils.host4+"getqun");
         params.addQueryStringParameter("choice",0+"");//改动
         params.addQueryStringParameter("userid",MainActivity.getUser().getUserid() + "");
         x.http().get(params, new Callback.CommonCallback<String>() {
             @Override
             public void onSuccess(String result) {
                 if(result.equals("[]")){
                     Toast.makeText(getActivity(),"暂时没有组织-____-\"",Toast.LENGTH_SHORT).show();
                 }
                 quns.clear();
                 Log.i("qunData",result);
                 progress2.setVisibility(View.GONE);
                 Gson gson = new Gson();
                 Type type = new TypeToken<List<Qun>>() {
                 }.getType();
                 List<Qun> newquns=gson.fromJson(result,type);
                 quns.addAll(newquns);
                 qunad=new QunAdapter(getActivity(),quns,R.layout.activity_friend_qun_layout);
                 qunlistView.setAdapter(qunad);
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
    //讨论组数据处理
    public void getTlzData(){
        RequestParams params=new RequestParams(HttpUtils.host4+"getqun");
        params.addQueryStringParameter("choice",4+ "");
        params.addQueryStringParameter("userid",MainActivity.getUser().getUserid() + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("[]")||result==null){
                    Toast.makeText(getActivity(),"暂时没有讨论组-____-\"",Toast.LENGTH_SHORT).show();
                }
                tlzs.clear();
                Log.i("tlzsData",result);
                progress4.setVisibility(View.GONE);
                Gson gson = new Gson();
                Type type = new TypeToken<List<TLZ>>() {
                }.getType();
                List<TLZ> newtlzs=gson.fromJson(result,type);
                tlzs.addAll(newtlzs);
                tlzad=new TlzAdapter(getActivity(),tlzs,R.layout.activity_friend_tlzs_layout);
                tlzlistview.setAdapter(tlzad);
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
    //左右图片点击方法
    public void leftclick(Friend_titlebar ft, final Integer i) {
        ft.setImgLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpFriend.setCurrentItem(i);
            }
        });
    }

    public void rightclick(Friend_titlebar ft, final Integer i) {
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
    //list拖动显示bottom
    public void showBar() {
        mHeaderAnimator = ObjectAnimator.ofFloat(mHead_bar, "translationY", 0);
        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar, "translationY", 0);
        llsearch = ObjectAnimator.ofFloat(llSreach, "translationY", 0);
        mHeaderAnimator.setDuration(300).start();
        mBottomAnimator.setDuration(400).start();
        llsearch.setDuration(300).start();

    }

    //加载圈的处理
    private void refreshData() {
        re_friend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = false;
                re_friend.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFriendData();
                        re_friend.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        re_qun.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                re_qun.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getQunData();
                        re_qun.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        re_tlz.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                re_tlz.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getTlzData();
                        re_tlz.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    //会话列表链接容云
    private void Rong() {
        //        //异步回调刷新数据??有用？？3天后：有用 >_<
        RequestParams requestParams1 = new RequestParams(HttpUtils.host4 + "getuser");
        requestParams1.addQueryStringParameter("choice",0+"");
        requestParams1.addQueryStringParameter("userid", +MainActivity.getUser().getUserid() + "");
        x.http().get(requestParams1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("UUUSSSSIIIMMMGG", result);
                Gson gson = new Gson();
                Type type = new TypeToken<User>() {
                }.getType();
                user = gson.fromJson(result, type);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(MainActivity.getUser().getUserid() + "", user.getUsername(), Uri.parse(HttpUtils.hostpc + user.getUserimg())));
                //刷新所有好友的头像
                refreshImg();
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
    //刷新用户头像
    private void refreshImg() {
        for (Friend i : friends) {
            Log.i("fffffrrrrree", i.getFriendid() + "");
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(i.getFriendid() + "", i.getName(), Uri.parse(HttpUtils.hostpc + i.getUser().getUserimg())));
        }
    }
    private  void getUserimg(){
        RequestParams params=new RequestParams(HttpUtils.host4+"getuser");
        params.addQueryStringParameter("choice",0+"");
        params.addQueryStringParameter("userid",MainActivity.getUser().getUserid()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<User>(){}.getType();
                User user=gson.fromJson(result,type);
                xUtilsImageUtils.display(userimg, HttpUtils.host4+"upload/"+user.getUserimg(),true);
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
    //是否有申请
    private  void ifpost(){
        RequestParams requestParams =new RequestParams(HttpUtils.host4+"friendservlet");
        requestParams.addQueryStringParameter("choice",3+"");
        requestParams.addQueryStringParameter("userid",MainActivity.getUser().getUserid()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("[]")){
                    no_yuan.setVisibility(View.GONE);
                }else {
                    no_yuan.setVisibility(View.VISIBLE);
                }
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

    //url转bitmap
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
