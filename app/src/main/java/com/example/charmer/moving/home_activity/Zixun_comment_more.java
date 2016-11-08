package com.example.charmer.moving.home_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.DateUtils;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.weyye.library.EmptyLayout;

public class Zixun_comment_more extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_comment_return;
    private TextView xiangxi_author;
    private RelativeLayout zixun_commentmore_header;
    private LoadMoreListView lv_comment;
    private SwipeRefreshLayout comment_sr_refresh;
    private EmptyLayout emptyLayout;
    private RelativeLayout activity_zixun_comment_more;
    private List<ListActivityBean.Comments> commentmoreList = new ArrayList<ListActivityBean.Comments>();
    private mAdapter adapter;
    int page_commentmore = 1;
    int totalpage_commentmore = 0;//总页数
    String childDiscussant;
    String fatherDiscussant;
    String zixunid;
    private ToastUtil toastUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun_comment_more);
        View v = View.inflate(Zixun_comment_more.this, R.layout.layout_toast_view, null);
        toastUtil = new ToastUtil(Zixun_comment_more.this, v, 200);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        initView();
        initData();
        bindEvents();
    }
    private void bindEvents() {
        comment_sr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comment_sr_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getmoreCommentslist(1,zixunid,"1",childDiscussant,fatherDiscussant);
                        //调用该方法结束刷新，否则加载圈会一直在
                        comment_sr_refresh.setRefreshing(false);

                    }
                }, 1000);
            }
        });

        lv_comment.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                loadCommentMore();
            }
        });
        iv_comment_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initData() {
        Intent intent =this.getIntent();
        childDiscussant=intent.getStringExtra("childDiscussant");
        fatherDiscussant=intent.getStringExtra("fatherDiscussant");
        zixunid=intent.getStringExtra("zixunid");
        System.out.println(childDiscussant+fatherDiscussant+zixunid);
        adapter=new mAdapter(Zixun_comment_more.this,commentmoreList,R.layout.zixun_comment_list);
        lv_comment.setAdapter(adapter);
        getmoreCommentslist(1,zixunid,"1",childDiscussant,fatherDiscussant);

    }
    private void getmoreCommentslist(int page, String zixunId, String state,String childDiscussant,String fatherDiscussant) {

        RequestParams params = new RequestParams(HttpUtils.hoster + "getcomments");
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("zixunId", zixunId);
        params.addQueryStringParameter("state", state);
        params.addQueryStringParameter("choice", "2");
        params.addQueryStringParameter("personA",childDiscussant);
        params.addQueryStringParameter("personB",fatherDiscussant);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("---------------"+result);
                Gson gson = new Gson();
                ListActivityBean bean = gson.fromJson(result, ListActivityBean.class);
                System.out.println(bean.commentList);
                totalpage_commentmore = bean.page;
                if (page_commentmore == 1) {
                    commentmoreList.clear();
                }
                commentmoreList.addAll(bean.commentList);
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
    private void initView() {

        iv_comment_return = (ImageView) findViewById(R.id.iv_comment_return);
        iv_comment_return.setOnClickListener(this);
        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);
        xiangxi_author.setOnClickListener(this);
        zixun_commentmore_header = (RelativeLayout) findViewById(R.id.zixun_commentmore_header);

        lv_comment = (LoadMoreListView) findViewById(R.id.lv_comment);

        comment_sr_refresh = (SwipeRefreshLayout) findViewById(R.id.comment_sr_refresh);

        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        activity_zixun_comment_more = (RelativeLayout) findViewById(R.id.activity_zixun_comment_more);

    }

    @Override
    public void onClick(View v) {

    }

    private void loadCommentMore() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (page_commentmore < totalpage_commentmore) {

                    getmoreCommentslist(++page_commentmore,zixunid,"1",childDiscussant,fatherDiscussant);
                } else {
                    toastUtil.Short(Zixun_comment_more.this, "已经到底了！").show();
                }

                Zixun_comment_more.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        lv_comment.setLoadCompleted();
                    }
                });
            }
        }.start();
    }
    public class mAdapter extends CommonAdapter<ListActivityBean.Comments> {

        public mAdapter(Context context, List lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, ListActivityBean.Comments comments, int position) {
            TextView tv_comment_name = ((TextView) viewHolder.getViewById(R.id.tv_comment_name));
            TextView tv_comment_content = ((TextView)viewHolder.getViewById(R.id.tv_comment_content));
            TextView tv_comment_time = ((TextView)viewHolder.getViewById(R.id.tv_comment_time));
            ImageView iv_comment_photo = ((ImageView)viewHolder.getViewById(R.id.iv_comment_photo));
            tv_comment_time.setText(DateUtils.getGapTimeFromNow(DateUtils.stringToDate(comments.commentTime)));
            tv_comment_name.setText(comments.childDiscussantName);
            if (comments.fatherDiscussant ==(long)0) {
                tv_comment_content.setText(comments.childComment);
            } else {
                tv_comment_content.setText("回复" + comments.fatherDiscussantName + "的评论：" + comments.childComment);
            }
            xUtilsImageUtils.display(iv_comment_photo, HttpUtils.hoster + "upload/" + comments.childDiscussantImg);
        }

    }


}
