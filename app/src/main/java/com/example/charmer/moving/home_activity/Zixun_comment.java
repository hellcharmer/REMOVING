package com.example.charmer.moving.home_activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.CircularProgress;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.weyye.library.EmptyLayout;

public class Zixun_comment extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout iv_comment_return;
    private TextView xiangxi_author;
    private RelativeLayout zixun_comment_header;
    private SwipeRefreshLayout comment_sr_refresh;
    private LoadMoreListView lv_comment;
    private EmptyLayout emptyLayout;
    private ImageView iv_comment;
    private EditText edt_comment_name;
    private RelativeLayout activity_zixun_comment;
    int page_comment = 1;
    int totalpage_comment = 0;//总页数
    MyAdapter adapter;
    int iv_sortcount = 0;
    String state = "0";
    String zixunId = "";
    private List<ListActivityBean.Comments> commentList = new ArrayList<ListActivityBean.Comments>();
    private ImageView iv_comment_sort;
    private RelativeLayout rl_comment_sort;
    private ToastUtil toastUtil;
    private CircularProgress pb_publish_comment;
    private boolean flag=true;//true为评论状态，false为回复状态
    private long childId=0;
    private String childcontent="";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun_comment);
        View v = View.inflate(Zixun_comment.this, R.layout.layout_toast_view, null);
        toastUtil = new ToastUtil(Zixun_comment.this, v, 200);
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
                        getCommentslist(1, zixunId, state);

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
        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(commentList.get(position).childDiscussant==Long.parseLong(MainActivity.getUser().getUseraccount())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            Zixun_comment.this);
                    builder.setMessage(getString(R.string.delete_sure));
                    builder.setTitle(getString(R.string.delete));
                    builder.setIcon(getResources().getDrawable(
                            R.drawable.delete1));
                    builder.setPositiveButton(
                            getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface,
                                        int which) {
                                    // TODO Auto-generated method
                                    ListActivityBean.Comments comments = new ListActivityBean.Comments(Integer.parseInt(zixunId), Long.parseLong(MainActivity.getUser().getUseraccount()), commentList.get(position).commentTime);
                                    lv_comment.setAdapter(adapter);
                                    Delete_comments(comments);
                                    handler =new Handler(){
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what){
                                                case 0:
                                                    commentList.remove(commentList.get(position));
                                                    adapter.notifyDataSetChanged();
                                                    break;
                                            }
                                        }
                                    };

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
                }else {

                    edt_comment_name.requestFocus();
                    edt_comment_name.setFocusable(true);
                    InputMethodManager inputManager = (InputMethodManager)edt_comment_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(edt_comment_name, 0);
                    childId = commentList.get(position).childDiscussant;
                    childcontent = commentList.get(position).childComment;
                    edt_comment_name.setHint("回复" + commentList.get(position).childDiscussantName + "的评论:");

                    flag = false;
                }
            }
        });

    }

    private void initData() {

        emptyLayout.bindView(lv_comment);
        Intent intent = this.getIntent();
        zixunId = intent.getStringExtra("zixunId");
        adapter = new MyAdapter(commentList);
        lv_comment.setAdapter(adapter);
        getCommentslist(page_comment, zixunId, state);
    }

    private void initView() {


        iv_comment_return = (RelativeLayout) findViewById(R.id.iv_comment_return);
        iv_comment_return.setOnClickListener(this);
        xiangxi_author = (TextView) findViewById(R.id.xiangxi_author);

        zixun_comment_header = (RelativeLayout) findViewById(R.id.zixun_comment_header);

        comment_sr_refresh = (SwipeRefreshLayout) findViewById(R.id.comment_sr_refresh);

        lv_comment = (LoadMoreListView) findViewById(R.id.lv_comment);

        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);

        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        iv_comment.setOnClickListener(this);
        edt_comment_name = (EditText) findViewById(R.id.edt_comment_name);
        edt_comment_name.addTextChangedListener(mTextWatcher);
        activity_zixun_comment = (RelativeLayout) findViewById(R.id.activity_zixun_comment);

        iv_comment_sort = (ImageView) findViewById(R.id.iv_comment_sort);
        rl_comment_sort = (RelativeLayout) findViewById(R.id.rl_comment_sort);
        rl_comment_sort.setOnClickListener(this);
        pb_publish_comment = (CircularProgress) findViewById(R.id.pb_publish_comment);
        comment_sr_refresh.setColorSchemeColors(ContextCompat.getColor(Zixun_comment.this,R.color.refresh_circle));
        comment_sr_refresh.setDistanceToTriggerSync(getResources().getDimensionPixelOffset(R.dimen.swipe_progress_appear_offset));
        comment_sr_refresh.setProgressViewEndTarget(true, getResources().getDimensionPixelOffset(R.dimen.swipe_progress_to_top));
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub


            if (temp.length() > 0) {
                iv_comment.setImageResource(R.drawable.comment_select);
                iv_comment.setClickable(true);
            } else {
                iv_comment.setImageResource(R.drawable.comment);
                iv_comment.setClickable(true);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_comment_sort:
                if (iv_sortcount % 2 == 0) {
                    commentList.clear();
                    iv_comment_sort.setImageResource(R.drawable.ic_ascending);
                    state = "1";
                    getCommentslist(page_comment, zixunId, state);
                } else {
                    commentList.clear();
                    iv_comment_sort.setImageResource(R.drawable.ic_descending);
                    state = "0";
                    getCommentslist(page_comment, zixunId, state);
                }
                iv_sortcount++;
                break;
            case R.id.iv_comment:
                if(edt_comment_name.getText().toString().equals("")) {

                }else{
                    if(flag==true) {
                        iv_comment.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        ListActivityBean.Comments comments = new ListActivityBean.Comments(Integer.parseInt(zixunId), Long.parseLong(MainActivity.getUser().getUseraccount()), (long) 0, new Date(System.currentTimeMillis()), edt_comment_name.getText().toString(), "");
                        Publish_comments(comments);
                    }else{
                        iv_comment.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        ListActivityBean.Comments comments = new ListActivityBean.Comments(Integer.parseInt(zixunId), Long.parseLong(MainActivity.getUser().getUseraccount()), childId, new Date(System.currentTimeMillis()), edt_comment_name.getText().toString(), childcontent);
                        Publish_comments(comments);
                    }
                }
                break;
            case R.id.iv_comment_return:
                finish();
                break;
        }
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
                if (page_comment < totalpage_comment) {

                    getCommentslist(++page_comment, zixunId, state);
                } else {
                    toastUtil.Short(Zixun_comment.this, "已经到底了！").show();
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

    private void Publish_comments(ListActivityBean.Comments comments) {
        pb_publish_comment.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(HttpUtils.hoster + "updatecomments");
        Gson gson =new Gson();
        String comment=gson.toJson(comments);
        params.addQueryStringParameter("comment",comment);
        params.addQueryStringParameter("choice", "0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if("true".equals(result)){
                    edt_comment_name.setText("");
                    getCommentslist(page_comment, zixunId, state);
                    Toast.makeText(Zixun_comment.this,"发布成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Zixun_comment.this,"发布失败",Toast.LENGTH_SHORT).show();
                }
                iv_comment.setVisibility(View.VISIBLE);
                pb_publish_comment.setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Zixun_comment.this,"网络已断开",Toast.LENGTH_SHORT).show();
                iv_comment.setVisibility(View.VISIBLE);
                pb_publish_comment.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void Delete_comments(final ListActivityBean.Comments comments) {
        RequestParams params = new RequestParams(HttpUtils.hoster + "updatecomments");
        Gson gson =new Gson();
        String comment=gson.toJson(comments);
        params.addQueryStringParameter("comment",comment);
        params.addQueryStringParameter("choice", "1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if("true".equals(result)){
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Toast.makeText(Zixun_comment.this,"删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Zixun_comment.this,"删除失败",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Zixun_comment.this,"删除失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getCommentslist(int page, String zixunId, String state) {

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
                System.out.println(bean.commentList);
                totalpage_comment = bean.page;

                if (page_comment == 1) {


                    commentList.clear();
                }

                commentList.addAll(bean.commentList);
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
        public View getView( int position, View convertView, ViewGroup parent) {
            final int positions =position;
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
                viewHolder.tv_comment_watchmore = ((RelativeLayout) convertView.findViewById(R.id.tv_comment_watchmore));
                //字体加粗
                TextPaint tp = viewHolder.tv_comment_name.getPaint();
                tp.setFakeBoldText(true);


                convertView.setTag(viewHolder);//缓存对象
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            try {


                //Log.i("TAG",(URLDecoder.decode(zixun.timeStamp,"utf-8")));
                viewHolder.tv_comment_time.setText(DateUtils.getGapTimeFromNow(DateUtils.stringToDate(comment.commentTime)));
                viewHolder.tv_comment_name.setText(comment.childDiscussantName);
                if (comment.fatherDiscussant ==(long)0) {
                    viewHolder.tv_comment_content.setText(comment.childComment);
                    viewHolder.tv_comment_watchmore.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_comment_watchmore.setVisibility(View.VISIBLE);
                    viewHolder.tv_comment_content.setText("回复" + comment.fatherDiscussantName + "的评论：" + comment.childComment);
                }

                xUtilsImageUtils.display(viewHolder.iv_comment_photo, HttpUtils.hoster + "upload/" + comment.childDiscussantImg,true);
                viewHolder.tv_comment_watchmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Zixun_comment.this,Zixun_comment_more.class);
                        intent.putExtra("childDiscussant",commentList.get(positions).childDiscussant+"");
                        intent.putExtra("fatherDiscussant",commentList.get(positions).fatherDiscussant+"");
                        intent.putExtra("zixunid",commentList.get(positions).zixun_id+"");
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
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
       RelativeLayout tv_comment_watchmore;

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(edt_comment_name.getText().toString().equals("")&&edt_comment_name.getHint()!="添加评论"&&keyCode==KeyEvent.KEYCODE_BACK){
            edt_comment_name.setHint("添加评论");
            flag=true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
