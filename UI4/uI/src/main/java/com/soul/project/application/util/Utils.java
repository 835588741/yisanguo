package com.soul.project.application.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
public class Utils{
	public static Context context;	
	
	
    
	/**
	 * URL生产拼接
	 * @param api
	 * @param map
	 * @return
	 */
	public static String getURL(String api,Map<String,Object> map)
	{
		String temp = api + "?";
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		while (iterator.hasNext()) 
		{
			Object object = iterator.next();
			String key = (String) object;
			if(key != null)
			temp += (key+"="+map.get(key)+"&");
		}
		// 去除最后一个‘&’
		temp = temp.substring(0,temp.length()-1);
		return temp;
	}
	
	/**
	 * 描述：检查网络可用
	 * @param activity 当前activity
	 * @return 返回是否可用
	 */
	public static boolean checkNet(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager == null)
		{
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isAvailable())
		{
			return false;
		}
		return true;
	}
	/**
	 * 描述：弹出连接网络错误提示框
	 */
	public static void AlertNetError()
	{
		AlertDialog.Builder alertError = new AlertDialog.Builder(context);
		alertError.setTitle("网络错误");
		alertError.setMessage("无法连接网络, 请检查网络配置");
		alertError.setNegativeButton("退出", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid()); 
				System.exit(0);
			}
		});
		alertError.setPositiveButton("设置", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alertError.create().show();
	}
	
	/**
	 * 描述：杀进程退出程序
	 */
	public static void exit(){
		// 跳转到桌�?
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
	}
	/**
	 * 描述：判断手机号是否合法
	 */
	public static boolean isMobile(String mobiles){
		String str="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(str);	  
		Matcher m = p.matcher(mobiles);  		  
		return m.matches();
	} 
	/**
	 * 检查SD卡是否可用
	 * @return true表示可用
	 */
	public static boolean checkSDCard()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}
	/**
	 * 描述：  显示退出对话框
	 * @param context 上下文
	 */
	public static void showExitDialog(final Context context)
	{
		new AlertDialog.Builder(context).setTitle("系统提示！").setMessage("确定退出程序?").
		setPositiveButton("确定", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		}).setNegativeButton("取消", null).create().show();
	}
	/**
	 * 描述：获得系统当前时间
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");       
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间        
    	String str = formatter.format(curDate);  
		return str;		
	}	
}
