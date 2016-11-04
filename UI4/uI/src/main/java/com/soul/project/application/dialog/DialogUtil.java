package com.soul.project.application.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soul.project.story.activity.R;

public class DialogUtil implements OnClickListener
{
	private Context context;
	public static DialogUtil dialogUtil;
	private TextView txtDialogTitle;
	private TextView txtDialogMessage;
	private EditText etInput;
	private Button btn1;
	private Button btn2;
	Dialog dialog;
	int type = 1;
	
	public static DialogUtil newInstance(Context context)
	{
//		if(dialogUtil == null)
			dialogUtil = new DialogUtil(context);
		return dialogUtil;
	}
	
	private DialogUtil(Context context)
	{
		this.context = context;
	}

    /** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public static Dialog createLoadingDialog(Context context, String msg) {  
  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.load_dialog, null);// 得到加载view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
        // main.xml中的ImageView  
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
        // 加载动画  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                context, R.anim.load_dialog_anim);  
        // 使用ImageView显示动画  
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
        if(msg == null || "".equals(msg))
        	msg = context.getString(R.string.action_loading);
        tipTextView.setText(msg);// 设置加载信息  
  
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog  
        
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局  
        return loadingDialog;  
    }  
    
    /**
     * 
     * @author 许仕永(xsy)
     * des: 
     * @param type 2|回复框   其他|消息框
     * @return
     */
    public Dialog getIOSDialog(int type)
    {
    	this.type = type;
    	
    	if(dialog == null)
    	{
    		dialog = new Dialog(context, R.style.myDialogTheme);// 创建自定义样式dialog  
    		LayoutInflater inflater = LayoutInflater.from(context);  
    		View v = inflater.inflate(R.layout.ios_dialog, null);// 得到加载view  
    		
    		txtDialogMessage = (TextView) v.findViewById(R.id.txtDialogMessage);
    		txtDialogTitle   = (TextView) v.findViewById(R.id.txtDialogTitle);
    		etInput = (EditText)v.findViewById(R.id.etInput);
    		btn1 = (Button)v.findViewById(R.id.btn1);// 现在回复
    		btn2 = (Button)v.findViewById(R.id.btn2);// 稍后再看
    		dialog.setContentView(v);
    		
    		btn1.setOnClickListener(this);
    		btn2.setOnClickListener(this);
    		
    		if(type == 2)
    		{
    			etInput.setVisibility(View.VISIBLE);
    			btn1.setText("发送回复");
    		}
    	}
        
		return dialog;
    }

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.btn1)
		{
			// 消息模式下点击回复
			if(type == 1)
			{
				type = 2;
				etInput.setVisibility(View.VISIBLE);
				btn1.setText("发送回复");
			}
		}
		else if(v.getId() == R.id.btn2)
		{
			if(dialog != null)
				dialog.dismiss();
		}
	}
}
