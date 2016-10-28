package com.example.charmer.moving.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.charmer.moving.R;

/**
 * Created by lenovo on 2016/10/17.
 */
public class Friend_titlebar extends RelativeLayout{
    private ImageView left;
    private ImageView right;
    private TextView toutext;

    public Friend_titlebar(Context context) {
        super(context);
    }

    public Friend_titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.activity_friend_titlebar,this,true);
        left = ((ImageView) view.findViewById(R.id.left));
        right = ((ImageView) view.findViewById(R.id.right));
        toutext = ((TextView) view.findViewById(R.id.toutext));
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.Friend_titlebar);
        int leftres=array.getResourceId(R.styleable.Friend_titlebar_left,R.drawable.tou);
        int rightres=array.getResourceId(R.styleable.Friend_titlebar_rihgt,R.drawable.goodfriend);
        String textres=array.getString(R.styleable.Friend_titlebar_toutext);
        left.setImageResource(leftres);
        right.setImageResource(rightres);
        toutext.setText(textres);

    }

    public Friend_titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setImgLeftOnClickListener(@Nullable OnClickListener l){
        left.setOnClickListener(l);
    }
    public void setImgRightOnClickListener(@Nullable OnClickListener l){
        right.setOnClickListener(l);
    }

}
