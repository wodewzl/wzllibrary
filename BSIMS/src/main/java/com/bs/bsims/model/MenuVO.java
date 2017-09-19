
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

public class MenuVO implements Serializable {
    private String mname;
    private String mview;
    private String malias;
    private List<MenuVO> menu;

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMview() {
        return mview;
    }

    public void setMview(String mview) {
        this.mview = mview;
    }

    public String getMalias() {
        return malias;
    }

    public void setMalias(String malias) {
        this.malias = malias;
    }

    public List<MenuVO> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuVO> menu) {
        this.menu = menu;
    }

}
