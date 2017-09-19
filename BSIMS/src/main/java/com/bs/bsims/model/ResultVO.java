
package com.bs.bsims.model;

import com.bs.bsims.application.BSApplication;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultVO implements Serializable {

    public static synchronized ResultVO getInstance() {
        return BSApplication.getInstance().getResultVO();
    }

    private ArrayList<DepartmentAndEmployeeVO> departments;
    private ArrayList<DepartmentAndEmployeeVO> users;
    // private ResultVO array;
    private ArrayList<DepartmentAndEmployeeVO> array;

    // public ResultVO getArray() {
    // return array;
    // }
    //
    // public void setArray(ResultVO array) {
    // this.array = array;
    // }

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

    public ArrayList<DepartmentAndEmployeeVO> getArray() {
        return array;
    }

    public void setArray(ArrayList<DepartmentAndEmployeeVO> array) {
        this.array = array;
    }

}
