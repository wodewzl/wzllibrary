
package com.bs.bsims.utils;

import android.text.TextUtils;

import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;

import java.util.List;
import java.util.Map;

public class UrlUtil {

    // 日志url

    public static String getJournalListUrl(String interfaceurl, String keyword,
            String did, String positions, String postsid, String loguid,
            String isboss, String ismycc, String isbosscc, String iscomment,
            String allcc, String refresh, String id) {
        // String url = Constant.JOURNAL_LIST;
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(keyword) && keyword != null) {
            sb.append("/keyword/").append(keyword);
        }
        if (!"".equals(did) && did != null) {
            sb.append("/did/").append(did);
        }
        if (!"".equals(positions) && positions != null) {
            sb.append("/positions/").append(positions);
        }
        if (!"".equals(postsid) && postsid != null) {
            sb.append("/postsid/").append(postsid);
        }

        if (!"".equals(loguid) && loguid != null) {
            sb.append("/loguid/").append(loguid);
        }

        if (!"".equals(isboss) && isboss != null) {
            sb.append("/isboss/").append(isboss);
        }
        if (!"".equals(isbosscc) && isbosscc != null) {
            sb.append("/isbosscc/").append(isbosscc);
        }
        if (!"".equals(ismycc) && ismycc != null) {
            sb.append("/ismycc/").append(ismycc);
        }

        if (!"".equals(iscomment) && iscomment != null) {
            sb.append("/iscomment/").append(iscomment);
        }

