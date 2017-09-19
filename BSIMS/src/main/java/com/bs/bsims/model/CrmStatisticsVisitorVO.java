
package com.bs.bsims.model;

import java.util.List;

public class CrmStatisticsVisitorVO {
    private String code;
    private String retinfo;
    private String system_time;
    private String count;

    private List<CrmStatisticsVisitorVO> array;
    private List<CrmStatisticsVisitorVO> customers;
    private CrmStatisticsVisitorVO info;
    private String totalCount;
    private String visitPerDay;
	private String uid;
    private String fullname;
    private String visitCount;
    private String cid;
    private String cname;
    private String name;
    public CrmStatisticsVisitorVO getInfo() {
  		return info;
  	}

  	public void setInfo(CrmStatisticsVisitorVO info) {
  		this.info = info;
  	}

  	public String getTotalCount() {
  		return totalCount;
  	}

  	public void setTotalCount(String totalCount) {
  		this.totalCount = totalCount;
  	}

  	public String getVisitPerDay() {
  		return visitPerDay;
  	}

  	public void setVisitPerDay(String visitPerDay) {
  		this.visitPerDay = visitPerDay;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<CrmStatisticsVisitorVO> getArray() {
        return array;
    }

    public void setArray(List<CrmStatisticsVisitorVO> array) {
        this.array = array;
    }

    public List<CrmStatisticsVisitorVO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CrmStatisticsVisitorVO> customers) {
        this.customers = customers;
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

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
