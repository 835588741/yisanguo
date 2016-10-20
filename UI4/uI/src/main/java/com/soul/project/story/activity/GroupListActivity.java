/**
 * 
 */
package com.soul.project.story.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.GroupListAdapter;
import com.soul.project.application.bean.GroupEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

/** 
 * @file GroupListActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年8月1日 下午2:46:56
 */
public class GroupListActivity extends BaseActivityWithSystemBarColor implements OnItemClickListener, OnClickListener
{
	private SwipeMenuListView groupListView;
	private Handler mHandler = new Handler();
	private GroupListAdapter adapter;
	int page = 1;
	int pagesize = 20;
	private Button btnBack;
	private Button btnLook;
	private Button btnCreate;
	private Button btnHelp;
	List<GroupEntity> list = new ArrayList<GroupEntity>();
	List<GroupEntity> listSearch = new ArrayList<GroupEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);
	
		initViews();
		initEvents();
		loadData(page, 0);
	}
	
	private void initEvents()
	{
		btnBack.setOnClickListener(this);
		btnLook.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
	}

	private void initViews()
	{
		btnHelp = (Button) this.findViewById(R.id.btnHelp);
		btnCreate= (Button)this.findViewById(R.id.btnCreate);
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnLook = (Button) this.findViewById(R.id.btnLook);
		
		groupListView = (SwipeMenuListView) this.findViewById(R.id.groupListView);
		// 隐藏第一个Item的divider
		groupListView.setHeaderDividersEnabled(false);
		
		groupListView.setListViewFooterTextViewColor(getResources().getColor(R.color.black));
		groupListView.setListViewHeaderTextViewColor(getResources().getColor(R.color.black));
		groupListView.setListViewHeaderTimeTextViewColor(getResources().getColor(R.color.gray));
		groupListView.setOnItemClickListener(this);
		groupListView.setXListViewListener(new RefreshListener());	
		adapter = new GroupListAdapter(this, list);
		groupListView.setAdapter(adapter);
	}

	@Override
	public void loadData(int page, int type)
	{
		int start = (page - 1) * pagesize;
		String url = API.GROUP_REQUEST+"getgrouplist.action?&page="+start+"&pagesize="+pagesize;
		Log.i("XU", "url = "+url);
		finalHttp.get(url, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "加载失败"+strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(GroupListActivity.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t != null && !"[]".equals(t.toString().trim()))
				{
					try
					{
						JSONObject ob = new JSONObject(t.toString());
						if(ob.getInt("code") == 200)
						{
							String json = ob.getJSONArray("data").toString();
							
							Gson gson = new Gson();
							List<GroupEntity> temp = gson.fromJson(json, new TypeToken<List<GroupEntity>>(){}.getType());
							if(temp != null)
							{
								for (int i = 0; i < temp.size(); i++)
								{
									list.add(temp.get(i));
								}
								adapter.notifyDataSetChanged();
							}
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					ToastUtil.show(GroupListActivity.this, "没有新数据");
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (position == parent.getAdapter().getCount() - 1)
		{
			page++;
			loadData(page, 0);
		}
	}
	
	class RefreshListener implements IXListViewListener
	{
		@Override
		public void onRefresh()
		{
			Log.i("XU", "onRefresh");
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
//					page = 1;
//					loadData(0,0);
					groupListView.stopRefresh();
					groupListView.stopLoadMore();
					groupListView.setRefreshTime("刚刚");
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
					groupListView.stopRefresh();
					groupListView.stopLoadMore();
					groupListView.setRefreshTime("刚刚");
				}
			}, 2000);
		}
	}

	int mode = 1; //列表
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnBack:finish();
				
				break;
			case R.id.btnLook:
				if(mode == 1)
				{
					// 查找
					look();
					mode = 2;
					btnLook.setText("列表");
				}
				else
				{
					adapter = new GroupListAdapter(this, list);
					groupListView.setAdapter(adapter);
					mode = 1;
					btnLook.setText("查找");
				}
				break;
			case R.id.btnCreate:

				create();
				break;
			default:
				break;
		}
	}
	

	AlertDialog dialogCreate;
	private void create()
	{
		Builder builder = new Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_group, null);
		builder.setView(view);
		final EditText editText = (EditText) view.findViewById(R.id.etSearchName);
		final String createCondition = editText.toString();
		Button btnConfirmSearch = (Button)view.findViewById(R.id.btnConfirmSearch);
		Button btnCancelSearch = (Button)view.findViewById(R.id.btnCancelSearch);
		btnConfirmSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialogCreate.dismiss();
				String likename = editText.getText().toString();

				if(createCondition != null && createCondition.length() > 0)
				{
					try
					{
						finalHttp.get(API.GROUP_REQUEST+"creategroup.action?&groupidname="+(URLEncoder.encode(likename,"utf-8"))+"&uuid="+MyApplication.getUUID(GroupListActivity.this), new AjaxCallBack<Object>()
						{
							Dialog dialog;
							@Override
							public void onFailure(Throwable t, int errorNo, String strMsg)
							{
								// TODO Auto-generated method stub
								super.onFailure(t, errorNo, strMsg);
								dialog.dismiss();
								Log.i("XU", "帮派"+strMsg);
							}

							@Override
							public void onStart()
							{
								// TODO Auto-generated method stub
								super.onStart();
								dialog = MessageDialog.createLoadingDialog(GroupListActivity.this, "加载中...");
								dialog.show();
							}

							@Override
							public void onSuccess(Object t)
							{
								// TODO Auto-generated method stub
								super.onSuccess(t);
								dialog.dismiss();
								if(t!=null)
								{
									try
									{
										JSONObject jsonObject = new JSONObject(t.toString());
										String mes = jsonObject.getString("message");
										if(mes != null)
										{
											ToastUtil.show(GroupListActivity.this, mes);
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
					catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					ToastUtil.show(GroupListActivity.this, "点击请输入部曲名字");
				}
			}
		});
		
		btnCancelSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialogCreate.dismiss();
			}
		});
		
		builder.setView(view);
		dialogCreate = builder.show();		
	}


	AlertDialog dialogLook;
	private void look()
	{
		Builder builder = new Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_query_group, null);
		final EditText editText = (EditText) view.findViewById(R.id.etSearchName);
		final String searchCondition = editText.toString();
		Button btnConfirmSearch = (Button)view.findViewById(R.id.btnConfirmSearch);
		Button btnCancelSearch = (Button)view.findViewById(R.id.btnCancelSearch);
		
		btnConfirmSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialogLook.dismiss();
				String likename = editText.toString();
				if(searchCondition != null && searchCondition.length() > 0)
				{
					try
					{
						finalHttp.get(API.GROUP_REQUEST+"lookup.action?likename="+(URLEncoder.encode(likename,"utf-8")), new AjaxCallBack<Object>()
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
								dialog = MessageDialog.createLoadingDialog(GroupListActivity.this, "加载中...");
								dialog.show();
							}

							@Override
							public void onSuccess(Object t)
							{
								// TODO Auto-generated method stub
								super.onSuccess(t);
								dialog.dismiss();
								if(t!=null)
								{
									try
									{
										JSONObject jsonObject = new JSONObject(t.toString());
										String mes = jsonObject.getString("message");
										if(mes != null)
										{
											ToastUtil.show(GroupListActivity.this, mes);
											Gson gson = new Gson();
											listSearch = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<GroupEntity>>(){}.getType());
											adapter = new GroupListAdapter(GroupListActivity.this, listSearch);
											groupListView.setAdapter(adapter);
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
					catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					ToastUtil.show(GroupListActivity.this, "请输入搜索关键词");
				}
			}
		});
		
		btnCancelSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialogLook.dismiss();
			}
		});
		
		builder.setView(view);
		dialogLook = builder.show();
	}
}
