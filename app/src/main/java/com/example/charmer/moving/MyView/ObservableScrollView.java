package com.example.charmer.moving.MyView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.example.charmer.moving.home_activity.ZixunInfo_xq;

public class ObservableScrollView extends ScrollView {  
  
    private ZixunInfo_xq.ScrollViewListener scrollViewListener = null;
  
    public ObservableScrollView(Context context) {  
        super(context);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScrollViewListener(ZixunInfo_xq.ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;  
    }  
  
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }  
    }
   /* 防止ScrollView内部重新走了 onLayout / onMeaure 流程 在这个流程中 ScrollView会将自身滚动到 获得 focus 的 child 位置上面
    1.view.requestFocus(); 让界面顶部的某一个View获取focus
    2.grid.setFocusable(false); 让Grid不能获得focus
    3.手动scrollto(0,0)
    4.重写ScrollView中的computeScrollDeltaToGetChildRectOnScreen,让该方法返回0
    */
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {

        return 0;
    }
  
}  
