package com.example.charmer.moving.relevantexercise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.Homepage;
import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.GridView_picture;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ExerciseinfoActivity extends AppCompatActivity {
    private BaseAdapter adapter;
    private BaseAdapter imgadapter;
    private ListView lv_exercise;
    private TextView title ;
    private Button enroll;
    private TextView name;
    private TextView successfulpublishpercent;
    private TextView publishNum;
    private ImageView imguser;
    private Integer currectNum;
    private Integer totalNum;
    private GridView_picture joinerImgs;
    private RelativeLayout finishthis;
    String exerciseId;
    private ImageView joinerImg;
    private static final String TAG = "ExerciseinfoActivity";
    final ArrayList<VariableExercise.Exercises> exerciseList = new ArrayList<VariableExercise.Exercises>();
    private TextView textintroduce;
    VariableExercise.DataSummary ds = new VariableExercise.DataSummary();
    final List<VariableExercise.DataSummary> dsListJoin = new ArrayList<VariableExercise.DataSummary>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exerciseinfo);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        Intent intent = this.getIntent();
        exerciseId = intent.getStringExtra("exerciseId");
        lv_exercise = ((ListView)findViewById(R.id.exemidinfolist));
        textintroduce = ((TextView) findViewById(R.id.textintroduce));
        title = ((TextView) findViewById(R.id.titleinfo));
        enroll = ((Button) findViewById(R.id.enroll));
        enroll.setEnabled(false);
        name = ((TextView) findViewById(R.id.name));
        successfulpublishpercent = ((TextView) findViewById(R.id.successfulpublishpercent));
        publishNum = ((TextView) findViewById(R.id.publishNum));
        imguser = ((ImageView) findViewById(R.id.imguser));
        joinerImgs = ((GridView_picture) findViewById(R.id.joinerImgs));
        finishthis =((RelativeLayout) findViewById(R.id.finishthis));
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
                    convertView = View.inflate(ExerciseinfoActivity.this, R.layout.exemidinfo, null);
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
                    currectNum = exercises.currentNumber;
                    viewHolder.totalNumber.setText(URLDecoder.decode(exercises.totalNumber.toString(),"utf-8"));
                    totalNum = exercises.totalNumber;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return convertView;
            }
        };

        lv_exercise.setAdapter(adapter);

        imgadapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return dsListJoin.size();
            }

            @Override
            public Object getItem(int position) {
                return dsListJoin.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final int position1 = position;
                convertView = View.inflate(ExerciseinfoActivity.this, R.layout.joinerimg, null);
                joinerImg= (ImageView) convertView.findViewById(R.id.joinerImg);
                VariableExercise.DataSummary vds = dsListJoin.get(position);
                xUtilsImageUtils.display(joinerImg, HttpUtils.hoster+"upload/"+vds.userImg);
                System.out.println("==-=-=-=-=-=-"+vds.userImg);
                joinerImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(ExerciseinfoActivity.this, Homepage.class);
                        intent1.putExtra("user",dsListJoin.get(position1).userAccount.toString());
                        startActivity(intent1);
                    }
                });
                return convertView;
            }
        };

        joinerImgs.setAdapter(imgadapter);

        getExerciseList(exerciseId);

        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ExerciseinfoActivity.this, Homepage.class);
                System.out.println("++++"+exerciseList.get(0).publisherId);
                intent1.putExtra("user",exerciseList.get(0).publisherId);
                startActivity(intent1);
            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ExerciseinfoActivity.this);
                builder.setMessage(getString(R.string.enrollExe_sure));
                builder.setTitle("您将报名此活动！");
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
                                if(MainActivity.getUser().getUseraccount().equals(exerciseList.get(0).publisherId.toString())){
                                    Toast.makeText(ExerciseinfoActivity.this,"发布者不能报名自己的活动！",Toast.LENGTH_SHORT).show();
                                }else if (currectNum>=totalNum){
                                    Toast.makeText(ExerciseinfoActivity.this,"活动人数已满！",Toast.LENGTH_SHORT).show();
                                }else{
                                    ExeSharedMthd.tryToEnroll(exerciseId, MainActivity.getUser().getUseraccount(), ExerciseinfoActivity.this);
                                }
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
        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getExerciseList(String exerciseId) {
        String str = HttpUtils.hoster+"getexebyid";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                exerciseList.clear();
                VariableExercise bean = gson.fromJson(result, VariableExercise.class);
                exerciseList.addAll(bean.exerciseList);
                dsListJoin.addAll(bean.dsListJoin);
                ds = bean.ds;
                if(exerciseList.size()>0) {
                    enroll.setEnabled(true);
                }
                try{
                    title.setText(URLDecoder.decode(bean.exerciseList.get(0).title,"utf-8"));
                    textintroduce.setText(URLDecoder.decode(bean.exerciseList.get(0).exerciseIntroduce,"utf-8"));
                    name.setText(ds.userName);
                    successfulpublishpercent.setText(ds.successfulpublishpercent);
                    publishNum.setText(ds.publishedNum+"");
                    xUtilsImageUtils.display(imguser,HttpUtils.hoster+"upload/"+ds.userImg);

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

                //通知listview更新界面
                adapter.notifyDataSetChanged();
                imgadapter.notifyDataSetChanged();
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
