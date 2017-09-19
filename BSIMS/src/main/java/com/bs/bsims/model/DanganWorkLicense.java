package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganWorkLicense implements Serializable {

	private String name;
	private String type;
	private String photo;
	private List<DanganWorkLicense> array;
	private String count;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	 
	public List<DanganWorkLicense> getArray() {
		return array;
	}
	public void setArray(List<DanganWorkLicense> array) {
		this.array = array;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}



}
