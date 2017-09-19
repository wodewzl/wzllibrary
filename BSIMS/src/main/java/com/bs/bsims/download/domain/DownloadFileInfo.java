package com.bs.bsims.download.domain;


/**
 * 下载文件的数据库封装
 * @author TMH
 * 
 * @deprecated
 */
public class DownloadFileInfo {

	private String fileId;

	/** 文件的URL，用作key */
	private String fileDownloadUrl;

	/** 文件保存名，用于比对文件 */
	private String saveName;

	/** 文件长度，用于判断是否下载完成 */
	private long length;

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileDownloadUrl() {
		return fileDownloadUrl;
	}

	public void setFileDownloadUrl(String fileDownloadUrl) {
		this.fileDownloadUrl = fileDownloadUrl;
	}

    private int id;

    public int getId() {
        return id;
    }
}
