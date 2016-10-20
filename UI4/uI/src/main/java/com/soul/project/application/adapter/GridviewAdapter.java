package com.soul.project.application.adapter;

import java.util.List;

import com.soul.project.application.bean.PlaceBean;
import com.soul.project.application.bean.PlaceBean.npc;
import com.soul.project.application.bean.PlaceBean.player;
import com.soul.project.application.interfaces.IItemClickCallBack;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GridviewAdapter extends BaseAdapter
{
	private Context context;
//	private IItemClickCallBack itemClickCallBack;
	private LayoutInflater inflater;
	private List<player> list;
	private List<player> listPlayer;
	int type;
	
	public GridviewAdapter(Context context,int type)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.type = type;
//		this.itemClickCallBack = itemClickCallBack;
	}
	
	public void setNPCList(List<player> list)
	{
		this.list = list;
	}
	
	public void setPlayerList(List<player> list)
	{
		this.listPlayer = list;
	}
	
	@Override
	public int getCount()
	{
		if(type ==0)
			return list.size();
		else
			return listPlayer.size();
	}

	@Override
	public Object getItem(int position)
	{
		if(type == 0)
		return list.get(position);
		else
			return listPlayer.get(position);
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
		
		if(type == 0)
		{
			player npc = list.get(position);
			holder.txtName.setText(npc.getName());
		}
		else
		{
			player player = listPlayer.get(position);
			holder.txtName.setText(player.getName());
		}
		
		return convertView;
	}
	
	private static final class ViewHolder
	{
		MTextView txtName;
	}
	
}
