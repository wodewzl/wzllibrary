package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class CrmProductVo implements Serializable {
	/**
	 * Crm产品的实体类
	 */

	private List<CrmProductVo> array;
	private String pid;
	private String name;
	private String money;
	private String unit;
	private String remark;
	private String count;
	private String code;
	private String totalPrice;

	private List<String> years;

	/**
	 * 
	 * 犹豫不想去创建modle类 这里就用这个 boss统计首页的model在这
	 * 
	 * 
	 * */

	private CrmProductVo info;
	private List<CrmProductVo> selected;
	private List<CrmProductVo> unselected;
	private String id;
	private String data_right;
	private List<String> data_left;
	private String right_name;
	

	public String getRight_name() {
		return right_name;
	}

	public void setRight_name(String right_name) {
		this.right_name = right_name;
	}

	public CrmProductVo getInfo() {
		return info;
	}

	public void setInfo(CrmProductVo info) {
		this.info = info;
	}

	public List<CrmProductVo> getSelected() {
		return selected;
	}

	public void setSelected(List<CrmProductVo> selected) {
		this.selected = selected;
	}

	public List<CrmProductVo> getUnselected() {
		return unselected;
	}

	public void setUnselected(List<CrmProductVo> unselected) {
		this.unselected = unselected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData_right() {
		return data_right;
	}

	public void setData_right(String data_right) {
		this.data_right = data_right;
	}

	public List<String> getData_left() {
		return data_left;
	}

	public void setData_left(List<String> data_left) {
		this.data_left = data_left;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	/**
	 * Crm销售线索
	 */

	private String tid;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	private String type;
	// private String name;部门名称
	private String title;// 季度名称
	private String target;// 目标金额
	private String actual;// 完成金额
	private String ratio;// 百分比ios用
	private String percent;// 百分比android用

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<CrmProductVo> getArray() {
		return array;
	}

	public void setArray(List<CrmProductVo> array) {
		this.array = array;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
