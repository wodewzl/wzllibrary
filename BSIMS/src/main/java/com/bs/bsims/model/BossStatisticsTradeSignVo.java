
package com.bs.bsims.model;

import java.util.List;

public class BossStatisticsTradeSignVo {
    private BossStatisticsTradeSignVo info;
    private String totalCount;
    private String countPerUser;
    private List<BossStatisticsTradeSignVo> list;
    private String uid;
    private String fullname;
    private String signCount;
    private String signCountShow;
    private List<CrmTranctVo> contracts;

    private String code;
    private String retinfo;
    private String system_time;

    public String getSignCountShow() {
        return signCountShow;
    }

    public void setSignCountShow(String signCountShow) {
        this.signCountShow = signCountShow;
    }

    public BossStatisticsTradeSignVo getInfo() {
        return info;
    }

    public void setInfo(BossStatisticsTradeSignVo info) {
        this.info = info;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCountPerUser() {
        return countPerUser;
    }

    public void setCountPerUser(String countPerUser) {
        this.countPerUser = countPerUser;
    }

    public List<BossStatisticsTradeSignVo> getList() {
        return list;
    }

    public void setList(List<BossStatisticsTradeSignVo> list) {
        this.list = list;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSignCount() {
        return signCount;
    }

    public void setSignCount(String signCount) {
        this.signCount = signCount;
    }

    public List<CrmTranctVo> getContracts() {
        return contracts;
    }

    public void setContracts(List<CrmTranctVo> contracts) {
        this.contracts = contracts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

}
