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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.soul.project.application.bean.MemberEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/**
 * @file MemberDataAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年8月4日 下午5:55:21
 */
public class MemberDataAdapter extends BaseAdapter
{
	private Context context;
	private List<MemberEntity> list;
	private LayoutInflater inflater ;
	FinalHttp finalHttp;
	String groupid;
	int mode = 1;
	boolean isManager;
	
	public MemberDataAdapter(Context context,List<MemberEntity> list, String groupid, int mode, boolean isManager)
	{
		this.isManager = isManager;
		this.mode = mode;
		this.groupid = groupid;
		this.finalHttp = new FinalHttp();
		this.finalHttp.configTimeout(8000);
		this.list = list;
		this.context = context;
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
			convertView = inflater.inflate(R.layout.item_member_list,null);
			holder.txtName = (MTextView)convertView.findViewById(R.id.txtName);
			holder.txtGrade = (MTextView)convertView.findViewById(R.id.txtGrade);
			holder.txtIsOnLine = (MTextView)convertView.findViewById(R.id.txtIsOnLine);
			holder.btnDeleteOut = (Button)convertView.findViewById(R.id.btnDeleteOut);
			holder.btnSendMessage = (Button)convertView.findViewById(R.id.btnSendMessage);
			holder.btnThrough = (Button)convertView.findViewById(R.id.btnThrough);
			holder.btnRefuse = (Button)convertView.findViewById(R.id.btnRefuse);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		MemberEntity entity = list.get(position);
		
		if(mode == 1)
		{
			holder.btnSendMessage.setVisibility(View.VISIBLE);
			holder.btnRefuse.setVisibility(View.GONE);
			holder.btnThrough.setVisibility(View.GONE);
			
			holder.btnSendMessage.setOnClickListener(new Event(position, 1,entity.getUuid(),null));
			
			if(isManager)
			{
				holder.btnDeleteOut.setVisibility(View.VISIBLE);
				holder.btnDeleteOut.setOnClickListener(new Event(position, 2, entity.getUuid(),null));
			}
			else 
				holder.btnDeleteOut.setVisibility(View.GONE);
		}
		else if(mode == 2)
		{
			holder.btnRefuse.setVisibility(View.VISIBLE);
			holder.btnThrough.setVisibility(View.VISIBLE);
			holder.btnSendMessage.setVisibility(View.GONE);
			holder.btnDeleteOut.setVisibility(View.GONE);
			
			holder.btnRefuse.setOnClickListener(new Event(position, 4, null, API.GROUP_REQUEST+"refusejoin.action?&applyer_uuid="+entity.getUuid()+"&groupid="+groupid));
			holder.btnThrough.setOnClickListener(new Event(position, 4, null, API.GROUP_REQUEST+"acceptjoin.action?&applyer_uuid="+entity.getUuid()+"&groupid="+groupid));
		}
		holder.txtIsOnLine.setText(("false".equals(entity.getIsonline())? "离线":"在线"));
		
		holder.txtName.setText(entity.getName());
		holder.txtGrade.setText(entity.getGrade()+"级");
		
		return convertView;
	}
	
	private class Event implements OnClickListener
	{
		private int position;
		private int type;
		private String uuid;
		private String url;
		
		public Event(int position,int type,String uuid,String url)
		{
			this.url = url;
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
				showIOSDialog();
//				sendMessage(uuid,etInput.getText().toString());
			}
			// 删除好友
			else if(type==2)
			{
				finalHttp.get(API.GROUP_REQUEST+"dismissed.action?bedismisser_uuid="+uuid+"&master_uuid="+MyApplication.getUUID(context)+"&groupid="+groupid,new AjaxCallBack<Object>()
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
								
								list.remove(position);
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
			else if(type == 4)
			{
				finalHttp.get(url, new AjaxCallBack<Object>()
				{

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
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
						
						try
						{
							list.remove(position);
							notifyDataSetChanged();
							
							JSONObject jsonObject = new JSONObject(t.toString());
							String mes;
							mes = jsonObject.getString("message");
							if(mes != null)
								ToastUtil.show(context, mes);
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
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
	private void showIOSDialog()
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
		
		btn1.setOnClickListener(new Event(0, 3, null,null));
		btn2.setOnClickListener(new Event(0, 3, null,null));
		
		etInput.setVisibility(View.VISIBLE);
		btn1.setText("发送消息");
		
		dialogIOS.show();
	}


	private static final class ViewHolder
	{
		MTextView txtName;
		MTextView txtGrade;
		MTextView txtIsOnLine;
		Button btnSendMessage;
		Button btnDeleteOut;
		Button btnThrough;
		Button btnRefuse;
	}
}
