package com.example.charmer.moving.mine_activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.charmer.moving.LoginActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.VariableExercise;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Personal_information extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout finishthis;
    private RelativeLayout person_rl_myphoto;
    private ImageView iv_myphoto;
    View view;
    private TextView take_photo;
    private TextView choose_picture;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LayoutInflater inflater;
    //头像的存储完整路径
    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +
            getPhotoFileName());

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    private RelativeLayout person_rl_username;
    private TextView tv_title;
    private EditText ed_updatainfo;
    private TextView iv_myusername;
    String useraccount;
    String userId;
    String userimg;
    String username;
    String sex;
    String myqrcode;
    String signature;
    SharedPreferences sharedPreferences;
    private RelativeLayout person_rl_myqrcode;
    private ImageView dilog_myqrcode;
    private RelativeLayout person_rl_sex;
    private TextView iv_mysex;
    private RelativeLayout person_rl_signature;
    private RelativeLayout person_rl_loginout;
    private TextView iv_mysignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        sharedPreferences = Personal_information.this.getSharedPreferences("sp_mobile", Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(
                Personal_information.this);
        inflater = LayoutInflater.from(this);
        initView();
        initData();
    }

    private void initData() {

        userId = sharedPreferences.getString("userId", "");
        useraccount = sharedPreferences.getString("useraccount", "");
        userimg = sharedPreferences.getString("userimg", "");
        username = sharedPreferences.getString("username", "");
        sex = sharedPreferences.getString("sex", "");
        myqrcode = sharedPreferences.getString("myqrcode", "");
        signature = sharedPreferences.getString("signature", "");
        iv_myusername.setText(username);
        iv_mysex.setText(sex);
        iv_mysignature.setText(signature);
    }

    private void initView() {
        finishthis = (RelativeLayout) findViewById(R.id.finishthis);
        finishthis.setOnClickListener(this);
        person_rl_myphoto = (RelativeLayout) findViewById(R.id.person_rl_myphoto);
        person_rl_myphoto.setOnClickListener(this);
        iv_myphoto = (ImageView) findViewById(R.id.iv_myphoto);

        person_rl_username = (RelativeLayout) findViewById(R.id.person_rl_username);
        person_rl_username.setOnClickListener(this);

        iv_myusername = (TextView) findViewById(R.id.iv_myusername);

        person_rl_myqrcode = (RelativeLayout) findViewById(R.id.person_rl_myqrcode);
        person_rl_myqrcode.setOnClickListener(this);

        person_rl_sex = (RelativeLayout) findViewById(R.id.person_rl_sex);
        person_rl_sex.setOnClickListener(this);
        iv_mysex = (TextView) findViewById(R.id.iv_mysex);

        person_rl_signature = (RelativeLayout) findViewById(R.id.person_rl_signature);
        person_rl_signature.setOnClickListener(this);
        person_rl_loginout = (RelativeLayout) findViewById(R.id.person_rl_loginout);
        person_rl_loginout.setOnClickListener(this);
        iv_mysignature = (TextView) findViewById(R.id.iv_mysignature);

    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        System.out.println("============" + UUID.randomUUID());
        return sdf.format(date) + "_" + UUID.randomUUID() + ".png";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.person_rl_myphoto:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        Personal_information.this);
                view = inflater.inflate(R.layout.upload_photo, null);
                take_photo = (TextView) view.findViewById(R.id.take_photo);
                take_photo.setOnClickListener(this);
                choose_picture = (TextView) view.findViewById(R.id.choose_picture);
                choose_picture.setOnClickListener(this);
                builder1.setView(view);
                dialog = builder1.create();

                dialog.show();

                break;
            case R.id.take_photo:
                getPicFromCamera();
                dialog.dismiss();
//                Toast.makeText(this,"1111",Toast.LENGTH_SHORT).show();
                break;
            case R.id.choose_picture:
