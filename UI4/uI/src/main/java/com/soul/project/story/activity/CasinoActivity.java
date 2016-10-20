package com.soul.project.story.activity;

import net.tsz.afinal.http.AjaxCallBack;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

/**
 * Casino 赌场下注
 * @file CasinoActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年8月11日 上午11:57:56
 */
public class CasinoActivity extends BaseActivityWithSystemBarColor
{
	MTextView txtRefresh;
	MTextView txtInfo;
	Button btnSmall;
	Button btnBig;
	Button btnReturn;
	Button btnRule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_casno);
		
		loadData(0, 1);
		initViews();
	}
	
	
	private void initViews()
	{
		btnBig = (Button)this.findViewById(R.id.btnBig);
		btnReturn = (Button)this.findViewById(R.id.btnReturn);
		btnRule = (Button)this.findViewById(R.id.btnRule);
		btnSmall = (Button)this.findViewById(R.id.btnSmall);
		txtRefresh = (MTextView)this.findViewById(R.id.txtRefresh);
		txtInfo = (MTextView)this.findViewById(R.id.txtInfo);
		
//		txtRefresh.setOnClickListener(this);
	}


	@Override		
	public void loadData(int id, int type)
	{
		finalHttp.get(API.URL+"diaoyu.action?&type="+type+"&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			Dialog dialog ;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(CasinoActivity.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				super.onSuccess(t);
				dialog.dismiss();
			}
		});
	}
}
