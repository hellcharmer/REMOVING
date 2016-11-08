package com.example.charmer.moving.Publishdynamic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.MyView.PullToZoomListView;
import com.example.charmer.moving.MyView.RoundImageView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Info;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.DensityUtil;
import com.example.charmer.moving.utils.StatusBarCompat;
import com.example.charmer.moving.utils.StatusBarUtils;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.example.charmer.moving.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineDynamic extends AppCompatActivity implements PullToZoomListView.PullToZoomListViewListener, AbsListView.OnScrollListener{

    private Context mContext;
    private PullToZoomListView ptzlv_container;
    private RelativeLayout mRlTop;
    private TextView tv_nick;
    private ArrayAdapter<String> mAdapter;
    private RoundImageView iv_avatar;
    private ImageView iv_certified;
    private ImageButton ib_back;
    private ImageButton ib_notification;
    private ImageView mTopCerfitied;
    private ViewGroup.MarginLayoutParams mRlAvatarViewLayoutParams;
    private RoundImageView riv_avatar;
    private RelativeLayout mRlAvatarView;
    private ProgressBar pb_loading;
    private View mHeaderView;
    private boolean loadMore = false;
    private int mTopAlpha;
    private boolean mTopBgIsDefault = true;
    private CommonAdapter<Info> mineadapter;
    private List<Info> infoList = new ArrayList<>();
    //记录图片的位置
    Map<Integer,String> grid = new HashMap<Integer,String>();
    Map<Integer,Boolean> judje = new HashMap<Integer,Boolean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_dynamic);
        mContext = this;
        StatusBarCompat.compat(this, Color.parseColor("#0099ff"));
        initView();
        initData();
        StatusBarUtils.from(this)
                .setTransparentStatusbar(true)//状态栏是否透明
                .setTransparentNavigationbar(false)//Navigationbar是否透明
                .setActionbarView(mRlTop)//view是否透明
                .setLightStatusBar(false)//状态栏字体是否为亮色
                .process();
    }

    private void initView() {
        mHeaderView = View.inflate(mContext, R.layout.header_userinfo, null);
        riv_avatar = (RoundImageView) mHeaderView.findViewById(R.id.riv_avatar);
        ptzlv_container = (PullToZoomListView) findViewById(R.id.ptzlv_container);
        mRlTop = (RelativeLayout) findViewById(R.id.rl_top);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        iv_certified = (ImageView) findViewById(R.id.iv_certified);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_notification = (ImageButton) findViewById(R.id.ib_notification);
        mRlAvatarView = (RelativeLayout) findViewById(R.id.rl_userinfo_top_avatar);
        mTopCerfitied = (ImageView) findViewById(R.id.iv_certified);
        mRlTop = (RelativeLayout) findViewById(R.id.rl_top);
        pb_loading = (ProgressBar) findViewById(R.id.progressBar);
        mRlAvatarViewLayoutParams = (ViewGroup.MarginLayoutParams) mRlAvatarView.getLayoutParams();


        //设置背景图
        ptzlv_container.getHeaderView().setImageResource(R.drawable.icon_scroller_header);
        ptzlv_container.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        //设置header大小
        ptzlv_container.setHeaderViewSize(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(mContext, 280));
        //设置headerView
        ptzlv_container.getHeaderContainer().addView(mHeaderView);
        ptzlv_container.setHeaderView();
        //设置Adapter
        ptzlv_container.setAdapter(mineadapter);

        ptzlv_container.setOnScrollListener(this);
        //添加刷新监听和加载更多监听
        ptzlv_container.setPullToZoomListViewListener(this);

    }
    private void initData() {
        getData();

    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessage(1);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onReload() {
        mHandler.removeMessages(3);
        pb_loading.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(3, 1000);
        Toast.makeText(mContext, "更新中...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadMore() {
        mHandler.removeMessages(4);
        mHandler.sendEmptyMessageDelayed(4, 1000);
        Toast.makeText(mContext, "加载中,请稍后...", Toast.LENGTH_SHORT).show();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateImageBgUI();
                    break;
                case 2:
                    updateImageBgUI();
                    mAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    pb_loading.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    loadMore = true;
                    initData();
                    ptzlv_container.setLoadFinish(true);
                    break;
            }
        }
    };


    /**
     * 更新图片背景
     */
    private void updateImageBgUI() {
        int[] location = new int[2];
        riv_avatar.getLocationOnScreen(location);
        if (location[1] < 255) {
            mTopBgIsDefault = false;
            if (ptzlv_container.getFirstVisiblePosition() >= 1 || location[1] < 0) {
                if (mTopAlpha != 255) {
                    mRlTop.setBackgroundColor(Color.argb(255, 66, 66, 66));
                    mTopAlpha = 255;
                    ib_back.setImageResource(R.drawable.icon_back_bold_white);
                }
            } else {
                mTopAlpha = 255 - location[1];
                mRlTop.setBackgroundColor(Color.argb(mTopAlpha, 66, 66, 66));
                ib_back.setImageResource(R.drawable.icon_back_white);
            }
        } else {
            setDefaultImageBg();
        }
    }

    /**
     * 设置默认背景
     */
    private void setDefaultImageBg() {
        if (!mTopBgIsDefault) {
            mTopBgIsDefault = true;
            mRlTop.setBackgroundResource(R.drawable.bg_nav_panel);
            tv_nick.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    class dynamicAdapter extends CommonAdapter<Info> {
        List<String> urlsList = new ArrayList<String>();
        public dynamicAdapter(Context context, List<Info> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Info info, int position) {
            ImageView userimg = viewHolder.getViewById(R.id.iv_photoImg);
            xUtilsImageUtils.display(userimg, HttpUtils.host_dynamic + info.getUser().getUserimg(), true);
            TextView infoContent = viewHolder.getViewById(R.id.tv_infoContent);
            infoContent.setText(info.getInfoContent());
            TextView infoDate = viewHolder.getViewById(R.id.tv_infoDate);
            SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm:ss ");
            String date = formatter.format(info.getInfoDate());
            infoDate.setText(date);
            NineGridTestLayout nineGridTestLayout= viewHolder.getViewById(R.id.gridview);
            if (judje.get(position)==null){
                if(info.getInfoPhotoImg()!=null &&!info.getInfoPhotoImg().equals("")){
                    Log.i("convert",info.getInfoPhotoImg());
                    judje.put(position,true);
                    grid.put(position,info.getInfoPhotoImg());
                }else{
                    Log.i("convert",position+"meiyou");
                    judje.put(position,false);
                    grid.put(position,"");
                }
            }

            if (judje.get(position)) {
                urlsList.clear();
                String[] imgs = null;
                imgs = grid.get(position).split(",");
                if (imgs.length > 0) {
                    for (int i = 0; i < imgs.length; i++) {
                        urlsList.add(HttpUtils.host_dynamic + imgs[i]);
                    }
                    nineGridTestLayout.setUrlList(urlsList);
                    nineGridTestLayout.setIsShowAll(info.isShowAll);
                }
            } else {
                Log.i("convert", position + "false");
                urlsList.clear();
//                nineGridTestLayout.setUrlList(urlsList);
//                nineGridTestLayout.setIsShowAll(info.isShowAll);
//                nineGridTestLayout.notifyDataSetChanged();
            }

        }
    }

    private void getData() {
        RequestParams params = new RequestParams(HttpUtils.host_dynamic + "queryinfobyuserid");
        params.addBodyParameter("userid", MainActivity.getUser().getUserid()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("oneactivity", "onSuccess:result " + result);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Info>>() {
                }.getType();
                List<Info> info = gson.fromJson(result, type);
                Log.i("oneactivity", "onSuccess: " + info);
                infoList.clear();
                infoList.addAll(info);
                if(mineadapter == null){
                mineadapter = new dynamicAdapter(getApplicationContext(),infoList,R.layout.minedynamic_item);
                    ptzlv_container.setAdapter(mineadapter);}
                else{
                    mineadapter.notifyDataSetChanged();
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
}


