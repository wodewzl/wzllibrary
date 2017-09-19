package com.bs.bsims.download.domain;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下载页面正在下载，退出页面后重回页面，进度条要继续显示数据，此类用于缓存控件的引用
 * @author Administrator
 *
 */
public class ViewCache {
	
	//下载的界面进度显示
	public static TextView txt_downloadDes; 
	public static ProgressBar progressBar;
	
	
	//下载完成后界面的变化
	public static View layout_fileNotDownloadLayout;
	public static View layout_fileDownloadingLayout;
	public static ImageView img_downloadCancel;
	public static Button btnDownloadFile;
	public static TextView txtDownloadPath;
	
}
