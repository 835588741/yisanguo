package com.soul.project.application.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
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
import android.widget.TextView;

import com.soul.project.application.bean.message;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

public class MessageListAdapter extends BaseAdapter
{
	private Context context;
	private List<message> list;
	private LayoutInflater inflater;
	protected FinalHttp finalHttp;
	
	public MessageListAdapter(Context context,List<message> list)
	{
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		finalHttp = new FinalHttp();
		// 网络超时 8秒
		finalHttp.configTimeout(8000);
	}
	
	public void cleanList()
	{
		if(list != null)
		{
			list.clear();
			notifyDataSetChanged();
		}
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
			convertView = (View)inflater.inflate(R.layout.item_messagelist, null);
			holder.btnReplay = (Button)convertView.findViewById(R.id.btnReplay);
			holder.txtMessage= (MTextView)convertView.findViewById(R.id.txtMessage);
			holder.txtMessageReplay = (MTextView)convertView.findViewById(R.id.txtMessageReplay);
			holder.txtSenderName = (MTextView)convertView.findViewById(R.id.txtSenderName);
			holder.txtSendTime = (MTextView)convertView.findViewById(R.id.txtSendTime);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		message m = list.get(position);
		
		if(m.getType() == 2)
		{
			holder.btnReplay.setVisibility(View.GONE);
			holder.txtMessageReplay.setVisibility(View.GONE);
		}
		else
		{
			holder.btnReplay.setVisibility(View.VISIBLE);
			
			if(m.getReplaymessage() != null && !"".equals(m.getReplaymessage().trim()))
			{
				holder.txtMessageReplay.setVisibility(View.VISIBLE);
				holder.txtMessageReplay.setText("我的回复:"+m.getReplaymessage());
				holder.btnReplay.setVisibility(View.GONE);
			}
			else
			{
				holder.txtMessageReplay.setVisibility(View.GONE);
				holder.btnReplay.setVisibility(View.VISIBLE);
			}
		}
		
		holder.txtMessage.setText(m.getMessage());
		holder.txtSenderName.setText(m.getSendername());
		holder.txtSendTime.setText(m.getSendtime());
		holder.btnReplay.setOnClickListener(new Event(1,m));
		
		return convertView;
	}
	
	private static final class ViewHolder
	{
		MTextView txtSenderName;
		MTextView txtSendTime;
		MTextView txtMessage;
		MTextView txtMessageReplay;
		Button btnReplay;
	}

	private class Event implements OnClickListener
	{
		message m;
		int type ;
		public Event(int type,message m)
		{
			this.m = m;
			this.type = type;
		}
		
		@Override
		public void onClick(View v)
		{
			if(type == 1)
			{
				showIOSDialog(2, m);
			}
			else
			{
				if(v.getId() == R.id.btn1)
				{
					if(etInput.getText() != null && etInput.getText().toString().length() > 0)
						sendMessage(m.getSenderid(),etInput.getText().toString(),m.getMid());
					else 
						ToastUtil.show(context, "还没输入消息内容呢", ToastUtil.WARN);
				}
				if(v.getId() == R.id.btn2)
				{
					if(dialogIOS != null)
						dialogIOS.dismiss();
				}
			}
		}
	}
	
	Dialog dialogIOS;
	private TextView txtDialogTitle;
	private TextView txtDialogMessage;
	private EditText etInput;
	private Button btn1;
	private Button btn2;
	int type;
	private void showIOSDialog(int type,message m)
	{
		this.type = type;
		dialogIOS = new Dialog(context, R.style.myDialogTheme);// 创建自定义样式dialog  
		LayoutInflater inflater = LayoutInflater.from(context);  
		View v = inflater.inflate(R.layout.ios_dialog, null);// 得到加载view  
		
		txtDialogMessage = (TextView) v.findViewById(R.id.txtDialogMessage);
		txtDialogTitle   = (TextView) v.findViewById(R.id.txtDialogTitle);
		etInput = (EditText)v.findViewById(R.id.etInput);
		btn1 = (Button)v.findViewById(R.id.btn1);// 现在回复
		btn2 = (Button)v.findViewById(R.id.btn2);// 稍后再看
		dialogIOS.setContentView(v);
		
		btn1.setOnClickListener(new Event(2,m));
		btn2.setOnClickListener(new Event(2,m));
		
		if(type == 2)
		{
			etInput.setVisibility(View.VISIBLE);
			btn1.setText("发送回复");
		}
		dialogIOS.show();
	}

	private void sendMessage(String uuid, String message,String mid)
	{
		etInput.setText("");
		if(dialogIOS != null)
			dialogIOS.dismiss();
		AjaxParams params = new AjaxParams();
		params.put("senderId", MyApplication.user.getUuid());
		params.put("receiveId", uuid);
		params.put("message", message);
		params.put("sendername", MyApplication.user.getName());
		params.put("isreplay", 2+"");
		params.put("mid", mid);
		
		finalHttp.post(API.MESSAGE_REQUEST+"sendmessage.action?", params, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
//				Log.i("XU", "发送消息失败");
				ToastUtil.show(context, "消息发送失败!code="+errorNo+"  msg="+strMsg, ToastUtil.INFO);
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
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						String message = jsonObject.getString("message");
						ToastUtil.show(context, ""+message, ToastUtil.INFO);
					}
					catch (Exception e)
					{
						// TODO: handle exception
						ToastUtil.show(context, "处理过程中发生异常!", ToastUtil.INFO);
					}
				}
			}
		});
	}
}
