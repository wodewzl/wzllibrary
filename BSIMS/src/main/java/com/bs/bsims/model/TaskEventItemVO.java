package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-5-21 下午4:18:45
 * @email 971371860@qq.com
 * @version V1.0
 */
public class TaskEventItemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4796627130164894718L;

	private List<TaskEventItem> array;
	private String count;
	private String code;
	private String retinfo;
	private String system_time;
	public List<TaskEventItem> getArray() {
		return array;
	}
	public void setArray(List<TaskEventItem> array) {
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
