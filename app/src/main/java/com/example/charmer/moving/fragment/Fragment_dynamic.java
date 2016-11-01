package com.example.charmer.moving.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.Publishdynamic.Publishdynamic;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Info;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2016/10/13.
 */
public class Fragment_dynamic extends BaseFragment {

    private boolean isRunning = false;
    private Integer dynamic_pageNo = 1; //第一页
    private Integer total_pageSize = 5; //总共多少页
    Map<Integer,Boolean> flag = new HashMap<>();
    //记录选中的位置 checkbox 点赞
    Map<Integer,Boolean> checkStatus = new HashMap<>();
    //记录likenum的位置
    Map<Integer,Integer> likenums = new HashMap<>();
    //有没有点过
    Map<Integer,Boolean> isliked=new HashMap<>();
    //是不是最新
    Map<Integer,Boolean> is_now=new HashMap<>();
    InfosAdapter infosAdapter;
    List<Info> infoList = new ArrayList<Info>();
    @InjectView(R.id.lv_info)
    LoadMoreListView lvInfo;
    @InjectView(R.id.pb_load)
    ProgressBar pbLoad;
    @InjectView(R.id.iv_publish)
    ImageView ivPublish;
    private GridView gv;
    private MyAdapter imgsAdapter;
    Integer likeNum;
    private String[] imgs;
    private SwipeRefreshLayout dynamic_refresh;

    private ObjectAnimator bottomAnimator;
    private RelativeLayout rl_test;
    private LinearLayout mBottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void initView() {

        dynamic_refresh = ((SwipeRefreshLayout) getView().findViewById(R.id.dynamic_refresh));
        rl_test = ((RelativeLayout) getView().findViewById(R.id.rl_test));
        mBottom = ((LinearLayout) getActivity().findViewById(R.id.main_bottom));
    }

