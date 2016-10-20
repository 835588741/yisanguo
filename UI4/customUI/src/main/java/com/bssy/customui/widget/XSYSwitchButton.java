/**
 * 
 */
package com.bssy.customui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.bssy.customui.R;

/**
 * @file XSYSwitchButton.java
   name="check" format="boolean"
   name="check_image" format="reference"
   name="uncheck_image" format="reference"
 * @author 许仕永(xsy)
 * @package_name com.bssy.customui.widget
 * @todo  TODO
 * @date 2016年5月23日 上午11:52:52
 */
public class XSYSwitchButton extends ImageButton implements OnClickListener
{
	private boolean state = false;
	private int checkImage ;
	private int uncheckImage ;
	private ChangeEvent changeEvent;
	
	public XSYSwitchButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.switchAttrs);//(attrs, R.styleable.XSYCircleImageView, defStyle, 0);
		state = a.getBoolean(R.styleable.switchAttrs_check, false);
		checkImage = a.getResourceId(R.styleable.switchAttrs_check_image, R.drawable.image_open_status);
		uncheckImage = a.getResourceId(R.styleable.switchAttrs_uncheck_image, R.drawable.image_close_status);
		
		a.recycle();
		if(state)
			this.setBackgroundResource(checkImage);
		else
			this.setBackgroundResource(uncheckImage);
		
		this.setOnClickListener(this);
	}
	
	public boolean isCheck()
	{
		return state;
	}
	
	public void setCheck(boolean isCheck)
	{
		state = isCheck;
		if(state)
			this.setBackgroundResource(checkImage);
		else
			this.setBackgroundResource(uncheckImage);
	}
	
	public void setChangeEventCallBack(ChangeEvent changeEvent)
	{
		this.changeEvent = changeEvent;
	}
	
	public interface ChangeEvent
	{
		public void checkChange(boolean state);
	}

	@Override
	public void onClick(View v)
	{
		setCheck(!state);
		
		if(changeEvent != null)
		{
			changeEvent.checkChange(state);
		}
	}
}
