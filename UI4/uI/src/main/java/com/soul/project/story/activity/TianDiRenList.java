package com.soul.project.story.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.TianDiRenListAdapter;
import com.soul.project.application.bean.HouseEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TianDiRenList extends BaseActivityWithSystemBarColor implements OnClickListener, OnItemClickListener
{
	ListView listView;
	Button btnReturnBack;
	List<HouseEntity> list;
	TianDiRenListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiandirenlist);
		
		listView = (ListView)this.findViewById(R.id.list);
		btnReturnBack= (Button)this.findViewById(R.id.btnReturnBack);
		btnReturnBack.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		loadData(1, 1);
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub
		finalHttp.get(API.URL_OTHER+"gethousemasterlist.action?", new AjaxCallBack<Object>()
		{	
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "loadData   "+strMsg);
				ToastUtil.showStaticToastShort(TianDiRenList.this, "数据加载失败:"+strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(TianDiRenList.this, "正在加载房主列表...");
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
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							String data = jsonObject.getString("data");
							Gson gson = new Gson();
							Type type = new TypeToken<List<HouseEntity>>() {}.getType();  
							list = gson.fromJson(data, type);
							
							adapter = new TianDiRenListAdapter(TianDiRenList.this, list);
							listView.setAdapter(adapter);
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
		if(v == btnReturnBack)
		{
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(this, TianDiRenPlay.class);
		intent.putExtra("hid", list.get(position).getHid());
		ActivityUtil.goToNewActivityWithComplement(this, intent);
	}

}
