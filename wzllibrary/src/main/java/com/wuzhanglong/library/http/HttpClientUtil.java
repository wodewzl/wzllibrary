package com.wuzhanglong.library.http;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.cache.ACache;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.interfaces.RequestMessageCallback;
import com.wuzhanglong.library.interfaces.UpdateCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.StringUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;

public class HttpClientUtil {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient = new SyncHttpClient();
    private static final String CHARSET_UTF8 = HTTP.UTF_8;
    private static final String CHARSET_GB2312 = "GB2312";
    private static DefaultHttpClient customerHttpClient;


    public static <T> BaseVO getRequest(Context context, final UpdateCallback callback, String url, RequestParams params, final Class<T> className) {
        BaseActivity activity = (BaseActivity) context;
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url + params.toString();
        Log.i("get_url", allUrl + params.toString().toString());
        if (!HttpUtils.isNetworkAvailable(context)) {
            final String cacheStr = ACache.get(context).getAsString(allUrl + params.toString());
            if (className != null) {
                final BaseVO vo = (BaseVO) gson.fromJson(cacheStr, className);
                if (vo != null) {
//                callback.baseHasData(vo);
                    return vo;
                }
            }
        }

        try {
            HttpGet getMethod = new HttpGet(allUrl);
            HttpResponse response = client.getHttpClient().execute(getMethod);
            // 获取状态码
            int res = response.getStatusLine().getStatusCode();
            if (res == 200) {
                StringBuilder builder = new StringBuilder();
                // 获取响应内容
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    builder.append(s);
                }
                String result = builder.toString();
                if (className == null) {
                    JSONObject jsonObject = new JSONObject(new String(result));
                    String code = (String) jsonObject.get("code");
                    if (code.equals("200")) {
                        activity.showCustomToast("提交成功");
                    } else {
                        String error = (String) jsonObject.get("error");
                        activity.showCustomToast(error);
                    }
                    return null;
                } else {
                    BaseVO vo = (BaseVO) gson.fromJson(result, className);
                    if (BaseConstant.RESULT_FAIL_CODE400.equals(vo.getCode())) {
//                    callback.baseNoData(vo);
                    } else {
//                    callback.baseHasData(vo);
                        if (!StringUtils.isEmpty(result)) {
                            ACache.get(context).put(allUrl + params.toString(), result, 60 * 60 * 24);
                        }
                    }
                    return vo;
                }
            } else {
                BaseVO vo = new BaseVO();
                vo.setCode("600");//网络请求失败
                return vo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void show(final RequestMessageCallback callback) {
        callback.sendMessage(null);
    }

    public static <T> void get(final Context context, final RequestMessageCallback callback, final String url, final RequestParams params, final Class<T> className) {
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        final String cacheStr = ACache.get(context).getAsString(allUrl + params.toString());

        if (!HttpUtils.isNetworkAvailable(context)) {
            if (className != null && cacheStr != null) {
                final BaseVO vo = (BaseVO) gson.fromJson(cacheStr, className);
                if (vo != null) {
                    callback.sendMessage(vo);
                    return;
                }
            } else {
                BaseVO noNetVO = new BaseVO();
                noNetVO.setCode("600");//网络请求失败
                callback.sendMessage(noNetVO);
            }
            return;
        } else {
            if (className != null && cacheStr != null) {
                final BaseVO vo = (BaseVO) gson.fromJson(cacheStr, className);
                if (vo != null) {
                    callback.sendMessage(vo);
                }
            }
        }


        Log.i("get_url", allUrl + params.toString());
        syncHttpClient.get(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                if (result.equals(cacheStr) || className == null) {
                    return;
                }
                BaseVO vo = (BaseVO) gson.fromJson(result, className);
                callback.sendMessage(vo);
                if (BaseConstant.RESULT_FAIL_CODE400.equals(vo.getCode())) {
//                    callback.baseNoData(vo);

                } else {
//                    callback.baseHasData(vo);
                    if (!StringUtils.isEmpty(result)) {
                        ACache.get(context).put(allUrl + params.toString(), result, 60 * 60 * 24);
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                BaseVO noNetVO = new BaseVO();
                noNetVO.setCode("600");//网络请求失败
                callback.sendMessage(noNetVO);
//                callback.baseNoNet();
            }
        });

    }

    public static <T> void get(final Context context, final UpdateCallback callback, final String url, final RequestParams params, final Class<T> className) {
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        final String cacheStr = ACache.get(context).getAsString(allUrl + params.toString());
        if (className != null) {
            final BaseVO vo = (BaseVO) gson.fromJson(cacheStr, className);
            if (vo != null) {
                callback.baseHasData(vo);
                if (!HttpUtils.isNetworkAvailable(context)) {
                    return;
                }
            }
        }

        Log.i("get_url", allUrl + params.toString());
        client.get(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                if (result.equals(cacheStr) || className == null) {
                    return;
                }
                BaseVO vo = (BaseVO) gson.fromJson(result, className);
                if (BaseConstant.RESULT_FAIL_CODE400.equals(vo.getCode())) {
                    callback.baseNoData(vo);
                } else {
                    callback.baseHasData(vo);
                    if (!StringUtils.isEmpty(result)) {
                        ACache.get(context).put(allUrl + params.toString(), result, 60 * 60 * 24);
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                callback.baseNoNet();
            }
        });

    }


    public static <T> void post(final Context context, final UpdateCallback callback, final String url, final RequestParams params, final Class<T> className, final String type) {
        //type 1 不finish 2 finish 3 finish 并有返回code
        final BaseActivity activity = (BaseActivity) context;
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                activity.dismissProgressDialog();
                try {
                    String result = new String(arg2);
                    if (className == null) {
                        JSONObject jsonObject = new JSONObject(new String(arg2));
                        String code = (String) jsonObject.get("code");

                        String info = "";
                        if (jsonObject.has("info")) {
                            info = (String) jsonObject.get("info");
                        } else if (jsonObject.has("error")) {
                            info = (String) jsonObject.get("error");
                        } else {
                            info = "操作成功";
                        }
                        if (code.equals("200")) {
                            String datas = "";
                            // 在此处做判断，包含字段的话再继续从JSON中获取code字段的内容
                            if (jsonObject.has("data")) {
//                                datas = (String) jsonObject.get("data");
                            } else if (jsonObject.has("datas")) {
                                datas = (String) jsonObject.get("datas");
                            }
                            if ("".equals(datas))
                                return;

                            if ("2".equals(type)) {
                                callback.finishActivity(activity, -1);
                            } else if ("3".equals(type)) {
                                callback.finishActivity(activity, 1);
                            }

                        }
                        activity.showCustomToast(info);
                    } else {
                        BaseVO vo = (BaseVO) gson.fromJson(result, className);
                        if (BaseConstant.RESULT_FAIL_CODE400.equals(vo.getCode())) {
                            String error = vo.getError();
                            if (error != null)
                                activity.showCustomToast(error);
                            String info = vo.getInfo();
                            if (info != null)
                                activity.showCustomToast(info);
                        } else {
                            callback.baseHasData(vo);
                            if ("2".equals(type)) {
                                callback.finishActivity(activity, -1);
                            } else if ("3".equals(type)) {
                                callback.finishActivity(activity, 1);
                            }
                        }
                    }
//					if (!StringUtils.isEmpty(result)) {
//						ACache.get(context).put(allUrl + params.toString(), result);
//					}
                } catch (Exception e) {
                    System.out.println();
                    callback.finishActivity(activity, -1);
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                System.out.println("================dddd");
            }
        });
    }


