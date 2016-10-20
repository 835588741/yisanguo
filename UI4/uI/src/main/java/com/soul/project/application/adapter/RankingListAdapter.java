package com.soul.project.application.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soul.project.application.bean.PlaceBean;
import com.soul.project.application.bean.PlaceBean.player;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

public class RankingListAdapter extends BaseAdapter
{
	private Context context;
	private List<PlaceBean.player> list;
	private LayoutInflater inflater;
	MTextView txtNumber;
	MTextView txtName;
	MTextView txtTag;
	private int type;
	
	public RankingListAdapter(Context context,List<PlaceBean.player> list)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	public void setList(List<PlaceBean.player> list)
	{
		this.list = list;
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
		convertView = inflater.inflate(R.layout.item_ranking_list, null);
		txtName = (MTextView)convertView.findViewById(R.id.txtName);
		txtNumber = (MTextView)convertView.findViewById(R.id.txtNumber);
		txtTag = (MTextView)convertView.findViewById(R.id.txtTag);
		
		player p = list.get(position);
		
		if(type==2)
			txtName.setText(Html.fromHtml("[<font color='#D2691E'>"+p.getCountryname()+"</font> <font color='#FF0000'>"+p.getPosition()+"</font>] <font color='black'>"+p.getName()+"</font>"));
		else
			txtName.setText(Html.fromHtml("<font color='#D2691E'>"+p.getCountryname()+"</font> | <font color='black'>"+p.getName()+"</font>"));
		
		String des = "";
		if(position == 0)
		{
			switch (type)
			{
				case 1:des = "【武林至尊】";break;
				case 2:des = "【九五之尊】";break;
				case 3:des = "【万金之主】";break;
				default:
					break;
			}
		}
		
		else if(position == 1)
		{
			switch (type)
			{
				case 1:des = "【独步武林】";break;
				case 2:des = "【权倾天下】";break;
				case 3:des = "【富甲四海】";break;
				default:
					break;
			}
		}
		
		
		else if(position == 2)
		{
			switch (type)
			{
				case 1:des = "【侠行九州】";break;
				case 2:des = "【功勋昭彰】";break;
				case 3:des = "【腰缠万贯】";break;
				default:
					break;
			}
		}
		else
		{
			switch (type)
			{
				case 1:des = "【武林高手】";break;
				case 2:des = "【朝廷权臣】";break;
				case 3:des = "【巨商富贾】";break;
				default:
					break;
			}
		}
		
		if(position == 0 || position == 1 || position == 2)
			txtTag.setTextColor(context.getResources().getColor(R.color.red));
		else
			txtTag.setTextColor(context.getResources().getColor(R.color.green));
		
		txtTag.setText(des);
		txtNumber.setText("【第"+(position+1)+"名】");
		return convertView;
	}

	private static final class ViewHolder
	{
		MTextView txtNumber;
		MTextView txtName;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
