package com.example.charmer.moving.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.pojo.Friend;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2016/10/11.
 */
public class Fragment_friend extends Fragment implements View.OnClickListener {
    @InjectView(R.id.vp_friend)
    ViewPager vpFriend;
    @InjectView(R.id.iv_touxiang)
    ImageView ivTouxiang;
    @InjectView(R.id.tv_talk)
    TextView tvTalk;
    @InjectView(R.id.iv_goodfriend)
    ImageView ivGoodfriend;
    @InjectView(R.id.rl_talk)
    RelativeLayout rlTalk;
    @InjectView(R.id.iv_talk)
    ImageView ivTalk;
    @InjectView(R.id.tv_goodfriend)
    TextView tvGoodfriend;
    @InjectView(R.id.iv_qun)
    ImageView ivQun;
    @InjectView(R.id.rl_friend)
    RelativeLayout rlFriend;
    @InjectView(R.id.iv_goodfriend1)
    ImageView ivGoodfriend1;
    @InjectView(R.id.tv_qun)
    TextView tvQun;
    @InjectView(R.id.iv_tlz)
    ImageView ivTlz;
    @InjectView(R.id.rl_qun)
    RelativeLayout rlQun;
    @InjectView(R.id.iv_qun1)
    ImageView ivQun1;
    @InjectView(R.id.tv_tlz)
    TextView tvTlz;
    @InjectView(R.id.iv_talk1)
    ImageView ivTalk1;
    @InjectView(R.id.rl_tlz)
    RelativeLayout rlTlz;
    @InjectView(R.id.iv_search)
    ImageView ivSearch;
    @InjectView(R.id.ll_sreach)
    LinearLayout llSreach;
    private View rl_talk;
    private ImageView iv_talk;
    private ImageView iv_goodfriend;
    private View rl_friend;
    private View rl_qun;
    private View rl_tlz;
    private ImageView iv_talk1;
    private ListView listView;
    private PagerAdapter pagerAdapter;
    private List<View> views= new ArrayList<View>();
    private List mDatas;
    CommonAdapter<Friend> goodfriendad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, null);
        vpFriend = (ViewPager) view.findViewById(R.id.vp_friend);
        initdata();
        initview();
        initEnt();


        ButterKnife.inject(this, view);
        return view;
    }

    private void initdata() {



    }
    private void initEnt(){
    }
    private void initview() {

        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
       View v1=layoutInflater.inflate(R.layout.activity_friend_talk,null);
        View v2=layoutInflater.inflate(R.layout.activity_friend_goodfriend,null);
        View v3=layoutInflater.inflate(R.layout.activity_friend_qun,null);
        View v4=layoutInflater.inflate(R.layout.activity_friend_tlzs,null);
        //好友listview处理
        listView= ((ListView) v1.findViewById(R.id.lv_talks));
       goodfriendad= new CommonAdapter<Friend>(getActivity(),mDatas,R.layout.activity_friend_talk_layout) {
            @Override
            public void convert(ViewHolder viewHolder, Friend fd, int position) {
                Log.i("alzy","dasdasdasd");
              TextView tv=viewHolder.getViewById(R.id.tlk);
                tv.setText("1");
            }
        };
        listView.setAdapter(goodfriendad);
        views.add(v1);
        views.add(v2);
        views.add(v3);
        views.add(v4);
        pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view= views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        };
        vpFriend.setAdapter(pagerAdapter);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_touxiang, R.id.iv_goodfriend, R.id.iv_talk, R.id.iv_qun, R.id.iv_goodfriend1, R.id.iv_tlz, R.id.iv_qun1, R.id.iv_talk1, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_touxiang:
                break;
            case R.id.iv_goodfriend:
                break;
            case R.id.iv_talk:
                break;
            case R.id.iv_qun:
                break;
            case R.id.iv_goodfriend1:
                break;
            case R.id.iv_tlz:
                break;
            case R.id.iv_qun1:
                break;
            case R.id.iv_talk1:
                break;
            case R.id.iv_search:
                break;
        }
    }
}
