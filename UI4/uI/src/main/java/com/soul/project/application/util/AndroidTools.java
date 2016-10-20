package com.soul.project.application.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 创建时间：2013-5-18 下午5:39:05  
 * 项目名称：SmartSchool  
 * @author Linhj  
 * @version V1.0   
 * @JKD JDK 1.6.0_21  
 * @filename AndroidTools.java  
 * @Description：  
 * @copyright Copyright (c) 2013 Soul,Inc. All Rights Reserved.
 */
public class AndroidTools {

	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return 分辨率---宽
	 */
	public static int getWindowsWidth(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return 分辨率---高
	 */
	public static int getWindowsHeight(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	
	
}
