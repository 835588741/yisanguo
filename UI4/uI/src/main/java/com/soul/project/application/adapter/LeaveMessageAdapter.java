/**
 * 
 */
package com.soul.project.application.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soul.project.application.bean.PlaceBean.leavelist;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

/**
 * @file MoveMessageAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年6月30日 上午10:32:31
 */
public class LeaveMessageAdapter extends BaseAdapter
{
	private List<leavelist> list;
	private LayoutInflater inflater;
	
	public LeaveMessageAdapter(Context context,List<leavelist> list)
	{
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_move_message_list, null);
			holder.txtText = (MTextView) convertView.findViewById(R.id.txtText);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.txtText.setText(Html.fromHtml(list.get(position).getMessage()));
		
		return convertView;
	}
	
	private static final class ViewHolder
	{
		MTextView txtText;
	}

}
