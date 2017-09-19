
package com.bs.bsims.model;

public class UserInfoVO {
    private String userid;
    private String headpic;
    private String fullname;
    private String sex;
    private String tel;
    private String did;
    private String dname;
    private String pname;
    private String belong;
    private String sortLetters;
    private String awork;
    private String cornet;
    private String name;
    private String phone;
    private String ismobile;
    private String code;
    private String uid;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

  

    private UserInfoVO info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfoVO getInfo() {
        return info;
    }

    public void setInfo(UserInfoVO info) {
        this.info = info;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
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

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Override
    public String toString() {
        return getFullname();
    }

    public String getAwork() {
        return awork;
    }

    public void setAwork(String awork) {
        this.awork = awork;
    }

    public String getIsmobile() {
        return ismobile;
    }

    public void setIsmobile(String ismobile) {
        this.ismobile = ismobile;
    }

}
