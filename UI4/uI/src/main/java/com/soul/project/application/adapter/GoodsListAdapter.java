/**
 * 
 */
package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.soul.project.application.bean.Goods;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.GoodsListActivty.CallBack;
import com.soul.project.story.activity.LoginActivity;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/**
 * @file GoodsListAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年6月15日 下午4:53:47
 */
public class GoodsListAdapter extends BaseAdapter
{
	private List<Goods> list;
	private LayoutInflater inflater;
	private Context context;
	FinalHttp finalHttp;
	EditText etSelectNumber;
	CallBack callBack;
	int activityType;
	
	public GoodsListAdapter(Context context,List<Goods> list, int activityType)
	{
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.activityType = activityType;
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}
	
	public void  addCallBack(CallBack callBack)
	{
		this.callBack = callBack;
	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_goods_list, null);
			
			holder.txtGoodsName = (MTextView)convertView.findViewById(R.id.txtGoodsName);
			holder.txtDiscard   = (MTextView)convertView.findViewById(R.id.txtDiscard);
			holder.txtArmed 	 = (MTextView)convertView.findViewById(R.id.txtArmed);
			holder.txtGoodsHP = (MTextView)convertView.findViewById(R.id.txtGoodsHP);
			holder.txtGoodsAttack = (MTextView)convertView.findViewById(R.id.txtGoodsAttack);
			holder.txtGoodsDefense= (MTextView)convertView.findViewById(R.id.txtGoodsDefense);
			holder.layoutAttr = (LinearLayout)convertView.findViewById(R.id.layoutAttr);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Goods goods = list.get(position);
		
		int type = goods.getType();
		int gtype = goods.getGtype();
		if(type != 9 && type != 10 && type != 11 && type != 8)
		{
			holder.layoutAttr.setVisibility(View.VISIBLE);
			holder.txtGoodsAttack.setText("攻+"+goods.getAddattack());
			holder.txtGoodsDefense.setText("防+"+goods.getAdddefence());
			holder.txtGoodsHP.setText("血+"+goods.getAddhp());
			holder.txtGoodsName.setText(goods.getName()+" "+goods.getGgrade()+"级");
		}
		else
		{
			String countString = "";
			if(gtype >= 281)
				countString = " "+goods.getCount()+"个";
			else
				countString = "";
			
			holder.txtGoodsName.setText(goods.getName()+countString);			
			holder.layoutAttr.setVisibility(View.GONE);
		}
		
//		holder.txtGoodsName.setText(goods.getName());
//		int type = goods.getType();
		
		if(type == 101 || type ==102 || type==103 || type == 2|| type == 3|| type == 4|| type==5||type==6 || type==7)
		{
			holder.txtArmed.setVisibility(View.VISIBLE);
			holder.txtArmed.setText("装备");
			holder.txtArmed.setOnClickListener(new Event(goods.getGid(),position,1,gtype));
		}
		else if(type == 8)
		{
			holder.txtArmed.setVisibility(View.VISIBLE);
			holder.txtArmed.setText("使用");
			holder.txtArmed.setOnClickListener(new Event(goods.getGid(),position,1,gtype));
		}
		else
		{
			holder.txtArmed.setVisibility(View.GONE);
		}
		
