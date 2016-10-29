package com.example.charmer.moving.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.charmer.moving.MyView.MyListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.pojo.Comment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/26.
 */

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private Context context;
    private View.OnClickListener replyToCommentListener;
    private View.OnClickListener replyToReplyListener;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList,
                          View.OnClickListener replyToCommentListener,
                          CommentReplyAdapter myAdapter, View.OnClickListener replyToReplyListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.commentList = new ArrayList<Comment>();
        this.commentList.addAll(commentList);
        this.replyToCommentListener = replyToCommentListener;
        this.replyToReplyListener = replyToReplyListener;
    }

    public void clearList() {
        this.commentList.clear();
    }

    public void updateList(List<Comment> commentList) {
        this.commentList.addAll(commentList);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Comment getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_user_photo = (ImageView) convertView
                    .findViewById(R.id.iv_user_photo);
            viewHolder.tv_user_name = (TextView) convertView
                    .findViewById(R.id.tv_user_name);
            viewHolder.tv_user_comment = (TextView) convertView
                    .findViewById(R.id.tv_user_comment);
            viewHolder.btn_comment_reply = (TextView) convertView
                    .findViewById(R.id.tv_user_reply);
            viewHolder.lv_user_comment_replys = (MyListView) convertView.findViewById(R.id.lv_user_comment_replys);
            viewHolder.btn_comment_reply.setTag(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = getItem(position);
        viewHolder.tv_user_name.setText(comment.getUsername());
        viewHolder.tv_user_comment.setText(comment.getContent());
        // 设置评论列表的点击效果透明
        viewHolder.lv_user_comment_replys.setSelector(new ColorDrawable(
                Color.TRANSPARENT));
        //判断当前评论是否有回复
        if (commentList.get(position).getReplyList() != null
                && commentList.get(position).getReplyList().size() != 0) {
            viewHolder.lv_user_comment_replys
                    .setAdapter(new CommentReplyAdapter(context, commentList
                            .get(position).getReplyList(), position,
                            replyToReplyListener));
        }
        //记录点击回复按钮时对应的position,用于确定所回复的对象
        viewHolder.btn_comment_reply.setTag(position);
        viewHolder.btn_comment_reply.setOnClickListener(replyToCommentListener);
        return convertView;
    }

    public class ViewHolder {
        private ImageView iv_user_photo; // 评论者 头像
        private TextView tv_user_name; // 评论者 昵称
        private TextView tv_user_comment; // 评论者 一级品论内容
        // private TextView tv_user_comment_date; //
        private TextView btn_comment_reply; // 评论者 二级评论按钮
        private MyListView lv_user_comment_replys; // 评论者 二级品论内容列表
    }
}
