package com.bs.bsims.model;

import java.io.Serializable;

/** 
 * @author peck
 * @Description:  任务详情中的  附件
 * @date 2015-5-22 下午3:24:33 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class TaskEventDetailsView_Info_AnnexVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7444084727894024624L;
	
//	{
//		"title":"员工访谈配置.docx",
//		"typename":"docx",
//		"filesize":"10.82 KB",
//		"filepath":"http://cp.beisheng.wang/Uploads/bs0001/Task/annex/20150521/555dc009cdf7d.docx"
//		}
	/** 任务id */
	private String title = "";
	/** 任务id */
	private String typename = "";
	/** 任务id */
	private String filesize = "";
	/** 任务id */
	private String filepath = "";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
