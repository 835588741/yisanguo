/**
 * 
 */
package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.soul.project.application.bean.Friend;
import com.soul.project.application.bean.PersonDetailEntity.userdata;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/**
 * @file FriendListAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年7月15日 下午2:27:47
 */
public class FriendListAdapter extends BaseAdapter
{
	Context context;
	FinalHttp finalHttp;
	List<Friend> friends;
	LayoutInflater inflater;
	int usetype;
	
	public FriendListAdapter(Context context,List<Friend> friends, int type)
	{
		this.usetype = type;
		this.finalHttp = new FinalHttp();
		this.finalHttp.configTimeout(8000);
		this.context = context;
		this.friends = friends;
		this.inflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return friends.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return friends.get(position);
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
			convertView = inflater.inflate(R.layout.item_friend_list, null);
			holder.btnDeleteFriend = (Button) convertView.findViewById(R.id.btnDeleteFriend);
			holder.btnSendMessage  = (Button)convertView.findViewById(R.id.btnSendMessage);
			holder.txtFname = (MTextView)convertView.findViewById(R.id.txtFname);
			holder.txtFstate= (MTextView)convertView.findViewById(R.id.txtFstate);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Friend friend = friends.get(position);
		holder.txtFname.setText(friend.getFname());
		holder.txtFstate.setText((friend.getState() == 2 ? "离线":"在线"));
		
		if(usetype==2)
		{
			holder.txtFstate.setText("未知");
			holder.btnDeleteFriend.setText("移出黑名单");
		}
		holder.btnDeleteFriend.setOnClickListener(new Event(position, 2, friend.getFuuid()));
		holder.btnSendMessage.setOnClickListener(new Event(position, 1, friend.getFuuid()));
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		private int position;
		private int type;
		private String uuid;
		
		public Event(int position,int type,String uuid)
		{
			this.position = position;
			this.type = type;
			this.uuid = uuid;
		}
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			// 发消息
			if(type==1)
			{
				showIOSDialog(uuid);
//				sendMessage(uuid,etInput.getText().toString());
			}
			// 删除好友
			else if(type==2)
			{
				String url;
				if(usetype == 1)
				{
					url = API.URL+"deletefriend.action?&fuuid="+uuid+"&buuid="+MyApplication.getUUID(context);
				}
				else
				{
					url = API.MESSAGE_REQUEST+"deletefromblack.action?&targetuuid="+uuid+"&uuid="+MyApplication.getUUID(context);
				}
				finalHttp.get(url,new AjaxCallBack<Object>()
				{
					Dialog dialog;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						dialog.dismiss();
						ToastUtil.show(context, "删除失败，请稍后重试!");
					}

					@Override
					public void onStart()
					{
						// TODO Auto-generated method stub
						super.onStart();
						dialog = MessageDialog.createLoadingDialog(context, "删除中...");
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
								String message;
								message = jsonObject.getString("message");
								
								friends.remove(position);
								notifyDataSetChanged();
								
								if(message != null)
									ToastUtil.show(context, message);
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} );
			}
			else if(type==3)
			{
				if(v.getId() == R.id.btn1)
				{
					sendMessage(uuid,etInput.getText().toString());
				}
				else
				{
					if(dialogIOS != null)
						dialogIOS.dismiss();
				}
			}
		}
	}
	
	private void sendMessage(String uuid, final String message)
	{
		etInput.setText("");
		if(dialogIOS != null)
			dialogIOS.dismiss();
		AjaxParams params = new AjaxParams();
		params.put("senderId", MyApplication.user.getUuid());
		params.put("receiveId", uuid);
		params.put("message", message);
		params.put("sendername", MyApplication.user.getName());
		params.put("isreplay", 1+"");
		params.put("mid", "");
		
		finalHttp.post(API.MESSAGE_REQUEST+"sendmessage.action?", params, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				ToastUtil.show(context, "发送消息失败:"+strMsg, ToastUtil.ERROR);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				ToastUtil.show(context, "成功向ta发送消息:"+message, ToastUtil.INFO);
			}
		});
	}
	
	Dialog dialogIOS;
	private EditText etInput;
	private Button btn1;
	private Button btn2;
	private void showIOSDialog(String uuid)
	{
//		this.type = type;
		dialogIOS = new Dialog(context, R.style.myDialogTheme);// 创建自定义样式dialog  
		LayoutInflater inflater = LayoutInflater.from(context);  
		View v = inflater.inflate(R.layout.ios_dialog, null);// 得到加载view  
		
//		txtDialogMessage = (TextView) v.findViewById(R.id.txtDialogMessage);
//		txtDialogTitle   = (TextView) v.findViewById(R.id.txtDialogTitle);
		etInput = (EditText)v.findViewById(R.id.etInput);
		btn1 = (Button)v.findViewById(R.id.btn1);// 现在回复
		btn2 = (Button)v.findViewById(R.id.btn2);// 稍后再看
		dialogIOS.setContentView(v);
		
		btn1.setOnClickListener(new Event(0, 3, uuid));
		btn2.setOnClickListener(new Event(0, 3, uuid));
		
		etInput.setVisibility(View.VISIBLE);
		btn1.setText("发送消息");
		
		dialogIOS.show();
	}

	private static final class ViewHolder
	{
		Button btnSendMessage;
		Button btnDeleteFriend;
		MTextView txtFstate;
		MTextView txtFname;
	}
}
