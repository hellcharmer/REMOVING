package com.example.charmer.moving.friendchat;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.example.charmer.moving.MainActivity;
import com.example.charmer.moving.contantData.HttpUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.example.charmer.moving.MainActivity.user;

/**
 * Created by lenovo on 2016/11/7.
 */

public class CreateQunImpl {
    public static Activity B;
    private String Token;
    private RongIMClient.CreateDiscussionCallback callback;
    private List<String> userids;
    private String title;
    private String users="";

    public CreateQunImpl(){}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void connectRong(final List<String> userids, final String title){
        for (int i=0;i<userids.size();i++){
            users=users+userids.get(i)+",";
        }
        Log.i("users:",users);
        Token= MainActivity.getToken();
        //链接融云
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("onSuccess:","hhhhh");
                RongIM.getInstance().createDiscussionChat(B,userids,title, new RongIMClient.CreateDiscussionCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.i("connectRong:",s);
                        RequestParams params=new RequestParams(HttpUtils.host4+"qunservlet");
                        params.addQueryStringParameter("choice",1+"");
                        params.addQueryStringParameter("tlzid",s);
                        params.addQueryStringParameter("tlzname",title);
                        params.addQueryStringParameter("tlzusers",users);
                        x.http().get(params, new Callback.CommonCallback<String>() {
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

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
//                bb61a435-021c-4fa9-8650-288b017e3024
//                RongIM.getInstance().startDiscussionChat(B, "bb61a435-021c-4fa9-8650-288b017e3024", "标题");
                RongIM.getInstance().refreshUserInfoCache(new io.rong.imlib.model.UserInfo(user.getUserid() + "",user.getUsername(), Uri.parse(HttpUtils.host4 + user.getUserimg())));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("errorr:",errorCode+"");
            }

            @Override
            public void onTokenIncorrect() {
                Log.e("errorr:","tokencuowu");
            }

            @Override
            public void onCallback(String s) {
                super.onCallback(s);

            }
        });

    }
  public void joinone(final String userid, final String tlzid) {
      Conntent();
      List<String> userids=new ArrayList<String>();
      /**
       * 添加一名或者一组用户加入讨论组。
       *
       * @param discussionId 讨论组 Id。
       * @param userIdList   邀请的用户 Id 列表。
       * @param callback     执行操作的回调。
       */
      RongIM.getInstance().getRongIMClient().addMemberToDiscussion(tlzid,userids , new RongIMClient.OperationCallback() {

          @Override
          public void onSuccess() {
          RequestParams requestParams=new RequestParams(HttpUtils.host4+"qunservlet");
              requestParams.addQueryStringParameter("choice",2+"");
              requestParams.addQueryStringParameter("userid",userid);
              requestParams.addQueryStringParameter("tlzid",tlzid);
              x.http().get(requestParams, new Callback.CommonCallback<String>() {
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

          @Override
          public void onError(RongIMClient.ErrorCode errorCode) {

          }
      });
    }
  public void removeone(final String tlzid, final String userid){
      Conntent();
      /**
       * 供创建者将某用户移出讨论组。
       *
       * 移出自己或者调用者非讨论组创建者将产生 {@link RongIMClient.ErrorCode#UNKNOWN} 错误。
       *
       * @param discussionId 讨论组 Id。
       * @param userId       用户 Id。
       * @param callback     执行操作的回调。
       */
      RongIM.getInstance().getRongIMClient().removeMemberFromDiscussion(tlzid,userid, new RongIMClient.OperationCallback() {
          @Override
          public void onSuccess() {
              RequestParams requestParams=new RequestParams(HttpUtils.host4+"qunservlet");
              requestParams.addQueryStringParameter("choice",3+"");
              requestParams.addQueryStringParameter("userid",userid);
              requestParams.addQueryStringParameter("tlzid",tlzid);
              x.http().get(requestParams, new Callback.CommonCallback<String>() {
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
          @Override
          public void onError(RongIMClient.ErrorCode errorCode) {

          }
      });
  }
    private void Conntent(){
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
            }
            @Override
            public void onSuccess(String userId) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
}

