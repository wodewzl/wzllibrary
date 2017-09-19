
package com.bs.bsims.imageLoad;

import android.content.Context;
import android.util.Log;

import com.bs.bsims.utils.FileUtil;

import java.io.File;

public abstract class AbstractFileCache {
    private Context mContext;

    private String dirString;

    public AbstractFileCache(Context context) {

        dirString = getCacheDir(context);
        boolean ret = FileUtil.createDirectory(dirString);
        Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
    }

    public File getFile(String url, Context context) {
        File f = new File(getSavePath(url, context));
        return f;
    }

    public abstract String getSavePath(String url, Context context);

    public abstract String getCacheDir(Context context);

    public void clear() {
        FileUtil.deleteDirectory(dirString);
    }

}
