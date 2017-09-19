
package com.bs.bsims.model;

import java.util.List;

public class DanganLineChartVO {
    private List<DanganLineChartVO> chart;
    private List<DanganLineChartVO> list;
    private String name;
    private String num;
    private String date;
    private String duration;
    private String maxchart;
    private String unit;
    private String code;
    private String retinfo;
    private String system_time;
    private boolean visible = true;

    public DanganLineChartVO() {
        super();
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<DanganLineChartVO> getChart() {
        return chart;
    }

    public void setChart(List<DanganLineChartVO> chart) {
        this.chart = chart;
    }

    public List<DanganLineChartVO> getList() {
        return list;
    }

    public void setList(List<DanganLineChartVO> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaxchart() {
        return maxchart;
    }

    public void setMaxchart(String maxchart) {
        this.maxchart = maxchart;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

}
