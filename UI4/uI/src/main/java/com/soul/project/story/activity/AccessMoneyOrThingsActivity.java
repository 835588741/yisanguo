package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.AccessListAdapter;
import com.soul.project.application.bean.Goods;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class AccessMoneyOrThingsActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	ListView accessList;
	List<Goods> list = new ArrayList<Goods>();
	AccessListAdapter adapter;
	/** 1|存钱 2|取钱 3|存物 4|取物*/
	int type;
	Button btnLeft;
	Button btnRight;
	MTextView txtBarTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_access_money);
		type = getIntent().getIntExtra("type", 1);
		initView();
		
		loadData(0, type);
	}
	
	private void initView()
	{
		txtBarTitle = (MTextView)this.findViewById(R.id.txtBarTitle);
		accessList = (ListView)this.findViewById(R.id.accessList);
		btnLeft = (Button)this.findViewById(R.id.btnLeft);
		btnRight = (Button)this.findViewById(R.id.btnRight);
		
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
	}

	@Override
	public void loadData(int id, int type)
	{
		int state = 1;
		if(type == 3)
			state = 2;
		else if(type == 4)
			state = 5;
		
		finalHttp.get(API.URL2+"getgoods.action?&masterid="+MyApplication.getUUID(this)+"&state="+state, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "嘻嘻嘻="+strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}
//			19   24 - 11 = 13    29 34 - 11= 23 + 13

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t != null)
				{
					Log.i("XU", ""+t.toString());
					Gson gson = new Gson();
					list = gson.fromJson(t.toString(), new TypeToken<List<Goods>>(){}.getType());
					adapter = new AccessListAdapter(AccessMoneyOrThingsActivity.this, list, AccessMoneyOrThingsActivity.this.type);
					accessList.setAdapter(adapter);
					
					if(AccessMoneyOrThingsActivity.this.type == 3)
						txtBarTitle.setText("我的资产 : "+list.size() +"/20");
					else if(AccessMoneyOrThingsActivity.this.type == 4)
						txtBarTitle.setText("我的行囊 : "+list.size() +"/20");
				}
				else if("[]".equals(t.toString().trim()))
				{
					ToastUtil.show(AccessMoneyOrThingsActivity.this, "你没有寄存任何东西!");
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnRight:
			case R.id.btnLeft:
				finish();
				break;

			default:
				break;
		}
	}
}
