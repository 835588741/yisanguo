package com.soul.project.application.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.soul.project.story.activity.MyApplication;

/**
 * BaseActivity
 * @author School
 */
public abstract class BaseActivity extends Activity implements OnClickListener {

	private static final String tag = "BaseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		((MyApplication) this.getApplication()).addActivity(this);
	}

	public abstract void init();

	@Override
	public abstract void onClick(View v);

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(tag, "BaseActivity - onDestroy");
		((MyApplication) this.getApplication()).removeActivity(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(tag, "BaseActivity - onRestart");
		if (!((MyApplication) this.getApplication()).isActivityContain(this)) {
			((MyApplication) this.getApplication()).addActivity(this);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(tag, "BaseActivity - onStart");
		if (!((MyApplication) this.getApplication()).isActivityContain(this)) {
			((MyApplication) this.getApplication()).addActivity(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}
}

