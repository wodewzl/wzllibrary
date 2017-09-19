
package com.bs.bsims.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class CommFileUtils {

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

    /**
     * 根据下载路径得到文件后缀（.doc）
     * 
     * @param fileDownloadPath
     * @return
     */
    public static String getSuffix(String fileDownloadPath) {
        int lastIndex = fileDownloadPath.lastIndexOf(".");
        String suffix = "";
        if (lastIndex > 0) {
            suffix = fileDownloadPath.substring(lastIndex,
                    fileDownloadPath.length());
        }
        return suffix;
    }

    /**
     * 根据下载路径得到文件名，不包含后缀
     * 
     * @param fileDownloadPath
     * @return
     */
    public static String getFileNameFromPath(String fileDownloadPath) {
        int lastIndex1 = fileDownloadPath.lastIndexOf("/");
        int lastIndex2 = fileDownloadPath.lastIndexOf(".");
        String fileName = fileDownloadPath.substring(lastIndex1 + 1, lastIndex2);
        return fileName;
    }

    /**
     * 根据文件名称(含后缀)，拿到文件名称
     * 
     * @param name
     * @return
     */
    public static String getFileNameFromName(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        String name = fileName.substring(0, lastIndex);
        return name;
    }

    /**
     * 根据下载路径得到文件名，含后缀
     * 
     * @param fileDownloadPath 下载路径
     * @return
     */
    public static String getFileNameCloudSuffix(String fileDownloadPath) {
        int lastIndex1 = fileDownloadPath.lastIndexOf("/");
        String fileName = fileDownloadPath.substring(lastIndex1 + 1, fileDownloadPath.length());
        return fileName;
    }

    /**
     * @deprecated 暂时没用上这个方法 当文件存在下载目录时，需要在文件后面加数字标示，返回这个数字标示
     * @param file
     * @param fileName 文件名称，不包含后缀
     * @return
     */
    public static int getFileNumber(File file, String fileName) {
        int mark = 0;
        String absolutePath = file.getAbsolutePath();

        // 防止文件名最后一个是数字，多判断一次
        int indexOf1 = absolutePath.indexOf(fileName);
        int indexOf2 = absolutePath.indexOf(".");
        if (indexOf1 == indexOf2)
            return 1;

        String substring = absolutePath.substring(indexOf1 + fileName.length(), indexOf2);
        int parseInt = Integer.parseInt(substring);
        mark = parseInt + 1;
        return mark;
    }

    /**
     * 获取拓展存储Cache的绝对路径，路径最后有系统的分隔符
     */
    public static String getExternalCacheDir(Context context) {
        StringBuilder sb = new StringBuilder();
        File file = context.getExternalCacheDir();
        if (file != null) {
            sb.append(file.getAbsolutePath()).append(File.separator);
        } else {
            sb.append(Environment.getExternalStorageDirectory().getPath())
                    .append("/Android/data/")
                    .append(context.getPackageName())
                    .append("/cache")
                    .append(File.separator).toString();
        }
        return sb.toString();
    }

}
