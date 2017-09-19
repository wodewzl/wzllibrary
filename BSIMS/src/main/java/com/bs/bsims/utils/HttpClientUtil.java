
package com.bs.bsims.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {

    // static DefaultHttpClient httpClient = new DefaultHttpClient();
    private static DefaultHttpClient httpClient;

    public static synchronized HttpClient getHttpClient() {
        // DefaultHttpClient httpClient = null;
        if (null == httpClient) {
            HttpParams params = new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    Constant.ENCODING);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
                    .setUserAgent(
                            params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 10000);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 10000);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            httpClient = new DefaultHttpClient(conMgr, params);
        }
        return httpClient;
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            return new DefaultHttpClient(ccm, params);

        } catch (Exception e) {
            return new DefaultHttpClient();
        }

        // AsyncHttpClient client = new AsyncHttpClient();
        // client.setConnectTimeout(15000);
        // client.setTimeout(15000);
        // return client.getHttpClient();
    }

    public static String get(String url, String encoding) throws Exception {
        // HttpParams params = new BasicHttpParams();
        // HttpConnectionParams.setSoTimeout(params, 10000);
        // HttpConnectionParams.setConnectionTimeout(params, 10000);
        // DefaultHttpClient httpClient = new DefaultHttpClient(params);
        // HttpGet httpGet = new HttpGet(url);
        // HttpResponse res = httpClient.execute(httpGet);

        // 创建HttpClient对象
        HttpClient client = getNewHttpClient();
        // 发送get请求创建HttpGet对象
        HttpGet getMethod = new HttpGet(url);
        HttpResponse response = client.execute(getMethod);

        // HttpResponse res = httpClient.execute(httpGet);
        return getContent(response, encoding);
    }

    public static String get(String url, String encoding, DefaultHttpClient client) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse res = client.execute(httpGet);
        return getContent(res, encoding);
    }

    public static String post(String url, StringEntity se, String host, String referer, String encoding) throws Exception {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(params, 10000);
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(se);
        httpPost.setHeader("Host", host);
        httpPost.setHeader("Referer", referer);
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Accept-Language", "zh-cn");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("UA-CPU", "x86");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 2.0.50727; InfoPath.2; CIBA)");
        httpPost.setHeader("Connection", "close");
        HttpResponse response = httpClient.execute(httpPost);

        return getContent(response, encoding);
    }

    public static String httpPost(String url, String queryString, String encoding) throws Exception {
        // HttpParams params = new BasicHttpParams();
        // HttpConnectionParams.setSoTimeout(params, 8000);
        // HttpConnectionParams.setConnectionTimeout(params, 8000);
        // DefaultHttpClient httpClient = new DefaultHttpClient(params);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(queryString));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.getParams().setParameter("http.socket.timeout", new Integer(20000));
        httpPost.setHeader("Connection", "close");
        HttpResponse response = getHttpClient().execute(httpPost);
        // HttpResponse response = httpClient.execute(httpPost);
        return getContent(response, encoding);
    }

    public static String getContent(HttpResponse res, String encoding) throws Exception {
        // HttpEntity ent = res.getEntity();
        //
        //
        // BufferedReader br = new BufferedReader(new
        // InputStreamReader(ent.getContent(), encoding));
        // StringBuilder sb = new StringBuilder();
        // String line = null;
        // while((line = br.readLine()) != null)
        // {
        // sb.append(line + "\n");
        // }
        // ent.consumeContent();
        // return sb.toString();
        HttpEntity ent = res.getEntity();
        String result = IOUtils.toString(ent.getContent(), encoding);
        ent.consumeContent();
        CustomLog.e("HttpClientUtil184", result);
        return result;
    }

    public static InputStream getStream(String url) throws Exception {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(params, 8000);
        HttpConnectionParams.setConnectionTimeout(params, 8000);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse res = httpClient.execute(httpGet);
        return res.getEntity().getContent();
    }

    public static InputStream getStream(String url, DefaultHttpClient client) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR 2.0.50727; InfoPath.2; CIBA)");
        httpGet.setHeader("Referer", "http://reg.126.com/regmail126/userRegist.do?action=fillinfo");
        // httpGet.setHeader("Accept", "*/*");
        // httpGet.setHeader("Accept-Language", "zh-cn");
        // httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Connection", "close");
        HttpResponse res = client.execute(httpGet);
        return res.getEntity().getContent();
    }

    public static void main(String args[]) throws Exception {
        String urlString = "http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx/getGeoIPContext";
        String value = HttpClientUtil.get(urlString, "utf-8");
        System.out.println(value);
    }

    public static boolean post(String actionUrl, Map<String, String> params,
            Map<String, File> files) throws IOException {

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(conn
                .getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1
                        .append("Content-Disposition: form-data; name=\"file[]\"; filename=\""
                                + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                in = conn.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
                System.out.println(sb2.toString());
                return true;
            }
            outStream.close();
            conn.disconnect();
        }
        // return in.toString();
        return false;
    }

    /**
     * get请求
     * 
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(String urlString, Map<String, String> params) {

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlString);

            if (null != params) {

                urlBuilder.append("?");

                Iterator<Entry<String, String>> iterator = params.entrySet()
                        .iterator();

                while (iterator.hasNext()) {
                    Entry<String, String> param = iterator.next();
                    if (param.getValue() != null && !"".equals(param.getValue())) {
                        urlBuilder
                                .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                                .append('=')
                                .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                        if (iterator.hasNext()) {
                            urlBuilder.append('&');
                        }
                    }

                }
            }
            // 创建HttpClient对象
            // HttpClient client = getNewHttpClient();
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            HttpClient client = asyncHttpClient.getHttpClient();
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
    public static String postRequest(String urlString,
            List<BasicNameValuePair> params) {

        try {
            // 1. 创建HttpClient对象
            HttpClient client = getNewHttpClient();
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

    public void commit(final Activity activity, RequestParams params) {
        CustomDialog.showProgressDialog(activity, "正在提交数据...");

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());

            String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_ADD;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(new String(arg2));
                        String str = (String) jsonObject.get("retinfo");
                        String code = (String) jsonObject.get("code");
                        if (Constant.RESULT_CODE.equals(code)) {
                            activity.finish();
                        }
                        CustomToast.showLongToast(activity, str);
                    } catch (Exception e) {
                    } finally {
                        CustomDialog.closeProgressDialog();
                    }

                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void get(String url, ResponseHandlerInterface responseHandler) {
        // client.get(Constant.BASEURL + url, responseHandler);
    }

    public static void getImage(String url, ResponseHandlerInterface responseHandler) {
        // client.get(url, responseHandler);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
