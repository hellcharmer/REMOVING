package com.example.charmer.moving.friendchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Creatqun2 extends AppCompatActivity implements View.OnClickListener{
    private  String userids;
    private EditText edqun;
    private  String edqunname;
    private ImageView jiatu;
    private static final int PHOTO_REQUEST = 1;
    private static final int PHOTO_CLIP = 3;
    private TextView tv_cancelqun;
    private TextView tv_createqun;
    private List<File> imgs=new ArrayList<File>();
    //头像的存储完整路径
    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+
            getPhotoFileName());
    private Button creatqun2;
     private Integer i=0;
    private ImageView success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatqun2);
        Intent intent=getIntent();
        userids=intent.getStringExtra("ids");
        Log.i("ids",userids);
        edqun = ((EditText) findViewById(R.id.edqun));
        jiatu = ((ImageView) findViewById(R.id.jiatu));
        edqun.addTextChangedListener(textWatcher);
        creatqun2 = ((Button) findViewById(R.id.tv_createqun2));
        creatqun2.setOnClickListener(this);
        jiatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, PHOTO_REQUEST);
            }
        });
        tv_cancelqun = ((TextView) findViewById(R.id.tv_cancelqun));
        tv_cancelqun.setOnClickListener(this);
        tv_createqun = ((TextView) findViewById(R.id.tv_createqun1));
        tv_createqun.setOnClickListener(this);
        success = ((ImageView) findViewById(R.id.success));
    }
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            edqunname=edqun.getText().toString();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Log.w("test", "data");
                        Bitmap photo = extras.getParcelable("data");
                        saveImageToGallery(getApplication(),photo);
                        jiatu.setImageBitmap(photo);
                        i++;
                    }
                }
                break;
            default:
                break;
        }
    }
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancelqun:
                finish();
                break;
         case R.id.tv_createqun1:
                if (i==0||edqunname.isEmpty()){
                    Toast.makeText(Creatqun2.this,"请补全信息",Toast.LENGTH_SHORT).show();
                }else{
                 success.setVisibility(View.VISIBLE);
                    creatqun2.setEnabled(true);
                }
                break;
            case R.id.tv_createqun2:
                imgs.add(file);
                RequestParams params = new RequestParams(HttpUtils.host4+"qunservlet");//upload 是你要访问的servlet
                for(int i=0;i<imgs.size();i++){
                    params.addBodyParameter("file",imgs.get(i));
                }
                params.addQueryStringParameter("userids",userids);
                params.addQueryStringParameter("qunname",edqunname);
                params.addBodyParameter("kong","kong");
                params.addBodyParameter("choice",0+"");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("ssssss",result);
                        Toast.makeText(Creatqun2.this,"",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("onError",ex+"");
                    }
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }
                    @Override
                    public void onFinished() {
                        finish();
                    }
                });
                break;
        }
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        System.out.println("============"+UUID.randomUUID());
        return sdf.format(date)+"_"+UUID.randomUUID() + ".png";
    }
    public void saveImageToGallery(Context context, Bitmap bmp) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

}
