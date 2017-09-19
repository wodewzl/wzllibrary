package com.bs.bsims.model;
import java.io.Serializable;
import java.util.List;

public class DanganWorkInterview implements Serializable {

	private String interviewtime;
	private String interviewcontent;
	private String typename;
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

	private String count;
	private String code;
	private List<DanganWorkInterview> array;

	public List<DanganWorkInterview> getArray() {
		return array;
	}

	public void setArray(List<DanganWorkInterview> array) {
		this.array = array;
	}

	public String getInterviewtime() {
		return interviewtime;
	}

	public void setInterviewtime(String interviewtime) {
		this.interviewtime = interviewtime;
	}

	public String getInterviewcontent() {
		return interviewcontent;
	}

	public void setInterviewcontent(String interviewcontent) {
		this.interviewcontent = interviewcontent;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

}
