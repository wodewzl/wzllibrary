
package com.bs.bsims.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class CommonImageUtils {

    public static int UNCONSTRAINED = -1;

    /**
     * 转换图片成圆形
     * 
     * @param bitmap 传入Bitmap对象
     * @return
     */

    // private static Bitmap bitmap;

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * Drawable 转化为 Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = Bitmap.Config.ARGB_8888; // RGB565绘制有黑色边框
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转换到Byte[]
     * 
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bas);
        return bas.toByteArray();
    }

    /**
     * bitmap转Drawable
     * 
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * 把字符串转化成bitmap
     * 
     * @param picStr
     */
    public static Bitmap String2Bitmap(String picStr) {
        byte[] bs = Base64.decode(picStr, Base64.DEFAULT);

        if (bs.length != 0) {
            Bitmap map = BitmapFactory.decodeByteArray(bs, 0, bs.length);
            return map;
        }

        return null;
    }

    /**
     * bitmap转成字符串
     * 
     * @param bitmap
     */
    public static String bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bs = baos.toByteArray();
        String imgStr = Base64.encodeToString(bs, Base64.DEFAULT);
        return imgStr;
    }

    /**
     * 圆图片
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 从SD卡加载图片
     * 
     * @param imagePath 文件全路径
     * @return
     */
    public static Bitmap getImageFromLocal(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, opts);

            // 裁剪一下图片的大小
            opts.inSampleSize = computeSampleSize(opts, -1, 384 * 384);
            opts.inJustDecodeBounds = false;

            file.setLastModified(System.currentTimeMillis());

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
                return bitmap;
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    /**
     * 根据需要进行缩放的比例，即options.inSampleSize
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math.min(Math.floor(w / minSideLength),

                Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED) && (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }

    }

    // ************** 头像裁剪有关 *****************************

    /**
     * 开启选择图片的跳转,在onActivityResult方法中返回，requestCode为返回码, 在返回的方法中，通过intent.getData()，得到图片的uri
     * 
     * @param activity
     * @param requestCode
     */
    public static void selectPhoto(Activity activity, int requestCode) {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intentFromGallery, requestCode);
    }

    /**
     * 开启相机的跳转,在onActivityResult方法中返回，requestCode为返回码
     * 
     * @param activity
     * @param requestCode
     * @param imageFileName 图片保存的位置，需要后缀
     */
    public static void openCapture(Activity activity, int requestCode, String imageFileName) {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 图片保存的路径，还需要设计一下具体的路径
        File file = new File(Environment.getExternalStorageDirectory(), imageFileName);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intentFromCapture, requestCode);
    }

    /**
     * 裁剪图片方法实现
     * 
     * @param activity 当前的activity
     * @param uri 图片的uri
     * @param code startActivityForResult时，返回的code
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int requestCode) {
        // 跳转到系统自带的一个图片剪辑器中去
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    // ************** 头像裁剪有关 end *****************************

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     * 
     * @param bitmap 原图
     * @param edgeLength 希望得到的正方形部分的边长,单位px
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 保存图片到本地
     * 
     * @param bitmap
     * @param savePath
     * @param fileName 带后缀的文件名称，统一.jpg
     * @return
     */
    public static boolean saveBitmap2local(Bitmap bitmap, String savePath, String fileName) {

        // 判断文件夹是否存在
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(savePath + File.separator + fileName);

        OutputStream outStream = null;

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
            return false;
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    outStream = null;
                }
                outStream = null;
            }
        }
    }
    
 
    
    

    /******************* 以下：照相后获取小图片 *************************************/

    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    /**
     * 旋转图片
     * 
     * @param bitmap
     * @param angle 角度
     * @return Bitmap
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        if (bitmap == null)
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /******************* 以上：照相后获取小图片 *************************************/

    // 根据手机屏幕压缩图片
    public static Bitmap loadImageBitmap(String url, int screenWidth, int screenHeigh) {
        if (screenWidth > 1500) {
            screenWidth = screenWidth * 7 / 10;
            screenHeigh = screenHeigh * 7 / 10;
        } else if (screenWidth > 1000) {
            screenWidth = screenWidth * 6 / 10;
            screenHeigh = screenHeigh * 6 / 10;
        } else if (screenWidth > 700) {
            screenWidth = screenWidth * 4 / 10;
            screenHeigh = screenHeigh * 4 / 10;
        } else if (screenWidth > 400) {
            screenWidth = screenWidth * 3 / 10;
            screenHeigh = screenHeigh * 3 / 10;
        } else {
            screenWidth = screenWidth * 2 / 10;
            screenHeigh = screenHeigh * 2 / 10;
        }
       Bitmap bitmap = BitmapFactory.decodeFile(url);
        if (null != bitmap) {
            if (bitmap.getWidth() > bitmap.getHeight()) {
                int height = (screenWidth * bitmap.getHeight()) / bitmap.getWidth();
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, screenWidth, height);
            } else {
                int width = (screenHeigh * bitmap.getWidth()) / bitmap.getHeight();
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, screenHeigh);
            }
            return bitmap;
        }
        return bitmap;
    }

}
