package com.example.charmer.moving.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import me.weyye.library.EmptyLayout;

import static com.example.charmer.moving.contantData.HttpUtils.host_dynamic;

public class one_activity extends AppCompatActivity {

    private ImageView iv_photoImg;
    private TextView tv_infoName;
    private TextView tv_infoContent;
    private TextView tv_infoDate;
    private LoadMoreListView lv_comment;
    private SwipeRefreshLayout comment_sr_refresh;
    private EmptyLayout emptyLayout;
    private ToastUtil toastUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_one);
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
//                        getRemarkList();
                        //调用该方法结束刷新，否则加载圈会一直在
                        comment_sr_refresh.setRefreshing(false);

                    }
                }, 1000);
            }
        });

        lv_comment.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
//                loadCommentMore();
            }
        });

    }

    private void initView() {
        iv_photoImg = ((ImageView) findViewById(R.id.iv_photoImg));
        tv_infoName = ((TextView) findViewById(R.id.tv_infoName));
        tv_infoContent = ((TextView) findViewById(R.id.tv_infoContent));
        tv_infoDate = ((TextView) findViewById(R.id.tv_infoDate));
        iv_photoImg = ((ImageView)findViewById(R.id.iv_photoImg));
        lv_comment = (LoadMoreListView) findViewById(R.id.lv_comment);
        comment_sr_refresh = (SwipeRefreshLayout) findViewById(R.id.comment_sr_refresh);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);



    }
    private void initData() {
        Intent intent = getIntent();
        String userimg = intent.getStringExtra("userimg");
        xUtilsImageUtils.display(iv_photoImg,host_dynamic+userimg,true);
        String username = intent.getStringExtra("username");
        tv_infoName.setText(username);
        String infoDate = intent.getStringExtra("infoDate");
        tv_infoDate.setText(infoDate);
        String infoContent = intent.getStringExtra("infoContent");
        tv_infoContent.setText(infoContent);
        Integer infoLikenum = Integer.parseInt(intent.getStringExtra("infoLikenum"));

        String imgs = intent.getStringExtra("imgs");
        String infoId = intent.getStringExtra("infoId");
        getRemarkList(infoId);
    }

    private void getRemarkList(String infoId){
        RequestParams params = new RequestParams(host_dynamic + "queryremark");
        params.addBodyParameter("infoId",infoId);
        x.http().get(params, new Callback.CommonCallback<String>() {
           @Override
           public void onSuccess(String result) {
               System.out.println("---------------"+result);
               Gson gson = new Gson();

               emptyLayout.showSuccess();

               //通知listView更新界面
//               adapter.notifyDataSetChanged();

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
}

