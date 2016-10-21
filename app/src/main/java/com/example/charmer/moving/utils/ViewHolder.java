package com.example.charmer.moving.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dliu on 2016/9/30.
 */
public class ViewHolder {

    View convertView;
    SparseArray<View> sparseArray;//key:int;value:View


    //返回viewholder关联的convertview
    public View getConvertView(){
        return  convertView;
    }


    public ViewHolder(Context context, int layoutId, ViewGroup parent){

        //解析布局文件

          this.convertView= LayoutInflater.from(context).inflate(layoutId,null);

          convertView.setTag(this);

           sparseArray=new SparseArray<View>();

    }


    //获取viewholder对象
    public static ViewHolder get(Context context, int layoutId, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if(convertView==null) {
            viewHolder= new ViewHolder(context,layoutId,parent);//创建对象
        }else{
            viewHolder= (ViewHolder) convertView.getTag();//获取viewholder
        }
        return viewHolder;
    }

    //根据id查找view
    public <T extends View> T getViewById(int resourceId){
       View v= sparseArray.get(resourceId);
        //没有找到view
        if(v==null){
           v= convertView.findViewById(resourceId);
            sparseArray.put(resourceId,v);
        }
        return (T)v;
    }


}
