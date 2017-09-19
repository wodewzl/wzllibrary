
package com.beisheng.synews.mode;

public class UpdateVersionVO {
    private String retcode;
    private UpdateVersionVO version;
    private UpdateVersionVO android;
    private String build;
    private String buildName;
    private String title;
    private String message;
    private String url;
    private String startImg;
    private String noticeTitle;
    private String noticeUrl;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public UpdateVersionVO getVersion() {
        return version;
    }

    public void setVersion(UpdateVersionVO version) {
        this.version = version;
    }

    public UpdateVersionVO getAndroid() {
        return android;
    }

    public void setAndroid(UpdateVersionVO android) {
        this.android = android;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartImg() {
        return startImg;
    }

    public void setStartImg(String startImg) {
        this.startImg = startImg;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

}
