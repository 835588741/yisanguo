/**
 * 
 */
package com.soul.project.application.adapter;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.soul.project.application.bean.Comment;
import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.soul.project.application.view.MTextView;
import com.soul.project.story.activity.MyApplication;
import com.soul.project.story.activity.PostDetailActivity.ReplayEvent;
import com.soul.project.story.activity.R;
import com.yisanguo.app.api.API;

/**
 * @file CommentListAdapter.java
 * @author 许仕永(xsy)
 * @package_name com.soul.project.application.adapter
 * @todo  TODO
 * @date 2016年6月17日 下午8:17:26
 */
public class CommentListAdapter extends BaseAdapter
{
	private Context context;
	private List<Comment> list;
	private LayoutInflater inflater;
	String strTitle;
	String strDescript;
	String strPosterName;
	String strPubtime;
	ReplayEvent replayEvent;
	FinalHttp finalHttp;
	String pid;
	String isboutique;
	String isfirst;
	String posterid;

	public void setCallBack(ReplayEvent replayEvent)
	{
		this.replayEvent = replayEvent;
	}
	
	public CommentListAdapter(Context context,List<Comment> list)
	{
		this.finalHttp = new FinalHttp();
		finalHttp.configTimeout(8000);
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		ViewHolderTitle holderTitle = null;
		int type = getItemViewType(position);
		
		if(convertView == null)
		{
			if(type == 0)
			{
				convertView = inflater.inflate(R.layout.item_posidetail_title, null);
				holderTitle = new ViewHolderTitle();
				holderTitle.txtDescript = (MTextView)convertView.findViewById(R.id.txtDescript);
				holderTitle.txtPosterName = (MTextView)convertView.findViewById(R.id.txtPosterName);
				holderTitle.txtPubTime = (MTextView)convertView.findViewById(R.id.txtPubTime);
				holderTitle.txtTitle = (MTextView)convertView.findViewById(R.id.txtTitle);		
				holderTitle.txtCommentCount = (MTextView)convertView.findViewById(R.id.txtCommentCount);
				holderTitle.txtBoutique = (MTextView)convertView.findViewById(R.id.txtBoutique);
				holderTitle.txtFirstPost= (MTextView)convertView.findViewById(R.id.txtFirstPost);
				holderTitle.txtDeletePost = (MTextView)convertView.findViewById(R.id.txtDeletePost);
				convertView.setTag(holderTitle);
			}
			else
			{
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_comment_list, null);
				holder.txtComment = (MTextView)convertView.findViewById(R.id.txtComment);
				holder.txtPosterName = (MTextView)convertView.findViewById(R.id.txtPosterName);
				holder.txtPubTime = (MTextView)convertView.findViewById(R.id.txtPubTime);
				
				holder.txtReplay = (MTextView)convertView.findViewById(R.id.txtReplay);
				holder.txtDelete = (MTextView)convertView.findViewById(R.id.txtDelete);
				holder.txtGag    = (MTextView)convertView.findViewById(R.id.txtGag);
				
				convertView.setTag(holder);
			}
		}
		else
		{
			if(type!=0)
			{
				holder = (ViewHolder) convertView.getTag();
			}
			else if(type==0)
			{
				holderTitle = (ViewHolderTitle)convertView.getTag();
			}
		}
		
		if(type!=0)
		{
			Comment comment = list.get(position);
			
			if("true".equals(MyApplication.user.getManager()))
			{
				holder.txtGag.setVisibility(View.VISIBLE);
				holder.txtDelete.setVisibility(View.VISIBLE);
				
				holder.txtDelete.setOnClickListener(new EventDel(position, comment.getCommenterid(), comment.getCid(), pid, 1));
				// 缺少一个禁封的功能
			}
			else
			{
				holder.txtGag.setVisibility(View.GONE);
				holder.txtDelete.setVisibility(View.GONE);
			}
			
//			holder.txtDelete.setOnClickListener(new EventDel(position, comment.getCommenterid(), comment.getCid(), null, 1));
			holder.txtReplay.setOnClickListener(new Event(MyApplication.getUUID(context), comment.getCommenterid(),"@"+comment.getCommentername()));
			holder.txtComment.setText(comment.getContent());
			holder.txtPosterName.setText(comment.getCommentername());
			holder.txtPubTime.setText("评论时间:"+comment.getCommenttime());
		}
		else if(type==0)
		{
			if("true".equals(MyApplication.user.getManager()))
			{
				holderTitle.txtBoutique.setVisibility(View.VISIBLE);
				holderTitle.txtFirstPost.setVisibility(View.VISIBLE);
				holderTitle.txtDeletePost.setVisibility(View.VISIBLE);
				
				if("true".equals(isboutique))
				{
					holderTitle.txtBoutique.setText("取消精华");
					holderTitle.txtBoutique.setOnClickListener(new EventFirstOrBou(pid, API.FORM_REQUEST+"cancelboutiquepost.action?&postid="+pid+"&managerid="+MyApplication.getUUID(context)));
				}
				else
				{
					holderTitle.txtBoutique.setText("精华");
					holderTitle.txtBoutique.setOnClickListener(new EventFirstOrBou(pid, API.FORM_REQUEST+"boutiquepost.action?&postid="+pid+"&managerid="+MyApplication.getUUID(context)));
				}
				
				if("true".equals(isfirst))
				{
					holderTitle.txtFirstPost.setText("取消置顶");
					holderTitle.txtFirstPost.setOnClickListener(new EventFirstOrBou(pid, API.FORM_REQUEST+"cancelfirstpost.action?&postid="+pid+"&managerid="+MyApplication.getUUID(context)));
				}
				else
				{
					holderTitle.txtFirstPost.setText("置顶");
					holderTitle.txtFirstPost.setOnClickListener(new EventFirstOrBou(pid, API.FORM_REQUEST+"firstpost.action?&postid="+pid+"&managerid="+MyApplication.getUUID(context)));
				}
				
				holderTitle.txtDeletePost.setOnClickListener(new EventDel(position, posterid, null, pid, 2));
			}
			else
			{
				holderTitle.txtBoutique.setVisibility(View.GONE);
				holderTitle.txtFirstPost.setVisibility(View.GONE);
				holderTitle.txtDeletePost.setVisibility(View.GONE);
			}
			
			holderTitle.txtDescript.setText(strDescript);
			holderTitle.txtPosterName.setText("[ "+strPosterName);
			holderTitle.txtPubTime.setText(strPubtime+" ]");
			holderTitle.txtTitle.setText(strTitle);
			if(list != null)
			holderTitle.txtCommentCount.setText("共有"+(list.size()-1)+"条回帖");
		}
		
