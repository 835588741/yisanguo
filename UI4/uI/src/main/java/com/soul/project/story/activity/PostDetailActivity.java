/**
 * 
 */
package com.soul.project.story.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.CommentListAdapter;
import com.soul.project.application.bean.Comment;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;
import com.yisanguo.app.api.ErrorHandler;

/** 帖子详情
 * @file PostDetailActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年6月17日 下午7:26:59
 */
public class PostDetailActivity extends BaseActivityWithSystemBarColor implements OnClickListener
{
	Button btnRefresh;
	
	Handler mHandler = new Handler();
	ListView commentList;
	CommentListAdapter adapter ;
	
	private Button btnPub;
	private Button btnOut;
	private EditText etContent; 
	
	String posierid;
	String strTitle;
	String strDescript;
	String strPubtime;
	String pid;
	String strPosterName;
	String replaywho;
	String receiverid;
	String isboutique;
	String isfirst;
	
	List<Comment> list = new ArrayList<Comment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postdetail);
		
		strTitle = getIntent().getStringExtra("title");//.putExtra("postername",entity.getPostername());
		strDescript = getIntent().getStringExtra("content");
		strPubtime = getIntent().getStringExtra("pubtime");
		strPosterName = getIntent().getStringExtra("postername");
		posierid = getIntent().getStringExtra("posierid");
		pid = getIntent().getStringExtra("pid");
		isfirst = getIntent().getStringExtra("isfirst");
		isboutique = getIntent().getStringExtra("isboutique");
		
		loadData(0,0);
		initViews();
	}
	
	private void initViews()
	{
		btnRefresh = (Button)this.findViewById(R.id.btnRefresh);
		btnOut = (Button)this.findViewById(R.id.btnOut);
		btnPub = (Button)this.findViewById(R.id.btnPub);
		
		btnOut.setOnClickListener(this);
		btnPub.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
//		txtPlaceName.setOnClickListener(this);
		
		commentList = (ListView)this.findViewById(R.id.commentList);
		adapter = new CommentListAdapter(this, list);
		adapter.setTitleViewValue(strTitle, strDescript, strPosterName, strPubtime,pid,posierid,isboutique,isfirst);
		adapter.setCallBack(new ReplayEvent());
		commentList.setAdapter(adapter);
	}
	
	public class ReplayEvent implements ReplayCallBack
	{
		@Override
		public void doReplay(String replayerid,String receiveri,String replaywh)
		{
			receiverid = receiveri;
			replaywho = replaywh;
			showEditCommentDialog();
		}
	}

	
	public interface ReplayCallBack 
	{
		public void doReplay(String replayerid,String receiverid,String replaywho);
	}
	
	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int)
	 
	 */
	@Override
	public void loadData(int id,int type)
	{
//		Log.i("XU", "请求地址="+API.URL+"querydata.action?&table=comment&type=1&condition=null&page=1&pagesize=1000");
		finalHttp.get(API.FORM_REQUEST+"getcomment.action?&belongpostid="+pid+"&&page=1&pagesize=1000", new AjaxCallBack<Object>()
		{
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
				dialog = MessageDialog.createLoadingDialog(PostDetailActivity.this, "努力加载中...");
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
					List<Comment> temp = gson.fromJson(t.toString(), new TypeToken<List<Comment>>(){}.getType());
					
					if(list != null)
					list.clear();
					
					if(temp != null)
					{
						list.add(new Comment());
						for (int i = 0; i < temp.size(); i++)
						{
							list.add(temp.get(i));
						}
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnOut:
				finish();
				break;
			case R.id.btnPub:
				replaywho = "";
				showEditCommentDialog();
				break;
			case R.id.btnCancel:
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
				break;
			case R.id.btnPubPost:
				submitComment();
				break;
			case R.id.btnRefresh:
				loadData(0,0);
				break;
			default:
				break;
		}
	}

	private void submitComment()
	{
		String content = etContent.getText().toString();
		
		if(posierid == null)
			return;
		
		if(content != null && !"".equals(content.trim()))
		{
			if(dialog != null && dialog.isShowing())
				dialog.dismiss();
			
			if(ErrorHandler.handlerError(this) == false)
				return;
			
			//String content,String commenterid,String commentername,String belongpostid
			AjaxParams params = new AjaxParams();
			params.put("content", replaywho+" "+content);
			params.put("belongpostid", pid);
			params.put("commenterid", MyApplication.user.getUuid());
			params.put("commentername", MyApplication.user.getName());
			
			finalHttp.post(API.FORM_REQUEST+"publishcomment.action?", params, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					ToastUtil.show(PostDetailActivity.this, "回帖失败!", ToastUtil.ERROR);
				}

				@Override
				public void onSuccess(Object t)
				{
					super.onSuccess(t);
					if(t != null )
					{
						try
						{
							JSONObject object = new JSONObject(t.toString());
							int code = object.getInt("code");
							if(code == 200)
							{
								ToastUtil.show(PostDetailActivity.this, "回帖成功!", ToastUtil.INFO);
							}
							
							mHandler.postDelayed(new Runnable()
							{
								@Override
								public void run()
								{
									loadData(0,0);
								}
							}, 1000);
						}
						catch (Exception e)
						{
							// TODO: handle exception
							ToastUtil.show(PostDetailActivity.this, "回帖失败!", ToastUtil.ERROR);
						}
						
						// 如果回复其他楼成功后，给他发一个系统提示消息（有可能replaywho已经在其他地方被置为空字段，所以不一定所有回复都会发系统消息提醒）
						if(!"".equals(replaywho) && !"".equals(receiverid))
						{
							String mes = "有神秘人在论坛帖子《"+strTitle+"》中@了你一下";
							try
							{
								finalHttp.get(API.MESSAGE_REQUEST+"systemmessage.action?&receiver_uuid="+receiverid+"&type=2&keyword="+(URLEncoder.encode(mes,"utf-8")), new AjaxCallBack<Object>()
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
				}
			});
		}
		else
		{
			etContent.setError("内容不能为空!");
		}
	}

	private void showEditCommentDialog()
	{
		dialog = new Dialog(this, R.style.myDialogTheme);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_publish_comment, null);
		dialog.setContentView(view);
		
		Button btnPub = (Button)view.findViewById(R.id.btnPubPost);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		
		btnPub.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
//		etTitle = (EditText)view.findViewById(R.id.etTitle);
		etContent = (EditText)view.findViewById(R.id.etContent);
		
		dialog.show();
	}

}
