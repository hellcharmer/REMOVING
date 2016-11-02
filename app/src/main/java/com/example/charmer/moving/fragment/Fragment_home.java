package com.example.charmer.moving.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.MyView.MyGridView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.Constant;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.home_activity.SearchActivity;
import com.example.charmer.moving.home_activity.ZixunInfo_xq;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.utils.DateUtils;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.weyye.library.EmptyLayout;


/**
 * Created by Administrator on 2016/9/19.
 */
public class Fragment_home extends Fragment {


    private ImageView iv_zan;


    private EmptyLayout emptyLayout;
    private SwipeRefreshLayout mSr_refresh1;
    private SwipeRefreshLayout mSr_refresh2;
    private SwipeRefreshLayout mSr_refresh3;
    private SwipeRefreshLayout mSr_refresh4;
    private SwipeRefreshLayout mSr_refresh5;
    private SwipeRefreshLayout mSr_refresh6;
    private SwipeRefreshLayout mSr_refresh7;
    private SwipeRefreshLayout mSr_refresh8;
    private SwipeRefreshLayout mSr_refresh9;
    private boolean isRunning = false;
    boolean isLoading=false;
    private View footview;
    private TextView mLoadMore;
    private ObjectAnimator mHeaderAnimator;
    private ObjectAnimator titlebarAnimator;
    private ObjectAnimator bottomAnimator;
    private ObjectAnimator plusAnimator;
    private Button bt_search_home;
    private static final String TAG = "Fragment_home";

    private LoadMoreListView lv_zixun;
    private LoadMoreListView lv_zixun_basketball;
    private LoadMoreListView lv_zixun_swimming;
    private LoadMoreListView lv_zixun_running;
    private LoadMoreListView lv_zixun_football;
    private LoadMoreListView lv_zixun_pingpang;
    private LoadMoreListView  lv_zixun_wangqiu;
    private LoadMoreListView  lv_zixun_qita;
    private MyGridView gv_shouye_huodong;
//    private RelativeLayout home_huodong_list;
//    private RelativeLayout home_zixun_listview;
//    private RelativeLayout huodong_zixun;
//    int home_huodong_listHeight;
//    int home_zixun_listviewHeight;
 //   int huodong_zixunHeight;
   // private BaseAdapter adapter;
    View view;
    View lv_zixun_head;
    int page_zixun = 1;//第一页
    int page_basketball = 1;
    int page_swim = 1;
    int page_run = 1;//第一页
    int page_football = 1;
    int page_pingpang = 1;
    int page_wangqiu = 1;//第一页
    int page_qita = 1;
    int totalpage_zixun=0;//总页数
    int totalpage_basketball=0;
    int totalpage_swimming=0;
    int totalpage_run=0;
    int totalpage_football=0;
    int totalpage_pingpang=0;
    int totalpage_wangqiu=0;
    int totalpage_qita=0;
    private ViewPager vp_zixun;
    private HorizontalScrollView hs_view;
    private LinearLayout main_bottom;
    private LinearLayout home_search_bar;
    private RelativeLayout plus_rl;
    MyAdapter adapter1;
    MyAdapter adapter2;
    MyAdapter adapter3;
    MyAdapter adapter4;
    MyAdapter adapter5;
    MyAdapter adapter6;
    MyAdapter adapter7;
    MyAdapter adapter8;

    HoudongAdapter adapter_huodong;
    int dianzannum=0;

    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离


    //private ArrayList<TabFragment> mTabs;//用来存放下方滚动的layout(layout_1,layout_2,layout_3)



    private RadioGroup myRadioGroup;

