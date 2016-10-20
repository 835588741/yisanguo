package com.soul.project.story.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soul.project.application.bean.PersonDetailEntity;
import com.soul.project.application.bean.PersonDetailEntity.goods;
import com.soul.project.application.bean.PersonDetailEntity.userdata;
import com.soul.project.application.dialog.DialogUtil;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class PlayerPanelActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	int tempValue = 120;
	String descript;
	String name;
	String dialog;
	String uuid;
	String country;
	String position;
	String sign;
	String chenghaodescript;
	String chenghao;
	String groupname;
	int type;
	int hp;
	int maxhp;
	int grade;
	long exp;
	long positionexp;
	long nextgradeexp;
	long nextpositionexp;
	int gender;
	int placeType;
	int attack;
	int defence;
	int dodge;
	int personType;
	int positiongrade;
	
	MTextView txtCurrentExp;
	MTextView txtNextExp;
//	MTextView txtUpGrade;
	
	MTextView txtGrade;
	MTextView txtDescript;
	MTextView txtPosition;
	MTextView txtName;
	
	MTextView txtKill;
	MTextView txtTalk;
	MTextView txtTransaction;
	MTextView txtGoOut;
	
	MTextView txtCountry;
	MTextView txtCurrentPositionExp;
	MTextView txtNextPositionExp;
	MTextView txtWuQi;
	MTextView txtTouKuai;
	MTextView txtPiFeng;
	MTextView txtKuiJia;
	MTextView txtYaoDai;
	MTextView txtXieZi;
	MTextView txtJieZi;
	MTextView txtHPDescript;
	MTextView txtHP;
	MTextView txtAttack;
	MTextView txtDefence;
	MTextView txtDodge;
	MTextView txtSign;
	MTextView txtGroup;
	MTextView txtAddFriend;
	MTextView txtChenghao;
	MTextView txtForum;
	MTextView txtChenghaoDescript;
	MTextView txtGroupName;
	
	MTextView btnKuiJiaNJ;
	MTextView btnYaoDiaNJ;
	MTextView btnXieZiNJ;
	MTextView btnWuqiNJ;
	MTextView btnTouKuaiNJ;
	MTextView btnPiFengNJ;
	
	Button btnForm;
	Button btnBack;
	Button btnGroup;
	Button btnHelp;
	
	com.soul.project.application.view.MTextView btnDisWuqi;
	com.soul.project.application.view.MTextView btnDisTouKui;
	com.soul.project.application.view.MTextView btnDisPiFeng;
	com.soul.project.application.view.MTextView btnDisKaiJia;
	com.soul.project.application.view.MTextView btnDisYaoDai;
	com.soul.project.application.view.MTextView btnDisXieZi;
	com.soul.project.application.view.MTextView btnDisJieZi;
	
	String[] gidarray = new String[7];
	
//	50 60 100    g*15  g*12  g*30  800  660  1600   50+  200+  400+  
	
