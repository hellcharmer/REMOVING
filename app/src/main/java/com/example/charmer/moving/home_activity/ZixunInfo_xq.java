package com.example.charmer.moving.home_activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.MyView.GridView_picture;
import com.example.charmer.moving.MyView.ObservableScrollView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.Constant;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class ZixunInfo_xq extends AppCompatActivity implements View.OnClickListener {

    private TextView home_xiangxi_title;
    private ImageView iv_home_return;
    private ImageView xiangxi_author_touxiang;
    private TextView xiangxi_author;
    private RelativeLayout home_xiangxi_header;
    private RelativeLayout rl_home_title;
    private TextView home_xiangxi_content;
    private TextView xiangxi_hide_title;
    private GridView_picture home_xiangxi_picture;
    private Button home_xiangxi_dianzan;
    private RelativeLayout btn_container_dianzan;
    private Button home_xiangxi_shoucang;
    private RelativeLayout btn_container_shoucang;
    private Button home_xiangxi_pinglun;
    private RelativeLayout btn_container_pinglun;
    private RelativeLayout rl_home_xiangxi_title;
    private LinearLayout home_xiangxi_bottom;
    private List<ListActivityBean.Zixun> zixunById = new ArrayList<ListActivityBean.Zixun>();
    private String[] imgs=new String[9];
    private List<String> imgs_list=new ArrayList<String>();
    private MyAdapter gridview_adapter;
    private static final int REQUEST_PREVIEW_CODE = 22;
    private ObservableScrollView sl_home_picture;
    private ObjectAnimator home_xiangxi_bottomAnimator;
    private ObjectAnimator rl_home_xiangxi_titleAnimator;
    private boolean isRunning = false;
    private int count=0;
    String zixunId="";
    Drawable startDra;
    SharedPreferences sharedPreferences;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                     count = (Integer)msg.obj;
                    if(count%2==0){
                        startDra = getResources().getDrawable(R.drawable.shoucang);
                        startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                        home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
                    }else {
                        startDra = getResources().getDrawable(R.drawable.shoucang_select);
                        startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                        home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
                    }
                    break;
                case 1:


                    handler.sendEmptyMessageDelayed(1,1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun_info_xiangqing);
        sharedPreferences = ZixunInfo_xq.this.getSharedPreferences("shoucang", Context.MODE_PRIVATE);
        initView();
        initData();
        bindEvents();
    }


    private void initView() {
        home_xiangxi_title = ((TextView) findViewById(R.id.home_xiangxi_title));
        iv_home_return = (ImageView) findViewById(R.id.iv_home_return);

        xiangxi_author_touxiang = (ImageView) findViewById(R.id.xiangxi_author_touxiang);

        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);

        home_xiangxi_header = (RelativeLayout) findViewById(R.id.home_xiangxi_header);

        rl_home_title = (RelativeLayout) findViewById(R.id.rl_home_title);

        home_xiangxi_content = (TextView) findViewById(R.id.home_xiangxi_content);
        xiangxi_hide_title = (TextView) findViewById(R.id.xiangxi_hide_title);
        home_xiangxi_picture = (GridView_picture) findViewById(R.id.home_xiangxi_picture);



        btn_container_dianzan = (RelativeLayout) findViewById(R.id.btn_container_dianzan);

        home_xiangxi_shoucang = (Button) findViewById(R.id.home_xiangxi_shoucang);
        home_xiangxi_shoucang.setOnClickListener(this);
        btn_container_shoucang = (RelativeLayout) findViewById(R.id.btn_container_shoucang);

        home_xiangxi_pinglun = (Button) findViewById(R.id.home_xiangxi_pinglun);
        home_xiangxi_pinglun.setOnClickListener(this);
        btn_container_pinglun = (RelativeLayout) findViewById(R.id.btn_container_pinglun);
        rl_home_xiangxi_title = (RelativeLayout) findViewById(R.id.rl_home_xiangxi_title);
        home_xiangxi_bottom = (LinearLayout) findViewById(R.id.home_xiangxi_bottom);

        sl_home_picture = (ObservableScrollView) findViewById(R.id.sl_home_picture);

    }

    private void initData() {
        Intent intent = this.getIntent();
         zixunId = intent.getStringExtra("zixunId");
        getZixunlistById(zixunId);

        String shoucangid =sharedPreferences.getString("shoucangid", "");

        if(shoucangid.equals("")){
           getshoucangstate(((MyApplication)ZixunInfo_xq.this.getApplication()).getUser().getUseraccount());
        }else {
            String[] shoucang = shoucangid.trim().split(",");
            for (int i = 0; i < shoucang.length; i++) {
                if (zixunId.equals(shoucang[i])) {
                    count = 1;
                    break;
                }
            }

            if (count % 2 == 0) {
                startDra = getResources().getDrawable(R.drawable.shoucang);
                startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
            } else {
                startDra = getResources().getDrawable(R.drawable.shoucang_select);
                startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
            }
        }

    }



    private void bindEvents(){
        // preview
        home_xiangxi_picture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(ZixunInfo_xq.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths((ArrayList<String>)imgs_list);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });

        sl_home_picture.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;
            private int direction;//0表示向上，1表示向下
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndY = event.getY();
                        float v1 = mEndY - mStartY;

                        if (v1 > 3 && !isRunning&& direction == 1) {
                            direction = 0;
                            showBar();
                            rl_home_xiangxi_title.setVisibility(View.VISIBLE);
                            mStartY = mEndY;
                            return false;
                        } else if (v1 < -3 && !isRunning && direction == 0) {
                            direction = 1;
                            hideBar();

                            mStartY = mEndY;
                            return false;
                        }
                        mStartY = mEndY;

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        sl_home_picture.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if(y<=48&&!isRunning){
                    hidetitleBar();
                }
            }
        });
        iv_home_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    public void hideBar() {
        home_xiangxi_bottomAnimator = ObjectAnimator.ofFloat(home_xiangxi_bottom, "translationY", Constant.displayHeight);
        rl_home_xiangxi_titleAnimator = ObjectAnimator.ofFloat(rl_home_xiangxi_title, "translationY", -rl_home_xiangxi_title.getHeight());
        home_xiangxi_bottomAnimator.setDuration(300).start();
        rl_home_xiangxi_titleAnimator.setDuration(300).start();
        home_xiangxi_bottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
            }
        });
        rl_home_xiangxi_titleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
            }
        });
    }
    public void hidetitleBar() {

        rl_home_xiangxi_titleAnimator = ObjectAnimator.ofFloat(rl_home_xiangxi_title, "translationY", -rl_home_xiangxi_title.getHeight());

        rl_home_xiangxi_titleAnimator.setDuration(100).start();

        rl_home_xiangxi_titleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
            }
        });
    }
    public void showBar() {
        home_xiangxi_bottomAnimator = ObjectAnimator.ofFloat(home_xiangxi_bottom, "translationY", 0);
        rl_home_xiangxi_titleAnimator=ObjectAnimator.ofFloat(rl_home_xiangxi_title, "translationY", 0);
        home_xiangxi_bottomAnimator.setDuration(300).start();
        rl_home_xiangxi_titleAnimator.setDuration(300).start();
    }

    private void getZixunlistById(String zixunId) {

        RequestParams params = new RequestParams(HttpUtils.host + "querybyId");
        params.addQueryStringParameter("zixunId", zixunId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                ListActivityBean bean = gson.fromJson(result, ListActivityBean.class);


                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");

                zixunById.clear();
                zixunById.addAll(bean.zixunlist);

                //通知listView更新界面
                //adapter1.notifyDataSetChanged();
                try {
                    xUtilsImageUtils.display(xiangxi_author_touxiang, HttpUtils.host+"userimg/"+ URLDecoder.decode(zixunById.get(0).publisherimg, "utf-8"),true);
                    imgs = URLDecoder.decode(zixunById.get(0).photoImg, "utf-8").split(",");
                    xiangxi_hide_title.setText(URLDecoder.decode(zixunById.get(0).title, "utf-8"));
                    home_xiangxi_title.setText(URLDecoder.decode(zixunById.get(0).title, "utf-8"));
                    home_xiangxi_content.setText(URLDecoder.decode(zixunById.get(0).content, "utf-8"));
                    home_xiangxi_pinglun.setText(zixunById.get(0).pingluns);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (imgs[0].equals("")) {
                    home_xiangxi_picture.setVisibility(View.GONE);

                }else {
                    for(int i=0;i<imgs.length;i++){
                        imgs_list.add("/storage/emulated/0/Pictures/"+imgs[i]);
                    }
                    //为GridView设置适配器

                    gridview_adapter = new MyAdapter(ZixunInfo_xq.this, imgs);
                    home_xiangxi_picture.setAdapter(gridview_adapter);

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
    private void getshoucangstate(String userId) {



        RequestParams params = new RequestParams(HttpUtils.host+"updatelikeservlet");
        params.addQueryStringParameter("zixunId",zixunId);
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if("true".equals(result)){
                    count=1;
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    msg.obj = count;
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void addshoucang(String userId){
        RequestParams params = new RequestParams(HttpUtils.host+"addzannum");
        params.addQueryStringParameter("zixunId",zixunId);
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void deleteshoucang(String userId){
        RequestParams params = new RequestParams(HttpUtils.host+"deletezannum");
        params.addQueryStringParameter("zixunId",zixunId);
        params.addQueryStringParameter("userId",userId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i(TAG,ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    //自定义适配器
    private class MyAdapter extends BaseAdapter {
        //上下文对象
        private Context context;
        private String[] imgs;
        //图片数组

        MyAdapter(Context context, String[] imgs) {
            this.context = context;
            this.imgs = imgs;
        }

        public int getCount() {
            return imgs.length;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ZixunInfo_xq.this, R.layout.home_xiangxi_picture_item, null);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //  System.out.println("=============="+imgs[0]);
            xUtilsImageUtils.display(viewHolder.image, HttpUtils.host +"upload/"+ imgs[position]);
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView image;
    }


    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home_xiangxi_shoucang:
                String shoucangid =sharedPreferences.getString("shoucangid", "");
                if(count%2!=0){
                    String [] shoucang =shoucangid.split(",");
                    String shoucangid_new="";
                    for (String string :shoucang){
                        if(!string.equals(zixunId)){
                            shoucangid_new+=string+",";
                        }
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("shoucangid",shoucangid_new);
                    editor.commit();//提交修改

                    startDra = getResources().getDrawable(R.drawable.shoucang);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
                    deleteshoucang(((MyApplication)ZixunInfo_xq.this.getApplication()).getUser().getUseraccount());
                }else {

                    shoucangid+=zixunId+",";
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("shoucangid",shoucangid);
                    editor.commit();//提交修改
                    startDra = getResources().getDrawable(R.drawable.shoucang_select);
                    startDra.setBounds(0, 0, startDra.getMinimumWidth(), startDra.getMinimumHeight());
                    home_xiangxi_shoucang.setCompoundDrawables(null, startDra, null, null);
                    addshoucang(((MyApplication)ZixunInfo_xq.this.getApplication()).getUser().getUseraccount());
                }
                count++;

                break;
            case R.id.home_xiangxi_pinglun:
                    Intent intent = new Intent(ZixunInfo_xq.this,Zixun_comment.class);
                    intent.putExtra("zixunId",zixunId);
                    startActivity(intent);
                break;
            default:

                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("页面销毁");
    }
}
