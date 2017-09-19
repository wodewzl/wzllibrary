package com.bs.bsims.update;

/**
 * 统一的网络请求回调接口
 * @author Administrator
 *
 */
public interface ICallback {
	
	void onSuccess(String jsonStr);
	void onError(Exception e);
	
}
