package com.soul.project.story.activity;

import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.MessageListAdapter;
import com.soul.project.application.bean.message;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class MessageListAcrtivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	ListView messageList;
	MTextView txtPlaceName;
	Button btnLeft;
	Button btnRight;
	MessageListAdapter adapter;
	LinearLayout rootLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		loadData(0,0);
		initViews();
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if(rootLayout != null)
		{
			String isNight = ShareDB.getStringFromDB(this, "isopen_night");
			if("true".equals(isNight))
			{
				rootLayout.setBackgroundResource(R.color.black_light_title_text_color);
			}
			else
			{
				rootLayout.setBackgroundResource(R.color.gray_light);
			}
		}
	}
	
	private void initViews()
	{
		rootLayout = (LinearLayout)this.findViewById(R.id.rootLayout);
			messageList = (ListView)this.findViewById(R.id.listGoods);
			btnLeft = (Button)this.findViewById(R.id.btnOut);
			btnLeft.setOnClickListener(this);
			
			btnRight = (Button)this.findViewById(R.id.btnClean);
			btnRight.setOnClickListener(this);
			txtPlaceName = (MTextView)this.findViewById(R.id.txtPlaceName);
		setValue();
	}
	
	private void setValue()
	{
		txtPlaceName.setText("我的消息");
	}

	@Override
	public void loadData(int id,int type)
	{
//		list = getIntent().getParcelableArrayListExtra("list");
		
		// 把消息全都重置为已读
		finalHttp.get(API.MESSAGE_REQUEST+"getmessage.action?&uuid="+MyApplication.user.getUuid()+"&isread=all", new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onLoading(long count, long current)
			{
				// TODO Auto-generated method stub
				super.onLoading(count, current);
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
						Gson gson = new Gson();
						List<message> messages = gson.fromJson(t.toString(), new TypeToken<List<message>>(){}.getType());
						if(messages != null)
						{
							adapter = new MessageListAdapter(MessageListAcrtivity.this, messages);
							messageList.setAdapter(adapter);
						}
					}
					catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnOut)
		{
			finish();
		}
		else if(v.getId() == R.id.btnClean)
		{
			Builder builder = new Builder(MessageListAcrtivity.this,R.style.myDialogTheme);
			builder.setMessage("是否确认清空聊天记录？（清空后无法恢复）");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					// 把消息全部清空
					finalHttp.get(API.MESSAGE_REQUEST+"clearmessage.action?&uuid="+MyApplication.user.getUuid(), new AjaxCallBack<Object>()
					{
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current)
						{
							// TODO Auto-generated method stub
							super.onLoading(count, current);
							adapter.cleanList();
						}

						@Override
						public void onSuccess(Object t)
						{
							// TODO Auto-generated method stub
							super.onSuccess(t);

							if(t != null)
							{
								ToastUtil.show(MessageListAcrtivity.this, ""+t.toString(), ToastUtil.INFO);
							}
						}
					});
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					
				}
			});
			builder.show();
		}
	}
}
