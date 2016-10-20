package com.soul.project.story.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.SHA1;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.application.view.MySwitch;
import com.yisanguo.app.api.API;

public class SettingActivity extends BaseActivityWithSystemBarColor implements OnCheckedChangeListener, OnClickListener
{
	MySwitch switch1;
	MySwitch switchBGM;
	MySwitch switchNightMode;
	MySwitch switchIsLook;
	MySwitch switchIsOpenPublicChat;
	String isOpen;
	String isOpenBGM;
	String isOpenNightMode;
	String islookbattle;
	String isOpenPublicChat;
	MTextView txtOut;
	TextView txt1;
	TextView txt1p5;
	Button btnMiBao;
	Button btnMiMa;
	TextView txt2;
	int time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		btnMiMa = (Button)this.findViewById(R.id.btnMiMa);
		btnMiBao= (Button)this.findViewById(R.id.btnMiBao);
		
		btnMiMa.setOnClickListener(this);
		btnMiBao.setOnClickListener(this);
		
		switch1 = (MySwitch)this.findViewById(R.id.switch1);
		switch1.setOnCheckedChangeListener(this);
		
		switchBGM = (MySwitch)this.findViewById(R.id.switchBGM);
		switchBGM.setOnCheckedChangeListener(this);
		
		switchIsLook = (MySwitch)this.findViewById(R.id.switchIsLook);
		switchIsLook.setOnCheckedChangeListener(this);
		
		switchNightMode = (MySwitch)this.findViewById(R.id.switchNightMode);
		switchNightMode.setOnCheckedChangeListener(this);
				
		switchIsOpenPublicChat = (MySwitch)this.findViewById(R.id.switchIsOpenPublicChat);
		switchIsOpenPublicChat.setOnCheckedChangeListener(this);
		
		txt1 = (TextView)this.findViewById(R.id.txt1);
		txt1p5 = (TextView)this.findViewById(R.id.txt1_5);
		txt2 = (TextView)this.findViewById(R.id.txt2);
		txtOut = (MTextView)this.findViewById(R.id.txtOut);
		txtOut.setOnClickListener(this);
		
		txt1.setOnClickListener(this);
		txt1p5.setOnClickListener(this);
		txt2.setOnClickListener(this);
		
		loadData(1, 1);
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub
		isOpen = ShareDB.getStringFromDB(this, "isopean_auto_move");
		time = ShareDB.getIntFromDB(this, "delay");//(this, "isopean_auto_move");
		isOpenBGM = ShareDB.getStringFromDB(this, "isopen_bgm");
		isOpenNightMode = ShareDB.getStringFromDB(this, "isopen_night");
		islookbattle = ShareDB.getStringFromDB(this, "islookbattle");
		isOpenPublicChat = ShareDB.getStringFromDB(this, "isOpenPublicChat");
		