		return convertView;
	}
	
	private class EventFirstOrBou implements OnClickListener
	{
		String postid;
		String url;
		
		public EventFirstOrBou(String postid,String url)
		{
			this.postid = postid;
			this.url = url;
		}
		
		@Override
		public void onClick(View v)
		{
			finalHttp.get(url, new AjaxCallBack<Object>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					ToastUtil.show(context, "操作失败！");
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
							JSONObject jsonObject;
							jsonObject = new JSONObject(t.toString());
							String mes = jsonObject.getString("message");
							if(mes != null)
								ToastUtil.show(context, mes);
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
	
	private class Event implements OnClickListener
	{
		private String replayerid;
		private String receiverid;
		private String replaywho;
		
		public Event(String replayerid,String receiverid,String replaywho)
		{
			this.replaywho  = replaywho;
			this.receiverid = receiverid;
			this.replayerid = replayerid;
		}

		@Override
		public void onClick(View v)
		{
				if(replayEvent != null)
					replayEvent.doReplay(replayerid, receiverid,replaywho);
		}
	}
	
	private class EventDel implements OnClickListener
	{
		//private String targetUUID;
		private int position;
		private String postid;
		private String commentid;
		int type;
		
		public EventDel(int position,String targetUUID, String commentid,String postid,int type)
		{
			Log.i("XU", "xxx pid="+postid);
			this.type = type;
			this.postid = postid;
			this.commentid = commentid;
			this.position = position;
//			this.targetUUID = targetUUID;
		}

		@Override
		public void onClick(View v)
		{
			if(type == 1 && v.getId() == R.id.txtDelete)
			{
				Log.i("XU", "xushiyong  pid = "+postid);
				finalHttp.get(API.FORM_REQUEST+"deletecomment.action?&commentid="+commentid+"&belongpostid="+postid+"&managerid="+MyApplication.getUUID(context), new AjaxCallBack<Object>()
				{
					Dialog dialog;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						super.onFailure(t, errorNo, strMsg);
						Log.i("XU", "请求失败="+strMsg);
						dialog.dismiss();
					}

					@Override
					public void onStart()
					{
						super.onStart();
						dialog = MessageDialog.createLoadingDialog(context, "请求中...");
						dialog.show();
					}

					@Override
					public void onSuccess(Object t)
					{
						super.onSuccess(t);
						dialog.dismiss();
						if(t != null)
						{
							try
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								String mes = jsonObject.getString("message");
								if(mes != null)
								{
									if(list != null && position < list.size())
									{
										list.remove(position);
										notifyDataSetChanged();
									}
									ToastUtil.show(context, mes);
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
			else if(type == 2 && v.getId() == R.id.txtDeletePost)
			{
				finalHttp.get(API.FORM_REQUEST+"deletepost.action?&postid="+postid+"&managerid="+MyApplication.getUUID(context), new AjaxCallBack<Object>()
				{
					Dialog dialog;
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						super.onFailure(t, errorNo, strMsg);
						Log.i("XU", "请求失败="+strMsg);
						dialog.dismiss();
					}

					@Override
					public void onStart()
					{
						super.onStart();
						dialog = MessageDialog.createLoadingDialog(context, "请求中...");
						dialog.show();
					}

					@Override
					public void onSuccess(Object t)
					{
						super.onSuccess(t);
						dialog.dismiss();
						if(t != null)
						{
							try
							{
								JSONObject jsonObject = new JSONObject(t.toString());
								String mes = jsonObject.getString("message");
								if(mes != null)
									ToastUtil.show(context, mes);
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
	}

	public void setTitleViewValue(String strTitle,String strDescript,String strPosterName,String strPubtime, String pid, String posierid, String isboutique, String isfirst)
	{
		this.posterid = posierid;
		this.pid = pid;
		this.strTitle = strTitle;
		this.strDescript = strDescript;
		this.strPubtime = strPubtime;
		this.strPosterName = strPosterName;
		this.isboutique = isboutique;
		this.isfirst = isfirst;
	}
	
	private static final class ViewHolderTitle
	{
		MTextView txtTitle;
		MTextView txtDescript;
		MTextView txtPubTime;
		MTextView txtCommentCount;
		MTextView txtPosterName;
		MTextView txtDeletePost;
		MTextView txtFirstPost;
		MTextView txtBoutique;
	}
	
	private static final class ViewHolder
	{
		MTextView txtPosterName;
		MTextView txtPubTime;
		MTextView txtComment;
		// 点击回复
		MTextView txtReplay;
		MTextView txtDelete;
		MTextView txtGag;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		if(position == 0)
			return 0;
		else 
			return 1;
	}

	@Override
	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return 2;
	}

}
