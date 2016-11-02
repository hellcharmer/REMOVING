package com.example.charmer.moving;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.contantData.EditTextClearTools;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.LoginInfo;
import com.example.charmer.moving.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String mobile;
     ProgressDialog progressDialog;
    @InjectView(R.id.input_mobile)
    EditText _mobileText;
    @InjectView(R.id.del_phonenumber)
    ImageView _del_phonenumber;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.del_password)
    ImageView _del_password;
    @InjectView(R.id.btn_login)
    AppCompatButton _loginButton;
    @InjectView(R.id.link_forgetpsd)
    TextView _forgetpsdLink;
    @InjectView(R.id.link_signup)
    TextView _signupLink;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        EditTextClearTools.addclerListener(_mobileText, _del_phonenumber);
        EditTextClearTools.addclerListener(_passwordText, _del_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _forgetpsdLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPsdActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

         progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在登录...");
        progressDialog.show();

         mobile = _mobileText.getText().toString();

        String password = _passwordText.getText().toString();
        LoginInfo loginInfo= new LoginInfo(mobile,password);
        Login(loginInfo);
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("mobile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("number", mobile);
        editor.commit();
//        new Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String str1,String str2) {

        System.out.println("____________"+str1+"+"+str2);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("userId",str1);
        intent.putExtra("useraccount",str2);
        startActivity(intent);
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

        if (mobile.isEmpty() || mobile.length() != 11) {
             _mobileText.setError("请输入有效的手机号");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
             _passwordText.setError("请输入4-10个字母或数字");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private void Login(LoginInfo loginInfo) {

        RequestParams params = new RequestParams(HttpUtils.hoster+"loginservlet");
        Gson gson =new Gson();
        String login = gson.toJson(loginInfo);
        params.addQueryStringParameter("info",login);
        params.addQueryStringParameter("state","2");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type_map = new TypeToken<Map<String,String>>(){}.getType();
                Map<String,String> map1= gson.fromJson(result, type_map);
                System.out.println("========="+map1.get("result"));
                if("1".equals(map1.get("result"))){

                    onLoginSuccess(map1.get("userId"),map1.get("useraccount"));
                    progressDialog.dismiss();
                }else {
                     onLoginFailed();
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


}
