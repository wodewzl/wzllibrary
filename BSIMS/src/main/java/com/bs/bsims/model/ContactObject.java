package com.bs.bsims.model;

import java.io.Serializable;

/**
 * 编辑联系人的Obj， 用于界面上的回调传值 也用于手机联系人
 * 
 * 客户联系人
 * 
 * @author Administrator
 * 
 */
public class ContactObject implements Serializable {

	private static final long serialVersionUID = 1764838514784027105L;

	private String company;
	private String firstLetters; // 显示数据拼音的首字母

	// ********* 上面为应用程序本地使用的字段，下面为请求服务器解析JSON所用字段 ******************
	
	private int lid = 0;
	private String names="";
	private String cids="";
	private String pics="";
	private String phones="";
	private String qqs="";
	private String emails="";
	private String birthdays="";
	private String remarks="";
	private String jobs="";
	private String departments="";
	private String userid="";
	private String picUrl="";
	private String customerName="";

	private String portraitPath = "";
	private String portraitName = "";
	
	/** type 1 添加 2 删除 3确认 0普通*/
	private int type ;
	/** 添加删除确认图片 */
	private int image;

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ContactObject() {
		super();
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getLid() {
		return lid;
	}

	public void setLid(int lid) {
		this.lid = lid;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public String getQqs() {
		return qqs;
	}

	public void setQqs(String qqs) {
		this.qqs = qqs;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getBirthdays() {
		return birthdays;
	}

	public void setBirthdays(String birthdays) {
		this.birthdays = birthdays;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getFirstLetters() {
		return firstLetters;
	}

	public void setFirstLetters(String firstLetters) {
		this.firstLetters = firstLetters;
	}

	public String getPortraitPath() {
		return portraitPath;
	}

	public void setPortraitPath(String portraitPath) {
		this.portraitPath = portraitPath;
	}

	public String getPortraitName() {
		return portraitName;
	}

	public void setPortraitName(String portraitName) {
		this.portraitName = portraitName;
	}

	@Override
	public String toString() {
		return "ContactObject [names=" + names + ", type=" + type + "]";
	}

	
	
	
}
