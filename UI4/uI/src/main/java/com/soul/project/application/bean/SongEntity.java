package com.soul.project.application.bean;

public class SongEntity
{
	String songName;
	String songDescript;
	public String getSongName()
	{
		return songName;
	}
	public void setSongName(String songName)
	{
		this.songName = songName;
	}
	public String getSongDescript()
	{
		return songDescript;
	}
	public void setSongDescript(String songDescript)
	{
		this.songDescript = songDescript;
	}
	@Override
	public String toString()
	{
		return "SongEntity [songName=" + songName + ", songDescript=" + songDescript + "]";
	}
	
	
}
