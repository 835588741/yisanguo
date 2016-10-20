package com.soul.project.story.activity;

import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class TianDiRenPlay extends BaseActivityWithSystemBarColor implements OnClickListener
{
	MTextView txtStart;
	Button btn1;
	Button btn2;
	Button btn5;
	Button btn10;
	Button btn20;
	Button btn50;
	TextView txtXJMoney;
	TextView txtZJMoney;
	TextView txtResult;
	TextView txtUserYa;
	String userTip = "";
	int money = 1;
	TextView txtZJ;
	TextView txtXJ;
	String[] pai = new String[]{"天牌","地牌","人牌"};
	boolean isCanClick = true;
	String hid;
	TextView txtZJHouseName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiandirenplay);
		hid = getIntent().getStringExtra("hid");
		timer = new Timer();
		initView();
		initEvent();
	}
	
	private void initView()
	{
		btn1 = (Button)this.findViewById(R.id.btn1);
		btn2 = (Button)this.findViewById(R.id.btn2);
		btn5 = (Button)this.findViewById(R.id.btn5);
		btn10 = (Button)this.findViewById(R.id.btn10);
		btn20 = (Button)this.findViewById(R.id.btn20);
		btn50 = (Button)this.findViewById(R.id.btn50);		
		
		txtZJHouseName = (TextView)this.findViewById(R.id.txtZJHouseName);
		txtUserYa = (TextView)this.findViewById(R.id.txtUserYa);
		txtResult = (TextView)this.findViewById(R.id.txtResult);
		txtStart = (MTextView)this.findViewById(R.id.txtStart);
		txtXJMoney = (TextView)this.findViewById(R.id.txtXJMoney);
		txtZJMoney = (TextView)this.findViewById(R.id.txtZJMoney);
		txtZJ = (TextView)this.findViewById(R.id.txtZJ);
		txtXJ = (TextView)this.findViewById(R.id.txtXJ);
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub

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
	
	Timer timer;
	int index = 0;
	
	private void start()
	{
		if(!isCanClick)
		{
			ToastUtil.showShort(TianDiRenPlay.this, "正在等待开牌结果，请耐心等待");
		}
		else
		{
			isCanClick = false;
			
			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					if(!isCanClick)
					{
						index ++;
						if(index >= pai.length)
							index = 0;
						handler.sendEmptyMessage(1024);
					}
				}
			}, 0,500);
			
			handler.sendEmptyMessageDelayed(1, 1200);
		}
	}
	
	private void stop()
	{
		
	}
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 1024)
			{
				txtZJ.setText(pai[index]);
				txtXJ.setText(pai[index]);
				return;
			}
//			if(msg.what == 1025)
//			{
//				txtResult.setTextSize(txtResult.getTextSize() / 2);
//			}
			
			finalHttp.get(API.URL_OTHER+"readcard.action?&uuid="+MyApplication.getUUID(TianDiRenPlay.this)+"&money="+money+"&hid="+hid, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					Log.i("XU", "异常="+strMsg);
					isCanClick = true;
					timer.cancel();
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
					timer.cancel();
					
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
									ToastUtil.showStaticToast(TianDiRenPlay.this, mes);
							}
							else if(code == 208)
							{
								String mes = jsonObject.getString("message");
								if(mes != null)
									ToastUtil.showStaticToast(TianDiRenPlay.this, mes);
								finish();
							}
							else
							{
								String mes = jsonObject.getString("message");
								if(mes != null)	
								{
									txtResult.setText(mes);
									Animation a = AnimationUtils.loadAnimation(TianDiRenPlay.this, R.anim.scanbig);
					    			txtResult.startAnimation(a); 
								}
								
								String zjname = jsonObject.getString("zjname");
								String zj = jsonObject.getString("zj");
								String xj = jsonObject.getString("xj");
//								JSONObject jsonObject = new JSONObject();
//								jsonObject.put("zjmoney", zjmoney);
//								jsonObject.put("xjmoney", xjmoney);
//								jsonObject.put("zjname", zjname);
//								jsonObject.put("code", 200);
//								jsonObject.put("zj", pai[zj]);
//								jsonObject.put("xj", pai[xj]);
//								jsonObject.put("message", "庄家地牌，闲家天牌，闲家赢！");
								
								long zjmoney = jsonObject.getLong("zjmoney");
								long xjmoney = jsonObject.getLong("xjmoney");
								txtZJHouseName.setText("房主【"+zjname+"】");
								txtZJMoney.setText("庄家:"+zjmoney+"(银)");
								txtXJMoney.setText("闲家:"+xjmoney+"(银)");
								txtZJ.setText(zj);
								txtXJ.setText(xj);
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
	

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.txtStart:
				if(isCanClick)
				start();
				break;
			case R.id.btn1:
				finish();
				break;
			case R.id.btn2:
				ToastUtil.showStaticToastShort(TianDiRenPlay.this, "本模式不能选择金额");
				break;
			case R.id.btn5:
				ToastUtil.showStaticToastShort(TianDiRenPlay.this, "本模式不能选择金额");
				break;
			case R.id.btn10:
				ToastUtil.showStaticToastShort(TianDiRenPlay.this, "本模式不能选择金额");
				break;
			case R.id.btn20:
				ToastUtil.showStaticToastShort(TianDiRenPlay.this, "本模式不能选择金额");
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
