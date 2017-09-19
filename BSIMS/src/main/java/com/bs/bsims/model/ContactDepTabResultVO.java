
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactDepTabResultVO implements Serializable {
    private List<ContactDepTabResultVO> list;
    private ContactDepTabResultVO array;
    private String lid;
    private String customer;
    private String lname;
    private String initial;
    private String addtime;
    private String cname;
    private String post;
    private String sex;
    private String birthday;
    private String hobby;
    private String[] phone;
    private String[] tel;
    private String[] qq;
    private String[] fax;
    private String[] email;
    private String userid;
    private String ldid;
    private String remark;
    private String cid;
    private String address;
    private String headpic;
    private String fullname;
    private String positions;
    private String dname;

    private String pname;
    private String sortLetters;
    private String code;
    private String retinfo;
    private String system_time;
    private String lheadpic;
    private String[] wechat;

    private String department;
    private String[] weibo;
    private String relationship;
    private String intimacy;
    private String company;
    private String crmEdit;
    private ArrayList<EmployeeVO> relation;
    private String count;
    private String del;
    private String nickname;
    private String typename;
    private int type;
    private int drawableid;
    private String typehint;
    private List<ContactDepTabResultVO> relationships;
    private List<ContactDepTabResultVO> intimacys;
    private List<ContactDepTabResultVO> customers;
    private int itmeid;
    private String id;
    private String name;
    private String relationship_id;
    private String intimacy_id;
    private ArrayList<EmployeeVO> insUser;
    private String website;
    private String isread;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public List<ContactDepTabResultVO> getList() {
        return list;
    }

    public void setList(List<ContactDepTabResultVO> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String[] getPhone() {
        return phone;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String[] getTel() {
        return tel;
    }

    public void setTel(String[] tel) {
        this.tel = tel;
    }

    public String[] getQq() {
        return qq;
    }

    public void setQq(String[] qq) {
        this.qq = qq;
    }

    public String[] getFax() {
        return fax;
    }

    public void setFax(String[] fax) {
        this.fax = fax;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String[] getWechat() {
        return wechat;
    }

    public void setWechat(String[] wechat) {
        this.wechat = wechat;
    }

    public String[] getWeibo() {
        return weibo;
    }

    public void setWeibo(String[] weibo) {
        this.weibo = weibo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(String intimacy) {
        this.intimacy = intimacy;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCrmEdit() {
        return crmEdit;
    }

    public void setCrmEdit(String crmEdit) {
        this.crmEdit = crmEdit;
    }

    public ArrayList<EmployeeVO> getRelation() {
        return relation;
    }

    public void setRelation(ArrayList<EmployeeVO> relation) {
        this.relation = relation;
    }

    public String getLheadpic() {
        return lheadpic;
    }

    public void setLheadpic(String lheadpic) {
        this.lheadpic = lheadpic;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public ContactDepTabResultVO() {
        super();
    }

    public ContactDepTabResultVO getArray() {
        return array;
    }

    public void setArray(ContactDepTabResultVO array) {
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLdid() {
        return ldid;
    }

    public void setLdid(String ldid) {
        this.ldid = ldid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
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

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getDrawableid() {
        return drawableid;
    }

    public void setDrawableid(int drawableid) {
        this.drawableid = drawableid;
    }

    public String getTypehint() {
        return typehint;
    }

    public void setTypehint(String typehint) {
        this.typehint = typehint;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ContactDepTabResultVO> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<ContactDepTabResultVO> relationships) {
        this.relationships = relationships;
    }

    public List<ContactDepTabResultVO> getIntimacys() {
        return intimacys;
    }

    public void setIntimacys(List<ContactDepTabResultVO> intimacys) {
        this.intimacys = intimacys;
    }

    public List<ContactDepTabResultVO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<ContactDepTabResultVO> customers) {
        this.customers = customers;
    }

    public int getItmeid() {
        return itmeid;
    }

    public void setItmeid(int itmeid) {
        this.itmeid = itmeid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getId() + "," + getName();
    }

    public String getRelationship_id() {
        return relationship_id;
    }

    public void setRelationship_id(String relationship_id) {
        this.relationship_id = relationship_id;
    }

    public String getIntimacy_id() {
        return intimacy_id;
    }

    public void setIntimacy_id(String intimacy_id) {
        this.intimacy_id = intimacy_id;
    }

    public ArrayList<EmployeeVO> getInsUser() {
        return insUser;
    }

    public void setInsUser(ArrayList<EmployeeVO> insUser) {
        this.insUser = insUser;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }
}
