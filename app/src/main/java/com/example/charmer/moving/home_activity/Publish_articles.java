package com.example.charmer.moving.home_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Date;
import java.util.UUID;

public class Publish_articles extends AppCompatActivity implements View.OnClickListener {
    private ImageButton home_publish_at_picture;
    private PopupWindow mPopupWindow;
    private ImageView iv_home_return;
    private TextView xiangxi_author;
    private RelativeLayout home_publish_header;
    private EditText publish_title;
    private EditText publish_content;
    private LinearLayout home_photo;
    private RelativeLayout rl_publish_photo;
    private RelativeLayout btn_container_dianzan;
    private ImageButton home_publish_at;
    private RelativeLayout btn_container_at;
    private RelativeLayout btn_container_picture;
    private LinearLayout home_xiangxi_bottom;
    private RelativeLayout activity_publish_articles;
    private boolean clicked = false;
    private TextView tv_publish_photo;
    private TextView tv_publish_album;
    private Animation scale_max, scale_min;
    //头像的存储完整路径
    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +
            getPhotoFileName());

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    private ImageView iv_publish_btn;
    private ImageView iv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_articles);

        initView();
        photo();
    }

    private void initView() {


        iv_home_return = (ImageView) findViewById(R.id.iv_home_return);
        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);
        home_publish_header = (RelativeLayout) findViewById(R.id.home_publish_header);
        publish_title = (EditText) findViewById(R.id.publish_title);

        publish_content = (EditText) findViewById(R.id.publish_content);

        home_photo = (LinearLayout) findViewById(R.id.home_photo);

        rl_publish_photo = (RelativeLayout) findViewById(R.id.rl_publish_photo);


        home_publish_at = (ImageButton) findViewById(R.id.home_publish_at);

        btn_container_at = (RelativeLayout) findViewById(R.id.btn_container_at);

        btn_container_picture = (RelativeLayout) findViewById(R.id.btn_container_picture);
        home_publish_at_picture = (ImageButton) findViewById(R.id.home_publish_at_picture);
        home_xiangxi_bottom = (LinearLayout) findViewById(R.id.home_xiangxi_bottom);

        activity_publish_articles = (RelativeLayout) findViewById(R.id.activity_publish_articles);

        tv_publish_photo = (TextView) findViewById(R.id.tv_publish_photo);

        tv_publish_album = (TextView) findViewById(R.id.tv_publish_album);
        iv_publish_btn = (ImageView) findViewById(R.id.iv_publish_btn);
        tv_publish_photo.setOnClickListener(this);
        tv_publish_album.setOnClickListener(this);

        iv_publish_btn.setOnClickListener(this);
        iv_test = (ImageView) findViewById(R.id.iv_test);
        iv_test.setOnClickListener(this);
    }

    private void photo() {
        scale_max = AnimationUtils.loadAnimation(this, R.anim.scale_max);
        scale_min = AnimationUtils.loadAnimation(this, R.anim.scale_min);
        home_publish_at_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clicked = !clicked;
                home_photo.setVisibility(clicked ? View.VISIBLE : View.GONE);
                rl_publish_photo.setVisibility(clicked ? View.VISIBLE : View.GONE);

                home_photo.startAnimation(clicked ? scale_max : scale_min);
                rl_publish_photo.setClickable(clicked);
            }
        });
        rl_publish_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clicked = !clicked;
                home_photo.setVisibility(clicked ? View.VISIBLE : View.GONE);
                home_photo.startAnimation(clicked ? scale_max : scale_min);
                rl_publish_photo.setVisibility(clicked ? View.VISIBLE : View.GONE);

                rl_publish_photo.setClickable(clicked);

            }
        });


    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        System.out.println("============" + UUID.randomUUID());
        return sdf.format(date) + "_" + UUID.randomUUID() + ".png";
    }

    private void sendImg() {
        RequestParams params = new RequestParams(HttpUtils.host + "upload");//upload 是你要访问的servlet

        params.addBodyParameter("fileName", "fileName");
        params.addBodyParameter("file", file);
//        params.addBodyParameter("file",file1);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
                        iv_test.setImageBitmap(photo);


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
        //首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_publish_photo:
                getPicFromCamera();
                clicked = !clicked;
                home_photo.setVisibility(View.GONE);

                rl_publish_photo.setVisibility(View.GONE);

                break;

            case R.id.tv_publish_album:
                getPicFromPhoto();
                clicked = !clicked;
                home_photo.setVisibility(View.GONE);

                rl_publish_photo.setVisibility(View.GONE);

                break;
            case R.id.iv_publish_btn:
                sendImg();
                break;

        }
    }
}
