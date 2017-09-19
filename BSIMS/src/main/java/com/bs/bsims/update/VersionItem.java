
package com.bs.bsims.update;

public class VersionItem {

    /** 版本号 */
    private String number = "";

    /** 版本名称 */
    private String name = "";

    /** 最低版本号 */
    private String minimum = "";

    /** 版本地址 */
    private String address = "";

    /** 更新内容 */
    private String content = "";

    /** APP大小 */
    private String size = "";

    /** 平台 */
    private String platform = "";

    /** 添加时间 */
    private String time = "";

    private String ismust = "0";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsmust() {
        return ismust;
    }

    public void setIsmust(String ismust) {
        this.ismust = ismust;
    }

}
