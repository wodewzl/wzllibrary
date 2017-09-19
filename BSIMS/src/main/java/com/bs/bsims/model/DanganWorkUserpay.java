package com.bs.bsims.model;

import java.util.List;

public class DanganWorkUserpay {

	private String time;

	private String news;

	private String pay;

	private String type;

	private String count;
	
	private String code;
	
	private List<DanganWorkUserpay> array;

	 
	public List<DanganWorkUserpay> getArray() {
		return array;
	}

	public void setArray(List<DanganWorkUserpay> array) {
		this.array = array;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
