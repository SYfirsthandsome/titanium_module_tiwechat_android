package com.mamashai.babycalendar.wxapi;

import org.appcelerator.kroll.common.Log;

import android.app.Activity;

import android.content.Intent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.*;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.ActivityManager;
import android.content.ComponentName;
import java.util.List;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;

import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        api = WXAPIFactory.createWXAPI(this, "wxc4e544191aa9121a", false);
        
        api.handleIntent(getIntent(), this);
        
        Log.d("WXEntryActivity", "~~~~~~~~~~~~~~~~~WXEntryActivity~~~~~~~~~~~~~");
	
	}
	@Override
	public void onReq(BaseReq req) {
		
		Log.d("WXEntryActivity", "~~~~~~~~~~~~~~~~onReq~~~~~~~~~~~~~~");
		
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		if(resp.transaction.equals("login")){
			SendAuth.Resp send_resp = (SendAuth.Resp)resp;

			if (resp.errCode == 0){
				TiApplication app = TiApplication.getInstance();
				TiProperties prop = app.getAppProperties();
				prop.setString("w_type", 	resp.transaction);
				prop.setString("w_code", 	send_resp.code);
				prop.setString("w_country", send_resp.country);
				prop.setString("w_lang", 	send_resp.lang);
				prop.setString("w_state", 	send_resp.state);
				prop.setString("w_occur_at",System.currentTimeMillis() + "");
			}

			Log.d("WXEntryActivity", "send_resp:" + send_resp.code + " " + send_resp.country + " " + send_resp.lang + " "+ send_resp.state + " " + send_resp.url);
		}
		else{
			Log.d("WXEntryActivity", "onResp: errCode: " + resp.errCode + " errStr:" + resp.errStr + " transaction:" + resp.transaction);
			if (resp.errCode == -2){
				Toast.makeText(this, "分享微信已取消", Toast.LENGTH_LONG).show();
			}
			else if (resp.errCode == -4){
				Toast.makeText(this, "分享微信发生异常", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this, "分享微信成功", Toast.LENGTH_LONG).show();

				TiApplication app = TiApplication.getInstance();
				TiProperties prop = app.getAppProperties();
				prop.setString("w_type", 	resp.transaction);
				prop.setString("w_occur_at",System.currentTimeMillis() + "");
			}
		}
		
		finish();
	}

}
