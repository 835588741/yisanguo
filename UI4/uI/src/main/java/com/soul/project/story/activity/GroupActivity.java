/**
 * 
 */
package com.soul.project.story.activity;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.soul.project.application.bean.GroupEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

/** 部曲
 * @file GroupActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年8月1日 下午6:28:26
 */
public class GroupActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	String groupid;
	GroupEntity entity;
	int page = 1;
	Button btnBack;
	Button btnManage;
	Button btnGroupList;
	Button btnSubmitComment;
	MTextView txtRefresh;
	private SwipeMenuListView groupChatListView;
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		groupid = getIntent().getStringExtra("groupid");
		initView();
		initEvent();
		loadData(0, 0);
	}
	
	private void initView()
	{
		btnBack = (Button)this.findViewById(R.id.btnBack);
		btnManage = (Button)this.findViewById(R.id.btnManage);
		btnGroupList = (Button)this.findViewById(R.id.btnGroupList);
		btnSubmitComment = (Button)this.findViewById(R.id.btnSubmitComment);
		txtRefresh = (MTextView)this.findViewById(R.id.txtRefresh);
		
		groupChatListView = (SwipeMenuListView) this.findViewById(R.id.postlist);
		// 隐藏第一个Item的divider
		groupChatListView.setHeaderDividersEnabled(false);
		
		groupChatListView.setListViewFooterTextViewColor(getResources().getColor(R.color.black));
		groupChatListView.setListViewHeaderTextViewColor(getResources().getColor(R.color.black));
		groupChatListView.setListViewHeaderTimeTextViewColor(getResources().getColor(R.color.gray));
		
		//
		if("0000".equals(groupid))
		{
			btnManage.setEnabled(false);
			btnManage.setClickable(false);
			btnManage.setFocusable(false);
			btnManage.setBackgroundColor(getResources().getColor(R.color.gray_light));

			btnSubmitComment.setEnabled(false);
			btnSubmitComment.setClickable(false);
			btnSubmitComment.setFocusable(false);
			btnSubmitComment.setBackgroundColor(getResources().getColor(R.color.gray_light));
		}
	}

	private void initEvent()
	{
		groupChatListView.setXListViewListener(new RefreshListener());	
		txtRefresh.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnManage.setOnClickListener(this);
		btnGroupList.setOnClickListener(this);
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
					groupChatListView.stopRefresh();
					groupChatListView.stopLoadMore();
					groupChatListView.setRefreshTime("刚刚");
				}
			}, 2000);
		}

		@Override
		public void onLoadMore()
		{
			Log.i("XU", "onLoadMore");
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					groupChatListView.stopRefresh();
					groupChatListView.stopLoadMore();
					groupChatListView.setRefreshTime("刚刚");
				}
			}, 2000);
		}
	}

	
	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int, int)
	 */
	@Override
	public void loadData(int id, int type)
	{
		finalHttp.get(API.GROUP_REQUEST+"getgroupinfo.action?&groupid="+groupid, new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(GroupActivity.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
//				{
//				    "code": 200,
//				    "data": {
//				        "groupid": "0001",
//				        "group_name": "南阳飞龙军",
//				        "group_master_uuid": "0001",
//				        "group_master_uuid2": "0040",
//				        "group_count": 2,
//				        "group_grade": 1,
//				        "group_max_people": 20,
//				        "group_state": 1,
//				        "group_create_time": null
//				    },
//				    "message": "成功获取数据!"
//				}
				
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						if(jsonObject.getInt("code") == 200)
						{
							String json = jsonObject.getJSONObject("data").toString();
							Gson gson = new Gson();
							entity = gson.fromJson(json, GroupEntity.class);
							
							initValues();
						}
						else
						{
							ToastUtil.show(GroupActivity.this, jsonObject.getString("message"));
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

	boolean isManager;
	private void initValues()
	{
		if(!"0000".equals(groupid))
		{
			btnManage.setVisibility(View.VISIBLE);
			
			if(MyApplication.getUUID(this).equals(entity.getGroup_master_uuid()) || MyApplication.getUUID(this).equals(entity.getGroup_master_uuid2()))
			{
				isManager = true;
				btnManage.setText("管理");
			}
			else
			{
				isManager = false;
				btnManage.setText("成员");
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnBack:finish();break;
			case R.id.btnManage:
				Intent intent = new Intent(this, GroupManageActivity.class);
				intent.putExtra("groupid", groupid);
				intent.putExtra("isManager", isManager);
				ActivityUtil.goToNewActivityWithComplement(this, intent);
				break;
			case R.id.txtRefresh:break;
			case R.id.btnGroupList:
				ActivityUtil.goToNewActivity(this, GroupListActivity.class);
				break;
			default:
				break;
		}
	}
}
