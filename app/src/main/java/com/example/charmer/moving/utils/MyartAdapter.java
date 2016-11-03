package com.example.charmer.moving.utils;


import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Exercises;
import com.example.charmer.moving.pojo.ZixunInfo;

import java.util.List;

/**
 * Created by loongggdroid on 2016/5/19.
 */
public class MyartAdapter extends RecyclerView.Adapter<MyartAdapter.ViewHolder> {
    // 数据集
    private List<ZixunInfo> exerciseList ;

    public MyartAdapter(List<ZixunInfo> exerciseList) {
        super();
        this.exerciseList = exerciseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView

        View view = View.inflate(viewGroup.getContext(), R.layout.activity_zi_xun, null);

        // 创建一个ViewHolder

        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        // 绑定数据到ViewHolder上
        ZixunInfo zixun = exerciseList.get(i);
        viewHolder.tv_xiangxi.setText(zixun.getPublisher()+" · "+ DateUtils.getGapTimeFromNow(zixun.getZixun_issuedate()));
        viewHolder.tv_shoucangpinglun.setText(zixun.getZixun_likes()+"人收藏 ·"+zixun.getZixun_pingluns()+"人评论");
        viewHolder.tv_name.setText(zixun.getZixun_name());
        viewHolder.tv_type.setText(zixun.getZixun_type());
        viewHolder.tv_content.setText(zixun.getZixun_text());
        xUtilsImageUtils.display(viewHolder.iv_photo, HttpUtils.hoster+"upload/"+ zixun.getPublisher_img(),true);
        if(zixun.getZixun_photo().equals("")){
            viewHolder.iv_picture.setVisibility(View.GONE);
        }else {
            viewHolder.iv_picture.setVisibility(View.VISIBLE);
            xUtilsImageUtils.display(viewHolder.iv_picture, HttpUtils.hoster + zixun.getZixun_photo().split(",")[0]);
        }

    }

    @Override
    public int getItemCount() {

        return exerciseList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_photo;
        ImageView iv_picture;
        TextView tv_type;
        TextView tv_xiangxi;
        TextView tv_name;
        TextView tv_content;
        TextView tv_shoucangpinglun;

        public ViewHolder(View itemView) {

            super(itemView);

            iv_photo = ((ImageView) itemView.findViewById(R.id.iv_photo ));
            iv_picture = ((ImageView) itemView.findViewById(R.id.iv_picture));
            tv_xiangxi = ((TextView) itemView.findViewById(R.id.tv_xiangxi));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_shoucangpinglun = ((TextView) itemView.findViewById(R.id.tv_shoucangpinglun));
            //字体加粗
            TextPaint tp = tv_name.getPaint();
            tp.setFakeBoldText(true);
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));

        }

    }

}



