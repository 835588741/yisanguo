//package com.soul.project.story.activity;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//import net.tsz.afinal.FinalHttp;
//import net.tsz.afinal.http.AjaxCallBack;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
//
//import com.soul.project.application.dialog.DialogUtil;
//import com.soul.project.application.util.NetManager;
//import com.soul.project.application.util.SHA1;
//import com.soul.project.application.util.ShareDB;
//import com.soul.project.application.util.ToastUtil;
//import com.soul.project.application.util.Utils;
//import com.yisanguo.app.api.API;
//
//public class RegisterActivity extends BaseActivityWithSystemBarColor implements OnClickListener
//{
//	private EditText edtUser,edtPwd,etNickName,edtVer;
//	private Button btnRegister;
//	private Button btnVoiVer;
//	private String strUser,strPwd,strPhone,strVer;
//	private Button btnMessVer;
//	private Dialog loaddingDialog;
//	private FinalHttp finalHttp;
//	private EditText etError;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		
//        // 设置状态栏颜色之前需先调用本段代码
//		Window win = this.getWindow();
//		WindowManager.LayoutParams winParams = win.getAttributes();
//		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//		winParams.flags |= bits;
//		win.setAttributes(winParams);
//        
//        // 创建状态栏的管理实例  
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);  
//        // 激活状态栏设置  
//        tintManager.setStatusBarTintEnabled(true);  
//        // 激活导航栏设置  
//        tintManager.setNavigationBarTintEnabled(true); 
////        tintManager.setTintColor(Color.parseColor("#99000FF"));  
//        tintManager.setStatusBarTintResource(R.drawable.image_bg_titlebar);
////        tintManager.setStatusBarTintColor(getResources().getColor(color)); 
//		
//		setContentView(R.layout.activity_register);
//		
//		SMSSDK.initSDK(this, "effb0a233b97", "b346a1bf288a486f7c9878e28134f83b");
//		EventHandler eh=new EventHandler(){
//
//			@Override
//			public void afterEvent(int event, int result, Object data) {
//				
//				Message msg = new Message();
//				msg.arg1 = event;
//				msg.arg2 = result;
//				msg.obj = data;
//				handler.sendMessage(msg);
//			}
//		};
//		SMSSDK.registerEventHandler(eh);
//		
//        finalHttp = new FinalHttp();
//        finalHttp.configTimeout(10 * 1000);
//        
//		initView();
//		initEvent();
//	}
//
//	private void initEvent()
//	{
//		btnMessVer.setOnClickListener(this);
//		btnVoiVer.setOnClickListener(this);
//	}
//
//	Handler handler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			int event = msg.arg1;
//			int result = msg.arg2;
//			Object data = msg.obj;
//			if (result == SMSSDK.RESULT_COMPLETE) {
//				//短信注册成功后，返回MainActivity,然后提示新好友
//				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//					ToastUtil.show(getApplicationContext(),  "提交验证码成功",ToastUtil.INFO);
//					if(NetManager.newInstance(RegisterActivity.this).isOpenNetwork())
//					{
//						Log.i("XU", "开始去执行注册工作");
//						// 连接服务器 执行注册
//						register();
//					}
//				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//					ToastUtil.show(getApplicationContext(),   "验证码已经发送",ToastUtil.INFO);
//				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
//					ToastUtil.show(getApplicationContext(),  "获取国家列表成功",ToastUtil.INFO);
//				}
//			} else {
//				ToastUtil.show(getApplicationContext(), "验证码错误", ToastUtil.ERROR);
//			}
//			
//		}
//	};
//	
//	private void register()
//	{
//		strPwd = new SHA1().getDigestOfString(strPwd.getBytes());
//
//		Log.i("XU", API.URL+"register.action?&account="+strUser+"&password="+strPwd+"&niname="+strPhone);
//		try
//		{
//			finalHttp.get(API.URL+"register.action?&account="+(URLEncoder.encode(strUser,"utf-8"))+"&password="+strPwd+"&niname="+URLEncoder.encode(strPhone,"utf-8"), new AjaxCallBack<Object>()
//			{
//				@Override
//				public void onFailure(Throwable t, int errorNo, String strMsg)
//				{
//					// TODO Auto-generated method stub
//					super.onFailure(t, errorNo, strMsg);
//					loaddingDialog.dismiss();
//					Log.i("XU", "注册失败="+errorNo);
//				}
//
//				@Override
//				public void onStart()
//				{
//					// TODO Auto-generated method stub
//					super.onStart();
//					loaddingDialog = DialogUtil.createLoadingDialog(RegisterActivity.this,getString(R.string.action_uploading));
//					loaddingDialog.show();
//				}
//
//				@Override
//				public void onSuccess(Object t)
//				{
//					super.onSuccess(t);
//					Log.i("XU", "注册成功="+t.toString());
//					loaddingDialog.dismiss();
//					if(t != null)
//					{
//						if(!"{}".equals(t.toString().trim()))
//						{
//							try
//							{
//								JSONObject object = new JSONObject(t.toString());
//								String message = object.getString("message");
//								ToastUtil.show(RegisterActivity.this, ""+message, ToastUtil.INFO);
//								
//								ShareDB.save2DB(RegisterActivity.this, "user_account", strUser);
//								ShareDB.save2DB(RegisterActivity.this, "user_password", strPwd);
//								ShareDB.save2DB(RegisterActivity.this, "user_nickname", strPhone);
//							}
//							catch (JSONException e)
//							{
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
////				ShareDataUtil.newInstance(LoginOrRegisterActivity.this).putValue("token", token);
////				ShareDataUtil.newInstance(LoginOrRegisterActivity.this).putValue("account", stringAccount);
////				ShareDataUtil.newInstance(LoginOrRegisterActivity.this).putValue("password", stringPassword);
////				ShareDataUtil.newInstance(LoginOrRegisterActivity.this).putValue("userid", userid);
//				}
//			});
//		}
//		catch (UnsupportedEncodingException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void initView()
//	{
//		etError = (EditText)this.findViewById(R.id.etError);
//		btnVoiVer = (Button)this.findViewById(R.id.btnVoiVer);
//		btnMessVer = (Button)this.findViewById(R.id.btnMessVer);
//		edtVer = (EditText)this.findViewById(R.id.etVer);
//		edtPwd = (EditText)this.findViewById(R.id.etPassword);
//		edtUser = (EditText)this.findViewById(R.id.etAccounts);
//		etNickName = (EditText)this.findViewById(R.id.etNickName);
//		
//		btnRegister = (Button)this.findViewById(R.id.btnRegister);
//		btnRegister.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Log.i("XU", "难道会在这？");
//				strPwd = edtPwd.getText().toString();
//				strUser= edtUser.getText().toString();
//				strPhone = etNickName.getText().toString();
//				strVer  = edtVer.getText().toString();
//				Log.i("XU", "难道会在这22222？");
//				
//				if(!isNullOrEmpty(strPwd) && !isNullOrEmpty(strUser) && !isNullOrEmpty(strPhone))
//				{
//					Log.i("XU", "难道会在这？233333");
//
//					if(strUser.length() < 11)//(!Utils.isMobile(strUser))
//					{
//						edtUser.setError("手机号码格式不正确");
//					}
//					else
//					{
//						if(!isNullOrEmpty(strVer))
//						{
//							SMSSDK.submitVerificationCode("86", strUser, strVer);
//						}else 
//						{
//							edtVer.setError("验证码不能为空");
//							//ToastUtil.show(RegisterActivity.this, "验证码不能为空", ToastUtil.ERROR);//.show();
//						}
//					}
//				}
//			}
//		});
//	}
//	
//	@Override
//	protected void onDestroy()
//	{
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		SMSSDK.unregisterAllEventHandler();
//	}
//	
//	private boolean isNullOrEmpty(String s)
//	{
//		if(s != null || !"".equals(s))
//			return false;
//		return true;
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		try
//		{
//			String phone = edtUser.getText().toString();
//			switch (v.getId())
//			{
//				case R.id.btnMessVer:
//					if(phone != null && !"".equals(phone.trim()))
//					{
//						if(phone.length() == 11)//(//Utils.isMobile(phone))
//						{
//							SMSSDK.getVerificationCode("86", phone);//(String country, String phone);
//						}
//						else
//						{
//							edtUser.setError("手机号码格式不正确");
//						}
//					}
//					else
//					{
//						edtUser.setError("请输入手机号码");
//					}
//					break;
//					
//				case R.id.btnVoiVer:
//					
//					if(phone != null && !"".equals(phone.trim()))
//					{
//						if(Utils.isMobile(phone))
//						{
//							SMSSDK.getVoiceVerifyCode(phone,"86");//(String country, String phone);
//						}
//						else
//						{
//							edtUser.setError("手机号码格式不正确");
//						}
//					}
//					else
//					{
//						edtUser.setError("请输入手机号码");
//					}
//
//					break;
//				default:
//					break;
//			}
//		}
//		catch (Exception e)
//		{
//			// TODO: handle exception
//			etError.setText("异常:"+e.toString());
//			ToastUtil.show(RegisterActivity.this, "异常:"+e.toString(), ToastUtil.ERROR);
//		}
//	}
//
//	@Override
//	public void loadData(int id)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//}
