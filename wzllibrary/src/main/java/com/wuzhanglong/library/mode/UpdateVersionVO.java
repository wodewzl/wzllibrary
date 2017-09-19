
package com.wuzhanglong.library.mode;

public class UpdateVersionVO extends BaseVO{
    private  UpdateVersionVO datas;
    private String android_version;
    private String android_url;

    public UpdateVersionVO getDatas() {
        return datas;
    }

    public void setDatas(UpdateVersionVO datas) {
        this.datas = datas;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }
}
