
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-6-24 下午2:11:19
 * @email 971371860@qq.com
 * @version V1.0
 */

public class StatisticsBossAttendanceIndexVO implements Serializable {

    private static final long serialVersionUID = -4138837128490503211L;

    // allnum 合计（根据类型，单位不同）
    // maxnum 任务标题
    // minnum 任务开始时间
    private String allnum;
    private String maxnum;
    private String minnum;
    private String unit;

    private List<StatisticsBossTaskIndex2Chart> chart;
    private List<StatisticsBossAttendanceIndexShow> show;

    private String code;
    private String retinfo;
    private String system_time;

    public String getAllnum() {
        return allnum;
    }

    public void setAllnum(String allnum) {
        this.allnum = allnum;
    }

    public String getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getMinnum() {
        return minnum;
    }

    public void setMinnum(String minnum) {
        this.minnum = minnum;
    }

    public List<StatisticsBossTaskIndex2Chart> getChart() {
        return chart;
    }

    public void setChart(List<StatisticsBossTaskIndex2Chart> chart) {
        this.chart = chart;
    }

    public List<StatisticsBossAttendanceIndexShow> getShow() {
        return show;
    }

    public void setShow(List<StatisticsBossAttendanceIndexShow> show) {
        this.show = show;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
