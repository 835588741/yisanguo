package com.soul.project.story.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.soul.project.application.dialog.MessageDialog;
import com.soul.project.application.util.ToastUtil;
import com.yisanguo.app.api.API;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class HelpActivity extends BaseActivity implements OnClickListener
{
	Button btnLeave1;
	Button btnLeave2;
	Button btnGoBack1;
	Button btnGoBack2;
	WebView webView;
	protected FinalHttp finalHttp;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		finalHttp = new FinalHttp();
		// 网络超时 8秒
		finalHttp.configTimeout(8000);
		
		btnGoBack1 = (Button)this.findViewById(R.id.btnGoBack1);
		btnGoBack2 = (Button)this.findViewById(R.id.btnGoBack2);
		btnLeave1  = (Button)this.findViewById(R.id.btnLeave1);
		btnLeave2  = (Button)this.findViewById(R.id.btnLeave2);
		webView =  (WebView)this.findViewById(R.id.wenView);
		
		btnGoBack1.setOnClickListener(this);
		btnGoBack2.setOnClickListener(this);
		btnLeave1.setOnClickListener(this);
		btnLeave2.setOnClickListener(this);
		
		
		   // 设置支持JavaScript等 
       WebSettings mWebSettings = webView.getSettings(); 
        mWebSettings.setJavaScriptEnabled(true); 
        mWebSettings.setBuiltInZoomControls(true); 
        mWebSettings.setLightTouchEnabled(true); 
        mWebSettings.setSupportZoom(true); 
        mWebSettings.setDefaultTextEncodingName("utf-8"); 
        webView.setHapticFeedbackEnabled(false); 
        // mWebView.setInitialScale(0); // 改变这个值可以设定初始大小 

        webView.loadUrl("http://112.124.109.206:8080/springMVC/xuanyisanguo.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        
//        //重要,用于与页面交互! 
//        webView.addJavascriptInterface(new Object() { 
//            @SuppressWarnings("unused") 
//            public void oneClick(final String locX, final String locY) {//此处的参数可传入作为js参数 
//                mHandler.post(new Runnable() { 
//                    public void run() { 
//                        mWebView.loadUrl("javascript:shows(" + locX + "," + locY + ")"); 
//                    } 
//                }); 
//            } 
//        }, "demo");//此名称在页面中被调用,方法如下: 
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btnGoBack1:
			case R.id.btnGoBack2:
				goback();
				break;
			case R.id.btnLeave1:
			case R.id.btnLeave2:
				finish();
				break;
			default:
				break;
		}
	}

	
	// 回城
	private void goback()
	{
		finalHttp.get(API.URL+"goback.action?&uuid="+MyApplication.getUUID(this), new AjaxCallBack<Object>()
		{
			Dialog dialog;
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}

			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
				dialog = MessageDialog.createLoadingDialog(HelpActivity.this, "Loadding");
				dialog.show();
			}

			@Override
			public void onSuccess(Object t)
			{
				// TODO Auto-generated method stub
				super.onSuccess(t);
				dialog.dismiss();
				if(t != null)
				{
					try
					{
						JSONObject jsonObject = new JSONObject(t.toString());
						String message;
						message = jsonObject.getString("message");
						HelpActivity.this.finish();
						ToastUtil.show(HelpActivity.this, message);
					}
					catch (JSONException e)
					{
						// TODO Auto-generated catch block
						Log.i("XU", "出现异常="+e.toString());
						e.printStackTrace();
					}
				}
			}
		});
	}


}
