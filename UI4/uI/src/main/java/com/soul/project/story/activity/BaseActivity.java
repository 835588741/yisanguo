/**
 * 
 */
package com.soul.project.story.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/** 内嵌自身actiivty栈管理
 * @file BaseActivity.java
 * @author 许仕永(xsy)
 * @package_name com.dearmax.gathering.base
 * @todo  TODO
 * @date 2016年3月18日 下午1:20:18
 */
public class BaseActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		MyApplication.list.add(this);
		
		  IntentFilter filter = new IntentFilter();
	       filter.addAction("exit_app");
	       registerReceiver(receiver, filter);
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals("exit_app"))
			{
				finish();
			}
		}
	};
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
//		MyApplication.list.remove(this);
	}
	
	public void exitAPP()
	{
//		Log.i("XU", "要退出了，清除所有Activity : "+MyApplication.list.size());
//		for (int i = 0; i < MyApplication.list.size(); i++)
//		{
//			MyApplication.list.get(i).finish();;
//		}
		sendBroadcast(new Intent("exit_app"));
	}
}
