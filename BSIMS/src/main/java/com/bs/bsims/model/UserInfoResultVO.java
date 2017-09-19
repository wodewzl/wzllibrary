
package com.bs.bsims.model;

import java.util.ArrayList;

public class UserInfoResultVO {
    private ArrayList<DepartmentAndEmployeeVO> departments;
    private ArrayList<DepartmentAndEmployeeVO> users;
    // private ArrayList<DepartmentAndEmployeeVO> array;
    private UserInfoResultVO array;
    private String departmentscount;
    private String userscount;

    private String code;
    private String retinfo;
    private String system_time;

    public ArrayList<DepartmentAndEmployeeVO> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<DepartmentAndEmployeeVO> departments) {
        this.departments = departments;
    }

    public ArrayList<DepartmentAndEmployeeVO> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<DepartmentAndEmployeeVO> users) {
        this.users = users;
    }

    public String getDepartmentscount() {
        return departmentscount;
    }

    public void setDepartmentscount(String departmentscount) {
        this.departmentscount = departmentscount;
    }

    public String getUserscount() {
        return userscount;
    }

    public void setUserscount(String userscount) {
        this.userscount = userscount;
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

    public UserInfoResultVO getArray() {
        return array;
    }

    public void setArray(UserInfoResultVO array) {
        this.array = array;
    }

    // public ArrayList<DepartmentAndEmployeeVO> getArray() {
    // return array;
    // }
    //
    // public void setArray(ArrayList<DepartmentAndEmployeeVO> array) {
    // this.array = array;
    // }

}
