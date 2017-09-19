package com.bs.bsims.receiver;


public class FileDownloadSuccessReceiver {

	private String TAG = "FileDownloadSuccessReceiver";
	
	/** 本地文件删除时候的广播 */
	public static final String receiverAction = "fileLocalDeleteSuccess";

	/** key:删除本地文件 */
	public static final String keyDeleteLocalFile = "keyDeleteLocalFile";
	
	/** key:删除网络文件 */
	public static final String keyDeleteNetFile = "keyDeleteNetFile";
	
	/** key:上传文件成功 */
	public static final String keyUploadFile = "keyUploadFile";
	
	/** key:下载文件成功 */
	public static final String keyDownloadFile = "keyDownloadFile";
	
}
