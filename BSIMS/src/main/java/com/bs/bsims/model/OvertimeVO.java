
package com.bs.bsims.model;

import java.util.List;

public class OvertimeVO {

    private List<EmployeeVO> appUser;
    private List<EmployeeVO> insUser;

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

}
