package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.gson.Gson;
import com.soul.project.application.bean.PlaceBean;
import com.soul.project.application.bean.PlaceBean.userdate;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.SHA1;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yisanguo.app.api.API;

	public class MyApplication extends Application{

		public static userdate user;
		public static IWXAPI wxapi;
		public static List<Activity> list = new ArrayList<Activity>();
//		public static String mAPPid= "wx9972e13ac66616bf";
		private static final String tag = "SchoolApplication";

		private static Context context = null;
		private boolean homeStop = false;
		// 判断Home Activity是否为Stop状�?
		private final List<Activity> activityList = new LinkedList<Activity>();
//		private UserInfo userInfo = null;
		
		private static MyApplication instance;
		
		
		public static MyApplication getContext(){
			return instance;
		}


		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
//			// 提取模拟当前玩家信息
//			user = new player();
//			user.setUuid("0001");
//			user.setDescript("你正值豆蔻年华，青春无限，心中亦满怀大志，在这乱世中创一份家业");
//			user.setGrade(20);
//			user.setHp(100);
//			user.setName("解锋镝");
//			user.setUuid("0001");
			
			if(wxapi == null)
			{
				wxapi = WXAPIFactory.createWXAPI(this, API.AppID,  true); 
				wxapi.registerApp(API.AppID);
			}
			instance=this;
			
			initImageLoader(getApplicationContext());
		}
		
		public static String getUUID(Context context)
		{
			if(user == null)
			{
				reload(context);
				//ActivityUtil.goToNewActivity((Activity) context, LoginActivity.class);
			}
//			else
//			{
//				if(user.getUuid() == null || "".equals(user.getUuid().trim()) || "null".equals(user.getUuid()))
//				{
//					ActivityUtil.goToNewActivity((Activity) context, LoginActivity.class);
//					return null;
//				}
//			}
			return user.getUuid();
		}
		
//		private void loadUUID(final Context context)
//		{
//			String user_account = ShareDB.getStringFromDB(context, "user_account");
//			String user_password= ShareDB.getStringFromDB(context, "user_password");
//			
//			FinalHttp finalHttp = new FinalHttp();
//			finalHttp.configTimeout(8000);
//			finalHttp.get(API.LOGIN_OR_REGISTERT+"getuuid.action?&account="+user_account+"&password="+user_password, new AjaxCallBack<Object>()
//			{
//				Dialog dialog;
//				@Override
//				public void onFailure(Throwable t, int errorNo, String strMsg)
//				{
//					// TODO Auto-generated method stub
//					super.onFailure(t, errorNo, strMsg);
//					dialog.dismiss();
//				}
//
//				@Override
//				public void onStart()
//				{
//					// TODO Auto-generated method stub
//					super.onStart();
//					dialog = MessageDialog.createLoadingDialog(context, "Load...");
//					dialog.show();
//				}
//
//				@Override
//				public void onSuccess(Object t)
//				{
//					// TODO Auto-generated method stub
//					super.onSuccess(t);
//					dialog.dismiss();
//					if(t != null)
//					{
//						try
//						{
//							JSONObject jsonObject = new JSONObject(t.toString());
//							if(jsonObject.getInt("code") == 200)
//							{
//								String uuid=jsonObject.getString("uuid");
//								PlaceBean.user u = new PlaceBean.user();
//							}
//						}
//						catch (JSONException e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//					}
//				}
//			});
//		}
		private static void reload(final Context context)
		{
			String user_account = ShareDB.getStringFromDB(context, "user_account");
			String user_password= ShareDB.getStringFromDB(context, "user_password");
			
			FinalHttp finalHttp = new FinalHttp();

			AjaxParams ajaxParams = new AjaxParams();
			ajaxParams.put("account", user_account);
			ajaxParams.put("password", user_password);
			ajaxParams.put("version", ""+LoginActivity.version);
			finalHttp.addHeader("sign", getSignature());
			
			finalHttp.post(API.LOGIN_OR_REGISTERT + "login.action?",ajaxParams, new AjaxCallBack<Object>()
			{
				Dialog dialog;

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					ToastUtil.show(context, "数据重载失败!" + strMsg, ToastUtil.ERROR);
					dialog.dismiss();
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					dialog = MessageDialog.createLoadingDialog(context, "正在请求服务器...");// CustomProgressDialog.getProgressDiaolgNoTitle(context,
					dialog.show();
				}

				@Override
				public void onSuccess(Object t)
				{
					// TODO Auto-generated method stub
					super.onSuccess(t);
					try
					{
						if (t != null && !"".equals(t.toString().trim()))
						{
							if ("{}".equals(t.toString().trim()))
							{
								ToastUtil.show(context, "不存在当前账号!", ToastUtil.ERROR);
							}
							else
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								if (jsonObject.getInt("code") == 200)
								{
									Log.i("XU", "重载数据--->");
									Gson gson = new Gson();
									userdate temp = gson.fromJson(jsonObject.get("data").toString(), userdate.class);
									if(temp != null)
										user = temp;
									else
										ActivityUtil.goToNewActivity((Activity) context, LoginActivity.class);
								}
								else
								{
									String mes = jsonObject.getString("message");
									if (mes != null)
										ToastUtil.show(context, mes);
									else
										ToastUtil.show(context, "异常原因，重载数据请求失败!");
								}
							}
						}
						else
							ToastUtil.show(context, "登陆请求失败!或网络异常");
					}
					catch (Exception e)
					{
						// TODO: handle exception
					}
					finally
					{
						if (dialog != null)
							dialog.dismiss();
					}
				}
			});
		}
		
		  public static String getSignature() {
		      
	          try {
	       	   PackageManager manager = context.getPackageManager();
	              /** 通过包管理器获得指定包名包含签名的包信息 **/
	       	   PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
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

		
		public static void exitAPP()
		{
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).finish();
			}
		}
		
		/**
		 * 添加Activity到容器中
		 * 
		 * @param activity
		 */
		public void addActivity(Activity activity) {
			activityList.add(activity);
		}

		/**
		 * 
		 */
