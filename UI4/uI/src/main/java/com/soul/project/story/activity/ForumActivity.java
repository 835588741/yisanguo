/**
 * 
 */
package com.soul.project.story.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soul.project.application.adapter.PostListAdapter;
import com.soul.project.application.bean.PostEntity;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ActivityUtil;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;
import com.yisanguo.app.api.ErrorHandler;

/** 论
 * @file ForumActivity.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.story.activity
 * @todo  TODO
 * @date 2016年6月17日 下午4:36:37
 */
public class ForumActivity extends BaseActivityWithSystemBarColor implements OnItemClickListener, OnClickListener
{
	private Button btnRefresh;
	private SwipeMenuListView postListView;
	private Handler mHandler = new Handler();
	private int page = 1;
	private int pagesize = 20;
	private PostListAdapter adapter;
	private Button btnPub;
	private Button btnOut;
	private EditText etTitle ;
	private EditText etContent; 
	private Dialog dialog;
	private List<PostEntity> list = new ArrayList<PostEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum);
		ToastUtil.showStaticToastShort(this, "【论坛管理员（解锋镝,苏颜沫,风流骑士）,严禁黄赌毒,请共同维护论坛文明秩序。】");
		initViews();
		loadData(0,0);
	}
	
	private void initViews()
	{
		btnRefresh = (Button)this.findViewById(R.id.btnRefresh);
		postListView = (SwipeMenuListView) this.findViewById(R.id.postlist);
		// 隐藏第一个Item的divider
		postListView.setHeaderDividersEnabled(false);
		postListView.setOnItemClickListener(this);
		postListView.setListViewFooterTextViewColor(getResources().getColor(R.color.black));
		postListView.setListViewHeaderTextViewColor(getResources().getColor(R.color.black));
		postListView.setListViewHeaderTimeTextViewColor(getResources().getColor(R.color.gray));
		
		postListView.setXListViewListener(new RefreshListener());
		
		adapter = new PostListAdapter(this, list);
		postListView.setAdapter(adapter);
		
		btnOut = (Button)this.findViewById(R.id.btnOut);
		btnPub = (Button)this.findViewById(R.id.btnPub);
		
		btnOut.setOnClickListener(this);
		btnPub.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);
	}

	class RefreshListener implements IXListViewListener
	{
		@Override
		public void onRefresh()
		{
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					page = 1;
					loadData(0,0);
//					postListView.stopRefresh();
//					postListView.stopLoadMore();
//					postListView.setRefreshTime("刚刚");
				}
			}, 2000);
		}

		@Override
		public void onLoadMore()
		{
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					postListView.stopRefresh();
					postListView.stopLoadMore();
					postListView.setRefreshTime("刚刚");
				}
			}, 2000);
		}
	}

	
	/* (non-Javadoc)
	 * @see com.soul.project.story.activity.BaseActivityWithSystemBarColor#loadData(int)
	 */
	@Override
	public void loadData(final int id,int type)
	{
		finalHttp.get(API.URL+"querydata.action?&table=post&condition=null&type=1&page="+page+"&pagesize="+pagesize, new AjaxCallBack<Object>()
		{
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.i("XU", "请求失败"+errorNo);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(ForumActivity.this, "努力加载中...");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				Log.i("XU", "t.t = "+t.toString());
				if(t != null)
				{
					if(id == 0 && list != null)
						list.clear();
					
					if("[]".equals(t.toString().trim()))
					{
						ToastUtil.show(ForumActivity.this, "没有更多帖子了，请稍后再刷新", ToastUtil.ERROR);
					}
					else
					{
						Gson gson = new Gson();
						List<PostEntity> temp = gson.fromJson(t.toString(), new TypeToken<List<PostEntity>>(){}.getType());
						for (int i = 0; i < temp.size(); i++)
						{
							list.add(temp.get(i));
						}
						adapter.notifyDataSetChanged();
					}
					
//					if(temp.size() < pagesize)
//						postListView.setListViewFooterTextViewText("没有更多帖子了");
					
				}
				
				postListView.stopRefresh();
				postListView.stopLoadMore();
				postListView.setRefreshTime("刚刚");
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		if(position <= list.size())
		{
			Intent intent = new Intent(this, PostDetailActivity.class);
			PostEntity entity = list.get(position - 1);
			intent.putExtra("postername",entity.getPostername());
			intent.putExtra("pubtime",entity.getPubtime());
			intent.putExtra("title",entity.getTitle());
			intent.putExtra("content",entity.getContent());
			intent.putExtra("posierid", entity.getPosterid());
			intent.putExtra("isfirst", entity.getIsfirst());
			intent.putExtra("isboutique", entity.getIsboutique());
			intent.putExtra("pid", entity.getPid());
			ActivityUtil.goToNewActivityWithComplement(this, intent);
		}
		else if(position == list.size() +1)
		{
			page ++;
			loadData(1,0);
		}
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.btnOut:
				finish();
				break;
			case R.id.btnPub:
				showEditPostDialog();
				break;
			case R.id.btnCancel:
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
				break;
			case R.id.btnPubPost:
				pubPost();
				break;
			case R.id.btnRefresh:
				page = 1;
				loadData(0,0);
				break;
			default:
				break;
		}
	}

	/** 提交帖子信息*/
	private void pubPost()
	{
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		
		if(content.length() > 2000)
		{
			ToastUtil.show(ForumActivity.this, "帖子内容不能多于2000字!");
		}
		else
		{
			if(content != null && !"".equals(content.trim()))
			{
				if(title == null || "".equals(title.trim()))
				{
					title = content.length() > 10 ?  content.substring(0, 10) : content;
				}
				
				
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
				
				if(ErrorHandler.handlerError(this) == false)
					return;
				
				//String title,String content,String posterid,String postername
				AjaxParams params = new AjaxParams();
				params.put("title", title);
				params.put("content", content);
				params.put("posterid", MyApplication.user.getUuid());
				params.put("postername", MyApplication.user.getName());
				
				finalHttp.post(API.FORM_REQUEST+"publishpost.action?", params, new AjaxCallBack<Object>()
				{
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						ToastUtil.show(ForumActivity.this, "发帖失败!", ToastUtil.ERROR);
					}

					@Override
					public void onSuccess(Object t)
					{
						// TODO Auto-generated method stub
						super.onSuccess(t);
//						{
//						    "message": "发帖成功!",
//						    "code": 200
//						}
						if(t != null )
						{
							try
							{
								JSONObject object = new JSONObject(t.toString());
								int code = object.getInt("code");
								if(code == 200)
								{
									ToastUtil.show(ForumActivity.this, "发帖成功!", ToastUtil.INFO);
								}
								
								mHandler.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										page = 1;
										loadData(0,0);
									}
								}, 1000);
							}
							catch (Exception e)
							{
								// TODO: handle exception
								ToastUtil.show(ForumActivity.this, "发帖失败!", ToastUtil.ERROR);
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
	}

	private void showEditPostDialog()
	{
		dialog = new Dialog(this, R.style.myDialogTheme);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_publich_post, null);
		dialog.setContentView(view);
		
		Button btnPub = (Button)view.findViewById(R.id.btnPubPost);
		Button btnCancel = (Button)view.findViewById(R.id.btnCancel);
		
		btnPub.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		etTitle = (EditText)view.findViewById(R.id.etTitle);
		etContent = (EditText)view.findViewById(R.id.etContent);
		
		dialog.show();
	}

}
