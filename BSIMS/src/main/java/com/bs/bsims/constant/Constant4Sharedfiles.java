
package com.bs.bsims.constant;

/**
 * @author peck
 * @Description: 文档管理接口
 * @date 2015-7-4 上午10:37:54
 * @email 971371860@qq.com
 * @version V1.0
 */

public class Constant4Sharedfiles {

    /** 1. 文档获取部门列表接口 */
    public static final String DEPLIST_PATH = "api.php/Sharedfiles/getDepList";
    /** 2. 文档列表页面接口 MyUpdate 类型（0:全部文档，-1:我收藏的文档，-2:我上传的文档，1-N：一级部门ID下的文档）（必传） */
    // http://cp.beisheng.wang/api.php/Sharedfiles/index/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
    public static final String TYPE_LIST_PATH = "api.php/Sharedfiles/index";
    /** 3. 文档上传添加接口 */
    // http://cp.beisheng.wang/api.php/Sharedfiles/insert/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
    public static final String INSERT_PATH = "api.php/Sharedfiles/insert";
    /** 4. 文档收藏取消收藏接口 */
    // http://cp.beisheng.wang/api.php/Sharedfiles/likes/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
    public static final String LIKES_PATH = "api.php/Sharedfiles/likes";
    //
    /** 5. 文档下载的接口 */
    // http://cp.beisheng.wang/api.php/Sharedfiles/download/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
    public static final String DOWNLOAD_PATH = "api.php/Sharedfiles/download";
    /** 6. 文档 删除的接口 */
    // http://cp.beisheng.wang/api.php/Sharedfiles/delete/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
    public static final String FILES_LIST_DELETE = "api.php/Sharedfiles/delete";

}