        if (!"".equals(allcc) && allcc != null) {
            sb.append("/allcc/").append(allcc);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());

        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }

    public static String getJournalDetailUrl(String interfaceurl, String logid,
            String refresh, String id, String iscomment, String ismycc, String allcc, String loguid) {
        // String url = Constant.JOURNAL_LIST;
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(logid)) {
            sb.append("/logid/").append(logid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }
        if (!"".equals(iscomment) && iscomment != null) {
            sb.append("/iscomment/").append(iscomment);
        }
        if (!"".equals(ismycc) && ismycc != null) {
            sb.append("/ismycc/").append(ismycc);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }

        if (!"".equals(allcc) && allcc != null) {
            sb.append("/allcc/").append(allcc);
        }
        if (!"".equals(loguid) && loguid != null) {
            sb.append("/loguid/").append(loguid);
        }

        return sb.toString();
    }

    // 评论
    public static String getJournalDisscussUrl(String interfaceurl, String logid,
            String refresh, String id) {
        // String url = Constant.JOURNAL_LIST;
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(logid)) {
            sb.append("/logid/").append(logid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }

    // 提交日志

    public static String getCommitJournalUrl(String interfaceurl,
            String userid, List<String> listContent, List<String> ccList) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(userid);
        for (int i = 1; i <= listContent.size(); i++) {
            sb.append("/" + "content" + i + "/").append(listContent.get(i - 1));
        }
        if (ccList.size() > 0) {
            sb.append("/ccuids/");
            for (int i = 0; i < ccList.size(); i++) {
                sb.append(ccList.get(i));
                if (i != ccList.size() - 1) {
                    sb.append(",");
                }
                // if (i < ccList.size() - 1) {
                // sb.append(ccList.get(i)).append(",");
                // } else if (i == ccList.size() - 1) {
                // sb.append(ccList.get(i));
                // }
            }
        }
        return sb.toString();
    }

    // 通讯录
    public static String getUrl(String interfaceurl, String company, String uid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(company);
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    // 通讯录个人详情
    public static String getPersonDetailUrl(String interfaceurl, String company, String uid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(company);
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        // if (!"".equals(nbs)) {
        // sb.append("/nbs/").append(nbs);
        // }
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    // 创意意见
    public static String getIdeaUrl(String interfaceurl, String refresh,
            String id, String myuid, String type, String isboss, String isall, String unread, String todo) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }

        if (!"".equals(myuid)) {
            sb.append("/myuid/").append(myuid);
        }

        if (!"".equals(type)) {
            sb.append("/type/").append(type);
        }
        if (!"".equals(isboss)) {
            sb.append("/isboss/").append(isboss);
        }
        if (!"".equals(unread) && unread != null) {
            sb.append("/unread/").append(unread);
        }

        if (!"".equals(isall)) {
            sb.append("/isall/").append(isall);
        }

        if (!"".equals(todo) && todo != null) {
            sb.append("/todo/").append(todo);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());

        return sb.toString();
    }

    // 创意建议详情

    public static String getIdeaDteailUrl(String interfaceurl, String id, String userid, String messageid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(id)) {
            sb.append("/id/").append(id);
        }
        if (!"".equals(userid)) {
            sb.append("/userid/").append(userid);
        }

        if (!"".equals(messageid) && messageid != null) {
            sb.append("/messageid/").append(messageid);
        }

        return sb.toString();
    }

    // 建议评论
    public static String getIdeaDiscuss(String interfaceurl, String id,
            String lead, String userid, String refresh, String refreshid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl);
        sb.append(Constant.FTOKEN).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(lead)) {
            sb.append("/lead/").append(lead);
        }

        if (!"".equals(id)) {
            sb.append("/id/").append(id);
        }

        if (!"".equals(userid)) {
            sb.append("/userid/").append(userid);
        }

        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(refreshid)) {
            sb.append("/").append(refreshid);
        }
        return sb.toString();
    }

    // 考勤汇总
    public static String getAttendanceUrl(String interfaceurl, String company,
            String uid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(company);
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalUlr(String interfaceurl, String isall,
            String statusid, String modeid, String bigtypeid,
            String smalltypeid, String keyword, String refresh, String id,
            String isboss, String bossIndex, String findUid, String date) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(isall) && isall != null) {
            sb.append("/isall/").append(isall);
        }
        if (!"".equals(statusid) && statusid != null) {
            sb.append("/statusid/").append(statusid);
        }
        if (!"".equals(modeid) && modeid != null) {
            sb.append("/modeid/").append(modeid);
        }
        if (!"".equals(bigtypeid) && bigtypeid != null) {
            sb.append("/bigtypeid/").append(bigtypeid);
        }

        if (!"".equals(smalltypeid) && smalltypeid != null) {
            sb.append("/smalltypeid/").append(smalltypeid);
        }

        if (!"".equals(keyword) && keyword != null) {
            sb.append("/keyword/").append(keyword);
        }

        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id) && id != null) {
            sb.append("/").append(id);
        }
        if (!"".equals(isboss) && isboss != null) {
            sb.append("/isboss/").append(isboss);
        }
        if (!"".equals(bossIndex) && bossIndex != null) {
            sb.append("/bossIndex/").append(bossIndex);
        }
        if (!"".equals(findUid) && findUid != null) {
            sb.append("/findUid/").append(findUid);
        }
        if (!"".equals(date) && date != null) {
            sb.append("/date/").append(date);
        }

        CustomLog.e("UrlUtil", sb.toString());
        return sb.toString();
    }

    // 审批列表
    // public static String getApprovalUlr(String interfaceurl, String isall,
    // String statusid, String modeid, String bigtypeid,
    // String smalltypeid, String keyword, String refresh, String id) {
    // StringBuffer sb = new StringBuffer();
    // sb.append(BSApplication.getInstance().getHttpTitle());
    // sb.append(interfaceurl).append(
    // BSApplication.getInstance().getmCompany());
    // sb.append("/userid/").append(BSApplication.getInstance().getUserId());
    // if (!"".equals(isall) && isall != null) {
    // sb.append("/isall/").append(isall);
    // }
    // if (!"".equals(statusid) && statusid != null) {
    // sb.append("/statusid/").append(statusid);
    // }
    // if (!"".equals(modeid) && modeid != null) {
    // sb.append("/modeid/").append(modeid);
    // }
    // if (!"".equals(bigtypeid) && bigtypeid != null) {
    // sb.append("/bigtypeid/").append(bigtypeid);
    // }
    //
    // if (!"".equals(smalltypeid) && smalltypeid != null) {
    // sb.append("/smalltypeid/").append(smalltypeid);
    // }
    //
    // if (!"".equals(keyword) && keyword != null) {
    // sb.append("/keyword/").append(keyword);
    // }
    //
    // if (!"".equals(refresh) && refresh != null) {
    // sb.append("/").append(refresh);
    // }
    //
    // if (!"".equals(id) && id != null) {
    // sb.append("/").append(id);
    // }
    // CustomLog.e("UrlUtil", sb.toString());
    // return sb.toString();
    // }

    public static String getApprovalInformUrl(String interfaceurl, String uid,
            String stime, String etime, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(stime)) {
            sb.append("/stime/").append(stime);
        }
        if (!"".equals(etime)) {
            sb.append("/etime/").append(etime);
        }

        if (!"".equals(type)) {
            sb.append("/type/").append(type);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalLeaveDetailUrl(String interfaceurl, String alid, String uid, String messageid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(alid)) {
            sb.append("/alid/").append(alid);
        }
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }

        if (!"".equals(messageid) && messageid != null) {
            sb.append("/messageid/").append(messageid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalOvertimeUrk(String interfaceurl, String uid, String hours) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }

        if (!"".equals(hours) && hours != null) {
            sb.append("/hours/").append(hours);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalDetailUrl(String interfaceurl, String uid, String alid, String messageid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(alid)) {
            sb.append("/alid/").append(alid);
        }

        if (!"".equals(messageid) && messageid != null) {
            sb.append("/messageid/").append(messageid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalAttendanceUrl(String interfaceurl,
            String uid, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(type)) {
            sb.append("/type/").append(type);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String gettApprovalAttendanceProofUrl(String interfaceurl,
            String uid, String type, String sltdate) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(type)) {
            sb.append("/type/").append(type);
        }
        if (!"".equals(sltdate)) {
            sb.append("/sltdate/").append(sltdate);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getApprovalFeeApplyUlr(String interfaceurl,
            String uid, String type, String total) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(type)) {
            sb.append("/type/").append(type);
        }

        if (!"".equals(total)) {
            sb.append("/total/").append(total);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getPublishListUrl(String interfaceurl, String sortid,
            String keyword, String notice, String refresh, String id,
            String isboss) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(sortid)) {
            sb.append("/sortid/").append(sortid);
        }

        if (!"".equals(keyword)) {
            sb.append("/keyword/").append(keyword);
        }
        if (!"".equals(notice)) {
            sb.append("/notice/").append(notice);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }

        if (!"".equals(isboss) && isboss != null) {
            sb.append("/isboss/").append(isboss);
        }

        return sb.toString();
    }

    public static String getPublishDetailUrl(String interfaceurl,
            String articleid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(articleid)) {
            sb.append("/articleid/").append(articleid);
        }
        sb.append("/userid/").append(BSApplication.getInstance().getUserFromServerVO().getUserid());
        sb.append("/isboss/").append(BSApplication.getInstance().getUserFromServerVO().getIsboss());

        return sb.toString();

    }

    public static String getNotifyUrl(String interfaceurl, String userid,
            String refresh, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(userid)) {
            sb.append("/userid/").append(userid);
        }
        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }

    public static String getNoticeDiscuss(String interfaceurl,
            String articleid, String refresh, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(articleid)) {
            sb.append("/articleid/").append(articleid);
        }
        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getSuppliesUrl(String interfaceurl, String uid,
            String total) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(total)) {
            sb.append("/total/").append(total);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getSuppliesDetailUrl(String interfaceurl, String uid, String alid, String messageid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(alid)) {
            sb.append("/alid/").append(alid);
        }

        if (!"".equals(messageid) && messageid != null) {
            sb.append("/messageid/").append(messageid);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        return sb.toString();
    }

    public static String getMainMessageUrl(String interfaceurl, String userid) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        if (!"".equals(userid)) {
            sb.append("/userid/").append(userid);
        }
        return sb.toString();
    }

    // 考勤
    public static String getWorkAttendanceDetailUrl(String interfaceurl,
            String uid, String ftoken, String date) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl);
        if (!"".equals(uid)) {
            sb.append("/uid/").append(uid);
        }
        if (!"".equals(ftoken)) {
            sb.append("/ftoken/").append(ftoken);
        }

        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(date)) {
            sb.append("/d/").append(date);
        }
        return sb.toString();
    }

    public static String getMyApprovalUrl(String interfaceurl, String userid,
            String refresh, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());

        if (!"".equals(userid)) {
            sb.append("/userid/").append(userid);
        }
        if (!"".equals(refresh)) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id)) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }

    public static String getUrlByMap(String interfaceurl,
            Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        // sb.append("http://cp.beisheng.wang/");
        sb.append(interfaceurl);
        sb.append("/ftoken/").append(BSApplication.getInstance().getmCompany());
        // sb.append("/ftoken/").append("RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());

        if (null != paramsMap && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                sb.append("/");
                sb.append(entry.getKey());
                sb.append("/");
                sb.append(entry.getValue());
            }
        }
        CustomLog.e("UrlUtil-getUrlByMap", sb.toString());
        return sb.toString();
    }

    /***
     * get 方式请求 已包含 ftoken 和 userid
     * 
     * @param interfaceurl
     * @param paramsMap
     * @param refresh 决定下拉刷新 还是上拉加载
     * @param id 列表中的ID值
     * @return
     */
    public static String getUrlByMap4Refresh(String interfaceurl,
            Map<String, String> paramsMap, String refresh, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl);
        sb.append("/ftoken/").append(BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());

        if (null != paramsMap && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                sb.append("/");
                sb.append(entry.getKey());
                sb.append("/");
                sb.append(entry.getValue());
            }
        }
        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id) && id != null) {
            sb.append("/").append(id);
        }
        CustomLog.e("UrlUtil-getUrlByMap", sb.toString());
        return sb.toString();
    }

    public static String getUrlByMap1(String interfaceurl,
            Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        // sb.append("http://cp.beisheng.wang/");
        sb.append(interfaceurl);
        sb.append("/ftoken/").append(BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());

        if (paramsMap == null) {
            return sb.toString();
        }

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            sb.append("/");
            sb.append(entry.getKey());
            sb.append("/");
            sb.append(entry.getValue());
        }
        CustomLog.e("UrlUtil-getUrlByMap", sb.toString());
        return sb.toString();
    }

    // 拼接webview解析的url的路径地址的方法
    public static String getMaptoAllWebviewUrl(String interfaceurl,
            Map<String, String> paramsMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        // sb.append("http://cp.beisheng.wang/");
        sb.append(interfaceurl);
        sb.append("/ftoken/").append(BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        // sb.append("/ftoken/").append("RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        if (paramsMap == null) {
            return sb.toString();
        }
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            sb.append("/");
            sb.append(entry.getKey());
            sb.append("/");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 任务统计 饼图
     * 
     * @param interfaceurl
     * @param isall
     * @param date
     * @param statusid
     * @param modeid
     * @param refresh
     * @param id
     * @return
     */
    public static String getTaskStatisticsUlr(String interfaceurl,
            String isall, String modeid, String date, String statusid,
            String did, String refresh, String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        // sb.append("http://cp.beisheng.wang/");
        // sb.append(interfaceurl);
        // sb.append("/ftoken/").append("RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        // sb.append("/userid/").append("22");
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(isall) && isall != null) {
            sb.append("/isall/").append(isall);
        }
        if (!"".equals(modeid) && modeid != null) {
            sb.append("/modeid/").append(modeid);
        }
        if (!"".equals(statusid) && statusid != null) {
            sb.append("/statusid/").append(statusid);
        }
        if (!"".equals(did) && did != null) {
            sb.append("/did/").append(did);
        }
        if (!"".equals(date) && date != null) {
            sb.append("/date/").append(date);
        }

        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id) && id != null) {
            sb.append("/").append(id);
        }
        CustomLog.e("UrlUtil", sb.toString());
        return sb.toString();
    }

    // 全部任务列表
    public static String getTaskHomeLAVAUlr(String interfaceurl, String isall,
            String statusid, String modeid, String bigtypeid,
            String smalltypeid, String keyword, String refresh, String id,
            String isboss, String bossIndex, String date, String unread) {
        StringBuffer sb = new StringBuffer();
        sb.append(BSApplication.getInstance().getHttpTitle());
        sb.append(interfaceurl).append(
                BSApplication.getInstance().getmCompany());
        sb.append("/userid/").append(BSApplication.getInstance().getUserId());
        if (!"".equals(isall) && isall != null) {
            sb.append("/isall/").append(isall);
        }
        if (!"".equals(statusid) && statusid != null) {
            sb.append("/statusid/").append(statusid);
        }
        if (!"".equals(modeid) && modeid != null) {
            sb.append("/modeid/").append(modeid);
        }
        if (!"".equals(bigtypeid) && bigtypeid != null) {
            sb.append("/bigtypeid/").append(bigtypeid);
        }

        if (!"".equals(smalltypeid) && smalltypeid != null) {
            sb.append("/smalltypeid/").append(smalltypeid);
        }

        if (!"".equals(keyword) && keyword != null) {
            sb.append("/keyword/").append(keyword);
        }

        if (!"".equals(refresh) && refresh != null) {
            sb.append("/").append(refresh);
        }

        if (!"".equals(id) && id != null) {
            sb.append("/").append(id);
        }
        if (!"".equals(isboss) && isboss != null) {
            sb.append("/isboss/").append(isboss);
        }

        if (!"".equals(bossIndex) && bossIndex != null) {
            sb.append("/bossIndex/").append(bossIndex);
        }
        if (!"".equals(unread) && unread != null) {
            sb.append("/unread/").append(unread);
        }

        if (!"".equals(date) && date != null) {
            sb.append("/date/").append(date);
        }
        CustomLog.e("UrlUtil", sb.toString());
        return sb.toString();
    }

}
