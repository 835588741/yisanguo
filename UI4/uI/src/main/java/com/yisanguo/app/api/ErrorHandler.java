/**
 * 
 */
package com.yisanguo.app.api;

import android.app.Activity;
import android.content.Context;

import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.story.activity.LoginActivity;
import com.soul.project.story.activity.MyApplication;

/** 错误集中处理器
 * @file ErrorHandler.java
 * @author 许仕永(xsy)
 * @package_name com.yisanguo.app.api
 * @todo  TODO
 * @date 2016-6-18 下午7:42:43
 */
public class ErrorHandler
{
	public static boolean handlerError(Context context)
	{
		if(MyApplication.user.getUuid() == null || "".equals(MyApplication.user.getUuid()))
		{
			ToastUtil.show(context, "游戏状态失效，请重新登录", ToastUtil.ERROR);
			ActivityUtil.goToNewActivity((Activity) context, LoginActivity.class);
			return false;
		}
		return true;
	}
}
