
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-5-15 上午11:36:46
 * @email 971371860@qq.com
 * @version V1.0
 */
public class UserFromServerVO implements Serializable {

    private static final long serialVersionUID = -353748050455794460L;
    // public UserFromServerVO() {
    // // TODO Auto-generated constructor stub
    // }

    private UserFromServerInnerMenuVO userFromServerInnerMenuVO;
    // "userid":"49",
    // "username":"18602727134",
    /** 用户ID */
    private String userid = "";

    /** 用户名 */
    private String username = "";
    // "headpic":"暂无",

    /** 用户头像 */
    private String headpic = "";

    // "fullname":"吴章龙",
    /** 用户全名 */
    private String fullname = "";
    // "sex":"女",
    // "tel":"18602727134",
    /** 电话【多个以逗分割“,”】 */
    private String tel = "";

    /** 姓别 【汉字：男、女】 */
    private String sex = "";

    // "qq":"暂无",
    // "email":"暂无",
    /** QQ */
    private String qq = "";

    /** Email */
    private String email = "";
    // "address":"湖北省枣阳市",
    /** 地址 */
    private String address = "";

    // "idcard":"420606199208232020",
    /** 身份证 */
    private String idcard = "";
    // "isinpost":"1",
    /** 是否在岗，1=是；0=否 */
    private String isinpost;
    // "islogin":"1",
    /** 是否允许登录，1=是；0=否 */
    private String islogin;
    // "did":"6",
    /** 部门ID */
    private String did = "";
    // "pid":"9",
    /** 岗位ID */
    private String pid = "";
    // "logins":"30",
    /** 登录次数 */
    private String logins;
    // "fjptags":"xkbaab1",
    /** jpush的tags */
    private String fjptags = "";
    // "jpalias":"xkbaab10049",
    /** jpush的alias */
    private String jpalias = "";
    // "cornet":"暂无",
    /** 短号 */
    private String cornet = "";

    // "dname":"产品一组",
    /** 部门名称 */
    private String dname = "";
    // "pname":"安卓工程师",
    /** 职位名称 */
    private String pname = "";

    // "postsname":"基层员工",
    /** 职务名称 */
    private String postsname = "";
    // "management":"0",
    /** 是否管理层 ，1=是；0=否 */
    private String management = "";
    // "bosscomment":"0",
    // "bosscc":"0",
    // "attexcid":"821012",
    // "postsid":"3",

    // "siteurl":"http://cp.beisheng.wang",
    // "return":"true",
    // "ftoken":"RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O"
    private String siteurl;

    /** 应用程序标识，类似cookie */
    private String ftoken = "";

    /** BOSS批注权限 */
    private String bosscomment = "";

    /** BOSS抄送权限 */
    private String bosscc = "";

    /** 固定打卡里的编号 */
    private String attexcid = "";
    /** 职务ID */
    private String postsid = "";
    private String age;

    /** 推送的时间 */
    private List<UserFromServerVO> remind;
    private String ismobile;
    // private String isLocation;

    private String denglu;// 首次登陆

    private String isCrm;
    
    private String isChat;//此账号是否聊天，不能聊天都是不能登陆的账号

    public String getIsChat() {
        return isChat;
    }

    public void setIsChat(String isChat) {
        this.isChat = isChat;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }

    public String getIsCrm() {
        return isCrm;
    }

    public void setIsCrm(String isCrm) {
        this.isCrm = isCrm;
    }

    public String getIsmobile() {
        return ismobile;
    }

    public void setIsmobile(String ismobile) {
        this.ismobile = ismobile;
    }

    public List<UserFromServerVO> getRemind() {
        return remind;
    }

    public void setRemind(List<UserFromServerVO> remind) {
        this.remind = remind;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getRinfo() {
        return rinfo;
    }

    public void setRinfo(String rinfo) {
        this.rinfo = rinfo;
    }

    /** 推送的时间 */
    private String rtime = "";
    /** 推送的内容 */
    private String rinfo = "";

    private List<MenuVO> menu;

    private String firmcname = "";

    private String isboss = "0";

    public String getIsboss() {
        return isboss;
    }

    public void setIsboss(String isboss) {
        this.isboss = isboss;
    }

    public String getFirmcname() {
        return firmcname;
    }

    public void setFirmcname(String firmcname) {
        this.firmcname = firmcname;
    }

    public List<MenuVO> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuVO> menu) {
        this.menu = menu;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public String getLogins() {
        return logins;
    }

    public void setLogins(String logins) {
        this.logins = logins;
    }

    /** jpush的tags */
    public String getFjptags() {
        return fjptags;
    }

    public void setFjptags(String fjptags) {
        this.fjptags = fjptags;
    }

    /** jpush的alias */
    public String getJpalias() {
        return jpalias;
    }

    public void setJpalias(String jpalias) {
        this.jpalias = jpalias;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
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

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    /** 是否管理层 ，1=是；0=否 */
    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getFtoken() {
        return ftoken;
    }

    public void setFtoken(String ftoken) {
        this.ftoken = ftoken;
    }

    public String getBosscomment() {
        return bosscomment;
    }

    public void setBosscomment(String bosscomment) {
        this.bosscomment = bosscomment;
    }

    public String getBosscc() {
        return bosscc;
    }

    public void setBosscc(String bosscc) {
        this.bosscc = bosscc;
    }

    public String getAttexcid() {
        return attexcid;
    }

    public void setAttexcid(String attexcid) {
        this.attexcid = attexcid;
    }

    public String getPostsid() {
        return postsid;
    }

    public void setPostsid(String postsid) {
        this.postsid = postsid;
    }

    public UserFromServerInnerMenuVO getUserFromServerInnerMenuVO() {
        return userFromServerInnerMenuVO;
    }

    public void setUserFromServerInnerMenuVO(
            UserFromServerInnerMenuVO userFromServerInnerMenuVO) {
        this.userFromServerInnerMenuVO = userFromServerInnerMenuVO;
    }

    public String getDenglu() {
        return denglu;
    }

    public void setDenglu(String denglu) {
        this.denglu = denglu;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
