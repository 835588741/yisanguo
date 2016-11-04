package com.soul.project.story.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soul.project.application.bean.PlaceBean.userdate;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.interfaces.IActivity;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.SHA1;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

public class LoginActivity extends BaseActivity implements IActivity, OnClickListener
{
	private EditText etUserName;
	private EditText etPassWord;
	private Button btnLogin;
	private Button btnSimpleRegister;
	private TextView txtReturn;
	private TextView txtRegister;
	private ProgressDialog dialog;
	private FinalHttp finalHttp;
	private Dialog loaddingDialog;
	private String account;
	private String password;
	// 版本控制，此版本号必须要和服务器上一直，不然不会返回信息
	public static int version = 21;
	private String localAccount;
	private String localPassword;
	private TextView txtProgress;
	private ProgressBar pb_progressbar;
	private Button btnCancel;
	private Dialog dialogForUpdateVersion;
	private long lengthOfFile;
	private TextView txtForGetPWD;
	private TextView txtQuestion;
	private EditText txtAnswer;
	private EditText etAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ShareDB.save2DB(this, "lasttime", System.currentTimeMillis());
		MyApplication.list.add(this);
		setStatusBarColor();
		
		//ActivityUtil.goToNewActivity(this, FengDiActivity.class);
		
		setContentView(R.layout.activity_login_layout);
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(10 * 1000);
		initView();
		initValue();
		initEvent();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		auto();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.list.remove(this);
	}

	private void auto()
	{
		localAccount = ShareDB.getStringFromDB(this, "user_account");
		localPassword = ShareDB.getStringFromDB(this, "user_password");

		if (localAccount != null)
			etUserName.setText(localAccount);

		if (localPassword != null)
			etPassWord.setText(localPassword);
	}

	@Override
	public void initView()
	{
		txtForGetPWD = (TextView)this.findViewById(R.id.txtForGetPWD);
		btnSimpleRegister = (Button) this.findViewById(R.id.btnSimpleRegister);
		etUserName = (EditText) this.findViewById(R.id.etAccounts);
		etPassWord = (EditText) this.findViewById(R.id.etPassword);
		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		txtReturn = (TextView) this.findViewById(R.id.include_view_btnLeft);
		txtRegister = (TextView) this.findViewById(R.id.include_view_btnRight);
		// btnRegister= (Button)this.findViewById(R.id.btnRegister);

		auto();
	}

	@Override
	public void initValue()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initEvent()
	{
		// TODO Auto-generated method stub
		btnLogin.setOnClickListener(this);
		txtRegister.setOnClickListener(this);
		txtReturn.setOnClickListener(this);
		// btnRegister.setOnClickListener(this);
		btnSimpleRegister.setOnClickListener(this);
		txtForGetPWD.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		// 登陆
			case R.id.btnLogin:
				// ActivityUtil.goToNewActivity(LoginActivity.this,
				// MainActivity.class);
				login();
				break;
			// 返回
			case R.id.include_view_btnLeft:
				finish();
				break;
			case R.id.btnCancel:
				if (dialogForUpdateVersion != null)
					dialogForUpdateVersion.dismiss();
				break;
			// 注册
			// case R.id.btnRegister:
			// ActivityUtil.goToNewActivity(this, RegisterActivity.class);
			// break;
			case R.id.btnSimpleRegister:
				ActivityUtil.goToNewActivity(this, SimpleRegister.class);
				break;
			case R.id.txtForGetPWD:
				resetpwd();
				break;
			default:
				break;
		}
	}
	
	String accountForReset = null;
	private void resetpwd()
	{
		final Dialog dialogForMiMa = new Dialog(this, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_forget_passwprd, null);
		etAccount = (EditText) view.findViewById(R.id.etAccount);
		Button btnConfirm = (Button)view.findViewById(R.id.btnConfirm);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		
		txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
		if(MyApplication.user != null)
		{
			txtQuestion.setText(MyApplication.user.getQuestion());
		}
		
//		String account = ShareDB.getStringFromDB(LoginActivity.this, "user_account");
//		if(account == null||"".equals(account.trim()))
//		{
//			etAccount.setVisibility(View.VISIBLE);
//		}

		txtAnswer = (EditText) view.findViewById(R.id.etPwd);
		
		btnConfirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String answer = txtAnswer.getText().toString();
				
				if(answer == null || "".equals(answer.trim()))
				{
					ToastUtil.showStaticToastShort(LoginActivity.this, "密保答案不能为空!");
					return;
				}
				
				AjaxParams params = new AjaxParams();
//				accountForReset = ShareDB.getStringFromDB(LoginActivity.this, "user_account");
//				if(accountForReset == null || "".equals(accountForReset.trim()))
//				{
					accountForReset = etAccount.getText().toString();
					
					if(accountForReset == null || "".equals(accountForReset.trim()))
					{
						ToastUtil.showStaticToastShort(LoginActivity.this, "账号不能为空!");
						return;
					}
//				}
				
				params.put("account",accountForReset);
				params.put("answer", answer);
				finalHttp.post(API.LOGIN_OR_REGISTERT+"resetmima.action?",params, new AjaxCallBack<Object>()
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
						dialog = MessageDialog.createLoadingDialog(LoginActivity.this, "请求服务器中...");
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
									Builder builder = new Builder(LoginActivity.this);
									builder.setTitle("重要信息");
									builder.setMessage(mes);
									builder.setPositiveButton("我会牢记的", null);
									builder.show();
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

	private void login()
	{
		account = getTextValue(etUserName);
		password = getTextValue(etPassWord);

		if (account == null || account.length() == 0 || password == null || password.length() == 0)
			return;
		password = (password.length() == 40 ? password : new SHA1().getDigestOfString(password.getBytes()));
		// http://169.254.85.186:8080/springMVC/loginAction/login.action?&account=835588741&password=29E686173E7779BD83FBB79E7DB00B2E68164A2B&version=5

		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("account", account);
		ajaxParams.put("password", password);
		ajaxParams.put("version", ""+version);
		finalHttp.addHeader("sign", getSignature());
		
//		&account=" + account + "&password=" + password + "&version=" + version
		// Log.i("XU",
		// "请求地址="+API.LOGIN_OR_REGISTERT+"login.action?&account="+account+"&password="+password+"&version="+version);
		finalHttp.post(API.LOGIN_OR_REGISTERT + "login.action?",ajaxParams, new AjaxCallBack<Object>()
		{
			Dialog dialog;

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				ToastUtil.show(LoginActivity.this, "登陆失败，请稍后重试!" + strMsg, ToastUtil.ERROR);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(LoginActivity.this, "正在登陆中...");// CustomProgressDialog.getProgressDiaolgNoTitle(LoginActivity.this,
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try
				{
					if (t != null && !"".equals(t.toString().trim()))
					{
						if ("{}".equals(t.toString().trim()))
						{
							ToastUtil.show(LoginActivity.this, "不存在该账号账号!", ToastUtil.ERROR);
						}
						else
						{
							JSONObject jsonObject = new JSONObject(t.toString());
							if (jsonObject.getInt("code") == 200)
							{
								ToastUtil.showStaticToastShort(LoginActivity.this, "登陆成功!正在写入数据...", ToastUtil.INFO);
								Gson gson = new Gson();
								userdate temp = gson.fromJson(jsonObject.get("data").toString(), userdate.class);
								MyApplication.user = temp;
								if (temp != null)
								{
									// 登录成功后服务器返回一部分的用户数据 这时应保存更新本地的这部分数据
									ShareDB.save2DB(LoginActivity.this, "user_account", temp.getAccount());
									ShareDB.save2DB(LoginActivity.this, "user_password", temp.getPassword());
									ShareDB.save2DB(LoginActivity.this, "user_nickname", temp.getName());

									ActivityUtil.goToNewActivity(LoginActivity.this, MainPanelActivity.class);
								}
								ToastUtil.showStaticToastShort(LoginActivity.this, "登录成功!正在进入主界面...", ToastUtil.INFO);
							}
							else if (jsonObject.getInt("code") == 405)
							{
								Builder builder = new Builder(LoginActivity.this);
								builder.setTitle("应用提示");
								builder.setCancelable(false);
								builder.setMessage(jsonObject.getString("message"));
								builder.setNegativeButton("退出应用", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										// TODO Auto-generated method stub
										dialog.dismiss();
										MyApplication.exitAPP();
									}
								});
								builder.setPositiveButton("下载新版", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										// TODO Auto-generated method stub
										dialog.dismiss();
										downFile(API.DOWN_LOAD);
									}
								});
								builder.show();
							}
							else if(jsonObject.getInt("code") == 1024)
							{
								sendBroadcast(new Intent("exit_app"));
							}
							else
							{
								String mes = jsonObject.getString("message");
								if (mes != null)
									ToastUtil.show(LoginActivity.this, mes);
								else
									ToastUtil.show(LoginActivity.this, "异常原因，登陆请求失败!");
							}
						}
					}
					else
						ToastUtil.show(LoginActivity.this, "登陆请求失败!或网络异常");
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
				finally
				{
					if (dialog != null)
						dialog.dismiss();
				}
			}
		});
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
	
	private void downFile(final String url)
	{
		// pBar.show();
		dialogForUpdateVersion = new Dialog(LoginActivity.this, R.style.myDialogTheme);
		Window dialogWindow = dialogForUpdateVersion.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_download_wait, null);
		dialogForUpdateVersion.setContentView(view);
		pb_progressbar = (ProgressBar) view.findViewById(R.id.pb_progressbar);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		txtProgress = (TextView) view.findViewById(R.id.txtProgress);
		lp.width = -1;
		lp.height = -2;
		dialogWindow.setAttributes(lp);
		dialogForUpdateVersion.setCancelable(false);
		dialogForUpdateVersion.show();

		new Thread()
		{
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					lengthOfFile = length;
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null)
					{
						File file = new File(Environment.getExternalStorageDirectory(), "new.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0)
							{
							}
							Message message = handler.obtainMessage();
							message.what = 1;
							message.arg1 = count;
							handler.sendMessage(message);
							// Log.i("XU", "总长度："+length+"  当前:"+count);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
					{
						fileOutputStream.close();
					}
					down();
				}
				catch (ClientProtocolException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == 1)
			{
				if (dialogForUpdateVersion.isShowing())
				{
					if (txtProgress != null)
					{
						int span = (int) (lengthOfFile / 5000.0);
						// 20000 / 5000 = 4
						pb_progressbar.setProgress(msg.arg1 / span);
						txtProgress.setText("" + getPrintSize(msg.arg1) + " / " + getPrintSize(lengthOfFile));
					}
				}
			}
			else
			{
				ToastUtil.show(LoginActivity.this, "下载完成即将开始安装...");
				dialogForUpdateVersion.dismiss();
			}
		};
	};

	public static String getPrintSize(long size)
	{
		// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
		if (size < 1024)
		{
			return String.valueOf(size) + "B";
		}
		else
		{
			size = size / 1024;
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (size < 1024)
		{
			return String.valueOf(size) + "KB";
		}
		else
		{
			size = size / 1024;
		}
		if (size < 1024)
		{
			// 因为如果以MB为单位的话，要保留最后1位小数，
			// 因此，把此数乘以100之后再取余
			size = size * 100;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
		}
		else
		{
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			size = size * 100 / 1024;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
		}
	}

	private void down()
	{
		handler.post(new Runnable()
		{
			public void run()
			{
				Message msg = handler.obtainMessage();
				msg.what = 2;
				handler.sendMessage(msg);
				update();
				Log.i("XU", "下载完成");
			}
		});
	}

	private void update()
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "new.apk")), "application/vnd.android.package-archive");
		startActivity(intent);
	}

	private String getTextValue(EditText editText)
	{
		// TODO Auto-generated method stub
		String temp = editText.getText().toString();
		if (temp == null || temp.trim().equals(""))
		{
			editText.setError("不能为空");
			return null;
		}
		else
			return temp;
	}

	public void setStatusBarColor()
	{
		// // 设置状态栏颜色之前需先调用本段代码
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
		// tintManager.setTintColor(Color.parseColor("#99000FF"));
		tintManager.setStatusBarTintColor(getResources().getColor(R.color.gray_light));
	}
}
