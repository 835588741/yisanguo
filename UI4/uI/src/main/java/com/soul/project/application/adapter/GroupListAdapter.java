/**
 * 
 */
package com.soul.project.application.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soul.project.application.bean.GroupEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.GroupListActivity;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/** 部曲列表数据适配器
 * @file GroupListAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年8月1日 下午8:05:02
 */
public class GroupListAdapter extends BaseAdapter
{
	Context context;
	List<GroupEntity> list;
	LayoutInflater inflater;
	FinalHttp finalHttp;
	
	public GroupListAdapter(Context context,List<GroupEntity> list)
	{
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.context = context;
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
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_group_list, null);
			holder.txtJoin   = (MTextView) convertView.findViewById(R.id.txtJoin);
			holder.txtMaster = (MTextView) convertView.findViewById(R.id.txtMaster);
			holder.txtMaster2= (MTextView) convertView.findViewById(R.id.txtMaster2);
			holder.txtGrade  = (MTextView) convertView.findViewById(R.id.txtGrade);
			holder.txtMember = (MTextView) convertView.findViewById(R.id.txtMember);
			holder.txtDesc   = (MTextView) convertView.findViewById(R.id.txtDesc);
			holder.txtName   = (MTextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		GroupEntity entity = list.get(position);
		holder.txtJoin.setOnClickListener(new Event(entity.getGroupid()));
		holder.txtDesc.setText(entity.getGroup_desc());
		holder.txtGrade.setText(entity.getGroup_grade());
		holder.txtMaster.setText(entity.getGroup_master());
		holder.txtMaster2.setText(entity.getGroup_master2());
		holder.txtMember.setText(entity.getGroup_count());
		holder.txtName.setText(entity.getGroup_name());
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		String groupid;
		
		public Event (String groupid)
		{
			this.groupid = groupid;
		}

		@Override
		public void onClick(View v)
		{
			finalHttp.get(API.GROUP_REQUEST+"join.action?&uuid="+MyApplication.getUUID(context)+"&groupid="+groupid, new AjaxCallBack<Object>()
			{
				Dialog dialog;
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
					ToastUtil.show(context, "申请提交失败");
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					dialog = MessageDialog.createLoadingDialog(context, "提交中...");
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
							String mess = jsonObject.getString("message");
							if(mess != null)
								ToastUtil.show(context, mess);
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
		MTextView txtMaster;
		MTextView txtMaster2;
		MTextView txtGrade;
		MTextView txtMember;
		MTextView txtDesc;
		MTextView txtName;
		MTextView txtJoin;
	}

}
