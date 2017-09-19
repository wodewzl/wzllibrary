package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 1. 任务统计首页接口 返回参数 图表展示折线图 chart  考勤统计首页接口 特有的
 * @date 2015-6-19 下午2:30:37
 * @email 971371860@qq.com
 * @version V1.0
 */

public class StatisticsBossTaskIndex2Chart implements Serializable {

	private static final long serialVersionUID = 7914212009618201166L;

	private List<StatisticsBossTaskIndex> list;
	private String status;
	/**
	 * 6. 考勤统计首页接口 特有的
	 */
	private String did;
	/**
	 * 6. 考勤统计首页接口 特有的
	 */
	private String name;

	public List<StatisticsBossTaskIndex> getList() {
		return list;
	}

	public void setList(List<StatisticsBossTaskIndex> list) {
		this.list = list;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
