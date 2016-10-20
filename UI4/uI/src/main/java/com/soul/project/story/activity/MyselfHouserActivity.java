package com.soul.project.story.activity;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

public class MyselfHouserActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	Button btnAddMoney;
	Button btnCheZhuang;
	Button btnReturnBack;
	TextView txtHouseBenJin;
	TextView txtHouseDiff;
	TextView txtHouseCurr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_houser);
		
		txtHouseCurr = (TextView)this.findViewById(R.id.txtHouseCurr);
		txtHouseBenJin = (TextView)this.findViewById(R.id.txtHouseBenJin);
		txtHouseDiff = (TextView)this.findViewById(R.id.txtHouseDiff);
		btnCheZhuang = (Button)this.findViewById(R.id.btnCheZhuang);
		btnAddMoney  = (Button)this.findViewById(R.id.btnAddMoney);
		btnReturnBack= (Button)this.findViewById(R.id.btnReturnBack);
		
		btnCheZhuang.setOnClickListener(this);
		btnAddMoney.setOnClickListener(this);
		btnReturnBack.setOnClickListener(this);
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		loadData(1, 1);
	}
	

	@Override
	public void onClick(View v)
	{
		if(v == btnAddMoney)
		{
			
		}
		else if(v == btnCheZhuang)
		{
			//zhuijia();
			chezhuang();
		}
		else if(v == btnReturnBack)
		{
			finish();
		}
	}

	private void chezhuang()
	{
		finalHttp.get(API.URL_OTHER+"cancelhousemaster.action?&uuid="+MyApplication.getUUID(MyselfHouserActivity.this), new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "stMs="+strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(MyselfHouserActivity.this, "获取信息中...");
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
						String mes ;
						if(code == 200)
						{
							mes = jsonObject.getString("message");
							if(mes != null)
							{
								Builder builder = new Builder(MyselfHouserActivity.this);
								builder.setTitle("提示");
								builder.setMessage(mes);
								builder.setPositiveButton("知道了", null);
								builder.show();
							}
							else if(code == 201)
							{
								mes = jsonObject.getString("message");
								if(mes != null)
								{
									ToastUtil.showStaticToast(MyselfHouserActivity.this, mes);
								}
							}
						}
					}
					catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			}
		});
	}


	private void zhuijia()
	{
		finalHttp.get(API.URL_OTHER+"metohousemaster.action?&uuid"+MyApplication.getUUID(MyselfHouserActivity.this)+"&money=1&type=1", new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(MyselfHouserActivity.this, "获取信息中...");
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
						// 钱不够
						if(code == 202)
						{
							String message = jsonObject.getString("message");
							if(message != null)
								ToastUtil.showStaticToast(MyselfHouserActivity.this, message);
						}
						else if(code == 200)
						{
							
						}
						// 输入坐庄金额
						else if(code == 201)
						{
							String message = jsonObject.getString("message");
							if(message != null)
							{
								
							
							Builder builder = new Builder(MyselfHouserActivity.this);
							View view = LayoutInflater.from(MyselfHouserActivity.this).inflate(R.layout.dialog_select_number_edittext, null);
							final EditText etSelectNumber = (EditText) view.findViewById(R.id.etSelectNumber);
							builder.setTitle("赌坊提示:");
							builder.setMessage(message);
							etSelectNumber.setHint("最低一百两");
							builder.setView(view);
							builder.setNegativeButton("取消行动", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									
								}
							});
							
							builder.setPositiveButton("我确定了", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									//String uuid,String gid,String number,String areaid)
									String str = etSelectNumber.getText().toString();
									if(str == null || str.length() < 1)
									{
										ToastUtil.showShort(MyselfHouserActivity.this, "请输入数额");
										return;
									}
									long money = 100;
									try
									{
										money = Long.valueOf(str);
									}
									catch (Exception e)
									{
										ToastUtil.showShort(MyselfHouserActivity.this, "格式不正确");
									}

									finalHttp.get(API.URL_OTHER+"metohousemaster.action?&uuid"+MyApplication.getUUID(MyselfHouserActivity.this)+"&money=1&type=2", new AjaxCallBack<Object>()
									{
										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
											ToastUtil.showShort(MyselfHouserActivity.this, "操作失败:错误码"+errorNo);
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
											if(t != null)
											{
												try
												{
													JSONObject jsonObject = new JSONObject(t.toString());
													String message = jsonObject.getString("message");
													if(message != null)
													{
														Builder builder = new Builder(MyselfHouserActivity.this);
														builder.setTitle("提示");
														builder.setMessage(message);
														builder.setNegativeButton("知道了", null);
														builder.show();
													}
												}
												catch (JSONException e)
												{
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
											//Log.i("XU", "成功"+t.toString());
										}
									});
								}
							});
							builder.show();
							}
						}
					}
					catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			}
		});		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.myself_houser, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub
		finalHttp.get(API.URL_OTHER+"metohousemaster.action?&uuid="+MyApplication.getUUID(MyselfHouserActivity.this)+"&money=1&type=1", new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(MyselfHouserActivity.this, "获取信息中...");
				dialog.show();						
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t!=null)
				{
					JSONObject jsonObject;
					try
					{
						jsonObject = new JSONObject(t.toString());
						long houseinitmoney = jsonObject.getLong("houseinitmoney");
						long housecurrendmoney = jsonObject.getLong("housecurrendmoney");
						txtHouseBenJin.setText("本金:"+houseinitmoney+" 银子");
						txtHouseCurr.setText("当前:"+housecurrendmoney+" 银子");
						long cur =  housecurrendmoney - houseinitmoney;
						if(cur < 0)
						{
							txtHouseDiff.setTextColor(getResources().getColor(R.color.green));
							txtHouseDiff.setText("收支情况:亏损 "+cur+" 银子");
						}
						else if(cur > 0)
						{
							txtHouseDiff.setTextColor(getResources().getColor(R.color.red));
							txtHouseDiff.setText("收支情况:盈余 "+cur+" 银子");
						}
						else 
						{
							txtHouseDiff.setTextColor(getResources().getColor(R.color.blue_two));
							txtHouseDiff.setText("收支情况:持平 "+cur+" 银子");
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					jsonObject.put("houseinitmoney", houseinitmoney / 1000);
//					jsonObject.put("housecurrendmoney", housecurrendmoney / 1000);
				}
			}
		});
				
	}

}