//	50 60 100    g*16  g*13  g*31  850  710  1650
	
	PersonDetailEntity datalEntity;
	Toast toast;
	ToastUtil toastUtil;
	DialogUtil dialogUtil;
	LinearLayout layoutUpGrade;
	LinearLayout layoutUpPosition;
	LinearLayout layoutPersonInfo;
	LinearLayout layoutLoadding;
	LinearLayout layoutFunction3;
	LinearLayout layoutWuqi;
	LinearLayout layoutTouKui;
	LinearLayout layoutPiFeng;
	LinearLayout layoutKaiJia;
	LinearLayout layoutYaoDai;
	LinearLayout layoutXieZi;
	LinearLayout layoutJieZi;
	LinearLayout layoutChenghao;
	LinearLayout loading;
	LinearLayout layoutGroup;
	
	int code ;
	String messgae;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_player_panel);
		
		
		Intent intent = getIntent();
		uuid = intent.getStringExtra("id");
		type = intent.getIntExtra("type", 0);
		placeType = intent.getIntExtra("placeType", 0);
		personType = intent.getIntExtra("personType", 0);
		
		loadData(0,0);
		
		if("1021".equals(uuid))
		{
			loadDailyTask();
		}
		
			
			// 任务人物
		if(personType == 3)
		{
			loadtask();
		}
		initViews();
		initEvents();
	}
	
	private void loadDailyTask()
	{
		finalHttp.get(API.URL+"getdailytaskstate.action?&taskreceiveruuid="+MyApplication.getUUID(this)+"&type=2", new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("XU", "出现错误"+strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "加载数据中~~~~~~");
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "执行完成"+t.toString());
				if(t != null)
				{
					try
					{
						JSONObject jsonObject;
						jsonObject = new JSONObject(t.toString());
						String mess = jsonObject.getString("message");
						showTaskNpcDialog(1024, mess);
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

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if(loading != null)
		{
			String isNight = ShareDB.getStringFromDB(this, "isopen_night");
			if("true".equals(isNight))
			{
				loading.setBackgroundResource(R.color.black_light_title_text_color);
			}
			else
			{
				loading.setBackgroundResource(R.color.gray_light);
			}
		}
		
		if(personType == 2 || personType == 3)
		{
			txtGroup.setVisibility(View.GONE);
			txtTransaction.setVisibility(View.GONE);
			txtAddFriend.setVisibility(View.GONE);
		}
	}
	
	private void loadtask()
	{
		finalHttp.get(API.URL+"taskcheck.action?&taskmasteruuid="+uuid+"&taskreceiveruuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "加载数据中~~~~~~");
				dialog.setCancelable(false);
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
						//Log.i("Test", "t = "+t.toString());
						JSONObject jsonObject = new JSONObject(t.toString());
						code = jsonObject.getInt("code");
						messgae = jsonObject.getString("message");
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

	private void initEvents()
	{
		txtKill.setOnClickListener(this);
		txtTalk.setOnClickListener(this);
		txtTransaction.setOnClickListener(this);
		txtGoOut.setOnClickListener(this);
		txtGroup.setOnClickListener(this);
//		txtUpGrade.setOnClickListener(this);
		txtAddFriend.setOnClickListener(this);
		
		btnDisWuqi.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisWuqi);
		btnDisTouKui.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisTouKui);
		btnDisPiFeng.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisPiFeng);
		btnDisKaiJia.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisKaiJia);
		btnDisYaoDai.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisYaoDai);
		btnDisXieZi.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisXieZi);
		btnDisJieZi.setOnClickListener(this);// = (Button)this.findViewById(R.id.btnDisJieZi);
		
		btnBack.setOnClickListener(this);
		btnForm.setOnClickListener(this);
		btnGroup.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
	}

	private void initValues()
	{
		layoutUpGrade.setVisibility(View.GONE);
		layoutChenghao.setVisibility(View.GONE);
		toastUtil = new ToastUtil(this);
		
		int span = maxhp / 5;
		
		if(country != null)
			txtCountry.setText("【"+country+"】");
		
		if(position != null)
			txtPosition.setText("【"+position+"】");
		
		if(chenghao != null && !"".equals(chenghao.trim()))
		{
			layoutChenghao.setVisibility(View.VISIBLE);
			txtChenghao.setText("【"+chenghao+"】");
			txtChenghaoDescript.setText("** "+chenghaodescript+" **");
		}
		
		if(groupname != null)
		{
			layoutGroup.setVisibility(View.VISIBLE);
			txtGroupName.setText(groupname);
		}
		else
		{
			layoutGroup.setVisibility(View.GONE);
		}
		
		// 是本人查看自己
		if(MyApplication.getUUID(this).equals(uuid))
		{
			layoutFunction3.setVisibility(View.VISIBLE);
			layoutPersonInfo.setVisibility(View.VISIBLE);
			txtSign.setVisibility(View.VISIBLE);
			txtSign.setOnClickListener(this);
			
			if(sign != null)
			{
				txtSign.setText(sign);
			}
			
			btnDisWuqi.setVisibility(View.VISIBLE); 
			btnDisTouKui.setVisibility(View.VISIBLE); 
			btnDisPiFeng.setVisibility(View.VISIBLE);
			btnDisKaiJia.setVisibility(View.VISIBLE); 
			btnDisYaoDai.setVisibility(View.VISIBLE); 
			btnDisXieZi.setVisibility(View.VISIBLE);
			btnDisJieZi.setVisibility(View.VISIBLE); 
			
			layoutUpPosition.setVisibility(View.VISIBLE);
			layoutUpGrade.setVisibility(View.VISIBLE);
			
			txtCurrentPositionExp.setText("当前功勋:"+positionexp);
			txtNextPositionExp.setText("下级功勋:"+nextpositionexp);//((positiongrade+1)*50));
			txtCurrentExp.setText("当前经验:"+exp);
			txtNextExp.setText("下级经验:"+nextgradeexp);//+((grade+1)*tempValue));
			txtGrade.setText("等级:"+grade);
			txtKill.setVisibility(View.GONE);
			txtTalk.setVisibility(View.GONE);
			txtTransaction.setVisibility(View.GONE);
			txtGroup.setVisibility(View.GONE);
			txtAddFriend.setVisibility(View.GONE);
			txtHP.setText("血量:"+hp);
			txtAttack.setText("攻击:"+attack);
			txtDefence.setText("防御:"+defence);
			txtDodge.setText("灵巧:"+dodge);
			
			if(hp > 0 && hp < span)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.red));
				txtHPDescript.setText("你如风中残烛，危在旦夕，似乎一阵风也能要了你的命。");
			}
			else if( hp >= span && hp<span * 2)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.red));
				txtHPDescript.setText("你身受重伤，创痕累累，身上满是血迹。");
			}
			else if(hp >= span*2 && hp < span * 3)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.pink));
				txtHPDescript.setText("你受了点轻伤，正气喘吁吁的。");
			}
			else if(hp >= span * 3 && hp < span * 4)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.greenyellow));
				txtHPDescript.setText("你的手脚似乎有点不灵巧。");
			}
			else if(hp >= span * 4)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.green));
				txtHPDescript.setText("你气血充盈，神采奕奕，活力无限。");
			}
			else 
			{
				txtHPDescript.setText("");
			}
		}
		else
		{
			layoutFunction3.setVisibility(View.GONE);
			layoutPersonInfo.setVisibility(View.GONE);
			
			btnDisWuqi.setVisibility(View.GONE); 
			btnDisTouKui.setVisibility(View.GONE); 
			btnDisPiFeng.setVisibility(View.GONE);
			btnDisKaiJia.setVisibility(View.GONE); 
			btnDisYaoDai.setVisibility(View.GONE); 
			btnDisXieZi.setVisibility(View.GONE);
			btnDisJieZi.setVisibility(View.GONE); 
			
			if(type == 1)
			{
				txtSign.setVisibility(View.VISIBLE);
				txtSign.setText(sign);
			}
			
			txtGrade.setText("等级:"+(grade >= (MyApplication.user.getGrade()+10) ? "???":grade));
			
			if(grade < (MyApplication.user.getGrade()+10))
			{
				txtHP.setText("血量:"+hp);
				txtAttack.setText("攻击:"+attack);
				txtDefence.setText("防御:"+defence);
				txtDodge.setText("灵巧:"+dodge);
			}
			
			txtKill.setVisibility(View.VISIBLE);
			txtTalk.setVisibility(View.VISIBLE);
			if(personType==2 || personType == 3)
				txtTransaction.setVisibility(View.GONE);
			else
				txtTransaction.setVisibility(View.VISIBLE);
				
			if(gender == 1)
				descript = descript.replaceAll("你", "他");
			else 
				descript = descript.replaceAll("你", "她");
			
			if(hp > 0 && hp < span)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.red));
				
				String temp1 =  "他已如风中残烛，危在旦夕，似乎一阵风也能要了他的命。";
				if(gender == 2)
					temp1 = temp1.replaceAll("他", "她");
				
				txtHPDescript.setText(temp1);
			}
			else if( hp >= span && hp < span * 2)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.red));
				String temp1 =  "他已是身受重伤，创痕累累，身上满是血迹。";
				if(gender == 2)
					temp1 = temp1.replaceAll("他", "她");
				txtHPDescript.setText(temp1);
			}
			else if(hp >= span*2 && hp < span * 3)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.pink));
				String temp1 =  "他受了点伤，正气喘吁吁的";
				if(gender == 2)
					temp1 = temp1.replaceAll("他", "她");
				txtHPDescript.setText(temp1);
			}
			else if(hp >= span * 3 && hp < span * 4)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.greenyellow));
				String temp1 =  "他似乎是受了点轻伤，手脚有点不太灵光。";
				if(gender == 2)
					temp1 = temp1.replaceAll("他", "她");
				txtHPDescript.setText(temp1);
			}
			else if(hp >= span * 4)
			{
				txtHPDescript.setTextColor(getResources().getColor(R.color.green));
				String temp1 =  "他气血充盈，神采奕奕，活力无限。";
				if(gender == 2)
					temp1 = temp1.replaceAll("他", "她");
				txtHPDescript.setText(temp1);
			}
			else 
			{
				txtHPDescript.setText("");
			}
		}
		txtDescript.setText(descript);
		txtName.setText(name);
	}

	private void initViews()
	{
		btnGroup = (Button)this.findViewById(R.id.btnGroup);
		btnForm = (Button)this.findViewById(R.id.btnForm);
		btnBack = (Button)this.findViewById(R.id.btnGoBack);
		layoutChenghao = (LinearLayout)this.findViewById(R.id.layoutChenghao);
		txtChenghao = (MTextView)this.findViewById(R.id.txtChenghao);
		txtChenghaoDescript = (MTextView)this.findViewById(R.id.txtChenghaoDescript);
		
		btnDisWuqi = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisWuqi);
		btnDisTouKui = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisTouKui);
		btnDisPiFeng = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisPiFeng);
		btnDisKaiJia = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisKaiJia);
		btnDisYaoDai = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisYaoDai);
		btnDisXieZi = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisXieZi);
		btnDisJieZi = (com.soul.project.application.view.MTextView)this.findViewById(R.id.btnDisJieZi);
		
		layoutWuqi = (LinearLayout)this.findViewById(R.id.layoutWuqi);
		layoutTouKui = (LinearLayout)this.findViewById(R.id.layoutTouKui);
		layoutPiFeng = (LinearLayout)this.findViewById(R.id.layoutPiFeng);
		layoutKaiJia = (LinearLayout)this.findViewById(R.id.layoutKaiJia);
		layoutYaoDai = (LinearLayout)this.findViewById(R.id.layoutYaoDai);
		layoutXieZi = (LinearLayout)this.findViewById(R.id.layoutXieZi);
		layoutJieZi = (LinearLayout)this.findViewById(R.id.layoutJieZi);
		
		txtGroupName = (MTextView)this.findViewById(R.id.txtGroupName);
		layoutGroup = (LinearLayout)this.findViewById(R.id.layoutGroup);
		loading = (LinearLayout)this.findViewById(R.id.loading);
		layoutFunction3 = (LinearLayout)this.findViewById(R.id.layoutFunction3);
		layoutLoadding = (LinearLayout)this.findViewById(R.id.loading);
		layoutPersonInfo = (LinearLayout)this.findViewById(R.id.layoutPersonInfo);
		layoutUpPosition = (LinearLayout)this.findViewById(R.id.layoutUpPosition);
		layoutUpGrade = (LinearLayout)this.findViewById(R.id.layoutUpGrade);
		txtCurrentExp = (MTextView)this.findViewById(R.id.txtCurrentExp);
		txtNextExp    = (MTextView)this.findViewById(R.id.txtNextExp);
