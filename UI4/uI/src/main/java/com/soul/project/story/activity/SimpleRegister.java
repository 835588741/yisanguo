/**
 * 
 */
package com.soul.project.story.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.soul.project.application.dialog.DialogUtil;
import com.soul.project.application.util.SHA1;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

/**
 * @file SimpleRegister.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年6月22日 下午5:46:30
 */
public class SimpleRegister extends BaseActivityWithSystemBarColor implements OnCheckedChangeListener
{
	LinearLayout layoutValid;
	private EditText edtUser,edtPwd,etNickName,edtVer;
	private Button btnRegister;
	private Button btnVoiVer;
	private String strUser,strPwd,strPhone,strVer;
	private Button btnMessVer;
	private Dialog loaddingDialog;
	private FinalHttp finalHttp;
	private EditText etError;
	private RadioGroup group;
	private RadioButton rgMan;
	private RadioButton rgWoman;
	private int gender = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        // 设置状态栏颜色之前需先调用本段代码
		Window win = this.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		winParams.flags |= bits;
		win.setAttributes(winParams);
        
        // 创建状态栏的管理实例  
        SystemBarTintManager tintManager = new SystemBarTintManager(this);  
        // 激活状态栏设置  
        tintManager.setStatusBarTintEnabled(true);  
        // 激活导航栏设置  
        tintManager.setNavigationBarTintEnabled(true); 
//        tintManager.setTintColor(Color.parseColor("#99000FF"));  
        tintManager.setStatusBarTintResource(R.drawable.image_bg_titlebar);
		
		setContentView(R.layout.activity_register);
		
        finalHttp = new FinalHttp();
        finalHttp.configTimeout(10 * 1000);
        
		initViews();
		initEvent();
	}
	
	private void register()
	{
		strPwd = new SHA1().getDigestOfString(strPwd.getBytes());

//		Log.i("XU", API.URL+"register.action?&account="+strUser+"&password="+strPwd+"&niname="+strPhone);
		try
		{
			finalHttp.get(API.LOGIN_OR_REGISTERT+"register.action?&account="+(URLEncoder.encode(strUser,"utf-8"))+"&password="+strPwd+"&niname="+URLEncoder.encode(strPhone,"utf-8")+"&gender="+gender, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					loaddingDialog.dismiss();
					Log.i("XU", "注册失败="+errorNo);
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					loaddingDialog = DialogUtil.createLoadingDialog(SimpleRegister.this,"正在提交数据...");
					loaddingDialog.setCancelable(false);
					loaddingDialog.show();
				}

				@Override
				public void onSuccess(Object t)
				{
					super.onSuccess(t);
					loaddingDialog.dismiss();
					if(t != null)
					{
						if(!"{}".equals(t.toString().trim()))
						{
							try
							{
								JSONObject object = new JSONObject(t.toString());
								String message = object.getString("message");
								ToastUtil.show(SimpleRegister.this, ""+message, ToastUtil.INFO);
								
								ShareDB.save2DB(SimpleRegister.this, "user_account", strUser);
								ShareDB.save2DB(SimpleRegister.this, "user_password", strPwd);
								ShareDB.save2DB(SimpleRegister.this, "user_nickname", strPhone);
								
								if(object.getInt("code") == 200)
								{
									finish();
								}
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void initEvent()
	{
		group.setOnCheckedChangeListener(this);
	}


	private void initViews()
	{
		group = (RadioGroup)this.findViewById(R.id.gender);
		rgMan = (RadioButton)this.findViewById(R.id.genderMan);
		rgWoman = (RadioButton)this.findViewById(R.id.genderWoman);
		
		layoutValid = (LinearLayout)this.findViewById(R.id.layoutValid);
		layoutValid.setVisibility(View.GONE);
		
		etError = (EditText)this.findViewById(R.id.etError);
		btnVoiVer = (Button)this.findViewById(R.id.btnVoiVer);
		btnMessVer = (Button)this.findViewById(R.id.btnMessVer);
		edtVer = (EditText)this.findViewById(R.id.etVer);
		edtPwd = (EditText)this.findViewById(R.id.etPassword);
		edtUser = (EditText)this.findViewById(R.id.etAccounts);
		etNickName = (EditText)this.findViewById(R.id.etNickName);
		
		btnRegister = (Button)this.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Log.i("XU", "点击了注册");
				strPwd = edtPwd.getText().toString();
				strUser= edtUser.getText().toString();
				strPhone = etNickName.getText().toString();
				Log.i("XU", "点击了注册11111");
				//strVer  = edtVer.getText().toString();
				if(!isNullOrEmpty(strPwd) && !isNullOrEmpty(strUser) && !isNullOrEmpty(strPhone))
				{
					Log.i("XU", "点击了注册222222222222");
//					if(!Utils.isMobile(strUser))
//					{
//						edtUser.setError("手机号码格式不正确");
//					}
//					else
//					{
						ShareDB.save2DB(SimpleRegister.this, "user_account", strUser);
						ShareDB.save2DB(SimpleRegister.this, "user_password", strPwd);
						
//						localAccount = ShareDB.getStringFromDB(this, "user_account");
//						localPassword= ShareDB.getStringFromDB(this, "user_password");
						register();
//					}
				}
			}
		});
	}

	private boolean isNullOrEmpty(String s)
	{
		if(s != null || !"".equals(s.trim()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int)
	 */
	@Override
	public void loadData(int id,int type)
	{
		// TODO Auto-generated method stub

	}

	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		if(checkedId == R.id.genderMan)
		{
			gender = 1;
		}
		else 
		{
			gender = 2;
		}
	}

}
