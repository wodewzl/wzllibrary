
package com.bs.bsims.xutils.impl;

/**
 * @author 梁瞎名
 * @EName LQC
 * @Important Nice
 * @Stoyre 帅的无与伦比
 * @date 2016-1-8 下午4:45:55
 */
import android.text.TextUtils;

import com.bs.bsims.utils.CustomLog;

import org.xutils.HttpManager;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.HttpTask;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.Map;

public class HttpUtilsByPC implements HttpManager {

    @Override
    public <T> Cancelable get(RequestParams arg0, CommonCallback<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getSync(RequestParams arg0, Class<T> arg1) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Cancelable post(RequestParams arg0, CommonCallback<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *post提交 
     */
    
    public <T> Cancelable sendPostBYPC(String url,
            Map<String, String> paramsMap, CommonCallback<String> callback) {
        // TODO Auto-generated method stub
        RequestParams params = new RequestParams(url);
        addParams(params, paramsMap);
        return request(HttpMethod.POST, params, callback);
    }

    @Override
    public <T> T postSync(RequestParams arg0, Class<T> arg1) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Cancelable request(HttpMethod method, RequestParams entity,
            CommonCallback<T> callback) {
        if (method == HttpMethod.GET) {
            return get(entity, callback);
        }
        entity.setMethod(method);
        Callback.Cancelable cancelable = null;
        if (callback instanceof Callback.Cancelable) {
            cancelable = (Callback.Cancelable) callback;
        }
        HttpTask task = new HttpTask(entity, cancelable, callback);
        return x.task().start(task);
    }

    @Override
    public <T> T requestSync(HttpMethod arg0, RequestParams arg1, Class<T> arg2) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    private String addParams(RequestParams params,
            Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer();
        // RequestParams params = new com.lidroid.xutils.http.RequestParams();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            sb.append("/");
            sb.append(entry.getKey());
            sb.append("/");
            sb.append(entry.getValue());
            File file = new File(entry.getValue());
            if (file.exists()) {
                params.addBodyParameter(entry.getKey(), file);
            } else {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        CustomLog.e("error", sb.toString());
        return sb.toString();
    }

}
