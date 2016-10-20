package com.soul.project.application.adapter;

import java.util.List;

import com.soul.project.application.bean.PostEntity;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PostListAdapter extends BaseAdapter
{
	Context context;
	List<PostEntity> list;
	LayoutInflater inflater;
	
	public PostListAdapter(Context context,List<PostEntity> list)
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
			convertView = inflater.inflate(R.layout.item_postlist,null);
			holder.txtDescript = (MTextView)convertView.findViewById(R.id.txtDescript);
			holder.txtPosterName = (MTextView)convertView.findViewById(R.id.txtPosterName);
			holder.txtPubTime = (MTextView)convertView.findViewById(R.id.txtPubTime);
			holder.txtTitle = (MTextView)convertView.findViewById(R.id.txtTitle);
			holder.txtTagBoutiquePost = (MTextView)convertView.findViewById(R.id.txtTagBoutiquePost);
			holder.txtTagFirstPost = (MTextView)convertView.findViewById(R.id.txtTagFirstPost);
			holder.txtInfoTag = (MTextView)convertView.findViewById(R.id.txtInfoTag);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		PostEntity entity = list.get(position);
		
		// 置顶标志
		if("true".equals(entity.getIsfirst()))
			holder.txtTagFirstPost.setVisibility(View.VISIBLE);
		else
			holder.txtTagFirstPost.setVisibility(View.GONE);
			
		// 精华标志
		if("true".equals(entity.getIsboutique()))
			holder.txtTagBoutiquePost.setVisibility(View.VISIBLE);
		else
			holder.txtTagBoutiquePost.setVisibility(View.GONE);
		
		holder.txtDescript.setText(entity.getContent());
		holder.txtPosterName.setText(entity.getPostername());
		holder.txtPubTime.setText(entity.getPubtime());
		holder.txtTitle.setText(entity.getTitle());
		holder.txtInfoTag.setText(entity.getCommentnum()+" 回   "+entity.getReadnum()+" 阅");
		return convertView;
	}

	private static final class ViewHolder
	{
		MTextView txtTitle;
		MTextView txtDescript;
		MTextView txtPubTime;
		MTextView txtPosterName;
		MTextView txtTagFirstPost;
		MTextView txtTagBoutiquePost;
		MTextView txtInfoTag;
	}
}
