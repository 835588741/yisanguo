/**
 * 文件名：ActivityUtil.java 2015-5-2 下午9:45:35
 * @author Administrator 	许仕永
 */
package com.soul.project.application.util;

import android.app.Activity;
import android.content.Intent;

import com.soul.project.story.activity.R;

/**Description : Activity常用工具类
 * Create time : 2015-5-2 下午9:45:35
 * Project name: King
 * File name   : ActivityUtil.java
 * Encoded     : UTF-8
 * @author     许仕永
 * @JKD        JDK 1.6.0_21 
 * @version    v1.0.0
 */
public class ActivityUtil
{
    /** 跳转到目标Activity并填充补间动画*/
    public static void goToNewActivityWithComplement(Activity activity, Class targeClass)
	{
    	Intent intent = new Intent(activity, targeClass);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
    
    /** 跳转到目标Activity并填充补间动画*/
    public static void goToNewActivityWithComplement(Activity activity,Intent intent)
	{
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
    
    /** 跳转到目标Activity*/
    public static void goToNewActivity(Activity activity, Class targeClass)
	{
    	Intent intent = new Intent(activity, targeClass);
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

    /** 跳转指定活动，并传返回码*/
	public static void goToNewActivityForResult(Activity activity,Class targeClass, int resultCode)
	{
    	Intent intent = new Intent(activity, targeClass);
		activity.startActivityForResult(intent, resultCode);
		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
