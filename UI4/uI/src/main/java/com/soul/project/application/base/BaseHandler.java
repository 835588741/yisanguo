package com.soul.project.application.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * BaseHandler
 * @author soul
 */
public abstract class BaseHandler extends Handler {

	private static final String TAG = "BaseHandler";

	private final Context context;

	public BaseHandler(Context context) {
		super();
		this.context = context;
	}

	public BaseHandler(Looper looper, Context context) {
		super(looper);
		this.context = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case BaseHandlerMsg.CommonServerFail_Code:
			break;
		}
	}
}
