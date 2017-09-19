
package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 1. 文档获取部门列表接口
 * @date 2015-7-4 上午10:45:59
 * @email 971371860@qq.com
 * @version V1.0
 */

public class SharedfilesdHomeAllFragmentVO implements Serializable {

    private static final long serialVersionUID = 7059934317283536358L;

    private List<SharedfilesdHomeAllFragmentB> array;

    private String code;
    private String retinfo;
    private String system_time;

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

    public List<SharedfilesdHomeAllFragmentB> getArray() {
        return array;
    }

    public void setArray(List<SharedfilesdHomeAllFragmentB> array) {
        this.array = array;
    }

}
