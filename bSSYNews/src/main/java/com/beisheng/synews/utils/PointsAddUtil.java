
package com.beisheng.synews.utils;

import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class PointsAddUtil {
    public static void commitdPoints(String type, String contentid, String title) {
        RequestParams params = new RequestParams();
        try {
            if (AppApplication.getInstance().getUserInfoVO() != null) {
                params.put("uid", AppApplication.getInstance().getUserInfoVO().getUid());
            }
            params.put("type", type);
            params.put("contentid", contentid);
            params.put("title", title);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constant.DOMAIN_NAME + Constant.SHOP_ADD_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
