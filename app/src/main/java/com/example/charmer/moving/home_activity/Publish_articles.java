package com.example.charmer.moving.home_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.GridView_picture;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
//    //头像的存储完整路径
//    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" +
//            getPhotoFileName());


    private ImageView iv_publish_btn;

    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ArrayList<String> imagePaths = null;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private List<File> file = new ArrayList<File>();

    private GridView_picture gridView;
    private int columnWidth;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_articles);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
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


//        home_publish_at = (ImageButton) findViewById(R.id.home_publish_at);

        btn_container_at = (RelativeLayout) findViewById(R.id.btn_container_at);

        btn_container_picture = (RelativeLayout) findViewById(R.id.btn_container_picture);
        home_publish_at_picture = (ImageButton) findViewById(R.id.home_publish_at_picture);
        home_xiangxi_bottom = (LinearLayout) findViewById(R.id.home_xiangxi_bottom);

        activity_publish_articles = (RelativeLayout) findViewById(R.id.activity_publish_articles);

        tv_publish_photo = (TextView) findViewById(R.id.tv_publish_photo);

        tv_publish_album = (TextView) findViewById(R.id.tv_publish_album);
        iv_publish_btn = (ImageView) findViewById(R.id.iv_publish_btn);
        gridView = (GridView_picture) findViewById(R.id.gridView);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // Item Width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;

        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(Publish_articles.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });

        tv_publish_photo.setOnClickListener(this);
        tv_publish_album.setOnClickListener(this);

        iv_publish_btn.setOnClickListener(this);

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
        iv_home_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        System.out.println("============" + UUID.randomUUID());
        return sdf.format(date) + "_" + UUID.randomUUID() + ".jpg";
    }

//    private void fabuhuondong(String userId, String str) {
//
//        RequestParams params = new RequestParams(HttpUtils.hoster + "addzixun");
//            params.addQueryStringParameter("userId", userId);
//            params.addQueryStringParameter("title", publish_title.getText().toString());
////            params.addQueryStringParameter("picture", str);
//            params.addQueryStringParameter("content", publish_content.getText().toString());
//            x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//                if ("true".equals(result)) {
//                    Toast.makeText(Publish_articles.this, "发布成功", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(Publish_articles.this, "发布失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//
//    }

    private void sendImg() {
        RequestParams params = new RequestParams(HttpUtils.hoster + "upload");//upload 是你要访问的servlet

        params.addQueryStringParameter("userId", MainActivity.getUser().getUseraccount());
        params.addQueryStringParameter("title", publish_title.getText().toString());

        params.addQueryStringParameter("content", publish_content.getText().toString());
        for (int i = 0; i < file.size(); i++) {
            Log.i("文件", "" + file.get(i));
            params.addBodyParameter("file", file.get(i));
            params.addBodyParameter("fileName", "fileName");

        }
        params.addQueryStringParameter("choice","0");


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("============333333===="+result);
                if ("true".equals(result.split(",")[result.split(",").length])) {
                    Toast.makeText(Publish_articles.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Publish_articles.this, "发布失败", Toast.LENGTH_SHORT).show();
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


    private void getPicFromCamera() {
        try {
            if (captureManager == null) {
                captureManager = new ImageCaptureManager(Publish_articles.this);
            }
            Intent intent = captureManager.dispatchTakePictureIntent();

            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(Publish_articles.this, "相机无法启动", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    if (data != null) {

                    }
                    break;

            }
        }

    }


    private void getPicFromPhoto() {
        PhotoPickerIntent intent = new PhotoPickerIntent(Publish_articles.this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(9); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态

//                ImageConfig config = new ImageConfig();
//                config.minHeight = 400;
//                config.minWidth = 400;
//                config.mimeType = new String[]{"image/jpeg", "image/png"};
//                config.minSize = 1 * 1024 * 1024; // 1Mb
//                intent.setImageConfig(config);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
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
                Toast.makeText(Publish_articles.this, "正在发布...", Toast.LENGTH_SHORT).show();
                sendImg();
                break;

        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
        for (int i = 0; i < imagePaths.size(); i++) {
            //头像的存储完整路径
            file.add(i, new File(imagePaths.get(i)));
//            System.out.println(imagePaths.size());
////            /storage/emulated/0/Pictures/
            //         str = str +imagePaths.get(i).substring(29)+",";
//            System.out.println("str========="+str);
        }

        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            Glide.with(Publish_articles.this)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }
}
