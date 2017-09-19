
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/*
 * 拜访记录
 */
public class CrmVisitorVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private CrmBussinesListindexVo business;
    private List<CrmVisitorVo> array;
    private String userid;
	private String vid;
    private String info;
    private String time;
    private String objective;
    private String mode;
    private String lon;
    private String lat;
    private String address;
    private String bid;
    private String hid;
    private String cname;
    private String cid;
    private String fullName;
    private String headpic;
    private String positionsName;
    private String departmentName;
    private String objectiveName;
    private String modeName;
    private String bname;
    private String crmEdit;// 编辑权限 0没有 1有权限
    private String comment;// 回复数量
    private String praise;// 点赞
    private String decline;// 点踩
    private String count;
    private String code;
    private String retinfo;
    private String isdeclined;
    private String ispraise;
    private String ispraised;
    private String hname;
    private String totalPage;
    private String csid;// 签到列表id
    private String falgecontant;
    private String isread;
    private String pic;
    private String pname;
    private String name;
    private String sexy;
    
    public String getIspraised() {
        return ispraised;
    }

    public void setIspraised(String ispraised) {
        this.ispraised = ispraised;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexy() {
        return sexy;
    }

    public void setSexy(String sexy) {
        this.sexy = sexy;
    }

    public String getUserid() {
    	return userid;
    }
    
    public void setUserid(String userid) {
    	this.userid = userid;
    }
    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFalgecontant() {
        return falgecontant;
    }

    public void setFalgecontant(String falgecontant) {
        this.falgecontant = falgecontant;
    }

    public String getCsid() {
        return csid;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    private String addtime;// 签到时间

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    //
    private String date;// 日期
    private List<CrmVisitorVo> list;

    public List<CrmVisitorVo> getList() {
        return list;
    }

    public void setList(List<CrmVisitorVo> list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    /**
     * 存放图片
     */
    private List<String> imgs;

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    private boolean agree;
    private boolean oppose;

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public boolean isOppose() {
        return oppose;
    }

    public void setOppose(boolean oppose) {
        this.oppose = oppose;
    }

    public String getIsdeclined() {
        return isdeclined;
    }

    public void setIsdeclined(String isdeclined) {
        this.isdeclined = isdeclined;
    }

 
    public String getIspraise() {
        return ispraise;
    }

    public void setIspraise(String ispraise) {
        this.ispraise = ispraise;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getDecline() {
        return decline;
    }

    public void setDecline(String decline) {
        this.decline = decline;
    }

    public String getCrmEdit() {
        return crmEdit;
    }

    public void setCrmEdit(String crmEdit) {
        this.crmEdit = crmEdit;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public CrmBussinesListindexVo getBusiness() {
        return business;
    }

    public void setBusiness(CrmBussinesListindexVo business) {
        this.business = business;
    }

    public List<CrmVisitorVo> getArray() {
        return array;
    }

    public void setArray(List<CrmVisitorVo> array) {
        this.array = array;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getPositionsName() {
        return positionsName;
    }

    public void setPositionsName(String positionsName) {
        this.positionsName = positionsName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

}
