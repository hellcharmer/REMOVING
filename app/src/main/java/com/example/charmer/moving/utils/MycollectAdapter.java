package com.example.charmer.moving.utils;


import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.ZixunInfo;

import java.util.List;

/**
 * Created by loongggdroid on 2016/5/19.
 */
public class MycollectAdapter extends RecyclerView.Adapter<MycollectAdapter.ViewHolder> {
    // 数据集
    private List<ZixunInfo> exerciseList ;

    public MycollectAdapter(List<ZixunInfo> exerciseList) {
        super();
        this.exerciseList = exerciseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView

        View view = View.inflate(viewGroup.getContext(), R.layout.search_result_list, null);

        // 创建一个ViewHolder

        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        // 绑定数据到ViewHolder上
        ZixunInfo zixun = exerciseList.get(i);
        viewHolder.tv_xiangxi.setText(zixun.getZixun_likes()+"人收藏 ·"+zixun.getPublisher()+" · "+ DateUtils.getGapTimeFromNow(zixun.getZixun_issuedate()));
        viewHolder.tv_name.setText(zixun.getZixun_name());
        xUtilsImageUtils.display(viewHolder.iv_picture, HttpUtils.hoster + "zixunpictures"+zixun.getZixun_photo().split(",")[0]);

    }

    @Override
    public int getItemCount() {

        return exerciseList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_picture;
        TextView tv_xiangxi;
        TextView tv_name;

        public ViewHolder(View itemView) {

            super(itemView);

            iv_picture = ((ImageView) itemView.findViewById(R.id.iv_searchresult_picture));
            tv_xiangxi = ((TextView) itemView.findViewById(R.id.tv_searchresult_tg));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_searchresult_title));
            //字体加粗
            TextPaint tp = tv_name.getPaint();
            tp.setFakeBoldText(true);

        }

    }

}



