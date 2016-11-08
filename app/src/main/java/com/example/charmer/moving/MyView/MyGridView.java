package com.example.charmer.moving.MyView;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

import com.example.charmer.moving.fragment.Fragment_home;

/**
 * 自定义gridview，解决ListView中嵌套gridview显示不正常的问题（1行半）
 * @author wangyx
 * @version 1.0.0 2012-9-14
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Fragment_home.exerciseList.size()* 450,
                MeasureSpec.EXACTLY);
        super.onMeasure(expandSpec, heightMeasureSpec);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }
}