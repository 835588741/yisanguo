package com.soul.project.application.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.SoundPool;

import com.soul.project.story.activity.BattleActivity;
import com.soul.project.story.activity.R;

public class SoundPlayerUtil
{
	public static SoundPool soundPool;
	private static SoundPlayerUtil soundPlayerUtil;
	Context context;
//	List<Map<String, Integer>> maps = new ArrayList<Map<String,Integer>>();
	Map<String, Integer> map;
	
	public static SoundPlayerUtil getInstance(Context context)
	{
		if(soundPlayerUtil == null)
		{
			soundPool=new SoundPool(12, 0,5);
			soundPlayerUtil = new SoundPlayerUtil(context);
		}
		return soundPlayerUtil;
	}
	
	private SoundPlayerUtil(Context context)
	{
		this.context = context;
		map = new HashMap<String, Integer>();
		map.put("wakeup", soundPool.load(context, R.raw.wakeup, 1));
		
//		map.put("", "");
//		maps.add(map);
	}
	
	public void  play(String name)
	{
		String isPlay = ShareDB.getStringFromDB(context, "isopen_bgm");
		
		if("true".equals(isPlay))
		{
			soundPool.play(map.get("wakeup"), 1, 1, 0, 0, 1);
		}
		//soundPool.setVolume(streamID, 1, 1);
	}
}

