package com.example.charmer.moving.relevantexercise;

import android.content.Context;
import android.widget.Toast;

import com.example.charmer.moving.contantData.HttpUtils;

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
        String str = HttpUtils.hoster+"enrollexe";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("state","0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("0".equals(result)){
                    Toast.makeText(context,"您已报名！请勿重复报名",Toast.LENGTH_SHORT).show();
                }else
                if ("1".equals(result)){
                    Toast.makeText(context,"报名成功！",Toast.LENGTH_SHORT).show();
                }else
                if("3".equals(result)) {
                    Toast.makeText(context,"参加本活动的人数已满！",Toast.LENGTH_SHORT).show();
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

    public static int agreeJoin(String exerciseId,String joiner,Context contexts){
        reAction = 0;
        final Context context = contexts;
        String str = HttpUtils.hoster+"enrollexe";
        RequestParams params = new RequestParams(str);

        params.addQueryStringParameter("exerciseId",exerciseId);
        params.addQueryStringParameter("joiner",joiner);
        params.addQueryStringParameter("state","1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                if ("0".equals(result)){
                    Toast.makeText(context,"人数已满，操作失败！",Toast.LENGTH_SHORT).show();
                }else
                if ("1".equals(result)){
                    Toast.makeText(context,"操作成功！",Toast.LENGTH_SHORT).show();
                    reAction = 1;
                }else
                {
                    Toast.makeText(context,"操作失败！",Toast.LENGTH_SHORT).show();
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
        System.out.println("=====2-=--==222"+reAction);
            return reAction;
    }

    public static int cancelEnroll(String exerciseId,String joiner,Context contexts){
        reAction = 0;
        final Context context = contexts;
        String str = HttpUtils.hoster+"cancelany";
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
        String str = HttpUtils.hoster+"cancelany";
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
        String str = HttpUtils.hoster+"cancelany";
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
