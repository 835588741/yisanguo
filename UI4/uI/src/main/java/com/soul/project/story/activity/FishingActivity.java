package com.soul.project.story.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;

import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class FishingActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	MTextView txtInfo;
	MTextView txtRefresh;
	Button btnReturn;
	Button btnGetL;
	Button btnGetR;
	Button btnRule;
	ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishing);
		
		initViews();
		
		initEvent();
	}
	
	private void initEvent()
	{
		btnGetL.setOnClickListener(this);
		btnGetR.setOnClickListener(this);
		btnReturn.setOnClickListener(this);
		btnRule.setOnClickListener(this);
		txtRefresh.setOnClickListener(this);
	}

	private void initViews()
	{
		progress = (ProgressBar)this.findViewById(R.id.progress);
		btnRule = (Button)this.findViewById(R.id.btnRule);
		btnReturn = (Button)this.findViewById(R.id.btnReturn);
		btnGetL = (Button)this.findViewById(R.id.btnGetL);
		btnGetR = (Button)this.findViewById(R.id.btnGetR);
		txtInfo = (MTextView)this.findViewById(R.id.txtInfo);
		txtRefresh = (MTextView)this.findViewById(R.id.txtRefresh);
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		loadData(0, 1);
	}
	
	@Override
	public void loadData(int id, int type)
	{
		finalHttp.get(API.URL+"diaoyu.action?&type="+type+"&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				if(progress != null)
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				if(progress != null)
				progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(progress != null)
				progress.setVisibility(View.GONE);
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						String mes = jsonObject.getString("message");
						if(mes != null)
							txtInfo.setText(mes);
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

	long start = 0;
	long stop = 0;
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnGetL:
			case R.id.btnGetR:

				start = System.currentTimeMillis();
				if(start - stop > 1200)
				{
					loadData(0, 2);
					stop = start;
				}
				else
				{
					txtInfo.setText("垂钓者切忌心浮气躁");
				}
				break;
			case R.id.btnReturn:
				finish();
				break;
			case R.id.txtRefresh:
				loadData(0, 3);
				break;
			default:
				break;
		}
	}
}
