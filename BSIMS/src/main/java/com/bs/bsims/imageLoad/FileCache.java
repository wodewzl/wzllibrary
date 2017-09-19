
package com.bs.bsims.imageLoad;

import android.content.Context;

import com.bs.bsims.utils.FileUtil;

public class FileCache extends AbstractFileCache {
    private Context mContext;

    public FileCache(Context context) {
        super(context);
        this.mContext = context;

    }

    @Override
    public String getSavePath(String url, Context context) {
        String filename = String.valueOf(url.hashCode());
        return getCacheDir(context) + filename;
    }

    @Override
    public String getCacheDir(Context context) {

        return FileUtil.getSaveFilePath(context);
    }

}
