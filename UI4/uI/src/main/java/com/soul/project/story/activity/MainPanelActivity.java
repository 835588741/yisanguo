/**
 * 
 */
package com.soul.project.story.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soul.project.application.adapter.AchieveMessageAdapter;
import com.soul.project.application.adapter.GoodsAdapter;
import com.soul.project.application.adapter.GridviewAdapter;
import com.soul.project.application.adapter.LeaveMessageAdapter;
import com.soul.project.application.bean.PlaceBean;
import com.soul.project.application.bean.PlaceBean.goods;
import com.soul.project.application.bean.PlaceBean.player;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ShareDB;
import com.soul.project.application.util.SoundPlayerUtil;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.util.Utils;
import com.soul.project.application.view.MGridView;
import com.soul.project.application.view.MTextView;
import com.soul.project.application.view.MValidDialog;
import com.yisanguo.app.api.API;
import com.yisanguo.app.api.ErrorHandler;

/**
 * @file MainPanelActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年6月13日 上午11:53:18
 */
public class MainPanelActivity extends BaseActivityWithSystemBarColor implements OnClickListener, OnItemClickListener
{
	private MTextView txtName;
	private MTextView txtDescript; 
	private MTextView txtEast;
	private MTextView txtWest;
	private MTextView txtSouth;
	private MTextView txtNorth;
	private MTextView txtNotice;
	private MTextView txtRefresh;
	private MTextView txtCenter;
	private MTextView txtNearByNumber;
	private MTextView txtFunction01;
	private MTextView txtFunction02;
	private MTextView txtFunction03;
	private MTextView txtFunction04;
	private MTextView numberNPC;
	private MTextView numberGoods;
	
	private Button btnThings;
	private Button btnStatus;
	private Button btnFriend;
	private Button btnMessage;
	private Button btnSetting;
	private Button btnTask;
	
	private ListView lvLeaveMessage;
	private ListView lvAchieveMessage;
	
	private LinearLayout layoutFunction;
	private LinearLayout layoutChatView;
	private LinearLayout layoutInputMessage;
	private TextView txtChatTest;
	private Button btnShowOrGoneInputLayout;
	private Button btnSendMess;
	private EditText txtChatInputTest;
	private TextView txtChatTestPosition;
	private TextView txtChatTestName;
	private TextView txtTeamMessage;
	
//	private boolean isShowInputTextLayout = false;
	
	private MGridView gvNPC;
	private MGridView gvGoods;
	private MGridView gvPerson;
	
	private int delayTime = 8500;
	
	private GridviewAdapter adapterNPC;
	private GridviewAdapter adapterPerson;
	private GoodsAdapter adapterGoods;
	
	public static PlaceBean placeBean;
	String[][] qa = new String[][]{{"4+5=?","9"},{"3+8=?","11"},{"6+2=?","8"},{"3+4=?","7"},{"6+3=?","9"},{"4+3=?","7"},{"3+2=?","5"},{"2+2=?","4"},{"3+3=?","6"},{"1+2=?","3"}};
	String[] name = new String[]{"公孙大娘","汉武大帝","孙悟空","猪八戒","白脸唐僧","憨厚沙僧","白公子","笑傲风间","追风少年","无名游侠","独孤信","秦始皇","张无忌","赵敏","金毛狮王","黑龙使者","清香白莲","张韶涵","张含韵","阿信","周杰伦","许文强","丁力","冯程程","孔子","老子","墨子","荆轲","高渐离","秦叔宝","程咬金","乔峰","慕容复","吨正常","段正淳","唐太宗","西夏公主","虚竹","扫地僧"};
	String[] tipMessage ;
	private ProgressBar progressBar;
	Random random = new Random();
	
	/** 此地的NPC*/
	private LinearLayout layoutSomeBody;
	/** 地上的东西*/
	private LinearLayout layoutSomething;
	/** 此地遇上的人*/
	private LinearLayout layoutMetSomeBody;
	private LinearLayout layoutBig;
//	private ScrollView mainPanel;
	private MTextView txtBattleDescript;
	String cityName = "";
	long lastTime = 0;
	boolean isCanSendMessage = true;
	private MediaPlayer player;
	int index = -1;
	String[] songs;
//	private String songlist = "http://7xqar4.com1.z0.glb.clouddn.com/sanguolists.txt";
//	List<SongEntity> list = new ArrayList<SongEntity>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpanel);
		
		int isnewplayer = ShareDB.getIntFromDB(this, "isnewplayer");
		if(isnewplayer == 0)
		{
			
		}
		songs = getResources().getStringArray(R.array.songs);
		player = new MediaPlayer();
		tipMessage = getResources().getStringArray(R.array.messages);
 		