//		txtUpGrade    = (MTextView)this.findViewById(R.id.txtUpGrade);
		txtHPDescript = (MTextView)this.findViewById(R.id.txtHPDescript);
		txtDescript = (MTextView)this.findViewById(R.id.txtDescript);
		txtGrade = (MTextView)this.findViewById(R.id.txtGrade);
		txtName = (MTextView)this.findViewById(R.id.txtName);
		txtPosition = (MTextView)this.findViewById(R.id.txtPosition);
		
//		MTextView btnKuiJiaNJ;
//		MTextView btnYaoDiaNJ;
//		MTextView btnXieZiNJ;
//		MTextView btnWuqiNJ;
//		MTextView btnTouKuaiNJ;
//		MTextView btnPiFengNJ;
		btnHelp = (Button)this.findViewById(R.id.btnHelp);
		btnKuiJiaNJ = (MTextView)this.findViewById(R.id.btnKuiJiaNJ);
		btnYaoDiaNJ = (MTextView)this.findViewById(R.id.btnYaoDiaNJ);
		btnXieZiNJ = (MTextView)this.findViewById(R.id.btnXieZiNJ);
		btnWuqiNJ = (MTextView)this.findViewById(R.id.btnWuqiNJ);
		btnTouKuaiNJ = (MTextView)this.findViewById(R.id.btnTouKuaiNJ);
		btnPiFengNJ = (MTextView)this.findViewById(R.id.btnPiFengNJ);
		
		txtGroup = (MTextView)this.findViewById(R.id.txtGroup);
		txtAddFriend = (MTextView)this.findViewById(R.id.txtAddFriend);
		
		txtSign = (MTextView)this.findViewById(R.id.txtSign);
		txtHP = (MTextView)this.findViewById(R.id.txtHP);
		txtAttack = (MTextView)this.findViewById(R.id.txtAttack);
		txtDefence = (MTextView)this.findViewById(R.id.txtDefence);
		txtDodge = (MTextView)this.findViewById(R.id.txtDodge);
		
		txtNextPositionExp = (MTextView)this.findViewById(R.id.txtNextPositionExp);
		txtCurrentPositionExp = (MTextView)this.findViewById(R.id.txtCurrentPositionExp);
		txtCountry = (MTextView)this.findViewById(R.id.txtCountry);
		txtKill  = (MTextView)this.findViewById(R.id.txtKill);
		
		if(type == 3)
			txtKill.setVisibility(View.GONE);
		
		txtTalk  = (MTextView)this.findViewById(R.id.txtTalk);
		txtGoOut = (MTextView)this.findViewById(R.id.txtGoOut);
		txtTransaction = (MTextView)this.findViewById(R.id.txtTransaction);
		
		txtWuQi = (MTextView)this.findViewById(R.id.txtWuQi);
		txtTouKuai = (MTextView)this.findViewById(R.id.txtTouKuai);
		txtPiFeng = (MTextView)this.findViewById(R.id.txtPiFeng);
		txtKuiJia = (MTextView)this.findViewById(R.id.txtKuiJia);
		txtYaoDai = (MTextView)this.findViewById(R.id.txtYaoDai);
		txtXieZi  = (MTextView)this.findViewById(R.id.txtXieZi);
		txtJieZi  = (MTextView)this.findViewById(R.id.txtJieZi);
	}

	@Override
	public void loadData(int id,int type)
	{
		finalHttp.get(API.URL+"getuserinfo.action?&uuid="+uuid, new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "Loading ...");
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
					Gson gson = new Gson();
					datalEntity = gson.fromJson(t.toString(), PersonDetailEntity.class);
					
					if(datalEntity != null)
					initViewsValue();
				}
			}
		});
	}
	

	private void initViewsValue()
	{
		if(personType == 2 || personType == 3)
		{
			int attackerGrade = datalEntity.getUserdata().getGrade();
			layoutWuqi.setVisibility(View.VISIBLE);
			if(attackerGrade <= 5)
			{
				setTVValue(txtWuQi,"【手持】木长刀");
			}
			else if(attackerGrade > 5 && attackerGrade <= 10)
			{
				setTVValue(txtWuQi,"【手持】铜长刀");
			}
			else if(attackerGrade > 10 && attackerGrade <= 15)
			{
				setTVValue(txtWuQi,"【手持】月牙刀");
			}
			else if(attackerGrade > 15 && attackerGrade <= 20)
			{
				setTVValue(txtWuQi,"【手持】长杆刀");
			}
			else if(attackerGrade > 20 && attackerGrade <= 25)
			{
				setTVValue(txtWuQi,"【手持】赤铜长刀");
			}
			else if(attackerGrade > 25 && attackerGrade <= 45)
			{
				setTVValue(txtWuQi,"【手持】黄铜长刀");
			}
			else if(attackerGrade > 45 && attackerGrade <= 60)
			{
				setTVValue(txtWuQi,"【手持】折铁刀");
			}
			else if(attackerGrade > 60 && attackerGrade <= 75)
			{
				setTVValue(txtWuQi,"【手持】横刀");
			}
			else if(attackerGrade > 75 && attackerGrade <= 90)
			{
				setTVValue(txtWuQi,"【手持】仪刀");
			}
			else if(attackerGrade > 90 && attackerGrade <= 105)
			{
				setTVValue(txtWuQi,"【手持】戟刀");
			}
			else if(attackerGrade > 105 && attackerGrade <= 120)
			{
				setTVValue(txtWuQi,"【手持】破甲长刀");
			}
			else if(attackerGrade > 120 && attackerGrade <= 150)
			{
				setTVValue(txtWuQi,"【手持】复式云纹军刀");
			}
			else if(attackerGrade > 150 && attackerGrade <= 200)
			{
				setTVValue(txtWuQi,"【手持】玄铁断魂刀");
			}
			else if(attackerGrade > 200 && attackerGrade <= 250)
			{
				setTVValue(txtWuQi,"【手持】素缨白莲枪");
			}
			else if(attackerGrade > 250)
			{
				setTVValue(txtWuQi,"【手持】麒麟吞日斩");
			}
		}
		
		for (int i = 0; i < datalEntity.getGoods().size(); i++)
		{
			goods goods = datalEntity.getGoods().get(i);
			if((goods.getType() == 101 || goods.getType() == 102 || goods.getType() == 103) && goods.getState() == 1)
			{
				layoutWuqi.setVisibility(View.VISIBLE);
				gidarray[0] = goods.getGid();
				setTVValue(txtWuQi,"【手持】"+goods.getName());
				btnWuqiNJ.setText("[ 耐:"+goods.getLife()+" ]");
			}
			else if(goods.getType() == 2 && goods.getState() == 1)
			{
				layoutTouKui.setVisibility(View.VISIBLE);
				layoutTouKui.setTag(goods.getGid());
				gidarray[1] = goods.getGid();
				setTVValue(txtTouKuai,"【头戴】"+goods.getName());
				btnTouKuaiNJ.setText("[ 耐:"+goods.getLife()+" ]");
			}
			else if(goods.getType() == 3 && goods.getState() == 1)
			{
				layoutPiFeng.setVisibility(View.VISIBLE);
				layoutPiFeng.setTag(goods.getGid());
				gidarray[2] = goods.getGid();
				btnPiFengNJ.setText("[ 耐:"+goods.getLife()+" ]");

				setTVValue(txtPiFeng,"【肩披】"+goods.getName());		
			}
			else if(goods.getType() == 4 && goods.getState() == 1)
			{
				layoutKaiJia.setVisibility(View.VISIBLE);
				layoutKaiJia.setTag(goods.getGid());
				gidarray[3] = goods.getGid();
				btnKuiJiaNJ.setText("[ 耐:"+goods.getLife()+" ]");

				setTVValue(txtKuiJia,"【身穿】"+goods.getName());		
			}
			else if(goods.getType() == 5 && goods.getState() == 1)
			{
				layoutYaoDai.setVisibility(View.VISIBLE);
				layoutYaoDai.setTag(goods.getGid());
				gidarray[4] = goods.getGid();

				btnYaoDiaNJ.setText("[ 耐:"+goods.getLife()+" ]");

				setTVValue(txtYaoDai,"【腰系】"+goods.getName());		
			}
			else if(goods.getType() == 6 && goods.getState() == 1)
			{
				layoutXieZi.setVisibility(View.VISIBLE);
				layoutXieZi.setTag(goods.getGid());
				gidarray[5] = goods.getGid();
				btnXieZiNJ.setText("[ 耐:"+goods.getLife()+" ]");

				setTVValue(txtXieZi,"【足踏】"+goods.getName());	
			}
			else if(goods.getType() == 7 && goods.getState() == 1)
			{
				layoutJieZi.setVisibility(View.VISIBLE);
				layoutJieZi.setTag(goods.getGid());
				gidarray[6] = goods.getGid();
				setTVValue(txtJieZi,"【指绕】"+goods.getName());	
			}
		}
		
		userdata entity = datalEntity.getUserdata();
		
		groupname = datalEntity.getGroupname();
		chenghaodescript = entity.getChenghaodescript();
		chenghao = entity.getChenghao();
		nextgradeexp = entity.getNextgradeexp();
		descript = entity.getDescript();
		name = entity.getName();
		dialog=entity.getDialog();
		uuid = entity.getUuid();
		country = entity.getCountryname();
		position = entity.getPosition();
		hp = entity.getHp();
		maxhp = entity.getMaxhp();
		grade = entity.getGrade();
		exp = entity.getExp();
		positionexp = entity.getPositionexp();
		nextpositionexp = entity.getNextpositionexp();
		attack = entity.getAttack();
		defence = entity.getDefence();
		dodge = entity.getDodge();
		positiongrade = entity.getPositiongrade();
		sign = entity.getSign();
		layoutLoadding.setVisibility(View.VISIBLE);
		gender = entity.getGender();
		initValues();
	}
	
	private void setTVValue(MTextView textview, String text)
	{
		textview.setVisibility(View.VISIBLE);
		textview.setText(text);
	}
	
	private void showEditSingDialog()
	{
		dialogSign = new Dialog(this, R.style.myDialogTheme);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_sign, null);
		dialogSign.setContentView(view);
		
		Button btnPub = (Button)view.findViewById(R.id.btnPubSign);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancelSign);
		
		btnPub.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		etSign = (EditText)view.findViewById(R.id.etSign);
		
		dialogSign.show();
	}

	Dialog dialogSign;
	EditText etSign;
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.txtGroup:
				requestBuildTeam();
				break;
			case R.id.btnDisWuqi:
				disArme(0);
				break;
			case R.id.btnDisTouKui:
				disArme(1);
				break;
			case R.id.btnDisPiFeng:
				disArme(2);
				break;
			case R.id.btnDisKaiJia:
				disArme(3);
				break;
			case R.id.btnDisYaoDai:
				disArme(4);
				break;
			case R.id.btnDisXieZi:
				disArme(5);
				break;
			case R.id.btnDisJieZi:
				disArme(6);
				break;
			case R.id.btnGroup:
