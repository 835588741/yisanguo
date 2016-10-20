/**
 * 
 */
package com.soul.project.application.view;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bssy.customui.util.XSYTransformationUtil;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/**
 * @file MBuilderDialog.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.view
 * @todo  TODO
 * @date 2016年7月23日 下午1:14:59
 */
public class MValidDialog extends Dialog implements android.view.View.OnClickListener
{
	Timer timer;
	boolean isShow;
	Context context;
	boolean isRight = false;
	EditText etAnswer;
	Button btnSubmit;
	MTextView txtQuestion;
	MTextView txtCountTime;
	FinalHttp finalHttp;
	String answer;
	int time = 35;
	
	public MValidDialog(Context context)
	{
		super(context);
		this.context = context;
		this.setCancelable(false);
	}

	public MValidDialog(Context context, int theme)
	{
		super(context, theme);
		this.context = context;
		this.setCancelable(false);
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		setView();
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				if(!isRight)
				{
					this.cancel();
					handler.sendEmptyMessage(1);
				}
			}
		}, 36000);
		
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				time --;
				if(time >= 0)
					handler.sendEmptyMessage(2);
				else
					this.cancel();
			}
		}, 1000, 1000);
	}
	
	public void setQandA(String q,String a)
	{
		if(q != null && txtQuestion != null)
			txtQuestion.setText(q);
		answer = a;
	}
	
	public void setView()
	{
		View view = LayoutInflater.from(context).inflate(R.layout.layout_valid_dialog, null);
		if(view != null)
		{
			txtCountTime = (MTextView)view.findViewById(R.id.txtCountTime);
			txtQuestion = (MTextView) view.findViewById(R.id.txtQuestion);
			btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
			etAnswer = (EditText)view.findViewById(R.id.etAnswer);
			btnSubmit.setOnClickListener(this);
			this.setContentView(view);
		}
	}
	

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.btnSubmit)
		{
			if(etAnswer != null)
			{
				String ans = etAnswer.getText().toString();
				if(ans == null || ans.length() <= 0 )
				{
					isRight = false;
					isShow = true;
					Toast.makeText(context, "还未填写答案!!!", Toast.LENGTH_SHORT).show();
				}
				else 
				{
					if(answer != null && ans != null)
					{
						if(ans.trim().equals(answer.trim()))
						{
							isRight = true;
							isShow = false;
							// 验证通过
							Toast.makeText(context, "验证成功通过!!!", Toast.LENGTH_SHORT).show();
							MValidDialog.this.dismiss();
						}
						else
						{
							// 回答错误 则30%概率会发送报告到服务器
							Random random = new Random();
							int n = random.nextInt(3);
							if(n == 1)
								sendMes();
							isRight = false;
							isShow = false;
							Toast.makeText(context, "答案错误!验证失败!", Toast.LENGTH_SHORT).show();
							MValidDialog.this.dismiss();
							context.sendBroadcast(new Intent("exit_app"));
						}
					}
				}
			}
		}
	}
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 1)
			{
				if(MValidDialog.this != null && MValidDialog.this.isShowing())
				{
					// 超时未响应
					sendMes();
					Toast.makeText(context, "响应超时，为保护您的账号安全，暂时退出游戏。", Toast.LENGTH_LONG).show();
					MValidDialog.this.dismiss();
					context.sendBroadcast(new Intent("exit_app"));
				}
			}
			else if(msg.what == 2)
			{
				String sec = XSYTransformationUtil.secontToMinAndSec(time);
				if(MValidDialog.this.isShowing() && txtCountTime != null)
					txtCountTime.setText(sec);
			}
		}
	};
	
	private void sendMes()
	{
		//http://192.168.245.1:8080/springMVC/requestMonitorAction/cheat.action?&uuid=0003&name=解锋镝

		//Log.i("XU", "发送报告到服务器"+API.URL3+"cheat.action?&uuid="+MyApplication.getUUID(context)+"&name="+MyApplication.user.getName());
		finalHttp.get(API.URL3+"cheat.action?&uuid="+MyApplication.getUUID(context)+"&name="+MyApplication.user.getName(), new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
			}
		});
	};
	
//	public void show() 
//	{
//		this.show();
//		isShow = true;
//	};
//	
//	public void dismiss() 
//	{
//		this.dismiss();
//		isShow = false;
//	}

	
//	Runnable runnable = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//			
//		}
//	};
}
