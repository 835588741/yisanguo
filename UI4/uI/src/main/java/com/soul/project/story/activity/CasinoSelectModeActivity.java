package com.soul.project.story.activity;

import com.soul.project.application.util.ActivityUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CasinoSelectModeActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	LinearLayout layoutSheLie;
	LinearLayout layoutShaiZi;
	LinearLayout layoutTianDiRen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_casino_select_mode);
		
		initViews();
		initEvent();
	}
	
	private void initEvent()
	{
		layoutSheLie.setOnClickListener(this);
		layoutShaiZi.setOnClickListener(this);
		layoutTianDiRen.setOnClickListener(this);
	}

	private void initViews()
	{
		layoutSheLie = (LinearLayout)this.findViewById(R.id.layoutSheLie);
		layoutShaiZi = (LinearLayout)this.findViewById(R.id.layoutShaiZi);
		layoutTianDiRen = (LinearLayout)this.findViewById(R.id.layoutTianDiRen);
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.layoutSheLie:
				ActivityUtil.goToNewActivity(this, SheLieActivity.class);
				break;
			case R.id.layoutShaiZi:
				ActivityUtil.goToNewActivity(this, YaShaiZiActivity.class);
				break;
			case R.id.layoutTianDiRen:
				ActivityUtil.goToNewActivity(this, TianDiRenList.class);
				break;
			default:
				break;
		}
	}
	
}
