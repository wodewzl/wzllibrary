
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 1. 任务统计首页接口 最外层数据
 * @date 2015-6-19 下午2:28:21
 * @email 971371860@qq.com
 * @version V1.0
 */

public class StatisticsBossTaskIndexVO implements Serializable {
    private static final long serialVersionUID = 6293399365371304938L;

    private List<StatisticsBossTaskIndex> num;
    private List<StatisticsBossTaskIndex2Chart> chart;
    private List<StatisticsBossTaskIndex> next;
    private List<StatisticsBossTaskIndex> last;

    private String maxchart;
    private String maxnum;
    private String code;
    private String retinfo;
    private String system_time;

    public List<StatisticsBossTaskIndex> getNum() {
        return num;
    }

    public void setNum(List<StatisticsBossTaskIndex> num) {
        this.num = num;
    }

    public List<StatisticsBossTaskIndex2Chart> getChart() {
        return chart;
    }

    public void setChart(List<StatisticsBossTaskIndex2Chart> chart) {
        this.chart = chart;
    }

    public List<StatisticsBossTaskIndex> getNext() {
        return next;
    }

    public void setNext(List<StatisticsBossTaskIndex> next) {
        this.next = next;
    }

    public List<StatisticsBossTaskIndex> getLast() {
        return last;
    }

    public void setLast(List<StatisticsBossTaskIndex> last) {
        this.last = last;
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

    public String getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getMaxchart() {
        return maxchart;
    }

    public void setMaxchart(String maxchart) {
        this.maxchart = maxchart;
    }
}
