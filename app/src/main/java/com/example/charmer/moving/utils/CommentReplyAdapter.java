package com.example.charmer.moving.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.charmer.moving.R;
import com.example.charmer.moving.pojo.Reply;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CommentReplyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private View.OnClickListener replyToReplyListener;
    private int parentPosition = -1;
    private List<Reply> replyList;

    public CommentReplyAdapter(Context context, List<Reply> replyList,
                               int parentPosition, View.OnClickListener replyToReplyListener) {
        this.inflater = LayoutInflater.from(context);
        this.parentPosition = parentPosition;
        this.replyToReplyListener = replyToReplyListener;
        this.replyList = replyList;
    }

    @Override
    public int getCount() {
        return replyList.size();
    }

    public void clearList() {
        this.replyList.clear();
    }

    public void updateList(List<Reply> replyList) {
        this.replyList.addAll(replyList);
    }

    @Override
    public Reply getItem(int position) {
        return replyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_comment_reply, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_comment_reply_text = (TextView) convertView
                    .findViewById(R.id.tv_comment_reply_text);
            viewHolder.tv_comment_reply_writer = (TextView) convertView
                    .findViewById(R.id.tv_comment_reply_writer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        Reply reply = getItem(position);
        viewHolder.tv_comment_reply_text.setText(reply.getContent());
        // 判断是回复楼主或者回复其他人
        if (TextUtils.isEmpty(reply.getReplyTo())) {
            viewHolder.tv_comment_reply_writer.setText(reply.getUsername());
        } else {
            int start = reply.getUsername().length();
            String text = reply.getUsername() + "回复" + reply.getReplyTo();
            SpannableStringBuilder ss = new SpannableStringBuilder(text);
            // 设置指定位置文字的背景颜色（将“回复”设置成灰色）
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#5A5A5A")),
                    start, start + 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            viewHolder.tv_comment_reply_writer.setText(ss);
        }
        // 记录发表回复时，对应的position,用于确定所回复的对象，如果是楼中楼，还需记录父节点的position
        convertView.setTag(R.id.tag_first, parentPosition);
        convertView.setTag(R.id.tag_second, position);
        convertView.setOnClickListener(replyToReplyListener);
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_comment_reply_writer; // 评论者昵称
        private TextView tv_comment_reply_text; // 评论 内容
    }
}
