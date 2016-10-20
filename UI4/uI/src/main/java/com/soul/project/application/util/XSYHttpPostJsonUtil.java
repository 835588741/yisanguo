/**
 * 
 */
package com.soul.project.application.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/** 专用于http请求提交json数据段
 * @file PostMode.java
 * @author 许仕永(xsy)
 * @package_name com.dearmax.gathering.networkrequest
 * @todo TODO
 * @date 2016年3月22日 下午2:55:06
 */
public class XSYHttpPostJsonUtil
{
	Context context;
	PostCallBack postCallBack;
	private Thread threadForPatch;
	private Thread threadForPost;
	private static XSYHttpPostJsonUtil postMode;
	/** 请求成功 */
	public static final int TYPE_SUCCESS = 0;
	/** 请求失败 */
	public static final int TYPE_FAIL = 1;

	private XSYHttpPostJsonUtil(Context context)
	{

	}

	public static XSYHttpPostJsonUtil newInstance(Context context)
	{
		if (postMode == null)
			postMode = new XSYHttpPostJsonUtil(context);
		return postMode;
	}

	public void postTypeAsynchronous(String urlPath, String json,PostCallBack postCallBack)
	{
		if(threadForPost == null)
		{
			threadForPost = new Thread(new MRunnable("post",urlPath, json, postCallBack));
			threadForPost.start();
		}
	}
	
	/**
	 * Post请求
	 * @author 许仕永(xsy)
	 * des: 
	 * @param urlPath 请求网络路径
	 * @param json	   要post的json数据
	 * @param postCallBack  回调监听 在子线程的运行的，如果要在这两个方法内操作UI请使用Handler
	 */
	public void post(String urlPath, String json,PostCallBack postCallBack)
	{
		try
		{
			this.postCallBack = postCallBack;
			URL url = new URL(urlPath);
			String content = String.valueOf(json);
			// 现在呢我们已经封装好了数据,接着呢我们要把封装好的数据传递过去
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(8000);
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置User-Agent: Fiddler
			conn.setRequestProperty("ser-Agent", "Fiddler");
			// 设置contentType
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(content.getBytes());
			os.close();
			// 服务器返回的响应码
			int code = conn.getResponseCode();
			if (code == 200)
			{
				InputStream is = conn.getInputStream();
//				Log.i("XU", "得到的数据=" + inputStream2String(is));
				String s =  inputStream2String(is);
				if (postCallBack != null)
					postCallBack.onSuccess(s);
			}
			else
			{
				if (postCallBack != null)
					postCallBack.onFail(code);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			Log.i("XU", "post 方法异常" + e.toString());
		}
		finally
		{
			threadForPost = null;
		}
	}

	
	/**
	 * Post请求
	 * @author 许仕永(xsy)
	 * des: 
	 * @param urlPath 请求网络路径
	 * @param json	   要post的json数据
	 * @param postCallBack  回调监听 在子线程的运行的，如果要在这两个方法内操作UI请使用Handler
	 */
	public void put(String urlPath, String json,String token, PostCallBack postCallBack)
	{
		try
		{
			Log.i("XU", "toke="+token+"    json="+json);
			URL url = new URL(urlPath);
			String content = String.valueOf(json);
			// 现在呢我们已经封装好了数据,接着呢我们要把封装好的数据传递过去
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(8000);
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			// 设置User-Agent: Fiddler
			conn.setRequestProperty("ser-Agent", "Fiddler");
			// 设置contentType
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-SIMPLE-TOKEN", token);
			OutputStream os = conn.getOutputStream();
			os.write(content.getBytes());
			os.close();
			// 服务器返回的响应码
			int code = conn.getResponseCode();
			if (code == 200)
			{
				InputStream is = conn.getInputStream();

				if (postCallBack != null)
				{
					String data = inputStream2String(is);
					Log.i("XU", "得到的数据=" + data+"   postCallBack :"+(postCallBack == null?"true":"false"));
					postCallBack.onSuccess(data);
				}
			}
			else
			{
				if (postCallBack != null)
					postCallBack.onFail(code);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			Log.i("XU", "post 方法异常" + e.toString());
		}
	}

	

	
	/**
	 *  异步Asynchronous Patch请求
	 * @author 许仕永(xsy)
	 * des: 
	 * @param urlPath 请求网络路径
	 * @param json	   要post的json数据
	 * @param postCallBack  回调监听 在子线程的运行的，如果要在这两个方法内操作UI请使用Handler
	 */
	public void patchTypeAsynchronous(String urlPath, String json,String token, PostCallBack postCallBack)
	{
		if(threadForPatch == null)
		{
			threadForPatch = new Thread(new MRunnable("patch",urlPath, json, postCallBack));
			threadForPatch.start();
		}
	}
	
	private class MRunnable implements Runnable
	{
		String urlPath,json,type;
		PostCallBack postCallBack;
		
		public MRunnable(String type ,String urlPath, String json, PostCallBack postCallBack)
		{
			this.urlPath = urlPath;
			this.json = json ;
			this.type = type;
			this.postCallBack = postCallBack;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if("post".equals(type))
				post(urlPath, json, postCallBack);
		}
	}
	

	/**
	 * 请求时的回调接口 ，1.onFail请求失败，并带回错误码  2. onSuccess请求成功，带回json数据
	 * @file PostMode.java
	 * @author 许仕永(xsy)
	 * @package_name com.dearmax.gathering.networkrequest
	 * @todo  TODO
	 * @date 2016年3月22日 下午5:55:20
	 */
	public interface PostCallBack
	{
		public void onFail(int code);

		public void onSuccess(String json);
	}

	/**
	 * 工具 输入流转字符串
	 * @author 许仕永(xsy)
	 * des: 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1)
		{
			baos.write(i);
		}
		return baos.toString();
	}
}
