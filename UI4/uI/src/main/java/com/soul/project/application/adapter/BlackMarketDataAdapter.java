package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.soul.project.application.bean.Goods;

public class BlackMarketDataAdapter extends BaseAdapter
{
	Context context;
	List<Goods> list;
	LayoutInflater inflater;
	FinalHttp finalHttp;
	
	public BlackMarketDataAdapter(Context context,List<Goods> list)
	{
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
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
		
		return null;
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
