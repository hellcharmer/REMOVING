package com.example.charmer.moving.relevantexercise;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;
import com.example.charmer.moving.fragment.WheelDialogFragment;
import com.example.charmer.moving.pojo.Exercises;
import com.example.charmer.moving.utils.DateUtil;
import com.example.charmer.moving.utils.ResUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import butterknife.OnClick;

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
    private Button commitbtn;
    private Exercises exepub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_exe);

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
        commitbtn = (Button) findViewById(R.id.commitbtn);

        exe_pb_name = (EditText) findViewById(R.id.exe_pb_name);
        exepb_cost = (EditText) findViewById(R.id.exepb_cost);
        exepb_place = (EditText) findViewById(R.id.exepb_place);
        exepb_intro = (EditText) findViewById(R.id.exepb_intro);
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(exepb_intro.getWindowToken(), 0);
        commitbtn.setOnClickListener(new commitbtn());

        findViewById(R.id.rltv).setOnClickListener(new rltvclick());
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
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
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
                exepub = new Exercises(Long.parseLong(MyApplication.getUser().getUseraccount()), exe_pb_name.getText().toString(), typeslt.getText().toString(),
                       themeslt.getText().toString(), exepb_intro.getText().toString(), exepb_place.getText().toString(),
                       DateUtil.stringToDate(etStartTime.getText().toString()), Double.parseDouble(exepb_cost.getText().toString()), mthdslt.getText().toString(),
                       Integer.parseInt(numslt.getText().toString()), new Date(System.currentTimeMillis()), "FUJKONVNKK","","","");

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
//                    finish();
//                    Intent intent = new Intent(PublishExe.this, ManagerexeActivity.class);
//                    startActivity(intent);

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