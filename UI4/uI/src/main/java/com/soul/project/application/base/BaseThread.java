package com.soul.project.application.base;

import android.content.Context;

import android.util.Log;

/**
 * 线程基类
 * @author soul
 */
public abstract class BaseThread extends Thread {
	
	private static final String tag = "BaseThread";
	
	protected final BaseHandler baseHandler;
	protected final Context context;
	
	public BaseThread(BaseHandler baseHandler, Context context) {
		this.context = context;
		this.baseHandler = baseHandler;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return the baseHandler
	 */
	public BaseHandler getBaseHandler() {
		return baseHandler;
	}

	@Override
	public abstract void run();
}