//		public void logout(Activity a, BaseHandler baseHandler) {
//			try {
//				// �?��其它应用
//				for (Activity activity : activityList) {
//					Log.i(tag,
//							"activity.getPackageName(): "
//									+ activity.getPackageName());
//					if ((!(activity.isFinishing())) && (a != activity)) {
//						activity.finish();
//					}
//				}
//				activityList.clear();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

		/**
		 * 遍历�?��的Activity并finish
		 */
		public void exit() {

			// �?��其它应用
			for (Activity activity : activityList) {
				Log.i(tag,
						"activity.getPackageName(): " + activity.getPackageName());
				if (!(activity.isFinishing())) {
					activity.finish();
				}
			}
			activityList.clear();

			/*
			 * ActivityManager activityMgr = (ActivityManager)
			 * this.getSystemService(ACTIVITY_SERVICE);
			 * activityMgr.restartPackage(getPackageName());
			 */

			// 跳转到桌�?
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);

			android.os.Process.killProcess(android.os.Process.myPid());
		}

		/**
		 * 从容器中删除Activity
		 * 
		 * @param activity
		 */
		public void removeActivity(Activity activity) {
			activityList.remove(activity);
		}

		/**
		 * 判断容器中是否存在这个Activity
		 * 
		 * @param activity
		 * @return
		 */
		public boolean isActivityContain(Activity activity) {
			return activityList.contains(activity);
		}

		/**
		 * @return the homeStop
		 */
		public boolean isHomeStop() {
			return homeStop;
		}

		/**
		 * @param homeStop
		 *            the homeStop to set
		 */
		public void setHomeStop(boolean homeStop) {
			this.homeStop = homeStop;
		}


		public static void initImageLoader(Context context) {
			// This configuration tuning is custom. You can tune every option, you
			// may tune some of them,
			// or you can create default configuration by
			// ImageLoaderConfiguration.createDefault(this);
			// method.
//			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//					context).threadPriority(Thread.NORM_PRIORITY - 2)
//					.denyCacheImageMultipleSizesInMemory()
//					.discCacheFileNameGenerator(new Md5FileNameGenerator())
//					.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
//																					// necessary
//																					// in
//																					// common
//					.build();
//			// Initialize ImageLoader with configuration.
//			ImageLoader.getInstance().init(config);
		}
	}
