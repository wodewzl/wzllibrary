
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 二、统计功能接口 1.任务统计接口
 * @date 2015-6-13 下午2:54:37
 * @email 971371860@qq.com
 * @version V1.0
 */

public class TaskStatisticsVO implements Serializable {

    private static final long serialVersionUID = 2573052303759081078L;

    private List<TaskStatistics> array;
    private String count;
    private String code;
    private String retinfo;
    private String num;
    /** 全部任务数量 **/
    private String allnum;
    private String system_time;

    public List<TaskStatistics> getArray() {
        return array;
    }

    public void setArray(List<TaskStatistics> array) {
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAllnum() {
        return allnum;
    }

    public void setAllnum(String allnum) {
        this.allnum = allnum;
    }

}
