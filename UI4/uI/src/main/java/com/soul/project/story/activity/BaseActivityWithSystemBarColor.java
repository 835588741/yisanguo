/**
 * 
 */
package com.soul.project.story.activity;

import net.tsz.afinal.FinalHttp;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.soul.project.application.util.NetManager;

/** 基础父类 （包含了状态栏一体化沉浸式、网络判断处理、http请求模块、退出应用）
 * @file BaseActivityWithSystemBarColor.java
 * @author 许仕永(xsy)
 * @package_name com.dearmax.gathering.base
 * @todo  TODO
 * @date 2016年3月18日 下午2:50:59
 */
public abstract class BaseActivityWithSystemBarColor extends Activity
{
	protected NetManager netManager;
	protected FinalHttp finalHttp;
	protected Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.list.add(this);
		
		netManager = NetManager.newInstance(this);
		finalHttp = new FinalHttp();
		// 网络超时 8秒
		finalHttp.configTimeout(8000);
		
		  IntentFilter filter = new IntentFilter();
	       filter.addAction("exit_app");
	       registerReceiver(receiver, filter);
		
        // 设置状态栏颜色之前需先调用本段代码
		Window win = this.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		winParams.flags |= bits;
		win.setAttributes(winParams);
        
        // 创建状态栏的管理实例  
        SystemBarTintManager tintManager = new SystemBarTintManager(this);  
        // 激活状态栏设置  
        tintManager.setStatusBarTintEnabled(true);  
        // 激活导航栏设置  
        tintManager.setNavigationBarTintEnabled(true); 
//        tintManager.setTintColor(Color.parseColor("#99000FF"));  
//        tintManager.setStatusBarTintResource(R.drawable.bg_statusbar);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.app_color)); 
	}
	
	  public String getSignature() {
	      
          try {
       	   PackageManager manager = getPackageManager();
              /** 通过包管理器获得指定包名包含签名的包信息 **/
       	   PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
              /******* 通过返回的包信息获得签名数组 *******/
       	   android.content.pm.Signature[] signatures = packageInfo.signatures;
              /******* 循环遍历签名数组拼接应用签名 *******/
       	   StringBuilder builder = new StringBuilder();
              for (android.content.pm.Signature signature : signatures) {
                  builder.append(signature.toCharsString());
              }
              /************** 得到应用签名 **************/
              return builder.toString();
          } catch (NameNotFoundException e) {
              e.printStackTrace();
          }
          return null;
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
	
	public abstract void loadData(int id,int type);
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		MyApplication.list.remove(this);
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
	
	/** 
	 * 由父类全权处理网络状态 （需由集成本父类的子类自行调用本方法授权）
	 * @author 许仕永(xsy)
	 * des:
	 */
	public void networkSituationProcessingBySuper()
	{
		// 断网时的处理
	}

	
//	/**
//	 * BaseActivityWithSystemBarColor.java 超类 显示加载对话框
//	 * @author 许仕永(xsy)
//	 * des: 
//	 * @param context
//	 * @param text
//	 */
//	protected void showDialog(Context context,String text)
//	{
//		dialog = MessageDialog.createLoadingDialog(context, text);
//		if(!dialog.isShowing())
//			dialog.show();
//	}
//	
//	/**
//	 * BaseActivityWithSystemBarColor.java 超类  隐藏关闭加载对话框
//	 * @author 许仕永(xsy)
//	 * des:
//	 */
//	protected void dismissDialog()
//	{
//		if(dialog != null)
//		{
//			// 正在显示
//			if(dialog.isShowing())
//				dialog.dismiss();
//		}
//	}
}
