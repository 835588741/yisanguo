/**
 * 
 */
package com.bssy.customui.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/** 手机信息管理
 * @file XSYPhoneUtil.java
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui.util
 * @todo  TODO
 * @date 2016年3月17日 上午11:32:59
 */
public class XSYPhoneUtil
{
	/**
	 * 获取手机屏幕信息
	 * @author 许仕永(xsy)
	 * des: 
	 * @param context
	 * @return 返回浮点型数组 各下标位分别为 [0]屏幕宽度、[1]屏幕高度、[2]屏幕密度、[3]屏幕密度DPI
	 */
	public static float[] getScreenWH(Context context)
	{
		DisplayMetrics metric = new DisplayMetrics();  
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int width = metric.widthPixels;     // 屏幕宽度（像素）  
		int height = metric.heightPixels;   // 屏幕高度（像素）  
		float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）  
		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240） 
		
		return new float[]{width,height,density,densityDpi}; 
	}
	
//	Resources resources = context.getResources();
//	DisplayMetrics dm = resources.getDisplayMetrics();
//	float density1 = dm.density;
//	int width = dm.widthPixels;
//	int height = dm.heightPixels;
}