		holder.txtDiscard.setOnClickListener(new Event(goods.getGid(), position,2,gtype));
		if(activityType != 0)
		{
			holder.txtDiscard.setVisibility(View.GONE);
			holder.txtArmed.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		String gid;
		int position;
		int type;
		int gtypeService;
		public Event(String gid,int position,int type, int gtype)
		{
			this.gtypeService = gtype;
			this.type = type;
			this.position = position;
			this.gid = gid;
		}

		@Override
		public void onClick(View v)
		{
			
			if(type==1)
			{
				if(gid!= null &&!"".equals(gid))
				{
					
					finalHttp.get(API.URL2+"armed.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid,new AjaxCallBack<Object>()
							{
						Dialog dialog;
						
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							Log.i("XU", "获取回馈失败="+strMsg);
							ToastUtil.show(context, "连接超时！数据获取失败！请检查网络！");
							dialog.dismiss();
						}
						
						@Override
						public void onStart()
						{
							// TODO Auto-generated method stub
							super.onStart();
							dialog = MessageDialog.createLoadingDialog(context, "Loding");
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
									int code = jsonObject.getInt("code");
									if(code == 200)
									{
										if(list!=null && list.size() > position)
										{

												list.remove(position);
												notifyDataSetChanged();
										}
									}
									ToastUtil.showStaticToastShort(context, ""+message);
								}
								catch (JSONException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
									ToastUtil.show(context, "数据获取失败!"+e.toString());
								}
							}
						}
							});
				}
			}
			else if(type==2)
			{
				if(gid!= null &&!"".equals(gid))
				{
					if(MyApplication.user == null)
					{
						ToastUtil.show(context, "数据失效!请重新登录!");
						ActivityUtil.goToNewActivity((Activity)context, LoginActivity.class);
					}
					else
					{
						if(list != null && position < list.size())
						{
							Goods goods = list.get(position);
							final int gtype = goods.getType();
							
							if(gtype != 9 && gtype != 10 &&gtype != 11)
							{
								finalHttp.get(API.URL2+"throwthing.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid+"&areaid="+MyApplication.user.getAreaid(), new AjaxCallBack<Object>()
										{
											Dialog dialog;
											@Override
											public void onFailure(Throwable t, int errorNo, String strMsg)
											{
												// TODO Auto-generated method stub
												super.onFailure(t, errorNo, strMsg);
												dialog.dismiss();
												Log.i("XU", "Error = "+errorNo+"   msg="+strMsg);
											}

											@Override
											public void onStart()
											{
												// TODO Auto-generated method stub
												super.onStart();
												dialog = MessageDialog.createLoadingDialog(context, "正在执行...");
												dialog.show();
											}

											@Override
											public void onSuccess(Object t)
											{
												// TODO Auto-generated method stub
												super.onSuccess(t);
												dialog.dismiss();
//												Log.i("XU", "t.toS="+t.toString());
												if(t != null)
												{
													try
													{
														JSONObject jsonObject = new JSONObject(t.toString());
														String message;
														message = jsonObject.getString("message");
														if(message != null)
															ToastUtil.showStaticToastShort(context, message);
														
														if(gtypeService >= 281 && gtypeService <= 330)
														{
															int count = list.get(position).getCount();
															if(count-1 == 0)
																list.remove(position);
															else
																list.get(position).setCount(count - 1);
															notifyDataSetChanged();
														}
														else
														{
															list.remove(position);
															notifyDataSetChanged();
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
//								throwThing(position, gid);
							}
							else
							{
								Builder builder = new Builder(context);
								View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_number_edittext, null);
								etSelectNumber = (EditText) view.findViewById(R.id.etSelectNumber);
								builder.setView(view);
								builder.setNegativeButton("舍不得了", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										// TODO Auto-generated method stub
										
									}
								});
								
								builder.setPositiveButton("土豪扔吧", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										//String uuid,String gid,String number,String areaid)
										// TODO Auto-generated method stub
										long money = 0;
										if(gtype == 9 )
										{
											money = Long.valueOf(etSelectNumber.getText().toString()) * 1000000;
										}
										else if(gtype == 10)
										{
											money =  Long.valueOf(etSelectNumber.getText().toString()) * 1000;
										}
										else if(gtype == 11)
										{
											money = Long.valueOf(etSelectNumber.getText().toString());
										}
										//Log.i("XU", "丢弃铜板="+money);
										//http://wlgac420108.jsp.jspee.org/requestAction/throwmoney.action?&uuid=0040&gid=de5bd662d2c54915b6b518c8e09d717f&areaid=13&number=10000

//										Log.i("XU", "url = "+API.URL+"throwmoney.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid+"&areaid="+MyApplication.user.getAreaid()+"&number="+money);
										finalHttp.get(API.URL2+"throwmoney.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid+"&areaid="+MyApplication.user.getAreaid()+"&number="+money, new AjaxCallBack<Object>()
										{
											@Override
											public void onFailure(Throwable t, int errorNo, String strMsg)
											{
												// TODO Auto-generated method stub
												super.onFailure(t, errorNo, strMsg);
												Log.i("XU", "失败"+strMsg);
												ToastUtil.show(context, "操作失败:错误码"+errorNo);
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
															if(callBack != null)
																callBack.call();
															ToastUtil.show(context, message);
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
				}
			}
		}
	}
	
	private void throwThing(final int position,String gid)
	{

	}
	
	private static final class ViewHolder
	{
		MTextView txtGoodsName;
		MTextView txtDiscard;
		MTextView txtArmed;
		MTextView txtGoodsAttack;
		MTextView txtGoodsDefense;
		MTextView txtGoodsHP;
		LinearLayout layoutAttr;
	}
}
