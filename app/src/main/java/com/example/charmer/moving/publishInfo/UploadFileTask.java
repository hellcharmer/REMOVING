package com.example.charmer.moving.publishInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2016/10/21.
 */
public class UploadFileTask extends AsyncTask<String, Void, String> {
    public static final String requestURL = "http://10.40.5.33:8080/dynamic/fileimageupload";
    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    private ProgressDialog pdialog;
    private Activity context = null;

    public UploadFileTask(Activity ctx) {
        this.context = ctx;
        pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容
        pdialog.dismiss();
        if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... params) {
        File file = new File(params[0]);
        return UploadUtils.uploadFile(file, requestURL);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}