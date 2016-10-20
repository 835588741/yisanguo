package com.bssy.customui.util;

/**
 * 转换类
 * @file TransformationUtil.java
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui.util
 * @todo  TODO
 * @date 2016年7月21日 下午8:26:43
 */
public class XSYTransformationUtil
{
	/**
	 * 时间转换  格式（传入秒数 ，返回 分:秒 格式）
	 * @author 许仕永(xsy)
	 * des: 
	 * @param second
	 * @return
	 */
	public static String secontToMinAndSec(int second)
	{
		int min = second / 60;
		int sec = second % 60;
		
		String mins; 
		if(min > 0 && min < 10)
			mins = "0"+min;
		else if(min <= 0)
			mins = "00";
		else
			mins = ""+min;
		
		String secs;
		if(sec > 0 && sec < 10)
			secs = "0"+sec;
		else if(sec <= 0)
			secs = "00";
		else
			secs = ""+sec;
		
		return mins+":"+secs;
	}
}
