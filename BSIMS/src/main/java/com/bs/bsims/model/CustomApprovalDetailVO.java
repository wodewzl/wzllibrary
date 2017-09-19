
package com.bs.bsims.model;

import java.util.ArrayList;
import java.util.List;

public class CustomApprovalDetailVO {
    private CustomApprovalDetailVO array;
    private String code;
    private String retinfo;
    private String system_time;

    private String otype;
    private String oname;
    private String ovalue;

    private String approvalid;
    private String typeid;
    private String status;
    private String userid;
    private String time;
    private String headpic;
    private String fullname;
    private String dname;
    private String pname;
    private String approval;
    private String typename;
    private ArrayList<String> imgs;

    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;
    private List<EmployeeVO> opinion;
    private List<CustomApprovalDetailVO> options;
    private List<CustomApprovalDetailVO> annexs;

    private String url;
    private String name;
    private String getGenre;

    public String getGetGenre() {
        return getGenre;
    }

    public void setGetGenre(String getGenre) {
        this.getGenre = getGenre;
    }

    public CustomApprovalDetailVO getArray() {
        return array;
    }

    public void setArray(CustomApprovalDetailVO array) {
        this.array = array;
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

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getOvalue() {
        return ovalue;
    }

    public void setOvalue(String ovalue) {
        this.ovalue = ovalue;
    }

    public String getApprovalid() {
        return approvalid;
    }

    public void setApprovalid(String approvalid) {
        this.approvalid = approvalid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public List<EmployeeVO> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<EmployeeVO> appUser) {
        this.appUser = appUser;
    }

    public List<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(List<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    public List<CustomApprovalDetailVO> getOptions() {
        return options;
    }

    public void setOptions(List<CustomApprovalDetailVO> options) {
        this.options = options;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public List<EmployeeVO> getOpinion() {
        return opinion;
    }

    public void setOpinion(List<EmployeeVO> opinion) {
        this.opinion = opinion;
    }

    public List<CustomApprovalDetailVO> getAnnexs() {
        return annexs;
    }

    public void setAnnexs(List<CustomApprovalDetailVO> annexs) {
        this.annexs = annexs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
