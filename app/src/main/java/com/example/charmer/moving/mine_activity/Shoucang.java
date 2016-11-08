package com.example.charmer.moving.mine_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.PersonalInfo;
import com.example.charmer.moving.pojo.ZixunInfo;
import com.example.charmer.moving.utils.MycollectAdapter;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class Shoucang extends AppCompatActivity {
    private RecyclerView lv;
    private RecyclerView.Adapter adapter2;
    final List<ZixunInfo> exerciseList2 = new ArrayList<ZixunInfo>();
    String useraccount;
    private RelativeLayout finishthis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);
        initView();
        SharedPreferences sharedPreferences = Shoucang.this.getSharedPreferences("sp_mobile", Context.MODE_PRIVATE);
        useraccount = sharedPreferences.getString("useraccount", "");

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(Shoucang.this);
        // 设置布局管理器
        lv.setLayoutManager(layoutManager);
        adapter2 = new MycollectAdapter(exerciseList2);
        lv.setAdapter(adapter2);
        getExerciseList();
        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getExerciseList() {


        String str = HttpUtils.hoster + "getpersonalinfo";
        RequestParams params = new RequestParams(str);
        //params.addQueryStringParameter("user",MainActivity.getUser().getUseraccount());
        params.addQueryStringParameter("user", useraccount);
        params.addQueryStringParameter("state", "2");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("========"+result);
                Gson gson = new Gson();
                PersonalInfo bean = gson.fromJson(result, PersonalInfo.class);

                exerciseList2.clear();
                exerciseList2.addAll(bean.collections);
                adapter2.notifyDataSetChanged();


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

    private void initView() {
        lv = (RecyclerView) findViewById(R.id.lv);
        finishthis = (RelativeLayout) findViewById(R.id.finishthis);
    }
}
