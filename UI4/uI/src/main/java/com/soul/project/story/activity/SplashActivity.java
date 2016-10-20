package com.soul.project.story.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.soul.project.application.util.ActivityUtil;

public class SplashActivity extends BaseActivity{

//	private int delayTime = 1000;
//	private FinalHttp finalHttp;
//	int versionCode;
//	String apkUrl;
//	ImageView imageView;
//	ProgressBar progress;
//	AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.list.add(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏  
		setContentView(R.layout.splash);
//		progress = (ProgressBar)this.findViewById(R.id.progress);
		
//		finalHttp = new FinalHttp();
//		finalHttp.configTimeout(8000);
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		handler.sendEmptyMessageDelayed(1000, 1500);
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.list.remove(this);
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
				ActivityUtil.goToNewActivity(SplashActivity.this, LoginActivity.class);//MainPanelActivity.class);
		};
	};
	
//	/**加载应用需要用到的持久性数据*/
//	class LoadAppData extends AsyncTask
//	{
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(Object result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected Object doInBackground(Object... params) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	}
}
