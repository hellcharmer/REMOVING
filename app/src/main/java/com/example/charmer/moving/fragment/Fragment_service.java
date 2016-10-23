package com.example.charmer.moving.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.relevantexercise.ExerciseinfoActivity;
import com.example.charmer.moving.relevantexercise.ManagerexeActivity;
import com.example.charmer.moving.relevantexercise.PublishExe;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;


/**
 * Created by Charmer on 2016/9/19.
 */
public class Fragment_service extends Fragment {

    private static final String TAG = "Fragment_service";
    private LoadMoreListView lv_exercise;
    private BaseAdapter adapter;
    private View view;
    private Spinner spinner1;
    private Spinner spinner2;
    private Button manager_btn;
    private Button tempbtn;
    private SwipeRefreshLayout mSr_refresh;
    private boolean isRunning = false;
    private ObjectAnimator mHeaderAnimator;
    private ObjectAnimator mBottomAnimator;
    private ObjectAnimator spinnerconAnimator;
    private ObjectAnimator ballAnimator;
    private LinearLayout mBottom_bar;
    private LinearLayout mHead_bar;
    private LinearLayout spinnercon;
    private RelativeLayout ball;
    private int i = 0;
    private int page = 1;
    private int totalPage;
    ToastUtil toastUtil;
    Requirement requirement = new Requirement("全部分类","全部主题");
    final ArrayList<VariableExercise.Exercises> exerciseList = new ArrayList<VariableExercise.Exercises>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_service,null);
        View v=inflater.inflate(R.layout.layout_toast_view,null);
        toastUtil=new ToastUtil(getActivity(),v,200);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manager_btn = (Button)view.findViewById(R.id.manager);
        tempbtn = (Button)view.findViewById(R.id.temp);
        lv_exercise = ((LoadMoreListView)view.findViewById(R.id.listview));
        lv_exercise.addHeaderView(View.inflate(getActivity(),R.layout.blankspace,null));
        spinner1 = (Spinner)view.findViewById(R.id.spinner1);
        spinner2 = (Spinner)view.findViewById(R.id.spinner2);

        mBottom_bar = (LinearLayout) getActivity().findViewById(R.id.main_bottom);
        mHead_bar = (LinearLayout) view.findViewById(R.id.headexericse);
        spinnercon = (LinearLayout) view.findViewById(R.id.spinnercon);
        ball = (RelativeLayout) view.findViewById(R.id.plus_rl);
        mSr_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sr_refresh);
        //设置下拉刷新加载圈的颜色
        mSr_refresh.setColorSchemeColors(getResources().getColor(R.color.refreshcolor));
        //设置下拉加载圈出现距离顶部的位置
        mSr_refresh.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        //设置下拉加载圈转动时距离顶部的位置
        mSr_refresh.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));

        bindEvents();

        final ArrayAdapter<CharSequence> adapterspinner1 = ArrayAdapter
                .createFromResource(getActivity(), R.array.exercisetype,
                        android.R.layout.simple_list_item_checked);
        final ArrayAdapter<CharSequence> adapterspinner2 = ArrayAdapter
                .createFromResource(getActivity(), R.array.exercisetheme,
                        android.R.layout.simple_list_item_checked);
        spinner1.setAdapter(adapterspinner1);
        spinner2.setAdapter(adapterspinner2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //System.out.println(adapterspinner1.getItem(position).toString());
                requirement.setExercisetype(adapterspinner1.getItem(position).toString());
                //System.out.println("zuo------------------");
                page = 1;
                getExerciseList(requirement,page);
                i++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(adapterspinner2.getItem(position).toString());
                requirement.setExercisetheme(adapterspinner2.getItem(position).toString());
                //System.out.println("you------------------");
                if(i>1) {
                    page = 1;
                    getExerciseList(requirement,page);

                }i++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
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
                Log.i(TAG, "加载listview item position:" + position);
                ViewHolder viewHolder = null;
                if(convertView == null) {
                    viewHolder = new ViewHolder();
                // 打气筒  view就是指每一个listview item
                    convertView = View.inflate(getActivity(), R.layout.activity_exercise, null);
                    viewHolder.publisherId = ((TextView) convertView.findViewById(R.id.publisherId));
                    viewHolder.type = ((TextView) convertView.findViewById(R.id.type));
                    viewHolder.theme = ((TextView) convertView.findViewById(R.id.theme));
                    viewHolder.place = ((TextView) convertView.findViewById(R.id.place));
                    viewHolder.activityTime = ((TextView) convertView.findViewById(R.id.activityTime));
                    viewHolder.currentNumber = ((TextView) convertView.findViewById(R.id.currentNumber));

                    convertView.setTag(viewHolder);//缓存对象
                }else{
                    viewHolder = (ViewHolder)convertView.getTag();
                }
                VariableExercise.Exercises exercises = exerciseList.get(position);

                try {
                    viewHolder.publisherId.setText(URLDecoder.decode(exercises.publisherId,"utf-8"));
                    viewHolder.type.setText(URLDecoder.decode(exercises.type,"utf-8"));
                    viewHolder.theme.setText(URLDecoder.decode(exercises.theme,"utf-8"));
                    viewHolder.place.setText(URLDecoder.decode(exercises.place,"utf-8"));
                    viewHolder.activityTime.setText(URLDecoder.decode(exercises.activityTime,"utf-8").substring(0,16));
                    viewHolder.currentNumber.setText(URLDecoder.decode(exercises.currentNumber.toString(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return convertView;
            }
        };


        //LayoutInflater inflater = LayoutInflater.from(getActivity());
        lv_exercise.setAdapter(adapter);



        manager_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManagerexeActivity.class);
                startActivity(intent);
            }
        });

        tempbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublishExe.class);
                startActivity(intent);
            }
        });
        lv_exercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ExerciseinfoActivity.class);
                intent.putExtra("exerciseId", exerciseList.get(position-1).exerciseId+"");
                startActivity(intent);
            }
        });
        lv_exercise.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {

                loadExemore();
            }
        });

    }


    private void bindEvents(){
        mSr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSr_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getExerciseList(requirement,page);
                        //调用该方法结束刷新，否则加载圈会一直在
                        mSr_refresh.setRefreshing(false);
                    }
                },1000);
            }
        });

        lv_exercise.setOnTouchListener(new View.OnTouchListener() {
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


    public void hideBar() {
        mHeaderAnimator = ObjectAnimator.ofFloat(mHead_bar, "translationY", -mHead_bar.getHeight());
        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar, "translationY", mBottom_bar.getHeight());
        spinnerconAnimator = ObjectAnimator.ofFloat(spinnercon, "translationY", -spinnercon.getHeight());
        ballAnimator = ObjectAnimator.ofFloat(ball, "translationY",200);
        mHeaderAnimator.setDuration(500).start();
        mBottomAnimator.setDuration(400).start();
        spinnerconAnimator.setDuration(500).start();
        ballAnimator.setDuration(300).start();
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
        spinnerconAnimator = ObjectAnimator.ofFloat(spinnercon,"translationY", 0);
        ballAnimator = ObjectAnimator.ofFloat(spinnercon,"translationY", 0);
        mHeaderAnimator.setDuration(300).start();
        mBottomAnimator.setDuration(400).start();
        spinnerconAnimator.setDuration(300).start();
        ballAnimator.setDuration(300).start();
    }

    private void loadExemore() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(page<totalPage) {

                    getExerciseList(requirement,++page);
                }else {
                    //Toast.makeText(getContext(),"已经到底了",Toast.LENGTH_LONG).show();
                    toastUtil.Short(getActivity(),"已经到底了！").show();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lv_exercise.setLoadCompleted();
                    }
                });
            }
        }.start();
    }

    private void getExerciseList(Requirement requirement,int page) {
        final int currentpage = page;
        String str = "http://10.40.5.13:8080/moving/getexercise";
        RequestParams params = new RequestParams(str);
        params.addQueryStringParameter("exercisetype",requirement.getExercisetype().toString());
        params.addQueryStringParameter("exercisetheme",requirement.getExercisetheme().toString());
        params.addQueryStringParameter("page",page+"");

        params.addQueryStringParameter("place","苏州");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if(currentpage==1){
                    exerciseList.clear();
                }

                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                totalPage = bean.totalPage;
                exerciseList.addAll(bean.exerciseList);
                //dongtaiList = bean.dongtailist;   error
                System.out.println(bean.exerciseList);

                Log.i("exerciseList", "exerciseList: "+exerciseList);
                //通知listview更新界面
                adapter.notifyDataSetChanged();

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

    private static class ViewHolder{
        TextView publisherId ;
        TextView type ;
        TextView theme ;
        TextView place ;
        TextView activityTime ;
        TextView cost ;
        TextView paymentMethod ;
        TextView currentNumber ;
        TextView totalNumber ;
}

    private class Requirement {
        private String exercisetype;
        private String exercisetheme;

        public String getExercisetheme() {
            return exercisetheme;
        }

        public void setExercisetheme(String exercisetheme) {
            this.exercisetheme = exercisetheme;
        }

        public String getExercisetype() {
            return exercisetype;
        }

        public void setExercisetype(String exercisetype) {
            this.exercisetype = exercisetype;
        }

        public Requirement() {
        }

        public Requirement(String exercisetype, String exercisetheme) {
            this.exercisetype = exercisetype;
            this.exercisetheme = exercisetheme;
        }


    }
}
