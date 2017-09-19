
package com.bs.bsims.model;

import java.io.Serializable;

/**
 * @author peck
 * @Description: 1. 文档获取部门列表接口
 * @date 2015-7-4 下午2:31:25
 * @email 971371860@qq.com
 * @version V1.0
 */

public class SharedfilesdHomeAllFragmentB implements Serializable {

    private static final long serialVersionUID = 5236352523615589511L;

    // "name":"管理部",
    // "did":"1",
    // "time":"1435561291",
    // "remind":"0"

    private String name;
    private String did;
    private String time;
    private String remind;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    /**
     * 最后上传文档时间（无文档则无此字段）
     * 
     * @return
     */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 红点标记（1有，0无）
     * 
     * @return
     */
    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }
}
