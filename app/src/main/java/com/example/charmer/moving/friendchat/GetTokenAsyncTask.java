package com.example.charmer.moving.friendchat;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/22.
 */
public class GetTokenAsyncTask extends AsyncTask<String,Integer,String> {
   private Button btn_friends;

    public GetTokenAsyncTask(Button btn_friends) {
        this.btn_friends = btn_friends;
    }

    private Integer j;
    private List<User> usersToken=new ArrayList<User>();//所有user的Token
    public  String Token;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected String doInBackground(String... params) {
       j= MyApplication.getUser().getUserid();
        Log.i("OOOOOOOOOO","asdasd"+j);
        RequestParams requestParams2=new RequestParams(HttpUtils.host4+"getalluserstoken");
        x.http().get(requestParams2, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<User>>(){}.getType();
                Log.i("TokenSuccess",result);
                List<User> newusersToken=gson.fromJson(result,type);
                usersToken.addAll(newusersToken);
                //赋予当前用户token
                for (int i=0;i<usersToken.size();i++){
                    if(j==usersToken.get(i).getUserid()){
                        Token=usersToken.get(i).getUsertoken();
                        Log.i("Tooooo3","kennn"+Token);
                        setToken(Token);

                    }
                }

                btn_friends.setBackgroundResource(R.drawable.friends);
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
        return Token;
    }

    public  String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }



    @Override
    protected void onCancelled() {
        super.onCancelled();

    }
}
