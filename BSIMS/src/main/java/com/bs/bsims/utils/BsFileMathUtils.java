package com.bs.bsims.utils;

import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.widget.ImageView;

import com.bs.bsims.R;

public class BsFileMathUtils {

	// 计算文件大小转换
	public static String FromartDoubleOfFileSize(String filesize) {

		// Double filesizeK = 0.0;
		// if (filesize.equals("0") && filesize.matches("[0-9]+"))
		// return 0 + "";
		// filesizeK = Double.parseDouble(filesize) / 1024;
		// if (filesizeK < 1000) {
		// return df.format(filesizeK) + "K";
		// }
		// else if (filesizeK < 10000 && filesizeK > 1000) {
		// filesizeK = filesizeK / 1024;
		// return df.format(filesizeK) + "M";
		// }
		// else if (filesizeK < 100000 && filesizeK > 10000) {
		// filesizeK = filesizeK / 1024;
		// return df.format(filesizeK) + "G"; //10444276
		// }
		// return "";
		if (filesize.equals("0") && filesize.matches("[0-9]+"))
			return 0 + "";
		double size = Double.parseDouble(filesize);
		DecimalFormat df = new DecimalFormat("0.0");
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return df.format(size / gb) + "gb";
			// return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return df.format(f) + "MB";
			// return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return df.format(f) + "KB";
			// return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return df.format(size) + "B";
	}

	// 判断传过来的标示来计算使用的图片和后缀名
	public static String SetShowFileNames(String extension, String showname, String filename) {
		int key = Integer.parseInt(extension);
		String Filename = "";
		switch (key) {
		case 1:
			Filename = showname + ".jpg";
			break;
		case 2:
			Filename = showname + ".doc";
			break;
		case 3:
			Filename = showname + ".xls";
			break;
		case 4:
			Filename = showname + ".ppt";
			break;
		case 5:
			Filename = showname + ".zip";
			break;
		default:
			Filename = showname + "." + filename.substring(filename.lastIndexOf(".") + 1);
			break;

		}
		return Filename;

	}

	// 计算SDCard的当前容量
	public static long GetSDCardSize() {
		// sdcard可以使用
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(path.getPath());
			// 获取单个数据块的大小(Byte)
			long blockSize = sf.getBlockSize();
			// 空闲的数据块的数量
			long freeBlocks = sf.getAvailableBlocks();
			// 返回SD卡空闲大小
			// return freeBlocks * blockSize; //单位Byte
			// return (freeBlocks * blockSize)/1024; //单位KB
			return (freeBlocks * blockSize)/1024/1024; // 单位MB
		} else {
			return 0;
		}

	}

	// 判断传过来的标示来计算使用的图片
	public static void SetShowFileName(String extension, ImageView showjpg, Context context) {
		int key = Integer.parseInt(extension);
		switch (key) {
		case 1:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_jpg));
			break;
		case 2:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_doc));
			break;
		case 3:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_xls));
			break;
		case 4:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_ppt));
			break;
		case 5:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_zip));
			break;
		default:
			showjpg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.filedonw_nothing));
			break;

		}

	}

	/* 计算图片旋转的角度 */

	/**
	 * *@param imgSource 源图像 *@param cx 旋转点相对于源图像坐上角横坐标 *@param cy
	 * 旋转点相对于源图像坐上角纵坐标 *@param theta 图像逆时针旋转的角度 *@return 旋转后的图像
	 * */
	/**
	 * 02. * 旋转图片，使图片保持正确的方向。 03. * @param bitmap 原始图片 04. * @param degrees
	 * 原始图片的角度 05. * @return Bitmap 旋转后的图片 06.
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}

}