     int _id = 1000;
    private LinearLayout layout,titleLayout;
    private ImageView mImageView;
    private List<ListActivityBean.Zixun> zixunlist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> basketballlist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> swimminglist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> runninglist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> footballlist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> pingpanglist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> wangqiulist = new ArrayList<ListActivityBean.Zixun>();
    private List<ListActivityBean.Zixun> qitalist = new ArrayList<ListActivityBean.Zixun>();
    public static final ArrayList<VariableExercise.Exercises> exerciseList = new ArrayList<VariableExercise.Exercises>();
    private  List<View> listView= new ArrayList<View>();
    private List<Map<String, String>> titleList = new ArrayList<Map<String,String>>();
    ToastUtil toastUtil;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.vp_home_listview, null,false);

//        main_bottom=getActivity().findViewById(R.id.main_bottom);


        //自定义toast布局
        View v=inflater.inflate(R.layout.layout_toast_view,null);
        toastUtil=new ToastUtil(getActivity(),v,200);
        initView();
        initlistviews();
        //加号按钮
        bindEvents();
      return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        //绑定
        emptyLayout.bindView(lv_zixun);
        emptyLayout.setOnButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新加载数据
                getExerciseList();
                getZixunlist(1,MainActivity.getUser().getUseraccount());
            }
        });
        vp_zixun.setCurrentItem(0);
        lv_zixun.setAdapter(adapter1);


        emptyLayout.showLoading("正在加载，请稍后");
        getExerciseList();
        getZixunlist(page_zixun, MainActivity.getUser().getUseraccount());
        //list 的每个点击事件
        lv_zixun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position-1).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_basketball.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_swimming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_running.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_football.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_pingpang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_wangqiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        lv_zixun_qita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toastUtil.Short(getActivity(),"这是第"+(position)+"个").show();
                Intent intent = new Intent(getActivity(), ZixunInfo_xq.class);
                intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
                startActivity(intent);
            }
        });
        bt_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);

                startActivity(intent);
            }
        });


    }

    private void initlistviews() {


        View view1 = View.inflate(getActivity(), R.layout.fragment_home, null);
        emptyLayout = (EmptyLayout)view1.findViewById(R.id.emptyLayout);
        //刷新控件
        mSr_refresh1 = (SwipeRefreshLayout)view1.findViewById(R.id.sr_refresh);

        lv_zixun = ((LoadMoreListView)view1.findViewById(R.id.lv_zixun));
       // home_huodong_list = ((RelativeLayout) view1.findViewById(R.id.home_huodong_list));
       // home_zixun_listview = ((RelativeLayout) view1.findViewById(R.id.home_zixun_listview));
       // huodong_zixun = ((RelativeLayout) view1.findViewById(R.id.huodong_zixun));
        View lv_zixun_head = View.inflate(getActivity(), R.layout.fragment_huodong, null);
        gv_shouye_huodong = ((MyGridView)lv_zixun_head.findViewById(R.id.shouye_huodong));
        adapter_huodong=new HoudongAdapter();
        lv_zixun.addHeaderView(lv_zixun_head);
        gv_shouye_huodong.setAdapter(adapter_huodong);
        adapter1 =new MyAdapter(zixunlist);
        listView.add(view1);


        View view2 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh2 = (SwipeRefreshLayout)view2.findViewById(R.id.sr_refresh);
        lv_zixun_basketball = ((LoadMoreListView)view2.findViewById(R.id.lv_zixun));
        adapter2 =new MyAdapter(basketballlist);
        listView.add(view2);

        View view3 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh3 = (SwipeRefreshLayout)view3.findViewById(R.id.sr_refresh);
        lv_zixun_swimming = ((LoadMoreListView)view3.findViewById(R.id.lv_zixun));
        adapter3=new MyAdapter(swimminglist);
        listView.add(view3);

        View view4 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh4 = (SwipeRefreshLayout)view4.findViewById(R.id.sr_refresh);
        lv_zixun_running = ((LoadMoreListView)view4.findViewById(R.id.lv_zixun));
        adapter4=new MyAdapter(runninglist);
        listView.add(view4);

        View view5 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh5 = (SwipeRefreshLayout)view5.findViewById(R.id.sr_refresh);
        lv_zixun_football = ((LoadMoreListView)view5.findViewById(R.id.lv_zixun));
        adapter5=new MyAdapter(footballlist);
        listView.add(view5);

        View view6 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh6 = (SwipeRefreshLayout)view6.findViewById(R.id.sr_refresh);
        lv_zixun_pingpang = ((LoadMoreListView)view6.findViewById(R.id.lv_zixun));
       adapter6=new MyAdapter(pingpanglist);
        listView.add(view6);

        View view7 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh7 = (SwipeRefreshLayout)view7.findViewById(R.id.sr_refresh);
        lv_zixun_wangqiu = ((LoadMoreListView)view7.findViewById(R.id.lv_zixun));
        adapter7=new MyAdapter(wangqiulist);
        listView.add(view7);

        View view8 = View.inflate(getActivity(), R.layout.fragment_home_type, null);
        mSr_refresh8 = (SwipeRefreshLayout)view8.findViewById(R.id.sr_refresh);
        lv_zixun_qita = ((LoadMoreListView)view8.findViewById(R.id.lv_zixun));
        adapter8=new MyAdapter(qitalist);
        listView.add(view8);




        //设置下拉刷新加载圈的颜色
        mSr_refresh1.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh2.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh3.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh4.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh5.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh6.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh7.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));
        mSr_refresh8.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.refresh_circle));


        //设置下拉加载圈出现距离顶部的位置
        mSr_refresh1.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh2.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh3.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh4.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh5.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh6.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh7.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        mSr_refresh8.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));

        //设置下拉加载圈转动时距离顶部的位置
        mSr_refresh1.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh2.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh3.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh4.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh5.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh6.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh7.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
        mSr_refresh8.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));

    }

    private void initView() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "首页");
        titleList.add(map);
        map = new HashMap<String, String>();
        map.put("title", "篮球");
        titleList.add(map);

        map = new HashMap<String, String>();
        map.put("title", "游泳");
        titleList.add(map);


        map = new HashMap<String, String>();
        map.put("title", "跑步");
        titleList.add(map);

        map = new HashMap<String, String>();
        map.put("title", "足球");
        titleList.add(map);

        map = new HashMap<String, String>();
        map.put("title", "乒乓");
        titleList.add(map);

        map = new HashMap<String, String>();
        map.put("title", "网球");
        titleList.add(map);

        map = new HashMap<String, String>();
        map.put("title", "其他");
        titleList.add(map);


    }


    private void initData() {
        titleLayout = (LinearLayout)view.findViewById(R.id.title_lay);
        layout = (LinearLayout)view.findViewById(R.id.lay);
        bt_search_home = (Button)view.findViewById(R.id.bt_search_home);
        mImageView = (ImageView)view.findViewById(R.id.img1);
        vp_zixun = ((ViewPager)view.findViewById(R.id.vp_zixun));
        plus_rl=(RelativeLayout)getActivity().findViewById(R.id.plus_rl);
        home_search_bar=(LinearLayout)view.findViewById(R.id.home_search_bar);
        main_bottom =(LinearLayout)getActivity().findViewById(R.id.main_bottom);
        hs_view = (HorizontalScrollView)view.findViewById(R.id.hs_view);
        myRadioGroup = new RadioGroup(getActivity());
        myRadioGroup.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i <titleList.size(); i++) {
            Map<String, String> map = titleList.get(i);
            RadioButton radio = new RadioButton(getActivity());

            //radio.setBackgroundResource(R.drawable.radiobtn_selector);
           radio.setButtonDrawable(android.R.color.transparent);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            radio.setPadding(30, 30, 30,28);
            //radio.setPadding(left, top, right, bottom)
            radio.setId(_id+i);
            radio.setText(map.get("title"));
            ColorStateList color = getResources().getColorStateList(R.color.home_title_type);
            radio.setTextColor(color);
            radio.setTextSize(18);
            radio.setTag(map);
            if (i == 0) {
                radio.setChecked(true);
                int itemWidth = (int) radio.getPaint().measureText(map.get("title"));
                mImageView.setLayoutParams(new  LinearLayout.LayoutParams(itemWidth+radio.getPaddingLeft()+radio.getPaddingRight(),4));
            }
            myRadioGroup.addView(radio);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)view.findViewById(radioButtonId);
                Map<String, Object> selectMap = (Map<String, Object>) rb.getTag();

                AnimationSet animationSet = new AnimationSet(true);
                TranslateAnimation translateAnimation;
                translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
                animationSet.addAnimation(translateAnimation);
                animationSet.setFillBefore(true);
                animationSet.setFillAfter(true);
                animationSet.setDuration(300);

                mImageView.startAnimation(animationSet);//开始上面白色横条图片的动画切换
                vp_zixun.setCurrentItem(radioButtonId - _id, false);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();//更新当前白色横条距离左边的距离
                //System.out.println("dis1:"+mCurrentCheckedRadioLeft);
                //System.out.println("dis2:"+( (int)mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2)));
                hs_view.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.activity_horizontal), 0);

                mImageView.setLayoutParams(new  LinearLayout.LayoutParams(rb.getRight()-rb.getLeft(),4));
                switch (checkedId) {
                    case 1000:
                        lv_zixun.setAdapter(adapter1);
                        getExerciseList();
                        getZixunlist(page_zixun,MainActivity.getUser().getUseraccount());

                        break;
                    case 1001:


                        lv_zixun_basketball.setAdapter(adapter2);
                       getZixunlist_basketball(page_basketball,MainActivity.getUser().getUseraccount());

                        break;
                    case 1002:

                        lv_zixun_swimming.setAdapter(adapter3);
                         getZixunlist_swim(page_swim,MainActivity.getUser().getUseraccount());

                        break;
                    case 1003:
                        lv_zixun_swimming.setAdapter(adapter4);
                        getZixunlist_run(page_swim,MainActivity.getUser().getUseraccount());
                        break;
                    case 1004:
                        lv_zixun_swimming.setAdapter(adapter5);
                        getZixunlist_football(page_swim,MainActivity.getUser().getUseraccount());
                        break;
                    case 1005:
                        lv_zixun_swimming.setAdapter(adapter6);
                        getZixunlist_pingpang(page_swim,MainActivity.getUser().getUseraccount());
                        break;
                    case 1006:
                        lv_zixun_swimming.setAdapter(adapter7);
                        getZixunlist_wangqiu(page_swim,MainActivity.getUser().getUseraccount());
                        break;
                    case 1007:
                        lv_zixun_swimming.setAdapter(adapter8);
                        getZixunlist_qita(page_swim,MainActivity.getUser().getUseraccount());
                        break;
                    default:
                        break;


                }
            }
        });

