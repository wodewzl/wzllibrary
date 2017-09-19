
package com.bs.bsims.model;

import java.util.List;

public class ContactDepTabVO {
    private List<ContactDepTabVO> array;
    private String lid;
    private String lname;
    private String addtime;
    private String cname;
    private String code;
    private String retinfo;
    private String system_time;
    private String sortLetters;
    private String crmAuth;
    private String nickname;
    private List<ContactDepTabVO> list;
    private String sex;
    private String post;
    private String company;
    private String lheadpic;
    private List<String> phone;
    private List<String> tel;

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getTel() {
        return tel;
    }

    public void setTel(List<String> tel) {
        this.tel = tel;
    }

    public String getLheadpic() {
        return lheadpic;
    }

    public void setLheadpic(String lheadpic) {
        this.lheadpic = lheadpic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCrmAuth() {
        return crmAuth;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<ContactDepTabVO> getList() {
        return list;
    }

    public void setList(List<ContactDepTabVO> list) {
        this.list = list;
    }

    public void setCrmAuth(String crmAuth) {
        this.crmAuth = crmAuth;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public ContactDepTabVO(List<ContactDepTabVO> array, String lid, String lname, String addtime, String cname, String code, String retinfo, String system_time, String sortLetters, String crmAuth) {
        super();
        this.array = array;
        this.lid = lid;
        this.lname = lname;
        this.addtime = addtime;
        this.cname = cname;
        this.code = code;
        this.retinfo = retinfo;
        this.system_time = system_time;
        this.sortLetters = sortLetters;
        this.crmAuth = crmAuth;
    }

    public ContactDepTabVO() {
        super();
    }

    public List<ContactDepTabVO> getArray() {
        return array;
    }

    public void setArray(List<ContactDepTabVO> array) {
        this.array = array;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

}
