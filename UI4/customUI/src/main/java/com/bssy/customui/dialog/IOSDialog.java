/**
 * 
 */
package com.bssy.customui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bssy.customui.R;
import com.bssy.customui.widget.XSYSimpleDataPickerView;
import com.bssy.customui.widget.XSYSimpleDataPickerView.ConfirmCallBack;

/** 仿IOS系统对话框
 * dialog = new IOSDialog(this).settingDialog(IOSDialog.TYPE_DATA_PICK_DIALOG
 * @file IOSDialog.java
 * @author 许仕永(xsy)
 * @package_name com.dearmax.gathering.dialog
 * @todo  TODO
 * @date 2016年5月26日 下午3:17:21
 */
public class IOSDialog extends Dialog
{
	Dialog dialog;
	Context context;
	LayoutInflater inflater;
	/** 常规单按钮对话框*/
	public static final int TYPE_NORMAL_SIGLE_BUTTON_DIALOG = 1;
	/** 常规双按钮对话框*/
	public static final int TYPE_NORMAL_DOUBLE_BUTTON_DIALOG = 2;	
	/** item 选择对话框*/
	public static final int TYPE_ITEM_SELECT_DIALOG = 3;
	/** 日期选择对话框*/
	public static final int TYPE_DATA_PICK_DIALOG = 4;
//	public IOSDialog iosDialog;
	
	public IOSDialog(Context context)
	{
		super(context);
		this.context = context;
		dialog = new Dialog(context, R.style.dialogThemeForIOS);
		inflater = LayoutInflater.from(context);
	}
	
//	public static IOSDialog newInstance(Context context)
//	{
//		if(iosDialog == null)
//			iosDialog = new IOSDialog(context);
//		return iosDialog;
//	}
	
	/**
	 * 
	 * @author 许仕永(xsy)
	 * des: 
	 * @param type 1|常规标题内容单按钮对话框  2|常规标题内容双按钮对话框  3|item 选择对话框
	 * @param title  对话框标题
	 * @param message 对话框内容
	 * @param button1Name 按钮1名字
	 * @param button1Event 按钮1时间
	 * @param button2Name
	 * @param button2Event
	 * @param gravity 设置位置 居中下上左右
	 * @return 返回Dialog实例对象
	 */
	public Dialog settingDialog(int type,String title,String message,String button1Name,android.view.View.OnClickListener button1Event,String button2Name,android.view.View.OnClickListener button2Event,ConfirmCallBack confirmCallBack,int gravity)
	{
		Window window = dialog.getWindow();
		if(gravity != 0)
			window.setGravity(gravity); // 此处可以设置dialog显示的位置
		else
			window.setGravity(Gravity.CENTER);
		
		DisplayMetrics dm = new DisplayMetrics();

		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (mScreenWidth - mScreenWidth / 8); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		
//		TextView textView = new TextView(context);
//		textView.setText("测试测试---->");
//		dialog.setContentView(textView);
//		
		// 双item 选择对话框
		if(type == TYPE_ITEM_SELECT_DIALOG)
		{
			View view = inflater.inflate(R.layout.dialog_ios_style2, null);
			Button btn1 = (Button) view.findViewById(R.id.btn1);
			Button btn2 = (Button) view.findViewById(R.id.btn2);
			
			if(button1Name != null)
				btn1.setText(button1Name);
			if(button1Event != null)
				btn1.setOnClickListener(button1Event);
			
			if(button2Name != null)
				btn2.setText(button2Name);
			if(button2Event != null)
				btn2.setOnClickListener(button2Event);
			
			dialog.setContentView(view);
		}
		// 日期选择对话框
		else if(TYPE_DATA_PICK_DIALOG == type)
		{
			View view = inflater.inflate(R.layout.dialog_ios_style3, null);
			
			XSYSimpleDataPickerView dataPickerView = (XSYSimpleDataPickerView)view.findViewById(R.id.datapick);
			if(confirmCallBack != null)
				dataPickerView.setConfirmListener(confirmCallBack,button2Event);
//			Button btn1 = (Button) view.findViewById(R.id.btn1);
//			Button btn2 = (Button) view.findViewById(R.id.btn2);
//			
//			if(button1Name != null)
//				btn1.setText(button1Name);
//			if(button1Event != null)
//				btn1.setOnClickListener(button1Event);
//			
//			if(button2Name != null)
//				btn2.setText(button2Name);
//			if(button2Event != null)
//				btn2.setOnClickListener(button2Event);
			
			dialog.setContentView(view);
		}
		// 普通信息提示对话框 （单按钮，双按钮）
		else
		{
			View view = inflater.inflate(R.layout.dialog_ios_style, null);
			TextView txtDialogTitle   = (TextView) view.findViewById(R.id.txtDialogTitle);
			TextView txtDialogMessage = (TextView) view.findViewById(R.id.txtDialogMessage);
			Button btn1 = (Button) view.findViewById(R.id.btn1);
			Button btn2 = (Button) view.findViewById(R.id.btn2);
			View lineView = (View) view.findViewById(R.id.lineview);
			
			// 单按钮
			if(type == TYPE_NORMAL_SIGLE_BUTTON_DIALOG)
			{
				if(button1Name != null)
					btn1.setText(button1Name);
				if(button1Event != null)
					btn1.setOnClickListener(button1Event);
				
				btn2.setVisibility(View.GONE);
				lineView.setVisibility(View.GONE);
			}
			// 双按钮
			else if(type == TYPE_NORMAL_DOUBLE_BUTTON_DIALOG)
			{
				btn2.setVisibility(View.VISIBLE);
				lineView.setVisibility(View.VISIBLE);
				
				if(button1Name != null)
					btn1.setText(button1Name);
				if(button1Event != null)
					btn1.setOnClickListener(button1Event);
				
				if(button2Name != null)
					btn2.setText(button2Name);
				if(button2Event != null)
					btn2.setOnClickListener(button2Event);
			}
			
			if(title != null)
				txtDialogTitle.setText(title);
			if(message != null)
				txtDialogMessage.setText(message);
			
			dialog.setContentView(view);
		}
		
		return dialog;
	}
	
	// default
	public void settingDialog(String title,String message)
	{
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
	}
}
