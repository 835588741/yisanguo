package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.FriendListAdapter;
import com.soul.project.application.bean.Friend;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

/**
 * 好友列表
 * @file FriendListActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年7月15日 下午2:10:37
 */
public class FriendListActivity extends BaseActivityWithSystemBarColor implements OnItemClickListener, OnClickListener
{
	SwipeMenuListView friendlist;
	private Handler mHandler = new Handler();
	List<Friend> list = new ArrayList<Friend>();
	List<Friend> listBlack = new ArrayList<Friend>();
	FriendListAdapter adapter;
	Button btnLeft;
	Button btnBlackList;
	MTextView txtNumber;
	boolean isBlackListMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendlist);
		
		initView();
		loadData(0, 0);
	}
	
	private void initView()
	{
		btnLeft = (Button)this.findViewById(R.id.btnLeft);
		btnBlackList= (Button)this.findViewById(R.id.btnBlackList);
		
		btnLeft.setOnClickListener(this);
		btnBlackList.setOnClickListener(this);
		
		txtNumber = (MTextView)this.findViewById(R.id.txtNumber);
		
		friendlist = (SwipeMenuListView) this.findViewById(R.id.friendlist);
		// 隐藏第一个Item的divider
		friendlist.setHeaderDividersEnabled(false);
		friendlist.setOnItemClickListener(this);
		friendlist.setListViewFooterTextViewColor(getResources().getColor(R.color.black));
		friendlist.setListViewHeaderTextViewColor(getResources().getColor(R.color.black));
		friendlist.setListViewHeaderTimeTextViewColor(getResources().getColor(R.color.gray));
		
		friendlist.setXListViewListener(new RefreshListener());		
	}

	@Override
	public void loadData(int id, int type)
	{
		if(list == null || list.size() == 0)
		{
			finalHttp.get(API.URL+"getfriendlist.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
							dialog = MessageDialog.createLoadingDialog(FriendListActivity.this, "获取朋友列表中...");
							dialog.show();
						}

						@Override
						public void onSuccess(Object t)
						{
							// TODO Auto-generated method stub
							super.onSuccess(t);
							dialog.dismiss();
							if(t != null)
							{
								Gson gson = new Gson();
								list = gson.fromJson(t.toString(), new TypeToken<List<Friend>>() {}.getType());
								adapter = new FriendListAdapter(FriendListActivity.this, list,1);
								friendlist.setAdapter(adapter);
								if(list != null)
								{
									int onlineCount = 0;
									for (int i = 0; i < list.size(); i++)
									{
										if(list.get(i).getState() == 1)
											onlineCount ++;
									}
									
									String numberCount =  onlineCount+" / "+list.size();
									txtNumber.setText(numberCount);
								}
							}
						}
					});
		}
		else
		{
			int onlineCount = 0;
			for (int i = 0; i < list.size(); i++)
			{
				if(list.get(i).getState() == 1)
					onlineCount ++;
			}
			
			String numberCount =  onlineCount+" / "+list.size();
			txtNumber.setText(numberCount);
			
			adapter = new FriendListAdapter(FriendListActivity.this, list,1);
			friendlist.setAdapter(adapter);
		}
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
					loadData(0,0);
//					postListView.stopRefresh();
//					postListView.stopLoadMore();
//					postListView.setRefreshTime("刚刚");
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
					friendlist.stopRefresh();
					friendlist.stopLoadMore();
					friendlist.setRefreshTime("刚刚");
				}
			}, 2000);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnLeft:
				finish();
				break;
			case R.id.btnBlackList:
				if(isBlackListMode)
				{
					btnBlackList.setText("黑名单");
					loadData(1, 1);
				}
				else
				{
					btnBlackList.setText("好友单");
					getBlackList();
				}
				isBlackListMode = !isBlackListMode;
				break;
			default:
				break;
		}
	}

	private void getBlackList()
	{
		if(listBlack == null || listBlack.size()==0)
		{
			finalHttp.get(API.MESSAGE_REQUEST+"getblacklist.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
					{
						Dialog dialog;
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							dialog.dismiss();
							Log.i("XU", "黑单数据失败"+strMsg);
						}

						@Override
						public void onStart()
						{
							// TODO Auto-generated method stub
							super.onStart();
							dialog = MessageDialog.createLoadingDialog(FriendListActivity.this, "获取黑名单中...");
							dialog.show();
						}

						@Override
						public void onSuccess(Object t)
						{
							// TODO Auto-generated method stub
							super.onSuccess(t);
							dialog.dismiss();
							if(t != null)
							{
								try
								{
									JSONObject jsonObject = new JSONObject(t.toString());
									String data = jsonObject.getString("data");
									Gson gson = new Gson();
									listBlack = gson.fromJson(data, new TypeToken<List<Friend>>() {}.getType());
									adapter = new FriendListAdapter(FriendListActivity.this, listBlack,2);
									friendlist.setAdapter(adapter);
									if(listBlack != null)
									{
										int onlineCount = 0;
										for (int i = 0; i < listBlack.size(); i++)
										{
											if(listBlack.get(i).getState() == 1)
												onlineCount ++;
										}
										
										String numberCount =  onlineCount+" / "+listBlack.size();
										txtNumber.setText(numberCount);
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
		else
		{
			int onlineCount = 0;
			for (int i = 0; i < listBlack.size(); i++)
			{
				if(listBlack.get(i).getState() == 1)
					onlineCount ++;
			}
			String numberCount =  onlineCount+" / "+listBlack.size();
			
			txtNumber.setText(numberCount);
			adapter = new FriendListAdapter(FriendListActivity.this, listBlack,2);
			friendlist.setAdapter(adapter);
		}
	}
}
