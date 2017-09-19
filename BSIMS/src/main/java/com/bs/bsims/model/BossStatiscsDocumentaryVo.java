/**
 * 
 */
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * BS北盛最帅程序员
 * 
 * Copyright (c) 2016
 * 
 * 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * 
 * @date 2016-1-20
 * 
 * @version 1.22
 */
public class BossStatiscsDocumentaryVo implements Serializable {

	private BossStatiscsDocumentaryVo info;
	private String totalCustomerCount;
	private String totalVisitCount;
	private String visitPerCustomer;
	private List<BossStatiscsDocumentaryVo> charts;
	private String id;
	private String name;
	private String count;
	private String perCustomer;
	private String percent;
	private String rate;
	private List<CrmStatisticsVisitorVO> customers;
	private String cid;
	private String cname;
	public BossStatiscsDocumentaryVo getInfo() {
		return info;
	}
	public void setInfo(BossStatiscsDocumentaryVo info) {
		this.info = info;
	}
	public String getTotalCustomerCount() {
		return totalCustomerCount;
	}
	public void setTotalCustomerCount(String totalCustomerCount) {
		this.totalCustomerCount = totalCustomerCount;
	}
	public String getTotalVisitCount() {
		return totalVisitCount;
	}
	public void setTotalVisitCount(String totalVisitCount) {
		this.totalVisitCount = totalVisitCount;
	}
	public String getVisitPerCustomer() {
		return visitPerCustomer;
	}
	public void setVisitPerCustomer(String visitPerCustomer) {
		this.visitPerCustomer = visitPerCustomer;
	}
	public List<BossStatiscsDocumentaryVo> getCharts() {
		return charts;
	}
	public void setCharts(List<BossStatiscsDocumentaryVo> charts) {
		this.charts = charts;
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
	public String getPerCustomer() {
		return perCustomer;
	}
	public void setPerCustomer(String perCustomer) {
		this.perCustomer = perCustomer;
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
 
	public List<CrmStatisticsVisitorVO> getCustomers() {
		return customers;
	}
	public void setCustomers(List<CrmStatisticsVisitorVO> customers) {
		this.customers = customers;
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
	public String getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(String visitCount) {
		this.visitCount = visitCount;
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
	private String visitCount;
	private String code;
	private String retinfo;

}
