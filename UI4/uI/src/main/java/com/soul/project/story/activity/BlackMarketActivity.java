/**
 * 
 */
package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.BlackMarketAdapter;
import com.soul.project.application.bean.Goods;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

/**
 * @file BlackMarketActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年8月3日 上午11:55:10
 */
public class BlackMarketActivity extends BaseActivityWithSystemBarColor implements OnItemClickListener, OnClickListener
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
	Button btnQita;
	Button btnMyGood;
	/** 拍卖*/
	Button btnAuction;
	
	int pos = 0;
	
	BlackMarketAdapter adapter;
	
	List<Goods> wuqiQiang = new ArrayList<Goods>();
	List<Goods> wuqiDao   = new ArrayList<Goods>();
	List<Goods> wuqiJian = new ArrayList<Goods>();
	List<Goods> zbPiFeng = new ArrayList<Goods>();
	List<Goods> zbTouKui = new ArrayList<Goods>();
	List<Goods> zbKaiJia = new ArrayList<Goods>();
	List<Goods> zbYaoDai = new ArrayList<Goods>();
	List<Goods> zbXieZi = new ArrayList<Goods>();
	List<Goods> zbMyGoods = new ArrayList<Goods>();
	List<Goods> zbQita = new ArrayList<Goods>();
	int page = 1;
	int type = 101;
	int pagesize = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_goods);
		
		initViews();
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction("remove_item");
		registerReceiver(receiverRemove, filter);
		
		isAppend = false;
		loadData(page, 101);
	}
	
	BroadcastReceiver receiverRemove = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals("remove_item"))
			{
				switch (type)
				{
					case 101:
						wuqiDao.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 102:
						wuqiQiang.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 103:
						wuqiJian.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 2:
						zbTouKui.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 3:
						zbPiFeng.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 4:
						zbKaiJia.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 5:
						zbYaoDai.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 6:
						zbXieZi.remove(pos);adapter.notifyDataSetChanged();
						break;
					case 7:
						break;
					default:
						break;
				}
			}
		}
	};
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if(receiverRemove != null)
		unregisterReceiver(receiverRemove);
	}
	
	private void initViews()
	{
		goodlist = (SwipeMenuListView)findViewById(R.id.loaddataview);
		adapter = new BlackMarketAdapter(this, wuqiDao,1);
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
		btnMyGood = (Button)this.findViewById(R.id.btnMyGood);
		btnQita   = (Button)this.findViewById(R.id.btnQiTa);
		btnAuction = (Button)this.findViewById(R.id.btnAuction);
				
		btnAuction.setOnClickListener(this);
		 btnWuQiDao.setOnClickListener(this);
		 btnWuQiJian.setOnClickListener(this);
		 btnWuQiQiang.setOnClickListener(this);
		 btnTouKui.setOnClickListener(this);
		 btnKaiJia.setOnClickListener(this);
		 btnPiFeng.setOnClickListener(this);
		 btnYaoDai.setOnClickListener(this);
		 btnXieZi.setOnClickListener(this);
		 btnQita.setOnClickListener(this);
		 btnMyGood.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int, int)
	 */
	@Override
	public void loadData(int page, final int type)
	{
		this.type = type;
		int start = (page - 1) * pagesize;
		String url;
			url = API.TRANSACTION+"getallsell.action?&type="+type+"&start="+start+"&size="+pagesize;
		
		finalHttp.get(url, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("XU", "获取武器失败"+errorNo+"  "+strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(BlackMarketActivity.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				super.onSuccess(t);
				dialog.dismiss();
				//{"code":200,"message":"获取列表成功!","data":[{"tid":"31ebede21152413ba662e05b601e0c5f","masterid":"0040","gid":"58354ce34c744859829d7e71df4250aa","price":10,"mastername":"诸葛武侯","life":null,"addhp":null,"addattack":null,"adddefence":null,"adddexterous":null,"descript":null}]}

				Log.i("XU", "获取武器："+t.toString());
				if(t != null && !"".equals(t.toString().trim()))
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							Gson gson = new Gson();
							List<Goods> temp = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Goods>>() {}.getType());
							
							switch (type)
							{
								case 101:
									if(!isAppend)
										wuqiDao.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										wuqiDao.add(temp.get(i));
									}
									adapter.setList(wuqiDao);
									break;
								case 102:
									if(!isAppend)
										wuqiQiang.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										wuqiQiang.add(temp.get(i));
									}
									adapter.setList(wuqiQiang);
									break;
								case 103:
									if(!isAppend)
										wuqiJian.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										wuqiJian.add(temp.get(i));
									}
									adapter.setList(wuqiJian);
									break;
									
								case 2:
									if(!isAppend)
										zbTouKui.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbTouKui.add(temp.get(i));
									}
									adapter.setList(zbTouKui);
									break;
								case 3:
									if(!isAppend)
										zbPiFeng.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbPiFeng.add(temp.get(i));
									}
									adapter.setList(zbPiFeng);
									break;
								case 4:
									if(!isAppend)
										zbKaiJia.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbKaiJia.add(temp.get(i));
									}
									adapter.setList(zbKaiJia);
									break;
								case 5:
									if(!isAppend)
										zbYaoDai.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbYaoDai.add(temp.get(i));
									}
									adapter.setList(zbYaoDai);
									break;
								case 6:
									if(!isAppend)
										zbXieZi.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbXieZi.add(temp.get(i));
									}
									adapter.setList(zbXieZi);
									break;
								case 7:
									if(!isAppend)
										zbQita.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbQita.add(temp.get(i));
									}
									adapter.setList(zbQita);
									break;
									
								case 1024:
									if(zbMyGoods != null)
										zbMyGoods.clear();
									for (int i = 0; i < temp.size(); i++)
									{
										zbMyGoods.add(temp.get(i));
									}
									adapter.setList(zbMyGoods);
								default:
									break;
							}
						}
						else
						{
							String mes = jsonObject.getString("message");
							if(mes != null)
							ToastUtil.show(BlackMarketActivity.this, mes);
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	boolean isAppend;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		pos = position - 1;
		
		if (position == parent.getAdapter().getCount() - 1)
		{
			isAppend = true;
			page ++;
			loadData(page, type);
		}
		else
		{
			Goods goods = null;
			Intent intent = new Intent(this, GoodsSellDetailActivity.class);
			switch (type)
			{
				case 101:
					goods = wuqiDao.get(position-1);
					break;
				case 102:
					goods = wuqiQiang.get(position-1);
					break;
				case 103:
					goods = wuqiJian.get(position-1);
					break;
				case 2:
					goods = zbTouKui.get(position-1);
					break;
				case 3:
					goods = zbPiFeng.get(position-1);
					break;
				case 4:
					goods = zbKaiJia.get(position-1);
					break;
				case 5:
					goods = zbYaoDai.get(position-1);
					break;
				case 6:
					goods = zbXieZi.get(position-1);
					break;
				case 7:
					goods = zbQita.get(position-1);
					break;
				case 1024:
					intent.putExtra("mode", 1024);
					goods = zbMyGoods.get(position - 1);
					break;
				default:
					break;
			}
			
			intent.putExtra("tid", goods.getTid());
			intent.putExtra("gid", goods.getGid());
			intent.putExtra("name", goods.getName());
			intent.putExtra("price", goods.getPrice());
			intent.putExtra("life", goods.getLife());
			intent.putExtra("star", goods.getStar());
			intent.putExtra("masterid", goods.getMasterid());
			intent.putExtra("mastername", goods.getMastername());
			intent.putExtra("descript", goods.getDescript());
			intent.putExtra("grade", goods.getGgrade());
			ActivityUtil.goToNewActivityWithComplement(this, intent);
		}
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


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnWuQiDao:
//				if(wuqiDao != null && wuqiDao.size() > 0)
//				{
//					adapter.setList(wuqiDao);
//				}
//				else
				isAppend = false;
					loadData(1, 101);
				break;
			case R.id.btnWuQiJIan:
//				if(wuqiJian != null && wuqiJian.size() > 0)
//				{
//					adapter.setList(wuqiJian);
//				}
//				else
				isAppend = false;
					loadData(1, 103);
				break;
			case R.id.btnWuQiQiang:
//				if(wuqiQiang != null && wuqiQiang.size() > 0)
//				{
//					adapter.setList(wuqiQiang);
//				}
//				else
				isAppend = false;
					loadData(1, 102);
				
				break;
			case R.id.btnTouKui:
//				if(zbTouKui != null && zbTouKui.size() > 0)
//				{
//					adapter.setList(zbTouKui);
//				}
//				else
				isAppend = false;
					loadData(1, 2);
				break;
			case R.id.btnKaiJia:
//				if(zbKaiJia != null && zbKaiJia.size() > 0)
//				{
//					adapter.setList(zbKaiJia);
//				}
//				else
				isAppend = false;
					loadData(1, 4);				
				break;
			case R.id.btnPiFeng:
//				if(zbPiFeng != null && zbPiFeng.size() > 0)
//				{
//					adapter.setList(zbPiFeng);
//				}
//				else
				isAppend = false;
					loadData(1, 3);					
				break;
			case R.id.btnYaoDai:
//				if(zbYaoDai != null && zbYaoDai.size() > 0)
//				{
//					adapter.setList(zbYaoDai);
//				}
//				else
				isAppend = false;
					loadData(1, 5);					
				break;
			case R.id.btnXiezi:
//				if(zbXieZi != null && zbXieZi.size() > 0)
//				{
//					adapter.setList(zbXieZi);
//				}
//				else
				isAppend = false;
					loadData(1, 6);					
				break;
			case R.id.btnQiTa:
				isAppend = false;
				loadData(1, 7);
				break;
				
			case R.id.btnMyGood:
				isAppend = false;
				getMyGoodsList();
				break;
				
			case R.id.btnAuction:
				Intent intent = new Intent(this, GoodsListActivty.class);
//				uuid = intent.getStringExtra("uuid");
//				activityType = intent.getIntExtra("activityType", 0);
				intent.putExtra("uuid", MyApplication.getUUID(this));
				intent.putExtra("activityType", 1024);
				ActivityUtil.goToNewActivityWithComplement(this, intent);
				break;
			default:
				break;
		}		
	}

	private void getMyGoodsList()
	{
		type = 1024;
		finalHttp.get(API.TRANSACTION+"getmysell.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Log.i("XU", "t = "+t.toString());
				if(t != null)
				{
					try
					{
						JSONObject jsonObject;
						jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							Gson gson = new Gson();
							zbMyGoods = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Goods>>() {}.getType());
							adapter.setList(zbMyGoods);
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
}
