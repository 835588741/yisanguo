package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soul.project.application.bean.Goods;
import com.soul.project.story.activity.R;

public class BlackMarketAdapter extends BaseAdapter
{


	Context context;
	List<Goods> list;
	LayoutInflater inflater;
	FinalHttp finalHttp;
	int type;
	
	public BlackMarketAdapter(Context context,List<Goods> list, int type)
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
			convertView = inflater.inflate(R.layout.item_black_buygoods_list, null);
//			holder.btnBuy = (Button)convertView.findViewById(R.id.btnBuy);
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
		
		//holder.btnBuy.setOnClickListener(new Event(goods.getGid(),type,position));
		holder.txtAttack.setText("攻:+"+goods.getAddattack());
		holder.txtDefence.setText("防:+"+goods.getAdddefence());
		holder.txtDodge.setText("灵:+"+goods.getAdddexterous());
		holder.txtHp.setText("血:+"+goods.getAddhp());
		holder.txtPrice.setText(""+goods.getPrice()+"两银");
		holder.txtGrade.setText(""+goods.getGgrade()+"级");
		holder.txtName.setText(""+goods.getName());
		
		return convertView;
	}
	
	
	private String getMoneyDesc(long money)
	{
//		1000000000
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
				if(tongban > 0)
					return ""+baiying+"两白银"+tongban+"个铜板";
				else
					return ""+baiying+"两白银";
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
//		Button btnBuy;
	}
}
