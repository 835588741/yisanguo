package com.soul.project.application.util;

import android.widget.EditText;

public class EditTextUtil {

	public static String getTextValue(EditText editText) {
		// TODO Auto-generated method stub
		String temp = editText.getText().toString();
		if(temp == null || temp.trim().equals(""))
		{
			editText.setError("不能为空");
			return null;
		}
		else 
			return temp;
			
	}
}
