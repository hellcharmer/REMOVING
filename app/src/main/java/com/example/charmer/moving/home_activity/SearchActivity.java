package com.example.charmer.moving.home_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmer.moving.MyView.LoadMoreListView;
import com.example.charmer.moving.R;
import com.example.charmer.moving.contantData.HttpUtils;
import com.example.charmer.moving.contantData.ToastUtil;
import com.example.charmer.moving.db.SearchHistorysDao;
import com.example.charmer.moving.pojo.ListActivityBean;
import com.example.charmer.moving.utils.DateUtils;
import com.example.charmer.moving.utils.xUtilsImageUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import me.weyye.library.EmptyLayout;


public class SearchActivity extends Activity implements OnClickListener {
	private EmptyLayout emptyLayout;
	private EditText et_search_keyword;

    private LoadMoreListView search_result;
	private RelativeLayout rl_search_result;
	private Button btn_search;
	private TextView tv_back;
	private TextView tv_clear;
	private LinearLayout ll_content;
	private ListView lv_history_word;

	private SearchHistorysDao dao;
	private ArrayList<SearchHistorysBean> historywordsList = new ArrayList<SearchHistorysBean>();
	private SearchHistoryAdapter mAdapter;
	private int count;
	int page_zixun = 1;//第一页
	int totalpage_zixun=0;//总页数
	MyAdapter adapter;
	private List<ListActivityBean.Zixun> zixunlist = new ArrayList<ListActivityBean.Zixun>();
	String word=null;
	ToastUtil toastUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		dao = new SearchHistorysDao(this);
		historywordsList = dao.findAll();
		//自定义toast布局

