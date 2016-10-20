/**
 * 
 */
package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.BuyGoodAdapter;
import com.soul.project.application.bean.Goods;
import com.soul.project.application.dialog.MessageDialog;
import com.yisanguo.app.api.API;

/**
 * @file BuyGoodsList.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年7月5日 下午12:41:12
 */
public class BuyGoodsList extends BaseActivityWithSystemBarColor implements OnItemClickListener, OnClickListener
{
	SwipeMenuListView goodlist;
	Handler mHandler = new Handler();
	Button btnWuQiQiang;
	Button btnWuQiDao;
	Button btnWuQiJian;
	
	Button btnTouKui;
	Button btnKaiJia;
	Button btnPiFeng;
	Button btnYaoDai;
	Button btnXieZi;
	Button btnGoOut;
	Button btnQita;
	
	LinearLayout layoutBar;
	
	List<Goods> wuqiQiang = new ArrayList<Goods>();
	List<Goods> wuqiDao = new ArrayList<Goods>();
	List<Goods> wuqiJian = new ArrayList<Goods>();
	List<Goods> zbPiFeng = new ArrayList<Goods>();
	List<Goods> zbTouKui = new ArrayList<Goods>();
	List<Goods> zbKaiJia = new ArrayList<Goods>();
	List<Goods> zbYaoDai = new ArrayList<Goods>();
	List<Goods> zbXieZi = new ArrayList<Goods>();
	List<Goods> zbQita = new ArrayList<Goods>();
	
	BuyGoodAdapter adapter ;
//	List<Goods> wuqiQiang = new ArrayList<Goods>();
//	List<Goods> wuqiQiang = new ArrayList<Goods>();
	int type = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_goods);
		
		type = getIntent().getIntExtra("type", 1);
		
		loadData(type, 101);
		initView();
	}
	
	private void initView()
	{
		layoutBar = (LinearLayout)this.findViewById(R.id.layoutBar);
		if(type==2)
			layoutBar.setVisibility(View.GONE);
		
		goodlist = (SwipeMenuListView)findViewById(R.id.loaddataview);
		adapter = new BuyGoodAdapter(this, wuqiDao,type);
		goodlist.setAdapter(adapter);
		// 隐藏第一个Item的divider
		goodlist.setHeaderDividersEnabled(false);
		goodlist.setOnItemClickListener(this);
		goodlist.setListViewFooterTextViewColor(getResources().getColor(R.color.black));
		goodlist.setListViewHeaderTextViewColor(getResources().getColor(R.color.black));
		goodlist.setListViewHeaderTimeTextViewColor(getResources().getColor(R.color.gray));
		goodlist.setXListViewListener(new RefreshListener());
		
		btnWuQiDao = (Button)this.findViewById(R.id.btnWuQiDao);
		btnWuQiJian = (Button)this.findViewById(R.id.btnWuQiJIan);
		btnWuQiQiang = (Button)this.findViewById(R.id.btnWuQiQiang);
		btnTouKui = (Button)this.findViewById(R.id.btnTouKui);
		 btnKaiJia  = (Button)this.findViewById(R.id.btnKaiJia);
		 btnPiFeng  = (Button)this.findViewById(R.id.btnPiFeng);
		 btnYaoDai  = (Button)this.findViewById(R.id.btnYaoDai);
		 btnXieZi  = (Button)this.findViewById(R.id.btnXiezi);
		 btnQita   = (Button)this.findViewById(R.id.btnQiTa);
		 
		 btnWuQiDao.setOnClickListener(this);
		 btnWuQiJian.setOnClickListener(this);
		 btnWuQiQiang.setOnClickListener(this);
		 btnTouKui.setOnClickListener(this);
		 btnKaiJia.setOnClickListener(this);
		 btnPiFeng.setOnClickListener(this);
		 btnYaoDai.setOnClickListener(this);
		 btnXieZi.setOnClickListener(this);
		 btnQita.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btnWuQiDao:
				if(wuqiDao != null && wuqiDao.size() > 0)
				{
					adapter.setList(wuqiDao);
				}
				else
					loadData(1, 101);
				break;
			case R.id.btnWuQiJIan:
				if(wuqiJian != null && wuqiJian.size() > 0)
				{
					adapter.setList(wuqiJian);
				}
				else
					loadData(1, 103);
				break;
			case R.id.btnWuQiQiang:
				if(wuqiQiang != null && wuqiQiang.size() > 0)
				{
					adapter.setList(wuqiQiang);
				}
				else
					loadData(1, 102);
				
				break;
			case R.id.btnTouKui:
				if(zbTouKui != null && zbTouKui.size() > 0)
				{
					adapter.setList(zbTouKui);
				}
				else
					loadData(1, 2);
				break;
			case R.id.btnKaiJia:
				if(zbKaiJia != null && zbKaiJia.size() > 0)
				{
					adapter.setList(zbKaiJia);
				}
				else
					loadData(1, 4);				
				break;
			case R.id.btnPiFeng:
				if(zbPiFeng != null && zbPiFeng.size() > 0)
				{
					adapter.setList(zbPiFeng);
				}
				else
					loadData(1, 3);					
				break;
			case R.id.btnYaoDai:
				if(zbYaoDai != null && zbYaoDai.size() > 0)
				{
					adapter.setList(zbYaoDai);
				}
				else
					loadData(1, 5);					
				break;
			case R.id.btnXiezi:
				if(zbXieZi != null && zbXieZi.size() > 0)
				{
					adapter.setList(zbXieZi);
				}
				else
					loadData(1, 6);					
				break;
			case R.id.btnQiTa:
				if(zbQita != null && zbQita.size() > 0)
				{
					adapter.setList(zbQita);
				}
				else
					loadData(1, 7);
			default:
				break;
		}
	}

	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int, int)
	 */
	@Override
	public void loadData(int id, int type)
	{
		String url;
		if(id==1)
			url = API.URL2+"getbuygoodslist.action?&type="+type;
		else
			url = API.URL2+"getselllist.action?uuid="+MyApplication.getUUID(this)+"";
		
		finalHttp.get(url, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(BuyGoodsList.this, "Loding");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				//com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2

				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t != null && !"".equals(t.toString().trim()))
				{
					Gson gson = new Gson();
					wuqiDao = gson.fromJson(t.toString(), new TypeToken<List<Goods>>() {}.getType());
					adapter.setList(wuqiDao);
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		
	}
	
	class RefreshListener implements IXListViewListener
	{
		@Override
		public void onRefresh()
		{
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					goodlist.stopRefresh();
					goodlist.stopLoadMore();
					goodlist.setRefreshTime("刚刚");
				}
			}, 2000);
		}

		@Override
		public void onLoadMore()
		{
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					Log.i("XU", "onLoadMore");
					goodlist.stopRefresh();
					goodlist.stopLoadMore();
					goodlist.setRefreshTime("刚刚");
				}
			}, 2000);
		}
	}


}
