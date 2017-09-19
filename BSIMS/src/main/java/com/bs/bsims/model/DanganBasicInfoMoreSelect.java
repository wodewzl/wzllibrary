package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class DanganBasicInfoMoreSelect implements Serializable {

	private List<DanganBasicInfoMoreSelect> array;
	private String type;
	private String name;
	private List<DanganBasicInfoMoreSelect> list;
	private String id;
	private String code;

	public List<DanganBasicInfoMoreSelect> getArray() {
		return array;
	}

	public void setArray(List<DanganBasicInfoMoreSelect> array) {
		this.array = array;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DanganBasicInfoMoreSelect> getList() {
		return list;
	}

	public void setList(List<DanganBasicInfoMoreSelect> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
