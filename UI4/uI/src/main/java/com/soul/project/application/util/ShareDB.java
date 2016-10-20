package com.soul.project.application.util;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ShareDB {
	
	static String dbFileNameString = "private—file.txt";

    public static void save2DB(Context context,String key,Set<String> values) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 用putString的方法保存数据
        editor.putStringSet(key, values);
        // 提交当前数据
        editor.commit();
    }
    
    public static Set<String> getStringSetFromDB(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        Set<String> defValues = null;
        return preferences.getStringSet(key, defValues);
    }
    
    public static void save2DB(Context context,String key,int value) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 用putString的方法保存数据
        editor.putInt(key, value);
        // 提交当前数据
        editor.commit();
    }
    
    public static void save2DB(Context context,String key,long value) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 用putString的方法保存数据
        editor.putLong(key, value);
        // 提交当前数据
        editor.commit();
    }
    
    public static long getLongFromDB(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        return preferences.getLong(key, -1);
    }
    
    public static void save2DB(Context context,String key,String value) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 用putString的方法保存数据
        editor.putString(key, value);
        // 提交当前数据
        editor.commit();
    }

    
    public static int getIntFromDB(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }
    
    public static String getStringFromDB(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(dbFileNameString, Activity.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
}
