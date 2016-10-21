package com.example.charmer.moving.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by asus on 2016/10/18.
 */

public class GridView_picture extends GridView {
    public GridView_picture(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView_picture(Context context) {
        super(context);
    }

    public GridView_picture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }


}
