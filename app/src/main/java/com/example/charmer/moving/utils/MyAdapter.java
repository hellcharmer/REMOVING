package com.example.charmer.moving.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.charmer.moving.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/17.
 */
public class MyAdapter extends BaseAdapter {
    private ArrayList<Bitmap> arrayList;
    private Context context;
    private int screenW;
    private int screenH;
    DisplayMetrics display;
    public MyAdapter(Context context, ArrayList<Bitmap> bmpList){
        this.context=context;
        this.arrayList=bmpList;
        screenW = context.getResources().getDisplayMetrics().widthPixels/4;
        screenH = context.getResources().getDisplayMetrics().heightPixels/4;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    //设置设配器数据
    public void setDate(ArrayList<Bitmap> bmpList){
        this.arrayList=bmpList;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= View.inflate(context, R.layout.activity_publish_img_item, null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(screenW, screenH));
        //绑定图片原始尺寸，方便以后应用
        int [] parameter={arrayList.get(position).getWidth(),arrayList.get(position).getHeight()};
        viewHolder.imageView.setTag(parameter);
        viewHolder.imageView.setImageBitmap(arrayList.get(position));

        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
    }
}
