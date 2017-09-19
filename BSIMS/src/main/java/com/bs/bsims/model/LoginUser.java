
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * 登陆后返回的用户信息
 * 
 * @author Administrator
 */
public class LoginUser implements Serializable {
    
    
    /**  整体对象**/
    private LoginUser array;
    public LoginUser getArray() {
        return array;
    }

    public void setArray(LoginUser array) {
        this.array = array;
    }

    /** 用户ID */
    private String userid = "";

    /** 用户名 */
    private String username = "";

    /** 用户头像 */
    private String headpic = "";

    /** 用户全名 */
    private String fullname = "";

    /** 用户全名首字母 */
    private String initial = "";

    /** 用户生日 */
    private String birthday = "";

    /** 电话【多个以逗分割“,”】 */
    private String tel = "";

    /** QQ */
    private String qq = "";

    /** Email */
    private String email = "";

    /** 地址 */
    private String address = "";

    /** 部门ID */
    private String did = "";

    /** 职位ID */
    private String pid = "";

    /** 最后一次登录时间，时间戳 */
    private String lastlogintime = "";

    /** 最后登陆IP */
    private String lastloginip = "";

    /** 现在登陆时间 */
    private String nowlogintime = "";

    /** 现在登陆IP */
    private String nowloginip = "";

    /** 关联环信用户名 */
    private String hxuname = "";

    /** 关联环信用户密码 */
    private String hxupassword = "";

    /** 用户注册时间 */
    private String addtime = "";

    /** 部门名称 */
    private String dname = "";

    /** 职位名称 */
    private String pname = "";

    /** 应用程序标识，类似cookie */
    private String ftoken = "";

    /** jpush的tags */
    private String fjptags = "";

    /** jpush的alias */
    private String jpalias = "";

    /************* 人事统计，新加的字段 ********************************************/
    /** 姓别 【汉字：男、女】 */
    private String sex = "";

    /** 身份证 */
    private String idcard = "";

    /** 学历ID */
    private String education = "";

    /** 婚姻1、已婚2、未婚 */
    private String marriage = "";

    /** 入职时间 */
    private String entrytime = "";

    /** 离职时间 */
    private String quittime = "";

    /** 转正时间 */
    private String positivetime = "";

    /** 登录次数 */
    private String logins;

    /** 管理员标识 */
    private String userflag;

    /** 是否在岗，1=是；0=否 */
    private String isinpost;

    /** 是否允许登录，1=是；0=否 */
    private String islogin;

    /** 特殊权限 */
    private String privilege = "";

    /** 毕业院校 */
    private String school = "";

    /** 毕业时间 */
    private String schooltime = "";

    /** 所学专业 */
    private String specialty = "";

    /** 应急联系人 */
    private String meetname = "";

    /** 应急联系人电话 */
    private String meettel = "";

    /** 招聘渠道 */
    private String channel = "";

    /** 短号 */
    private String cornet = "";

    /** 职务名称 */
    private String postsname = "";

    /** 职务级别 */
    private String plevel = "";

    /** 管辖部门id */
    private String pdepart = "";

    /** 管辖岗位 */
    private String ppositions = "";

    /** 是否管理层 */
    private String management = "";

    /** 保险json */
    private String safe = "";

    /** 合同json */
    private String contract = "";

    /** ================= 首页侧滑权限 =========================== */

    /** 员工档案 1,0 */
    private String archivespurview;

    /** 人事统计 1,0 */
    private String statisticspurview;

    public String getArchivespurview() {
        return archivespurview;
    }

    public void setArchivespurview(String archivespurview) {
        this.archivespurview = archivespurview;
    }

    public String getStatisticspurview() {
        return statisticspurview;
    }

    public void setStatisticspurview(String statisticspurview) {
        this.statisticspurview = statisticspurview;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }

    public String getQuittime() {
        return quittime;
    }

    public void setQuittime(String quittime) {
        this.quittime = quittime;
    }

    public String getPositivetime() {
        return positivetime;
    }

    public void setPositivetime(String positivetime) {
        this.positivetime = positivetime;
    }

    public String getLogins() {
        return logins;
    }

    public void setLogins(String logins) {
        this.logins = logins;
    }

    public String getUserflag() {
        return userflag;
    }

    public void setUserflag(String userflag) {
        this.userflag = userflag;
    }

    public String getIsinpost() {
        return isinpost;
    }

    public void setIsinpost(String isinpost) {
        this.isinpost = isinpost;
    }

    public String getIslogin() {
        return islogin;
    }

    public void setIslogin(String islogin) {
        this.islogin = islogin;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchooltime() {
        return schooltime;
    }

    public void setSchooltime(String schooltime) {
        this.schooltime = schooltime;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getMeetname() {
        return meetname;
    }

    public void setMeetname(String meetname) {
        this.meetname = meetname;
    }

    public String getMeettel() {
        return meettel;
    }

    public void setMeettel(String meettel) {
        this.meettel = meettel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
    }

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public String getPdepart() {
        return pdepart;
    }

    public void setPdepart(String pdepart) {
        this.pdepart = pdepart;
    }

    public String getPpositions() {
        return ppositions;
    }

    public void setPpositions(String ppositions) {
        this.ppositions = ppositions;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getFjptags() {
        return fjptags;
    }

    public void setFjptags(String fjptags) {
        this.fjptags = fjptags;
    }

    public String getJpalias() {
        return jpalias;
    }

    public void setJpalias(String jpalias) {
        this.jpalias = jpalias;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(String lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getLastloginip() {
        return lastloginip;
    }

    public void setLastloginip(String lastloginip) {
        this.lastloginip = lastloginip;
    }

    public String getNowlogintime() {
        return nowlogintime;
    }

    public void setNowlogintime(String nowlogintime) {
        this.nowlogintime = nowlogintime;
    }

    public String getNowloginip() {
        return nowloginip;
    }

    public void setNowloginip(String nowloginip) {
        this.nowloginip = nowloginip;
    }

    public String getHxuname() {
        return hxuname;
    }

    public void setHxuname(String hxuname) {
        this.hxuname = hxuname;
    }

    public String getHxupassword() {
        return hxupassword;
    }

    public void setHxupassword(String hxupassword) {
        this.hxupassword = hxupassword;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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

    public String getFtoken() {
        return ftoken;
    }

    public void setFtoken(String ftoken) {
        this.ftoken = ftoken;
    }

}
