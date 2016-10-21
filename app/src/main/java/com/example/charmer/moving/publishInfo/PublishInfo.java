package com.example.charmer.moving.publishInfo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.utils.MyAdapter;

import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PublishInfo extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<Bitmap> aList;
    MyAdapter adapter;
    Context context;
    private String picturePath;
    private Bitmap bmp;
    private ImageView iv_cancel;
    private ImageView iView;
    private ImageView iv_publishmore;
    private EditText et_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_info);
        initView();
        initEvent();
    }

    private void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == aList.size() - 1) {
                    new AlertDialog.Builder(context).setItems(new String[]{"画册添加", "拍照添加"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, 1);
                                    break;
                                case 1:
                                    Intent intent1 = new Intent();
                                    intent1.setAction("android.media.action.IMAGE_CAPTURE");
                                    intent1.addCategory("android.intent.category.DEFAULT");
                                    //TODO 创建照片存储路径，方便调用
                                    picturePath = "/mnt/sdcard/DCIM/pic" + DateFormat.format("kkmmss", new Date()).toString() + ".jpg";
                                    File file = new File(picturePath);
                                    Uri uri = Uri.fromFile(file);
                                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    startActivity(intent1);
                                    break;
                            }
                        }
                    }).create().show();

                } else {
                    iView = (ImageView) view.findViewById(R.id.iv);
                    //获取当前控件绑定图片的原始尺寸
                    int[] parm = (int[]) iView.getTag();
                    //当前绑定的图片
                    Bitmap bmp = aList.get(position);
                    final Dialog dialog = new Dialog(context);
                    View contentview = View.inflate(context, R.layout.activity_publish_img_dialog, null);
                    ImageView iv = (ImageView) contentview.findViewById(R.id.iv_dialog);
                    Button bt = (Button) contentview.findViewById(R.id.btn);
                    Bitmap bmBitmap = Bitmap.createScaledBitmap(bmp, parm[0], parm[1], true);
                    iv.setImageBitmap(bmBitmap);
                    dialog.setContentView(contentview);
                    bt.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });



                    //设置对话框尺寸
                    Window diaWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = diaWindow.getAttributes();
                    lp.width = getResources().getDisplayMetrics().widthPixels - 10;
                    lp.height = getResources().getDisplayMetrics().heightPixels - 100;
                    diaWindow.setAttributes(lp);
                    dialog.show();
                }
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(),"quxiao",Toast.LENGTH_LONG).show();
            }
        });

        iv_publishmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishInfo();
            }
        });
    }


    private void initView() {
        iv_cancel = ((ImageView) findViewById(R.id.iv_cancel));
        gridView = (GridView) findViewById(R.id.gridView1);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
        aList = new ArrayList<Bitmap>();
        aList.add(bmp);
        adapter = new MyAdapter(PublishInfo.this, aList);
        gridView.setAdapter(adapter);
        context = PublishInfo.this;
        iv_publishmore = ((ImageView) findViewById(R.id.iv_publishmore));
        et_text = ((EditText) findViewById(R.id.et_text));
    }

    //点击画囊后执行
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.i("info", filePathColumn[0] + "");
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(picturePath)) {
            Bitmap addbmp = BitmapFactory.decodeFile(picturePath);
            aList.add(addbmp);
            //先移除用来添加的图标，再添加以保证添加的图片始终在最后
            aList.remove(bmp);
            aList.add(bmp);
            adapter.setDate(aList);
            adapter.notifyDataSetChanged();
            //刷新后释放，防止手机休眠后自动添加
            picturePath = null;
        }
    }

    //发布动态
    private void publishInfo() {
        RequestParams requestparams = new RequestParams(HttpUtils.host_dynamic+"insertinfoservlet");

//        Info info = new Info(et_text.getText().toString(),new Timestamp(System.currentTimeMillis()),String.valueOf(((MyApplication)this.getApplication()).getUser().getUserid()));
    }

}

    //    //头像的存储完整路径
//    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+ getPhotoFileName());
//    private static final int PHOTO_REQUEST = 1;
//    private static final int CAMERA_REQUEST = 2;
//    private static final int PHOTO_CLIP = 3;
//    private ImageView iv_imageView;
//    private TextView tv_fromphone;
//    private TextView tv_fromtakephone;
//    private TextView tv_upload;
//    private ImageView iv_cancel;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_publish_info);
//        iv_cancel = ((ImageView) findViewById(R.id.iv_cancel));
//        iv_imageView = ((ImageView)findViewById(R.id.iv_imageView));
//
//        //popupwindow加载
//        iv_imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(v);
//            }
//        });
//
//        //取消 不发布
//        iv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                  PublishInfo.this.finish();
//            }
//        });
//
//    }
//
//    private void initdata() {
//
//        //从手机里找图片
//        tv_fromphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPicFromPhoto();
//            }
//        });
//
//        //手机拍照拿图片
//        tv_fromtakephone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPicFromCamera();
//            }
//        });
//
//        //上传照片
//        tv_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendImg();
//            }
//        });
//    }
//
//    private void showPopupWindow(View view) {
//        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_publish_photo, null);
//        tv_fromphone = ((TextView)contentView.findViewById(R.id.tv_fromphone));
//        tv_fromtakephone = ((TextView)contentView.findViewById(R.id.tv_fromtakephone));
//        tv_upload = ((TextView)contentView.findViewById(R.id.tv_upload));
//        initdata();
//        final PopupWindow popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchable(true);
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//
//        // 设置好参数之后再show
//        //popupWindow.showAsDropDown(view,100,50);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.common_bottom_bar_normal_bg));
//        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
//    }
//
//
//    private void sendImg() {
//        RequestParams params = new RequestParams(HttpUtils.host_dynamic);//upload 是你要访问的servlet
//        params.addBodyParameter("fileName","fileName");
//        params.addBodyParameter("file",file);
//
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
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
//    }
//
//
//    private void getPicFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 下面这句指定调用相机拍照后的照片存储的路径
//        System.out.println("getPicFromCamera==========="+file.getAbsolutePath());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent,CAMERA_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case CAMERA_REQUEST:
//                switch (resultCode) {
//                    case -1://-1表示拍照成功  固定
//                        System.out.println("CAMERA_REQUEST"+file.getAbsolutePath());
//                        if (file.exists()) {
//                            photoClip(Uri.fromFile(file));
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                break;
//            case PHOTO_REQUEST:
//                if (data != null) {
//                    photoClip(data.getData());
//
//                }
//                break;
//            case PHOTO_CLIP:
//                if (data != null) {
//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Log.w("test", "data");
//                        Bitmap photo = extras.getParcelable("data");
//                        saveImageToGallery(getApplication(),photo);//保存bitmap到本地
//                        iv_imageView.setImageBitmap(photo);
//
//
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    private void photoClip(Uri uri) {
//        // 调用系统中自带的图片剪裁
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PHOTO_CLIP);
//    }
//
//    public void saveImageToGallery(Context context, Bitmap bmp) {
//        // 首先保存图片
////        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
////        if (!appDir.exists()) {
////            appDir.mkdir();
////        }
////        String fileName = System.currentTimeMillis() + ".jpg";
////        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
//    }
//
//    private void getPicFromPhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                "image/*");
//        startActivityForResult(intent, PHOTO_REQUEST);
//    }
//    // 使用系统当前日期加以调整作为照片的名称
//    private String getPhotoFileName() {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        System.out.println("============"+ UUID.randomUUID());
//        return sdf.format(date)+"_"+UUID.randomUUID() + ".png";
//    }




