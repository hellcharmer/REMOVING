package com.example.charmer.moving.friendchat;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.User;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/27.
 */

public class Fragment_search2 extends mBaseFragment{
    private String content;
    private ProgressBar progress;
    private List<User> list=new ArrayList<User>();
    private CommonAdapter<User> madapter;
    private ListView lv_people;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search2, null);
        content=SearchTalk.getContent();
        return v;
    }

    @Override
    public void initView() {
        lv_people = ((ListView) getView().findViewById(R.id.lv_people));
        progress = ((ProgressBar) getView().findViewById(R.id.progress));
        //根据账号名字查询
        RequestParams requestParams=new RequestParams(HttpUtils.host4+"getuser");
        requestParams.addQueryStringParameter("choice",2+"");
        requestParams.addQueryStringParameter("queryname",content);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                list.clear();
                progress.setVisibility(View.GONE);
                Log.i("query",result);
                Gson gson=new Gson();
                Type type = new TypeToken<List<User>>() {
                }.getType();
                List<User> newlist=gson.fromJson(result,type);
                System.out.println("====================="+result);
                if (result.equals("[]")){
                    Toast.makeText(getActivity(),"地球上没有这个人(╯-╰)",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"找到啦(/≥▽≤)/~",Toast.LENGTH_SHORT).show();
                list.addAll(newlist);
                 madapter=new UserAdapter(getActivity(),list,R.layout.searchpeople);
                lv_people.setAdapter(madapter);}
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initEvent() {
        lv_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加");
                builder.setMessage("添加此人为好友？");

                builder.setIcon(R.drawable.qun);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"再见吧",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PostFriend post=new PostFriend();

                        post.postfriend(MainActivity.getUser().getUserid(),list.get(position).getUserid(),list.get(position).getUsername());
                        Toast.makeText(getActivity(),"已经通知他啦",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNeutralButton("再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"好的",Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public void initData() {

    }
    //自定义适配器
    class UserAdapter extends CommonAdapter<User> {
        public UserAdapter(Context context, List<User> lists, int layoutId) {
            super(context, lists, layoutId);
        }
        @Override
        public void convert(ViewHolder viewHolder, User user, int position) {
            TextView tv_usernae=((TextView) viewHolder.getViewById(R.id.tv_userna));
            tv_usernae.setText(user.getUsername());
            TextView tv_acc=((TextView) viewHolder.getViewById(R.id.tv_acc));
            tv_acc.setText("("+user.getUseraccount()+")");
            ImageView iv_sex=((ImageView) viewHolder.getViewById(R.id.iv_sex));
            boolean sex=user.getUsersex();
            if(sex){
             iv_sex.setImageResource(R.drawable.male);
            }else{
             iv_sex.setImageResource(R.drawable.female);
            }
           ImageView iv_tou= ((ImageView) viewHolder.getViewById(R.id.iv_tou));

            xUtilsImageUtils.display(iv_tou, HttpUtils.host4 +"upload/"+ user.getUserimg(), true);
        }
    }
}
