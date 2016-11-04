package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.GoodsListAdapter;
import com.soul.project.application.bean.Goods;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ResponeUtil;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class GoodsListActivty extends BaseActivityWithSystemBarColor implements OnClickListener, OnItemClickListener
{
	private Button btnOut;
	private Button btnClean;
	private ListView listGoods;
	private GoodsListAdapter adapter;
	private List<Goods> list = new ArrayList<Goods>();
	private String uuid;
//	private MTextView txtTitle;
	LinearLayout rootLayout;
	int activityType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		
		Intent intent = getIntent();
		uuid = intent.getStringExtra("uuid");
		activityType = intent.getIntExtra("activityType", 0);
		
		if(activityType == 1024)
		{
			ToastUtil.show(GoodsListActivty.this, "直接点击物品项，即可弹出价格输入，输入完毕提交完成拍卖。");
		}
		else if(activityType == 1026)
		{
			ToastUtil.showShort(GoodsListActivty.this, "请点击选择要进行价值评估的装备或物品。");
		}
		else if(activityType == 1027)
		{
			ToastUtil.showShort(GoodsListActivty.this, "请点击选择要进行升级的装备。");
		}
		
		initView();
		initEvent();
		loadData(0,0);
	}
	
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
	}
	
	public interface CallBack
	{
		public void call();
	}

	private void initEvent()
	{
		btnOut.setOnClickListener(this);
		btnClean.setOnClickListener(this);
//		if(activityType != 0)
			listGoods.setOnItemClickListener(this);
	}

	private void initView()
	{
		rootLayout = (LinearLayout)this.findViewById(R.id.rootLayout);
		btnClean = (Button)this.findViewById(R.id.btnClean);
		btnOut   = (Button)this.findViewById(R.id.btnOut);
		listGoods= (ListView)this.findViewById(R.id.listGoods);
	}

	@Override
	public void loadData(int id,int type)
	{
		String url ; 
		if(activityType != 0)
			url = API.URL2+"getgoods.action?&masterid="+MyApplication.getUUID(this)+"&state=2";
		else
			url = API.URL2+"getgoods.action?&masterid="+uuid+"&state=2";
		finalHttp.get(url, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "请求失败:"+strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "物品="+t.toString());
				if(t != null)
				{
					//[{"gid":"6be10ed0fb6848c99e01c4d99de0bb82","name":"七百七十八个铜板","masterid":"0003","price":778,"life":null,"state":2,"addhp":0,"addattack":0,"adddefence":0,"adddexterous":0,"type":11,"ggrade":null,"descript":null,"gareaid":null,"gtype":0},
					//{"gid":"931bd1a9419244919b4467800fc69b57","name":"长木刀","masterid":"0003","price":70,"life":255,"state":2,"addhp":0,"addattack":20,"adddefence":0,"adddexterous":0,"type":101,"ggrade":1,"descript":"用桃木销成的长刀，也就能用来练练简单的刀法。","gareaid":-1,"gtype":0},{"gid":"d9555641b7f8479e9e1955a3a6283b8d","name":"六十七两白银","masterid":"0003","price":67000,"life":null,"state":2,"addhp":0,"addattack":0,"adddefence":0,"adddexterous":0,"type":10,"ggrade":null,"descript":null,"gareaid":null,"gtype":0}]

					Gson gson = new Gson();
					list = gson.fromJson(t.toString(), new TypeToken<List<Goods>>(){}.getType());
					
					if(list != null && list.size() > 0)
					{
						adapter = new GoodsListAdapter(GoodsListActivty.this, list,activityType);
						adapter.addCallBack(new CallBack()
						{
							
							@Override
							public void call()
							{
								// TODO Auto-generated method stub
								loadData(0, 0);
							}
						});
						listGoods.setAdapter(adapter);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnOut  :finish();break;
			case R.id.btnClean:clean();break;
			case R.id.btnConfirm:
				if(activityType == 1024)
					auction();
				else
					requestTransaction();
				if(dialog != null)
					dialog.dismiss();
				break;
			case R.id.btnCancel:if(dialog != null)dialog.dismiss();break;
			default:
				break;
		}
	}

	private void auction()
	{
		String text = etInputPrice.getText().toString();
		if(text != null && text.length() > 0 && !"0".equals(text))
		{
			try
			{
				Long number = Long.valueOf(text);
				if(number > 0)
				{
					finalHttp.get(API.TRANSACTION+"sell.action?&seller_uuid="+uuid+"&gid="+gid+"&price="+number, new AjaxCallBack<Object>()
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
							dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "提交中...");
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
									JSONObject jsonObject;
									jsonObject = new JSONObject(t.toString());
									String mes = jsonObject.getString("message");
									list.remove(position);
									adapter.notifyDataSetChanged();
									if(mes != null)
										ToastUtil.show(GoodsListActivty.this, mes);
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
			catch (Exception e)
			{
				
			}
		}
		else
		{
			ToastUtil.show(GoodsListActivty.this, "请正确输入交易价格!");
		}
	}

	private void requestTransaction()
	{
		String text = etInputPrice.getText().toString();
		if(text != null && text.length() > 0 && !"0".equals(text))
		{
			try
			{
				Long number = Long.valueOf(text);
				if(number > 0)
				{
					//07-31 02:19:11.695: I/XU(5115): 请求地址:http://169.254.85.186:8080/springMVC/requestTransaction/transaction.action?&seller_uuid=0040&buyer_uuid=0001&gid=58354ce34c744859829d7e71df4250aa
//					Log.i("XU", "请求地址:"+API.TRANSACTION+"transaction.action?&seller_uuid="+MyApplication.getUUID(this)+"&buyer_uuid="+uuid+"&gid="+gid);
					finalHttp.get(API.TRANSACTION+"transaction.action?&seller_uuid="+MyApplication.getUUID(this)+"&buyer_uuid="+uuid+"&gid="+gid+"&price="+number, new AjaxCallBack<Object>()
							{
						Dialog dialog;
								@Override
								public void onFailure(Throwable t, int errorNo, String strMsg)
								{
									// TODO Auto-generated method stub
									super.onFailure(t, errorNo, strMsg);
									dialog.dismiss();
									ToastUtil.show(GoodsListActivty.this, "交易请求提交失败!"+strMsg);
								}

								@Override
								public void onStart()
								{
									// TODO Auto-generated method stub
									super.onStart();
									dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "提交中...");
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
											String message = jsonObject.getString("message");
											if(message != null)
											ToastUtil.show(GoodsListActivty.this, message);
										
											if(GoodsListActivty.this.dialog != null)
											{
												GoodsListActivty.this.dialog.dismiss();
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
							});
				}
			}
			catch (Exception e)
			{
				
			}
		}
		else
		{
			ToastUtil.show(GoodsListActivty.this, "请正确输入交易价格!");
		}
	}

	// 清空行囊
	private void clean()
	{
		// TODO Auto-generated method stub
		
	}

	EditText etInputPrice;
	Button btnConfirm;
	Button btnCancel;
	String gid;
	String gname;
	MTextView txtGname;
	Dialog dialog;
	int position;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		gid = list.get(position).getGid();
		
		if(activityType == 1024 || activityType == 1)
		{
			dialog = new Dialog(this,R.style.myDialogTheme);
			LayoutInflater inflater = LayoutInflater.from(this);
			View viewDialog = inflater.inflate(R.layout.dialog_transaction_select_price, null);
			dialog.setContentView(viewDialog);
			
			this.position = position;
			gname = list.get(position).getName();
			etInputPrice = (EditText) viewDialog.findViewById(R.id.etInputPrice);
			btnConfirm = (Button)viewDialog.findViewById(R.id.btnConfirm);
			btnCancel  = (Button)viewDialog.findViewById(R.id.btnCancel);
			txtGname   = (MTextView)viewDialog.findViewById(R.id.txtGname);
			if(gname != null)
			{
				if(activityType == 1024)
					txtGname.setText("你选择将"+gname+"做为拍卖的物品,请确认");
				else
					txtGname.setText("你选择用"+gname+"做交易,请确认");
			}
			btnConfirm.setOnClickListener(this);
			btnCancel.setOnClickListener(this);
			
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = -1;
			lp.height = -2;
			dialogWindow.setAttributes(lp);
			
			dialog.show();
		}
		else if(activityType == 1025)
		{
			Builder builder = new Builder(GoodsListActivty.this);
			builder.setItems(new String[]{"用银钱维修","使用续灵宝石"}, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if(which == 0)
					{
						finalHttp.get(API.URL2+"repair.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid, new AjaxCallBack<Object>()
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
										dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "获取维修价格中...");
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
												String mes = jsonObject.getString("message");
												if(code == 200)
												{
													Builder builder = new Builder(GoodsListActivty.this);
													builder.setTitle("维修报价");
													if(mes != null)
													builder.setMessage(mes);
													builder.setNegativeButton("我要维修", new android.content.DialogInterface.OnClickListener()
													{
														@Override
														public void onClick(DialogInterface dialog, int which)
														{
															// TODO Auto-generated method stub
															finalHttp.get(API.URL2+"repairconfirm.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid, new AjaxCallBack<Object>()
															{
																@Override
																public void onFailure(Throwable t, int errorNo, String strMsg)
																{
																	// TODO Auto-generated method stub
																	super.onFailure(t, errorNo, strMsg);
																	ToastUtil.showShort(GoodsListActivty.this, "维修失败");
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
																			String mes = jsonObject.getString("message");
																			if(mes != null)
																				ToastUtil.showShort(GoodsListActivty.this, mes);
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
													builder.setPositiveButton("我不修了", null);
													builder.show();
												}
												else
												{
													if(mes != null)
													ToastUtil.showStaticToast(GoodsListActivty.this, mes);
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
					else if(which == 1)
					{
						finalHttp.get(API.URL2+"repairbyxlbs.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid, new AjaxCallBack<Object>()
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
								dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "请求服务器中...");
								dialog.show();
							}

							@Override
							public void onSuccess(Object t)
							{
								// TODO Auto-generated method stub
								super.onSuccess(t);
								dialog.dismiss();
								ResponeUtil.getInstance().analysis(GoodsListActivty.this, t);
							}
						});
					}
				}
			});
			builder.setTitle("请选择维修类型");
			builder.setNegativeButton("取消", null);
			builder.show();
		}
		else if(activityType == 1026)
		{
			// 评估
			finalHttp.get(API.URL_EQU+"pingu.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid, new AjaxCallBack<Object>()
			{
				Dialog dialog;
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					Log.i("XU", "出现错误"+strMsg);
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "申请评估中...");
					dialog.show();
				}

				@Override
				public void onSuccess(Object t)
				{
					// TODO Auto-generated method stub
					super.onSuccess(t);
					dialog.dismiss();
					Log.i("XU", "成功完成"+t.toString());
					if(t != null)
					{
						try
						{
							JSONObject jsonObject;
							jsonObject = new JSONObject(t.toString());
							int code = jsonObject.getInt("code");
							String mes = jsonObject.getString("message");
							if(code == 200)
							{
								Builder builder = new Builder(GoodsListActivty.this);
								builder.setTitle("评估结果");
								builder.setMessage(Html.fromHtml(mes));
								builder.setPositiveButton("知道了", null);
								builder.show();
							}
							else
							{
								ToastUtil.showStaticToastShort(GoodsListActivty.this, mes);
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
		else if(activityType == 1027)
		{
			// 获取提示和升级条件
			updatequei(1001,API.URL_EQU+"upgraderequest.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid);
		}
		else
		{
			Goods goods = list.get(position);
			int type = goods.getType();
			Intent intent = new Intent(this, GoodDetailActivity.class);
			if(type == 9 && type == 10 && type == 11)
			{
				intent.putExtra("type", 1);
				intent.putExtra("name", goods.getName());
				intent.putExtra("desc", "这是财富的象征，只要有它就可以买很多很多馒头包子。");
				intent.putExtra("grade", 0);
			}
			else
			{
				intent.putExtra("type", 2);
				intent.putExtra("name", goods.getName());
				intent.putExtra("desc", goods.getDescript());
				intent.putExtra("grade", goods.getGgrade());
				intent.putExtra("attack", goods.getAddattack());
				intent.putExtra("defence", goods.getAdddefence());
				intent.putExtra("life", goods.getLife());
				intent.putExtra("star", goods.getStar());
				intent.putExtra("hp", goods.getAddhp());
			}
		//	{"gid":"931bd1a9419244919b4467800fc69b57","name":"长木刀","masterid":"0003","price":70,"life":255,"state":2,
		//   "addhp":0,"addattack":20,"adddefence":0,"adddexterous":0,"type":101,"ggrade":1,"descript":"用桃木销成的长刀，也就能用来练练简单的刀法。","gareaid":-1,"gtype":0}
			ActivityUtil.goToNewActivityWithComplement(GoodsListActivty.this, intent);
		}
	}
	
	private void updatequei(final int type,String url)
	{
		finalHttp.get(url, new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(GoodsListActivty.this, "连接服务器中...");
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
						String mes = jsonObject.getString("message");
//						int code = jsonObject.getInt("code");
						if(mes!= null)
						{
							Message message = handler.obtainMessage();
							message.what = type;
							message.obj = mes;
							handler.sendMessage(message);
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
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == 1001)
			{
				String mes = (String) msg.obj;
				Builder builder = new Builder(GoodsListActivty.this);
				builder.setTitle("提示");
				if(mes!=null)
				builder.setMessage(Html.fromHtml(mes));
				builder.setPositiveButton("确定升级", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						updatequei(1002,API.URL_EQU+"upgradeequip.action?&uuid="+MyApplication.getUUID(GoodsListActivty.this)+"&gid="+gid);
					}
				});
				builder.setNegativeButton("取消升级", null);
				builder.show();
			}
			else if(msg.what == 1002)
			{
				String mes = (String) msg.obj;
				Builder builder = new Builder(GoodsListActivty.this);
				builder.setTitle("提示");
				if(mes!=null)
				builder.setMessage(Html.fromHtml(mes));
				builder.setPositiveButton("我知道了", null);
				builder.show();
			}
		};
	};

}
