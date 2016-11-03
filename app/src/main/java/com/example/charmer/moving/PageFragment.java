package com.example.charmer.moving;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Exercises;
import com.example.charmer.moving.pojo.PersonalInfo;
import com.example.charmer.moving.pojo.ZixunInfo;
import com.example.charmer.moving.utils.MyAdapter;
import com.example.charmer.moving.utils.MyartAdapter;
import com.example.charmer.moving.utils.MycollectAdapter;
import com.example.charmer.moving.utils.MytryAdapter;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private int choice;
    private RecyclerView lv;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.Adapter adapter3;
    private RecyclerView.Adapter adapter4;
    final List<ZixunInfo> exerciseList1 = new ArrayList<ZixunInfo>();
    final List<ZixunInfo> exerciseList2 = new ArrayList<ZixunInfo>();
    final List<Exercises> exerciseList3 = new ArrayList<Exercises>();
    final List<Exercises> exerciseList4 = new ArrayList<Exercises>();
    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homepage_list, null);
        lv = (RecyclerView) view.findViewById(R.id.lv);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        lv.setLayoutManager(layoutManager);

        System.out.println("加载page:"+mPage);

        List<String> list = new ArrayList<String>();
        if(mPage==1){
            adapter1 = new MyartAdapter(exerciseList1);
            lv.setAdapter(adapter1);
            choice=1;
        }else if(mPage==2){
            adapter2 = new MycollectAdapter(exerciseList2);
            lv.setAdapter(adapter2);
            choice=2;
        }else if(mPage==3){
            adapter3 = new MytryAdapter(exerciseList3);
            lv.setAdapter(adapter3);
            choice=3;
        }else{
            adapter4 = new MytryAdapter(exerciseList4);
            lv.setAdapter(adapter4);
            choice=4;
        }
        getExerciseList(choice);
        //lv.setAdapter(new MyAdapter(list));
        return view;
    }

    private void getExerciseList(final int a) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int state = a;
                System.out.println("----++++"+state);
                String str = HttpUtils.hoster+"getpersonalinfo";
                RequestParams params = new RequestParams(str);
                //params.addQueryStringParameter("user",MainActivity.getUser().getUseraccount());
                params.addQueryStringParameter("user","13154623281");
                params.addQueryStringParameter("state",state+"");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        PersonalInfo bean = gson.fromJson(result, PersonalInfo.class);
                        if(choice==1){
                            exerciseList1.clear();
                            exerciseList1.addAll(bean.articles);
                            adapter1.notifyDataSetChanged();
                        }else if(choice==2){
                            exerciseList2.clear();
                            exerciseList2.addAll(bean.collections);
                            adapter2.notifyDataSetChanged();
                        }else if(choice==3){
                            exerciseList3.clear();
                            exerciseList3.addAll(bean.publish);
                            adapter3.notifyDataSetChanged();
                        }else if(choice==4){
                            exerciseList4.clear();
                            exerciseList4.addAll(bean.participate);
                            adapter4.notifyDataSetChanged();
                        }

                        //dongtaiList = bean.dongtailist;   error
//                if(exerciseList.size()>0){
//                    noData.setText("");
//                }else{
//                    noData.setText("没有数据呢!");
//                }
                        //通知listview更新界面

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
        },a*10);



    }
}
