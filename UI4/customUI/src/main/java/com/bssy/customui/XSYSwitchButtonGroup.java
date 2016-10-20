/**
 * 
 */
package com.bssy.customui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 仿IOS 2-3按钮SwitchButton的单选
 * @file XSYSwitchButtonGroup.java
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui
 * @todo  TODO
 * @date 2015-11-5 上午10:07:05
 */
public class XSYSwitchButtonGroup extends LinearLayout implements OnCheckedChangeListener
{
	private LayoutInflater inflater;
	private Context context;
	private View view;
	private LeftEvent leftEvent;
	private RightEvent rightEvent;
	private CenterEvent centerEvent;
	
	private int defaultBGColorSelected ;
	private int defaultBGColorUnSelected;
	private int defaultTextColorSelected ;
	private int defaultTextColorUnSelected;
	private int defaultBorderColor;
	private int defaultBorderSize;
	
	private int selectTextColor;
	private int unselectTextColor;
	private int selectBGColor;
	private int unselectBGColor;
	private int borderColor ;
	private int borderSize;
	private int buttonNum = 2;
	private int selectId = 0;
	
	private RadioButton leftRadioButton;
	private RadioButton rightRadioButton;
	private RadioButton centerRadioButton;
	
	
	public XSYSwitchButtonGroup(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		if(view == null)
		{
			inflater = LayoutInflater.from(context);
			initValue();
			initView(attrs);
		}
	}

	private void initValue()
	{
		// TODO Auto-generated method stub
		defaultBGColorSelected = getResources().getColor(android.R.color.holo_blue_light);
		defaultBGColorUnSelected = getResources().getColor(android.R.color.white);
		
		defaultTextColorSelected = getResources().getColor(android.R.color.white);
		defaultTextColorUnSelected = getResources().getColor(android.R.color.holo_blue_bright);
		
		defaultBorderColor = getResources().getColor(android.R.color.holo_blue_bright);
		defaultBorderSize = 1;
	}

	private void initView(AttributeSet attrs)
	{
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.switch_buttons_layout, null);
		this.addView(view);
		
		RadioGroup group = (RadioGroup)view.findViewById(R.id.radiogroup);
		group.setOnCheckedChangeListener(this);
		
		leftRadioButton = (RadioButton)view.findViewById(R.id.rbLeft);
		rightRadioButton = (RadioButton)view.findViewById(R.id.rbRight);
		centerRadioButton = (RadioButton)view.findViewById(R.id.rbCenter);
		
		// 获取属性
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.switchButtonsAttrs);
		
		// 获取用户设置的按钮数量
		buttonNum = a.getInteger(R.styleable.switchButtonsAttrs_numOfButton,2);
		
		// 获取用户设置的按钮文本
		String centerString = a.getString(R.styleable.switchButtonsAttrs_centerString);
		String rightString = a.getString(R.styleable.switchButtonsAttrs_rightString);
		String leftString = a.getString(R.styleable.switchButtonsAttrs_leftString);
		

		// 获取用户设置的各类颜色，如果没有就采用默认值
		selectTextColor = a.getColor(R.styleable.switchButtonsAttrs_selectTextColor, defaultTextColorSelected);
		unselectTextColor = a.getColor(R.styleable.switchButtonsAttrs_unselectTextColor, defaultTextColorUnSelected);
		selectBGColor = a.getColor(R.styleable.switchButtonsAttrs_selectBGColor, defaultBGColorSelected);
		unselectBGColor = a.getColor(R.styleable.switchButtonsAttrs_unselectBGColor, defaultBGColorUnSelected);
		borderColor = a.getColor(R.styleable.switchButtonsAttrs_borderColor, defaultBorderColor);
		borderSize = a.getInteger(R.styleable.switchButtonsAttrs_borderSize, defaultBorderSize);
		
		// 设置边框 --> 原理:为单选组设置总背景，再在单选组内设置padding为 1dp
		group.setBackgroundColor(borderColor);
		// 设置边框粗细尺寸
		group.setPadding(borderSize, borderSize, borderSize, borderSize);
		
		// 为按钮设置文本
		if(centerString != null)
			centerRadioButton.setText(centerString);
		if(leftString != null)
			leftRadioButton.setText(leftString);
		if(rightString != null)
			rightRadioButton.setText(rightString);
		
		leftRadioButton.setBackgroundColor(selectBGColor);
		rightRadioButton.setBackgroundColor(unselectBGColor);
		centerRadioButton.setBackgroundColor(unselectBGColor);
		
		leftRadioButton.setTextColor(selectTextColor);
		rightRadioButton.setTextColor(unselectTextColor);
		centerRadioButton.setTextColor(unselectTextColor);
		
		// 如果用户设置了按钮数为2，或者未设置默认2的话，隐藏中间那个按钮
		if(buttonNum == 2)
		{
			centerRadioButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		Log.i("XU","select---?");
		if(checkedId == R.id.rbLeft)
		{
			select(0);
			if(leftEvent   == null) return ; leftEvent.leftEvent();
		}
		else if(checkedId == R.id.rbRight)
		{
			select(1);
			if(rightEvent  == null) return ; rightEvent.rightEvent();
		}
		else if(checkedId == R.id.rbCenter)
		{
			select(2);
			if(centerEvent == null) return ; centerEvent.centerEvent();
		}
	}
	
	// 选定的按钮处理
	private void select(int id)
	{
		// TODO Auto-generated method stub
		Log.i("XU","select---?"+id);
		if(id == 0)
		{
			leftRadioButton.setTextColor(selectTextColor);
			leftRadioButton.setBackgroundColor(selectBGColor);
			
			rightRadioButton.setTextColor(unselectTextColor);
			rightRadioButton.setBackgroundColor(unselectBGColor);
			
			// 小优化， 如果不存在中间按钮则不为按钮（RadioButton）设置属性
			if(buttonNum > 2)
			{
				centerRadioButton.setTextColor(unselectTextColor);
				centerRadioButton.setBackgroundColor(unselectBGColor);
			}
		}
		else if(id == 1)
		{
			leftRadioButton.setTextColor(unselectTextColor);
			leftRadioButton.setBackgroundColor(unselectBGColor);
			
			rightRadioButton.setTextColor(selectTextColor);
			rightRadioButton.setBackgroundColor(selectBGColor);
			
			// 小优化， 如果不存在中间按钮则不为按钮（RadioButton）设置属性
			if(buttonNum > 2)
			{
				centerRadioButton.setTextColor(unselectTextColor);
				centerRadioButton.setBackgroundColor(unselectBGColor);
			}
		}
		else
		{
			leftRadioButton.setTextColor(unselectTextColor);
			leftRadioButton.setBackgroundColor(unselectBGColor);
			
			rightRadioButton.setTextColor(unselectTextColor);
			rightRadioButton.setBackgroundColor(unselectBGColor);
			
			centerRadioButton.setTextColor(selectTextColor);
			centerRadioButton.setBackgroundColor(selectBGColor);
		}
	}
	

	/** 传导事件*/
	public void setLeftEvent(LeftEvent leftEvent)
	{
		this.leftEvent = leftEvent;
	}
	
	
	public void setRightEvent(RightEvent rightEvent)
	{
		this.rightEvent = rightEvent;
	}
	
	
	public void setCenterEvent(CenterEvent centerEvent)
	{
		this.centerEvent = centerEvent;
	}

	
	/** 事件接口*/
	public interface CenterEvent
	{
		public void centerEvent();
	};
	
	public interface LeftEvent
	{
		public void leftEvent();
	};
	
	public interface RightEvent
	{
		public void rightEvent();
	};
}