//				ActivityUtil.goToNewActivity(this, GroupListActivity.class);
				Intent intent = new Intent(PlayerPanelActivity.this,GroupActivity.class);
				intent.putExtra("groupid", datalEntity.getUserdata().getGroupid());
				ActivityUtil.goToNewActivityWithComplement(PlayerPanelActivity.this, intent);
				break;
			case R.id.btnForm:	
				ActivityUtil.goToNewActivity(PlayerPanelActivity.this, ForumActivity.class);
				finish();
				break;
			case R.id.btnGoBack:
				goback();
				break;
			case R.id.txtAddFriend:
				addFriend(uuid);
				break;
			case R.id.txtSign:
				showEditSingDialog();
				break;
			case R.id.btnPubSign:
				submitSign();
				break;
			case R.id.btnCancelSign:
				if(dialog != null)
					dialogSign.dismiss();
				break;
			case R.id.txtKill:
				if(placeType == 1)
					ToastUtil.show(PlayerPanelActivity.this, "你不能在这里杀人!");
				else
					kill();break;
			case R.id.btnLeft:
				if(dialogIOSTaskNpcDialog != null && !this.isFinishing())
					dialogIOSTaskNpcDialog.dismiss();
				break;
			case R.id.txtTalk:
				Log.i("Test", "type="+type+"  personType"+personType+"  is null?"+(messgae == null));
				// npc
				if(type == 0)
				{
					// 普通NPC
					if(personType == 2)
						ToastUtil.show(PlayerPanelActivity.this, dialog);//.show(dialog, Toast.LENGTH_LONG);
					// 任务NPC
					else
					{
						if(messgae == null)
							ToastUtil.show(PlayerPanelActivity.this, dialog);
						else
							showTaskNpcDialog(code, messgae);
					}
				}
				else if(type== 1)
				{
					showIOSDialog(2);
				}
