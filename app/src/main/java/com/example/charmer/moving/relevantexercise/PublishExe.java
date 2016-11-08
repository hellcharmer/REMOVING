package com.example.charmer.moving.relevantexercise;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.fragment.WheelDialogFragment;
import com.example.charmer.moving.pojo.Exercises;
import com.example.charmer.moving.utils.DateUtil;
import com.example.charmer.moving.utils.ResUtil;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

public class PublishExe extends AppCompatActivity  implements View.OnTouchListener {

    private EditText exe_pb_name;
    private EditText exepb_cost;
    private EditText exepb_place;
    private EditText exepb_intro;
    private TextView mthdslt;
    private TextView numslt;
    private TextView typeslt;
    private TextView themeslt;
    private TextView etStartTime;
    private RelativeLayout commitbtn;
    private Exercises exepub;
    private RelativeLayout finishthis;
    private RelativeLayout gaode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_exe);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        etStartTime = (TextView) this.findViewById(R.id.et_start_time);
        etStartTime.setOnTouchListener(this);
        typeslt = (TextView) findViewById(R.id.typeslt);
        typeslt.setOnClickListener(new tvclick(R.array.exepbtype, typeslt));
        themeslt = (TextView) findViewById(R.id.themeslt);
        themeslt.setOnClickListener(new tvclick(R.array.exepbtheme, themeslt));
        numslt = (TextView) findViewById(R.id.exepb_num);
        numslt.setOnClickListener(new tvclick(R.array.exepbnumber, numslt));
        mthdslt = (TextView) findViewById(R.id.exepb_ctmth);
        mthdslt.setOnClickListener(new tvclick(R.array.exepbcostmthd, mthdslt));
        commitbtn = (RelativeLayout) findViewById(R.id.commitbtn);
        finishthis =((RelativeLayout) findViewById(R.id.finishthis));
        exe_pb_name = (EditText) findViewById(R.id.exe_pb_name);
        exepb_cost = (EditText) findViewById(R.id.exepb_cost);
        exepb_place = (EditText) findViewById(R.id.exepb_place);
        exepb_intro = (EditText) findViewById(R.id.exepb_intro);
        gaode =((RelativeLayout) findViewById(R.id.gaode));
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(exepb_intro.getWindowToken(), 0);
        commitbtn.setOnClickListener(new commitbtn());

        findViewById(R.id.rltv).setOnClickListener(new rltvclick());
        finishthis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishExe.this,EventsActivity.class);
                Bundle bundle = new Bundle();
                String str = "";
                bundle.putString("str1", str);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);

            }
        });
    }


    private class tvclick implements View.OnClickListener {
        private TextView tempview;
        private int resId;

        public tvclick(int resId, TextView tempview) {
            this.resId = resId;
            this.tempview = tempview;
        }

        @Override
        public void onClick(View v) {
            final WheelDialogFragment wheelViewDialogFragment = WheelDialogFragment
                    .newInstance(ResUtil.getStringArray(this.resId),
                            ResUtil.getString(R.string.app_cancel),
                            ResUtil.getString(R.string.app_sure), true, false, false);
            wheelViewDialogFragment.setWheelDialogListener(new WheelDialogFragment.OnWheelDialogListener() {
                @Override
                public void onClickLeft(String value) {
                    wheelViewDialogFragment.dismiss();
                }

                @Override
                public void onClickRight(String value) {
                    wheelViewDialogFragment.dismiss();
                    tempview.setText(value);
                    tempview.setTextColor(getResources().getColor(R.color.refreshcolor));
                }

                @Override
                public void onValueChanged(String value) {
                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                }
            });
            wheelViewDialogFragment.show(getSupportFragmentManager(), "");

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.date_time_dialog, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            datePicker.setMinDate(cal.getTimeInMillis());
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 15);
            datePicker.setMaxDate(cal.getTimeInMillis());

            final int inType = etStartTime.getInputType();
            etStartTime.setInputType(InputType.TYPE_NULL);
            etStartTime.onTouchEvent(event);
            etStartTime.setInputType(inType);
            //etStartTime.setSelection(etStartTime.getText().length());

            builder.setTitle("请确定活动起始时间");
            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(String.format("%d-%02d-%02d",
                            datePicker.getYear(),
                            datePicker.getMonth() + 1,
                            datePicker.getDayOfMonth()));
                    sb.append("  ");
                    sb.append(timePicker.getCurrentHour())
                            .append(":").append(timePicker.getCurrentMinute());
                    etStartTime.setText(sb);
                    dialog.cancel();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                String str = b.getString("str1");//str即为回传的值
                exepb_place.setText(str);
                break;
            default:
                break;

        }
    }
        private class rltvclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public class commitbtn implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            System.out.println("----------------"+exepb_intro.getText().toString());

            if (!"".equals(exe_pb_name.getText().toString().trim()) && !"".equals(exepb_place.getText().toString().trim())
                    && !"".equals(etStartTime.getText().toString().trim()) &&!"".equals(exepb_cost.getText().toString().trim())
                    && !"请选类型".equals(typeslt.getText().toString()) && !"请选主题".equals(themeslt.getText().toString())
                    && !"请选方式".equals(mthdslt.getText().toString()) &&!"请选人数".equals(numslt.getText().toString()))
            {
                //Toast.makeText(PublishExe.this, "提交成功", Toast.LENGTH_SHORT).show();
                exepub = new Exercises(Long.parseLong(MainActivity.getUser().getUseraccount()), exe_pb_name.getText().toString(), typeslt.getText().toString(),
                       themeslt.getText().toString(), exepb_intro.getText().toString(), exepb_place.getText().toString(),
                       DateUtil.stringToDate(etStartTime.getText().toString()), Double.parseDouble(exepb_cost.getText().toString()), mthdslt.getText().toString(),
                       Integer.parseInt(numslt.getText().toString()), new Date(System.currentTimeMillis()),"","","");

                sendexepub(exepub);


            } else {
                Toast.makeText(PublishExe.this, "请完善基本信息", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void sendexepub(Exercises exepub){
        String str = "http://10.40.5.13:8080/moving/pubexe";
        RequestParams params = new RequestParams(str);
        Gson gson = new Gson();
        String exe = gson.toJson(exepub);
        params.addQueryStringParameter("exe",exe);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                System.out.println("+_+_+_+__"+result);
                if ("true".equals(result)){
                    Toast.makeText(PublishExe.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(PublishExe.this, ManagerexeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(PublishExe.this, "发布失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PublishExe.this, "网络错误", Toast.LENGTH_SHORT).show();
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