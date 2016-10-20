/**
 * 
 */
package com.bssy.customui.output;

import android.util.Log;

/** Log控制管理
 * @file XLog.java
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui.output
 * @todo  TODO
 * @date 2016年2月24日 下午1:41:42
 */
public class XLog
{
	private String tag = "XSY"; 
	private static XLog log;
	private int mode = 0; // 控制显示与否  // 0|调试开发模式  1|上线模式（不再打印）
	
	private XLog(String tag,int mode)
	{
		this.tag = tag;
		this.mode = mode;
	}
	private XLog()
	{
	}
	
	public static XLog newInatance(String tag,int mode)
	{
		if(log == null)
			log = new XLog(tag,mode);
		return log;
	}
	
	public static XLog newInatance()
	{
		if(log == null)
			log = new XLog();
		return log;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	
	public void d(String mess)
	{
		if(mode == 0)
			Log.d(tag, mess);
	}
	public void i(String mess)
	{
		if(mode == 0)
			Log.i(tag, mess);
	}
	public void w(String mess)
	{
		if(mode == 0)
			Log.w(tag, mess);
	}
	public void e(String mess)
	{
		if(mode == 0)
			Log.e(tag, mess);
	}
}
