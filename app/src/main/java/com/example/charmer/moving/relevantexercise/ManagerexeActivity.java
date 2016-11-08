package com.example.charmer.moving.relevantexercise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.VariableExercise;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ManagerexeActivity extends AppCompatActivity {

    private BaseAdapter adapter1;
    private BaseAdapter adapter2;
    private BaseAdapter adapter3;
    private Button cancelExe;
    private Button cancelJoin;
    private Button cancelEnroll;
    private TextView noData1;
    private TextView noData2;
    private TextView noData3;
    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    private ListView exelist1;
    private ListView exelist2;
    private ListView exelist3;
    private SwipeRefreshLayout mSr_refresh;
    private SwipeRefreshLayout mSr_refresh2;
    private SwipeRefreshLayout mSr_refresh3;
    private RelativeLayout finishthis;
    private ImageView isscan;
    ArrayList<View> viewContainter = new ArrayList<View>();
    List<String> titleContainer = new ArrayList<String>();
    public String TAG = "tag";
    final ArrayList<VariableExercise.Exercises> exeinfolist1 = new ArrayList<VariableExercise.Exercises>();
    final ArrayList<VariableExercise.Exercises> exeinfolist2 = new ArrayList<VariableExercise.Exercises>();
    final ArrayList<VariableExercise.Exercises> exeinfolist3 = new ArrayList<VariableExercise.Exercises>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerexe);

        pager = (ViewPager) this.findViewById(R.id.vper);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.strip);
        finishthis =((RelativeLayout) findViewById(R.id.finishthis));


        //取消tab下面的长横线
       // titleStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.colorSkybule));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.white));
        tabStrip.setTextSpacing(200);

        View view1 = LayoutInflater.from(this).inflate(R.layout.blanklist, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.blanklistsupply, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.blanklistenroll, null);

        mSr_refresh = (SwipeRefreshLayout) view1.findViewById(R.id.exeitemrefresh);
        //设置下拉刷新加载圈的颜色
        mSr_refresh.setColorSchemeColors(getResources().getColor(R.color.refreshcolor));
        //设置下拉加载圈出现距离顶部的位置
        mSr_refresh.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        //设置下拉加载圈转动时距离顶部的位置
        mSr_refresh.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));

        mSr_refresh2 = (SwipeRefreshLayout) view2.findViewById(R.id.exeitem2refresh);
        //设置下拉刷新加载圈的颜色
        mSr_refresh2.setColorSchemeColors(getResources().getColor(R.color.refreshcolor));
        //设置下拉加载圈出现距离顶部的位置
        mSr_refresh2.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        //设置下拉加载圈转动时距离顶部的位置
        mSr_refresh2.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));

        mSr_refresh3 = (SwipeRefreshLayout) view3.findViewById(R.id.exeitem3refresh);
        //设置下拉刷新加载圈的颜色
        mSr_refresh3.setColorSchemeColors(getResources().getColor(R.color.refreshcolor));
        //设置下拉加载圈出现距离顶部的位置
        mSr_refresh3.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        //设置下拉加载圈转动时距离顶部的位置
        mSr_refresh3.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));

        bindEvents();
        exelist1 = (ListView)view1.findViewById(R.id.exeitem);
        exelist2 = (ListView)view2.findViewById(R.id.exeitem2);
        exelist3 = (ListView)view3.findViewById(R.id.exeitem3);
        noData1 = (TextView) view1.findViewById(R.id.noData);
        noData2 = (TextView) view2.findViewById(R.id.noData);
        noData3 = (TextView) view3.findViewById(R.id.noData);
        //viewpager开始添加view
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        //页签项
        titleContainer.add("我的发布");
        titleContainer.add("我的参加");
        titleContainer.add("我的报名");

        tabStrip.setTextSize(1,18);
        tabStrip.setTextColor(this.getResources().getColor(R.color.white));
        pager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }
            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }
            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleContainer.get(position);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.d(TAG, "--------changed:" + arg0);

            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                Log.d(TAG, "--------onPageScrolled:" + arg0);
