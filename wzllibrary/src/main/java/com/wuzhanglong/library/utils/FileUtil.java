
package com.wuzhanglong.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import cz.msebera.android.httpclient.util.EncodingUtils;

public class FileUtil {
    private static final int FILE_BUFFER_SIZE = 51200;

    public static boolean fileIsExist(String filePath) {
        if (filePath == null || filePath.length() < 1) {
            return false;
        }

        File f = new File(filePath);
        if (!f.exists()) {
            return false;
        }
        return true;
    }

    public static InputStream readFile(String filePath) {
        if (null == filePath) {
            return null;
        }

        InputStream is = null;

        try {
            if (fileIsExist(filePath)) {
                File f = new File(filePath);
                is = new FileInputStream(f);
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        return is;
    }

    public static boolean createDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();

    }

    public static boolean deleteDirectory(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);

        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();

            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    deleteDirectory(list[i].getAbsolutePath());
                } else {
                    list[i].delete();
                }
            }
        }

        file.delete();
        return true;
    }

    public static boolean writeFile(String filePath, InputStream inputStream) {

        if (null == filePath || filePath.length() < 1) {
            return false;
        }

        try {
            File file = new File(filePath);
            if (file.exists()) {
                deleteDirectory(filePath);
            }

            String pth = filePath.substring(0, filePath.lastIndexOf("/"));
            boolean ret = createDirectory(pth);
            if (!ret) {
                return false;
            }

            boolean ret1 = file.createNewFile();
            if (!ret) {
                return false;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int c = inputStream.read(buf);
            while (-1 != c) {
                fileOutputStream.write(buf, 0, c);
                c = inputStream.read(buf);
            }

            fileOutputStream.flush();
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean writeFile(String filePath, String fileContent) {
        return writeFile(filePath, fileContent, false);
    }

    public static boolean writeCacheFile(String filePath, String fileContent) {
        return writeCacheFile(filePath, fileContent, false);
    }

    public static boolean writeFile(String filePath, String fileContent, boolean append) {
        if (null == filePath || fileContent == null || filePath.length() < 1 || fileContent.length() < 1) {
            return false;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return false;
                }
            }

            BufferedWriter output = new BufferedWriter(new FileWriter(file, append));
            output.write(fileContent);
            output.flush();
            output.close();
        } catch (IOException ioe) {
            return false;
        }

        return true;
    }

    public static boolean writeCacheFile(String filePath, String fileContent, boolean append) {
        if (null == filePath || fileContent == null || filePath.length() < 1 || fileContent.length() < 1) {
            return false;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    return false;
                }
            }

            BufferedWriter output = new BufferedWriter(new FileWriter(file, append));
            output.write(fileContent);
            output.flush();
            output.close();
        } catch (IOException ioe) {
            return false;
        }

        return true;
    }

    public static long getFileSize(String filePath) {
        if (null == filePath) {
            return 0;
        }

        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return 0;
        }

        return file.length();
    }

    public static long getFileModifyTime(String filePath) {
        if (null == filePath) {
            return 0;
        }

        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return 0;
        }

        return file.lastModified();
    }

    public static boolean setFileModifyTime(String filePath, long modifyTime) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return false;
        }

        return file.setLastModified(modifyTime);
    }

    public static boolean copyFile(ContentResolver cr, String fromPath, String destUri) {
        if (null == cr || null == fromPath || fromPath.length() < 1 || null == destUri || destUri.length() < 1) {
            return false;
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(fromPath);
            if (null == is) {
                return false;
            }

            // check output uri
            String path = null;
            Uri uri = null;

            String lwUri = destUri.toLowerCase();
            if (lwUri.startsWith("content://")) {
                uri = Uri.parse(destUri);
            } else if (lwUri.startsWith("file://")) {
                uri = Uri.parse(destUri);
                path = uri.getPath();
            } else {
                path = destUri;
            }

            // open output
            if (null != path) {
                File fl = new File(path);
                String pth = path.substring(0, path.lastIndexOf("/"));
                File pf = new File(pth);

                if (pf.exists() && !pf.isDirectory()) {
                    pf.delete();
                }

                pf = new File(pth + File.separator);

                if (!pf.exists()) {
                    if (!pf.mkdirs()) {
                    }
                }

                pf = new File(path);
                if (pf.exists()) {
                    if (pf.isDirectory())
                        deleteDirectory(path);
                    else
                        pf.delete();
                }

                os = new FileOutputStream(path);
                fl.setLastModified(System.currentTimeMillis());
            } else {
                os = new ParcelFileDescriptor.AutoCloseOutputStream(cr.openFileDescriptor(uri, "w"));
            }

            // copy file
            byte[] dat = new byte[1024];
            int i = is.read(dat);
            while (-1 != i) {
                os.write(dat, 0, i);
                i = is.read(dat);
            }

            is.close();
            is = null;

            os.flush();
            os.close();
            os = null;

            return true;

        } catch (Exception ex) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception ex) {
                }
                ;
            }
            if (null != os) {
                try {
                    os.close();
                } catch (Exception ex) {
                }
                ;
            }
        }
        return false;
    }

    public static byte[] readAll(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];
        int c = is.read(buf);
        while (-1 != c) {
            baos.write(buf, 0, c);
            c = is.read(buf);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

    public static byte[] readFile(Context ctx, Uri uri) {
        if (null == ctx || null == uri) {
            return null;
        }

        InputStream is = null;
        String scheme = uri.getScheme().toLowerCase();
        if (scheme.equals("file")) {
            is = readFile(uri.getPath());
        }

        try {
            is = ctx.getContentResolver().openInputStream(uri);
            if (null == is) {
                return null;
            }

            byte[] bret = readAll(is);
            is.close();
            is = null;

            return bret;
        } catch (FileNotFoundException fne) {
        } catch (Exception ex) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception ex) {
                }
                ;
            }
        }
        return null;
    }

    public static boolean writeFile(String filePath, byte[] content) {
        if (null == filePath || null == content) {
            return false;
        }

        FileOutputStream fos = null;
        try {
            String pth = filePath.substring(0, filePath.lastIndexOf("/"));
            File pf = null;
            pf = new File(pth);
            if (pf.exists() && !pf.isDirectory()) {
                pf.delete();
            }
            pf = new File(filePath);
            if (pf.exists()) {
                if (pf.isDirectory())
                    FileUtil.deleteDirectory(filePath);
                else
                    pf.delete();
            }

            pf = new File(pth + File.separator);
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                }
            }

            fos = new FileOutputStream(filePath);
            fos.write(content);
            fos.flush();
            fos.close();
            fos = null;
            pf.setLastModified(System.currentTimeMillis());

            return true;

        } catch (Exception ex) {
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception ex) {
                }
                ;
            }
        }
        return false;
    }

    /************* ZIP file operation ***************/
    public static boolean readZipFile(String zipFileName, StringBuffer crc) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                long size = entry.getSize();
                crc.append(entry.getCrc() + ", size: " + size);
            }
            zis.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static byte[] readGZipFile(String zipFileName) {
        if (fileIsExist(zipFileName)) {
            try {
                FileInputStream fin = new FileInputStream(zipFileName);
                int size;
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((size = fin.read(buffer, 0, buffer.length)) != -1) {
                    baos.write(buffer, 0, size);
                }
                return baos.toByteArray();
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static boolean zipFile(String baseDirName, String fileName, String targerFileName) throws IOException {
        if (baseDirName == null || "".equals(baseDirName)) {
            return false;
        }
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return false;
        }

        String baseDirPath = baseDir.getAbsolutePath();
        File targerFile = new File(targerFileName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targerFile));
        File file = new File(baseDir, fileName);

        boolean zipResult = false;
        if (file.isFile()) {
            zipResult = fileToZip(baseDirPath, file, out);
        } else {
            zipResult = dirToZip(baseDirPath, file, out);
        }
        out.close();
        return zipResult;
    }

    public static boolean unZipFile(String fileName, String unZipDir) throws Exception {
        File f = new File(unZipDir);

        if (!f.exists()) {
            f.mkdirs();
        }

        BufferedInputStream is = null;
        ZipEntry entry;
        ZipFile zipfile = new ZipFile(fileName);
        Enumeration<?> enumeration = zipfile.entries();
        byte data[] = new byte[FILE_BUFFER_SIZE];

        while (enumeration.hasMoreElements()) {
            entry = (ZipEntry) enumeration.nextElement();

            if (entry.isDirectory()) {
                File f1 = new File(unZipDir + "/" + entry.getName());
                if (!f1.exists()) {
                    f1.mkdirs();
                }
            } else {
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                String name = unZipDir + "/" + entry.getName();
                RandomAccessFile m_randFile = null;
                File file = new File(name);
                if (file.exists()) {
                    file.delete();
                }

                file.createNewFile();
                m_randFile = new RandomAccessFile(file, "rw");
                int begin = 0;

                while ((count = is.read(data, 0, FILE_BUFFER_SIZE)) != -1) {
                    try {
                        m_randFile.seek(begin);
                    } catch (Exception ex) {
                    }

                    m_randFile.write(data, 0, count);
                    begin = begin + count;
                }

                file.delete();
                m_randFile.close();
                is.close();
            }
        }

        return true;
    }

    private static boolean fileToZip(String baseDirPath, File file, ZipOutputStream out) throws IOException {
        FileInputStream in = null;
        ZipEntry entry = null;

        byte[] buffer = new byte[FILE_BUFFER_SIZE];
        int bytes_read;
        try {
            in = new FileInputStream(file);
            entry = new ZipEntry(getEntryName(baseDirPath, file));
            out.putNextEntry(entry);

            while ((bytes_read = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
            out.closeEntry();
            in.close();
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) {
                out.closeEntry();
            }

            if (in != null) {
                in.close();
            }
        }
        return true;
    }

    private static boolean dirToZip(String baseDirPath, File dir, ZipOutputStream out) throws IOException {
        if (!dir.isDirectory()) {
            return false;
        }

        File[] files = dir.listFiles();
        if (files.length == 0) {
            ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));

            try {
                out.putNextEntry(entry);
                out.closeEntry();
            } catch (IOException e) {
            }
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                fileToZip(baseDirPath, files[i], out);
            } else {
                dirToZip(baseDirPath, files[i], out);
            }
        }
        return true;
    }

    private static String getEntryName(String baseDirPath, File file) {
        if (!baseDirPath.endsWith(File.separator)) {
            baseDirPath = baseDirPath + File.separator;
        }

        String filePath = file.getAbsolutePath();
        if (file.isDirectory()) {
            filePath = filePath + "/";
        }

        int index = filePath.indexOf(baseDirPath);
        return filePath.substring(index + baseDirPath.length());
    }

    public static String getSaveFilePath(Context context,String filePath) {
        File file = new File(getRootFilePath(context) + filePath);
        if (!file.exists()) {
            // file.mkdir();
            file.mkdirs();
        }
        return getRootFilePath(context) + filePath;
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath(Context context) {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return context.getFilesDir().toString() + File.separator;
        }
    }

    /**
     * 读取文件为String
     * 
     * @param context
     * @return
     */
    public static String read(Context context, String fileName) {

        String fileFullName = context.getFilesDir().toString() + File.separator
                + fileName;

        String res = null;
        File file = new File(fileFullName);
        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                int length = in.available();
                if (length != 0) {
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static String readCache(String fileName) {
        String res = null;
        File file = new File(fileName);
        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                int length = in.available();
                if (length != 0) {
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(Integer.valueOf((int) new Float(f2).doubleValue())) + "K";

        } else {
            return df.format(Integer.valueOf((int) new Float(f).doubleValue())) + "M";
        }
    }

    public static String countNumber(String number) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();
        String str = "次下载";
        if (count < 10000) {
            return sb.append(number).append(str).toString();
        } else if (count > 10000 && count < 100000000) {
            if ("0.0".equals(String.valueOf(count % 10000))) {
                return sb.append(Integer.parseInt(number) / 10000 + "万" + str).toString();
            }
            return sb.append(df.format(count / 10000) + "万" + str).toString();
        } else if (count > 100000000) {
            if ("0".equals(String.valueOf(count % 100000000))) {
                return sb.append(Integer.parseInt(number) / 100000000 + "亿" + str).toString();
            }
            return sb.append(df.format(count / 100000000) + "亿" + str).toString();
        }
        return sb.toString();
    }

    public static String countSize(double d) {
        // System.out.println("UnitConversion d:" + d);
        String s = null;
        if (d >= 0 && d < 1024) {
            s = new DecimalFormat("###.##").format(d) + "byte";
        } else {
            if (d >= 1024 && d < (1024 * 1024)) {
                double d2 = d / 1024;
                s = new DecimalFormat("###.##").format(d2) + "KB";
            } else {
                double d2 = d / (1024 * 1024);
                s = new DecimalFormat("###.##").format(d2) + "M";
            }
        }

        return s;
    }

    private static final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {
                    ".3gp", "video/3gpp"
            }, {
                    ".apk", "application/vnd.android.package-archive"
            }, {
                    ".asf", "video/x-ms-asf"
            }, {
                    ".avi", "video/x-msvideo"
            }, {
                    ".bin", "application/octet-stream"
            },
            {
                    ".bmp", "image/bmp"
            }, {
                    ".c", "text/plain"
            }, {
                    ".class", "application/octet-stream"
            }, {
                    ".conf", "text/plain"
            }, {
                    ".cpp", "text/plain"
            }, {
                    ".doc", "application/msword"
            },
            {
                    ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            }, {
                    ".xls", "application/vnd.ms-excel"
            },
            {
                    ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            }, {
                    ".exe", "application/octet-stream"
            }, {
                    ".gif", "image/gif"
            }, {
                    ".gtar", "application/x-gtar"
            },
            {
                    ".gz", "application/x-gzip"
            }, {
                    ".h", "text/plain"
            }, {
                    ".htm", "text/html"
            }, {
                    ".html", "text/html"
            }, {
                    ".jar", "application/java-archive"
            }, {
                    ".java", "text/plain"
            },
            {
                    ".jpeg", "image/jpeg"
            }, {
                    ".jpg", "image/jpeg"
            }, {
                    ".js", "application/x-javascript"
            }, {
                    ".log", "text/plain"
            }, {
                    ".m3u", "audio/x-mpegurl"
            }, {
                    ".m4a", "audio/mp4a-latm"
            },
            {
                    ".m4b", "audio/mp4a-latm"
            }, {
                    ".m4p", "audio/mp4a-latm"
            }, {
                    ".m4u", "video/vnd.mpegurl"
            }, {
                    ".m4v", "video/x-m4v"
            }, {
                    ".mov", "video/quicktime"
            }, {
                    ".mp2", "audio/x-mpeg"
            },
            {
                    ".mp3", "audio/x-mpeg"
            }, {
                    ".mp4", "video/mp4"
            }, {
                    ".mpc", "application/vnd.mpohun.certificate"
            }, {
                    ".mpe", "video/mpeg"
            }, {
                    ".mpeg", "video/mpeg"
            }, {
                    ".mpg", "video/mpeg"
            },
            {
                    ".mpg4", "video/mp4"
            }, {
                    ".mpga", "audio/mpeg"
            }, {
                    ".msg", "application/vnd.ms-outlook"
            }, {
                    ".ogg", "audio/ogg"
            }, {
                    ".pdf", "application/pdf"
            }, {
                    ".png", "image/png"
            },
            {
                    ".pps", "application/vnd.ms-powerpoint"
            }, {
                    ".ppt", "application/vnd.ms-powerpoint"
            }, {
                    ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            },
            {
                    ".prop", "text/plain"
            }, {
                    ".rar", "application/x-rar-compressed"
            }, {
                    ".rc", "text/plain"
            }, {
                    ".rmvb", "audio/x-pn-realaudio"
            }, {
                    ".rtf", "application/rtf"
            },
            {
                    ".sh", "text/plain"
            }, {
                    ".tar", "application/x-tar"
            }, {
                    ".tgz", "application/x-compressed"
            }, {
                    ".txt", "text/plain"
            }, {
                    ".wav", "audio/x-wav"
            }, {
                    ".wma", "audio/x-ms-wma"
            },
            {
                    ".wmv", "audio/x-ms-wmv"
            }, {
                    ".wps", "application/vnd.ms-works"
            }, {
                    ".xml", "text/plain"
            }, {
                    ".z", "application/x-compress"
            }, {
                    ".zip", "application/x-zip-compressed"
            },
            {
                    "", "*/*"
            }
    };

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * 
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(/* uri */Uri.fromFile(file), type);
            // 跳转
            context.startActivity(intent); // 这里最好try一下，有可能会报错。
                                           // //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
        } catch (Exception e) {
        }

    }

}