//                Toast.makeText(this,"2222",Toast.LENGTH_SHORT).show();
                getPicFromPhoto();
                dialog.dismiss();
                break;
            case R.id.person_rl_username:

                view = inflater.inflate(R.layout.dilog_personalupdate, null);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText("修改用户名");

                ed_updatainfo = (EditText) view.findViewById(R.id.ed_updatainfo);
                ed_updatainfo.setText(iv_myusername.getText().toString());
                builder.setView(view);
                builder.setPositiveButton(
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int which) {
                                // TODO Auto-generated method

                                submit("username");

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
                break;
            case R.id.person_rl_myqrcode:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(
                        Personal_information.this);
                view = inflater.inflate(R.layout.myqrcode, null);
                dilog_myqrcode = (ImageView) view.findViewById(R.id.dilog_myqrcode);
                File cacheDir = new File("/data/data/com.example.charmer.moving/QRcodepicture/"+myqrcode);
                if (cacheDir.exists()) {
                    String qrcode = "/data/data/com.example.charmer.moving/QRcodepicture/" + myqrcode;
                    Bitmap bm = BitmapFactory.decodeFile(qrcode);
                    dilog_myqrcode.setImageBitmap(bm);
                } else {
                    //从网络上拿数据
                    getInfo();
                }
                builder2.setView(view);
                builder2.create().show();
                break;
            case R.id.person_rl_sex:

                view = inflater.inflate(R.layout.dilog_personalupdate, null);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText("修改性别" );

                ed_updatainfo = (EditText) view.findViewById(R.id.ed_updatainfo);
                ed_updatainfo.setText(iv_mysex.getText().toString());
                builder.setView(view);
                builder.setPositiveButton(
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int which) {
                                // TODO Auto-generated method

                                submit("usersex");

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
                break;
            case R.id.person_rl_signature:
                view = inflater.inflate(R.layout.dilog_personalupdate, null);
                tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText("修改个性签名");

                ed_updatainfo = (EditText) view.findViewById(R.id.ed_updatainfo);
                ed_updatainfo.setText(iv_mysignature.getText().toString());
                builder.setView(view);
                builder.setPositiveButton(
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface,
                                    int which) {
                                // TODO Auto-generated method

                                submit("signature");

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
                break;
            case R.id.person_rl_loginout:
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("userId", "");
                editor.putString("useraccount", "");
                editor.putString("userimg", "");
                editor.putString("username", "");
                editor.putString("sex", "");
                editor.putString("myqrcode", "");
                editor.putString("signature", "");
                editor.commit();
                Intent intent = new Intent(Personal_information.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.finishthis:
               finish();
                break;
        }
    }



    private void sendImg() {
        RequestParams params = new RequestParams(HttpUtils.hoster + "upload");//upload 是你要访问的servlet
        params.addBodyParameter("fileName", "fileName");
        params.addBodyParameter("file", file);
        params.addQueryStringParameter("choice","1");
        params.addQueryStringParameter("user",useraccount);
//        params.addBodyParameter("file",file1);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("userimg",result.split(",")[0]);
                editor.commit();
                updateuserinfo("userimg",result.split(",")[0]);
                Toast.makeText(Personal_information.this, "修改成功", Toast.LENGTH_SHORT).show();
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


    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        System.out.println("getPicFromCamera===========" + file.getAbsolutePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功  固定
                        System.out.println("CAMERA_REQUEST" + file.getAbsolutePath());
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
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
                        saveImageToGallery(getApplication(), photo);//保存bitmap到本地
                        sendImg();
                        iv_myphoto.setImageBitmap(photo);


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

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
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

    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void submit(String temp) {
        // validate
        String updateinfo = ed_updatainfo.getText().toString().trim();
        if (TextUtils.isEmpty(updateinfo)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if ("username".equals(temp)) {
                updateuserinfo("username",updateinfo);
            } else if ("usersex".equals(temp)) {
                updateuserinfo("usersex",updateinfo);
            } else if ("signature".equals(temp)) {
                updateuserinfo("signature",updateinfo);
            }


        }

        // TODO validate success, do something


    }
    private void getInfo() {
        RequestParams params = new RequestParams(HttpUtils.hoster + "getpersonalinfo");//upload 是你要访问的servlet
        params.addQueryStringParameter("user",useraccount);
        params.addQueryStringParameter("state","5");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                final VariableExercise.DataSummary dataSummary=gson.fromJson(result,VariableExercise.DataSummary.class);
                //网络图片下载到本地

                Glide.with(Personal_information.this).load(HttpUtils.hoster+"qrcode/"+dataSummary.QRcode).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        dilog_myqrcode.setImageBitmap(resource);
                        saveBitmaptofile_Qrcode(resource,dataSummary.QRcode);
                    }
                }); //方法中设置asBitmap可以设置回调类型
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
    static boolean saveBitmaptofile_Qrcode(Bitmap bmp,String mobile){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            File cacheDir = new File("/data/data/com.example.charmer.moving/QRcodepicture/");//设置目录参数
            if(cacheDir.exists()){

            }else{
                cacheDir.mkdirs();//新建目录
            }

            stream = new FileOutputStream("/data/data/com.example.charmer.moving/QRcodepicture/"+mobile);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }
    private void updateuserinfo(final String temp,final String content){
        RequestParams params = new RequestParams(HttpUtils.hoster + "updateuserinfo");//upload 是你要访问的servlet
        params.addQueryStringParameter("user",useraccount);
        params.addQueryStringParameter("state",temp);
        params.addQueryStringParameter("content",content);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if("true".equals(result)){
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

                    if ("username".equals(temp)) {
                        iv_myusername.setText(content);
                        editor.putString("username", content);
                    } else if ("usersex".equals(temp)) {
                        iv_mysex.setText(content);
                        editor.putString("sex", content);
                    } else if ("signature".equals(temp)) {
                        iv_mysignature.setText(content);
                        editor.putString("signature", content);
                    }
                    editor.commit();
                }else {
                    Toast.makeText(Personal_information.this,"修改失败",Toast.LENGTH_SHORT).show();
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
