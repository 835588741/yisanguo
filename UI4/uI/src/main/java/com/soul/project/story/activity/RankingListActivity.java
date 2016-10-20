/**
 * 
 */
package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.RankingListAdapter;
import com.soul.project.application.bean.PlaceBean;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.view.MTextView;
import com.yisanguo.app.api.API;

/**
 * @file RankingListActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年6月21日 下午4:43:35
 */
public class RankingListActivity extends BaseActivityWithSystemBarColor implements OnCheckedChangeListener
{
	private RadioGroup group;
	private ListView listview;
	private RankingListAdapter adapter;
	private RadioButton rbGrade;
	private RadioButton rbMoney;
	private RadioButton rbRight;
	private RadioButton rbCountry;
	private List<PlaceBean.player> listGrade = new ArrayList<PlaceBean.player>();
	private List<PlaceBean.player> listMoney = new ArrayList<PlaceBean.player>();
	private List<PlaceBean.player> listRight = new ArrayList<PlaceBean.player>();
	private LinearLayout layoutCounty;
	MTextView txtTag1;
	MTextView txtCountry1;
	MTextView txtCountry1money;
	MTextView txtCountry1person;
	
	MTextView txtTag2;
	MTextView txtCountry2;
	MTextView txtCountry2money;
	MTextView txtCountry2person;
	
	MTextView txtTag3;
	MTextView txtCountry3;
	MTextView txtCountry3money;
	MTextView txtCountry3person;
	
