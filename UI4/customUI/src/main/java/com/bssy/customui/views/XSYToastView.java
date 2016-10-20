package com.bssy.customui.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bssy.customui.R;

public class XSYToastView
{
//	enum TYPE
//	{
//		ERROR,WARN,INFO
//	}
	/**错误样式**/
	public static final int ERROR = 1;
	/**警告样式**/
	public static final int WARN  = 2;
	/**普通信息样式**/
	public static final int INFO  = 3;
	/**加载样式**/
	public static final int LOAD  = 4;
	/**成功提示样式**/
	public static final int SUCC  = 5;
	
	/** 长条形风格 该类风格下有 ERROR、WARN、 INFO、LOAD、SUCC五种样式*/
	public static final int STYLE_BAR = 1;
	/** 方形居中风格 该类风格下有 ERROR、SUCC两种样式*/
	public static final int STYLE_ROUND = 2;
	
	private int style = 1;
	
	/**
	 * Toast显示
	 * @param context
	 * @param str	显示的字符串
	 * @param typeOfToastShow	Toast类型 ---> ERROR、WARN、INFO、LOAD
	 */
	public static void show(Context context,String str,int typeOfToastShow)
	{
		Toast toast = new Toast(context);
		View view = getView(typeOfToastShow, context , str);
		toast.setView(view);
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**获取视图*/
	private static View getView(int type,Context context,String str)
	{
		View view = null;
		ImageView img = null;
		TextView txt = null;
		
		if(type == STYLE_BAR)
		{
			view = View.inflate(context, R.layout.cmp_rich_toast, null);
			img = (ImageView)view.findViewById(R.id.toast_img);
			txt = (TextView)view.findViewById(R.id.toast_txt);
			
			switch(type)
			{
				case ERROR:
					txt.setText(str);
					img.setBackgroundResource(R.drawable.toast_error);
					break;
				case WARN :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.toast_warn);
					break;
				case LOAD :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.toast_load);
					break;
				case SUCC :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.toast_success);
					break;
				default :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.toast_info);
			}
		}
		else
		{
			view = View.inflate(context, R.layout.view_rich_toast, null);
			txt = (TextView)view.findViewById(R.id.txtTest);
			img = (ImageView)view.findViewById(R.id.image);

			switch(type)
			{
				case ERROR:
					txt.setText(str);
					img.setBackgroundResource(R.drawable.image_toast_error);
					break;
				case SUCC :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.image_toast_success);
					break;
				default :
					txt.setText(str);
					img.setBackgroundResource(R.drawable.image_toast_success);
			}
		}

		return view;
	}

	/**
	 * 可以控制显示时间的Toast
	 * @param context
	 * @param str 显示的字符串
	 * @param typeOfToastShow	Toast类型 ---> ERROR、WARN、INFO、LOAD
	 * @param showTime		显示时间
	 */
	public static void show(Context context,String str,int typeOfToastShow,int showTime)
	{
		Toast toast = new Toast(context);
		View view = getView(typeOfToastShow, context , str);
		toast.setView(view);
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setDuration(showTime);
		toast.show();
	}
}
