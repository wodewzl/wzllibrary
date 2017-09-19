package com.bs.bsims.utils;

import java.util.HashMap;
import java.util.Map;

import com.bs.bsims.R;

public class FileIconMapping {
	
	private static final Map<String, Integer> mapping = new HashMap<String, Integer>();
	
	static {
		mapping.put("doc", R.drawable.ic_download_word_big);
		mapping.put("docx", R.drawable.ic_download_word_big);
		mapping.put("ppt", R.drawable.ic_download_ppt_big);
		mapping.put("xls", R.drawable.ic_download_excel_big);
		mapping.put("xlsl", R.drawable.ic_download_excel_big);
		mapping.put("jpg", R.drawable.ic_download_jpg_big);
		mapping.put("png", R.drawable.ic_download_jpg_big);
		mapping.put("zip", R.drawable.ic_download_zip_big);
		mapping.put("*", R.drawable.ic_download_unknown_big);
	}
	
	
	
	public static Integer getIcon(String key) {
		Integer integer;
		boolean containsKey = mapping.containsKey(key);
		
		if(containsKey)
			integer = mapping.get(key);
		else
			integer = mapping.get("*");
		
		return integer;
	}
	
	public static Integer getIcon_task(String key) {
		Integer integer;
		boolean containsKey = mapping_task.containsKey(key);
		
		if(containsKey)
			integer = mapping_task.get(key);
		else
			integer = mapping_task.get("*");
		
		return integer;
	}
	
private static final Map<String, Integer> mapping_task = new HashMap<String, Integer>();
	
	static {
		mapping_task.put(".doc", R.drawable.ic_download_word_big);
		mapping_task.put(".docx", R.drawable.ic_download_word_big);
		mapping_task.put(".ppt", R.drawable.ic_download_ppt_big);
		mapping_task.put(".xls", R.drawable.ic_download_excel_big);
		mapping_task.put(".xlsl", R.drawable.ic_download_excel_big);
		mapping_task.put(".jpg", R.drawable.ic_download_jpg_big);
		mapping_task.put(".zip", R.drawable.ic_download_zip_big);
		mapping_task.put("*", R.drawable.ic_download_unknown_big);
	}
}
