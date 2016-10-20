package com.soul.project.story.activity;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

public class GoodDetailActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	TextView txtName;
	TextView txtGrade;
	TextView txtDesc;
	TextView txtAttack;
	TextView txtDefence;
	TextView txtNJZ;
	TextView txtHp;
	TextView txtStar;
	
	LinearLayout layout;
	
	Button btnReturn;
	Button btnPickUp;
	
	String name ;
	String desc;
	int grade;
	int attack;
	int defence;
	int hp;
	int type;
	int life;
	int star;
	String gid;
	
	int typeOfActivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gooddetail);
		
		Intent intent = getIntent();
		
		typeOfActivity = intent.getIntExtra("type", 1);
		
		if(typeOfActivity == 1)
		{
			name = intent.getStringExtra("name");
			desc = intent.getStringExtra("desc");
			grade = intent.getIntExtra("grade", 0);
		}
		else
		{
			name = intent.getStringExtra("name");
			desc = intent.getStringExtra("desc");
			grade = intent.getIntExtra("grade", 0);
			attack = intent.getIntExtra("attack", 0);
			defence = intent.getIntExtra("defence", 0);
			hp = intent.getIntExtra("hp", 0);
			life = intent.getIntExtra("life", 0);
			star = intent.getIntExtra("star", 0);
			if(typeOfActivity == 1023)
				gid = intent.getStringExtra("gid");
		}
		initview();
	}
	
	private void initview()
	{
		btnReturn = (Button)this.findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(this);

		if(typeOfActivity == 1023)
		{
			btnPickUp = (Button)this.findViewById(R.id.btnPickUp);
			btnPickUp.setVisibility(View.VISIBLE);
			btnPickUp.setOnClickListener(this);
		}
		
		txtStar = (TextView)this.findViewById(R.id.txtStar);
		txtName = (TextView)this.findViewById(R.id.txtName);
		txtGrade = (TextView)this.findViewById(R.id.txtGrade);
		txtDesc = (TextView)this.findViewById(R.id.txtDesc);
		txtAttack = (TextView)this.findViewById(R.id.txtAttack);
		txtDefence = (TextView)this.findViewById(R.id.txtDefence);
		txtNJZ = (TextView)this.findViewById(R.id.txtNJZ);
		txtHp = (TextView)this.findViewById(R.id.txtHp);
		layout = (LinearLayout)this.findViewById(R.id.layout);
		
		if(type == 1)
			layout.setVisibility(View.GONE);
		else
		{
			txtAttack.setText("攻击+"+attack);
			txtDefence.setText("防御+"+defence);
			txtHp.setText("血量+"+hp);
			txtNJZ.setText("[ 耐久值："+life+" ]");
			txtStar.setText("[ "+star+" 星 ]");
		}
		txtName.setText(name);
		txtDesc.setText(desc);
		
		if(grade != 0)
			txtGrade.setText(grade+"级");
		
	}

	@Override
	public void loadData(int id, int type)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v)
	{
		if(v == btnReturn)
		{
			finish();
		}
		else if(v == btnPickUp)
		{
			pickup();
		}
	}

	private void pickup()
	{
		if(gid != null)
		{
			finalHttp.get(API.URL2+"pickup.action?&uuid="+MyApplication.getUUID(this)+"&gid="+gid, new AjaxCallBack<Object>()
			{
				Dialog dialog;
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					dialog.dismiss();
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					dialog = MessageDialog.createLoadingDialog(GoodDetailActivity.this, "你试图捡起地上的["+name+"]");
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
							JSONObject jsonObject;
							jsonObject = new JSONObject(t.toString());
							String message = jsonObject.getString("message");
							if(message != null)
								ToastUtil.showStaticToastShort(GoodDetailActivity.this, message);
							else
								ToastUtil.showStaticToastShort(GoodDetailActivity.this, "系统繁忙!稍后重试!");
							finish();
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
		else
		{
			ToastUtil.show(GoodDetailActivity.this, "东东已经不存在，可能被别人捡走了");
		}		
	}

}
