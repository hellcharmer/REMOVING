package com.example.charmer.moving.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.Publishdynamic.Publishdynamic;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Info;
import com.example.charmer.moving.pojo.QueryInfoBean;
import com.example.charmer.moving.pojo.Remark;
import com.example.charmer.moving.pojo.User;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.example.charmer.moving.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.charmer.moving.MainActivity.getUser;

/**
 * Created by Administrator on 2016/10/13.
 */
public class Fragment_dynamic extends BaseFragment {

    private boolean isRunning = false;
    private Integer dynamic_pageNo = 1; //第一页
    private Integer total_pageSize = 5; //每页几条数据
    private Integer totalpage;
    Map<Integer,Boolean> flag = new HashMap<>();
    //记录选中的位置 checkbox 点赞
    Map<Integer,Boolean> checkStatus = new HashMap<>();
    //记录likenum的位置
    Map<Integer,Integer> likenums = new HashMap<>();
    //有没有点过
    Map<Integer,Boolean> isliked=new HashMap<>();
    //是不是最新
    Map<Integer,Boolean> is_now=new HashMap<>();
    //记录评论的位置
    Map<Integer,Boolean> remarkContent = new HashMap<>();
    InfosAdapter infosAdapter;
    List<Info> infoList = new ArrayList<Info>();
    @InjectView(R.id.lv_info)
    LoadMoreListView lvInfo;
    @InjectView(R.id.pb_load)
    ProgressBar pbLoad;
    @InjectView(R.id.iv_publish)
    ImageView ivPublish;
    private NineGridTestLayout nineGridTestLayout;
    private CommentAdapter remarksAdapter;
    Integer likeNum;
    private SwipeRefreshLayout dynamic_refresh;
    private ObjectAnimator bottomAnimator;
    private RelativeLayout rl_test;
    private LinearLayout mBottom;
    private RelativeLayout rl_title;
    private TextView rb_guys;
    private EditText input_comment;
    private TextView btn_publish_comment;
    private TextView comment;
    private ListView lv_commentlist;
    private TextView tv_cancel;
    private TextView tv_delete;
    private String username;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //handleMessage界面更新
                case 1:
                    getData(dynamic_pageNo);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.inject(this, v);
        Integer userid = MainActivity.getUser().getUserid();
        getuserbyuserid(userid);
        return v;

    }

    @Override
    public void initView() {

        dynamic_refresh = ((SwipeRefreshLayout) getView().findViewById(R.id.dynamic_refresh));
        rl_test = ((RelativeLayout) getView().findViewById(R.id.rl_test));
        mBottom = ((LinearLayout) getActivity().findViewById(R.id.main_bottom));
        rl_title = ((RelativeLayout) getActivity().findViewById(R.id.rl_title));
        rb_guys = ((TextView) getView().findViewById(R.id.rb_guys));
        getData(dynamic_pageNo);
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
        //////////////传值过去
        lvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),one_activity.class);
                intent.putExtra("userimg",infoList.get(position).getUser().getUserimg());
                intent.putExtra("username",infoList.get(position).getUser().getUsername());
                intent.putExtra("infoDate",infoList.get(position).getInfoDate());
                intent.putExtra("infoContent",infoList.get(position).getInfoContent());
                intent.putExtra("imgs",infoList.get(position).getInfoPhotoImg());
                intent.putExtra("infoId",infoList.get(position).getInfoId()+"");
                intent.putExtra("infoLikeNum",infoList.get(position).getInfoLikeNum()+"");
                Log.i("info==", "onItemClick: "+infoList.get(position).getInfoLikeNum());
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getData(1);
    }

    @Override
    public void initData() {
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
                if(dynamic_pageNo<totalpage) {

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
        requestParams.addQueryStringParameter("userid", MainActivity.getUser().getUserid()+"");
        requestParams.addQueryStringParameter("pageNo",dynamic_pageNo+"");
        requestParams.addQueryStringParameter("pageSize",total_pageSize+"");
        Log.i("info", "getData: "+total_pageSize);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pbLoad.setVisibility(View.GONE);
                Log.i("info","lll"+result);
                //json转换为List<Info>
                Gson gson = new Gson();

                System.out.println(result);
                Type type = new TypeToken<QueryInfoBean>() {}.getType();
                QueryInfoBean queryInfoBean = gson.fromJson(result, type);  //解析成List<Info>
                List<Info> infos = queryInfoBean.getInfoList();
                totalpage = queryInfoBean.getTotalPage();
                String str = infos.get(0).getUser().getUsername();
                Log.i("username", "username" + str);
                if(dynamic_pageNo==1){
                    infoList.clear();
                }

                infoList.addAll(infos);
                if (infosAdapter == null) {
                    Log.i("lala", "onSuccess: ");
                    infosAdapter = new InfosAdapter(getActivity(),infoList,R.layout.fragment_info_more);
                    lvInfo.setAdapter(infosAdapter);
                } else {
                    Log.i("sssssssssss","sssssssssssss");
                    infosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

//                getData(dynamic_pageNo);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    class InfosAdapter extends CommonAdapter<Info> {

        public InfosAdapter(Context context, List<Info> lists, int layoutId) {
            super(context, lists, layoutId);

            //初始化checkStstus：默认都是未选中状态
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
        public void convert(ViewHolder viewHolder, final Info info, final int position) {


            List<String> urlsList = new ArrayList<String>();
            //取出控件
            comment = viewHolder.getViewById(R.id.btn_input_comment);
            final List<Remark> remarkList = new ArrayList<Remark>();
            lv_commentlist = viewHolder.getViewById(R.id.lv_commentlist);
            List<Remark> remarks = info.getRemark();
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
                }
            }
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
            xUtilsImageUtils.display(userimg, HttpUtils.host_dynamic +"upload/"+ info.getUser().getUserimg(), true);
            TextView username = viewHolder.getViewById(R.id.tv_infoName);
            username.setText(info.getUser().getUsername());
            Log.i("info", "convert:username ");
            TextView infoContent = viewHolder.getViewById(R.id.tv_infoContent);
            infoContent.setText(info.getInfoContent());
            TextView infoDate = viewHolder.getViewById(R.id.tv_infoDate);
            SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm:ss ");
            String date = formatter.format(info.getInfoDate());
            infoDate.setText(date);
            final TextView tv_likeNumber = viewHolder.getViewById(R.id.tv_likeNumber);
            tv_likeNumber.setText(likenums.get(position)+"");

            if(remarkContent.get(position)==null){
                if(remarks.isEmpty()){
                    remarkContent.put(position,false);
                }else{
                    remarkContent.put(position,true);
                }
            }else{
                if (!remarks.isEmpty()){
                    remarkContent.put(position,true);
                }
            }

            if(remarkContent.get(position)){
                if(remarkList.isEmpty()){
                    Log.i("info", "remarks: "+remarks);
                    remarkList.addAll(remarks);
                    Log.i("info", "remarks: " + remarkList);
                    remarksAdapter = new CommentAdapter(getActivity(),remarkList);
                    lv_commentlist.setAdapter(remarksAdapter);

                }else{

                    lv_commentlist.setAdapter(remarksAdapter);
                }
            }
            else{
                remarkList.clear();
                remarksAdapter = new CommentAdapter(getActivity(),remarkList);
                lv_commentlist.setAdapter(remarksAdapter);
            }


            //取出gridview
            nineGridTestLayout = viewHolder.getViewById(R.id.ng_grid);

            if(info.getInfoPhotoImg() == null || "".equals(info.getInfoPhotoImg())){
                nineGridTestLayout.setVisibility(View.GONE);
            }else{
                nineGridTestLayout.setVisibility(View.VISIBLE);
                urlsList.clear();
                String[] imgs = info.getInfoPhotoImg().split(",");
                if (imgs.length > 0) {
                    for (int i = 0; i < imgs.length; i++) {
                        urlsList.add(HttpUtils.host_dynamic + imgs[i]);
                    }
                    nineGridTestLayout.setUrlList(urlsList);
                    nineGridTestLayout.setIsShowAll(info.isShowAll);
                }
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
                    requestParams1.addQueryStringParameter("userId", getUser().getUserid()+"");
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


            //点击评论textview
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer fid = 0;
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    showdialog(fid,info.getInfoId(),position);

                }
            });

            //点击listview
            lv_commentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Integer fid = remarkList.get(position).getFatherDiscussant();
                    Timestamp remarktime = remarkList.get(position).getCommentTime();
                    Log.i("info", "onItemClick: father"+fid);
                    Integer cid = remarkList.get(position).getChildDiscussant();
                    String cname = remarkList.get(position).getChildDiscussantName();
                    if(cid == getUser().getUserid()){
                        showdeletedialog(remarktime,info.getInfoId(),position);

//                        remarkList.remove(position);
//                        remarksAdapter.notifyDataSetChanged();

                    }
                    else{
                        showdialogitem(cid,info.getInfoId(),cname);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
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
        rl_title.getBackground().setAlpha(255);
        rb_guys.setTextColor(Color.WHITE);
        ivPublish.setImageResource(R.drawable.publish_dynamic);

    }

    private void hideBar() {
        rl_title.getBackground().setAlpha(50);
        rb_guys.setTextColor(Color.BLACK);
        ivPublish.setImageResource(R.drawable.publish_black);
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

    //内部评论适配器
    private class CommentAdapter extends BaseAdapter {
        private List<Remark> remarkslist1;
        Context context;
        CommentAdapter( Context context,List<Remark> remarkslist1){
            this.context = context;
            this.remarkslist1 = remarkslist1;
        }

        @Override
        public int getCount() {
            return remarkslist1.size();
        }

        @Override
        public Object getItem(int position) {
            return remarkslist1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.lv_commentlist_item, null);
                viewHolder.tv_commentname= (TextView) convertView.findViewById(R.id.tv_commentname);
                viewHolder.replayorcomment = (TextView) convertView.findViewById(R.id.replayorcomment);
                viewHolder.tv_replayname = (TextView) convertView.findViewById(R.id.tv_replayname);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Remark remark = remarkslist1.get(position);
            if(remark.getFatherDiscussant()==null||remark.getFatherDiscussant()==0){
                viewHolder.tv_commentname.setText(remark.childDiscussantName+"");
                viewHolder.replayorcomment.setText("  评论："+remark.childComment+"");
                viewHolder.tv_replayname.setVisibility(View.GONE);
                viewHolder.tv_content.setVisibility(View.GONE);
            }
            else{
                viewHolder.tv_commentname.setText(remark.childDiscussantName+"");
                viewHolder.replayorcomment.setText("  回复:  ");
                viewHolder.tv_replayname.setText(remark.fatherDiscussantName+"");
                Log.i("info", "getView: remark.fatherDiscussantName"+remark.fatherDiscussantName);
                viewHolder.tv_content.setText(remark.childComment+"");
            }
            return convertView;
        }
         class ViewHolder {
            public TextView tv_commentname;
            public TextView replayorcomment;
            public TextView tv_replayname;
            public TextView tv_content;
        }
    }

    public void showdialog(final Integer ChildDiscount , final Integer infoId, final int position){
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_input_comment, null);
        input_comment = ((EditText) view.findViewById(R.id.input_comment));
        btn_publish_comment = ((TextView) view.findViewById(R.id.btn_publish_comment));
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,50);
        btn_publish_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "onClick: infoid"+infoId);
                sendRemark(ChildDiscount,infoId,position);
                view.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });
    }

    private void showdialogitem(final Integer fatherDiscount,final Integer infoId,final String fname) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_input_comment, null);
        input_comment = ((EditText) view.findViewById(R.id.input_comment));
        btn_publish_comment = ((TextView) view.findViewById(R.id.btn_publish_comment));
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,50);
        btn_publish_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "onClick: infoid"+infoId);
                sendRemark1(fatherDiscount,infoId,fname);
                view.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });
    }

    private void sendRemark1(Integer childDiscount, Integer infoId,String fname) {
        if (input_comment.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //生成评论数据
            Remark remark = new Remark(infoId, getUser().getUserid(),
                    input_comment.getText().toString()+"",childDiscount,"");
            remark.setChildDiscussantName(fname);
            Log.i("info", "sendRemark: "+remark);
            RequestParams params = new RequestParams(HttpUtils.host_dynamic + "sendremark");
            Gson gson =new Gson();
            String remarkInfo = gson.toJson(remark);
            Log.i("info", "sendRemark:two "+remarkInfo);
            params.addQueryStringParameter("remarkInfo",remarkInfo);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if("true".equals(result)){
                        input_comment.setText("");
                        Toast.makeText(getContext(),"评论成功,dynamic_pageNo:"+dynamic_pageNo,Toast.LENGTH_LONG).show();
                        getData(dynamic_pageNo);
                    }
                    else{
                        Toast.makeText(getContext(),"评论失败",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getContext(),"网络已断开",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        }

    }

    public void sendRemark(Integer ChildDiscount,Integer infoId,int position){
        if (input_comment.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //生成评论数据
            Remark remark = new Remark(infoId, getUser().getUserid(),
                    input_comment.getText().toString()+"",ChildDiscount,"");
            remark.setChildDiscussantName(username);
            infoList.get(position).getRemark().add(remark);
            infosAdapter.notifyDataSetChanged();
            Log.i("info", "sendRemark: "+remark);
            RequestParams params = new RequestParams(HttpUtils.host_dynamic + "sendremark");
            Gson gson =new Gson();
            String remarkInfo = gson.toJson(remark);
            Log.i("info", "sendRemark:two "+remarkInfo);
            try {
                params.addQueryStringParameter("remarkInfo",URLEncoder.encode(remarkInfo,"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if("true".equals(result)){
                        input_comment.setText("");
                        Toast.makeText(getContext(),"评论成功",Toast.LENGTH_LONG).show();
                        getData(dynamic_pageNo);
                    }
                    else{
                        Toast.makeText(getContext(),"评论失败",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getContext(),"网络已断开",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        }

    }

    public void showdeletedialog(final Timestamp remarkTime, final Integer infoId,final int position) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dynamic_remark_delete, null);
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,50);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);
        tv_cancel = ((TextView) view.findViewById(R.id.tv_cancel));
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tv_delete = ((TextView) view.findViewById(R.id.tv_delete));
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRemark(remarkTime,infoId,position);
                popupWindow.dismiss();
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
//                remarksAdapter.notifyDataSetChanged();
//                infosAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteRemark(Timestamp remarkTime, Integer infoId,int position) {
        Remark remark = new Remark(infoId, getUser().getUserid(), remarkTime);

        RequestParams params = new RequestParams(HttpUtils.host_dynamic+"deleteremark");
        Gson gson =new Gson();
        String remarkInfo = gson.toJson(remark);
        Log.i("info", "sendRemark:two "+remarkInfo);
        params.addQueryStringParameter("remarkInfo",remarkInfo);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if("true".equals(result)){
                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_LONG).show();
//                    Message msg = new Message();
//                    msg.what = 1;
//                    handler.sendMessage(msg);
                }else{
                    Toast.makeText(getContext(),"删除失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(),"网络已近断开",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void getuserbyuserid(Integer userid){
        RequestParams params=new RequestParams(HttpUtils.host_dynamic+"queryuserbyuserid");
        params.addBodyParameter("userid",String.valueOf(userid));
        x.http().get(params, new Callback.CommonCallback<String >() {
        @Override
        public void onSuccess(String result) {
            Gson gson = new Gson();
            User user = gson.fromJson(result,User.class);
            username = user.getUsername();
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

}
