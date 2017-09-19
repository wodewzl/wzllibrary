
package com.bs.bsims.model;

import java.io.Serializable;

public class CcVO implements Serializable {

    private String logccid;
    private String isread;
    private String fullname;
    private String headpic;
    private String lcuser;

    public String getLogccid() {
        return logccid;
    }

    public void setLogccid(String logccid) {
        this.logccid = logccid;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getLcuser() {
        return lcuser;
    }

    public void setLcuser(String lcuser) {
        this.lcuser = lcuser;
    }

}