    public static <T> void post(final Context context, final UpdateCallback callback, final String url, final RequestParams params, final Class<T> className, final PostCallback postCallback) {

        final BaseActivity activity = (BaseActivity) context;
        final Gson gson = new Gson();
        final String allUrl = BaseConstant.DOMAIN_NAME + url;
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                activity.dismissProgressDialog();
                try {
                    String result = new String(arg2);

                    if (postCallback == null) {
                        JSONObject jsonObject = new JSONObject(new String(arg2));
                        String code = (String) jsonObject.get("code");

                        String info = "";
                        if (jsonObject.has("info")) {
                            info = (String) jsonObject.get("info");
                        } else if (jsonObject.has("error")) {
                            info = (String) jsonObject.get("error");
                        } else {
                            info = "操作成功";
                        }

                        if (code.equals("200")) {
                            String datas = "";
                            // 在此处做判断，包含字段的话再继续从JSON中获取code字段的内容
                            if (jsonObject.has("data")) {
                                datas = (String) jsonObject.get("data");
                            } else if (jsonObject.has("datas")) {
                                datas = (String) jsonObject.get("datas");
                            }
                        } else {
                            activity.showCustomToast(info);
                        }

                    } else {
                        BaseVO vo;
                        if (className == null) {
                            vo = (BaseVO) gson.fromJson(result, BaseVO.class);
                        } else {
                            vo = (BaseVO) gson.fromJson(result, className);
                        }

                        String info = "";
                        if (vo.getError() != null) {
                            info = vo.getError();
                        } else if (vo.getInfo() != null) {
                            info = vo.getInfo();
                        } else {
                            info = "操作成功";
                        }
                        if ("200".equals(vo.getCode())) {
                            postCallback.success(vo);
                        } else {
                            activity.showCustomToast(info);
                        }
                    }
                } catch (Exception e) {
                    System.out.println();
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                activity.dismissProgressDialog();
            }
        });
    }



}
