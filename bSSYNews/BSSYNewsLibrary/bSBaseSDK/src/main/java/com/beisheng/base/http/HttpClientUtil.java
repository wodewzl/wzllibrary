
package com.beisheng.base.http;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {
    private static String TAG = "CustomHttpClient";
    private static final String CHARSET_UTF8 = HTTP.UTF_8;
    private static final String CHARSET_GB2312 = "GB2312";
    private static DefaultHttpClient customerHttpClient;
    private static CookieStore cookieStore;

    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    private HttpClientUtil() {

    }

    /**
     * HttpClient post方法
     * 
     * @param url
     * @param nameValuePairs
     * @return
     * @throws IOException
     */
    public static String PostFromWebByHttpClient(Context context, String url,
            NameValuePair... nameValuePairs) throws IOException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (nameValuePairs != null) {
                for (int i = 0; i < nameValuePairs.length; i++) {
                    params.add(nameValuePairs[i]);
                }
            }
            UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
                    CHARSET_GB2312);
            Log.i(TAG, url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(urlEncoded);
            DefaultHttpClient client = getHttpClient(context);
            if (cookieStore != null) {
                client.setCookieStore(cookieStore);
            }
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            cookieStore = client.getCookieStore();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "----UnsupportedEncodingException" + e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            Log.w(TAG, "----ClientProtocolException" + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.w(TAG, "----IOException" + e.getMessage());
            return null;
        }
    }

    public static String getFromWebByHttpClient(Context context, String url,
            NameValuePair... nameValuePairs) throws Exception {
        // http地址
        // String httpUrl =
        // "http://192.168.1.110:8080/httpget.jsp?par=HttpClient_android_Get";
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (nameValuePairs != null && nameValuePairs.length > 0) {
            sb.append("?");
            for (int i = 0; i < nameValuePairs.length; i++) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s",
                        nameValuePairs[i].getName(),
                        nameValuePairs[i].getValue()));
            }
        }
        Log.i(TAG, sb.toString());
        // HttpGet连接对象
        HttpGet httpRequest = new HttpGet(sb.toString());
        // 取得HttpClient对象
        DefaultHttpClient httpclient = getHttpClient(context);
        if (cookieStore != null) {
            httpclient.setCookieStore(cookieStore);
        }
        // 请求HttpClient，取得HttpResponse
        HttpResponse httpResponse = httpclient.execute(httpRequest);
        // 请求成功
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("连接失败,请稍候再试");
        }
        cookieStore = httpclient.getCookieStore();
        return EntityUtils.toString(httpResponse.getEntity());

    }

    /**
     * 创建httpClient实例
     * 
     * @return
     * @throws Exception
     */
    private static synchronized DefaultHttpClient getHttpClient(Context context) {
        HttpParams params = new BasicHttpParams();
        // 设置�?��基本参数
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
        HttpProtocolParams.setUseExpectContinue(params, true);

        HttpProtocolParams
                .setUserAgent(
                        params,
                        "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // 超时设置
        /* 从连接池中取连接的超时时�? */
        ConnManagerParams.setTimeout(params, 10000);
        /* 连接超时 */
        int ConnectionTimeOut = 30000;
        if (!HttpUtils.isWifiDataEnable(context)) {
            ConnectionTimeOut = 100000;
        }
        HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeOut);
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(params, 40000);
        // 设置我们的HttpClient支持HTTP和HTTPS两种模式
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));

        // 使用线程安全的连接管理来创建HttpClient
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                params, schReg);
        customerHttpClient = new DefaultHttpClient(conMgr, params);
        return customerHttpClient;
    }

    /**
     * get请求
     * 
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(Context context, String urlString, Map<String, String> params) {

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlString);

            if (null != params && params.size() > 0) {

                urlBuilder.append("/");

                Iterator<Entry<String, String>> iterator = params.entrySet()
                        .iterator();

                while (iterator.hasNext()) {
                    Entry<String, String> param = iterator.next();
                    if (param.getValue() != null && !"".equals(param.getValue())) {
                        urlBuilder
                                .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                                .append('/')
                                .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                        if (iterator.hasNext()) {
                            urlBuilder.append('/');
                        }
                    }

                }
            }
            // 创建HttpClient对象
            DefaultHttpClient client = getHttpClient(context);
            // 发送get请求创建HttpGet对象
            HttpGet getMethod = new HttpGet(urlBuilder.toString());
            Log.i("get_url", urlBuilder.toString());
            HttpResponse response = client.execute(getMethod);
            // 获取状态码
            int res = response.getStatusLine().getStatusCode();
            if (res == 200) {
                StringBuilder builder = new StringBuilder();
                // 获取响应内容
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                for (String s = reader.readLine(); s != null; s = reader
                        .readLine()) {
                    builder.append(s);
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求
     * 
     * @param urlString
     * @param params
     * @return
     */
    public static String postRequest(Context context, String urlString,
            List<BasicNameValuePair> params) {

        try {
            // 1. 创建HttpClient对象
            DefaultHttpClient client = getHttpClient(context);
            // 2. 发get请求创建HttpGet对象
            HttpPost postMethod = new HttpPost(urlString);
            postMethod.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(postMethod);
            int statueCode = response.getStatusLine().getStatusCode();
            if (statueCode == 200) {
                System.out.println(statueCode);
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {

        }
        return null;
    }
}