//        vp_zixun.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                return true;
//            }
//        });

        vp_zixun.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toastUtil.Short(getActivity(),"这是第"+(position+1)+"页").show();

                RadioButton radioButton = (RadioButton)view.findViewById(_id+position);
                radioButton.performClick();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        MyPAdapter adapter =new MyPAdapter(listView);
        //vp_zixun .setOffscreenPageLimit(2);
        vp_zixun.setAdapter(adapter);

    }

    private void bindEvents() {

        //设置为0表示，此时不需要考虑父控件的布局问题，直接使用getMeasuredWidth和getMeasuredHeight获取此view的自身的实际大小
//        home_huodong_list.measure(0,0);
//        home_huodong_listHeight = home_huodong_list.getMeasuredHeight();
//        home_zixun_listview.measure(0,0);
//        home_zixun_listviewHeight = home_huodong_list.getMeasuredHeight();
//        huodong_zixun.measure(0,0);
//        huodong_zixunHeight = huodong_zixun.getMeasuredHeight();
//        lv_zixun.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//
//                //view.getScrollY();
//                int scrollHeight = 0;
//                View c = view.getChildAt(0);//获得listview第一个item
//                if (c == null) {
//                    scrollHeight = 0;
//                    return;
//                }
//                int firstVisiblePosition = view.getFirstVisiblePosition();
//                int top = c.getTop();
//                scrollHeight =  -top + firstVisiblePosition * c.getHeight() ;
//                huodong_zixun.setPadding(0, (int)(-scrollHeight),0,0);
//               // home_zixun_listview.setPadding(0, (int)(-scrollHeight),0,0);
//                System.out.println(scrollHeight);
//            }
//        });
        mSr_refresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    getExerciseList();
                      getZixunlist(1,MainActivity.getUser().getUseraccount());


                        //调用该方法结束刷新，否则加载圈会一直在
                        mSr_refresh1.setRefreshing(false);

               }
                },1000);
            }
        });
        mSr_refresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getZixunlist_basketball(1,MainActivity.getUser().getUseraccount());
                        //调用该方法结束刷新，否则加载圈会一直在
                        mSr_refresh2.setRefreshing(false);
                    }
                },1000);
            }
        });
        mSr_refresh3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getZixunlist_swim(1,MainActivity.getUser().getUseraccount());
                        //调用该方法结束刷新，否则加载圈会一直在
                        mSr_refresh3.setRefreshing(false);
                    }
                },1000);
            }
        });
        lv_zixun.setOnTouchListener(new View.OnTouchListener() {
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
        lv_zixun_basketball.setOnTouchListener(new View.OnTouchListener() {
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
        lv_zixun_swimming.setOnTouchListener(new View.OnTouchListener() {
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
        lv_zixun.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {

                loadZixunMore();
            }
        });
        lv_zixun_basketball.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                //System.out.println("==================1");
                loadBasketballMore();
            }
        });
        lv_zixun_swimming.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                //System.out.println("==================1");
                loadSwimmingMore();
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
                if(page_zixun<totalpage_zixun) {

                    getZixunlist(++page_zixun,MainActivity.getUser().getUseraccount());
                }else {
                    toastUtil.Short(getActivity(),"已经到底了！").show();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            lv_zixun.setLoadCompleted();

                    }
                });
            }
        }.start();
    }
    private void loadBasketballMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(page_basketball<totalpage_basketball) {

                    getZixunlist_basketball(++page_basketball,MainActivity.getUser().getUseraccount());
                }else {
                    toastUtil.Short(getActivity(),"已经到底了！").show();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lv_zixun_basketball.setLoadCompleted();
                    }
                });
            }
        }.start();
    }
    private void loadSwimmingMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(page_swim<totalpage_swimming) {

                    getZixunlist_basketball(++page_swim,MainActivity.getUser().getUseraccount());
                }else {
                    toastUtil.Short(getActivity(),"已经到底了！").show();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lv_zixun_swimming.setLoadCompleted();
                    }
                });
            }
        }.start();
    }
    public void hideBar() {
        mHeaderAnimator = ObjectAnimator.ofFloat(home_search_bar, "translationY", -home_search_bar.getHeight()-7);
        titlebarAnimator = ObjectAnimator.ofFloat(hs_view, "translationY", -hs_view.getHeight());
        bottomAnimator = ObjectAnimator.ofFloat(main_bottom, "translationY", Constant.displayHeight);
        plusAnimator = ObjectAnimator.ofFloat(plus_rl, "translationY", Constant.displayHeight);
        mHeaderAnimator.setDuration(300).start();
        titlebarAnimator.setDuration(300).start();
        bottomAnimator.setDuration(300).start();
        plusAnimator.setDuration(300).start();
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
        titlebarAnimator.addListener(new AnimatorListenerAdapter() {
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
        plusAnimator.addListener(new AnimatorListenerAdapter() {
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
        mHeaderAnimator = ObjectAnimator.ofFloat(home_search_bar, "translationY", 0);
        titlebarAnimator = ObjectAnimator.ofFloat(hs_view, "translationY", 0);
        bottomAnimator = ObjectAnimator.ofFloat(main_bottom, "translationY", 0);
        plusAnimator = ObjectAnimator.ofFloat(plus_rl, "translationY", 0);
        mHeaderAnimator.setDuration(300).start();
        titlebarAnimator.setDuration(300).start();
        bottomAnimator.setDuration(500).start();
        plusAnimator.setDuration(500).start();
    }


    private void getZixunlist(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_zixun = bean.page;


                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                if(page_zixun==1){


                    zixunlist.clear();
                }
                //zixunlist.clear();
                zixunlist.addAll(bean.zixunlist);
                // 成功
                emptyLayout.showSuccess();
                //通知listView更新界面
                adapter1.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                emptyLayout.showError("加载失败，点击重新加载"); // 显示失败
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getExerciseList() {

        //GPS定位值==place
        String str = HttpUtils.hoster+"getexercise";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("exercisetype","全部分类");
        params.addQueryStringParameter("exercisetheme","全部主题");
        params.addQueryStringParameter("page","1");
        params.addQueryStringParameter("place","苏州");

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                exerciseList.clear();
                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                exerciseList.addAll(bean.exerciseList);
                //dongtaiList = bean.dongtailist;   error
                System.out.println(bean.exerciseList);
                // 成功
                emptyLayout.showSuccess();
                adapter_huodong.notifyDataSetChanged();

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

    private void getZixunlist_basketball(int page,String userId) {
        String url= HttpUtils.hoster+"toappmain";//访问网络的url
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("type","篮球");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_basketball=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                if(page_basketball==1){
                    basketballlist.clear();
                }
//                zixunlist.clear();
                basketballlist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getZixunlist_swim(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=游泳");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_swimming=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
               // zixunlist.clear();
                if(page_swim==1){
                    swimminglist.clear();
                }
                swimminglist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter3.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getZixunlist_run(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=跑步");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_run=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                // zixunlist.clear();
                if(page_run==1){
                    runninglist.clear();
                }
                if(bean.zixunlist!=null){
                    runninglist.addAll(bean.zixunlist);
                    //通知listView更新界面
                    adapter4.notifyDataSetChanged();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getZixunlist_football(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=足球");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_football=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                // zixunlist.clear();
                if(page_football==1){
                    footballlist.clear();
                }
                footballlist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter5.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getZixunlist_pingpang(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=乒乓");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_pingpang=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                // zixunlist.clear();
                if(page_pingpang==1){
                    pingpanglist.clear();
                }
                pingpanglist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter6.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getZixunlist_wangqiu(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=网球");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_wangqiu=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                // zixunlist.clear();
                if(page_wangqiu==1){
                    wangqiulist.clear();
                }
                wangqiulist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter7.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getZixunlist_qita(int page,String userId) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"toappmain?type=其他");
        params.addQueryStringParameter("page",page+"");
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
                totalpage_qita=bean.page;
                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                // zixunlist.clear();
                if(page_qita==1){
                    qitalist.clear();
                }
                qitalist.addAll(bean.zixunlist);
                //通知listView更新界面
                adapter8.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }







    private class MyPAdapter extends PagerAdapter {
        private   List<View> listView;
        public MyPAdapter( List<View> listView) {
            this.listView = listView;//构造方法，参数是我们的页卡，这样比较方便。
        }

        @Override
        public int getCount() {
            return listView.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //container.removeView((View) object);
            System.out.println(position);
            container.removeView(listView.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {



            container.addView(listView.get(position));
            //System.out.println(position);
            return listView.get(position);
        }
    }

    private class MyAdapter extends BaseAdapter {
        private   List<ListActivityBean.Zixun> list;

        public MyAdapter( List<ListActivityBean.Zixun> list) {
            this.list= list;
        }
        @Override
        public int getCount() {
           // System.out.println(zixunlist.size());
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "加载ListView item_position:" + position);

          // final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dianzanshu", Context.MODE_PRIVATE);

            //打气筒
             ViewHolder viewHolder=null;

            final ListActivityBean.Zixun zixun = list.get(position);
//            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
////            editor.putInt(zixun.zixunId+"dianzanshu",zixun.likeNumber);
////            editor.putString(zixun.zixunId+"state", zixun.state);
//            editor.commit();//提交修改
////            editor.putInt(zixun.zixunId+"dianzanshu",zixun.likeNumber);//??
//            editor.putString(zixun.zixunId+"state", zixun.state);//??
//            editor.commit();//提交修改 ????
//            if((sharedPreferences.getString(zixun.zixunId+"state", zixun.state)).equals("1")){
//                choiceZan.add(zixun.zixunId);
//            }//??可优化
            if(convertView==null){
                viewHolder =new ViewHolder();

                    convertView = View.inflate(getActivity(), R.layout.activity_zi_xun, null);
                    viewHolder.iv_photo = ((ImageView) convertView.findViewById(R.id.iv_photo ));
                    viewHolder.iv_picture = ((ImageView) convertView.findViewById(R.id.iv_picture));
                    viewHolder.tv_xiangxi = ((TextView) convertView.findViewById(R.id.tv_xiangxi));
                    viewHolder.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
                    viewHolder.tv_shoucangpinglun = ((TextView) convertView.findViewById(R.id.tv_shoucangpinglun));
                    //字体加粗
                    TextPaint tp = viewHolder.tv_name.getPaint();
                    tp.setFakeBoldText(true);
                    viewHolder.tv_type = ((TextView) convertView.findViewById(R.id.tv_type));
                    viewHolder.tv_content = ((TextView) convertView.findViewById(R.id.tv_content));
//                    iv_zan=viewHolder.iv_zan = ((ImageView) convertView.findViewById(R.id.iv_dianzan));
//                    final TextView dianzanshu=viewHolder.tv_dianzanshu = ((TextView) convertView.findViewById(tv_dianzanshu));
//
//                iv_zan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final int sp_dianzanshu = sharedPreferences.getInt(zixun.zixunId+"dianzanshu", 0);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                        if (choiceZan.contains((Integer) (((ImageView) v).getTag()))) {
//                            ((ImageView) v).setImageResource(R.drawable.zan_no);
//                            System.out.println("-----------------"+sp_dianzanshu);
//                            dianzannum=sp_dianzanshu -1;
//                            dianzan.put(position,dianzannum);
//                            editor.putInt(zixun.zixunId+"dianzanshu",dianzannum );
//                            editor.commit();//提交修改
//                          //  deleteDianZanNum(zixun.zixunId);
//                            dianzanshu.setText(dianzannum+"");
//                            choiceZan.remove((Integer) (((ImageView) v).getTag()));
//                            choiceZanNum.remove((Integer) ((dianzanshu).getTag()));
//                        } else {
//                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zan);
//                            ((ImageView) v).setImageBitmap(bitmap);
//                            System.out.println("+++++++++++"+sp_dianzanshu);
//                            dianzannum=sp_dianzanshu +1;
//                            // System.out.println(zixun.zixunId);
//                            dianzan.put(position,dianzannum);
//                            editor.putInt(zixun.zixunId+"dianzanshu",dianzannum );
//                            editor.commit();//提交修改
//                          // addDianZanNum(zixun.zixunId);
//                            dianzanshu.setText(dianzannum+"");
//                            choiceZan.add((Integer) (((ImageView) v).getTag()));
//                            choiceZanNum.add((Integer) ((dianzanshu).getTag()));
//                        }
//                    }
//
//
//                });

                convertView.setTag(viewHolder);//缓存对象
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }


            try {


                //Log.i("TAG",(URLDecoder.decode(zixun.timeStamp,"utf-8")));
                viewHolder.tv_xiangxi.setText(URLDecoder.decode(zixun.publisher+" · "+ DateUtils.getGapTimeFromNow(DateUtils.stringToDate(URLDecoder.decode(zixun.timeStamp,"utf-8"))) ,"utf-8"));
                viewHolder.tv_shoucangpinglun.setText(URLDecoder.decode(zixun.likes+"人收藏 ·"+zixun.pingluns+"人评论","utf-8"));
                viewHolder.tv_name.setText(URLDecoder.decode(zixun.title,"utf-8"));
                viewHolder.tv_type.setText(URLDecoder.decode(zixun.type,"utf-8"));
                viewHolder.tv_content.setText(URLDecoder.decode(zixun.content,"utf-8"));
                xUtilsImageUtils.display(viewHolder.iv_photo, HttpUtils.hoster+"upload/"+ URLDecoder.decode(zixun.publisherimg, "utf-8"),true);
                if(list.get(position).photoImg.equals("")){
                    viewHolder.iv_picture.setVisibility(View.GONE);
                }else {
                    viewHolder.iv_picture.setVisibility(View.VISIBLE);
                    xUtilsImageUtils.display(viewHolder.iv_picture, HttpUtils.hoster + URLDecoder.decode(zixun.photoImg, "utf-8").split(",")[0]);
                }

                // Log.i("====",position+"=="+zixunlist.get(0).likes);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


//            //页面显示处理
//            if(choiceZan.contains(zixun.zixunId)){
//                viewHolder.iv_zan.setImageResource(R.drawable.zan);
//
//            }else{
//                viewHolder.iv_zan.setImageResource(R.drawable.zan_no);
//
//            }
//            if (choiceZanNum.contains(zixun.zixunId)){
//                viewHolder.tv_dianzanshu.setText(dianzan.get(position)+"");
//            }else {
//
//
//                viewHolder.tv_dianzanshu.setText(sharedPreferences.getInt(zixun.zixunId+"dianzanshu", 0)+"");
//            }
//            viewHolder.iv_zan.setTag(zixun.zixunId);//设置一个唯一的标识，建议主键
//            viewHolder.tv_dianzanshu.setTag(zixun.zixunId);
            return convertView;
        }


    }


    private class HoudongAdapter extends BaseAdapter {

        @Override
        public int getCount() {
           //  System.out.println(exerciseList.size());
            return exerciseList.size();
        }

        @Override
        public Object getItem(int position) {
            return exerciseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "加载GridView item_position:" + position);

            ViewHolder viewHolder=null;


            if(convertView==null){
                viewHolder =new ViewHolder();

                convertView = View.inflate(getActivity(), R.layout.view_huodong_item, null);
                viewHolder.huodong_tx = ((ImageView) convertView.findViewById(R.id.huodong_tx));
                viewHolder.huongdong_Title = ((TextView) convertView.findViewById(R.id.huongdong_Title));
                viewHolder.huodong_xiangxi = ((TextView) convertView.findViewById(R.id.huodong_xiangxi));


                convertView.setTag(viewHolder);//缓存对象
            }else{

                viewHolder=(ViewHolder)convertView.getTag();
            }

            VariableExercise.Exercises exercises = exerciseList.get(position);
            try {

                viewHolder.huongdong_Title.setText(URLDecoder.decode(exercises.title,"utf-8"));
                viewHolder.huodong_xiangxi.setText(exercises.exerciseId + "·"+DateUtils.getGapTimeFromNow(DateUtils.stringToDate(URLDecoder.decode(exercises.activityTime,"utf-8"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }





            return convertView;
        }

}

    private static class ViewHolder{
        ImageView iv_photo;
        ImageView huodong_tx;
        TextView huongdong_Title;
        TextView huodong_xiangxi;
        ImageView iv_picture;
        TextView tv_type;
        TextView tv_xiangxi;
        TextView tv_name;
        TextView tv_content;
        TextView tv_shoucangpinglun;

    }



}
