package com.soul.project.story.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DrugsActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	Button btnD1;
	Button btnD2;
	Button btnD3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drug);
		
		initView();
		initEvent();
	}
	
	private void initEvent()
	{
		btnD1.setOnClickListener(this);
		btnD2.setOnClickListener(this);
		btnD3.setOnClickListener(this);
	}

	private void initView()
	{
		btnD1 = (Button)this.findViewById(R.id.btnBuyD1);
		btnD2 = (Button)this.findViewById(R.id.btnBuyD2);
		btnD3 = (Button)this.findViewById(R.id.btnBuyD3);
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnBuyD1:buy(1);break;
			case R.id.btnBuyD2:buy(2);break;
			case R.id.btnBuyD3:buy(3);break;
			default:
				break;
		}
	}

	private void buy(int type)
	{
		finalHttp.get(API.URL2+"drugs.action?&type="+type+"&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(DrugsActivity.this, "提交中...");
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
							ToastUtil.showStaticToastShort(DrugsActivity.this, mes);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}
}
