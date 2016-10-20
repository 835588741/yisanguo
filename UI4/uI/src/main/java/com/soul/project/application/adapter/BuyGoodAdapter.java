package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.soul.project.application.bean.Goods;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

public class BuyGoodAdapter extends BaseAdapter
{
	Context context;
	List<Goods> list;
	LayoutInflater inflater;
	FinalHttp finalHttp;
	int type;
	
	public BuyGoodAdapter(Context context,List<Goods> list, int type)
	{
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.type = type;
	}
	
	public void setList(List<Goods> list)
	{
		this.list = list;
		notifyDataSetChanged();
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
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_buygoods_list, null);
			holder.btnBuy = (Button)convertView.findViewById(R.id.btnBuy);
			if(type==2)
				holder.btnBuy.setText("卖掉");
			holder.txtAttack = (TextView)convertView.findViewById(R.id.txtAttack);
			holder.txtDefence = (TextView)convertView.findViewById(R.id.txtDefence);
			holder.txtDodge = (TextView)convertView.findViewById(R.id.txtDodge);
			holder.txtGrade = (TextView)convertView.findViewById(R.id.txtGrade);
			holder.txtHp = (TextView)convertView.findViewById(R.id.txtHp);
			holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
			holder.txtPrice = (TextView)convertView.findViewById(R.id.txtPrice);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		Goods goods = list.get(position);
		
		holder.btnBuy.setOnClickListener(new Event(goods.getGid(),type,position));
		holder.txtAttack.setText("攻:+"+goods.getAddattack());
		holder.txtDefence.setText("防:+"+goods.getAdddefence());
		holder.txtDodge.setText("灵:+"+goods.getAdddexterous());
		holder.txtHp.setText("血:+"+goods.getAddhp());
		holder.txtPrice.setText(""+getMoneyDesc(goods.getPrice()-goods.getPrice()/10));
		holder.txtGrade.setText("等级:"+goods.getGgrade());
		holder.txtName.setText(""+goods.getName());
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		String gid;
		int type;
		int position;
		
		public Event(String gid,int type,int position)
		{
			this.gid = gid;
			this.type = type;
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			if(gid!=null && !"".equals(gid))
			{
				String url ;
				if(type == 1)
					url = API.URL2+"buything.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid;
				else
					url = API.URL2+"sell.action?&uuid="+MyApplication.getUUID(context)+"&gid="+gid;
					
				finalHttp.get(url, new AjaxCallBack<Object>()
				{
					Dialog dialog;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Log.i("XU", "code="+errorNo+"  msg="+strMsg);
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
						Log.i("XU", "ZZZZZZZZZZZZZZ");
						if(t != null)
						{
							Log.i("XU", "买卖结果 = "+t.toString());
							try
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								String message = jsonObject.getString("message");
								ToastUtil.show(context, ""+message);
								if(type==2)
								{
									if(position < list.size())
									{
										list.remove((position));
										notifyDataSetChanged();
									}
								}
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
	}
	
	
	private String getMoneyDesc(long money)
	{
		//moneyhuangjin*1000*1000+moneybaiying*1000+moneytongban
		if(money < 1000 && money>0 )
		{
			return ""+money+"个铜板";
		}
		else
		{
			long tongban = money % 1000;
			
			long baiying = money / 1000;
			
			if(baiying > 1000)
			{
				long huangjin = baiying / 1000;
				if(tongban > 0)
					return huangjin+"两黄金"+baiying+"两白银"+tongban+"个铜板";
				else
				{
					return huangjin+"两黄金"+baiying+"两白银";
				}
			}
			else
			{
				return ""+baiying+"两白银"+tongban+"个铜板";
			}
		}
	}


	private static final class ViewHolder
	{
		TextView txtGrade;
		TextView txtName;
		TextView txtPrice;
		TextView txtAttack;
		TextView txtDefence;
		TextView txtDodge;
		TextView txtHp;
		Button btnBuy;
	}
}
