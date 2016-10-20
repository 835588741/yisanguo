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
public class AccessListAdapter extends BaseAdapter
{
	private List<Goods> list;
	private LayoutInflater inflater;
	private Context context;
	FinalHttp finalHttp;
	EditText etSelectNumber;
	CallBack callBack;
	int type;
	
	/**
	 * @param context
	 * @param list
	 * @param type 1|存入  2|取出
	 */
	public AccessListAdapter(Context context,List<Goods> list,int type)
	{
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.type = type;
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
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
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
			holder.txtGoodsHP = (MTextView)convertView.findViewById(R.id.txtGoodsHP);
			holder.txtGoodsAttack = (MTextView)convertView.findViewById(R.id.txtGoodsAttack);
			holder.txtGoodsDefense= (MTextView)convertView.findViewById(R.id.txtGoodsDefense);
			holder.layoutAttr = (LinearLayout)convertView.findViewById(R.id.layoutAttr);
			holder.txtGoodsName = (MTextView)convertView.findViewById(R.id.txtGoodsName);
			holder.txtDiscard   = (MTextView)convertView.findViewById(R.id.txtDiscard);
			holder.txtDiscard.setVisibility(View.GONE);
			holder.txtArmed 	 = (MTextView)convertView.findViewById(R.id.txtArmed);
			if(type == 1 || type==3)
				holder.txtArmed.setText("存入");
			else if(type==2 || type==4)
				holder.txtArmed.setText("取出");
				
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Goods goods = list.get(position);
		
		int typeG = goods.getType();
		if(typeG != 9 && type != 10 && type != 11 && type != 8)
		{
			holder.layoutAttr.setVisibility(View.VISIBLE);
			holder.txtGoodsAttack.setText("攻+"+goods.getAddattack());
			holder.txtGoodsDefense.setText("防+"+goods.getAdddefence());
			holder.txtGoodsHP.setText("血+"+goods.getAddhp());
			holder.txtGoodsName.setText(goods.getName()+" "+goods.getGgrade()+"级");
		}
		else
		{
			holder.layoutAttr.setVisibility(View.GONE);
			holder.txtGoodsName.setText(goods.getName());
		}

		holder.txtArmed.setOnClickListener(new Event(goods.getGid(), position,type));
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		String gid;
		int position;
		int type;
		public Event(String gid,int position,int type)
		{
			this.type = type;
			this.position = position;
			this.gid = gid;
		}

		@Override
		public void onClick(View v)
		{
			String questUrl = null;
			
			if(type == 3)
			{
				questUrl = API.URL2+"deposit.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid+"&type=1";
			}
			else if(type == 4)
			{
				questUrl = API.URL2+"take.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid+"&type=1";
			}
			
					finalHttp.get(questUrl,new AjaxCallBack<Object>()
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
									String message;
									
									int code = jsonObject.getInt("code");
									if(code == 200)
									{
										list.remove(position);
										notifyDataSetChanged();
									}
									message = jsonObject.getString("message");
									if(message != null)
									{
										ToastUtil.show(context, message);
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