		View v=View.inflate(SearchActivity.this,R.layout.layout_toast_view,null);
		toastUtil=new ToastUtil(SearchActivity.this,v,200);
		initView();
		emptyLayout.bindView(search_result);
		search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				toastUtil.Short(SearchActivity.this,"这是第"+(position)+"个").show();
				Intent intent = new Intent(SearchActivity.this, ZixunInfo_xq.class);
				intent.putExtra("zixunId",zixunlist.get(position).zixunId+"");
				startActivity(intent);
			}
		});
		search_result.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
			@Override
			public void onloadMore() {

				loadZixunMore();
			}
		});
	}

	private void initView() {
		et_search_keyword = (EditText) findViewById(R.id.et_search_keyword);

		et_search_keyword.setOnClickListener(this);
		search_result = (LoadMoreListView)findViewById(R.id.search_result);
		rl_search_result = (RelativeLayout)findViewById(R.id.rl_search_result);
		emptyLayout = (EmptyLayout)findViewById(R.id.emptyLayout);

		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);

		tv_clear = (TextView) findViewById(R.id.tv_clear);
		tv_clear.setOnClickListener(this);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		lv_history_word = (ListView) findViewById(R.id.lv_history_word);
		mAdapter = new SearchHistoryAdapter(historywordsList);
		count = mAdapter.getCount();
		if (count > 0) {

			tv_clear.setVisibility(View.VISIBLE);
		} else {
			tv_clear.setVisibility(View.GONE);
		}
		lv_history_word.setAdapter(mAdapter);
		lv_history_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				SearchHistorysBean bean = (SearchHistorysBean) mAdapter.getItem(position);

				et_search_keyword.setText(bean.historyword);

				mAdapter.notifyDataSetChanged();
				btn_search.performClick();
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.tv_back:
				finish();
				break;
			case R.id.btn_search:// 点击搜索，先拿关键词
				String searchWord = et_search_keyword.getText().toString().trim();
				if (TextUtils.isEmpty(searchWord)) {
					Toast.makeText(this, "搜索词不能为空！",Toast.LENGTH_SHORT).show();
				} else {
					// 存储数据
					dao.addOrUpdate(searchWord);
					historywordsList.clear();
					historywordsList.addAll(dao.findAll());
					mAdapter.notifyDataSetChanged();
					tv_clear.setVisibility(View.VISIBLE);

					//转换页面：

					word = et_search_keyword.getText().toString().trim();
					adapter=new MyAdapter(zixunlist);
					search_result.setAdapter(adapter);
					getSearchZixunlist(page_zixun,word);
					ll_content.setVisibility(View.GONE);

					rl_search_result.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.tv_clear:
				// 点击清除历史的时候：清除数据库中所有的数据
				dao.deleteAll();
				historywordsList.clear();
				mAdapter.notifyDataSetChanged();
				tv_clear.setVisibility(View.GONE);
				break;
			case R.id.et_search_keyword:
				ll_content.setVisibility(View.VISIBLE);
				rl_search_result.setVisibility(View.GONE);

				break;
			default:
				break;

		}

	}
	private void loadZixunMore() {

		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(page_zixun<totalpage_zixun) {

					getSearchZixunlist(++page_zixun,word);
				}else {
					Toast.makeText(SearchActivity.this,"已经到底了！",Toast.LENGTH_SHORT).show();
				}

				SearchActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {

						search_result.setLoadCompleted();
					}
				});
			}
		}.start();
	}
	private void getSearchZixunlist(int page,String tv_word) {
		emptyLayout.showLoading("正在加载，请稍后");
		RequestParams params = new RequestParams(HttpUtils.hoster+"searchzixun");
		params.addQueryStringParameter("page",page+"");
		params.addQueryStringParameter("tv_word",tv_word);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				ListActivityBean bean=gson.fromJson(result, ListActivityBean.class);
				totalpage_zixun = bean.page;


				//System.out.println(bean.status);
				//System.out.println(bean.zixunlist.size());
				//Log.i(TAG,bean.status+"---------");
				//Log.i(TAG,bean.zixunlist.size()+"---------");
				if(page_zixun==1){


					zixunlist.clear();
				}
				//zixunlist.clear();
				zixunlist.addAll(bean.zixunlist);
				if(zixunlist.size()==0){
					emptyLayout.showError("暂无数据！"); // 显示失败
				}else {
					//成功
					emptyLayout.showSuccess();
				}
				//通知listView更新界面
				adapter.notifyDataSetChanged();

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
	class SearchHistoryAdapter extends BaseAdapter {

		private ArrayList<SearchHistorysBean> historywordsList;

		public SearchHistoryAdapter(ArrayList<SearchHistorysBean> historywordsList) {
			this.historywordsList = historywordsList;
		}

		@Override
		public int getCount() {

			return historywordsList == null ? 0 : historywordsList.size();
		}

		@Override
		public Object getItem(int position) {

			return historywordsList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_history_word, null);
				holder.tv_word = (TextView) convertView.findViewById(R.id.tv_search_record);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_word.setText(historywordsList.get(position).toString());

			return convertView;
		}

	}
	private class MyAdapter extends BaseAdapter {
		private List<ListActivityBean.Zixun> list;

		public MyAdapter( List<ListActivityBean.Zixun> list) {
			this.list= list;
		}
		@Override
		public int getCount() {
			// System.out.println(zixunlist.size());
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.i("TAG", "加载ListView item_position:" + position);







			//打气筒
			ViewHolder viewHolder=null;

			final ListActivityBean.Zixun zixun = list.get(position);

			if(convertView==null){
				viewHolder =new ViewHolder();

				convertView = View.inflate(SearchActivity.this, R.layout.search_result_list, null);
				viewHolder.iv_picture = ((ImageView) convertView.findViewById(R.id.iv_searchresult_picture));
				viewHolder.tv_xiangxi = ((TextView) convertView.findViewById(R.id.tv_searchresult_tg));
				viewHolder.tv_name = ((TextView) convertView.findViewById(R.id.tv_searchresult_title));
				//字体加粗
				TextPaint tp = viewHolder.tv_name.getPaint();
				tp.setFakeBoldText(true);

				convertView.setTag(viewHolder);//缓存对象
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}


			try {


				//Log.i("TAG",(URLDecoder.decode(zixun.timeStamp,"utf-8")));
				viewHolder.tv_xiangxi.setText(URLDecoder.decode(zixun.likes+"人收藏 ·"+zixun.publisher+" · "+ DateUtils.getGapTimeFromNow(DateUtils.stringToDate(URLDecoder.decode(zixun.timeStamp,"utf-8"))) ,"utf-8"));
				viewHolder.tv_name.setText(URLDecoder.decode(zixun.title,"utf-8"));



				xUtilsImageUtils.display(viewHolder.iv_picture, HttpUtils.hoster + URLDecoder.decode(zixun.photoImg, "utf-8").split(",")[0]);


				// Log.i("====",position+"=="+zixunlist.get(0).likes);


			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}



			return convertView;
		}


	}
	class ViewHolder {
		TextView tv_word;

		ImageView iv_picture;

		TextView tv_xiangxi;
		TextView tv_name;
	}
}
