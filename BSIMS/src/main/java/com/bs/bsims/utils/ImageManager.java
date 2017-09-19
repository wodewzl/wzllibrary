/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bs.bsims.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bs.bsims.xutils.impl.PhoneInfoUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Manages retrieval and storage of icon images. Use the put method to download and store images.
 * Use the get method to retrieve images from the manager.
 */
public class ImageManager {
    private static final String TAG = "ImageManager";

    // ����Ŀǰ�����֧��596px, ������ͬ����С
    // ���߶�Ϊ1192px, ������н�ȡ
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    // public static final int IMAGE_MAX_WIDTH = 800;
    // public static final int IMAGE_MAX_HEIGHT = 1192;
    public static int IMAGE_MAX_WIDTH = 240;
    public static int IMAGE_MAX_HEIGHT = 320;
    // public static final int IMAGE_MAX_WIDTH = 800;
    // public static final int IMAGE_MAX_HEIGHT = 1080;

    private Context mContext;
    // In memory cache.
    private final Map<String, SoftReference<Bitmap>> mCache;
    // MD5 hasher.
    private MessageDigest mDigest;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public ImageManager(Context context) {
        this.mContext = context;
        this.mCache = new HashMap<String, SoftReference<Bitmap>>();

        /**
         * 当用户手机处于3G以上或者WIFI 状态下
         */
        if (PhoneInfoUtil.NETWORKTYPE_4IMG) {
            IMAGE_MAX_WIDTH = 800;
            IMAGE_MAX_HEIGHT = 1192;
        }

        try {
            this.mDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // This shouldn't happen.
            throw new RuntimeException("No MD5 algorithm.");
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();

        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }

        return builder.toString();
    }

    // MD5 hases are used to generate filenames based off a URL.
    private String getMd5(String url) {
        this.mDigest.update(url.getBytes());

        return this.getHashString(this.mDigest);
    }

    // Looks to see if an image is in the file system.
    private Bitmap lookupFile(String url) {
        String hashedUrl = this.getMd5(url);
        FileInputStream fis = null;

        try {
            fis = this.mContext.openFileInput(hashedUrl);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            // Not there.
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }

    /**
     * ��Bitmapд�뻺����.
     * 
     * @param filePath file path
     * @param bitmap
     * @param quality 1~100
     */
    public void put(String file, Bitmap bitmap, int quality) {
        synchronized (this) {
            this.mCache.put(file, new SoftReference<Bitmap>(bitmap));
        }

        this.writeFile(file, bitmap, quality);
    }

    /**
     * ��Bitmapд�뱾�ػ����ļ�.
     * 
     * @param file URL/PATH
     * @param bitmap
     * @param quality
     */
    private void writeFile(String file, Bitmap bitmap, int quality) {
        if (bitmap == null) {
            Log.w(ImageManager.TAG, "Can't write file. Bitmap is null.");
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos); // PNG
            Log.d(ImageManager.TAG, "Writing file: " + file);
        } catch (IOException ioe) {
            Log.e(ImageManager.TAG, ioe.getMessage());
        } finally {
            try {
                if (null != fos) {
                    bitmap.recycle();
                    fos.flush();
                    fos.close();
                }
                // bitmap.recycle();
            } catch (IOException e) {
                Log.e(ImageManager.TAG, "Could not close file.");
            }
        }
    }