//				else if(type == 3)
//				{
//				}
				break;
			case R.id.txtTransaction:transaction();break;
			case R.id.txtGoOut:finish();break;
			case R.id.btnHelp:helping();break;
			case R.id.btn1:
				// 消息模式下点击回复
//				if(type == 1)
//				{
//					type = 2;
//					etInput.setVisibility(View.VISIBLE);
//					btn1.setText("发送回复");
//				}
//				else if(type == 2)
//				{
					if(etInput.getText() != null && etInput.getText().toString().length() > 0)
						sendMessage(uuid,etInput.getText().toString());
					else 
						ToastUtil.show(this, "还没输入消息内容呢", ToastUtil.WARN);
//				}
				break;
			case R.id.btn2:
				if(dialogIOS != null)
					dialogIOS.dismiss();
				break;
//			case R.id.txtUpGrade:
//				if(exp >= nextgradeexp && nextgradeexp != 0)
//				{
//						updateGrade();
//				}
//				else
//				{
//					ToastUtil.show(PlayerPanelActivity.this, getString(R.string.upgrade_fail));
//				}
//				break;
			default:
				break;
		}
	}
	
	private void helping()
	{
		ActivityUtil.goToNewActivity(PlayerPanelActivity.this, HelpActivity.class);
	}

	private void requestBuildTeam()
	{
		finalHttp.get(API.TEAM_REQUEST+"buildTeam.action?&teammaster="+MyApplication.getUUID(this)+"&member="+uuid, new AjaxCallBack<Object>()
		{
			@Override
			public void onLoading(long count, long current)
			{
				// TODO Auto-generated method stub
				super.onLoading(count, current);
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
						String mes = jsonObject.getString("message");
						if(mes != null)
							ToastUtil.showStaticToastShort(PlayerPanelActivity.this, mes);
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

	private void transaction()
	{
		Intent intent = new Intent(PlayerPanelActivity.this, GoodsListActivty.class);
		intent.putExtra("uuid", uuid);
		intent.putExtra("activityType", 1);
//		Log.i("XU", "uuid="+uuid);
		ActivityUtil.goToNewActivityWithComplement(this, intent);
	}

	// 回城
	private void goback()
	{
		finalHttp.get(API.URL+"goback.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "Loadding");
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
						PlayerPanelActivity.this.finish();
						ToastUtil.show(PlayerPanelActivity.this, message);
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						Log.i("XU", "出现异常="+e.toString());
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void addFriend(String uuidFriend)
	{
		if(uuidFriend.equals(MyApplication.getUUID(this)))
		{
			ToastUtil.show(this, "不能添加自己为好友!");
		}
		else
		{
			finalHttp.get(API.URL+"addfriend.action?&fuuid="+uuidFriend+"&buuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
			{
				Dialog dialog ;
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
					dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "Loading...");
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
							String message;
							JSONObject jsonObject = new JSONObject(t.toString());
							message = jsonObject.getString("message");
							ToastUtil.show(PlayerPanelActivity.this, message);
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

	private void disArme(final int layouttype)
	{
		String gid = gidarray[layouttype];
		finalHttp.get(API.URL2+"disarmed.action?&uuid="+MyApplication.getUUID(this)+"&gid="+gid, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("XU", "卸除武装失败="+strMsg);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "Loading...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "成功卸除武装"+t.toString());
				if(t != null)
				{
					switch (layouttype)
					{
						case 0:
							layoutWuqi.setVisibility(View.GONE);
							break;
						case 1:
							layoutTouKui.setVisibility(View.GONE);
							break;
						case 2:
							layoutPiFeng.setVisibility(View.GONE);
							break;
						case 3:
							layoutKaiJia.setVisibility(View.GONE);
							break;
						case 4:
							layoutYaoDai.setVisibility(View.GONE);
							break;
						case 5:
							layoutXieZi.setVisibility(View.GONE);
							break;
						case 6:
							layoutJieZi.setVisibility(View.GONE);
							break;
						default:
							break;
					}
					
					// xushiyong
					loadData(0, 0);
				}
			}
		});
	}

//	private String getGoodsGid(int type)
//	{
//		for (int i = 0; i < datalEntity.getGoods().size(); i++)
//		{
//			goods goods = datalEntity.getGoods().get(i);
//			
//			if(goods.getType() == type)
//			{
//				return goods.getGid();
//			}
//		}
//		return null;
//	}
	
	
	private void submitSign()
	{
		sign = etSign.getText().toString();
		if(sign == null || "".equals(sign.trim()))
		{
			ToastUtil.show(this, "不能为空");
		}
		else
		{
			if(dialogSign != null)
				dialogSign.dismiss();
			try
			{
				finalHttp.get(API.URL+"sign.action?&uuid="+MyApplication.getUUID(this)+"&sign="+(URLEncoder.encode(sign,"utf-8")), new AjaxCallBack<Object>()
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
						dialog  = MessageDialog.createLoadingDialog(PlayerPanelActivity.this, "");
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
								JSONObject object = new JSONObject(t.toString());
								String messageStr = object.getString("message");
								int code = object.getInt("code");
								ToastUtil.show(PlayerPanelActivity.this, messageStr);
								txtSign.setText(sign);
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
			catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updateGrade()
	{
		finalHttp.get(API.URL+"upgrade.action?&uuid="+uuid+"&exp="+exp, new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("Test", "验证失败");
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = DialogUtil.createLoadingDialog(PlayerPanelActivity.this, "正在效验，请稍等...");
				dialog.setCancelable(false);
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
						JSONObject object = new JSONObject(t.toString());
						int code = object.getInt("code");
						if(code == 200)
						{
							String message = object.getString("message");
							PlayerPanelActivity.this.exp = object.getLong("result");
							PlayerPanelActivity.this.nextgradeexp = object.getLong("next");
							MyApplication.user.setExp(PlayerPanelActivity.this.exp);
							//07-04 13:02:23.274: I/Test(5912): url= http://wlgac420108.jsp.jspee.org/requestAction/getplaceinfoall.action?&name=%E8%AF%B8%E8%91%9B%E6%AD%A6%E4%BE%AF&curid=5&id=5&uuid=0040&type=0

							txtCurrentExp.setText("当前经验:"+PlayerPanelActivity.this.exp);
							txtNextExp.setText("下级经验:"+(PlayerPanelActivity.this.nextgradeexp));
							grade += 1;
							txtGrade.setText("等级:"+(grade));
							ToastUtil.show(PlayerPanelActivity.this, ""+message);
						}
						// xushiyong
						loadData(0, 0);
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

	private void kill()
	{
		if(grade < 10 && personType==1)
		{
			ToastUtil.show(this, "对方还是个涉世未深的孩子，你怎么忍心下此毒手!");
		}
		else
		{
			if(MyApplication.user.getUuid() != null && !"".equals(MyApplication.user.getUuid()) && (uuid != null && !"".equals(uuid)))
			{
				Intent intent = new Intent(this, BattleActivity.class);
				intent.putExtra("attackerid", MyApplication.user.getUuid());
				intent.putExtra("defencerid", uuid);
				ActivityUtil.goToNewActivityWithComplement(this, intent);
				finish();
			}
			else
			{
				ToastUtil.show(this, "状态失效!请刷新或重新登录。");
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
		
		finalHttp.post(API.MESSAGE_REQUEST+"sendmessage.action?", params, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "发送消息失败"+strMsg);
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
				ToastUtil.show(PlayerPanelActivity.this, "成功向ta发送消息:"+message, ToastUtil.INFO);
			}
		});
	}

	Dialog dialogIOS;
	private TextView txtDialogTitle;
	private TextView txtDialogMessage;
	private EditText etInput;
	private Button btn1;
	private Button btn2;
	private void showIOSDialog(int type)
	{
//		this.type = type;
		dialogIOS = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog  
		LayoutInflater inflater = LayoutInflater.from(this);  
		View v = inflater.inflate(R.layout.ios_dialog, null);// 得到加载view  
		
		txtDialogMessage = (TextView) v.findViewById(R.id.txtDialogMessage);
		txtDialogTitle   = (TextView) v.findViewById(R.id.txtDialogTitle);
		etInput = (EditText)v.findViewById(R.id.etInput);
		btn1 = (Button)v.findViewById(R.id.btn1);// 现在回复
		btn2 = (Button)v.findViewById(R.id.btn2);// 稍后再看
		dialogIOS.setContentView(v);
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		
		if(type == 2)
		{
			etInput.setVisibility(View.VISIBLE);
			btn1.setText("发送消息");
		}
		dialogIOS.show();
	}
	
	Dialog dialogIOSTaskNpcDialog;
	MTextView txtTaskMester;
	MTextView txtTaskDescripe;
	Button btnLeft;
	View lineview;
	Button btnRight;
	private void showTaskNpcDialog(int code,String message)
	{
		dialogIOSTaskNpcDialog = new Dialog(this, R.style.myDialogTheme);// 创建自定义样式dialog  
		LayoutInflater inflater = LayoutInflater.from(this);  
		View v = inflater.inflate(R.layout.ios_dialog_task_npc, null);// 得到加载view  
		btnLeft = (Button) v.findViewById(R.id.btnLeft);
		btnRight = (Button) v.findViewById(R.id.btnRight);
		lineview = v.findViewById(R.id.lineview);
		txtTaskDescripe = (MTextView)v.findViewById(R.id.txtTaskDescripe);
		txtTaskMester   = (MTextView)v.findViewById(R.id.txtTaskMester);
		dialogIOSTaskNpcDialog.setContentView(v);
		
		txtTaskMester.setText(name);
		txtTaskDescripe.setText(Html.fromHtml(message));
		
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		
		if(code == 0)
		{
			btnLeft.setText("义不容辞,这就完成任务!");
		}
		else if(code == 1)
		{
			btnLeft.setText("继续去完成任务吧");
		}
		else if(code == 2)
		{
			btnLeft.setText("任务已完成，告辞了");
		}
		else if(code == 3)
		{
			btnLeft.setText("这是我应该做的");
		}
		else if(code == 4)
		{
			btnLeft.setText("好吧，我再转转");
		}
		else 
		{
			btnLeft.setText("好的，明白");
		}
		dialogIOSTaskNpcDialog.show();
	}
	
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(toastUtil != null)
			toastUtil.cancel();
	}
}
