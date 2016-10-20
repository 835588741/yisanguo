package com.bssy.customui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import com.bssy.customui.R;

/**
 * 简单日期选择器
 * @file XSYSimpleDataPickerView.java
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui.widget
 * @todo  TODO
 * @date 2016年3月15日 下午1:08:03
 */
public class XSYSimpleDataPickerView extends LinearLayout implements OnClickListener
{
	private NumberPicker np1,np2,np3;
	private LayoutInflater inflater;
	private static String str1 = "1999";
	private static String str2 = "1";
	private static String str3 = "1";
//	private Button btnConfirm;
	Button btn1;
	Button btn2;
	private ConfirmCallBack callBack;
	private OnClickListener cancelClick;
	
	public XSYSimpleDataPickerView(Context context)
	{
		super(context);
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.simple_datepicker_view, null);
		// -1 match_parent   -2 warp_parent
		this.addView(view, -1, -2);
		
		initView();
		initEvent();
	}
	
	public XSYSimpleDataPickerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.simple_datepicker_view, null);
		// -1 match_parent   -2 warp_parent
		this.addView(view, -1, -2);
		
		initView();
		initEvent();
	}
	
	/**
	 * 设置确定选择日期监听
	 * @author 许仕永(xsy)
	 * des: 
	 * @param callBack
	 */
	public void setConfirmListener(ConfirmCallBack callBack,OnClickListener cancelClick)
	{
		this.callBack = callBack;
		this.cancelClick = cancelClick;
		
		if(cancelClick != null)
		{
			btn1.setOnClickListener(cancelClick);
		}
	}

	private void initEvent()
	{
		btn1.setOnClickListener(this);
	}

	private void initView()
	{
//		btnConfirm = (Button)findViewById(R.id.btnConfirm);
		np1 = (NumberPicker) findViewById(R.id.np1);
		np2 = (NumberPicker) findViewById(R.id.np2);
		np3 = (NumberPicker) findViewById(R.id.np3);

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btnConfirm);
//		
		btn2.setOnClickListener(this);
		
		if(cancelClick != null)
		{
			btn1.setOnClickListener(cancelClick);
		}
		
		np1.setMaxValue(2299);
		np1.setMinValue(1970);
		np1.setValue(1999);
		np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str1 = np1.getValue() + "";
				if (Integer.parseInt(str1) % 4 == 0
						&& Integer.parseInt(str1) % 100 != 0
						|| Integer.parseInt(str1) % 400 == 0) {
					if(str2.equals("1")||str2.equals("3")||str2.equals("5")||str2.equals("7")||str2.equals("8")||str2.equals("10")||str2.equals("12")){
						np3.setMaxValue(31);
						np3.setMinValue(1);
					}else if(str2.equals("4")||str2.equals("6")||str2.equals("9")||str2.equals("11")){
						np3.setMaxValue(30);
						np3.setMinValue(1);
					}else{
							np3.setMaxValue(29);
							np3.setMinValue(1);
						}
					
				} else {
					if(str2.equals("1")||str2.equals("3")||str2.equals("5")||str2.equals("7")||str2.equals("8")||str2.equals("10")||str2.equals("12")){
						np3.setMaxValue(31);
						np3.setMinValue(1);
					}else if(str2.equals("4")||str2.equals("6")||str2.equals("9")||str2.equals("11")){
						np3.setMaxValue(30);
						np3.setMinValue(1);
					}else{
							np3.setMaxValue(28);
							np3.setMinValue(1);
						}
				}
			}
		});

		np2.setMaxValue(12);
		np2.setMinValue(1);
		np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str2 = np2.getValue()+"";
				if(str2.equals("1")||str2.equals("3")||str2.equals("5")||str2.equals("7")||str2.equals("8")||str2.equals("10")||str2.equals("12")){
					np3.setMaxValue(31);
					np3.setMinValue(1);
				}else if(str2.equals("4")||str2.equals("6")||str2.equals("9")||str2.equals("11")){
					np3.setMaxValue(30);
					np3.setMinValue(1);
				}else{
					if (Integer.parseInt(str1) % 4 == 0
							&& Integer.parseInt(str1) % 100 != 0
							|| Integer.parseInt(str1) % 400 == 0) {
						np3.setMaxValue(29);
						np3.setMinValue(1);
					} else {
						np3.setMaxValue(28);
						np3.setMinValue(1);
					}
				}
			}
		});

		np3.setMaxValue(31);
		np3.setMinValue(1);
		np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				str3 = np3.getValue()+"";
			}
		});
	}

	/** 获取日期 yyyy-mm-dd*/
	public static String getDate(){
		return str1+"-"+str2+"-"+str3;
	}
	
	/** 获取年份*/
	public static String getYear(){
		return str1;
	}
	
	/** 获取月份*/
	public static String getMonth(){
		return str2;
	}
	
	/** 获取天 */
	public static String getDay(){
		return str3;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnConfirm)
		{
			if(str2.length() == 1)
				str2 = "0"+str2;
			if(str3.length() == 1)
				str3 = "0"+str3;
			
			callBack.callBack(str1+str2+str3);
//			hide();
		}
	}
	//8000 - 
	
	/** 显示日期选择视图*/
	public void show()
	{
		if(!this.isShown())
			this.setVisibility(View.VISIBLE);
	}
	
	/** 隐藏日期选择视图*/
	public void hide()
	{
		if(this.isShown())
			this.setVisibility(View.GONE);
	}
	
	/** 回调接口*/
	public interface ConfirmCallBack
	{
		public void callBack(String data);
	}
	
}
