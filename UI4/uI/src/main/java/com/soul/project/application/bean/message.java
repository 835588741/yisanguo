package com.soul.project.application.bean;

import android.os.Parcel;

public class message
{
	/**
	 * 
	 */
	String mid;
	String receiveid;
	String senderid;
	String message;
	String sendtime;
	String readflag;
	String sendername;
	String receivername;
	String replaymessage;
	int type;
//	
//	public message(Parcel in)
//	{
//		mid = in.readString();
//		receiveid = in.readString();
//		senderid = in.readString();
//		message = in.readString();
//		sendtime = in.readString();
//		readflag = in.readString();
//		sendername = in.readString();
//		receivername = in.readString();
//	}

	@Override
	public String toString()
	{
		return "message [mid=" + mid + ", receiveid=" + receiveid + ", senderid=" + senderid + ", message="
				+ message + ", sendtime=" + sendtime + ", readflag=" + readflag + ", sendername=" + sendername
				+ ", receivername=" + receivername + "]";
	}

	public String getReplaymessage()
	{
		return replaymessage;
	}

	public void setReplaymessage(String replaymessage)
	{
		this.replaymessage = replaymessage;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getSendername()
	{
		return sendername;
	}

	public void setSendername(String sendername)
	{
		this.sendername = sendername;
	}

	public String getReceivername()
	{
		return receivername;
	}

	public void setReceivername(String receivername)
	{
		this.receivername = receivername;
	}

	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getReceiveid()
	{
		return receiveid;
	}
	public void setReceiveid(String receiveid)
	{
		this.receiveid = receiveid;
	}
	public String getSenderid()
	{
		return senderid;
	}
	public void setSenderid(String senderid)
	{
		this.senderid = senderid;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getSendtime()
	{
		return sendtime;
	}
	public void setSendtime(String sendtime)
	{
		this.sendtime = sendtime;
	}
	public String getReadflag()
	{
		return readflag;
	}
	public void setReadflag(String readflag)
	{
		this.readflag = readflag;
	}
	
//	@Override
//	public int describeContents()
//	{
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
