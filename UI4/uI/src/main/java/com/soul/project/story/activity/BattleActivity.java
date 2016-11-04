/**
 * 
 */
package com.soul.project.story.activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.util.Utils;
import com.soul.project.application.view.MTextView;
import com.soul.project.application.view.MyProgress;
import com.yisanguo.app.api.API;

/**
 * @file BattleActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo TODO
 * @date 2016年6月20日 下午5:46:06
 */
public class BattleActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	MTextView txtDescript;
	MTextView txtPlaceName;
	String attackerid;
	String defencerid;
	boolean isCanClick = true;
	Button btnGoOut;
	Button btnFriend;
	int state;
	RelativeLayout rootLayout;
	private ProgressBar progressBar;
	// 是否可以点击退出Battle
	boolean isCanClickToLeave = true;
	Random random = new Random();
	boolean isCanNextClick = true;
	
	private LinearLayout layoutChatView;
	private LinearLayout layoutInputMessage;
	private TextView txtChatTest;
	private Button btnShowOrGoneInputLayout;
	private Button btnSendMess;
	private EditText txtChatInputTest;
	private TextView txtChatTestPosition;
	private TextView txtChatTestName;
	
	private MTextView txtAttackerName;
	private MTextView txtDefencerName;
	private MyProgress pbAttackerHP;
	private MyProgress pbDefencerHP;
	
	boolean isCanSendMessage = true;
	private int delayTime = 8500;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battle);

		IntentFilter filter = new IntentFilter("refresh_chat");
		registerReceiver(receiver, filter);
		
		attackerid = getIntent().getStringExtra("attackerid");
		defencerid = getIntent().getStringExtra("defencerid");
		initViews();
		loadData(1, 0);
	}
	
	BroadcastReceiver receive = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals("exit_app"))
			{
				if(!BattleActivity.this.isFinishing())
					finish();
			}
			else if(intent.getAction().equals("refresh_chat"))
			{
				String mess = intent.getStringExtra("message");
				if(mess != null)
					txtChatTest.setText(mess);
				else
					txtChatTest.setText("加油！加油！打死ta！");
			}
		}
	};
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if(rootLayout != null)
		{
			String isNight = ShareDB.getStringFromDB(this, "isopen_night");
			if("true".equals(isNight))
			{
				rootLayout.setBackgroundResource(R.color.black_light_title_text_color);
			}
			else
			{
				rootLayout.setBackgroundResource(R.color.gray_light);
			}
		}
		
		String s = ShareDB.getStringFromDB(this, "isOpenPublicChat");
		if("true".equals(s))
		{
			layoutChatView.setVisibility(View.GONE);
		}
		else
		{
			layoutChatView.setVisibility(View.VISIBLE);
		}
	}

	private void initViews()
	{
		// hp show
		txtAttackerName = (MTextView)this.findViewById(R.id.txtAttackerName);
		txtDefencerName = (MTextView)this.findViewById(R.id.txtDefencerName);
		pbAttackerHP = (MyProgress)this.findViewById(R.id.pbAttackerHP);
		pbDefencerHP = (MyProgress)this.findViewById(R.id.pbDefencerHP);
		
		rootLayout = (RelativeLayout)this.findViewById(R.id.rootLayout);
		progressBar = (ProgressBar) this.findViewById(R.id.progress);
		btnGoOut = (Button) this.findViewById(R.id.btnGoOut);
		btnFriend = (Button) this.findViewById(R.id.btnFriend);
		txtPlaceName = (MTextView) this.findViewById(R.id.txtPlaceName);
		txtDescript = (MTextView) this.findViewById(R.id.txtDescript);
		txtPlaceName.setOnClickListener(this);
		btnGoOut.setOnClickListener(this);
		btnFriend.setOnClickListener(this);
		
		txtChatTestName = (TextView)this.findViewById(R.id.txtChatTestName);
		txtChatTestPosition = (TextView)this.findViewById(R.id.txtChatTestPosition);
		// 整个公聊屏幕
		layoutChatView = (LinearLayout)this.findViewById(R.id.layoutChatView);
		// 公聊输入区域
		layoutInputMessage = (LinearLayout)this.findViewById(R.id.layoutInputMessage);
		// 公聊信息滚展控件
		txtChatTest = (TextView)this.findViewById(R.id.txtChatTest);
		// 公聊信息输入区域是否显示的点击按钮
		btnShowOrGoneInputLayout = (Button)this.findViewById(R.id.btnShowOrGoneInputLayout);
		// 公聊信息发送控件
		btnSendMess = (Button)this.findViewById(R.id.btnSendMess);
		// 公聊信息输入控件
		txtChatInputTest = (EditText)this.findViewById(R.id.txtChatInputTest);
		
		btnSendMess.setOnClickListener(this);
		btnShowOrGoneInputLayout.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData
	 * (int)
	 */
	@Override
	public void loadData(int id, int type)
	{
		// Log.i("XU",
		// "url："+API.URL+"battle.action?&attackerid="+attackerid+"&defencerid="+defencerid+"&type='"+id+"'");
		finalHttp.get(
				API.URL + "battle.action?&attackerid=" + attackerid + "&defencerid=" + defencerid + "&type=" + id,
				new AjaxCallBack<Object>()
				{
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						//isCanClick = true;
						isCanNextClick = true;
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onStart()
					{
						// TODO Auto-generated method stub
						super.onStart();
						progressBar.setVisibility(View.VISIBLE);
						isCanNextClick = false;
					}

					@Override
					public void onSuccess(Object t)
					{
						// TODO Auto-generated method stub
						super.onSuccess(t);
						progressBar.setVisibility(View.GONE);
						isCanNextClick = true;
						//isCanClick = true;
						if (t != null)
						{
							try
							{
								JSONObject object = new JSONObject(t.toString());
								state = object.getInt("statecode");
								
								try
								{
									String an = object.getString("attackername");
									String dn = object.getString("defencername");
									int ah = object.getInt("attackerhp");
									int dh = object.getInt("defencerhp");
									int at = object.getInt("at");
									int dt = object.getInt("dt");
									txtAttackerName.setText(an);
									txtDefencerName.setText(dn);
									pbAttackerHP.setProgress(ah);
									pbDefencerHP.setProgress(dh);
									pbAttackerHP.setText(at);
									pbDefencerHP.setText(dt);
								}
								catch (JSONException e)
								{
								}
								
								// 不能杀害本城之人
								if(object != null && object.getInt("code")==205)
								{
									ToastUtil.show(BattleActivity.this, object.getString("message"));
								}
								else
								{
									String temp = object.getString("descript");
									if("服务器数据处理状态异常 错误码503".equals(temp))
									{
										finish();
									}
									
									if ("战斗结束,按离开键可以离开".equals(temp))
									{
										txtDescript.setText(Html.fromHtml(txtDescript.getText().toString() + "\n" + temp));
									}
									else
									{
										txtDescript.setText(Html.fromHtml(temp == null ? "他看起来想杀了你" : temp));
									}
									
									String result = object.getString("result");
									Log.i("Test", "战斗结果" + result + "  state=" + state);
									if (result != null && !"".equals(result))
									{
										// 同归于尽
										if ("alldie".equals(result))
										{
											MyApplication.user.setAreaid(1);
											// finish();
											// ActivityUtil.goToNewActivity(BattleActivity.this,
											// MainPanelActivity.class);
										}
										// 不分胜负
										else if ("cant".equals(result))
										{
											
											// finish();
										}
										else if (!MyApplication.user.getUuid().equals(result))
										{
											MyApplication.user.setAreaid(1);
											// finish();
											// ActivityUtil.goToNewActivity(BattleActivity.this,
											// MainPanelActivity.class);
										}
									}
									
									if (state == 0)
									{
										String isAuto = ShareDB.getStringFromDB(BattleActivity.this, "isopean_auto_move");
										if ("true".equals(isAuto))
										{
											int time = ShareDB.getIntFromDB(BattleActivity.this, "delay");
											int deyal = 2000;
											String delayTime = "2";
											if (time == 1)
											{
												deyal = 1000;
												delayTime = "1";
											}
											else if (time == 2)
											{
												deyal = 1500;
												delayTime = "1.5";
											}
											else if (time == 3)
											{
												deyal = 2000;
												delayTime = "2";
											}
											
											handler.postDelayed(new Runnable()
											{
												@Override
												public void run()
												{
													// TODO Auto-generated method
													// stub
													finish();
												}
											}, deyal);
											ToastUtil.show(BattleActivity.this, delayTime + " 秒后自动退出战斗界面");
										}
									}
								}
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.i("XU", "解析异常=" + e.toString());
							}
						}
					}
				});
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
	}

	long curtime = 0;
	long lasttime = 0;

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.txtPlaceName)
		{
			if (isCanNextClick)
			{
				if (state != 0)
					loadData(2, 0);
			}
			else
			{
				if (state != 0)
					txtDescript.setText("你眉头紧锁，十分不耐烦地紧紧盯着对方，以期寻到破绽。");
			}
		}
		else if(v.getId() == R.id.btnShowOrGoneInputLayout)
		{
			if(layoutInputMessage.isShown())
			{
				btnShowOrGoneInputLayout.setText("发言");
				layoutInputMessage.setVisibility(View.GONE);
			}
			else 
			{
				btnShowOrGoneInputLayout.setText("收起");
				layoutInputMessage.setVisibility(View.VISIBLE);
			}
		}
		else if(v.getId() == R.id.btnSendMess)
		{
			if(isCanSendMessage)
				sendPublicMessage(); 
			else 
				ToastUtil.showStaticToastShort(BattleActivity.this, "发表频率太快，请稍后！");
		}
		else if (v.getId() == R.id.btnGoOut)
		{
			curtime = System.currentTimeMillis();
			if (curtime - lasttime > 500)
			{
				handler.sendEmptyMessageDelayed(1000, 800);
				lasttime = curtime;
			}
		}
		else if (v.getId() == R.id.btnFriend)
		{
			curtime = System.currentTimeMillis();
			if (curtime - lasttime > 500)
			{
				handler.sendEmptyMessageDelayed(1000, 800);
				lasttime = curtime;
			}
		}
	}
	
	private void sendPublicMessage()
	{
		String mes = txtChatInputTest.getText().toString();
		if(mes != null && mes.length() >= 5)
		{
			AjaxParams params = new AjaxParams();
			params.put("message", mes);
			params.put("uuid", MyApplication.getUUID(this));
			finalHttp.post(API.MESSAGE_REQUEST+"sendpublicmessage.action?",params, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					isCanSendMessage = true;
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					isCanSendMessage = false;
				}

				@Override
				public void onSuccess(Object t)
				{
					super.onSuccess(t);
					isCanSendMessage = true;
					
					if(t != null)
					{
						try
						{
							JSONObject jsonObject = new JSONObject(t.toString());
							int code = jsonObject.getInt("code");
							if(code == 200)
							{
								if(layoutInputMessage.isShown())
								{
									layoutInputMessage.setVisibility(View.GONE);
									txtChatInputTest.setText("");
								}
							}
								String message = jsonObject.getString("message");
								if(message != null)
								ToastUtil.showStaticToastShort(BattleActivity.this, message);
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		else
		{
			ToastUtil.showStaticToastShort(this, "信息必须不能为空且大于5个字！");
		}
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == 1000)
			{
				String uuid = MyApplication.getUUID(BattleActivity.this);
				if (state != 0)
				{
					if (random.nextInt(8) == 0)
					{
						if (isCanClickToLeave)
						{
							isCanClickToLeave = false;
							txtDescript.setText("你不愿再纠缠，于是施展轻功，若流星赶月般，撒腿便跑");
							// finish();
							if (uuid != null)
							{
								finish();
								//ActivityUtil.goToNewActivity(BattleActivity.this, MainPanelActivity.class);

								finalHttp.get(API.URL + "releasebattle.action?&uuid=" + uuid,
										new AjaxCallBack<Object>()
										{
											@Override
											public void onFailure(Throwable t, int errorNo, String strMsg)
											{
												// TODO Auto-generated method
												super.onFailure(t, errorNo, strMsg);
												Log.i("XU", "逃跑失败:" + strMsg + " code=" + errorNo);
												isCanClickToLeave = true;
											}

											@Override
											public void onStart()
											{
												// TODO Auto-generated method
												// stub
												super.onStart();
											}

											@Override
											public void onSuccess(Object t)
											{
												// TODO Auto-generated method
												// stub
												super.onSuccess(t);
												Log.i("XU", "逃跑成功");
												isCanClickToLeave = true;
											}
										});
							}
						}
					}
					else
					{
						txtDescript.setText("你试图逃离战局，但是对方一把把你拦下，喝到：想走！没那么容易!，看来对方丝毫没有要轻易放过你的意思。");
						loadData(2, 0);
					}
				}
				else
				{
					finish();//ActivityUtil.goToNewActivity(BattleActivity.this, MainPanelActivity.class);
					finalHttp.get(API.URL + "releasebattle.action?&uuid=" + uuid, new AjaxCallBack<Object>()
					{

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							Log.i("XU", "逃跑失败:" + strMsg + " code=" + errorNo);
						}

						@Override
						public void onSuccess(Object t)
						{
							// TODO Auto-generated method stub
							super.onSuccess(t);
							Log.i("XU", "逃跑成功");
						}
					});
				}
			}
		};
	};
	
	Timer timer;
	boolean isExitApp;
	boolean isCanRequest = true;
	private void timerToScauld()
	{
		String s = ShareDB.getStringFromDB(this, "isOpenPublicChat");
		if("true".equals(s))
		{
			return;
		}
		
		if(timer != null)
			timer.cancel();
		
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				if(!isExitApp)
				{
					timerToGetPM();
				}
			}
		}, 4000, delayTime);
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		isExitApp = true;
		if(timer != null)
		timer.cancel();
	};
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		isExitApp = true;
		if(timer != null)
		timer.cancel();
	};
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		isExitApp = false;
		timerToScauld();
	};
	
	private void timerToGetPM()
	{
		if(!Utils.checkNet(this))
		{
			ToastUtil.showStaticToastShort(this, "当前网络不可用！");
			return ;
		}
		
		if(!isCanRequest)
		{
			return;
		}	
		
		finalHttp.get(API.MESSAGE_REQUEST+"getpublicmessage.action?uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				isCanRequest = true;
				txtChatTestPosition.setText("");
				txtChatTest.setText("玄异三国欢迎【"+MyApplication.user.getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				isCanRequest = false;
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				isCanRequest = true;
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
//						{
//							String authorname = jsonObject.getString("authorname");
//						}
						String message = jsonObject.getString("message");
						
						if(code == 206)
						{
							txtChatTestPosition.setText("");
							txtChatTestName.setText("");
							txtChatTest.setText("玄异三国欢迎【"+MyApplication.user.getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
						}
						else if(code == 207)
						{
							txtChatTestPosition.setText("");
							txtChatTestName.setText("");
							txtChatTest.setText(Html.fromHtml(message));
						}
						else if(code == 200)
						{
							String authorname = jsonObject.getString("authorname");
							String position = jsonObject.getString("position");
							
							layoutChatView.setVisibility(View.VISIBLE);
							
							if(position != null)
								txtChatTestPosition.setText(position+"|");
							if(authorname != null)
								txtChatTestName.setText(authorname+"：");
							
							if(message == null || message.length() == 0)
							{
								txtChatTestPosition.setText("");
								txtChatTestName.setText("");
								txtChatTest.setText("玄异三国欢迎【"+MyApplication.user.getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
							}
							else
								txtChatTest.setText(Html.fromHtml(message));
						}
							
//						else
//						{
//							layoutChatView.setVisibility(View.VISIBLE);
//						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
	}

}
