package com.example.charmer.moving.friendchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Post;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostAcyivity extends AppCompatActivity {
    @InjectView(R.id.tv_ask)
    TextView tvAsk;
    @InjectView(R.id.tv_post)
    TextView tvPost;
    private List<Post> psls = new ArrayList<Post>();
    private List<Post> psingls = new ArrayList<Post>();
    private List<Post> rels = new ArrayList<Post>();
    private ViewPager vp_post;
    private ImageView friendyuan;
    private ImageView chiluyuan;
    private PagerAdapter vpPost;
    private List<View> views = new ArrayList<View>();
    private ListView lv_post;
    private ListView lv_rejert;
    private CommonAdapter<Post> psingad;
    private CommonAdapter<Post> relsad;
    private TextView tv7;
    private TextView tv8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_acyivity);
        ButterKnife.inject(this);
        initView();
        initData();
        initEvt();
    }

    private void initView() {
        vp_post = ((ViewPager) findViewById(R.id.vp_post));
        friendyuan = ((ImageView) findViewById(R.id.friendyuan));
        chiluyuan = ((ImageView) findViewById(R.id.chuliyuan));
        LayoutInflater layoutInflater = LayoutInflater.from(PostAcyivity.this);
        View v1 = layoutInflater.inflate(R.layout.posting, null);
        View v2 = layoutInflater.inflate(R.layout.rejert, null);
        lv_post = ((ListView) v1.findViewById(R.id.lv_posting));
        lv_rejert = ((ListView) v2.findViewById(R.id.lv_rejert));
        tv8 = ((TextView) v1.findViewById(R.id.tv8));
        tv7 = ((TextView) v2.findViewById(R.id.tv7));
        views.add(v1);
        views.add(v2);
        //vp适配器打气
        vpPost = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

        };
        vp_post.setAdapter(vpPost);
    }

    private void initData() {
        ifpost();
    }

    private void initEvt() {
        //vp滑动改变
        vp_post.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                 Toast.makeText(PostAcyivity.this,position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
      tvAsk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              friendyuan.setVisibility(View.GONE);
               vp_post.setCurrentItem(0);
              tvAsk.setBackgroundResource(R.drawable.postshape);
//              tvAsk.setBackgroundColor(getResources().getColor(R.color.exeinfomid));
              tvAsk.setTextColor(getResources().getColor(R.color.colorSkybule));
              tvPost.setBackgroundResource(R.drawable.askshape);
              tvPost.setTextColor(getResources().getColor(R.color.exeinfomid));
          }
      });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chiluyuan.setVisibility(View.GONE);
                vp_post.setCurrentItem(1);
                tvPost.setBackgroundResource(R.drawable.postshape);
                tvPost.setTextColor(getResources().getColor(R.color.colorSkybule));
                tvAsk.setBackgroundResource(R.drawable.askshape);
                tvAsk.setTextColor(getResources().getColor(R.color.exeinfomid));
            }
        });
    }


    //是否有申请
    private void ifpost() {
        RequestParams requestParams = new RequestParams(HttpUtils.host4 + "getallpost");
        requestParams.addQueryStringParameter("userid", MainActivity.getUser().getUserid() + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("[]")) {
                    Toast.makeText(PostAcyivity.this, "没有您的最新消息(；°○° )", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostAcyivity.this, "主上有人很欣赏你啊 (ˉ▽ˉ；) ", Toast.LENGTH_SHORT).show();
                    psls.clear();
                    psingls.clear();
                    rels.clear();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Post>>() {
                    }.getType();
                    Log.i("postacccc", result);
                    List<Post> newpsls = gson.fromJson(result, type);
                    psls.addAll(newpsls);
                    for (int i = 0; i < psls.size(); i++) {
                        if (psls.get(i).getAgree() == 1 && psls.get(i).getPostid() != MainActivity.getUser().getUserid()) {
                            psingls.add(psls.get(i));
                        }
                        if (psls.get(i).getAgree() != 1 && psls.get(i).getPostid() == MainActivity.getUser().getUserid()) {
                            rels.add(psls.get(i));
                        }
                    }
                    if (!psingls.isEmpty()) {
                        friendyuan.setVisibility(View.VISIBLE);
                        psingad = new Postad(PostAcyivity.this, psingls, R.layout.posting_layout);
                        lv_post.setAdapter(psingad);
                    }else{
                        tv8.setVisibility(View.VISIBLE);
                    }
                    if (!rels.isEmpty()) {
                        chiluyuan.setVisibility(View.VISIBLE);
                        relsad=new Rejertad(PostAcyivity.this,rels,R.layout.rejert_layout);
                        lv_rejert.setAdapter(relsad);
                    }else{
                        tv7.setVisibility(View.VISIBLE);
                    }

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

    //访问适配器
    class Postad extends CommonAdapter<Post> {
        public Postad(Context context, List<Post> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Post post, final int position) {
            ImageView postimg = ((ImageView) viewHolder.getViewById(R.id.postimg));
            xUtilsImageUtils.display(postimg, HttpUtils.hostpc + post.getPostimg(), true);
            TextView postname = ((TextView) viewHolder.getViewById(R.id.postname));
            postname.setText(post.getPostname());
            TextView tv6 = ((TextView) viewHolder.getViewById(R.id.tv6));
            final Integer postiid = post.getPost();
            if (post.getState() == 0) {
                tv6.setText("添加你为好友");
            }
            if (post.getState() == 1) {
                tv6.setText("加入群" + post.getGetname());
            }
            Button agree = ((Button) viewHolder.getViewById(R.id.agree));
            Button rejert = ((Button) viewHolder.getViewById(R.id.rejert));
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer jude = 1;
                    Jude(postiid, jude);
                    psingls.remove(position);
                    psingad.notifyDataSetChanged();
                }
            });
            rejert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer jude = 0;
                    Jude(postiid, jude);
                    psingls.remove(position);
                    psingad.notifyDataSetChanged();
                }
            });
        }
    }

    //申请适配器
    class Rejertad extends CommonAdapter<Post> {
        public Rejertad(Context context, List<Post> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, final Post post, final int position) {
            //同意处理
                   if(post.getAgree()==0){
                      RelativeLayout rlpost1= ((RelativeLayout) viewHolder.getViewById(R.id.rlpost1));
                       rlpost1.setVisibility(View.VISIBLE);
                     TextView tv_agree=  ((TextView) viewHolder.getViewById(R.id.tv_agree));
                       if (post.getState()==0){
                           tv_agree.setText("已添加"+post.getGetname()+"为好友");
                       }if (post.getState()==1){
                           tv_agree.setText("已加入群"+post.getGetname());
                       }
                    TextView tv_agree2=  ((TextView) viewHolder.getViewById(R.id.tv_agree2));
                      final Integer pst= post.getPost();
                       tv_agree2.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               RequestParams requestParams=new RequestParams(HttpUtils.host4+"friendservlet");
                               requestParams.addQueryStringParameter("choice",5+"");
                               requestParams.addQueryStringParameter("post",pst+"");
                               x.http().get(requestParams, new Callback.CommonCallback<String>() {
                                   @Override
                                   public void onSuccess(String result) {
                                       Toast.makeText(PostAcyivity.this,"一起聊聊吧",Toast.LENGTH_SHORT).show();
                                       rels.remove(position);
                                       relsad.notifyDataSetChanged();
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
                       });
                   }
            //拒绝处理
            if (post.getAgree()==2){
                RelativeLayout rlpost2= ((RelativeLayout) viewHolder.getViewById(R.id.rlpost2));
                rlpost2.setVisibility(View.VISIBLE);
                TextView tv_agree3=  ((TextView) viewHolder.getViewById(R.id.tv_agree3));
                if (post.getState()==0){
                    tv_agree3.setText("被"+post.getGetname()+"拒绝");
                }if (post.getState()==1){
                    tv_agree3.setText("被群"+post.getGetname()+"拒绝");
                }
                TextView tv_agree4=  ((TextView) viewHolder.getViewById(R.id.tv_agree4));
                final Integer pst2= post.getPost();
                tv_agree4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestParams requestParams=new RequestParams(HttpUtils.host4+"deletepost");
                        requestParams.addQueryStringParameter("post",pst2+"");
                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(PostAcyivity.this,"忘掉他吧",Toast.LENGTH_SHORT).show();
                                rels.remove(position);
                                relsad.notifyDataSetChanged();

                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.i("reonError",ex+"");
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
            }
        }
    }

    private void Jude(Integer postid, Integer jude) {
        RequestParams params = new RequestParams(HttpUtils.host4 + "friendservlet");
        params.addQueryStringParameter("choice", 0 + "");
        params.addQueryStringParameter("post", postid + "");
        params.addQueryStringParameter("jude", jude + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(PostAcyivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PostAcyivity.this, "操作失败", Toast.LENGTH_SHORT).show();
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
