package com.example.charmer.moving.Publishdynamic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Info;
import com.example.charmer.moving.pojo.User;
import com.google.gson.Gson;
import com.lidong.photopicker.ImageCaptureManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Publishdynamic extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 10;  //相机
    private static final int REQUEST_PREVIEW_CODE = 20;  //图片预览
    private ArrayList<String> imagePaths = new ArrayList<>();  //存放图片路径
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private GridView gridView;
    private GridAdapter gridAdapter;
    private ImageView mButton;
    private EditText et_infoContent;
    private List<File> imageFileLists=new ArrayList<File>();  //存放图片集合文件
    private ImageView iv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishdynamic);
        gridView = (GridView) findViewById(R.id.gridView);
        mButton = (ImageView) findViewById(R.id.button);
        et_infoContent= (EditText)findViewById(R.id.et_content);
        iv_cancel = ((ImageView) findViewById(R.id.iv_cancel));

        //设置gridview布局 一行几个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // preview 列表视图
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs) ){
                    PhotoPickerIntent intent = new PhotoPickerIntent(getApplicationContext());
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(9); // 最多选择照片数量，默认为9
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE); //相机拍照
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(getApplicationContext());
                    intent.setCurrentItem(position);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);  //选择图库
                }
            }
        });

        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        insertinfo();
                    }
                }.start();
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.i("publish", "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
//                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    //图片路径
    private void loadAdpater(ArrayList<String> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("000000")){
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        Log.i("publish","imagePaths"+imagePaths+"");
        for(int i = 0;i<imagePaths.size()-1;i++){
            imageFileLists.add(i,new File(imagePaths.get(i)));
        }
        if(gridAdapter == null){
            gridAdapter  = new GridAdapter(imagePaths);
            gridView.setAdapter(gridAdapter);}
        else {
            gridAdapter.notifyDataSetChanged();
        }
        try{
            JSONArray obj = new JSONArray(imagePaths);
            Log.i("publish","obj"+ obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //把图片加载进去
    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 10){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(getApplicationContext());
        }

        public int getCount(){
            return  listUrls.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final String path=listUrls.get(position);
            if (path.equals("000000")){
                holder.image.setImageResource(R.mipmap.ic_photo_upload);
            }else {
                Glide.with(getApplicationContext())
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        System.out.println("============" + UUID.randomUUID());
        Log.e("UUID:",UUID.randomUUID()+"");
        return sdf.format(date) + "_" + UUID.randomUUID() + ".jpg";
    }


    private void insertinfo(){

        String infoContent =et_infoContent.getText().toString().trim();
        Log.i("publish","infoContent"+infoContent+"");
        User user = new User(MyApplication.getUser().getUserid());
        Info info = new Info(infoContent,user);
        Gson gson = new Gson();
        String infoList = gson.toJson(info);
        Log.i("publish","infoList"+infoList);
        RequestParams requestparams = new RequestParams(HttpUtils.host_dynamic+"insertinfoservlet");


        for (int i=0;i<imageFileLists.size();i++){
            requestparams.addBodyParameter("file"+i,imageFileLists.get(i));
        }
//        Log.i("file","file"+imageFileLists.get(1));
        try {
            requestparams.addQueryStringParameter("infoList", URLEncoder.encode(infoList,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("publish","userId"+MyApplication.getUser().getUserid()+"");
        x.http().post(requestparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Toast.makeText(getApplicationContext(),"发布动态成功",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.e("error","error"+ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(getApplicationContext(),"网络异常",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void showdialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Publishdynamic.this);
        dialog.setTitle("动态");
        dialog.setMessage("是否放弃本次编辑");
        dialog.setCancelable(false);
        dialog.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
        dialog.show();
    }
}



