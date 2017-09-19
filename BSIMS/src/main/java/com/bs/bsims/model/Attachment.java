package com.bs.bsims.model;

public class Attachment {
	private String title="";
	/** 1添加2取消3正常 */
	private int type=0;
	/** 文件类型后缀 */
	private String fileType="";
	/** 文件大小 */
	private float file_size=0.0f;
	
	private boolean isDel=false;
	
	/** 文件路径 */
	private String filePath="";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public float getFile_size() {
		return file_size;
	}

	public void setFile_size(float file_size) {
		this.file_size = file_size;
	}


	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

	/** 1添加2取消3正常 */
	public int getType() {
		return type;
	}

	/** 1添加2取消3正常 */
	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
