package com.soul.project.story.activity;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class SheLieActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	private MTextView txtDescript;
	private MTextView txtName;
	private MTextView txtAttack;
	private MTextView txtTip;
	private Button btnRight;
	private Button btnLeft;
	private Button btnRefresh;
	Random random = new Random();
	public boolean isRunning = false;
	private boolean isOnLine = false;
	String[] tips = new String[]{};
	String[] strs2 = new String[]{"赌场护卫","山野樵夫","外乡游客","赌坊老板"};
	String[] strdesc2 = new String[]{"一个赌场护卫正在巡视猎场，还一路哼着歌正巧立马路过你这边。","一名山野樵夫莫名来到你面前，应该是正巧路过来砍柴的。","一名外乡游客来到此地游览山色风光，还独立吟诗作对","赌坊老板也正在狩猎之中，气着高头大马，拿着精良弓箭，哼唱着小二郎。"};
	String[] strs = new String[]{"松鼠","兔子","飞鹰","野猪","野狼","梅花鹿","大巨熊","凶恶猛虎","大型金钱豹","墨玉麒麟"};
	String[] strdesc = new String[]{"一只可爱的小松鼠飞快地蹿到你面前的草地上，还没注意到你的利箭已经悄悄瞄准它了。","一只白白的小兔子，虽然属于狩猎低等品，但也聊胜于无。","你注意到一只飞鹰飞掠而来，暗自搭弓引箭。","一只肥硕的野猪拖着笨重的身躯闯入你的视线内，你暗自一阵欢喜。","一只饥饿的野狼拖着疲惫的身躯来到你的视线范围内，你连忙打起精神。","一只色彩斑斓的梅花麋鹿一蹦一跳进入你的伏击圈内。","一只独眼的大巨熊挥舞着比你大腿还粗的双臂（前肢）朝你奔来，你吓得一个哆嗦。","一声虎啸，你吓得心头一惊，正想用手捂住双眼，差点手中弓箭脱手而落，你一头冷汗，原来是一只凶恶猛虎觅食而来。","一只大型金钱豹飞奔过来，躺在此地休息。","你一眨眼间发现居然有一只墨玉麒麟在此地降落，这可是预兆着祥瑞的神兽啊！"};
	int mode;
	int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shelie);

		Log.i("XU", "getSignature = "+getSignature());
		
		initView();
		initEvent();
	}
	
	  public String getSignature() {
	      
	           try {
	        	   PackageManager manager = getPackageManager();
	               /** 通过包管理器获得指定包名包含签名的包信息 **/
	        	   PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
	               /******* 通过返回的包信息获得签名数组 *******/
	        	   android.content.pm.Signature[] signatures = packageInfo.signatures;
	               /******* 循环遍历签名数组拼接应用签名 *******/
	        	   StringBuilder builder = new StringBuilder();
	               for (android.content.pm.Signature signature : signatures) {
	                   builder.append(signature.toCharsString());
	               }
	               /************** 得到应用签名 **************/
	               return builder.toString();
	           } catch (NameNotFoundException e) {
	               e.printStackTrace();
	           }
	           return null;
	   }
	
	@Override
	protected void onStart()
	{
		super.onStart();
		ToastUtil.showShort(this, "你备好干粮和饮水，找了个隐秘的地方猫着，准备守株待兔。");
		if(!isRunning)
		{
			isRunning = true;
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		isRunning = false;
	}
	
	Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			while (isRunning)
			{
				try
				{
					Thread.sleep(random.nextInt(15) * 1000 + 8000);
					handler.sendEmptyMessage(1);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 1)
			{
				isOnLine = true;
				int rand = 0;
				int type = random.nextInt(2);
				if(type == 0)
				{
					mode = 1;
					rand = random.nextInt(strs.length);
					level = rand;
					txtName.setText(strs[rand]);
					txtDescript.setText(strdesc[rand]);
				}
				else if(type == 1)
				{
					mode = 2;
					rand = random.nextInt(strs2.length);
					level = rand;
					txtName.setText(strs2[rand]);
					txtDescript.setText(strdesc2[rand]);
				}
				
				handler.sendEmptyMessageDelayed(2, random.nextInt(400)+550-rand * 20);
			}
			else if(msg.what == 2)
			{
				isOnLine = false;
				txtDescript.setGravity(Gravity.CENTER);
				txtDescript.setText("你面前空无一物，你疲乏之下闭目养神，但耳朵却还是丝毫不放松警惕。");
				txtName.setText("");
			}
		};
	};
	
	private void initEvent()
	{
		txtDescript.setOnClickListener(this);
		txtAttack.setOnClickListener(this);
		txtName.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
	}

	private void initView()
	{
		txtTip = (MTextView)this.findViewById(R.id.txtTip);
		txtDescript = (MTextView)this.findViewById(R.id.txtDescript);
		txtAttack = (MTextView)this.findViewById(R.id.txtAttack);
		txtName = (MTextView)this.findViewById(R.id.txtName);
		btnRight = (Button)this.findViewById(R.id.btnRight);
		btnLeft = (Button)this.findViewById(R.id.btnLeft);
		btnRefresh = (Button)this.findViewById(R.id.btnRefresh);
	}

	@Override
	public void loadData(int id, int type)
	{
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnRight:
			case R.id.btnLeft:
				finish();
				break;
			case R.id.txtAttack:
				if(isOnLine)
				{
					AjaxParams params = new AjaxParams();
					params.put("uuid", MyApplication.getUUID(this));
					params.put("sign", getSignature());
					params.put("level", ""+level);
					params.put("type", ""+mode);
					
					finalHttp.post(API.URL_OTHER+"shelie.action?",params, new AjaxCallBack<Object>()
					{
						Dialog dialog;
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							Log.i("XU", "错误"+strMsg);
							dialog.dismiss();
						}

						@Override
						public void onStart()
						{
							// TODO Auto-generated method stub
							super.onStart();
							dialog = MessageDialog.createLoadingDialog(SheLieActivity.this, "获取奖励中...");
							dialog.show();
						}

						@Override
						public void onSuccess(Object t)
						{
							// TODO Auto-generated method stub
							super.onSuccess(t);
							dialog.dismiss();
							Log.i("XU", "涉猎结果="+t.toString());
							if(t != null )
							{
								try
								{
									JSONObject jsonObject;
									jsonObject = new JSONObject(t.toString());
									String mes = jsonObject.getString("message");
									int count = jsonObject.getInt("data");
									txtAttack.setText("一箭射出 ("+count+")");
									if(mes != null)
									{
										Builder builder = new Builder(SheLieActivity.this);
										builder.setTitle("猎报");
										builder.setMessage(mes);
										builder.setNegativeButton("知道了", null);
										builder.show();
									}
//										ToastUtil.showStaticToast(SheLieActivity.this, mes);
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}
							}
						}
					});
					ToastUtil.showStaticToastShort(this, "你一箭射中了");
				}
				else
				{
					AjaxParams params = new AjaxParams();
					params.put("uuid", MyApplication.getUUID(this));
					params.put("sign", getSignature());
					params.put("level", ""+level);
					params.put("type", "3");
					
					finalHttp.post(API.URL_OTHER+"shelie.action?",params, new AjaxCallBack<Object>()
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
							Log.i("XU", "涉猎结果222="+t.toString());
							super.onSuccess(t);
							if(t != null )
							{
//								08-27 16:09:44.579: I/XU(25147): 涉猎结果={"code":201,"message":"赌坊老板: 你的体力已经不够了，还是先回去休息一下，明天再来吧。"}

								try
								{
									JSONObject jsonObject;
									jsonObject = new JSONObject(t.toString());
									String mes = jsonObject.getString("message");
									if(mes != null && !"".equals(mes))
									{
										int count = jsonObject.getInt("data");
										txtAttack.setText("一箭射出 ("+count+")");
										Builder builder = new Builder(SheLieActivity.this);
										builder.setTitle("猎报");
										builder.setMessage(mes);
										builder.setNegativeButton("知道了", null);
										builder.show();
									}
//										ToastUtil.showStaticToast(SheLieActivity.this, mes);
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}
							}
						}
					});
					ToastUtil.showStaticToastShort(this, "你笨手笨脚的，慢了一拍的功夫，猎物已经跑了。");
				}
				
				break;
			default:
				break;
		}
	}

}
