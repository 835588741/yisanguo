package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.MemberDataAdapter;
import com.soul.project.application.bean.MemberEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class GroupManageActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	List<MemberEntity> list = new ArrayList<MemberEntity>();
	MemberDataAdapter adapter;
	ListView memberlist;
	MTextView txtTag;
	Button btnLeft;
	Button btnReviewed;
	String groupid;
	int mode=1;
	boolean isManager;
	Button btnExit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_manage);
		groupid = getIntent().getStringExtra("groupid");
		isManager = getIntent().getBooleanExtra("isManager", false);
		initView();
		loadData(0, 0);
	}
	
	private void initView()
	{
		btnExit = (Button)this.findViewById(R.id.btnExit);
		txtTag = (MTextView)this.findViewById(R.id.txtTag);
		btnLeft = (Button)this.findViewById(R.id.btnLeft);
		btnReviewed= (Button)this.findViewById(R.id.btnReviewed);
		memberlist = (ListView)this.findViewById(R.id.memberlist);
		
		if(!isManager)
		{
			btnExit.setVisibility(View.VISIBLE);
			btnReviewed.setVisibility(View.GONE);
			txtTag.setText("部曲成员列表");
			btnExit.setOnClickListener(this);
		}
		else
		{
			btnExit.setVisibility(View.GONE);
			btnReviewed.setVisibility(View.VISIBLE);
		}
		
		btnLeft.setOnClickListener(this);
		btnReviewed.setOnClickListener(this);
	}

	@Override
	public void loadData(int id, int type)
	{
//		Log.i("XU", ""+API.GROUP_REQUEST+"getgroupmemberlist.action?groupid="+groupid);
		finalHttp.get(API.GROUP_REQUEST+"getgroupmemberlist.action?groupid="+groupid, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", ""+strMsg);			
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(GroupManageActivity.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
//				Log.i("XU", ""+t.toString());
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						String mes = jsonObject.getString("message");
						if(mes != null)
						{
							ToastUtil.show(GroupManageActivity.this, mes);
							String json = jsonObject.getString("data");
							Gson gson = new Gson();
							list = gson.fromJson(json, new TypeToken<List<MemberEntity>>(){}.getType());
							adapter = new MemberDataAdapter(GroupManageActivity.this, list,groupid,1,isManager);
							memberlist.setAdapter(adapter);
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


	@Override
	public void onClick(View v)
	{
		if(v == btnLeft)
		{
			finish();
		}
		else if(v == btnExit)
		{
			Builder builder = new Builder(this);
			builder.setTitle("提示");
			builder.setMessage("是否确认要退出部曲？");
			builder.setNegativeButton("我再想想", null);
			builder.setPositiveButton("坚决退出", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					exitGroup();
				}
			});
			builder.show();
		}
		// 审核
		else if(v == btnReviewed)
		{
			if(mode == 1)
			{
				loadReviewed();
				mode = 2;
				txtTag.setText("申请加入的列表");
				btnReviewed.setText("管理");
			}
			else
			{
				loadData(0, 0);
				mode = 1;
				txtTag.setText("部曲成员管理");
				btnReviewed.setText("审核");
			}
		}
	}

	private void exitGroup()
	{
		finalHttp.get(API.GROUP_REQUEST+"backedout.action?&uuid="+MyApplication.getUUID(this)+"&groupid="+groupid, new AjaxCallBack<Object>()
		{
			Dialog dialog;
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
				dialog = MessageDialog.createLoadingDialog(GroupManageActivity.this, "加载中...");
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
						String mes = jsonObject.getString("message");
						if(mes != null)
						{
							ToastUtil.show(GroupManageActivity.this, mes);
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

	private void loadReviewed()
	{
		finalHttp.get(API.GROUP_REQUEST+"getjoinlist.action?&uuid="+MyApplication.getUUID(this)+"&groupid="+groupid, new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(GroupManageActivity.this, "加载中...");
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
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							Gson gson = new Gson();
							list = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<MemberEntity>>(){}.getType());
							adapter = new MemberDataAdapter(GroupManageActivity.this, list,groupid,2,isManager);
							memberlist.setAdapter(adapter);
						}
						else 
						{
							String mes = jsonObject.getString("message");
							if(mes != null)
							{
								ToastUtil.show(GroupManageActivity.this, mes);
							}
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
