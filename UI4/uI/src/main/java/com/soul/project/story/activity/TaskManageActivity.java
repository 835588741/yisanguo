package com.soul.project.story.activity;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class TaskManageActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{

	MTextView txtTaskDescript;
	MTextView txtTaskDescriptDaily;
	Button btnHelp;
	Button btnDuoBaoTask;
	MTextView txtTaskName;
	Button btnBranchTask;
	Button btnOut;
	Button btnChangeTask;
	String dailytaskdesc;
	String newpeopletask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmanage);
		
		btnOut = (Button)this.findViewById(R.id.btnOut);
		btnHelp = (Button)this.findViewById(R.id.btnHelp);
		btnDuoBaoTask = (Button)this.findViewById(R.id.btnDuoBaoTask);
		btnBranchTask = (Button)this.findViewById(R.id.btnBranchTask);
		btnChangeTask = (Button)this.findViewById(R.id.btnChangeTask);
		
		txtTaskDescript = (MTextView)this.findViewById(R.id.txtTaskDescript);
		txtTaskDescriptDaily = (MTextView)this.findViewById(R.id.txtTaskDescriptDaily);
		txtTaskName = (MTextView)this.findViewById(R.id.txtTaskName);
		
		txtTaskDescript.setOnClickListener(this);
		btnBranchTask.setOnClickListener(this);
		btnDuoBaoTask.setOnClickListener(this);
		btnChangeTask.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnOut.setOnClickListener(this);
		
		loadDailyTask();
	}
	
	@Override
	public void loadData(int id,int type)
	{
		// TODO Auto-generated method stub
		finalHttp.get(API.URL+"gettaskstate.action?&taskreceiveruuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(TaskManageActivity.this, "加载数据中~~~~~~");
				dialog.setCancelable(false);
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t != null)
				{
					newpeopletask = t.toString();
					if(newpeopletask.contains("0/0"))
					{
						newpeopletask = "您现在还没有接受任务。\n\n"+newpeopletask;
					}
					
					txtTaskDescript.setText(newpeopletask);
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnOut)
		{
			finish();
		}
		else if(v.getId() == R.id.btnHelp)
		{
			ActivityUtil.goToNewActivity(TaskManageActivity.this, ForumActivity.class);
			finish();
		}
		else if(v.getId() == R.id.btnBranchTask)
		{
			if(newpeopletask == null)
				loadData(0,0);
			txtTaskDescriptDaily.setVisibility(View.GONE);
			txtTaskDescript.setVisibility(View.VISIBLE);
			btnChangeTask.setVisibility(View.GONE);
		}
		else if(v.getId() == R.id.btnDuoBaoTask)
		{
			if(dailytaskdesc == null)
				loadDailyTask();
			txtTaskDescriptDaily.setVisibility(View.VISIBLE);
			txtTaskDescript.setVisibility(View.GONE);
			btnChangeTask.setVisibility(View.VISIBLE);
		}
		else if(R.id.btnChangeTask == v.getId())
		{
			Builder builder = new Builder(this);
			builder.setTitle("温馨提示");
			builder.setMessage("是否确认要花费10两银子更换任务？");
			builder.setNegativeButton("我确定", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					changeTask();
				}
			});
			builder.setPositiveButton("我拒绝", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					
				}
			});
			builder.show();
		}
	}
	
	private void changeTask()
	{
		finalHttp.get(API.URL+"changetask.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("XU", "任务失败="+strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(TaskManageActivity.this, "加载数据中~~~~~~");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "更换任务="+t.toString());
				if(t != null)
				{
					try
					{
						JSONObject jsonObject;
						jsonObject = new JSONObject(t.toString());
						String mes = jsonObject.getString("message");
						dailytaskdesc = jsonObject.getString("message2");
						if(dailytaskdesc == null)
							dailytaskdesc = "";
						txtTaskDescriptDaily.setText(Html.fromHtml(dailytaskdesc));
						if(mes != null)
							ToastUtil.showStaticToast(TaskManageActivity.this, mes);
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

	private void loadDailyTask()
	{
		finalHttp.get(API.URL+"getdailytaskstate.action?&taskreceiveruuid="+MyApplication.getUUID(this)+"&type=1", new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(TaskManageActivity.this, "加载数据中~~~~~~");
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
						JSONObject jsonObject;
						jsonObject = new JSONObject(t.toString());
						dailytaskdesc = jsonObject.getString("message");
						txtTaskDescriptDaily.setText(Html.fromHtml(dailytaskdesc));
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
