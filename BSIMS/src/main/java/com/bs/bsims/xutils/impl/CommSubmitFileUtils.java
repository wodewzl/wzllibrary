
package com.bs.bsims.xutils.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bs.bsims.model.ContactObject;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.UserManager;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class CommSubmitFileUtils {

    private SharedPreferences sp;

    public CommSubmitFileUtils(SharedPreferences sp) {
        this.sp = sp;
    }

    /**
     * 提交文件到服务器
     * 
     * @param url
     * @param list 里面是文件的本地地址
     * @param callBack 回调接口
     */
    public void submitFile(Context context, String url, Map<String, String> map,
            List<ContactObject> list, Callback.CommonCallback<String> callBack) {
        // map.put("ftoken", "QBzTBXBUMNzKkE5MzAxRjhCQTM0MAO0O0OO0O0O");
        map.put("ftoken", UserManager.getLoginUser(sp).getFtoken());
        map.put("userid", UserManager.getLoginUser(sp).getUserid());
        RequestParams params = new RequestParams(url);
        StringBuffer testUrl = new StringBuffer();
        // 使用multipart表单上传文件
        params.setMultipart(true);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.addBodyParameter(entry.getKey(), entry.getValue());
            testUrl.append("/").append(entry.getKey()).append("/").append(entry.getValue());
        }

        // 添加引用程序标识
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        params.addBodyParameter("ftoken", UserManager.getLoginUser(sp).getFtoken());

        CustomLog.e("NetUrl", testUrl.toString());

        // 上传的图片
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ContactObject contactObject = list.get(i);
                String portraitPath = contactObject.getPortraitPath();
                if (!portraitPath.equals("")) {
                    File file = new File(portraitPath);
                    params.addBodyParameter(contactObject.getPortraitName(), file);
                }
            }
        }

        // HttpM httpUtils = new HttpUtils();
        // httpUtils.send(HttpMethod.POST, url, params, callBack);
        x.http().post(params, callBack);
    }

    /**
     * @param context
     * @param url 网络地址
     * @param map 请求参数
     * @param fileMap 封装要上传的文件信息，key为http请求参数的字段名称，value为上传文件的本地地址
     * @param callBack 回调
     */
    public void submitFile(Context context, String url, Map<String, String> map,
            Map<String, String> fileMap, Callback.CommonCallback<String> callBack) {
        map.put("ftoken", "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        // map.put("ftoken", UserManager.getLoginUser(sp).getFtoken());
        // map.put("userid", UserManager.getLoginUser(sp).getUserid());
        RequestParams params = new RequestParams(url);
        StringBuffer testUrl = new StringBuffer();
        // 使用multipart表单上传文件
        params.setMultipart(true);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.addBodyParameter(entry.getKey(), entry.getValue());
            testUrl.append("/").append(entry.getKey()).append("/").append(entry.getValue());
        }

        CustomLog.e("NetUrl", testUrl.toString());

        // 上传的文件
        if (null != fileMap && fileMap.size() > 0) {
            CustomLog.e("CommSubmitFileUtils", "上传的fileMap不为null");
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                File file = new File(entry.getValue());
                params.addBodyParameter(entry.getKey(), file);
            }
        }

        // HttpUtils httpUtils = new HttpUtils();
        // httpUtils.send(HttpMethod.POST, url, params, callBack);
        x.http().post(params, callBack);
    }

}
