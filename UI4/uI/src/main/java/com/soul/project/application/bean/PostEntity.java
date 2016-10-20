package com.soul.project.application.bean;

import java.util.ArrayList;
import java.util.List;

public class PostEntity
{
	String pid;
	String title;
	String content;
	String posterid;
	String pubtime;
	String postername;
	int commentcount;
	int state;
	String isfirst;
	String isboutique;
	int commentnum;
	int readnum;
	List<Comment> commentList = new ArrayList<Comment>();
	
	@Override
	public String toString()
	{
		return "PostEntity [pid=" + pid + ", title=" + title + ", content=" + content + ", posterid=" + posterid
				+ ", pubtime=" + pubtime + ", postername=" + postername + ", commentcount=" + commentcount + ", state="
				+ state + ", isfirst=" + isfirst + ", commentList=" + commentList + "]";
	}

	public int getCommentnum()
	{
		return commentnum;
	}

	public void setCommentnum(int commentnum)
	{
		this.commentnum = commentnum;
	}

	public int getReadnum()
	{
		return readnum;
	}

	public void setReadnum(int readnum)
	{
		this.readnum = readnum;
	}

	public String getIsboutique()
	{			
		return isboutique;
	}

	public void setIsboutique(String isboutique)
	{
		this.isboutique = isboutique;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getIsfirst()
	{
		return isfirst;
	}

	public void setIsfirst(String isfirst)
	{
		this.isfirst = isfirst;
	}

	public List<Comment> getCommentList()
	{
		return commentList;
	}

	public void setCommentList(List<Comment> commentList)
	{
		this.commentList = commentList;
	}

	public String getPid()
	{
		return pid;
	}
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getPosterid()
	{
		return posterid;
	}
	public void setPosterid(String posterid)
	{
		this.posterid = posterid;
	}
	public String getPubtime()
	{
		return pubtime;
	}
	public void setPubtime(String pubtime)
	{
		this.pubtime = pubtime;
	}
	public String getPostername()
	{
		return postername;
	}
	public void setPostername(String postername)
	{
		this.postername = postername;
	}
	public int getCommentcount()
	{
		return commentcount;
	}
	public void setCommentcount(int commentcount)
	{
		this.commentcount = commentcount;
	}
	
	
}