		if(time == 0 || time == 3)
		{
			txt1.setBackgroundColor(getResources().getColor(R.color.white));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.white));
			txt2.setBackgroundColor(getResources().getColor(R.color.blue));
		}
		else if(time == 2)
		{
			txt1.setBackgroundColor(getResources().getColor(R.color.white));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.blue));
			txt2.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else if(time == 1)
		{
			txt1.setBackgroundColor(getResources().getColor(R.color.blue));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.white));
			txt2.setBackgroundColor(getResources().getColor(R.color.white));
		}
		
		if(isOpen == null || "false".equals(isOpen))
		{
			switch1.setChecked(false);
		}
		else
		{
			switch1.setChecked(true);
		}
		
		if(isOpenBGM == null || "false".equals(isOpenBGM))
		{
			switchBGM.setChecked(false);
		}
		else
		{
			switchBGM.setChecked(true);
		}
		
		if(isOpenNightMode == null || "false".equals(isOpenNightMode))
		{
			switchNightMode.setChecked(false);
		}
		else
		{
			switchNightMode.setChecked(true);
		}
		
		if(switchIsLook == null || "false".equals(islookbattle))
		{
			switchIsLook.setChecked(true);
		}
		else
		{
			switchIsLook.setChecked(false);
		}
		
		if(isOpenPublicChat == null || "false".equals(isOpenPublicChat))
		{
			switchIsOpenPublicChat.setChecked(true);
		}
		else
		{
			switchIsOpenPublicChat.setChecked(false);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		// TODO Auto-generated method stub
		if(buttonView.getId() == R.id.switch1)
		{
			if(isChecked)
			{
				ShareDB.save2DB(SettingActivity.this, "isopean_auto_move","true");
			}
			else
			{
				ShareDB.save2DB(SettingActivity.this, "isopean_auto_move","false");
			}
		}
		else if(buttonView.getId() == R.id.switchBGM)
		{
			if(isChecked)
			{
				ShareDB.save2DB(SettingActivity.this, "isopen_bgm","true");
			}
			else
			{
				ShareDB.save2DB(SettingActivity.this, "isopen_bgm","false");
			}
		}
		else if(buttonView.getId() == R.id.switchNightMode)
		{
			if(isChecked)
			{
				ShareDB.save2DB(SettingActivity.this, "isopen_night","true");
			}
			else
			{
				ShareDB.save2DB(SettingActivity.this, "isopen_night","false");
			}
		}
		else if(buttonView.getId() == R.id.switchIsLook)
		{
			Log.i("XU", "switch 是否观战选择"+isOpenPublicChat);
			
			islookbattle = ShareDB.getStringFromDB(this, "islookbattle");
			if(!isChecked)
			{
				ShareDB.save2DB(SettingActivity.this, "islookbattle","true");
			}
			else
			{
				ShareDB.save2DB(SettingActivity.this, "islookbattle","false");
			}
		}
		else if(buttonView.getId() == R.id.switchIsOpenPublicChat)
		{
			isOpenPublicChat = ShareDB.getStringFromDB(this, "isOpenPublicChat");
			Log.i("XU", "switch 选择"+isOpenPublicChat);
			if(!isChecked)
			{
				ShareDB.save2DB(SettingActivity.this, "isOpenPublicChat","true");
			}
			else
			{
				ShareDB.save2DB(SettingActivity.this, "isOpenPublicChat","false");
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId() == R.id.txtOut)
			finish();
		else if(v.getId() == R.id.txt2)
		{
			ShareDB.save2DB(this, "delay",3);//getIntFromDB(this, "delay");//(t
			txt1.setBackgroundColor(getResources().getColor(R.color.blue));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.white));
			txt2.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else if(v.getId() == R.id.txt1_5)
		{
			ShareDB.save2DB(this, "delay",2);//getIntFromDB(this, "delay");//(t
			txt1.setBackgroundColor(getResources().getColor(R.color.white));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.blue));
			txt2.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else if(v.getId() == R.id.txt1)
		{
			ShareDB.save2DB(this, "delay",1);//getIntFromDB(this, "delay");//(t
			txt1.setBackgroundColor(getResources().getColor(R.color.blue));
			txt1p5.setBackgroundColor(getResources().getColor(R.color.white));
			txt2.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else if(v.getId() == R.id.btnMiMa)
		{
			modpwd();
		}
		else if(v.getId() == R.id.btnMiBao)
		{
			settingMB();
		}
	}

	private void settingMB()
	{
		finalHttp.get(API.LOGIN_OR_REGISTERT+"getmibaostate.action?&uuid="+MyApplication.getUUID(SettingActivity.this), new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("Test", "密保反馈失败="+t.toString());
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(SettingActivity.this, "请求服务器中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						Log.i("Test", "密保反馈成功="+t.toString());
						// 修改密保
						if(code == 200)
						{
							String message = jsonObject.getString("message");
							String title = jsonObject.getString("title");
							String question = jsonObject.getString("question");
							
							mibaosetting(message,title,question,0);
						}
						// 初始化密保
						else if(code == 201)
						{
							String message = jsonObject.getString("message");
							String title = jsonObject.getString("title");
							mibaosetting(message,title,null,1);
						}
						else
						{
							String mes = jsonObject.getString("message");
							if(mes != null)
								ToastUtil.show(SettingActivity.this, mes);
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
	}
	
	EditText oldPwd;
	EditText newPwd;
	private void modpwd()
	{
		final Dialog dialogForMiMa = new Dialog(this, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_mod_password, null);
		Button btnConfirm = (Button)view.findViewById(R.id.btnConfirm);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		
		oldPwd = (EditText) view.findViewById(R.id.etOldPwd);
		newPwd = (EditText) view.findViewById(R.id.etNewPwd);
		
		btnConfirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				String oldpwd = oldPwd.getText().toString();
				String newpwd = newPwd.getText().toString();
				
				if(oldpwd == null || "".equals(oldpwd.trim()))
				{
					ToastUtil.showStaticToastShort(SettingActivity.this, "原密保不能为空!");
					return;
				}
				if(newpwd == null || "".equals(newpwd.trim()))
				{
					ToastUtil.showStaticToastShort(SettingActivity.this, "新密保不能为空!");
					return;
				}
				
				SHA1 sha1 = new SHA1();
				oldpwd = sha1.getDigestOfString(oldpwd.getBytes());
				newpwd = sha1.getDigestOfString(newpwd.getBytes());
				
				AjaxParams params = new AjaxParams();
				params.put("uuid", MyApplication.getUUID(SettingActivity.this));
				params.put("newpwd", newpwd);
				params.put("oldpwd", oldpwd);
				finalHttp.post(API.LOGIN_OR_REGISTERT+"modmima.action?",params, new AjaxCallBack<Object>()
				{
					Dialog dialog ;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						dialog.dismiss();
					}

					@Override
					public void onStart()
					{
						// TODO Auto-generated method stub
						super.onStart();
						dialog = MessageDialog.createLoadingDialog(SettingActivity.this, "请求服务器中...");
						dialog.show();
					}

					@Override
					public void onSuccess(Object t)
					{
						// TODO Auto-generated method stub
						super.onSuccess(t);
						dialog.dismiss();
						dialogForMiMa.dismiss();
						if(t!=null)
						{
							try
							{
								JSONObject jsonObject;
								jsonObject = new JSONObject(t.toString());
								String mes = jsonObject.getString("message");
								if(mes != null)
								{
									ToastUtil.showStaticToastShort(SettingActivity.this, mes);
									ActivityUtil.goToNewActivity(SettingActivity.this, LoginActivity.class);
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
				
			}
		});
		btnCancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				dialogForMiMa.dismiss();
			}
		});
		dialogForMiMa.setContentView(view);
		dialogForMiMa.show();
	}
	
	EditText etNewQuestion;
	EditText etNewAnswer;
	EditText etOldAnswer;
	private void mibaosetting(String message, String title, String question, final int type)
	{
		final Dialog dialogForMiBao = new Dialog(this, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_mibao_init, null);
		TextView txtDialogTitle = (TextView) view.findViewById(R.id.txtDialogTitle);
		TextView txtOldQuestion = (TextView) view.findViewById(R.id.txtOldQuestion);
		TextView txtDialogMessage = (TextView) view.findViewById(R.id.txtDialogMessage);
		 etOldAnswer = (EditText) view.findViewById(R.id.etOldAnswer);
		 etNewQuestion = (EditText)view.findViewById(R.id.etNewQuestion);
		 etNewAnswer = (EditText)view.findViewById(R.id.etNewAnswer);
		Button btnConfirm = (Button)view.findViewById(R.id.btnConfirm);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		if(type == 1)
		{
			txtOldQuestion.setVisibility(View.GONE);
			etOldAnswer.setVisibility(View.GONE);
		}
		
		txtDialogTitle.setText(title);
		txtDialogMessage.setText(message);
		if(question != null)
		txtOldQuestion.setText(question);
		
		btnConfirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//String question, String answer, String confirmanswer
				String question = etNewQuestion.getText().toString();
				String answer   = etNewAnswer.getText().toString();
				// 旧密保答案
				String confirmanswer = etOldAnswer.getText().toString();
				
				if(question == null || "".equals(question.trim()))
				{
					ToastUtil.showStaticToastShort(SettingActivity.this, "密保问题不能为空!");
					return;
				}
				else if(answer == null || "".equals(answer.trim()))
				{
					ToastUtil.showStaticToastShort(SettingActivity.this, "密保答案不能为空!");
					return;
				}
				else if((confirmanswer == null || "".equals(confirmanswer.trim())) && type==0)
				{
					ToastUtil.showStaticToastShort(SettingActivity.this, "原密保答案不能为空!");
					return;
				}
				
				AjaxParams p = new AjaxParams();
				p.put("uuid", MyApplication.getUUID(SettingActivity.this));
				p.put("question", question);
				p.put("answer", answer);
				p.put("confirmanswer", confirmanswer);
				
				finalHttp.post(API.LOGIN_OR_REGISTERT+"settingmibao.action?",p, new AjaxCallBack<Object>()
				{
					Dialog dialog;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						dialog.dismiss();
					}

					@Override
					public void onStart()
					{
						// TODO Auto-generated method stub
						super.onStart();
						dialog = MessageDialog.createLoadingDialog(SettingActivity.this, "请求服务器中...");
						dialog.show();
					}

					@Override
					public void onSuccess(Object t)
					{
						// TODO Auto-generated method stub
						super.onSuccess(t);	
						dialog.dismiss();
						dialogForMiBao.dismiss();
						if(t != null)
						{
							try
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								String mes = jsonObject.getString("message");
								if(mes != null)
									ToastUtil.showStaticToast(SettingActivity.this, mes);
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
		});
		
		btnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				dialogForMiBao.dismiss();
			}
		});
		
		dialogForMiBao.setContentView(view);
		dialogForMiBao.show();
	}
}
