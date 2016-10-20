package com.bssy.customui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivityNoTitleFullScreen extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                      WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏  
	}
}
