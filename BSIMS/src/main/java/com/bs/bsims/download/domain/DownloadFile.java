
package com.bs.bsims.download.domain;


import java.io.Serializable;

/**
 * @author peck
 * @Description: 2. 文档列表页面接口 增加参数
 * @date 2015-7-4 下午4:50:06
 * @email 971371860@qq.com
 * @version V1.0
 */
public class DownloadFile implements Serializable {

    private static final long serialVersionUID = -4759567097936827875L;

    /** 文件id */
    private String sharedid = "";

    /** 文件名称 */
    private String title = "";

    /** 文件路径 */
    private String filepath = "";

    /** 文件大小 */
    private long filesize;

    /** 文件类型 */
    private String filetype = "";

    /** 发布人id */
    private String userid = "";

    /** 下载记录人ID */
    private String downloadrecord = "";

    // /** 发布时间 */
    // private long time;
    /** 发布时间 */
    private String time;

    /** 发布人名字 */
    private String fullname = "";

    /** 发布人部门 */
    private String dname = "";

    /** 当前用户是否下载, 1已下载 */
    private int isdown;

    /** 抄送的人员json */
    private String recipient = "暂无";

    /** 文件是否已经被完全下载完毕 **/
    private String download;

    /** 文件收藏上拉加载的id **/
    private String slid;

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    //
    // {
    // "sharedid": "11",
    // "title": "secondarytile",
    // "filepath":
    // "http://cp.beisheng.wang//Uploads/bs0001/Sharedfiles/1/20150629/5590ed49b54c2.png",
    // "size": "637",
    // "extension": "1",
    // "did": "1",
    // "userid": "26",
    // "downloadrecord": "15,31,26",
    // "open": "1",
    // "likes": "0",
    // "time": "1435561291",
    // "fullname": "刘小龙",
    // "dname": "管理部",
    // "pname": "副总"
    // }
    /**  */
    private String pname;

    private String isDownLoading = "0";

    public String getIsDownLoading() {
        return isDownLoading;
    }

    public void setIsDownLoading(String isDownLoading) {
        this.isDownLoading = isDownLoading;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    /**  */
    private String likes;
    /**  */
    private String open;
    /**  */
    private String did;
    /**  */
    private String extension;
    /**  */
    private String size;

    private int progress = 0;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSharedid() {
        return sharedid;
    }

    public void setSharedid(String sharedid) {
        this.sharedid = sharedid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDownloadrecord() {
        return downloadrecord;
    }

    public void setDownloadrecord(String downloadrecord) {
        this.downloadrecord = downloadrecord;
    }

    // public long getTime() {
    // return time;
    // }
    //
    // public void setTime(long time) {
    // this.time = time;
    // }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public int getIsdown() {
        return isdown;
    }

    public void setIsdown(int isdown) {
        this.isdown = isdown;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFileNull(DownloadFile d) {
        d = null;
    }
}
