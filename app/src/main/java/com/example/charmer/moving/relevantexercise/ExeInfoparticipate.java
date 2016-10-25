package com.example.charmer.moving.relevantexercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class ExeInfoparticipate extends AppCompatActivity {


    private BaseAdapter adapter;
    private ListView lv_exercise;
    private TextView title ;
    private Button canceljoin;
    private TextView name;
    private TextView successfulpublishpercent;
    private TextView appointmentRate;
    private ImageView imguser;
    private static final String TAG = "ExerciseinfoActivity";
    final ArrayList<VariableExercise.Exercises> exerciseList = new ArrayList<VariableExercise.Exercises>();
    private TextView textintroduce;
    VariableExercise.DataSummary ds = new VariableExercise.DataSummary();
    private String exerciseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe_infoparticipate);
        Intent intent = this.getIntent();
        exerciseId = intent.getStringExtra("exerciseId");
        lv_exercise = ((ListView)findViewById(R.id.exemidinfolist));
        textintroduce = ((TextView) findViewById(R.id.textintroduce));
        title = ((TextView) findViewById(R.id.titleinfo));
        canceljoin = ((Button) findViewById(R.id.canceljoin));
        name = ((TextView) findViewById(R.id.name));
        successfulpublishpercent = ((TextView) findViewById(R.id.successfulpublishpercent));
        appointmentRate = ((TextView) findViewById(R.id.appointmentRate));
        imguser = ((ImageView) findViewById(R.id.imguser));
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

                viewHolder = new ViewHolder();
                // 打气筒  view就是指每一个listview item
                convertView = View.inflate(ExeInfoparticipate.this, R.layout.exemidinfo, null);
                viewHolder.type = ((TextView) convertView.findViewById(R.id.type));
                viewHolder.theme = ((TextView) convertView.findViewById(R.id.theme));
                viewHolder.place = ((TextView) convertView.findViewById(R.id.place));
                viewHolder.activityTime = ((TextView) convertView.findViewById(R.id.activityTime));
                viewHolder.cost = ((TextView) convertView.findViewById(R.id.cost));
                viewHolder.paymentMethod = ((TextView) convertView.findViewById(R.id.paymentMethod));
                viewHolder.currentNumber = ((TextView) convertView.findViewById(R.id.currentNumber));
                viewHolder.totalNumber = ((TextView) convertView.findViewById(R.id.totalNumber));


                VariableExercise.Exercises exercises = exerciseList.get(position);

                try {
                    viewHolder.type.setText(URLDecoder.decode(exercises.type,"utf-8"));
                    viewHolder.theme.setText(URLDecoder.decode(exercises.theme,"utf-8"));
                    viewHolder.place.setText(URLDecoder.decode(exercises.place,"utf-8"));
                    viewHolder.activityTime.setText(URLDecoder.decode(exercises.activityTime,"utf-8").substring(0,16));
                    viewHolder.cost.setText(URLDecoder.decode(exercises.cost.toString(),"utf-8"));
                    viewHolder.paymentMethod.setText(URLDecoder.decode(exercises.paymentMethod,"utf-8"));
                    viewHolder.currentNumber.setText(URLDecoder.decode(exercises.currentNumber.toString(),"utf-8"));
                    viewHolder.totalNumber.setText(URLDecoder.decode(exercises.totalNumber.toString(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return convertView;
            }
        };
        lv_exercise.setAdapter(adapter);
        getExerciseList(exerciseId);

        canceljoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(1==ExeSharedMthd.cancelJoin(exerciseId, MyApplication.getUser().getUseraccount(),ExeInfoparticipate.this)){
                    finish();
                }
            }
        });

    }
    private void getExerciseList(String exerciseId) {
        String str = "http://10.40.5.13:8080/moving/getexebyid";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                exerciseList.clear();
                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                exerciseList.addAll(bean.exerciseList);
                ds = bean.ds;
                try{
                    title.setText(URLDecoder.decode(bean.exerciseList.get(0).title,"utf-8"));
                    textintroduce.setText(URLDecoder.decode(bean.exerciseList.get(0).exerciseIntroduce,"utf-8"));
                    name.setText(ds.userName);
                    successfulpublishpercent.setText(ds.successfulpublishpercent);
                    appointmentRate.setText(ds.appointmentRate);
                    xUtilsImageUtils.display(imguser,"http://10.40.5.13:8080/moving/upload/"+ds.userImg);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
        TextView type ;
        TextView theme ;
        TextView place ;
        TextView activityTime ;
        TextView cost ;
        TextView paymentMethod ;
        TextView currentNumber ;
        TextView totalNumber ;
    }
}
