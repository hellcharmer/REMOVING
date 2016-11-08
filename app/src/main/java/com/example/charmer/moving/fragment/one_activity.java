package com.example.charmer.moving.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Remark;
import com.example.charmer.moving.pojo.User;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.example.charmer.moving.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class one_activity extends AppCompatActivity {

    private ImageView iv_photoImg;
    private TextView tv_infoName;
    private TextView tv_infoContent;
    private TextView tv_infoDate;
    private NineGridTestLayout nineGridTestLayout;
    private ImageView iv_cancel;
    private List<Remark> remarkList = new ArrayList<Remark>();
    private ListView lv_commentlist;
    private AdapterComment myadapter;
    private EditText input_comment;
    private TextView btn_publish_comment;
    private EditText input_comment1;
    private TextView btn_send;
    private TextView tv_likenum;
    private CheckBox iv_like;
    private String userimg;
    private LinearLayout linear;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_one);
        Integer userid = MainActivity.getUser().getUserid();
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        getuserbyuserid(userid);
        initView();
        initData();
        initevent();

    }

    private void initevent() {
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        input_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_comment.setFocusable(true);
                input_comment.setFocusableInTouchMode(true);
                input_comment.requestFocus();
                input_comment.findFocus();
            }
        });
        iv_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_likenum.setText(String.valueOf(Integer.parseInt(tv_likenum.getText().toString())+1));
                }
                else {
                    tv_likenum.setText(String.valueOf(Integer.parseInt(tv_likenum.getText().toString())-1));
                }
            }
        });


    }

    private void initView() {
        //评论狂
        linear = ((LinearLayout) findViewById(R.id.linearLayout));
        iv_photoImg = ((ImageView) findViewById(R.id.iv_photoImg));
        tv_infoName = ((TextView) findViewById(R.id.tv_infoName));
        tv_infoContent = ((TextView) findViewById(R.id.tv_infoContent));
        tv_infoDate = ((TextView) findViewById(R.id.tv_infoDate));
        iv_photoImg = ((ImageView) findViewById(R.id.iv_photoImg));
        nineGridTestLayout = (NineGridTestLayout) findViewById(R.id.gridview);
        iv_cancel = ((ImageView) findViewById(R.id.iv_cancel));
        lv_commentlist = ((ListView) findViewById(R.id.lv_commentlist));
        input_comment = ((EditText) findViewById(R.id.input_comment));
        btn_publish_comment = ((TextView) findViewById(R.id.btn_publish_comment));
        tv_likenum = ((TextView) findViewById(R.id.tv_likeNumber));
        iv_like = ((CheckBox) findViewById(R.id.iv_like));

    }

    private void initData() {
        Intent intent = getIntent();
        String userimg = intent.getStringExtra("userimg");
        xUtilsImageUtils.display(iv_photoImg, HttpUtils.host_dynamic + userimg, true);
        String username = intent.getStringExtra("username");
        tv_infoName.setText(username);
        String infoDate = intent.getStringExtra("infoDate");
        tv_infoDate.setText(infoDate);
        String infoContent = intent.getStringExtra("infoContent");
        tv_infoContent.setText(infoContent);
        String likenum = intent.getStringExtra("infoLikeNum");
        Log.i("info==", "initData1: "+likenum);
        tv_likenum.setText(likenum);
        List<String> urlsList = new ArrayList<String>();
        String imgs = intent.getStringExtra("imgs");
        if (imgs != null && !"".equals(imgs)) {
            String[] images = imgs.split(",");
            if (images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    urlsList.add(HttpUtils.host_dynamic + images[i]);
                }
                nineGridTestLayout.setUrlList(urlsList);
                nineGridTestLayout.setIsShowAll(true);
            }
        } else if ("".equals(imgs) && imgs == null) {
            nineGridTestLayout.notifyDataSetChanged();
        }
        final String infoId = intent.getStringExtra("infoId");
        Log.i("oneactivity", "initData: " + infoId);
        getRemarkList(infoId);
        myadapter = new AdapterComment(getApplicationContext(), remarkList);
        lv_commentlist.setAdapter(myadapter);
        btn_publish_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRemark(MainActivity.getUser().getUserid(), Integer.parseInt(infoId));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void showdialog(final Integer infoId,final Integer fatherId,final String fatherName) {
        linear.setVisibility(View.GONE);
        final View view = LayoutInflater.from(this).inflate(R.layout.view_input_comment1, null);
        input_comment1 = ((EditText) view.findViewById(R.id.input_comment1));
        btn_send = ((TextView) view.findViewById(R.id.btn_send));
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,50);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "onClick: inputcomment"+input_comment1.getText().toString()+"");
                Log.i("info", "onClick: infoid" + infoId);
                sendRemark1(fatherId, infoId,fatherName);
                view.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void getRemarkList(String infoId) {
        RequestParams params = new RequestParams(HttpUtils.host_dynamic + "queryremark");
        params.addBodyParameter("infoId", infoId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("oneactivity", "onSuccess:result " + result);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Remark>>() {
                }.getType();
                List<Remark> remark = gson.fromJson(result, type);
                Log.i("oneactivity", "onSuccess: " + remark);
                remarkList.clear();
                remarkList.addAll(remark);
                if(myadapter==null){
                    myadapter = new AdapterComment(getApplicationContext(),remarkList);
                    lv_commentlist.setAdapter(myadapter);
                }else{
                    myadapter.notifyDataSetChanged();
                    myadapter.notifyDataSetInvalidated();
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
    public class AdapterComment extends BaseAdapter {
        Context context;
        List<Remark> remarkList;

        public AdapterComment(Context c, List<Remark> remarkList) {
            this.context = c;
            this.remarkList = remarkList;
        }

        @Override
        public int getCount() {
            return remarkList.size();
        }

        @Override
        public Object getItem(int i) {
            return remarkList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            // 重用convertView

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_one_item, null);
                holder.childDiscussantImg = ((ImageView) convertView.findViewById(R.id.iv_childimg));
                holder.childDiscussantName = (TextView) convertView.findViewById(R.id.tv_childname);
                holder.childComment = (TextView) convertView.findViewById(R.id.tv_remark);
                holder.commentTime = ((TextView) convertView.findViewById(R.id.tv_remarktime));
                holder.delete = ((TextView) convertView.findViewById(R.id.delete_remark));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Remark remark = remarkList.get(position);
            if (remark.getFatherDiscussant() == null || remark.getFatherDiscussant() == 0) {
                holder.childDiscussantName.setText(remark.childDiscussantName + "");
            } else {
                holder.childDiscussantName.setText(remark.childDiscussantName + " (回复 " + remark.fatherDiscussantName + " )");
            }
            holder.commentTime.setText(remark.commentTime + "");
            xUtilsImageUtils.display(holder.childDiscussantImg, HttpUtils.host_dynamic +"upload/"+ remark.getChildDiscussantImg());
            holder.childComment.setText(remark.childComment);
            if (MainActivity.getUser().getUserid().equals(remark.getChildDiscussant())) {
                holder.delete.setText("删除");
                holder.delete.setTextColor(Color.RED);
            } else {
                holder.delete.setText("回复");
                holder.delete.setTextColor(Color.BLUE);
            }
            final ViewHolder finalHolder = holder;
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalHolder.delete.getText().toString().equals("删除")) {
                        Log.i("sssss", "onClick: "+"回复");
                        deleteRemark(String.valueOf(remark.getCommentTime()), remark.getInfoId());
                        remarkList.remove(remarkList.get(position));
                        notifyDataSetChanged();
                    } if(finalHolder.delete.getText().toString().equals("回复")) {
                        Log.i("info===", "onClick: "+"回复");
                        showdialog(remark.getInfoId(),remark.getChildDiscussant(),remark.getChildDiscussantName());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

                    }
                }
            });
            return convertView;
        }

        /**
         * 静态类，便于GC回收
         */
        class ViewHolder {
            TextView delete;
            ImageView childDiscussantImg;
            TextView childDiscussantName;
            TextView childComment;
            TextView commentTime;
        }

    }
    public void deleteRemark(String remarkTime, Integer infoId) {
        Remark remark = new Remark(infoId,MainActivity.getUser().getUserid(), Timestamp.valueOf(remarkTime));
        RequestParams params = new RequestParams(HttpUtils.host_dynamic + "deleteremark");
        Gson gson = new Gson();
        String remarkInfo = gson.toJson(remark);
        Log.i("info", "sendRemark:two " + remarkInfo);
        params.addQueryStringParameter("remarkInfo", remarkInfo);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if ("true".equals(result)) {
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "网络已近断开", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void sendRemark(Integer ChildDiscount, Integer infoId) {
        if (input_comment.getText().toString().equals("")) {
            Toast.makeText(getApplication(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //生成评论数据

            Remark remark = new Remark(infoId,ChildDiscount, MainActivity.getUser().getUsername(), new Timestamp(System.currentTimeMillis()), input_comment.getText().toString() + "",0,"");
            Log.i("info", "infoListonClick: "+remark);
            remark.setChildDiscussantImg(userimg);
            remarkList.add(remark);
            myadapter.notifyDataSetChanged();
            RequestParams params = new RequestParams(HttpUtils.host_dynamic + "sendremark");
            Gson gson = new Gson();
            String remarkInfo = gson.toJson(remark);
            Log.i("info", "sendRemark:two " + remarkInfo);
            params.addQueryStringParameter("remarkInfo", remarkInfo);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if ("true".equals(result)) {
                        input_comment.setText("");
                        Toast.makeText(getApplication(), "评论成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "评论失败", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getApplication(), "网络已断开", Toast.LENGTH_SHORT).show();
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
    public void sendRemark1(Integer fatherId, Integer infoId,String fathername) {
        Log.i("info===", "sendRemark1: "+fatherId+fathername);
        if (input_comment1.getText().toString().equals("")) {
            Toast.makeText(getApplication(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //生成评论数据

            Remark remark = new Remark(infoId,MainActivity.getUser().getUserid(), input_comment1.getText().toString() + "",fatherId, "");
            remark.setFatherDiscussantName(fathername);
            remark.setCommentTime(new Timestamp(System.currentTimeMillis()));
            Log.i("info===", "sendRemark1: "+fatherId+fathername);
            remark.setChildDiscussantImg(userimg);
            remark.setChildDiscussantName(username);
            remarkList.add(remark);
            myadapter.notifyDataSetChanged();
            Log.i("info", "sendRemark: " + remark);
            RequestParams params = new RequestParams(HttpUtils.host_dynamic + "sendremark");
            Gson gson = new Gson();
            String remarkInfo = gson.toJson(remark);
            Log.i("info", "sendRemark:two " + remarkInfo);
            params.addQueryStringParameter("remarkInfo", remarkInfo);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if ("true".equals(result)) {
                        input_comment.setText("");
                        Toast.makeText(getApplication(), "评论真的成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "评论失败", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getApplication(), "网络已断开", Toast.LENGTH_SHORT).show();
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
    public void getuserbyuserid(Integer userid){
        RequestParams params=new RequestParams(HttpUtils.host_dynamic+"queryuserbyuserid");
        params.addBodyParameter("userid",String.valueOf(userid));
        x.http().get(params, new Callback.CommonCallback<String >() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                User user = gson.fromJson(result,User.class);
                userimg = user.getUserimg();
                username = user.getUsername();
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
}

