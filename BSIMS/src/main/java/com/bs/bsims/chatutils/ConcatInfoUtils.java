/**
 * 
 */

package com.bs.bsims.chatutils;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.emoji.EmojiconHandler;
import com.bs.bsims.emoji.EmojiconSpan;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.yzxtcp.UCSManager;
import com.yzxtcp.data.UcsErrorCode;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.listener.ILoginListener;

import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-7
 * @version 2.0
 */
// 整理通讯录跟IM关系的工具类
public class ConcatInfoUtils {

    private static ConcatInfoUtils mInstance;
    private ResultVO mResultVO;
    private List<DepartmentAndEmployeeVO> mEmpList;
    public IMJavaBean imJavaBean;

    public static ConcatInfoUtils getInstance() {
        if (mInstance == null) {
            synchronized (ConcatInfoUtils.class) {
                if (mInstance == null) {
                    mInstance = new ConcatInfoUtils();
                }
            }
        }
        return mInstance;
    }

    public ConcatInfoUtils() {
        mResultVO = BSApplication.getInstance().getResultVO();
        mEmpList = new ArrayList<DepartmentAndEmployeeVO>();
        mEmpList.clear();
        if (mResultVO != null && mResultVO.getUsers() != null) {
            mEmpList = mResultVO.getUsers();
        }
    }

    // 根据UserId查询信息
    public DepartmentAndEmployeeVO getUserByBQX(String BQXId) {
        if (mEmpList.size() == 0) {
            mResultVO = BSApplication.getInstance().getResultVO();
            mEmpList = new ArrayList<DepartmentAndEmployeeVO>();
            mEmpList.clear();
            if (mResultVO != null && mResultVO.getUsers() != null) {
                mEmpList = mResultVO.getUsers();
            }
        }
        for (int i = 0; i < mEmpList.size(); i++) {
            if (mEmpList.get(i).getUserid().equals(BQXId) && !mEmpList.get(i).getHxuname().equals("暂无")) {
                return mEmpList.get(i);
            }
        }
        return null;
    }

    // 根据IMID查询信息
    public DepartmentAndEmployeeVO getUserByIM(String IMId) {
        if (mEmpList.size() == 0) {
            mResultVO = BSApplication.getInstance().getResultVO();
            mEmpList = new ArrayList<DepartmentAndEmployeeVO>();
            mEmpList.clear();
            if (mResultVO != null && mResultVO.getUsers() != null) {
                mEmpList = mResultVO.getUsers();
            }
        }
        for (int i = 0; i < mEmpList.size(); i++) {
            if (mEmpList.get(i).getHxuname().equals(IMId) && !mEmpList.get(i).getHxuname().equals("暂无")) {
                return mEmpList.get(i);
            }
        }
        return null;
    }

    /**
     * 请求聊天接口token和聊天的userId
     **/

    public void getIMJavaBean() {
        if (BSApplication.getInstance().getIMjavaBean() != null) {
            if (!BSApplication.getInstance().isAppliactionChat()) {
                getConnEctIMs();
            }
        }
        else
        {
            final String url = BSApplication.getInstance().getHttpTitle() + Constant.IMLOGIN;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uids", BSApplication.getInstance().getUserId());
            map.put("ftoken", BSApplication.getInstance().getmCompany());
            new HttpUtilsByPC().sendPostBYPC(url, map,
                    new RequestCallBackPC() {
                        @Override
                        public void onFailurePC(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onSuccessPC(ResponseInfo rstr) {
                            // TODO Auto-generated method stub
                            Gson gson = new Gson();
                            try {
                                imJavaBean = gson.fromJson(rstr.result.toString(), IMJavaBean.class);
                                if (Constant.RESULT_CODE.equals(imJavaBean.getCode()) && null != imJavaBean.getClient() && null != imJavaBean.getClient().get(0)) {
                                    BSApplication.getInstance().saveIMjavaBean(imJavaBean);
                                    getConnEctIMs();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    public static String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        EmojiconSpan[] spans = ssb.getSpans(0, cs.length(), EmojiconSpan.class);
        for (int i = 0; i < spans.length; i++) {
            EmojiconSpan span = spans[i];
            String c = EmojiconHandler.getEmojiCode(span.getmResourceId());
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            ssb.replace(a, b, c);
        }
        ssb.clearSpans();
        return ssb.toString();
    }

    public String trantleStr(String str) {
        String systemStr = "";
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+|\\D+|[a-z]");
//        Pattern p = Pattern.compile("[\u4e00-\u9fa5]*[\\d|\\w]");
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        while (m.find()) {
            // 如果是存数字
            if (pattern.matcher(m.group()).matches()) {
                if (getUserByIM(m.group()) != null) {
                    systemStr += getUserByIM(m.group()).getFullname() + " ";
                }
                else {
                    systemStr += m.group();
                }

            }
            else {
                systemStr += m.group() + " ";
            }
        }
        return systemStr;
    }

    public void getDepartmentData(final Context context) {
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.USER_INFO;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        Gson gson = new Gson();
                        try {
                            ResultVO resultVO = gson.fromJson(rstr.result.toString(), ResultVO.class);
                            if (resultVO != null && resultVO.getCode().equals(Constant.RESULT_CODE)) {
                                BSApplication.getInstance().setResultVO(resultVO);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getConnEctIMs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                UCSManager.connect(BSApplication.getInstance().getIMjavaBean().getClient().get(0).getLoginToken(), new ILoginListener() {
                    @Override
                    public void onLogin(UcsReason reason) {
                        // TODO Auto-generated method stub
                        if (reason.getReason() == UcsErrorCode.NET_ERROR_CONNECTOK) {
                            BSApplication.getInstance().setAppliactionChat(true);
                        }
                        else {
                            BSApplication.getInstance().setAppliactionChat(false);
                            CustomLog.e("resss", "login fail errorCode = " + reason.getReason() + ",errorMsg = " + reason.getMsg());
                        }
                    }
                });
            }
        }).start();
    }

}
