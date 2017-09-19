
package com.bs.bsims.xutils.impl;


import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.HttpException;

/**
 * @author 梁瞎名
 * @EName LQC
 * @Important Nice
 * @Stoyre 帅的无与伦比
 * @date 2016-1-8 下午4:45:55
 */
public abstract class RequestCallBackPC implements CommonCallback<String> {
    private String TAG = "RequestCallBackPC";

    @Override
    public void onError(Throwable ex, boolean arg1) {

        // TODO Auto-generated method stub
        if (ex instanceof HttpException) // 网络错误
            onFailurePC((HttpException) ex, arg1 + "");
        else
            onFailurePC(null, arg1 + "");
    }

    @Override
    public void onSuccess(String arg0) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setResult(arg0);
        onSuccessPC(responseInfo);
    }

    @Override
    public void onCancelled(CancelledException arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinished() {
        // TODO Auto-generated method stub

    }

    public abstract void onFailurePC(HttpException arg0, String arg1);

    public abstract void onSuccessPC(ResponseInfo arg0);

    public class ResponseInfo {
        public String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
