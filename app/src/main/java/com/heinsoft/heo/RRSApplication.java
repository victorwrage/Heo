package com.heinsoft.heo;


import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.heinsoft.heo.util.Constant;
import com.socks.library.KLog;


/**
 * @ClassName:	NFCApplication 
 * @Description:TODO(Application) 
 * @author:	xiaoyl
 * @date:	2013-7-10 下午4:01:27 
 *  
 */
public class RRSApplication extends VApplication {
	private static RRSApplication instance;
	public static boolean isExit = false;


	public RRSApplication() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
	//	setupDatabase();
		initOcr();
	}

	private void initOcr() {
		OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken result) {
				// 调用成功，返回AccessToken对象
				Constant.PUBLIC_OCR_TOKEN = result.getAccessToken();
			}
			@Override
			public void onError(OCRError error) {
				// 调用失败，返回OCRError子类SDKError对象
				KLog.v(error.toString());
			}
		}, getApplicationContext(), Constant.PUBLIC_OCR_KEY, Constant.PUBLIC_OCR_SECRET);
	}




	public static RRSApplication getInstance() {
		if (null == instance) {
			instance = new RRSApplication();
		}
		return instance;
	}
}
