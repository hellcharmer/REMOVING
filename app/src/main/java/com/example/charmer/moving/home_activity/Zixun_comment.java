package com.example.charmer.moving.home_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.utils.DateUtils;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import me.weyye.library.EmptyLayout;

public class Zixun_comment extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_comment_return;
    private TextView xiangxi_author;
    private RelativeLayout zixun_comment_header;
    private SwipeRefreshLayout sr_refresh;
    private LoadMoreListView lv_comment;
    private EmptyLayout emptyLayout;
    private ImageView iv_comment;
    private EditText edt_comment_name;
    private RelativeLayout activity_zixun_comment;
    int page_comment = 1;
    int totalpage_comment = 0;//总页数
    MyAdapter adapter;
    int iv_sortcount=0;
    String state ="0";
    String zixunId="";
    private List<ListActivityBean.Comments> commentList = new ArrayList<ListActivityBean.Comments>();
    private ImageView iv_comment_sort;
    private ToastUtil toastUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun_comment);
        View v=View.inflate(Zixun_comment.this,R.layout.layout_toast_view,null);
        toastUtil=new ToastUtil(Zixun_comment.this,v,200);
        initView();
        initData();
        bindEvents();
    }

    private void bindEvents() {
        sr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCommentslist(1,zixunId,state);

                        //调用该方法结束刷新，否则加载圈会一直在
                        sr_refresh.setRefreshing(false);

                    }
                },1000);
            }
        });
        lv_comment.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {

                loadCommentMore();
            }
        });
        if(edt_comment_name.getText().toString().equals("")){
            iv_comment.setImageResource(R.drawable.comment);
            iv_comment.setClickable(false);
        }else {
            iv_comment.setImageResource(R.drawable.comment_select);
            iv_comment.setClickable(true);
        }
    }

    private void initData() {
        Intent intent = this.getIntent();
        zixunId = intent.getStringExtra("zixunId");
        adapter = new MyAdapter(commentList);
        lv_comment.setAdapter(adapter);
        getCommentslist(page_comment,zixunId,state);
    }

    private void initView() {


        iv_comment_return = (ImageView) findViewById(R.id.iv_comment_return);
        iv_comment_return.setOnClickListener(this);
        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);

        zixun_comment_header = (RelativeLayout) findViewById(R.id.zixun_comment_header);

        sr_refresh = (SwipeRefreshLayout) findViewById(R.id.sr_refresh);

        lv_comment = (LoadMoreListView) findViewById(R.id.lv_comment);

        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        iv_comment.setOnClickListener(this);
        edt_comment_name = (EditText) findViewById(R.id.edt_comment_name);

        activity_zixun_comment = (RelativeLayout) findViewById(R.id.activity_zixun_comment);

        iv_comment_sort = (ImageView) findViewById(R.id.iv_comment_sort);
        iv_comment_sort.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_comment_sort:
                if(iv_sortcount%2!=0) {
                    iv_comment_sort.setImageResource(R.drawable.ic_ascending);
                    state="1";
                    getCommentslist(page_comment,zixunId,state);
                }else {
                    iv_comment_sort.setImageResource(R.drawable.ic_descending);
                    state="0";
                    getCommentslist(page_comment,zixunId,state);
                }
                iv_sortcount++;
                break;
        }
    }
    private void loadCommentMore() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(page_comment<totalpage_comment) {

                   getCommentslist(++page_comment,zixunId,state);
                }else {
                    toastUtil.Short(Zixun_comment.this,"已经到底了！").show();
                }

                Zixun_comment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lv_comment.setLoadCompleted();
                    }
                });
            }
        }.start();
    }
    private void getCommentslist(int page, String zixunId,String state) {

        RequestParams params = new RequestParams(HttpUtils.hoster + "getcomments");
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("zixunId", zixunId);
        params.addQueryStringParameter("state", state);
        params.addQueryStringParameter("choice", "0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ListActivityBean bean = gson.fromJson(result, ListActivityBean.class);
                totalpage_comment = bean.page;


                //System.out.println(bean.status);
                //System.out.println(bean.zixunlist.size());
                //Log.i(TAG,bean.status+"---------");
                //Log.i(TAG,bean.zixunlist.size()+"---------");
                if (page_comment == 1) {


                    commentList.clear();
                }
                //zixunlist.clear();
                commentList.addAll(bean.commentsList);
                // 成功
                emptyLayout.showSuccess();
                //通知listView更新界面
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                emptyLayout.showError("加载失败，点击重新加载"); // 显示失败
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private class MyAdapter extends BaseAdapter {
        private List<ListActivityBean.Comments> list;

        public MyAdapter(List<ListActivityBean.Comments> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            // System.out.println(zixunlist.size());
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            //打气筒
            ViewHolder viewHolder = null;

            final ListActivityBean.Comments comment = list.get(position);

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = View.inflate(Zixun_comment.this, R.layout.zixun_comment_list, null);
                viewHolder.iv_comment_photo = ((ImageView) convertView.findViewById(R.id.iv_comment_photo));
                viewHolder.tv_comment_name = ((TextView) convertView.findViewById(R.id.tv_comment_name));
                viewHolder.tv_comment_content = ((TextView) convertView.findViewById(R.id.tv_comment_content));
                viewHolder.tv_comment_time = ((TextView) convertView.findViewById(R.id.tv_comment_time));
                //字体加粗
                TextPaint tp = viewHolder.tv_comment_name.getPaint();
                tp.setFakeBoldText(true);


                convertView.setTag(viewHolder);//缓存对象
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            try {


                //Log.i("TAG",(URLDecoder.decode(zixun.timeStamp,"utf-8")));
                viewHolder.tv_comment_time.setText(DateUtils.getGapTimeFromNow(DateUtils.stringToDate(URLDecoder.decode(comment.commentTime, "utf-8"))));
                viewHolder.tv_comment_name.setText(comment.childDiscussantName);
                if (comment.fatherDiscussant == 0) {
                    viewHolder.tv_comment_content.setText(comment.childComment);
                } else {
                    viewHolder.tv_comment_content.setText("回复" + comment.fatherDiscussantName + "的评论：" + comment.childComment);
                }

                xUtilsImageUtils.display(viewHolder.iv_comment_photo, HttpUtils.host + "upload/" + comment.childDiscussantImg);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return convertView;
        }


    }

    private static class ViewHolder {
        ImageView iv_comment_photo;


        TextView tv_comment_name;
        TextView tv_comment_content;
        TextView tv_comment_time;

    }
}
