/**
 * 
 */
package com.springmvc.util;

import org.json.JSONException;
import org.json.JSONObject;

/** 
 * @file ResponeJsonObjectUtil.java
 * @author 许仕永(xsy)
 * @package_name com.springmvc.util
 * @todo  TODO
 * @date 2016年3月7日 上午9:31:07
 */
public class ResponeJsonObjectUtil
{
	public static JSONObject getJSONObject(String result,String respone,String descript)
	{
		JSONObject object = new JSONObject();
		try
		{
			object.put("respone", result);
			object.put("result", respone);
			object.put("descript", descript);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return object;
		}
		
		return object;
	}
}
