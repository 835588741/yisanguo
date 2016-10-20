/**
 * 
 */
package com.bssy.customui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 搴旂敤缁熶竴椋庢牸鏍囬鏍�
 * @file AppTitleBar.java
 * @author 璁镐粫姘�xsy)
 * @package_name com.soul.project.application.view
 * @todo  TODO
 * @date 2015-11-2 涓嬪崍5:11:58
 *    
        "leftViewIcon" format="reference"
        "rightViewIcon" format="reference"
        
        "leftViewStringColor" format="color"
        "rightViewStringColor" format="color"
        
        "leftViewString" format="string"
        "rightViewString" format="string"
        
        "titlebarString" format="string"
        "titlebarStringColor" format="color"
        
        "hidden_left_icon" format="bloean"
        "hidden_right_icon" format="bloean"
        
        "leftViewTextBottomLine" format="boolean"
        "rightViewTextBottomLine" format="boolean"
        
        name="leftButtonClickable" format="boolean"
        
        "centerViewIcon" format="reference"
 
 */
public class XSYAppTitleBar extends RelativeLayout
{
	private LayoutInflater inflater;
	private TextView leftView;
	private TextView rightView;
	private TextView centerView;
	private LinearLayout layoutLeft;
	private LinearLayout layoutRight;
	private LinearLayout layoutSearchBar;
	private boolean isShowSearchBar = false;
	private Context context;
	private int rightIcon = -1;
	private String rightString;
	private EditText editText;
	private boolean leftButtonClickable ;
	private LeftButtonClickEvent leftButtonClickEvent;
	private RightButtonCLickEvent rightButtonCLickEvent;

	
	public XSYAppTitleBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init(context,attrs,0);
	}
	
	public XSYAppTitleBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs);
		this.context = context;
		init(context,attrs,defStyle);
	}


	private void init(Context context, AttributeSet attrs, int defStyle)
	{
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.apptitlebar, null);
		this.addView(view);
		initView(view);
		
		initValue(context,attrs,defStyle);
		initEvent();
	}
	
	public void setRightClickAble(boolean b)
	{
		layoutRight.setClickable(b);
	}
	
	public boolean getRightClickAble()
	{
		return layoutRight.isClickable();
	}


	//鍒濆鍖栦簨浠�titlebar 宸﹀彸涓ょ鐨勪袱涓姛鑳借彍鍗�
	private void initEvent()
	{
		// TODO Auto-generated method stub
		layoutLeft.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(leftButtonClickEvent == null)
				{
					if(leftButtonClickable)
						((Activity)context).finish();
				}
				else
					leftButtonClickEvent.leftEvent();
			}
		});
		
		layoutRight.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(rightButtonCLickEvent == null)
					;