	MTextView txtTag4;
	MTextView txtCountry4;
	MTextView txtCountry4money;
	MTextView txtCountry4person;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rankinglist);
		loadData(0,0);
		initViews();
	}
	
	private void initViews()
	{
		txtTag1 = (MTextView)this.findViewById(R.id.txtTag1);
		txtCountry1 = (MTextView)this.findViewById(R.id.txtCountry1);
		txtCountry1money = (MTextView)this.findViewById(R.id.txtCountry1money);
		txtCountry1person  = (MTextView)this.findViewById(R.id.txtCountry1person);
		
		txtTag2 = (MTextView)this.findViewById(R.id.txtTag2);
		txtCountry2 = (MTextView)this.findViewById(R.id.txtCountry2);
		txtCountry2money = (MTextView)this.findViewById(R.id.txtCountry2money);
		txtCountry2person  = (MTextView)this.findViewById(R.id.txtCountry2person);

		txtTag3 = (MTextView)this.findViewById(R.id.txtTag3);
		txtCountry3 = (MTextView)this.findViewById(R.id.txtCountry3);
		txtCountry3money = (MTextView)this.findViewById(R.id.txtCountry3money);
		txtCountry3person  = (MTextView)this.findViewById(R.id.txtCountry3person);
		
		txtTag4 = (MTextView)this.findViewById(R.id.txtTag4);
		txtCountry4 = (MTextView)this.findViewById(R.id.txtCountry4);
		txtCountry4money = (MTextView)this.findViewById(R.id.txtCountry4money);
		txtCountry4person  = (MTextView)this.findViewById(R.id.txtCountry4person);

		layoutCounty = (LinearLayout)this.findViewById(R.id.layoutCounty);
		
		rbCountry = (RadioButton)this.findViewById(R.id.rbCountry);
		rbRight = (RadioButton)this.findViewById(R.id.rbRight);
		rbGrade = (RadioButton)this.findViewById(R.id.rbGrade);
		rbMoney = (RadioButton)this.findViewById(R.id.rbMoney);
		adapter = new RankingListAdapter(this, listGrade);
		group = (RadioGroup)this.findViewById(R.id.radiogroup);
		group.setOnCheckedChangeListener(this);
		listview = (ListView)this.findViewById(R.id.listview);
		listview.setAdapter(adapter);
	}

	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int)
	 */
	@Override
	public void loadData(int id,int type)
	{
		//Log.i("XU", "url="+API.URL+"ranking.action?&order=grade");
		finalHttp.get(API.URL+"ranking.action?&order=grade", new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Log.i("XU", "111获取出错："+strMsg+"  ceod="+errorNo);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(RankingListActivity.this, "加载数据中...");
				dialog.setCancelable(true);
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "1111t.toString="+t.toString());
				if(t != null)
				{
					Gson gson = new Gson();
					listGrade = gson.fromJson(t.toString(), new TypeToken<List<PlaceBean.player>>() {}.getType());
					adapter.setType(1);
					adapter.setList(listGrade);
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		finalHttp.get(API.URL+"ranking.action?&order=money", new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(RankingListActivity.this, "加载数据中...");
				dialog.setCancelable(true);
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
					listMoney = gson.fromJson(t.toString(), new TypeToken<List<PlaceBean.player>>() {}.getType());
				}
			}
		});
		
		// 09-01 14:07:06.099: I/XU(1844): 请求地址=http://112.124.109.206:8080/springMVC/rankAction/countryrank.action?

		finalHttp.get(API.URL_RANK+"countryrank.action?", new AjaxCallBack<Object>()
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
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						JSONObject nanyang = jsonObject.getJSONObject("nanyang");
						JSONObject luoyang = jsonObject.getJSONObject("luoyang");
						JSONObject xuchang = jsonObject.getJSONObject("xuchang");
						JSONObject xinshoucun = jsonObject.getJSONObject("xinshoucun");
						
						if(nanyang != null)
						{
							String countryname = nanyang.getString("countryname");
							txtCountry1.setText(countryname);
							long money = nanyang.getLong("money");
							txtCountry1money.setText("财富: "+money+"银");
							int people = nanyang.getInt("people");
							txtCountry1person.setText("国民: "+people+"万");
						}
						
						if(luoyang != null)
						{
							String countryname = luoyang.getString("countryname");
							txtCountry2.setText(countryname);
							long money = luoyang.getLong("money");
							txtCountry2money.setText("财富: "+money+"银");
							int people = luoyang.getInt("people");
							txtCountry2person.setText("国民: "+people+"万");
						}
						
						if(xinshoucun != null)
						{
							String countryname = xinshoucun.getString("countryname");
							txtCountry3.setText(countryname);
							long money = xinshoucun.getLong("money");
							txtCountry3money.setText("财富: "+money+"银");
							int people = xinshoucun.getInt("people");
							txtCountry3person.setText("国民: "+people+"万");
						}
						
						if(xuchang != null)
						{
							String countryname = xuchang.getString("countryname");
							txtCountry4.setText(countryname);
							long money = xuchang.getLong("money");
							txtCountry4money.setText("财富: "+money+"银");
							int people = xuchang.getInt("people");
							txtCountry4person.setText("国民: "+people+"万");
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						Log.i("XU",""+e.toString());
						e.printStackTrace();
					}
				}
			}
		});
		
		finalHttp.get(API.URL+"ranking.action?&order=positiongrade", new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "222获取出错："+strMsg+"  ceod="+errorNo);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(RankingListActivity.this, "加载数据中...");
				dialog.setCancelable(true);
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Log.i("XU", "22222t.toString="+t.toString());
				dialog.dismiss();
				if(t != null)
				{
					Gson gson = new Gson();
					listRight = gson.fromJson(t.toString(), new TypeToken<List<PlaceBean.player>>() {}.getType());
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
//		Log.i("XU", "cheid="+checkedId +"   grade="+R.id.rbGrade+"   money="+R.id.rbMoney);
		
		if(checkedId == R.id.rbGrade)
		{
			listview.setVisibility(View.VISIBLE);
			layoutCounty.setVisibility(View.GONE);
			
			rbGrade.setBackgroundResource(R.drawable.bg_switch_select);
			rbGrade.setTextColor(getResources().getColor(R.color.white));
			
			rbMoney.setBackgroundResource(android.R.color.transparent);
			rbMoney.setTextColor(getResources().getColor(R.color.black));
			rbRight.setBackgroundResource(android.R.color.transparent);
			rbRight.setTextColor(getResources().getColor(R.color.black));
			rbCountry.setBackgroundResource(android.R.color.transparent);
			rbCountry.setTextColor(getResources().getColor(R.color.black));
			
			adapter.setType(1);
			adapter.setList(listGrade);
			adapter.notifyDataSetChanged();
		}
		else if(checkedId == R.id.rbMoney)
		{
			listview.setVisibility(View.VISIBLE);
			layoutCounty.setVisibility(View.GONE);
			
			rbMoney.setBackgroundResource(R.drawable.bg_switch_select);
			rbMoney.setTextColor(getResources().getColor(R.color.white));
			
			rbGrade.setBackgroundResource(android.R.color.transparent);
			rbGrade.setTextColor(getResources().getColor(R.color.black));
			rbRight.setBackgroundResource(android.R.color.transparent);
			rbRight.setTextColor(getResources().getColor(R.color.black));
			rbCountry.setBackgroundResource(android.R.color.transparent);
			rbCountry.setTextColor(getResources().getColor(R.color.black));
			
			adapter.setList(listMoney);
			adapter.setType(3);
			adapter.notifyDataSetChanged();
		}
		else if(checkedId == R.id.rbRight)
		{
			listview.setVisibility(View.VISIBLE);
			layoutCounty.setVisibility(View.GONE);
			
			rbRight.setBackgroundResource(R.drawable.bg_switch_select);
			rbRight.setTextColor(getResources().getColor(R.color.white));
			
			rbGrade.setBackgroundResource(android.R.color.transparent);
			rbGrade.setTextColor(getResources().getColor(R.color.black));
			rbMoney.setBackgroundResource(android.R.color.transparent);
			rbMoney.setTextColor(getResources().getColor(R.color.black));
			rbCountry.setBackgroundResource(android.R.color.transparent);
			rbCountry.setTextColor(getResources().getColor(R.color.black));
			
			adapter.setType(2);
			adapter.setList(listRight);
			adapter.notifyDataSetChanged();
		}
		else if(checkedId == R.id.rbCountry)
		{
			listview.setVisibility(View.GONE);
			layoutCounty.setVisibility(View.VISIBLE);
			
			rbMoney.setBackgroundResource(android.R.color.transparent);
			rbMoney.setTextColor(getResources().getColor(R.color.black));
			
			rbGrade.setBackgroundResource(android.R.color.transparent);
			rbGrade.setTextColor(getResources().getColor(R.color.black));
			
			rbRight.setBackgroundResource(android.R.color.transparent);
			rbRight.setTextColor(getResources().getColor(R.color.black));
			
			rbCountry.setBackgroundResource(R.drawable.bg_switch_select);
			rbCountry.setTextColor(getResources().getColor(R.color.white));
		}
	}

}
