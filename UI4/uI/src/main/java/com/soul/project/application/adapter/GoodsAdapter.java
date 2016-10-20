package com.soul.project.application.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soul.project.application.bean.PlaceBean.goods;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

public class GoodsAdapter extends BaseAdapter
{
	private Context context;
//	private IItemClickCallBack itemClickCallBack;
	private LayoutInflater inflater;
	private List<goods> list;
	int type;
	
	public GoodsAdapter(Context context,List<goods> list)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
//		this.itemClickCallBack = itemClickCallBack;
	}
	
	public void removeData(int position)
	{
		if(list != null)
			list.remove(position);
		notifyDataSetChanged();
	}
	
//	public void setNPCList(List<goods> list)
//	{
//		this.list = list;
//	}
//	
//	public void setPlayerList(List<goods> list)
//	{
//		this.listPlayer = list;
//	}
	
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
			convertView = inflater.inflate(R.layout.item_gridview_npc, null);
			holder.txtName = (MTextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		goods good = list.get(position);
		holder.txtName.setText(good.getName());
		
		return convertView;
	}
	
	private static final class ViewHolder
	{
		MTextView txtName;
	}
	
}