    @Override
    public void initEvent() {
        ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Publishdynamic.class);
                startActivity(intent);
            }
        });

        //设置下拉刷新加载圈的颜色
        //设置卷内的颜色
        dynamic_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉加载圈出现距离顶部的位置
        dynamic_refresh.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        //设置下拉加载圈转动时距离顶部的位置
        dynamic_refresh.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        //设置下拉刷新监听
        bindEvents();


    }



    @Override
    public void initData() {

        getData(dynamic_pageNo); //获取网络数据，显示在listview上
        lvInfo.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                loadZixunMore();

            }
        });

    }

    private void loadZixunMore() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(dynamic_pageNo<total_pageSize) {

                    getData(++dynamic_pageNo);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lvInfo.setLoadCompleted();
                    }
                });
            }
        }.start();
    }

    //xutils获取网络数据
    public void getData(final Integer dynamic_pageNo) {
        //xutils获取网络数据
        String url = HttpUtils.host_dynamic + "queryinfoservlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("pageNo",dynamic_pageNo+"");
        requestParams.addQueryStringParameter("pageSize",total_pageSize+"");

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pbLoad.setVisibility(View.GONE);
                Log.i("Fragment_dynamic", "onSuccess" + result);
                //json转换为List<Info>
                Gson gson = new Gson();
                Type type = new TypeToken<List<Info>>() {
                }.getType();
                List<Info> infos = gson.fromJson(result, type);  //解析成List<Info>
                String str = infos.get(0).getUser().getUsername();
                Log.i("username", "username" + str);
                if(dynamic_pageNo==1){
                    infoList.clear();
                }

                infoList.addAll(infos);

                if (infosAdapter == null) {
                    infosAdapter = new InfosAdapter(getActivity(),infoList,R.layout.fragment_info_more);
                    lvInfo.setAdapter(infosAdapter);
                } else {
                    Log.i("sssssssssss","sssssssssssss");
                    infosAdapter.notifyDataSetChanged();
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

    class InfosAdapter extends CommonAdapter<Info>{


        public InfosAdapter(Context context, List<Info> lists, int layoutId) {
            super(context, lists, layoutId);

            //初始化checkStstus：默认都是位选中状态
            for(int i = 0;i<lists.size();i++){
                Log.i("jin","jin");
                checkStatus.put(i,false);
            }

            for(int i = 0;i<lists.size();i++){
                is_now.put(i,true);
            }
            //初始化likenum
            for(int i = 0;i<lists.size();i++){
                likenums.put(i,lists.get(i).getInfoLikeNum());
            }
            for(int i = 0;i<lists.size();i++){
                flag.put(i,false);
            }
            for(int i = 0;i<lists.size();i++){
                isliked.put(i,true);
            }


        }

        @Override
        public void convert(ViewHolder viewHolder,final Info info, final int position) {
            //取出控件
            final CheckBox iv_like = viewHolder.getViewById(R.id.iv_like);
            final boolean flagmsg = info.isInfoState();
            if (is_now.get(position)==null){
                is_now.put(position,true);
            }
            if(is_now.get(position)){
                likenums.put(position,info.getInfoLikeNum());
                if(flagmsg){
                    iv_like.setChecked(true);
                    checkStatus.put(position,true);
                }else{
                    iv_like.setChecked(false);
                    checkStatus.put(position,false);
                }}
               if (checkStatus.get(position)==null){
                   likenums.put(position,info.getInfoLikeNum());
                   if(flagmsg){
                       iv_like.setChecked(true);
                       checkStatus.put(position,true);
                   }else{
                       iv_like.setChecked(false);
                       checkStatus.put(position,false);
                   }
               }



            iv_like.setChecked(checkStatus.get(position));

            flag.put(position,false);
            isliked.put(position,true);
            ImageView userimg = viewHolder.getViewById(R.id.iv_photoImg);
            xUtilsImageUtils.display(userimg, HttpUtils.host_dynamic + info.getUser().getUserimg(), true);
            TextView username = viewHolder.getViewById(R.id.tv_infoName);
            username.setText(info.getUser().getUsername());
            TextView infoContent = viewHolder.getViewById(R.id.tv_infoContent);
            infoContent.setText(info.getInfoContent());
            TextView infoDate = viewHolder.getViewById(R.id.tv_infoDate);
            SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm:ss ");
            String date = formatter.format(info.getInfoDate());
            infoDate.setText(date);
            final TextView tv_likeNumber = viewHolder.getViewById(R.id.tv_likeNumber);
            tv_likeNumber.setText(likenums.get(position)+"");



            //取出gridview
            gv = viewHolder.getViewById(R.id.gridview);
            imgs = info.getInfoPhotoImg().split(",");
            if(imgs.length>0){
                imgsAdapter = new MyAdapter(getActivity(),imgs);
                gv.setAdapter(imgsAdapter);
            }



            iv_like.setTag(position); //每个checkbox的tag不一样
            //显示一个view时，重新设置view上的iv_like状态
            iv_like.setChecked(checkStatus.get(position));
            //iv_like的选中事件，改变点赞数


            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkStatus.get(position)){
                        iv_like.setChecked(false);
                        checkStatus.put(position,false);
                        is_now.put(position,false);
                    }else{
                        iv_like.setChecked(true);
                        checkStatus.put(position,true);
                        is_now.put(position,false);
                    }

                    RequestParams requestParams1 = new RequestParams(HttpUtils.host_dynamic+"updatelikeservlet");
                    requestParams1.addQueryStringParameter("infoId",info.getInfoId()+"");
                    requestParams1.addQueryStringParameter("userId",MainActivity.getUser().getUserid()+"");
                    x.http().get(requestParams1, new Callback.CommonCallback<String>() {
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
                    ////buttonView表示当前的iv_like,点击的是哪个控件就是哪个控件

                    if(checkStatus.get(position) && position == (int) iv_like.getTag() ){

                        checkStatus.put((int)v.getTag(),true);
                        flag.put((int)v.getTag(),true);
                        //改变点赞数

                        if(flag.get(position)) {
                            Log.i("position",position+"");
                            likeNum = Integer.parseInt(tv_likeNumber.getText().toString()) + 1;
                            tv_likeNumber.setText(likeNum+"");
                            likenums.put((int)v.getTag(),likeNum);
                            is_now.put(position,false);
                            RequestParams requestParams = new RequestParams(HttpUtils.host_dynamic+"updateaddinfolikenum");
                            requestParams.addQueryStringParameter("infoId",info.getInfoId()+"");
                            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.i("success","success"+result);

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
                        flag.put((int)v.getTag(),false);

                    }
                    else if(!checkStatus.get(position) && position == (int) iv_like.getTag()){
                        checkStatus.put((int)v.getTag(),false);
                        flag.put((int)v.getTag(),true);
                        //改变点赞数 取消选中
                        if(flag.get(position)) {
                            likeNum = Integer.parseInt(tv_likeNumber.getText().toString()) -1;
                            tv_likeNumber.setText(likeNum+"");
                            likenums.put((int)v.getTag(),likeNum);
                            is_now.put(position,false);
                            RequestParams requestParams = new RequestParams(HttpUtils.host_dynamic+"updatedeleteinfolikenum");
                            requestParams.addQueryStringParameter("infoId",info.getInfoId()+"");
                            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    Log.i("success","success"+result);

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
                        flag.put((int)v.getTag(),false);
                    }

                }

            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private String[] imgs;
        MyAdapter(Context context,String[] imgs){
            this.context = context;
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder ;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.activity_publish_img_item, null);
                viewHolder.imgs = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();;
            }
            xUtilsImageUtils.display(viewHolder.imgs, HttpUtils.host_dynamic + imgs[position]);
            return convertView;

        }
        class ViewHolder {
            public ImageView imgs;
        }

    }

    private void bindEvents(){
        dynamic_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dynamic_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dynamic_pageNo = 1;
                        getData(dynamic_pageNo);
                        checkStatus.clear();
                        likenums.clear();
                        flag.clear();
                        isliked.clear();
                        //调用该方法结束刷新，否则加载圈会一直在
                        dynamic_refresh.setRefreshing(false);

                    }
                },1000);
            }
        });

        lvInfo.setOnTouchListener(new View.OnTouchListener() {
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




    }

    private void showBar() {

        bottomAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", 0);

        bottomAnimator.setDuration(400).start();

    }

    private void hideBar() {
        bottomAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", mBottom.getHeight());
        bottomAnimator.setDuration(400).start();
        bottomAnimator.addListener(new AnimatorListenerAdapter() {
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
}
