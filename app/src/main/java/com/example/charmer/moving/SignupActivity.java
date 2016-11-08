package com.example.charmer.moving;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.contantData.EditTextClearTools;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.LoginInfo;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    String APPKEY = "101732155b605";
    String APPSECRETE = "69d1850f4b74100266ab576b64e6cb16";
    ProgressDialog progressDialog;
    public Bitmap QRcode = null;
    int i = 30;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.del_name)
    ImageView _del_name;
    @InjectView(R.id.input_mobile)
    EditText _mobileText;
    @InjectView(R.id.del_mobile)
    ImageView _del_mobile;
    @InjectView(R.id.request_code_btn)
    Button _request_code_btn;
    @InjectView(R.id.input_verification)
    EditText _verification;
    @InjectView(R.id.del_verification)
    ImageView _del_verification;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.del_password)
    ImageView _del_password;
    @InjectView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @InjectView(R.id.del_reEnterPassword)
    ImageView _del_reEnterPassword;
    @InjectView(R.id.btn_signup)
    AppCompatButton _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        EditTextClearTools.addclerListener(_nameText, _del_name);
        EditTextClearTools.addclerListener(_mobileText, _del_mobile);
        EditTextClearTools.addclerListener(_verification, _del_verification);
        EditTextClearTools.addclerListener(_passwordText, _del_password);
        EditTextClearTools.addclerListener(_reEnterPasswordText, _del_reEnterPassword);
        // 启动短信验证sdk
        SMSSDK.initSDK(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        _request_code_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String phoneNums = _mobileText.getText().toString();
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                _request_code_btn.setClickable(false);
                _request_code_btn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
            }
        });
        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneNums = _mobileText.getText().toString();
                signup();
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNums, _verification
                        .getText().toString());
                //createProgressBar();

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

         progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在注册...");
        progressDialog.show();

         String name = _nameText.getText().toString();
        String verification = _verification.getText().toString();
         String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        QRcode= CodeUtils.createImage(mobile, 400, 400, null);
        Bitmap  bitmap = BitmapFactory.decodeResource(SignupActivity.this.getResources(), R.drawable.morentouxiang);
        saveBitmaptofile(QRcode,mobile);
        saveBitmaptofile(bitmap,"morentouxiang");
        File file=new File("/data/data/com.example.charmer.moving/QRcodepicture/"+mobile+".png");

        sendImg(file);
        LoginInfo loginInfo= new LoginInfo(name,password,mobile,mobile+".png");
        Signup(loginInfo);
      //  Signup(name,mobile,password);
//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String verification = _verification.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty()) {
            //  _nameText.setError("用户名不能为空");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (verification.isEmpty() || verification.length() != 4) {
            //  _verification.setError("请输入有效的验证码");
            valid = false;
        } else {
            _verification.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 11) {
            // _mobileText.setError("请输入有效的手机号");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            //  _passwordText.setError("请输入4-10个字母或数字");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            // _reEnterPasswordText.setError("密码不匹配");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                _request_code_btn.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                _request_code_btn.setText("获取验证码");
                _request_code_btn.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
    private void Signup(LoginInfo SignupInfo) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"loginservlet");
        Gson gson =new Gson();
        String info = gson.toJson(SignupInfo);
        params.addQueryStringParameter("info",info);
        params.addQueryStringParameter("state","1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if("0".equals(result)) {
                    Toast.makeText(SignupActivity.this,"此号码已被使用",Toast.LENGTH_SHORT).show();
                    _signupButton.setEnabled(true);
                    progressDialog.dismiss();
                }else if("1".equals(result)){
                    onSignupFailed();
                }else{
                    onSignupSuccess();
                    progressDialog.dismiss();
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
    static boolean saveBitmaptofile(Bitmap bmp,String mobile){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            File cacheDir = new File("/data/data/com.example.charmer.moving/QRcodepicture/");//设置目录参数
            if(cacheDir.exists()){

            }else{
                cacheDir.mkdirs();//新建目录
            }

            stream = new FileOutputStream("/data/data/com.example.charmer.moving/QRcodepicture/"+mobile+".png");
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }
    private void sendImg(File file) {
        RequestParams params = new RequestParams(HttpUtils.hoster + "qrcode");//upload 是你要访问的servlet
        Log.i("文件",""+file);
        params.addBodyParameter("file", file);
        params.addBodyParameter("fikongle","ss");


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("---------------===="+result);
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