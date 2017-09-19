
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author peck
 * @Description: 任务统计接口
 * @date 2015-6-13 下午2:55:20
 * @email 971371860@qq.com
 * @version V1.0
 */

public class TaskStatistics implements Serializable {
    private static final long serialVersionUID = -3441426980311887518L;

    // 返回 参数 说明
    // type =1时返回一下参数////////////
    // num 个数
    // status 状态（0进行中，1待初审，2待定审，3完成，5过期）
    // percent 百分比
    // ////////////////////////////////////////////////////////////////////////////
    // type =2时返回一下参数
    // num 个数
    // dname 部门名称
    // did 部门ID
    // percent 百分比

    private String dname;
    private String num;
    private String status;
    private String percent;
    private String did;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

}
