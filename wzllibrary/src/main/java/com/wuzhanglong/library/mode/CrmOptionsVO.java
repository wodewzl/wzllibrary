package com.wuzhanglong.library.mode;

import java.io.Serializable;
import java.util.List;

public class CrmOptionsVO extends BaseVO implements Serializable {
	private String count;
	private List<CrmOptionsVO> array;
	private String key;
	private List<CrmOptionsVO> option;
	private String id;
	private String name;
	private List<CrmOptionsVO> info;
	private List<CrmOptionsVO> industry;
	private List<CrmOptionsVO> level;
	private List<CrmOptionsVO> source;

	private List<String> years;

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	/**
	 * 接口筛选更改
	 */

	private List<CrmOptionsVO> left;
	private List<CrmOptionsVO> right;


	public void setInfo(List<CrmOptionsVO> info) {
		this.info = info;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<CrmOptionsVO> getOption() {
		return option;
	}

	public void setOption(List<CrmOptionsVO> option) {
		this.option = option;
	}

	public void setArray(List<CrmOptionsVO> array) {
		this.array = array;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CrmOptionsVO> getArray() {
		return array;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CrmOptionsVO> getIndustry() {
		return industry;
	}

	public void setIndustry(List<CrmOptionsVO> industry) {
		this.industry = industry;
	}

	public List<CrmOptionsVO> getLevel() {
		return level;
	}

	public void setLevel(List<CrmOptionsVO> level) {
		this.level = level;
	}

	public List<CrmOptionsVO> getSource() {
		return source;
	}

	public void setSource(List<CrmOptionsVO> source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return id + "," + name;
	}

	public List<CrmOptionsVO> getLeft() {
		return left;
	}

	public void setLeft(List<CrmOptionsVO> left) {
		this.left = left;
	}

	public List<CrmOptionsVO> getRight() {
		return right;
	}

	public void setRight(List<CrmOptionsVO> right) {
		this.right = right;
	}
}
