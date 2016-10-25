package com.example.charmer.moving.relevantexercise;

import android.content.Context;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Charmer on 2016/10/25.
 */

public class ExeSharedMthd {

    private  static int reAction= 0;

    public static void tryToEnroll(String exerciseId,String joiner,Context contexts){
        final Context context = contexts;
        String str = "http://10.40.5.13:8080/moving/enrollexe";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("0".equals(result)){
                    Toast.makeText(context,"您已报名！请勿重复报名",Toast.LENGTH_SHORT).show();
                }else
                if ("1".equals(result)){
                    Toast.makeText(context,"报名成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"报名失败！",Toast.LENGTH_SHORT).show();
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

    public static int cancelEnroll(String exerciseId,String joiner,Context contexts){
        reAction = 0;
        final Context context = contexts;
        String str = "http://10.40.5.13:8080/moving/cancelany";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("choice","1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                Toast.makeText(context,"报名取消成功！",Toast.LENGTH_SHORT).show();
                    reAction = 1;
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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
        return reAction;
    }


    public static int cancelJoin(String exerciseId,String joiner,Context contexts){
        final Context context = contexts;
        reAction = 0;
        String str = "http://10.40.5.13:8080/moving/cancelany";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("choice","2");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                    Toast.makeText(context,"取消参加成功！",Toast.LENGTH_SHORT).show();
                    reAction = 1;
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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
        return reAction;
    }

    public static int cancelExe(String exerciseId,Context contexts){
        final Context context = contexts;
        reAction = 0;
        String str = "http://10.40.5.13:8080/moving/cancelany";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("choice","3");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("true".equals(result)){
                    Toast.makeText(context,"取消活动成功！",Toast.LENGTH_SHORT).show();
                    reAction = 1;
                }else {
                    Toast.makeText(context,"取消失败！",Toast.LENGTH_SHORT).show();
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
        return reAction;
    }
}
