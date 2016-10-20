///**
// * 文件名：CustomProgressDialog.java 2014-7-21 下午2:54:51
// * @author Administrator 	
// */
//package com.soul.project.application.dialog;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.TextView;
//import com.soul.project.story.activity.R;
//
///** 对话框进度条
// * 创建时间: 2014-7-21 下午2:54:51
// *
// * @author xsy
// * 项目名称: immomo
// * 文件名: CustomProgressDialog.java
// * 编码: 
// * @Description：
// * @JKD JDK 1.6.0_21 
// * @version v1.0
// * @TODO
// */
//public class CustomProgressDialog
//{
//	/**
//	 * 获取进度条对话框
//	 * @param context 上下文对象
//	 * @param title   对话框标题
//	 * @param mess	    对话框显示的内容消息
//	 * @return		    返回预设的进度条对话框	  
//	 */
//	public static ProgressDialog getProgressDiaolg(Context context,String title,String mess,int textSize)
//	{
//		ProgressDialog dialog = ProgressDialog.show(context, title, mess);
//		dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.cmp_progress_dialog_indeterminate));
//		if(textSize != -1)
//		{
//			View v = dialog.getWindow().getDecorView();//取到dialog的整个view
//			setDialogText(v,textSize);//并对该view进行设置
//		}
//		return dialog;
//	}
//	
//	/**
//	 * 无标题进度条对话框 (特定，)
//	 * @param context	上下文对象
//	 * @param mess      对话框显示的内容消息
//	 * @param textSize  需要设置对话框中的字体大小 ， 传入-1 则维持默认值不变
//	 * @return			返回预设的进度条对话框	  
//	 */
//	public static ProgressDialog getProgressDiaolgNoTitle(Context context,String mess,int textSize)
//	{
//		ProgressDialog dialog = ProgressDialog.show(context, null, mess);
//		dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.cmp_progress_dialog_indeterminate));
//		
////		setDialogSize(context,dialog,0,0);
//		
//		if(textSize != -1)
//		{
//			View v = dialog.getWindow().getDecorView();//取到dialog的整个view
//			setDialogText(v,textSize);//并对该view进行设置
//		}
//		
//		return dialog;
//	}
//	
//	
//	/**
//	 * 进度条对话框 (可以设置宽高)
//	 * @param context	上下文对象
//	 * @param mess      对话框显示的内容消息
//	 * @param textSize  需要设置对话框中的字体大小 ， 传入-1 则维持默认值不变
//	 * @return			返回预设的进度条对话框	  
//	 */
//	public static ProgressDialog getProgressDiaolg(Context context,String mess,int textSize,int width,int height)
//	{
//		ProgressDialog dialog = ProgressDialog.show(context, null, mess);
//		dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.progressdialog_rotate));
//		
//		setDialogSize(context,dialog,width,height);
//		
//		if(textSize != -1)
//		{
//			View v = dialog.getWindow().getDecorView();//取到dialog的整个view
//			setDialogText(v,textSize);//并对该view进行设置
//		}
//		
//		return dialog;
//	}
//	
//	/**
//	 * 设置Dialog的Size
//	 * @param context
//	 * @param dialog
//	 * @param height 期望的进度条对话框高度
//	 * @param width  期望的进度条对话框宽度
//	 */
//	private static void setDialogSize(Context context, ProgressDialog dialog,int width,int height)
//	{
//		// TODO Auto-generated method stub
//		
//		 int screenWidth;
//		 int screenHeight;
//		
//			// 获取屏幕密度
//		 DisplayMetrics dm = new DisplayMetrics();
//		 dm = context.getResources().getDisplayMetrics();
//		 
//		 //宽度处理
//		 if(width == 0)
//			 screenWidth = dm.widthPixels; // 屏幕宽
//		 else
//			 screenWidth = width;
//		 
//		 //高度处理
//		 if(height == 0)
//			 screenHeight = dm.heightPixels; // 屏幕高
//		 else
//			 screenHeight = height;
//
//		 
//		// 获取对话框当前的参数值
//		 WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //
//		
//		 p.height = (int) WindowManager.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6
//		 p.width = (int) (screenWidth * 0.65); // 宽度设置为屏幕的0.75
//		
//		 dialog.getWindow().setAttributes(p);
//	}
//
//	/**
//	  * 设置ProgressDialog中的显示字体 
//	  * @param v  ProgressDialog对象转为View传入
//	  * @param textSize  字体大小
//	  */
//	private static void setDialogText(View v,int textSize)
//	{
//		//如果属于视图组，则需要再回调以实现获取TextView设置字体大小
//		if(v instanceof ViewGroup)
//		{
//			ViewGroup parent = (ViewGroup) v;
//			//获取子视图总数，并循环
//			int count = parent.getChildCount();
//			for ( int i = 0; i < count; i++ )
//			{
//				View child = parent.getChildAt(i);
//				setDialogText(child,textSize);
//			}
//		} 
//		//判断视图是否属于TextView类型 ，如果确定，则对该视图对象设置字体
//		else if(v instanceof TextView)
//		{
//			((TextView) v).setTextSize(textSize); // 是textview，设置字号
//		}
//	}
//}