//                Log.d(TAG, "--------onPageScrolled:" + arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                Log.d(TAG, "------selected:" + arg0);
                switch (arg0){
                    case 0:
                        exelist2.setAdapter(adapter2);
                        getExespart();
                        break;
                    case 1:
                        exelist1.setAdapter(adapter1);
                        getExe();
                        exelist3.setAdapter(adapter3);
                        getExesenroll();
                        break;
                    case 2:
                        exelist2.setAdapter(adapter2);
                        getExespart();
                        break;
                }
            }
        });


        adapter1 = new MyexeAdapter();
        adapter2 = new exeAdapter();
        adapter3 = new exeenrollAdapter();

        exelist1.setAdapter(adapter1);
        getExe();
        exelist2.setAdapter(adapter2);
        getExespart();
        exelist3.setAdapter(adapter3);
        getExesenroll();
        exelist1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManagerexeActivity.this, ExeInfopublisher.class);
                intent.putExtra("exerciseId", exeinfolist1.get(position).exerciseId+"");
                startActivity(intent);

            }
        });
        exelist2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManagerexeActivity.this, ExeInfoparticipate.class);
                intent.putExtra("exerciseId", exeinfolist2.get(position).exerciseId+"");
                startActivity(intent);

            }
        });
        exelist3.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManagerexeActivity.this, ExeInfoEnroll.class);
                intent.putExtra("exerciseId", exeinfolist3.get(position).exerciseId+"");
                startActivity(intent);

            }
        });
        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("+++++++++++++++onRestart");
        exelist1.setAdapter(adapter1);
        getExe();
        exelist2.setAdapter(adapter2);
        getExespart();
        exelist3.setAdapter(adapter3);
        getExesenroll();
    }

    private void bindEvents() {
        mSr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getExe();
                        //调用该方法结束刷新，否则加载圈会一直在
                        if(exeinfolist1.size()>0){
                            noData1.setText("");
                        }else{
                            noData1.setText("没有数据呢!");
                        }
                        mSr_refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        mSr_refresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getExespart();
                        //调用该方法结束刷新，否则加载圈会一直在
                        if(exeinfolist2.size()>0){
                            noData2.setText("");
                        }else{
                            noData2.setText("没有数据呢!");
                        }
                        mSr_refresh2.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        mSr_refresh3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getExesenroll();
                        //调用该方法结束刷新，否则加载圈会一直在
                        if(exeinfolist3.size()>0){
                            noData3.setText("");
                        }else{
                            noData3.setText("没有数据呢!");
                        }
                        mSr_refresh3.setRefreshing(false);
                    }
                }, 1000);
            }
        });

    }

    private void getExe() {
        String str = HttpUtils.hoster+"getexebypublish";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("publisher", MainActivity.getUser().getUseraccount());

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                exeinfolist1.clear();
                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                exeinfolist1.addAll(bean.exerciseList);
                //dongtaiList = bean.dongtailist;   error
                if(exeinfolist1.size()>0){
                    noData1.setText("");
                }else{
                    noData1.setText("没有数据呢!");
                }
                //通知listview更新界面
                adapter1.notifyDataSetChanged();

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

    private void getExespart() {
        String str = HttpUtils.hoster+"getexebypart";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("participator",MainActivity.getUser().getUseraccount());
        params.addQueryStringParameter("mode","0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(!"".equals(result)) {
                    Gson gson = new Gson();
                    exeinfolist2.clear();
                    VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                    exeinfolist2.addAll(bean.exerciseList);
                    //dongtaiList = bean.dongtailist;   error
                    if(exeinfolist2.size()>0){
                        noData2.setText("");
                    }else{
                        noData2.setText("没有数据呢!");
                    }
                    //通知listview更新界面
                    adapter2.notifyDataSetChanged();
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

    private void getExesenroll() {
        String str = HttpUtils.hoster+"getexebypart";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("participator",MainActivity.getUser().getUseraccount());
        params.addQueryStringParameter("mode","1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                exeinfolist3.clear();
                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                exeinfolist3.addAll(bean.exerciseList);
                //dongtaiList = bean.dongtailist;   error
                if(exeinfolist3.size()>0){
                    noData3.setText("");
                }else{
                    noData3.setText("没有数据呢!");
                }
                //通知listview更新界面
                adapter3.notifyDataSetChanged();

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

    class MyexeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return exeinfolist1.size();
        }

        @Override
        public Object getItem(int position) {
            return exeinfolist1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "加载listview item position:" + position);
            final int position1 = position;
            ViewHolder viewHolder = null;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                // 打气筒  view就是指每一个listview item
                convertView = View.inflate(ManagerexeActivity.this, R.layout.exeitemintro, null);
                viewHolder.type = ((TextView) convertView.findViewById(R.id.exetype));
                viewHolder.title = ((TextView) convertView.findViewById(R.id.exetitle));
                viewHolder.place = ((TextView) convertView.findViewById(R.id.exeplace));
                viewHolder.activityTime = ((TextView) convertView.findViewById(R.id.exetime));
                viewHolder.releaseTime = ((TextView) convertView.findViewById(R.id.releasetime));
                cancelExe = (Button) convertView.findViewById(R.id.cancelExe);
                convertView.setTag(viewHolder);//缓存对象
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final VariableExercise.Exercises exercises = exeinfolist1.get(position);

            try {
                viewHolder.type.setText(URLDecoder.decode(exercises.type,"utf-8"));
                viewHolder.title.setText(URLDecoder.decode(exercises.title,"utf-8"));
                viewHolder.place.setText(URLDecoder.decode(exercises.place,"utf-8"));
                viewHolder.activityTime.setText(URLDecoder.decode(exercises.activityTime,"utf-8").substring(5,16));
                viewHolder.releaseTime.setText(URLDecoder.decode(exercises.releaseTime,"utf-8").substring(5,16));
                cancelExe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ManagerexeActivity.this);
                        builder.setMessage(getString(R.string.cancel_sure));
                        builder.setTitle(R.string.cancelExeintro);
//                    builder.setIcon(getResources().getDrawable(
//                            R.drawable.delete1));
                        builder.setPositiveButton(
                                getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int which) {
                                        // TODO Auto-generated method
                                        cancelExe(position1,exeinfolist1.get(position1).exerciseId.toString(),MainActivity.getUser().getUseraccount(),ManagerexeActivity.this);
                                    }
                                });
                        builder.setNegativeButton(
                                getString(R.string.app_cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int which) {
                                        // TODO Auto-generated method
                                        // stub
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return convertView;
        }

    }

    class exeAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return exeinfolist2.size();
    }

    @Override
    public Object getItem(int position) {
        return exeinfolist2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "加载listview item position:" + position);
        final int position1 = position;
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            // 打气筒  view就是指每一个listview item
            convertView = View.inflate(ManagerexeActivity.this, R.layout.joinexeitemintro, null);
            viewHolder.type = ((TextView) convertView.findViewById(R.id.exetype));
            viewHolder.title = ((TextView) convertView.findViewById(R.id.exetitle));
            viewHolder.place = ((TextView) convertView.findViewById(R.id.exeplace));
            viewHolder.activityTime = ((TextView) convertView.findViewById(R.id.exetime));
            viewHolder.releaseTime = ((TextView) convertView.findViewById(R.id.releasetime));
            cancelJoin = (Button) convertView.findViewById(R.id.cancelJoin);
            isscan = (ImageView) convertView.findViewById(R.id.isscan);

            convertView.setTag(viewHolder);//缓存对象
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        VariableExercise.Exercises exercises = exeinfolist2.get(position);

        try {
            viewHolder.type.setText(URLDecoder.decode(exercises.type,"utf-8"));
            viewHolder.place.setText(URLDecoder.decode(exercises.place,"utf-8"));
            viewHolder.title.setText(URLDecoder.decode(exercises.title,"utf-8"));
            viewHolder.activityTime.setText(URLDecoder.decode(exercises.activityTime,"utf-8").substring(5,16));
            viewHolder.releaseTime.setText(URLDecoder.decode(exercises.releaseTime,"utf-8").substring(5,16));
            System.out.println("-----------"+exercises.isScan);
            if(!exercises.isScan){
                isscan.setVisibility(View.GONE);
            }

            cancelJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            ManagerexeActivity.this);
                    builder.setMessage(getString(R.string.cancel_sure));
                    builder.setTitle(R.string.canceljoinExeintro);
//                    builder.setIcon(getResources().getDrawable(
//                            R.drawable.delete1));
                    builder.setPositiveButton(
                            getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface,
                                        int which) {
                                    // TODO Auto-generated method
                                    cancelRequest(position1,exeinfolist2.get(position1).exerciseId.toString(), MainActivity.getUser().getUseraccount(),ManagerexeActivity.this);
                                }
                            });
                    builder.setNegativeButton(
                            getString(R.string.app_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface,
                                        int which) {
                                    // TODO Auto-generated method
                                    // stub
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.create().show();

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertView;
    }

}

    class exeenrollAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return exeinfolist3.size();
        }

        @Override
        public Object getItem(int position) {
            return exeinfolist3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int position1 = position;
            Log.i(TAG, "加载listview item position:" + position);
            ViewHolder viewHolder = null;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                // 打气筒  view就是指每一个listview item
                convertView = View.inflate(ManagerexeActivity.this, R.layout.exeitemenroll, null);
                viewHolder.type = ((TextView) convertView.findViewById(R.id.exetype));
                viewHolder.place = ((TextView) convertView.findViewById(R.id.exeplace));
                viewHolder.title = ((TextView) convertView.findViewById(R.id.exetitle));
                viewHolder.activityTime = ((TextView) convertView.findViewById(R.id.exetime));
                viewHolder.releaseTime = ((TextView) convertView.findViewById(R.id.releasetime));
                cancelEnroll = ((Button) convertView.findViewById(R.id.cancelEnroll));
                convertView.setTag(viewHolder);//缓存对象
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            VariableExercise.Exercises exercises = exeinfolist3.get(position);

            try {
                viewHolder.type.setText(URLDecoder.decode(exercises.type,"utf-8"));
                viewHolder.place.setText(URLDecoder.decode(exercises.place,"utf-8"));
                viewHolder.title.setText(URLDecoder.decode(exercises.title,"utf-8"));
                viewHolder.activityTime.setText(URLDecoder.decode(exercises.activityTime,"utf-8").substring(5,16));
                viewHolder.releaseTime.setText(URLDecoder.decode(exercises.releaseTime,"utf-8").substring(5,16));

                cancelEnroll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ManagerexeActivity.this);
                        builder.setMessage(getString(R.string.cancel_sure));
                        builder.setTitle(R.string.cancelenrollintro);
//                    builder.setIcon(getResources().getDrawable(
//                            R.drawable.delete1));
                        builder.setPositiveButton(
                                getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int which) {
                                        // TODO Auto-generated method
                                        cancelEnroll(position1,exeinfolist3.get(position1).exerciseId.toString(), MainActivity.getUser().getUseraccount(), ManagerexeActivity.this);
                                    }
                                });
                        builder.setNegativeButton(
                                getString(R.string.app_cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface,
                                            int which) {
                                        // TODO Auto-generated method
                                        // stub
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                });

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return convertView;
        }

    }

    private static class ViewHolder{
        TextView type ;
        TextView title ;
        TextView place ;
        TextView activityTime ;
        TextView releaseTime ;
        TextView theme;

    }

    public void cancelExe(int position,String exerciseId,String publisher,Context contexts){
        final int position1 = position;
        final Context context = contexts;
        String str = HttpUtils.hoster+"cancelany";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("publisher",publisher);
        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("choice","3");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                    Toast.makeText(context,"取消活动成功！",Toast.LENGTH_SHORT).show();
                    exeinfolist1.remove(position1);
                    adapter1.notifyDataSetChanged();
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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

    public void cancelRequest(int position,String exerciseId,String joiner,Context contexts){
        final Context context = contexts;
        final int position1 =position;
        String str = HttpUtils.hoster+"cancelany";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("choice","4");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                   // Toast.makeText(context,"取消参加成功！",Toast.LENGTH_SHORT).show();
                    exeinfolist2.remove(position1);
                    adapter2.notifyDataSetChanged();
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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
    public void cancelEnroll(int position,String exerciseId,String joiner,Context contexts){
        final int position1 = position;
        final Context context = contexts;
        String str = HttpUtils.hoster+"cancelany";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("choice","1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                    Toast.makeText(context,"报名取消成功！",Toast.LENGTH_SHORT).show();
                    exeinfolist3.remove(position1);
                    adapter3.notifyDataSetChanged();
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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
}