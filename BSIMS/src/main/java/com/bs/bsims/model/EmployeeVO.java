
package com.bs.bsims.model;

import com.amap.api.maps2d.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeVO implements Serializable {

    public List<EmployeeVO> getPoints() {
        return points;
    }

    public void setPoints(List<EmployeeVO> points) {
        this.points = points;
    }

    private static final long serialVersionUID = -239676522961484066L;
    /** 用户ID */
    private String userid = "";
    /** 用户头像 */
    private String headpic = "";
    /** 用户全名 */
    private String fullname = "";
    /** 用户全名首字母 */
    private String initial = "";
    /** 部门名称 */
    private String dname = "";
    /** 职位名称 */
    private String pname = "";
    /** 用户电话 */
    private String tel = "";
    /** 是否选中 */
    private boolean isCheck;

    private String hxuname = "";

    /** 日志总数 */
    private int logcount;

    private String postsname = "";

    /** 未写数量 */
    private int notwritten;
    /** 评论数 */
    private int commentcount;

    /** 层级列表中分级需要 */
    private int user_level;

    /** 0未读1已读 */
    private String isread = "";
    /** type 1 添加 2 删除 3确认 0普通 */
    private int type = 0;
    /** 添加删除确认图片 */
    private int image = 0;

    /** 性别 */
    private String sex = "";

    private String status;

    private String content;

    private String name;
    private String price;
    private String num;
    private String unit;
    private String proven;
    private String cornet;
    private String phone;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String count;
    private String code;
    private String retinfo;
    private String system_time;
    private ArrayList<EmployeeVO> array;
    private ArrayList<DepartmentAndEmployeeVO> departments;
    private ArrayList<DepartmentAndEmployeeVO> users;

    private String title;

    private String t_lon;// 经度
    private String t_lat;// 纬度
    private String t_address;
    private String t_addtime;// 最新的时间戳
    private String t_status;// 在线离线
    private String t_mobilename;// 电话类型
    private String t_wantype;// 网络类型
    private String t_locationtype;// 定位类型
    private String t_city;// 城市

    public String getT_city() {
        return t_city;
    }

    public void setT_city(String t_city) {
        this.t_city = t_city;
    }

    private EmployeeVO user;
    private EmployeeVO info;
    private List<EmployeeVO> follow;
    private String dateTime;

    private String isdefault;// 是否必须知会人

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getT_mobilename() {
        return t_mobilename;
    }

    public void setT_mobilename(String t_mobilename) {
        this.t_mobilename = t_mobilename;
    }

    public String getT_wantype() {
        return t_wantype;
    }

    public void setT_wantype(String t_wantype) {
        this.t_wantype = t_wantype;
    }

    public String getT_locationtype() {
        return t_locationtype;
    }

    public void setT_locationtype(String t_locationtype) {
        this.t_locationtype = t_locationtype;
    }

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
    }

    private List<EmployeeVO> points;

    private Marker e_markers;

    public Marker getE_markers() {
        return e_markers;
    }

    public void setE_markers(Marker e_markers) {
        this.e_markers = e_markers;
    }

    public String getT_lon() {
        return t_lon;
    }

    public void setT_lon(String t_lon) {
        this.t_lon = t_lon;
    }

    public String getT_lat() {
        return t_lat;
    }

    public void setT_lat(String t_lat) {
        this.t_lat = t_lat;
    }

    public String getT_address() {
        return t_address;
    }

    public void setT_address(String t_address) {
        this.t_address = t_address;
    }

    public String getT_addtime() {
        return t_addtime;
    }

    public void setT_addtime(String t_addtime) {
        this.t_addtime = t_addtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<EmployeeVO> getArray() {
        return array;
    }

    public void setArray(ArrayList<EmployeeVO> array) {
        this.array = array;
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

    public String getSystem_time() {
        return system_time;
    }

    public void setSystem_time(String system_time) {
        this.system_time = system_time;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCornet() {
        return cornet;
    }

    public void setCornet(String cornet) {
        this.cornet = cornet;
    }

    public String getProven() {
        return proven;
    }

    public void setProven(String proven) {
        this.proven = proven;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    /** type 1 添加 2 删除 3确认 0普通 */
    public int getType() {
        return type;
    }

    /** type 1 添加 2 删除 3确认 0普通 */
    public void setType(int type) {
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getHxuname() {
        return hxuname;
    }

    public void setHxuname(String hxuname) {
        this.hxuname = hxuname;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
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

    public int getLogcount() {
        return logcount;
    }

    public void setLogcount(int logcount) {
        this.logcount = logcount;
    }

    public String getPostsname() {
        return postsname;
    }

    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    public int getNotwritten() {
        return notwritten;
    }

    public void setNotwritten(int notwritten) {
        this.notwritten = notwritten;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    @Override
    public String toString() {
        return "Employee [fullname=" + fullname + ", dname=" + dname + ", pname=" + pname + "]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<DepartmentAndEmployeeVO> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<DepartmentAndEmployeeVO> users) {
        this.users = users;
    }

    public EmployeeVO getUser() {
        return user;
    }

    public void setUser(EmployeeVO user) {
        this.user = user;
    }

    public List<EmployeeVO> getFollow() {
        return follow;
    }

    public void setFollow(List<EmployeeVO> follow) {
        this.follow = follow;
    }

    public EmployeeVO getInfo() {
        return info;
    }

    public void setInfo(EmployeeVO info) {
        this.info = info;
    }

}
