package com.bs.bsims.update;

import java.util.Map;

/**
 * 请求网络方法
 * @author Administrator
 *
 */
public interface INetMethod {
	static final String TAG = "NetUrl";
	
	void networkGet(String url, Map<String, String> paramsMap, ICallback callback);
	void networkPost(String url, Map<String, String> paramsMap, ICallback callback);
	
	
	
}	
