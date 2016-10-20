/**
 * 
 */
package com.soul.project.story.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;

import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @file YaShaiZiActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016-8-27 下午1:24:41
 */
public class YaShaiZiActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	ImageView ivZJ;
	ImageView ivXJ;
	TextView txtZJ;
	TextView txtXJ;
	TextView txtXJMoney;
	TextView txtZJMoney;
	TextView txtResult;
	TextView txtUserYa;
	
	int[] images = new int[]{R.drawable.si001,R.drawable.si002,R.drawable.si003,R.drawable.si004,R.drawable.si005,R.drawable.si006};
	
	MTextView txtStart;
	Button btn1;
	Button btn2;
	Button btn5;
	Button btn10;
	Button btn20;
	Button btn50;
	
	AnimationDrawable animZJ;
	AnimationDrawable animXJ;
	
	String userTip = "";
	int money = 1;
	boolean isCanClick = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	//http://112.124.109.206:8080/springMVC/otherAction/bigorsmall.action?&uuid=0001&money=1

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yashaizi);
		initViews();
		initEvent();
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		isCanClick = true;
	}
	
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		isCanClick = true;
	}

	private void start()
	{
		if(!isCanClick)
		{
			ToastUtil.showShort(YaShaiZiActivity.this, "正在等待开盘结果，请耐心等待");
		}
		else
		{
			isCanClick = false;
			
			ivZJ.setBackgroundResource(R.anim.anim_shaizi_duchang);
			ivXJ.setBackgroundResource(R.anim.anim_shaizi_duchang);
		    animZJ = (AnimationDrawable) ivZJ.getBackground();
		    animXJ = (AnimationDrawable) ivXJ.getBackground();
		     
			animXJ.start();
			animZJ.start();
			handler.sendEmptyMessageDelayed(1, 2500);
		}
	}
	
	private void stop()
	{
		if(animXJ != null)
			animXJ.stop();
		if(animZJ != null)
			animZJ.stop();
	}
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			//Log.i("XU", API.URL_OTHER+"bigorsmall.action?&uuid="+MyApplication.getUUID(YaShaiZiActivity.this)+"&money="+money);
			finalHttp.get(API.URL_OTHER+"bigorsmall.action?&uuid="+MyApplication.getUUID(YaShaiZiActivity.this)+"&money="+money, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					isCanClick = true;
					stop();
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					isCanClick = false;
				}

				@Override
				public void onSuccess(Object t)
				{
					// TODO Auto-generated method stub
					super.onSuccess(t);
					isCanClick = true;
					stop();
					
					if(t != null)
					{
						JSONObject jsonObject;
						try
						{
							jsonObject = new JSONObject(t.toString());
							int code = jsonObject.getInt("code");
							if(code != 200)
							{
								String mes = jsonObject.getString("message");
								if(mes != null)
									ToastUtil.showStaticToast(YaShaiZiActivity.this, mes);
							}
							else
							{
								String mes = jsonObject.getString("message");
								if(mes != null)	
									txtResult.setText(mes);
								
								String txtzj = jsonObject.getString("txtzj"); 
								if(txtzj != null)
									txtZJ.setText(txtzj);
								
								String txtxj = jsonObject.getString("txtxj"); 
								if(txtxj != null)
									txtXJ.setText(txtxj);
								
								long zjmoney = jsonObject.getLong("zjmoney");
								txtZJMoney.setText("庄家(银): "+zjmoney);
								
								long xjmoney = jsonObject.getLong("xjmoney");
								txtXJMoney.setText(xjmoney+" :闲家(银)");
								
								int zj = jsonObject.getInt("zj");
								int xj = jsonObject.getInt("xj");
								
								ivZJ.setBackgroundResource(images[zj]);
								ivXJ.setBackgroundResource(images[xj]);
							}
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		};
	};
	
	private void initViews()
	{
		ivZJ = (ImageView)this.findViewById(R.id.ivZJ);
		ivXJ = (ImageView)this.findViewById(R.id.ivXJ);
		
		txtXJMoney = (TextView)this.findViewById(R.id.txtXJMoney);
		txtZJMoney = (TextView)this.findViewById(R.id.txtZJMoney);
		
		ivZJ.setBackgroundResource(R.anim.anim_shaizi_duchang);
		ivXJ.setBackgroundResource(R.anim.anim_shaizi_duchang);
	     animZJ = (AnimationDrawable) ivZJ.getBackground();
	     animXJ = (AnimationDrawable) ivXJ.getBackground();
		
		txtZJ = (TextView)this.findViewById(R.id.txtZJ);
		txtXJ = (TextView)this.findViewById(R.id.txtXJ);
		
		txtUserYa = (TextView)this.findViewById(R.id.txtUserYa);
		txtResult = (TextView)this.findViewById(R.id.txtResult);
		txtStart = (MTextView)this.findViewById(R.id.txtStart);
		
		btn1 = (Button)this.findViewById(R.id.btn1);
		btn2 = (Button)this.findViewById(R.id.btn2);
		btn5 = (Button)this.findViewById(R.id.btn5);
		btn10 = (Button)this.findViewById(R.id.btn10);
		btn20 = (Button)this.findViewById(R.id.btn20);
		btn50 = (Button)this.findViewById(R.id.btn50);
	}
	
	private void initEvent()
	{
		txtStart.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn10.setOnClickListener(this);
		btn20.setOnClickListener(this);
		btn50.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int, int)
	 */
	@Override
	public void loadData(int id, int type)
	{
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.txtStart:
				start();
				break;
			case R.id.btn1:
				finish();
				break;
			case R.id.btn2:
				money = 1;
				break;
			case R.id.btn5:
				money = 2;
				break;
			case R.id.btn10:
				money = 2;
				break;
			case R.id.btn20:
				money = 1;
				break;
			case R.id.btn50:
				finish();
				break;

			default:
				break;
		}
		userTip = "你下注压了:["+money+" 两白银]";
		txtUserYa.setText(userTip);
	}
}
