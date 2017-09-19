
package com.bs.bsims.model;

import java.util.List;

public class BossStatisticsClientVO {
    private BossStatisticsClientVO info;
    private String totalCount;
    private String newCount;
    private String publicCount;
    private String loseCount;
    private String charts1_max;
    private String charts2_max;
    private String charts3_max;
    private String charts4_max;
    private List<BossStatisticsClientVO> charts1;
    private List<BossStatisticsClientVO> charts2;
    private List<BossStatisticsClientVO> charts3;
    private List<BossStatisticsClientVO> charts4;
    private String id;
    private String name;
    private String count;
    private String percent;
    private String rate;
    private String fullname;
    private String userid;
    private String money;
    private String totalMoney;

    private String code;
    private String retinfo;
    private String system_time;

    public String getCharts4_max() {
        return charts4_max;
    }

    public void setCharts4_max(String charts4_max) {
        this.charts4_max = charts4_max;
    }

    public String getCharts3_max() {
        return charts3_max;
    }

    public void setCharts3_max(String charts3_max) {
        this.charts3_max = charts3_max;
    }

    public String getCharts2_max() {
        return charts2_max;
    }

    public void setCharts2_max(String charts2_max) {
        this.charts2_max = charts2_max;
    }

    public String getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(String loseCount) {
        this.loseCount = loseCount;
    }

    public String getCharts1_max() {
        return charts1_max;
    }

    public void setCharts1_max(String charts1_max) {
        this.charts1_max = charts1_max;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getNewCount() {
        return newCount;
    }

    public void setNewCount(String newCount) {
        this.newCount = newCount;
    }

    public String getPublicCount() {
        return publicCount;
    }

    public void setPublicCount(String publicCount) {
        this.publicCount = publicCount;
    }

    public List<BossStatisticsClientVO> getCharts3() {
        return charts3;
    }

    public void setCharts3(List<BossStatisticsClientVO> charts3) {
        this.charts3 = charts3;
    }

    public List<BossStatisticsClientVO> getCharts4() {
        return charts4;
    }

    public void setCharts4(List<BossStatisticsClientVO> charts4) {
        this.charts4 = charts4;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BossStatisticsClientVO getInfo() {
        return info;
    }

    public void setInfo(BossStatisticsClientVO info) {
        this.info = info;
    }

    public List<BossStatisticsClientVO> getCharts1() {
        return charts1;
    }

    public void setCharts1(List<BossStatisticsClientVO> charts1) {
        this.charts1 = charts1;
    }

    public List<BossStatisticsClientVO> getCharts2() {
        return charts2;
    }

    public void setCharts2(List<BossStatisticsClientVO> charts2) {
        this.charts2 = charts2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
