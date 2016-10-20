package com.soul.project.story.activity.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.soul.project.story.activity.MyApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Context context = WXEntryActivity.this;
    public String mAPPid= "wx9972e13ac66616bf";
    

    private void handleIntent(Intent paramIntent) {
    	MyApplication.wxapi.handleIntent(paramIntent, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if(MyApplication.wxapi == null)
        {
        	MyApplication.wxapi = WXAPIFactory.createWXAPI(this, mAPPid,  true); 
        	MyApplication.wxapi.registerApp(mAPPid);
        }
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        // TODO Auto-generated method stub
    	Log.i("XU", "resp.errCode="+resp.errCode);
        switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:
            if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                break;
            }
            String code = ((SendAuth.Resp) resp).code;
            //不能直接获取到openid了，需要通过code等参数去请求
            //String openid = ((SendAuth.Resp) resp).openId;
            String openid = null;
            if(code != null)
            {
            	executeHttpGet(code);
            }
            
//            Log.i("XU", "--->openid="+openid);
            //new SharedPreferencesclass(49, code, context);
            System.out.println("微信确认登录返回的code：" + code);
            Toast.makeText(context, "微信确认登录返回的code：" + code+"  opendid="+openid, Toast.LENGTH_LONG).show();
            //MyResumeLoginView.loginHandler.sendEmptyMessage(MyResumeLoginView.WEIXIN_LOGIN_SUCCESS);
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
            break;
        default:
            break;
        }
        finish();
    }

    
    public void executeHttpGet(String code) {
//    	
//    	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+API.AppID+"&secret="+API.AppSecret+"&code="+code+"&grant_type=authorization_code";
//    	mRequset.requestForJsonObject(url, null, new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				Log.i("XU", "微信回馈====>"+response.toString());
//				Toast.makeText(context, "微信回馈====>"+response.toString(), Toast.LENGTH_LONG).show();
//			}
//		}, new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// TODO Auto-generated method stub
//				Log.i("XU", "微信错误回馈====>"+error.toString());
//				Toast.makeText(context, "微信错误回馈====>"+error.toString(), Toast.LENGTH_LONG).show();
//			}
//		});
    }
}

