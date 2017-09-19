package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganBasicinfo implements Serializable {
	/*
	 * 头像以上
	 */
	private String headpic;
	private String fullname;
	private String sex;
	private String dname;
	private String pname;

	/*
	 * 住址以上
	 */
	private String birthday;
	private String hometown;
	private String accounts;
	private String nation;
	private String height;
	private String marriage;
	private String birth;
	private String address;

	/*
	 * 、 教育经历 list
	 */
	private List<DanganBasicinfo> workexp;
	private List<DanganBasicinfo> eduexp;
	private String school;
	private String specialty;
	private String education;
	private String starttime;
	private String endtime;

	/*
	 * 、 教育经历 list
	 */
	
	private String workplace;
	private String post;
	private String description;
	
	
	private List<DanganBasicinfo> array;
	
	private String code;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<DanganBasicinfo> getArray() {
		return array;
	}
	public void setArray(List<DanganBasicinfo> array) {
		this.array = array;
	}
	public String getHeadpic() {
		return headpic;
	}
	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<DanganBasicinfo> getEduexp() {
		return eduexp;
	}
	public void setEduexp(List<DanganBasicinfo> eduexp) {
		this.eduexp = eduexp;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public List<DanganBasicinfo> getWorkexp() {
		return workexp;
	}
	public void setWorkexp(List<DanganBasicinfo> workexp) {
		this.workexp = workexp;
	}
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}
	public String getPositivetime() {
		return positivetime;
	}
	public void setPositivetime(String positivetime) {
		this.positivetime = positivetime;
	}
	private String entrytime;
	private String positivetime;
	
	

}
