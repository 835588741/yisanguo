package com.soul.project.application.dialog;

import android.app.AlertDialog.Builder;
import android.content.Context;

public class MBuilder extends Builder
{
	String tagString;
	
	public MBuilder(Context arg0)
	{
		super(arg0);
	}
	
	public MBuilder(Context context,String tagString)
	{
		super(context);
		this.tagString = tagString;
	}

	public String getTagString()
	{
		return tagString;
	}

	public void setTagString(String tagString)
	{
		this.tagString = tagString;
	}
	
	
}