		initViews();
		initEvent();
	}
	
	private void initPlayer(String url)
	{
		try
		{
			if(player == null)
				player = new MediaPlayer();
			
			if(player.isPlaying())
			{
				player.pause();
				player.reset();
			}
			
			player.setDataSource(url);
			player.setOnCompletionListener(new OnCompletionListener(){  
				@Override  
				public void onCompletion(MediaPlayer mp) {  
					//mp.release();//释放音频资源  
					player.reset(); // reset需要在setDataSource之前
					changeSong(1);
				}  
			});
//			player.prepareAsync();();
			// 播放时保持屏幕唤醒
			player.setScreenOnWhilePlaying(true);
			player.prepare();
			player.start();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	
	/**
	 * 切换歌曲
	 * @author 许仕永(xsy)
	 * des: 
	 * @param flag  1|下一首  0|上一首
	 */
	private void changeSong(int flag)
	{
		// 回退前一首
		if(flag == 0)
		{
			index --;
			if(index < 0)
				index = 0;
		}
		// 进入下一首
		else
		{
			index ++;
			if(index >= songs.length)
				index = songs.length - 1;
		}
		// 初始并播放指定url
		initPlayer("http://7xqar4.com1.z0.glb.clouddn.com/"+songs[index]);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		if(layoutBig != null)
		{
			String isNight = ShareDB.getStringFromDB(this, "isopen_night");
			if("true".equals(isNight))
			{
				layoutBig.setBackgroundResource(R.color.black_light_title_text_color);
			}
			else
			{
				layoutBig.setBackgroundResource(R.color.gray_light);
			}
		}

		String s = ShareDB.getStringFromDB(MainPanelActivity.this, "isOpenPublicChat");
		if("true".equals(s))
		{
			layoutChatView.setVisibility(View.GONE);
		}
		else
		{
			layoutChatView.setVisibility(View.VISIBLE);
		}
		
		
		if(MyApplication.user != null)
			loadData(MyApplication.user.getAreaid(),0);
		else
			ActivityUtil.goToNewActivity(this, LoginActivity.class);
	}

	private void initEvent()
	{
		txtEast.setOnClickListener(this);
		txtWest.setOnClickListener(this);
		txtSouth.setOnClickListener(this);
		txtNorth.setOnClickListener(this);
		txtRefresh.setOnClickListener(this);
		gvNPC.setOnItemClickListener(this);
		gvPerson.setOnItemClickListener(this);
		
		btnFriend.setOnClickListener(this);
		btnMessage.setOnClickListener(this);
		btnStatus.setOnClickListener(this);
		btnThings.setOnClickListener(this);
		btnTask.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
		btnSendMess.setOnClickListener(this);
		btnShowOrGoneInputLayout.setOnClickListener(this);
	}

	private void initViews()
	{
		txtChatTestName = (TextView)this.findViewById(R.id.txtChatTestName);
		txtChatTestPosition = (TextView)this.findViewById(R.id.txtChatTestPosition);
		// 整个公聊屏幕
		layoutChatView = (LinearLayout)this.findViewById(R.id.layoutChatView);
		// 公聊输入区域
		layoutInputMessage = (LinearLayout)this.findViewById(R.id.layoutInputMessage);
		// 公聊信息滚展控件
		txtChatTest = (TextView)this.findViewById(R.id.txtChatTest);
		// 公聊信息输入区域是否显示的点击按钮
		btnShowOrGoneInputLayout = (Button)this.findViewById(R.id.btnShowOrGoneInputLayout);
		// 公聊信息发送控件
		btnSendMess = (Button)this.findViewById(R.id.btnSendMess);
		// 公聊信息输入控件
		txtChatInputTest = (EditText)this.findViewById(R.id.txtChatInputTest);
		
		// 组队消息
		txtTeamMessage = (TextView)this.findViewById(R.id.txtTeamMessage);
		
		numberGoods = (MTextView)this.findViewById(R.id.numberGoods);
		numberNPC = (MTextView)this.findViewById(R.id.numberNPC);
		btnSetting = (Button)this.findViewById(R.id.btnSetting);
		btnTask = (Button)this.findViewById(R.id.btnTask);
		layoutBig = (LinearLayout)this.findViewById(R.id.layoutBig);
		layoutBig.setBackgroundResource(R.color.gray_light);
		layoutFunction = (LinearLayout)this.findViewById(R.id.layoutFunction);
		btnFriend = (Button)this.findViewById(R.id.btnFriend);
		btnMessage = (Button)this.findViewById(R.id.btnMessage);
		btnStatus = (Button)this.findViewById(R.id.btnStatus);
		btnThings = (Button)this.findViewById(R.id.btnThings);
		
		txtFunction01 = (MTextView)this.findViewById(R.id.txtFunction01);
		txtFunction02 = (MTextView)this.findViewById(R.id.txtFunction02);
		txtFunction03 = (MTextView)this.findViewById(R.id.txtFunction03);
		txtFunction04 = (MTextView)this.findViewById(R.id.txtFunction04);
		
		progressBar = (ProgressBar)this.findViewById(R.id.progress);
		
		txtBattleDescript = (MTextView)this.findViewById(R.id.txtBattleDescript);
		
		txtNotice = (MTextView)this.findViewById(R.id.txtNotice);
		txtNearByNumber = (MTextView)this.findViewById(R.id.txtNearByNumber);
		txtName = (MTextView)this.findViewById(R.id.txtName);
		txtDescript = (MTextView)this.findViewById(R.id.txtDescript);
		txtEast = (MTextView)this.findViewById(R.id.txtEast);
		txtWest = (MTextView)this.findViewById(R.id.txtWest);
		txtSouth = (MTextView)this.findViewById(R.id.txtSouth);
		txtNorth = (MTextView)this.findViewById(R.id.txtNorth);
		txtRefresh = (MTextView)this.findViewById(R.id.txtRefresh);
		txtCenter = (MTextView)this.findViewById(R.id.txtCenter);
		
		lvLeaveMessage = (ListView)this.findViewById(R.id.listLeave);
		lvAchieveMessage = (ListView)this.findViewById(R.id.listAchieve);
		
		gvNPC = (MGridView)this.findViewById(R.id.gvNPC);
		gvGoods = (MGridView)this.findViewById(R.id.gvGoods);
		gvPerson = (MGridView)this.findViewById(R.id.gvPerson);
		
		Drawable drawable = getResources().getDrawable(R.drawable.selector_gridview_item);
		gvNPC.setSelector(drawable);
		gvGoods.setSelector(drawable);
		gvPerson.setSelector(drawable);
		
		gvGoods.setOnItemClickListener(this);
		
		layoutSomeBody = (LinearLayout)this.findViewById(R.id.layoutSomeBody);
		layoutSomething= (LinearLayout)this.findViewById(R.id.layoutSomething);
		layoutMetSomeBody = (LinearLayout)this.findViewById(R.id.layoutMetSomeBody);
	}

	@Override
	public void onClick(View v)
	{
		if(placeBean != null)
		switch (v.getId())
		{
			case R.id.txtEast: loadData(placeBean.getEastid(),1);break;
			case R.id.txtWest: loadData(placeBean.getWestid(),1);break;
			case R.id.txtSouth:loadData(placeBean.getSouthid(),1);break;
			case R.id.txtNorth:loadData(placeBean.getNorthid(),1);break;
			case R.id.btnShowOrGoneInputLayout:
				if(layoutInputMessage.isShown())
				{
					btnShowOrGoneInputLayout.setText("发言");
					layoutInputMessage.setVisibility(View.GONE);
				}
				else 
				{
					btnShowOrGoneInputLayout.setText("收起");
					layoutInputMessage.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.btnSendMess:if(isCanSendMessage)sendPublicMessage(); else ToastUtil.showStaticToastShort(MainPanelActivity.this, "发表频率太快，请稍后！");break;
			case R.id.btnFriend: friendlist();break;
			case R.id.btnStatus: me();break;
			case R.id.btnMessage:message();break;
			case R.id.btnThings: things();break;
			case R.id.btnTask:task();break;
			case R.id.btnSetting:setting();break;
			case R.id.txtRefresh:loadData(MyApplication.user.getAreaid(),0);break;
			case R.id.btnPub:pubWish();break;
			case R.id.btnCancel:if(wishDialog != null)wishDialog.dismiss();break;
			case R.id.btnChange:etMasterName.setText(name[random.nextInt(name.length)]);break;
			default:
				break;
		}
	}
	
	private void sendPublicMessage()
	{
		String mes = txtChatInputTest.getText().toString();
		if(mes != null && mes.length() >= 5)
		{
			AjaxParams params = new AjaxParams();
			params.put("message", mes);
			params.put("uuid", placeBean.getUserdata().getUuid());
			finalHttp.post(API.MESSAGE_REQUEST+"sendpublicmessage.action?",params, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					isCanSendMessage = true;
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					isCanSendMessage = false;
				}

				@Override
				public void onSuccess(Object t)
				{
					super.onSuccess(t);
					isCanSendMessage = true;
					
					if(t != null)
					{
						try
						{
							JSONObject jsonObject = new JSONObject(t.toString());
							int code = jsonObject.getInt("code");
							if(code == 200)
							{
								if(layoutInputMessage.isShown())
								{
									layoutInputMessage.setVisibility(View.GONE);
									txtChatInputTest.setText("");
								}
							}
								String message = jsonObject.getString("message");
								if(message != null)
								ToastUtil.showStaticToastShort(MainPanelActivity.this, message);
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
			ToastUtil.showStaticToastShort(MainPanelActivity.this, "信息必须不能为空且大于5个字！");
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		isExitApp = true;
		if(timer != null)
		timer.cancel();
		
		if(player != null)
		{
			player.pause();
			player.stop();
			player.release();
			player = null;
		}
	};
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		isExitApp = true;
		if(timer != null)
		timer.cancel();
	};
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		isExitApp = false;
		timerToScauld();
	};
	
	Timer timer;
	boolean isExitApp;
	boolean isCanRequest = true;
	private void timerToScauld()
	{
		String s = ShareDB.getStringFromDB(MainPanelActivity.this, "isOpenPublicChat");
		if("true".equals(s))
		{
			return;
		}
		
		if(timer != null)
			timer.cancel();
		
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				if(!isExitApp)
				{
					timerToGetPM();
				}
			}
		}, 4000, delayTime);
	}
	
	private void timerToGetPM()
	{
		if(!Utils.checkNet(this))
		{
			ToastUtil.showStaticToastShort(MainPanelActivity.this, "当前网络不可用！");
			return ;
		}
		
		if(!isCanRequest)
		{
			return;
		}	
		
		finalHttp.get(API.MESSAGE_REQUEST+"getpublicmessage.action?uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				isCanRequest = true;
				txtChatTestPosition.setText("");
				txtChatTestName.setText("");
				txtChatTest.setText("玄异三国欢迎【"+placeBean.getUserdata().getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				isCanRequest = false;
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				isCanRequest = true;
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						Log.i("Test", "code = = "+code);
//						{
//							String authorname = jsonObject.getString("authorname");
//						}
						String message = jsonObject.getString("message");
						
						if(code == 206)
						{
							txtChatTestPosition.setText("");
							txtChatTestName.setText("");
							txtChatTest.setText("玄异三国欢迎【"+placeBean.getUserdata().getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
						}
						else if(code == 207)
						{
//							String author = "";
//							if(code == 2007)
//							{
//								author = jsonObject.getString("author");
////								txtChatTestName.setText(author);
//							}
							txtChatTestPosition.setText("");
							txtChatTestName.setText("");
							txtChatTest.setText(Html.fromHtml(message));
							Log.i("Test", "teammessage11111333333 :"+jsonObject.toString());
							String teammessage = "";
							try
							{
								teammessage = jsonObject.getString("teammessage");
							}
							catch (Exception e)
							{
								// TODO: handle exception
							}
							if(teammessage != null && !"".equals(teammessage))
							{
								txtTeamMessage.setVisibility(View.VISIBLE);
								txtTeamMessage.setText(teammessage);
							}
							else
							{
								txtTeamMessage.setVisibility(View.GONE);
							}
						}
						else if(code == 200)
						{
							String authorname = jsonObject.getString("authorname");
							String position = jsonObject.getString("position");
							
							layoutChatView.setVisibility(View.VISIBLE);
							
							if(position != null)
								txtChatTestPosition.setText(position+"|");
							if(authorname != null)
								txtChatTestName.setText(authorname+"：");
							
							if(message == null || message.length() == 0)
							{
								txtChatTestPosition.setText("");
								txtChatTestName.setText("");
								txtChatTest.setText("玄异三国欢迎【"+placeBean.getUserdata().getName()+(MyApplication.user.getGender() == 1?" 公子":" 小姐")+"】回来！");
							}
							else
								txtChatTest.setText(Html.fromHtml(message));
							
							String teammessage = jsonObject.getString("teammessage");
							Log.i("Test", "teammessage :"+teammessage);
							if(teammessage != null && !"".equals(teammessage))
							{
								txtTeamMessage.setVisibility(View.VISIBLE);
								txtTeamMessage.setText(teammessage);
							}
							else
							{
								txtTeamMessage.setVisibility(View.GONE);
							}
						}
							
//						else
//						{
//							layoutChatView.setVisibility(View.VISIBLE);
//						}
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

	private void pubWish()
	{
		String content = etContent.getText().toString();
		String name = etMasterName.getText().toString();
		if(content == null || content.length() < 1)
		{
			ToastUtil.show(this, "内容不能为空");
		}
		else
		{
		AjaxParams params = new AjaxParams();
		params.put("content", content);
		params.put("name", name);
		params.put("uuid", MyApplication.getUUID(this));

		finalHttp.post(API.URL_OTHER+"wish.action?", params,new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				ToastUtil.show(MainPanelActivity.this, "投放许愿瓶失败!");
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
						if(message != null)
						{
							ToastUtil.show(MainPanelActivity.this, message);
							if(wishDialog != null)
								wishDialog.dismiss();
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
	}

	private void friendlist()
	{
		ActivityUtil.goToNewActivity(this, FriendListActivity.class);
	}

	private void setting()
	{
		ActivityUtil.goToNewActivity(MainPanelActivity.this, SettingActivity.class);
	}

	private void task()
	{
		ActivityUtil.goToNewActivity(this, TaskManageActivity.class);
	}
	
	Dialog wishDialog;
	EditText etContent;
	EditText etMasterName;
	Button btnChangeName;
	// wish
	private void wish()
	{
		wishDialog = new Dialog(MainPanelActivity.this, R.style.myDialogTheme);
		View view = LayoutInflater.from(MainPanelActivity.this).inflate(R.layout.dialog_wish, null);
		wishDialog.setContentView(view);
			
		Button btnPub = (Button)view.findViewById(R.id.btnPub);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		btnChangeName = (Button)view.findViewById(R.id.btnChange);	
		btnPub.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnChangeName.setOnClickListener(this);
		
//		etTitle = (EditText)view.findViewById(R.id.etTitle);
		btnChangeName = (Button)view.findViewById(R.id.btnChange);
		etContent = (EditText)view.findViewById(R.id.etContent);
		etMasterName = (EditText)view.findViewById(R.id.etMasterName);
		etMasterName.setText(name[random.nextInt(name.length)]);
		
		wishDialog.show();
	}

	private void message()
	{
		if(ErrorHandler.handlerError(this) == false)
			return;
		Intent intent = new Intent(this, MessageListAcrtivity.class);
//		placeBean.getMessage();
		placeBean.setMessage("false");
//		intent.putParcelableArrayListExtra("list", placeBean.getMessage());
		ActivityUtil.goToNewActivityWithComplement(this, intent);
	}

	private void things()
	{
		if(ErrorHandler.handlerError(this) == false)
			return;
		Intent intent = new Intent(this, GoodsListActivty.class);
//		intent.putExtra("descript", "你正值豆蔻年华，青春无限，心中亦满怀大志，在这乱世中创一份家业");
//		intent.putExtra("grade", 20);
//		intent.putExtra("hp", 100);
//		intent.putExtra("name", "解锋镝");
		intent.putExtra("uuid", MyApplication.user.getUuid());
//		intent.putExtra("type", 1);
		
		ActivityUtil.goToNewActivityWithComplement(this, intent);
	}

	private void me()
	{
		if(ErrorHandler.handlerError(this) == false)
			return;
		Intent intent = new Intent(this, PlayerPanelActivity.class);
		intent.putExtra("id", MyApplication.user.getUuid());
		intent.putExtra("type", 1);
		intent.putExtra("placeType", placeBean.getType());
		intent.putExtra("personType", MyApplication.user.getType());
		ActivityUtil.goToNewActivityWithComplement(this, intent);		
	}

	@Override
	public void loadData(int id,int type)
	{
		if(ErrorHandler.handlerError(this) == false || id==-1)
		{
			return;
		}
		
		progressDialogToValid();
		
		String url = null;
		try
		{
			url = API.URL+"getplaceinfoall.action?&name="+URLEncoder.encode(MyApplication.user.getName(),"utf-8")+"&curid="+MyApplication.user.getAreaid()+"&id="+id+"&uuid="+MyApplication.user.getUuid()+"&type="+type+"&token="+MyApplication.user.getToken();
		}
		catch (UnsupportedEncodingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finalHttp.get(url, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onStart()
			{
				super.onStart();
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(Object t)
			{
				super.onSuccess(t);
				progressBar.setVisibility(View.GONE);
				
				if(t != null && !"null".equals(t.toString().trim()))
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							Gson gson = new Gson();
							placeBean = gson.fromJson(t.toString(), PlaceBean.class);
							if(!placeBean.getName().contains("乐台"))
							{
								if(placeBean != null)
								player.pause();
							}
							
							MyApplication.user.setAreaid(placeBean.getAreaid());
							initValues();
						}
						// 有人正在杀你
						else if(code == 201)
						{
							String enemy = jsonObject.getString("enemy");
							String message= jsonObject.getString("message");
							Intent intent = new Intent(MainPanelActivity.this, BattleActivity.class);
							intent.putExtra("attackerid", enemy);
							intent.putExtra("defencerid", MyApplication.getUUID(MainPanelActivity.this));
							ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
						}
						// 更新提示
						else if(code == 405)
						{
							Builder builder = new Builder(MainPanelActivity.this);
							builder.setTitle("应用提示");
							builder.setCancelable(false);
							builder.setMessage(jsonObject.getString("message"));
							builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									MyApplication.exitAPP();
								}
							});
							builder.show();
						}
						else if(code == 1024)
						{
							Builder builder = new Builder(MainPanelActivity.this);
							builder.setTitle("应用提示");
							builder.setCancelable(false);
							builder.setMessage(jsonObject.getString("message"));
							builder.setNegativeButton("退出游戏", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									MyApplication.exitAPP();
								}
							});
							builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									ActivityUtil.goToNewActivity(MainPanelActivity.this, LoginActivity.class);
								}
							});

							builder.show();
						}
						// 显示提示文字
						else if(code == 1025)
						{
							String mes = jsonObject.getString("message");
							if(mes != null)
								ToastUtil.showStaticToastShort(MainPanelActivity.this, mes);
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
	
	// defence system
	private void progressDialogToValid()
	{
		long lasttime = ShareDB.getLongFromDB(this, "lasttime");
		
		if(System.currentTimeMillis() - lasttime >= 1800000)//1800000)
		{
			ShareDB.save2DB(this, "lasttime", System.currentTimeMillis());
			MValidDialog dialog = new MValidDialog(this, R.style.myDialogTheme);
			Window dialogWindow = dialog.getWindow();
		    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		    lp.width = -1;
		    lp.height = -2;
		    dialogWindow.setAttributes(lp);
			Random random = new Random();
			int i = random.nextInt(qa.length);
			dialog.setQandA(qa[i][0], qa[i][1]);
			dialog.show();
		}
	}

	private void initValues()
	{
		String east = placeBean.getEastname();
		
		if(east == null || "".equals(east))
		{
			//txtEast.setText("东:没有路了");
			txtEast.setVisibility(View.INVISIBLE);
		}
		else
		{
			east = "东:"+east;
			txtEast.setVisibility(View.VISIBLE);
			txtEast.setText(east);
		}
		
		String west = placeBean.getWestname();
		if(west == null || "".equals(west.trim()))
		{
//			txtWest.setText("西:没有路了");
			txtWest.setVisibility(View.INVISIBLE);
		}
		else
		{
			west = "西:"+west;
			txtWest.setVisibility(View.VISIBLE);			
			txtWest.setText(west);
		}
		
		
		String south = placeBean.getSouthname();
		if(south == null || "".equals(south))
		{
//			txtSouth.setText("南:没有路了");
			txtSouth.setVisibility(View.INVISIBLE);
		}
		else
		{
			south = "南:"+south;
			txtSouth.setVisibility(View.VISIBLE);
			txtSouth.setText(south);
		}
		
		String north = placeBean.getNorthname();
		if(north == null || "".equals(north))
		{
//			txtNorth.setText("北:没有路了");
			txtNorth.setVisibility(View.INVISIBLE);
		}	
		else
		{
			north = "北:"+north;
			txtNorth.setVisibility(View.VISIBLE);
			txtNorth.setText(north);
		}
		
		txtDescript.setText(placeBean.getDescript());
		txtName.setText(" [  "+placeBean.getName()+"  ] ");
//		txtPlaceName.setText(placeBean.getName());
		txtCenter.setText(placeBean.getName());
		txtNotice.setVisibility(View.GONE);
		lvAchieveMessage.setVisibility(View.GONE);
		lvLeaveMessage.setVisibility(View.GONE);
		
		if(placeBean.getAchievelist() != null && placeBean.getAchievelist().size() > 0)
		{
			lvAchieveMessage.setVisibility(View.VISIBLE);
			AchieveMessageAdapter adapter = new AchieveMessageAdapter(this, placeBean.getAchievelist());
			lvAchieveMessage.setAdapter(adapter);
		}
		
		// 交易
		if("true".equals(placeBean.getTransaction()))
		{
			loadTransaction();
		}
		
		// 组队
		if("true".equals(placeBean.getInvitation()))
		{
			loadInvitation();
		}
		
		if(placeBean.getLeavelist() != null && placeBean.getLeavelist().size() > 0)
		{
			lvLeaveMessage.setVisibility(View.VISIBLE);
			LeaveMessageAdapter adapter = new LeaveMessageAdapter(this, placeBean.getLeavelist());
			lvLeaveMessage.setAdapter(adapter);
		}
		
		if(placeBean.getNotice().getNotice() != null && !"".equals(placeBean.getNotice().getNotice().trim()) )
		{
			txtNotice.setVisibility(View.VISIBLE);
			
			if("管理员".equals(placeBean.getNotice().getAuthor()))
			{
				txtNotice.setTextColor(getResources().getColor(R.color.red));
				txtNotice.setBackgroundColor(getResources().getColor(R.color.black_light_title_text_color));
			}
			else if("解锋镝".equals(placeBean.getNotice().getAuthor()))
			{
				txtNotice.setTextColor(getResources().getColor(R.color.white));
				txtNotice.setBackgroundColor(getResources().getColor(R.color.blue));
			}
			else
			{
				txtNotice.setTextColor(getResources().getColor(R.color.white));
				txtNotice.setBackgroundColor(getResources().getColor(R.color.green));
			}
			
			String temp = placeBean.getNotice().getAuthor();
			if(temp != null && temp.length() > 0)
				temp = "【"+temp+"】:";
			else
				temp = "";
			
			txtNotice.setText("【系统公告】"+temp+placeBean.getNotice().getNotice());
		}
		
		if(placeBean.getNpc() != null && placeBean.getNpc().size() > 0)
		{
			layoutSomeBody.setVisibility(View.VISIBLE);			
			adapterNPC = new GridviewAdapter(this, 0);
			adapterNPC.setNPCList(placeBean.getNpc());
			numberNPC.setText(Html.fromHtml("你遇到了: [ <font color='red'>"+placeBean.getNpc().size()+"</font> ] 人"));
			gvNPC.setAdapter(adapterNPC);
		}
		else
		{
			layoutSomeBody.setVisibility(View.GONE);
		}
		
//		if(placeBean.get)
		
		if(placeBean.getPlayer() != null && placeBean.getPlayer().size() > 0)
		{
			layoutMetSomeBody.setVisibility(View.VISIBLE);
			if(placeBean.getPlayer().size() == 1)
				txtNearByNumber.setText("你独自一个人孤单地站在此地:");
			else
				txtNearByNumber.setText(Html.fromHtml("你身边有: [ <font color='red'>"+(placeBean.getPlayer().size()-1)+"</font> ] 人"));
			
			layoutMetSomeBody.setVisibility(View.VISIBLE);			
			adapterPerson = new GridviewAdapter(this, 1);
			adapterPerson.setPlayerList(placeBean.getPlayer());
			
			gvPerson.setAdapter(adapterPerson);
		}
		else
		{
			layoutMetSomeBody.setVisibility(View.GONE);
		}
		
		if(placeBean.getGoods() != null && placeBean.getGoods().size() > 0)
		{
			layoutSomething.setVisibility(View.VISIBLE);
			adapterGoods = new GoodsAdapter(MainPanelActivity.this, placeBean.getGoods());
			numberGoods.setText(Html.fromHtml("地上有: [ <font color='red'>"+placeBean.getGoods().size()+"</font> ] 个物品"));
			gvGoods.setAdapter(adapterGoods);
		}
		else
		{
			layoutSomething.setVisibility(View.GONE);
		}
		
		if(placeBean.getMessage() != null && "true".equals(placeBean.getMessage().trim()))
		{
//			btnMessage.setBackgroundColor(getResources().getColor(R.color.red));
			btnMessage.setBackground(getResources().getDrawable(R.drawable.massage_selector));
			btnMessage.setTextColor(getResources().getColor(R.color.red));
			//ivMessagePoint.setVisibility(View.VISIBLE);
		}
		else
		{
			btnMessage.setTextColor(getResources().getColor(R.color.blue_two));
			btnMessage.setBackground(getResources().getDrawable(R.drawable.edittext_selecctor));
			//ivMessagePoint.setVisibility(View.GONE);
		}
		
		layoutFunction.setVisibility(View.GONE);
		txtFunction01.setVisibility(View.GONE);
		txtFunction02.setVisibility(View.GONE);
		txtFunction03.setVisibility(View.GONE);
		txtFunction04.setVisibility(View.GONE);
		
		if(placeBean.getFunction1() != null && !"".equals(placeBean.getFunction1()))
		{
			layoutFunction.setVisibility(View.VISIBLE);

			if(placeBean.getFunction1() != null && !"".equals(placeBean.getFunction1()))
			{
				txtFunction01.setVisibility(View.VISIBLE);
				txtFunction01.setText(placeBean.getFunction1());
				txtFunction01.setOnClickListener(new FunctionOpeartion(1,placeBean.getFunction1()));
			}
			else
			{
				txtFunction01.setVisibility(View.INVISIBLE);
			}
			
			if(placeBean.getFunction2() != null  && !"".equals(placeBean.getFunction2()))
			{
				txtFunction02.setVisibility(View.VISIBLE);
				txtFunction02.setText(placeBean.getFunction2());
				txtFunction02.setOnClickListener(new FunctionOpeartion(2,placeBean.getFunction2()));				
			}
			else
			{
				txtFunction02.setVisibility(View.INVISIBLE);
			}
			
			if(placeBean.getFunction3() != null  && !"".equals(placeBean.getFunction3()))
			{
				txtFunction03.setVisibility(View.VISIBLE);
				txtFunction03.setText(placeBean.getFunction3());
				txtFunction03.setOnClickListener(new FunctionOpeartion(3,placeBean.getFunction3()));
			}
			else
			{
				txtFunction03.setVisibility(View.INVISIBLE);
			}
			
			if(placeBean.getFunction4() != null  && !"".equals(placeBean.getFunction4()))
			{
				txtFunction04.setVisibility(View.VISIBLE);
				txtFunction04.setText(placeBean.getFunction4());
				txtFunction04.setOnClickListener(new FunctionOpeartion(4,placeBean.getFunction4()));				
			}
			else
			{
				txtFunction04.setVisibility(View.INVISIBLE);
			}
		}
		
		if(placeBean.getBattle_string() != null && !"".equals(placeBean.getBattle_string()))
		{
			String islookbattle = ShareDB.getStringFromDB(this, "islookbattle");
			if(islookbattle == null || "false".equals(islookbattle))
			{
				txtBattleDescript.setVisibility(View.VISIBLE);
			}
			else
			{
				txtBattleDescript.setVisibility(View.GONE);
			}
			
			txtBattleDescript.setText(Html.fromHtml(placeBean.getBattle_string()));
		}
		else
		{
			txtBattleDescript.setText("");
		}
		
				String temp = placeBean.getUserdata().getDialog();//.getPlayer().get(i).getDialog();
				MyApplication.user = placeBean.getUserdata();
				
					if(temp != null && !"null".equals(temp) && (temp.length() > 8))
					{
						Builder builder = new Builder(MainPanelActivity.this,R.style.dialogThemeForIOS);
						builder.setPositiveButton("收起战报", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								// TODO Auto-generated method stub
							}
						});
						builder.setMessage(Html.fromHtml(temp));
						builder.show();
					}
	}
	
	private void loadInvitation()
	{
		finalHttp.get(API.TEAM_REQUEST+"getinvitation.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
						int code = jsonObject.getInt("code");
						if(code == 200)
						{
							Builder builder = new Builder(MainPanelActivity.this);
							builder.setTitle("组队邀请");
							String mes = jsonObject.getString("mess");
							
							
							if(mes != null)
							builder.setMessage(mes);
							
							final String teammasterid = jsonObject.getString("teammasterid");
							final String teammemberid = jsonObject.getString("teammemberid");
							
							Log.i("XU", "teammasterid="+teammasterid+"   teammemberid="+teammemberid);
							Log.i("XU", "邀请数据"+t.toString());
//							final String mess = jsonObject.getString("mess");
							
							builder.setPositiveButton("同意入队",new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									finalHttp.get(API.TEAM_REQUEST+"responeBuildTeam.action?&teammaster="+teammasterid+"&member="+teammemberid+"&type=1", new AjaxCallBack<Object>()
									{
										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
											Log.i("XU", "失败+"+strMsg);
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
													if(mes!=null)
														ToastUtil.showStaticToastShort(MainPanelActivity.this, mes);
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
							});
							builder.setNegativeButton("拒绝加入",new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									// TODO Auto-generated method stub
									finalHttp.get(API.TEAM_REQUEST+"responeBuildTeam.action?&teammaster="+teammasterid+"&member="+teammemberid+"&type=2", new AjaxCallBack<Object>()
									{
										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
											Log.i("XU", "失败+"+strMsg);
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
													if(mes!=null)
														ToastUtil.showStaticToastShort(MainPanelActivity.this, mes);
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
							});
							builder.show();
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

	EditText etReplay;
//	String replayTitleString = "";
	Dialog showWishDialog;
	private void getWishs()
	{
		finalHttp.get(API.URL_OTHER+"getwish.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
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
				dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "获取中...");
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
						String mes = jsonObject.getString("message");
//						replayTitleString = "";
//						if(mes != null)
//						{
//							replayTitleString = (mes.length() > 6 ? mes.substring(0, 5):mes);
//						}
						if(code == 200)
						{
							final String muuid = jsonObject.getString("uuid");
							String mname = jsonObject.getString("name");
							showWishDialog = new Dialog(MainPanelActivity.this, R.style.myDialogTheme);
							View view = LayoutInflater.from(MainPanelActivity.this).inflate(R.layout.dialog_show_wish_view, null);
							etReplay = (EditText) view.findViewById(R.id.etReplay);
							Button btnReplay = (Button) view.findViewById(R.id.btnReplay);
							Button btnBack = (Button)view.findViewById(R.id.btnBack);
							MTextView txtContent = (MTextView) view.findViewById(R.id.txtContent);
							MTextView txtTitle = (MTextView) view.findViewById(R.id.txtTitle);
							
							btnReplay.setOnClickListener(new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									String replay = etReplay.getText().toString();
									if(replay != null && replay.length() >= 5)
									{
										if(showWishDialog != null) showWishDialog.dismiss();
										AjaxParams params = new AjaxParams();
										params.put("uuid", muuid);
//										params.put("title", replayTitleString);
										params.put("message", replay);
										finalHttp.post(API.URL_OTHER+"replaywish.action?",params, new AjaxCallBack<Object>()
										{
											@Override
											public void onFailure(Throwable t, int errorNo, String strMsg)
											{
												// TODO Auto-generated method stub
												super.onFailure(t, errorNo, strMsg);
												ToastUtil.showShort(MainPanelActivity.this, "回复失败，网络异常");
											}

											@Override
											public void onSuccess(Object t)
											{
												// TODO Auto-generated method stub
												super.onSuccess(t);
												if(t != null)
												{
													ToastUtil.showShort(MainPanelActivity.this, "回复瓶主成功");
												}
											}
										});
									}
									else
										ToastUtil.showShort(MainPanelActivity.this, "许愿至少要5个字");
								}
							});
							btnBack.setOnClickListener(new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									// TODO Auto-generated method stub
									if(showWishDialog != null) showWishDialog.dismiss();
								}
							});
							txtTitle.setText("* ["+mname+"] 的愿望*");
							txtContent.setText(Html.fromHtml(mes));
							showWishDialog.setContentView(view);
							showWishDialog.show();
						}
						else
						{
							ToastUtil.showStaticToast(MainPanelActivity.this, mes);
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
	
	String tid;
	private void loadTransaction()
	{
		finalHttp.get(API.TRANSACTION+"gettransactionlist.action?&buyer_uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				//ToastUtil.show(MainPanelActivity.this, "获取交易信息失败");
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
						JSONArray array = new JSONArray(t.toString());
//						for (int i = 0; i < array.length(); i++)
						{
							if(array.length() == 0)
								return;
							JSONObject jsonObject = array.getJSONObject(0);
							Builder builder = new Builder(MainPanelActivity.this);
							builder.setTitle("交易提醒");
							builder.setMessage("【"+jsonObject.getString("seller_name")+"】想把【"+jsonObject.getString("gname")+"】以【"+jsonObject.getLong("price")+"】两白银卖给你。");
							tid = jsonObject.getString("tid");
							builder.setNegativeButton("接受交易", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									finalHttp.get(API.TRANSACTION+"agree.action?&tid="+tid+"&buyer_uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
									{

										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
											ToastUtil.show(MainPanelActivity.this, "交易失败!");
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
														ToastUtil.show(MainPanelActivity.this, mes);
												}
												catch (JSONException e)
												{
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}

										@Override
										public void onStart()
										{
											// TODO Auto-generated method stub
											super.onStart();
										}
									});
								}
							});
							builder.setPositiveButton("拒绝交易", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									finalHttp.get(API.TRANSACTION+"refuse.action?&tid="+tid+"&buyer_uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
									{

										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{
											// TODO Auto-generated method stub
											super.onFailure(t, errorNo, strMsg);
										}



										@Override
										public void onSuccess(Object t)
										{
											// TODO Auto-generated method stub
											super.onSuccess(t);
										}



										@Override
										public void onStart()
										{
											// TODO Auto-generated method stub
											super.onStart();
										}
									});
								}
							});
							builder.setCancelable(false);
							builder.show();
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
	
	private void doWaKuangOrHuilu(int type)
	{
		// 1|挖矿  2|贿赂牢头
		finalHttp.get(API.URL_OTHER+"dowakuangorhuilu.action?&uuid="+MyApplication.getUUID(this)+"&type="+type, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(Object t)
			{
				super.onSuccess(t);
				progressBar.setVisibility(View.GONE);
				if(t != null)
				{
					JSONObject jsonObject;
					try
					{
						jsonObject = new JSONObject(t.toString());
						int code = jsonObject.getInt("code");
						String mes = jsonObject.getString("message");
						if(code == 200)
						{
							loadData(MyApplication.user.getAreaid(),0);
						}
						Builder builder = new Builder(MainPanelActivity.this);
						if(mes != null)
						builder.setMessage(mes);
						builder.setPositiveButton("知道了", null);
						builder.show();
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
	
	private void juanqian()
	{
		Builder builder = new Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_number_edittext, null);
		final EditText etSelectNumber = (EditText) view.findViewById(R.id.etSelectNumber);
		builder.setTitle("官府告示:");
		builder.setMessage("最低捐献一两，十两为一阶段，一阶段为1功勋，例如：十九两不足二十，按十两计算，低于十两无功勋。");
		etSelectNumber.setHint("最低捐献一两");
		builder.setView(view);
		builder.setNegativeButton("我不捐了", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		builder.setPositiveButton("捐钱报国", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//String uuid,String gid,String number,String areaid)
				String str = etSelectNumber.getText().toString();
				if(str == null || str.length() < 1)
				{
					ToastUtil.showShort(MainPanelActivity.this, "请输入数额");
					return;
				}
				long money = 1;
				try
				{
					money = Long.valueOf(str);
				}
				catch (Exception e)
				{
					ToastUtil.showShort(MainPanelActivity.this, "格式不正确");
				}

				finalHttp.get(API.TRANSACTION+"donatemoney.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)+"&countryid="+placeBean.getCountryid()+"&money="+money, new AjaxCallBack<Object>()
				{
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Log.i("XU", "失败"+strMsg);
						ToastUtil.showShort(MainPanelActivity.this, "操作失败:错误码"+errorNo);
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
								if(message != null)
								{
									Builder builder = new Builder(MainPanelActivity.this);
									builder.setTitle(placeBean.getCountryname()+"府衙官文通告");
									builder.setMessage(message);
									builder.setNegativeButton("知道了", null);
									builder.show();
								}
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//Log.i("XU", "成功"+t.toString());
					}
				});
			}
		});
		builder.show();
	}

	private class FunctionOpeartion implements OnClickListener
	{
		int type;
		String name;
		
		public FunctionOpeartion(int type,String name)
		{
			this.name = name;
			this.type = type;
		}

		@Override
		public void onClick(View v)
		{
			if("论坛".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, ForumActivity.class);
			}
			else if("排行榜".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, RankingListActivity.class);
			}
			else if("投许愿瓶".equals(name))
			{
				wish();
			}
			else if("捞许愿瓶".equals(name))
			{
				getWishs();
			}
			else if("挖矿".equals(name))
			{
				doWaKuangOrHuilu(1);
			}
			else if("迷城争霸战".equals(name))
			{
				Builder builder = new Builder(MainPanelActivity.this);
				builder.setTitle("提示");
				builder.setMessage("参与迷城争霸战需要交纳5两银子做为奖池基金，用以奖励最终得胜的王者。在杀光其他所有入城争霸者之后，可以占据王座，获得迷城所有入注的资金和功勋等，还有概率可以获得宝石。");
				builder.setPositiveButton("我要参加", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						michengTask(API.URL_TASK+"michengzhengbazhan.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this),0);
					}
				});
				builder.setNegativeButton("不参加了", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						
					}
				});
				builder.show();
			}
			else if("缔命契约".equals(name))
			{
				Builder builder = new Builder(MainPanelActivity.this);
				builder.setTitle("提示");
				builder.setMessage("一旦缔约将会清楚当前的经验值，并设置下级经验扩大10倍，仅限于15级的玩家账号。");
				builder.setPositiveButton("确定缔约", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						michengTask(API.URL_TASK+"diyue.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this),0);
					}
				});
				builder.setNegativeButton("取消缔约", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						
					}
				});
				builder.show();
			}
			else if("占据王座".equals(name))
			{
				michengTask(API.URL_TASK+"takewangzuo.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this),1);
			}
			else if("贿赂牢头".equals(name))
			{
				doWaKuangOrHuilu(2);
			}
			else if("入场玩玩".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, CasinoSelectModeActivity.class);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("装备评估".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, GoodsListActivty.class);
				intent.putExtra("activityType", 1026);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("维修装备".equals(name))
			{
				ToastUtil.showShort(MainPanelActivity.this, "请选择要维修的装备");
				Intent intent = new Intent(MainPanelActivity.this, GoodsListActivty.class);
				intent.putExtra("activityType", 1025);
				intent.putExtra("uuid", MyApplication.getUUID(MainPanelActivity.this));
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("国战捐钱".equals(name))
			{
				juanqian();
			}
			else if("听乐".equals(name))
			{
				changeSong(1);
			}
			else if("兑换戒指".equals(name))
			{
				duihuanjiezhi();
			}
			else if("升级装备".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, GoodsListActivty.class);
				intent.putExtra("activityType", 1027);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("扔钱打赏".equals(name))
			{
				ToastUtil.showStaticToastShort(MainPanelActivity.this, "点击自己的行囊，点击金银铜三类，选择丢弃，打赏多少随君意。");
			}
			else if("观舞".equals(name))
			{
				ToastUtil.showStaticToastShort(MainPanelActivity.this, "舞姬们今天不舒服");
			}
			else if("我要坐庄".equals(name))
			{
				metohousemaster();
			}
			else if("前往".equals(name))
			{
				Builder builder = new Builder(MainPanelActivity.this);
				builder.setTitle("选择前往的城市: 传送费50铜板");
				builder.setItems(new String[]{"洛阳","南阳","邺城","许昌","长安","新手村"}, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						String city = "003";
						
						switch (which)
						{
							case 0:city="221";cityName="洛阳";break;
							case 1:city="111";cityName="南阳";break;
							case 2:city="521";cityName="邺城";break;
							case 3:city="334";cityName="许昌";break;
							case 4:city="421";cityName="长安";break;

							default:
								city = "003";
								name="新手村";
								break;
						}
						//http://wlgac420108.jsp.jspee.org/requestAction/getplaceinfoall.action?&name=%E7%8E%84%E5%90%8C%E5%A4%AA%E5%AD%90&curid=111&id=221&uuid=0001&type=1&version=1

						try
						{
							String url = API.URL+"getplaceinfoall.action?&name="+URLEncoder.encode(MyApplication.user.getName(),"utf-8")+"&curid="+MyApplication.user.getAreaid()+"&id="+city+"&uuid="+MyApplication.user.getUuid()+"&type="+2+"&token="+MyApplication.user.getToken();
							finalHttp.get(url, new AjaxCallBack<Object>()
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
									dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "正在前往"+cityName);
									dialog.show();
								}

								@Override
								public void onSuccess(Object t)
								{
									// TODO Auto-generated method stub
									super.onSuccess(t);
									dialog.dismiss();
									Log.i("XU", "移动数据 = "+t);
									if(t != null)
									{
										try
										{
											JSONObject jsonObject = new JSONObject(t.toString());
											int code = jsonObject.getInt("code");
											
											if(code == 200)
											{
												Gson gson = new Gson();
												placeBean = gson.fromJson(t.toString(), PlaceBean.class);
												MyApplication.user.setAreaid(placeBean.getAreaid());
												initValues();
											}
											else if(code == 202)
											{
												ToastUtil.show(MainPanelActivity.this, jsonObject.getString("message"));
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
						catch (UnsupportedEncodingException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
								
					}
				});
				builder.show();
			}
			else if("住店休息".equals(name))
			{
				rest();
				SoundPlayerUtil.getInstance(MainPanelActivity.this).play("wakeup");
			}
			else if("出战".equals(name))
			{
				chuzhan();
			}
			else if("退兵".equals(name))
			{
				tuibing();
			}
			else if("占领".equals(name))
			{
				zhanling();
			}
			else if("打听消息".equals(name))
			{
				ToastUtil.showStaticToast(MainPanelActivity.this, tipMessage[random.nextInt(tipMessage.length)]);
			}
			else if("垂钓".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, FishingActivity.class);
			}
			else if("买入".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, BuyGoodsList.class);
				intent.putExtra("type", 1);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("买药".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, DrugsActivity.class);
			}
			else if("黑市".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, BlackMarketActivity.class);
			}
			else if("卖出".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, BuyGoodsList.class);
				intent.putExtra("type", 2);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("加入".equals(name))
			{
				String cid = placeBean.getCountryid();
				String cname= placeBean.getCountryname();
				String uid = placeBean.getUserdata().getCountryid();
				
				if(cid != null && uid!=null)
				{
					if(cid.equals(uid))
					{
						ToastUtil.show(MainPanelActivity.this, "你已是【"+cname+"】民众，不要重复加入!");
					}
					else
					{
						Builder builder = new Builder(MainPanelActivity.this);
						StringBuilder builderString = new StringBuilder();
						builderString.append("[入城缔约]:\n\n");
						builderString.append("1.不得以任何手段引入敌人残害本城国民。\n");
						builderString.append("2.无条件维护本城利益,义务参加攻防国战。\n");
						builderString.append("3.如果叛城另投他城,官职将会降低一级（除非得到王侯的推荐而加入）。\n");
						builder.setPositiveButton("我遵守条约", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								joinCity(MyApplication.getUUID(MainPanelActivity.this),placeBean.getCountryid());
							}
						});
						builder.setNegativeButton("我反悔了", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								// TODO Auto-generated method stub
								
							}
						});
						builder.setMessage(builderString);
						builder.setTitle("城约");
						builder.show();
					}
				}
			}
			else if("存物".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, AccessMoneyOrThingsActivity.class);
				intent.putExtra("type", 3);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("取物".equals(name))
			{
				Intent intent = new Intent(MainPanelActivity.this, AccessMoneyOrThingsActivity.class);
				intent.putExtra("type", 4);
				ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
			}
			else if("悬赏单".equals(name))
			{
				ActivityUtil.goToNewActivity(MainPanelActivity.this, XuanShangActivity.class);
			}
			else if("攻击城门".equals(name) || "攻击旗楼".equals(name))
			{
				if(placeBean.getCountryid().equals(MyApplication.user.getCountryid()))
				{
					ToastUtil.show(MainPanelActivity.this, "你不能攻击己方防御。");
					return;
				}
				
				if(System.currentTimeMillis() - lastTime < 1000)
				{
					ToastUtil.showStaticToast(MainPanelActivity.this,"你累的气喘吁吁地，不得不停下休息一会");
				}
				else
				{
					lastTime = System.currentTimeMillis();
					finalHttp.get(API.URL4+"attack.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)+"&countryid="+placeBean.getCountryid()+"&doorareaid="+placeBean.getAreaid(), new AjaxCallBack<Object>()
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
											ToastUtil.showStaticToast(MainPanelActivity.this,jsonObject.getString("message") );
											int code = jsonObject.getInt("code");
											// 有人正在杀你
											if(code == 201)
											{
												String enemy = jsonObject.getString("enemy");
												if(enemy != null)
												{
													Intent intent = new Intent(MainPanelActivity.this, BattleActivity.class);
													intent.putExtra("attackerid", enemy);
													intent.putExtra("defencerid", MyApplication.getUUID(MainPanelActivity.this));
													ActivityUtil.goToNewActivityWithComplement(MainPanelActivity.this, intent);
												}
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
				}
			else if("领军饷".equals(name))
			{
				finalHttp.get(API.TRANSACTION+"receivesalary.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)+"&country="+placeBean.getCountryid(), new AjaxCallBack<Object>()
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
						dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "获取中...");
						dialog.show();
					}

					@Override
					public void onSuccess(Object t)
					{
						// TODO Auto-generated method stub
						super.onSuccess(t);
						dialog.dismiss();
						if(t!= null)
						{
							try
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								String mes = jsonObject.getString("message");
								if(mes!=null)
									ToastUtil.showStaticToast(MainPanelActivity.this, mes);
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.i("XU", "e = "+e.toString());
							}
						}
					}
				});
			}
		}
		


		private void duihuanjiezhi()
		{
			//API.URL_TASK+"michengzhengbazhan.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)
			finalHttp.get(API.URL2+"duihuan.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
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
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "连接服务器中...");
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
							String mes = jsonObject.getString("message");
							int code = jsonObject.getInt("code");
							if(mes!= null)
							{
								if(code != 200)
								{
									ToastUtil.showStaticToast(MainPanelActivity.this, mes);
//									ToastUtil.showStaticToast(MainPanelActivity.this, mes);
								}
								else
								{
									Builder builder = new Builder(MainPanelActivity.this);
									builder.setTitle("提示");
									if(mes!=null)
									builder.setMessage(mes);
									builder.setPositiveButton("知道了", null);
									builder.show();
								}
								
								loadData(MyApplication.user.getAreaid(),0);
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

		private void michengTask(String url,final int type)
		{
			//API.URL_TASK+"michengzhengbazhan.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)
			finalHttp.get(url, new AjaxCallBack<Object>()
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
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "连接服务器中...");
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
							String mes = jsonObject.getString("message");
							int code = jsonObject.getInt("code");
							if(mes!= null)
							{
								if(code != 300)
								{
									if(type == 0)
										ToastUtil.showStaticToastShort(MainPanelActivity.this, mes);
									else
										ToastUtil.showStaticToast(MainPanelActivity.this, mes);
								}
								else
								{
									Builder builder = new Builder(MainPanelActivity.this);
									builder.setTitle("提示");
									if(mes!=null)
									builder.setMessage(mes);
									builder.setPositiveButton("知道了", null);
									builder.show();
								}
								
								loadData(MyApplication.user.getAreaid(),0);
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

		private void metohousemaster()
		{
			finalHttp.get(API.URL_OTHER+"metohousemaster.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)+"&money=1&type=1", new AjaxCallBack<Object>()
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
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "获取信息中...");
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
							// 钱不够
							if(code == 202)
							{
								String message = jsonObject.getString("message");
								if(message != null)
									ToastUtil.showStaticToast(MainPanelActivity.this, message);
							}
							else if(code == 200)
							{
								ActivityUtil.goToNewActivity(MainPanelActivity.this, MyselfHouserActivity.class);
							}
							// 输入坐庄金额
							else if(code == 201)
							{
								String message = jsonObject.getString("message");
								if(message != null)
								{
									
								
								Builder builder = new Builder(MainPanelActivity.this);
								View view = LayoutInflater.from(MainPanelActivity.this).inflate(R.layout.dialog_select_number_edittext, null);
								final EditText etSelectNumber = (EditText) view.findViewById(R.id.etSelectNumber);
								builder.setTitle("赌坊提示:");
								builder.setMessage(message);
								etSelectNumber.setHint("最低二百两");
								builder.setView(view);
								builder.setNegativeButton("取消行动", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										// TODO Auto-generated method stub
										
									}
								});
								
								builder.setPositiveButton("我确定了", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										//String uuid,String gid,String number,String areaid)
										String str = etSelectNumber.getText().toString();
										if(str == null || str.length() < 1)
										{
											ToastUtil.showShort(MainPanelActivity.this, "请输入数额");
											return;
										}
										long money = 200;
										try
										{
											money = Long.valueOf(str);
										}
										catch (Exception e)
										{
											ToastUtil.showShort(MainPanelActivity.this, "格式不正确");
										}

										finalHttp.get(API.URL_OTHER+"metohousemaster.action?&uuid="+MyApplication.getUUID(MainPanelActivity.this)+"&money="+money+"&type=2", new AjaxCallBack<Object>()
										{
											@Override
											public void onFailure(Throwable t, int errorNo, String strMsg)
											{
												// TODO Auto-generated method stub
												super.onFailure(t, errorNo, strMsg);
												ToastUtil.showShort(MainPanelActivity.this, "操作失败:错误码"+errorNo);
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
														if(message != null)
														{
															Builder builder = new Builder(MainPanelActivity.this);
															builder.setTitle("提示");
															builder.setMessage(message);
															builder.setNegativeButton("知道了", null);
															builder.show();
														}
													}
													catch (JSONException e)
													{
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
												//Log.i("XU", "成功"+t.toString());
											}
										});
									}
								});
								builder.show();
								}
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

		private void zhanling()
		{
			finalHttp.get(API.URL4+"zhanling.action?uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
			{
				Dialog dialog;
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					ToastUtil.showStaticToastShort(MainPanelActivity.this, "出现异常!!!", ToastUtil.ERROR);
					dialog.dismiss();
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "出战提交请求中...");
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
							String mess = jsonObject.getString("message");
							if(code == 200)
							{
								if(mess != null)
								{
									Builder report = new Builder(MainPanelActivity.this);
									report.setTitle("太守府军报");
									report.setMessage(mess);
									report.setPositiveButton("好的", new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											// TODO Auto-generated method stub
											
										}
									});
									report.show();
								}
							}
							else
							{
								ToastUtil.showStaticToastShort(MainPanelActivity.this, mess, ToastUtil.ERROR);
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
		
		private void tuibing()
		{
			finalHttp.get(API.URL4+"battleout.action?uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
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
				public void onLoading(long count, long current)
				{
					// TODO Auto-generated method stub
					super.onLoading(count, current);
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "出战提交请求中...");
					dialog.show();					
				}

				@Override
				public void onSuccess(Object t)
				{
					// TODO Auto-generated method stub
					super.onSuccess(t);
					dialog.dismiss();
					JSONObject jsonObject;
					try
					{
						jsonObject = new JSONObject(t.toString());
						String string  = jsonObject.getString("message");
						if(string != null)
						{
							ToastUtil.show(MainPanelActivity.this, string);
							loadData(MyApplication.user.getAreaid(),0);								
						}
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		
		private void chuzhan()
		{
			Builder builder = new Builder(MainPanelActivity.this);
			builder.setTitle("出战军法约束");
			builder.setMessage("1.为防范出战时的逃兵行为，或恶意占据要塞，施行制裁，必须先缴纳10两银子保证金才能领兵进入要塞（占领要塞会返还抵押的10银,出战超出时间或战死或逃亡则没收）\n\n 2.可以多备写药品，因为战斗中若阵亡，无法再回要塞。\n\n 3.切不可出战后逃亡，若有事可以点退兵，如果传送回客栈视为逃亡将会扣除10点功勋,不足则会在充盈时扣除");
			builder.setPositiveButton("遵守军法出战！", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					finalHttp.get(API.URL4+"battle.action?uuid="+MyApplication.getUUID(MainPanelActivity.this), new AjaxCallBack<Object>()
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
								public void onLoading(long count, long current)
								{
									// TODO Auto-generated method stub
									super.onLoading(count, current);
									dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "出战提交请求中...");
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
										JSONObject jsonObject;
										try
										{
											jsonObject = new JSONObject(t.toString());
											String string  = jsonObject.getString("message");
											if(string != null)
											{
												ToastUtil.show(MainPanelActivity.this, string);
												loadData(MyApplication.user.getAreaid(),0);								
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
			});
			builder.setNegativeButton("我放弃出战", null);
			builder.show();
		}
		
		
		private void joinCity(String uuid, String countryid)
		{
			finalHttp.get(API.URL+"join.action?&uuid="+uuid+"&countryid="+countryid, new AjaxCallBack<Object>()
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
					dialog = MessageDialog.createLoadingDialog(MainPanelActivity.this, "已经提交加入申请");
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
						JSONObject jsonObject;
						try
						{
							jsonObject = new JSONObject(t.toString());
							String string  = jsonObject.getString("message");
							if(string != null)
								ToastUtil.show(MainPanelActivity.this, string);
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


		private void rest()
		{
			finalHttp.get(API.URL+"rest.action?&uuid="+MyApplication.user.getUuid(), new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
				}

				@Override
				public void onSuccess(Object t)
				{
					// TODO Auto-generated method stub
					super.onSuccess(t);
					if(t != null)
					{
						JSONObject jsonObject;
						try
						{
							jsonObject = new JSONObject(t.toString());
							int code = jsonObject.getInt("code");
							String mes =  jsonObject.getString("message");
							if(mes != null)
							ToastUtil.showStaticToast(MainPanelActivity.this,mes);
							
							if(code == 1024)
							{
								exitAPP();
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
	{
		if(parent.getId() == R.id.gvNPC)
		{
			player npc = placeBean.getNpc().get(position);
			Intent intent = new Intent(this, PlayerPanelActivity.class);
			intent.putExtra("id", npc.getUuid());
			intent.putExtra("type", 0);
			intent.putExtra("personType",npc.getType());
			intent.putExtra("placeType", placeBean.getType());

			ActivityUtil.goToNewActivityWithComplement(this, intent);
		}
		else if(parent.getId() == R.id.gvPerson)
		{
			player player = placeBean.getPlayer().get(position);
			Intent intent = new Intent(this, PlayerPanelActivity.class);
			intent.putExtra("id", player.getUuid());
			intent.putExtra("type", 1);
			intent.putExtra("personType", player.getType());
			intent.putExtra("placeType", placeBean.getType());
			
			ActivityUtil.goToNewActivityWithComplement(this, intent);
		}
		else if(parent.getId() == R.id.gvGoods)
		{
			goods gd = placeBean.getGoods().get(position);
			
			Intent intent = new Intent(this, GoodDetailActivity.class);
			intent.putExtra("type", 1023);
			intent.putExtra("name", gd.getName());
			intent.putExtra("desc", gd.getDescript());
			intent.putExtra("grade", gd.getGrade());
			intent.putExtra("attack", gd.getAddattack());
			intent.putExtra("defence", gd.getAdddefence());
			intent.putExtra("hp", gd.getAddhp());
			intent.putExtra("life", gd.getLife());
			intent.putExtra("star", gd.getStar());
			intent.putExtra("gid", gd.getGid());
			ActivityUtil.goToNewActivityWithComplement(this, intent);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		Builder builder = new Builder(MainPanelActivity.this);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				exitAPP();
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				
			}
		});
		builder.setTitle("温馨提示");
		builder.setMessage("您是否要退出游戏?");
		builder.show();
	}
	
}
