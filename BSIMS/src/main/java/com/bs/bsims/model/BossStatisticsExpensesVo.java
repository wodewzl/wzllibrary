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
 * @date 2016-2-14
 * 
 * @version 1.22
 */
public class BossStatisticsExpensesVo implements Serializable {
	
	private BossStatisticsExpensesVo info;
	private String total;
	private String allnum;	
	private List<BossStatisticsExpensesVo> chart;	
	private String name;	
	private String percentnum;	
	private String percent;	
	private List<BossStatisticsExpensesVo> list;
	private List<BossStatisticsExpensesVo> show;
	private String did;
	private String dname;	
	private String num;	
	private String contrast;	
	private String code;	
	private String retinfo;
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public BossStatisticsExpensesVo getInfo() {
		return info;
	}
	public void setInfo(BossStatisticsExpensesVo info) {
		this.info = info;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAllnum() {
		return allnum;
	}
	public void setAllnum(String allnum) {
		this.allnum = allnum;
	}
	public List<BossStatisticsExpensesVo> getChart() {
		return chart;
	}
	public void setChart(List<BossStatisticsExpensesVo> chart) {
		this.chart = chart;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPercentnum() {
		return percentnum;
	}
	public void setPercentnum(String percentnum) {
		this.percentnum = percentnum;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public List<BossStatisticsExpensesVo> getList() {
		return list;
	}
	public void setList(List<BossStatisticsExpensesVo> list) {
		this.list = list;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getContrast() {
		return contrast;
	}
	public void setContrast(String contrast) {
		this.contrast = contrast;
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
	public List<BossStatisticsExpensesVo> getShow() {
		return show;
	}
	public void setShow(List<BossStatisticsExpensesVo> show) {
		this.show = show;
	}
}
