package com.example.charmer.moving.friendchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.pojo.Friend;
import com.example.charmer.moving.pojo.Qun;
import com.example.charmer.moving.pojo.TLZ;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

import static com.example.charmer.moving.R.id.iv_userimg;
import static com.example.charmer.moving.R.id.tv_qunname;

public class ConversationActivity extends FragmentActivity {
    @InjectView(iv_userimg)
    ImageView ivUserimg;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.haoyou)
    RelativeLayout haoyou;
    @InjectView(R.id.qunuserimg)
    ImageView qunuserimg;
    @InjectView(tv_qunname)
    TextView tvQunname;

    @InjectView(R.id.tlzimg)
    ImageView tlzimg;
    @InjectView(R.id.tlzname)
    TextView tlzname;
    @InjectView(R.id.tlz)
    RelativeLayout tlz;
    private Friend fd;
    private Qun qu;
    private TLZ tlzinfo;
    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private RelativeLayout qun;
    private ImageView tuiqun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.inject(this);
        qun = ((RelativeLayout) findViewById(R.id.qun));
//        mTargetIds=getIntent().getData().getQueryParameter("targetIds");
//        Log.i("typeid:",mTargetIds);
        tuiqun = ((ImageView) findViewById(R.id.tuiqun));
        mTargetId = getIntent().getData().getQueryParameter("targetId");

        Log.i("typeid:", mTargetId);
        String type = getIntent().getData().getPath();
        Log.i("typeis", type);

        if (type.equals("/conversation/private")) {
            RequestParams requestParams = new RequestParams(HttpUtils.host4 + "friendservlet");
            requestParams.addQueryStringParameter("choice", 2+"");
            requestParams.addQueryStringParameter("friendid", mTargetId);
            requestParams.addQueryStringParameter("userid", MainActivity.getUser().getUserid()+"");
            x.http().get(requestParams, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Log.i("friendsuccess", result);
                    fd = new Friend();
                    Gson gson = new Gson();
                    Type type = new TypeToken<Friend>() {
                    }.getType();
                    fd = gson.fromJson(result, type);
                    haoyou.setVisibility(View.VISIBLE);
                    xUtilsImageUtils.display(ivUserimg, HttpUtils.host4 + fd.getFriendimg(), true);
                    username.setText(fd.getUser().getUsername());
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
        else if (type.equals("/conversation/group")) {
            tuiqun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 RequestParams params=new RequestParams(HttpUtils.host4+"qunservlet");
                    params.addQueryStringParameter("choice",4+"");
                    params.addQueryStringParameter("userid",MainActivity.getUser().getUserid()+"");
                    params.addQueryStringParameter("qunid",mTargetId);
                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                         finish();
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
            Log.i("typeis", type);
            Log.i("typeis:", mTargetId);
            RequestParams requestParams = new RequestParams(HttpUtils.host4 + "getqun");//改动
            requestParams.addQueryStringParameter("choice", 2+"");
            requestParams.addQueryStringParameter("qunid", mTargetId);
            x.http().get(requestParams, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Log.i("qusuccess", result);
                    qu = new Qun();
                    Gson gson = new Gson();
                    Type type = new TypeToken<Qun>() {
                    }.getType();
                    qu = gson.fromJson(result, type);
                    qun.setVisibility(View.VISIBLE);
                    xUtilsImageUtils.display(qunuserimg,qu.getQunimg(),false);
                    tvQunname.setText(qu.getQunname());
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
        else if(type.equals("/conversation/discussion")){

            RequestParams requestParams = new RequestParams(HttpUtils.host4 + "getqun");
            requestParams.addQueryStringParameter("choice", 3+"");
            requestParams.addQueryStringParameter("tlzid", mTargetId);
            x.http().get(requestParams, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Log.i("qusuccess", result);
                    tlzinfo = new TLZ();
                    Gson gson = new Gson();
                    Type type = new TypeToken<TLZ>() {
                    }.getType();
                    tlzinfo = gson.fromJson(result, type);
                    tlz.setVisibility(View.VISIBLE);
                    tlzname.setText(tlzinfo.getTlzname());
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

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                Toast.makeText(ConversationActivity.this, "长按", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }
}
