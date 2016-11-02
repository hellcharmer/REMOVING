package com.example.charmer.moving.home_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class zixunInfo_xiangqing extends AppCompatActivity implements View.OnClickListener {

    private TextView home_xiangxi_title;
    private ImageView iv_home_return;
    private ImageView xiangxi_author_touxiang;
    private TextView xiangxi_author;
    private RelativeLayout home_xiangxi_header;
    private RelativeLayout rl_home_title;
    private TextView home_xiangxi_content;
    private GridView home_xiangxi_picture;
    private Button home_xiangxi_dianzan;
    private RelativeLayout btn_container_dianzan;
    private Button home_xiangxi_shoucang;
    private RelativeLayout btn_container_shoucang;
    private Button home_xiangxi_pinglun;
    private RelativeLayout btn_container_pinglun;
    private LinearLayout home_xiangxi_bottom;
    private List<ListActivityBean.Zixun> zixunById = new ArrayList<ListActivityBean.Zixun>();
    private String[] imgs;
    private MyAdapter gridview_adapter;
    private ScrollView sl_home_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun_info_xiangqing);
        initView();
        initData();
    }


    private void initView() {
        home_xiangxi_title = ((TextView) findViewById(R.id.home_xiangxi_title));
        iv_home_return = (ImageView) findViewById(R.id.iv_home_return);

        xiangxi_author_touxiang = (ImageView) findViewById(R.id.xiangxi_author_touxiang);

        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);

        home_xiangxi_header = (RelativeLayout) findViewById(R.id.home_xiangxi_header);

        rl_home_title = (RelativeLayout) findViewById(R.id.rl_home_title);

        home_xiangxi_content = (TextView) findViewById(R.id.home_xiangxi_content);

        home_xiangxi_picture = (GridView) findViewById(R.id.home_xiangxi_picture);

       //home_xiangxi_dianzan = (Button) findViewById(R.id.home_xiangxi_dianzan);

        btn_container_dianzan = (RelativeLayout) findViewById(R.id.btn_container_dianzan);

        home_xiangxi_shoucang = (Button) findViewById(R.id.home_xiangxi_shoucang);

        btn_container_shoucang = (RelativeLayout) findViewById(R.id.btn_container_shoucang);

        home_xiangxi_pinglun = (Button) findViewById(R.id.home_xiangxi_pinglun);

        btn_container_pinglun = (RelativeLayout) findViewById(R.id.btn_container_pinglun);

        home_xiangxi_bottom = (LinearLayout) findViewById(R.id.home_xiangxi_bottom);

        sl_home_picture = (ScrollView) findViewById(R.id.sl_home_picture);

    }

    private void initData() {
        Intent intent = this.getIntent();
        String zixunId = intent.getStringExtra("zixunId");


        getZixunlistById(zixunId);
    }


    private void getZixunlistById(String zixunId) {

        RequestParams params = new RequestParams(HttpUtils.hoster + "querybyId");
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
                    imgs = URLDecoder.decode(zixunById.get(0).photoImg, "utf-8").split(",");

                    home_xiangxi_title.setText(URLDecoder.decode(zixunById.get(0).title, "utf-8"));
                    home_xiangxi_content.setText(URLDecoder.decode(zixunById.get(0).content, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (imgs.length > 0) {
                    //为GridView设置适配器
                    gridview_adapter = new MyAdapter(zixunInfo_xiangqing.this, imgs);
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
                convertView = View.inflate(zixunInfo_xiangqing.this, R.layout.home_xiangxi_picture_item, null);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //  System.out.println("=============="+imgs[0]);
            xUtilsImageUtils.display(viewHolder.image, HttpUtils.hoster + imgs[position]);
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView image;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.home_xiangxi_dianzan:
//
//                break;
            case R.id.home_xiangxi_shoucang:

                break;
            case R.id.home_xiangxi_pinglun:

                break;
        }
    }
}
