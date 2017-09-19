
package com.bs.bsims.model;

import com.bs.bsims.download.domain.DownloadFile;

import java.io.Serializable;
import java.util.List;

/**
 * @author peck
 * @Description: 2. 文档列表页面接口 我上传的文档
 * @date 2015-7-4 下午4:50:06
 * @email 971371860@qq.com
 * @version V1.0
 */

public class SharedfilesdHomeMyUploadVO implements Serializable {

    private static final long serialVersionUID = -4595998182132945534L;

    private List<DownloadFile> array;

    private String count;
    private String code;
    private String retinfo;
    private String system_time;

    public List<DownloadFile> getArray() {
        return array;
    }

    public void setArray(List<DownloadFile> array) {
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
}
