package com.soul.project.application.bean;

public class Comment
{
	String cid;
	String content;
	String commentername;
	String commenterid;
	String commenttime;
	String belongpostid;
	public String getCid()
	{
		return cid;
	}
	public void setCid(String cid)
	{
		this.cid = cid;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getCommentername()
	{
		return commentername;
	}
	public void setCommentername(String commentername)
	{
		this.commentername = commentername;
	}
	public String getCommenterid()
	{
		return commenterid;
	}
	public void setCommenterid(String commenterid)
	{
		this.commenterid = commenterid;
	}
	public String getCommenttime()
	{
		return commenttime;
	}
	public void setCommenttime(String commenttime)
	{
		this.commenttime = commenttime;
	}
	public String getBelongpostid()
	{
		return belongpostid;
	}
	public void setBelongpostid(String belongpostid)
	{
		this.belongpostid = belongpostid;
	}
	@Override
	public String toString()
	{
		return "Comment [cid=" + cid + ", content=" + content + ", commentername=" + commentername + ", commenterid="
				+ commenterid + ", commenttime=" + commenttime + ", belongpostid=" + belongpostid + "]";
	}
	
	
}
