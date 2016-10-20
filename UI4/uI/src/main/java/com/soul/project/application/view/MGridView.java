package com.soul.project.application.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MGridView extends GridView
{

	public MGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

//	 public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	        //加上下面的话即可实现listview在scrollview中滑动
//	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//	        System.out.println("expandSpec = " + expandSpec);
//	        super.onMeasure(widthMeasureSpec, 500);
//	 }
	
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
 
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
                MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}
