package com.soul.project.application.adapter;

import java.util.List;

import com.soul.project.application.bean.HouseEntity;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TianDiRenListAdapter extends BaseAdapter
{
	private Context context;
	private List<HouseEntity> list;
	private LayoutInflater inflater;
	
	public TianDiRenListAdapter(Context context,List<HouseEntity> list)
	{
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.item_house_list, null);
			holder.txtHouseMaster = (MTextView) convertView.findViewById(R.id.txtHouseMaster);
			holder.txtHouseState = (MTextView) convertView.findViewById(R.id.txtHouseState);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		HouseEntity entity = list.get(position);
		holder.txtHouseMaster.setText("房主:"+entity.getName());
		holder.txtHouseState.setText("状态:"+entity.getState());
		return convertView;
	}
	
	private static final class ViewHolder 
	{	
		MTextView txtHouseMaster;
		MTextView txtHouseState;
	}
}
