package com.bs.bsims.download.domain;

import java.util.Arrays;

/**
 * 浏览本地文件的封装
 * @author Administrator
 *
 */
public class LocalFilePathObj {
	
	//当前路径
	private String prePath;

	//此路径下的文件名称
	private String[] lists;

	public String getPrePath() {
		return prePath;
	}

	public void setPrePath(String prePath) {
		this.prePath = prePath;
	}

	public String[] getLists() {
		return lists;
	}

	public void setLists(String[] lists) {
		this.lists = lists;
	}

	@Override
	public String toString() {
		return "FilePathObj [prePath=" + prePath + ", lists="
				+ Arrays.toString(lists) + "]";
	}
}
