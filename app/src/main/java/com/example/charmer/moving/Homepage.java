package com.example.charmer.moving;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.PersonalInfo;
import com.example.charmer.moving.pojo.VariableExercise;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.example.charmer.moving.utils.ViewPagerAdapter;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Homepage extends AppCompatActivity {
    private LinearLayout head_layout;
    private TabLayout toolbar_tab;
    private ViewPager main_vp_container;
    private TextView userName;
    private TextView personalsay;
    private TextView articles;
    private TextView collections;
    private TextView publish;
    private TextView join;
    private ImageView head_iv;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout root_layout;
    private VariableExercise.DataSummary ds =new VariableExercise.DataSummary();
    private String useraccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        Intent intent = this.getIntent();
        useraccount = intent.getStringExtra("user");

        AppBarLayout app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        root_layout = (CoordinatorLayout) findViewById(R.id.root_layout);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsing_toolbar_layout);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle(ds.userName);
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });
        toolbar_tab = (TabLayout) findViewById(R.id.toolbar_tab);
        main_vp_container = (ViewPager) findViewById(R.id.main_vp_container);
        userName = (TextView) findViewById(R.id.userName);
        head_iv = (ImageView) findViewById(R.id.head_iv);
        personalsay = (TextView) findViewById(R.id.personalsay);
        articles = (TextView)findViewById(R.id.articles);
        collections = (TextView)findViewById(R.id.collections);
        publish = (TextView)findViewById(R.id.publish);
        join = (TextView)findViewById(R.id.join);
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this,useraccount);
        main_vp_container.setAdapter(vpAdapter);
        main_vp_container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (toolbar_tab));
        toolbar_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (main_vp_container));
        //tablayout和viewpager建立联系为什么不用下面这个方法呢？自己去研究一下，可能收获更多
        //toolbar_tab.setupWithViewPager(main_vp_container);
        loadBlurAndSetStatusBar();
        getExerciseList();
        ImageView head_iv = (ImageView) findViewById(R.id.head_iv);
        Glide.with(this).load(R.mipmap.bbg).bitmapTransform(new RoundedCornersTransformation(this,
                90, 0)).into(head_iv);
    }

    /**
     * 设置毛玻璃效果和沉浸状态栏
     */
    private void loadBlurAndSetStatusBar() {
        StatusBarUtil.setTranslucent(Homepage.this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        Glide.with(this).load(R.mipmap.bbg).bitmapTransform(new BlurTransformation(this, 100))
                .into(new SimpleTarget<GlideDrawable>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        head_layout.setBackground(resource);
                        root_layout.setBackground(resource);
                    }
                });

        Glide.with(this).load(R.mipmap.bbg).bitmapTransform(new BlurTransformation(this, 100))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        mCollapsingToolbarLayout.setContentScrim(resource);
                    }
                });
    }
    private void getExerciseList() {

        String str = HttpUtils.hoster+"getpersonalinfo";
        RequestParams params = new RequestParams(str);
        //params.addQueryStringParameter("user",MainActivity.getUser().getUseraccount());
        params.addQueryStringParameter("user",useraccount);
        params.addQueryStringParameter("state","0");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                PersonalInfo bean = gson.fromJson(result, PersonalInfo.class);
                ds = bean.ds;
                userName.setText(ds.userName);
                xUtilsImageUtils.display(head_iv,HttpUtils.hoster+"upload/"+ds.userImg,true);
                if(ds.personalsay!=null&&!"".equals(ds.personalsay)){
                    personalsay.setText(ds.personalsay);
                }
                articles.setText(ds.articles+"篇");
                collections.setText(ds.collections+"篇");
                publish.setText(ds.publishedNum+"次/"+ds.successfulpublishpercent);
                join.setText(ds.joinedNum+"次/"+ds.appointmentRate);

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