    private String writeToFile(InputStream is, String filename) {
        Log.d("LDS", "new write to file");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(this.mContext.openFileOutput(
                    filename, Context.MODE_PRIVATE));
            byte[] buffer = new byte[1024];
            int l;
            while ((l = in.read(buffer)) != -1) {
                out.write(buffer, 0, l);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    Log.d("LDS", "new write to file to -> " + filename);
                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return this.mContext.getFilesDir() + "/" + filename;
    }

    /**
     * �жϻ��������Ƿ���ڸ��ļ���Ӧ��bitmap
     */
    public boolean isContains(String file) {
        return this.mCache.containsKey(file);
    }

    public void clear() {
        String[] files = this.mContext.fileList();

        for (String file : files) {
            this.mContext.deleteFile(file);
        }

        synchronized (this) {
            this.mCache.clear();
        }
    }

    public void cleanup(HashSet<String> keepers) {
        String[] files = this.mContext.fileList();
        HashSet<String> hashedUrls = new HashSet<String>();

        for (String imageUrl : keepers) {
            hashedUrls.add(this.getMd5(imageUrl));
        }

        for (String file : files) {
            if (!hashedUrls.contains(file)) {
                Log.d(ImageManager.TAG, "Deleting unused file: " + file);
                this.mContext.deleteFile(file);
            }
        }
    }

    /**
     * Compress and resize the Image <br />
     * ��Ϊ����ͼƬ��С�ͳߴ����, ���񶼻��ͼƬ����һ������ѹ��, ���Ա���ѹ��Ӧ�� ����ͼƬ���ᱻ����ѹ������ɵ�ͼƬ�������
     * 
     * @param targetFile
     * @param quality , 0~100, recommend 100
     * @return
     * @throws IOException
     */
    public File compressImage(File targetFile, int quality) throws IOException {
        String filepath = targetFile.getAbsolutePath();

        // 1. Calculate scale
        int scale = 1;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, o);
        if ((o.outWidth > ImageManager.IMAGE_MAX_WIDTH)
                || (o.outHeight > ImageManager.IMAGE_MAX_HEIGHT)) {
            scale = (int) Math.pow(
                    2.0,
                    (int) Math.round(Math.log(ImageManager.IMAGE_MAX_WIDTH
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
            // scale = 2;
        }
        Log.d(ImageManager.TAG, scale + " scale");

        // 2. File -> Bitmap (Returning a smaller image)
        o.inJustDecodeBounds = false;
        o.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, o);

        if (bitmap == null) {
            return null;
        }

        // 2.1. Resize Bitmap
        if (bitmap.getWidth() > ImageManager.IMAGE_MAX_WIDTH) {
            bitmap = ImageManager
                    .resizeBitmap(bitmap, ImageManager.IMAGE_MAX_WIDTH,
                            ImageManager.IMAGE_MAX_HEIGHT);
        }
        bitmap = createBitmap4Watermark(bitmap);
        // 3. Bitmap -> File
        this.writeFile(filepath, bitmap, quality);

        // 4. Get resized Image File
        /*
         * String filePath = this.getMd5(targetFile.getPath()); File compressedImage =
         * this.mContext.getFileStreamPath(filePath);
         */
        return targetFile;
    }

    /**
     * ���ֳ������СBitmap
     * 
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @param quality 1~100
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if ((originWidth < maxWidth) && (originHeight < maxHeight)) {
            return bitmap;
        }

        int newWidth = originWidth;
        int newHeight = originHeight;

        // ��ͼƬ���, �򱣳ֳ��������ͼƬ
        if (originWidth > maxWidth) {
            newWidth = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            newHeight = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
                    true);
        }

        originWidth = bitmap.getWidth();
        originHeight = bitmap.getHeight();

        // ��ͼƬ��, ����в���ȡ
        if (newHeight > maxHeight) {
            newHeight = maxHeight;

            int half_diff = (int) ((originHeight - maxHeight) / 2.0);
            bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, newWidth,
                    newHeight);
        }

        Log.d(ImageManager.TAG, newWidth + " width");
        Log.d(ImageManager.TAG, newHeight + " height");

        return bitmap;
    }

    private Bitmap createBitmap4Watermark(Bitmap photo) {
        if (photo == null) {
            return null;
        }
        // 设置画笔
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        // 字体大小
        textPaint.setTextSize(20.0f);
        textPaint.setTextSize(12.0f);
        textPaint.setTextSize(20.0f);
        // textPaint.setTextSize(22.0f);
        // textPaint.setTextSize(48.0f);
        // 采用默认的宽度
        textPaint.setTypeface(Typeface.DEFAULT);
        // 采用的颜色
        textPaint.setColor(Color.GREEN);
        textPaint.setColor(Color.BLUE);

        // 时间水印
        String mark = "2015/7/11 星期六";
        mark = DateUtils.getCurrentTimeMM();
        mark = DateUtils.getCurrentTimeYYMMddhhmm();
        mark = DateUtils.getCurrentTimeMMddhhmm();
        // mark = DateUtils.parseMDHM(mark);
        float textWidth = textPaint.measureText(mark);

        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, Config.ARGB_8888);
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        // draw src into
        cv.drawBitmap(photo, 10, 10, null);// 在 0，0坐标开始画入src
        // draw watermark into
        // cv.drawBitmap(watermark, mark_x, mark_y, null);// 在src的右下角画入水印
        cv.drawText(mark, photoWidth - textWidth, photoHeight - 26, textPaint);
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;

    }

}
