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
import android.widget.TextView;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

public class GoodsSellDetailActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	MTextView txtGoodName;
	MTextView txtGoodGrade;
	MTextView txtGoodDesc;
	MTextView txtPrice;
	MTextView txtSellerName;
	TextView txtNJZ;
	TextView txtStar;
	Button btnBuy;
	Button btnLeft;
	Button btnRight;
	
	String tid;
	String gid;
	String name;
	String descript;
	String mastername;
	String masterid;
	long price;
	int grade;
	int mode = 1;
	int life ;
	int star;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		
		Intent intent = getIntent();
		tid = intent.getStringExtra("tid");
		gid = intent.getStringExtra("gid");
		descript = intent.getStringExtra("descript");
		mastername = intent.getStringExtra("mastername");
		name = intent.getStringExtra("name");
		price = intent.getLongExtra("price", 1);
		grade = intent.getIntExtra("grade", 1);
		mode = intent.getIntExtra("mode", 1);
		masterid = intent.getStringExtra("masterid");
		life = intent.getIntExtra("life", 0);
		star = intent.getIntExtra("star", 0);
		initViews();
		initValue();
	}
	
	public void initValue()
	{
		txtGoodGrade.setText(grade+"级");
		txtGoodDesc.setText(descript);
		txtGoodName.setText(name);
		txtNJZ.setText("[ 耐久值："+life+" ]");
		txtPrice.setText(conver(""+(price))+"两白银");
		txtSellerName.setText(mastername);
		txtStar.setText("[ "+star+" 星 ]");
	}
	
	private void initViews()
	{
		txtStar = (TextView)this.findViewById(R.id.txtStar);
		txtNJZ = (TextView)this.findViewById(R.id.txtNJZ);
		txtGoodDesc = (MTextView)this.findViewById(R.id.txtGoodDesc);
		txtGoodGrade = (MTextView)this.findViewById(R.id.txtGoodGrade);
		txtGoodName = (MTextView)this.findViewById(R.id.txtGoodName);
		txtPrice = (MTextView)this.findViewById(R.id.txtPrice);
		txtSellerName = (MTextView)this.findViewById(R.id.txtSellerName);
		
		btnBuy = (Button)this.findViewById(R.id.btnBuy);
		btnLeft = (Button)this.findViewById(R.id.btnLeft);
		btnRight = (Button)this.findViewById(R.id.btnRight);
		
		if(mode == 1024)
		{
			btnBuy.setText("取回物品");
		}
		else
		{
			btnBuy.setText("我要买下");
		}
		
		if(masterid.equals(MyApplication.getUUID(this)))
		{
			btnBuy.setText("取回物品");
		}
		else
		{
			btnBuy.setText("我要买下");
		}
		
		btnBuy.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
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
			case R.id.btnLeft:
			case R.id.btnRight:
				finish();
				break;
			case R.id.btnBuy:
				buying();
				break;
			default:
				break;
		}
	}
	
	private void buying()
	{
		//String tid,String gid,String buyer_uuid
		finalHttp.get(API.TRANSACTION+"buy.action?&tid="+tid+"&gid="+gid+"&buyer_uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", ""+strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(GoodsSellDetailActivity.this, "加载中...");
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
						String mes = jsonObject.getString("message");
						if(mes != null)
							ToastUtil.show(GoodsSellDetailActivity.this, mes);
						int code = jsonObject.getInt("code");
						Log.i("XU", "code= "+code);
						if(code ==  200 || code == 202)
						{
							Intent intent = new Intent("remove_item");
							sendBroadcast(intent);
							finish();
						}
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

	public static String conver(String temp)
	{
	       // 单位数组  
     String[] units = new String[] {"十", "百", "千", "万", "十", "百", "千", "亿"};  
       
     // 中文大写数字数组  
     String[] numeric = new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};  
       
     String res = "";  
       
         // 遍历一行中所有数字  
         for (int k = -1; temp.length() > 0; k++)  
         {  
             // 解析最后一位  
             int j = Integer.parseInt(temp.substring(temp.length() - 1, temp.length()));  
             String rtemp = numeric[j];  
               
             // 数值不是0且不是个位 或者是万位或者是亿位 则去取单位  
             if (j != 0 && k != -1 || k % 8 == 3 || k % 8 == 7)  
             {  
                 rtemp += units[k % 8];  
             }  
               
             // 拼在之前的前面  
             res = rtemp + res;  
               
             // 去除最后一位  
             temp = temp.substring(0, temp.length() - 1);  
         }  
           
         // 去除后面连续的零零..  
         while (res.endsWith(numeric[0]))  
         {  
             res = res.substring(0, res.lastIndexOf(numeric[0]));  
         }  
           
         // 将零零替换成零  
         while (res.indexOf(numeric[0] + numeric[0]) != -1)  
         {  
             res = res.replaceAll(numeric[0] + numeric[0], numeric[0]);  
         }  
           
         // 将 零+某个单位 这样的窜替换成 该单位 去掉单位前面的零  
         for (int m = 1; m < units.length; m++)  
         {  
             res = res.replaceAll(numeric[0] + units[m], units[m]);  
         }  
         
         return res;
	}

}