//					Intent intent = new Intent(context, MainActivity.class);
				else
					rightButtonCLickEvent.rightEvent();
			}
		});
	}
	
	/**
	 * 涓哄乏绔寜閽缃簨浠�
	 * @author 璁镐粫姘�xsy)
	 * des: 
	 * @param leftButtonClickEvent
	 */
	public void setLeftButtonClickEvent(LeftButtonClickEvent leftButtonClickEvent)
	{
		this.leftButtonClickEvent = leftButtonClickEvent;
	}
	
	/**
	 * 涓哄彸绔寜閽缃簨浠�
	 * @author 璁镐粫姘�xsy)
	 * des: 
	 * @param rightButtonCLickEvent
	 */
	public void setRightButtonClickEvent(RightButtonCLickEvent rightButtonCLickEvent)
	{
		this.rightButtonCLickEvent = rightButtonCLickEvent;
	}

	// 宸﹀彸绔寜閽殑浜嬩欢鎺ュ彛瀹氫箟
	public interface LeftButtonClickEvent
	{
		public void leftEvent();
	}
	
	public interface RightButtonCLickEvent
	{
		public void rightEvent();
	}

	private void initView(View view)
	{
		// TODO Auto-generated method stub
		editText = (EditText)view.findViewById(R.id.etSearchValue);
		leftView = (TextView)view.findViewById(R.id.include_view_btnLeft);
		rightView = (TextView)view.findViewById(R.id.include_view_btnRight);
		centerView = (TextView)view.findViewById(R.id.include_view_titlebar_text);
		layoutLeft = (LinearLayout)view.findViewById(R.id.layoutLeft);
		layoutRight = (LinearLayout)view.findViewById(R.id.layoutRight);
		layoutSearchBar = (LinearLayout)view.findViewById(R.id.layoutSearchBar);
	}
	

	public void showSearchBar()
	{
		// 已经显示
		if(isShowSearchBar)
		{
			isShowSearchBar = false;
			layoutSearchBar.setVisibility(View.GONE);
			centerView.setVisibility(View.VISIBLE);
			
			if(rightString != null)
				setRightString(rightString);
			else
				setRightString("");
			
			if(rightIcon != -1)
				rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightIcon, 0);
		}
		else
		{
			isShowSearchBar = true;
			layoutSearchBar.setVisibility(View.VISIBLE);
			centerView.setVisibility(View.GONE);
			
			setRightString("搜索");
			rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
	}
	
	public boolean isShowSearchBar()
	{
		return isShowSearchBar;
	}
	
	public String getSearchBarValue()
	{
		return editText.getText().toString();
	}
	
	/**
	 * 鍒濆鍖栧睘鎬у�
	 * @author 璁镐粫姘�xsy)
	 * des: 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	private void initValue(Context context, AttributeSet attrs, int defStyle)
	{
		// TODO Auto-generated method stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.apptitlebar);//.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
		
		int leftIcon = a.getResourceId(R.styleable.apptitlebar_leftViewIcon, -1);
		String leftString  = a.getString(R.styleable.apptitlebar_leftViewString);
		int leftStringColor = a.getColor(R.styleable.apptitlebar_leftViewStringColor , -1);
		
		rightIcon = a.getResourceId(R.styleable.apptitlebar_rightViewIcon, -1);
		rightString = a.getString(R.styleable.apptitlebar_rightViewString);
		
		leftButtonClickable = a.getBoolean(R.styleable.apptitlebar_leftButtonClickable, true);
		
		int rightStringColor = a.getColor(R.styleable.apptitlebar_rightViewStringColor, -1);

		int centerIcon = a.getResourceId(R.styleable.apptitlebar_centerViewIcon, -1);
		
		String titlebarString = a.getString(R.styleable.apptitlebar_titlebarString);
		int titlebarStringColor = a.getColor(R.styleable.apptitlebar_titlebarStringColor, -1);
		
		// 鍙崇榛樿灏辨病鏈夛紝鎵�互涓嶉渶瑕佸彟璁炬鍙傛暟鏉ュ垽鏂槸鍚﹂殣钘忓彸瑙嗗浘
		boolean hiddenLeft  = a.getBoolean(R.styleable.apptitlebar_hidden_left_icon, false);
//		boolean hiddenRight = a.getBoolean(R.styleable.apptitlebar_hidden_right_icon, false);
		
		//宸﹀彸涓よ竟鐨勬枃瀛楁槸鍚﹂渶瑕佷笅鍒掔嚎
		boolean leftViewTextBottomLine  = a.getBoolean(R.styleable.apptitlebar_leftViewTextBottomLine, false);
		boolean rightViewTextBottomLine = a.getBoolean(R.styleable.apptitlebar_rightViewTextBottomLine, false);

		// 璁剧疆鏄惁鏄剧ず涓嬪垝绾�
		setRightViewTextWithBottomLine(rightViewTextBottomLine);
		setLeftViewTextWithBottomLine(leftViewTextBottomLine);
		
		if(hiddenLeft)
		{
			leftView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
		else
		{
			leftView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_return, 0, 0, 0);
		}
//		if(!hiddenRight)
//		{
//			
//		}
		
		if(leftIcon != -1)
			leftView.setCompoundDrawablesWithIntrinsicBounds(leftIcon, 0,0, 0);//setBackgroundResource(leftIcon);
		if(leftString != null)
			leftView.setText(leftString);
		if(leftStringColor != -1)
			leftView.setTextColor(leftStringColor);
		
		if(rightIcon != -1)
			rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0,rightIcon, 0);//setBackgroundResource(rightIcon);
		if(rightString != null)
			rightView.setText(rightString);
		if(rightStringColor != -1)
			rightView.setTextColor(rightStringColor);
		
		if(centerIcon != -1)
			centerView.setCompoundDrawablesWithIntrinsicBounds(0, 0,centerIcon, 0);
		
		if(titlebarString != null)
			centerView.setText(titlebarString);
		if(titlebarStringColor != -1)
			centerView.setTextColor(titlebarStringColor);
		
		a.recycle();
	}
	
	public void setRightViewIcon(int imageId)
	{
		if(imageId != -1)
			rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0,imageId, 0);//setBackgroundResource(rightIcon);
	}
	
	/** 璁剧疆titlebar宸︾鏂囨湰瑙嗗浘鏄惁鏄剧ず涓嬪垝绾*/
	public void setLeftViewTextWithBottomLine(boolean bool)
	{
		if(bool)
			leftView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//涓嬪垝绾�
	}
	
	/** 璁剧疆titlebar鍙崇鏂囨湰瑙嗗浘鏄惁鏄剧ず涓嬪垝绾*/
	public void setRightViewTextWithBottomLine(boolean bool)
	{
		if(bool)
			rightView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//涓嬪垝绾�
	}
	
	/****
	 * 涓嬪垪鏂规硶鏄洿鎺ョ敤灏嶈薄瑾跨敤渚嗚ō缃殑
	 * @author 璁镐粫姘�xsy)
	 * des: 
	 * @param title
	 */
	public void setRightViewOnClickEvent(OnClickListener clickListener)
	{
		rightView.setOnClickListener(clickListener);
	}
	
	public void setTitle(String title)
	{
		centerView.setText(title);
	}
	
	public void setTitleColor(int color)
	{
		centerView.setTextColor(color);
	}

	public void setLeftString(String leftString)
	{
		leftView.setText(leftString);
	}
	
	public void setRightString(String rightString)
	{
		rightView.setText(rightString);
	}
	
	public String getRightViewString()
	{
		return rightView.getText().toString();
	}

}
