package com.example.charmer.moving.friendchat;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Qun;
import com.example.charmer.moving.utils.CommonAdapter;
import com.example.charmer.moving.utils.ViewHolder;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/27.
 */

public class Fragment_search3 extends mBaseFragment {
   private String content;
    private List<Qun> quns=new ArrayList<Qun>();
    CommonAdapter<Qun> qunad;
    private ListView search3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search3, null);
        content=SearchTalk.getContent();
        search3 = ((ListView) v.findViewById(R.id.search3));
        return v;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        RequestParams params=new RequestParams(HttpUtils.host4+"getqun");//改动
        params.addQueryStringParameter("content",content);
        params.addQueryStringParameter("choice",1+"");//改动
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                quns.clear();
                Log.i( "qunonSuccess:",result);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Qun>>(){}.getType();
                List<Qun> newquns=gson.fromJson(result,type);
                if (result.equals("[]")){
                    Toast.makeText(getActivity(),"该组织太过神秘╮(╯3╰)╭",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"找到组织啦~(￣▽￣)~",Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = ( InputMethodManager ) getView().getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    if ( imm.isActive( ) ) {
                        imm.hideSoftInputFromWindow( getView().getApplicationWindowToken( ) , 0 );
                    }
                    quns.addAll(newquns);
                    qunad = new QunAd(getActivity(), quns, R.layout.fragment_search3_layout);
                    search3.setAdapter(qunad);
                }
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
    class QunAd extends  CommonAdapter<Qun> {
        public QunAd(Context context, List<Qun> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, final Qun qun, int position) {
            final FoldingCell fc = (FoldingCell) viewHolder.getViewById(R.id.folding_cell);
            // attach click listener to folding cell
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fc.toggle(false);
                }
            });
           ImageView qunimg= ((ImageView) viewHolder.getViewById(R.id.iv_qunimg));
            xUtilsImageUtils.display(qunimg, qun.getQunimg(), false);
            TextView tvqunname=((TextView) viewHolder.getViewById(R.id.tvqunname));
            tvqunname.setText(qun.getQunname());
            ImageView bigqun= ((ImageView) viewHolder.getViewById(R.id.bigqun));
            xUtilsImageUtils.display(bigqun, qun.getQunimg(), false);
            TextView tv_bingqunname=((TextView) viewHolder.getViewById(R.id.tv_bingqunname));
            tv_bingqunname.setText(qun.getQunname());
           ImageView gou= ((ImageView) viewHolder.getViewById(R.id.gou));
            gou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"回去等消息吧 ~zZ",Toast.LENGTH_SHORT).show();
                    fc.toggle(false);
                    RequestParams reparams=new RequestParams(HttpUtils.host4+"insertpost");
                    reparams.addQueryStringParameter("userid", MainActivity.getUser().getUserid()+"");
                    reparams.addQueryStringParameter("getid", qun.getQunid()+"");
                    reparams.addQueryStringParameter("getname", qun.getQunname());
                    reparams.addQueryStringParameter("state", 1+"");
                    x.http().get(reparams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

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
            });
        }
    }
}
