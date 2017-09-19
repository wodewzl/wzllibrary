
package com.bs.bsims.constant;

/**
 * @author peck
 * @Description: 任务事件
 * @date 2015-5-28 下午5:09:25
 * @email 971371860@qq.com
 * @version V1.0
 */

public class Constant4TaskEventPath {

    /** 任务事件列表 http://cp.beisheng.wang/api.php/Task/index */
    public static final String TASKEVENTLIST_PATH = "api.php/Task/index";
    public static final String TASKEVENTLIST_PATH_T = "api.php/Task/index"
            + "/ftoken/";

    /** 任务事件详情 http:// 192.168.2.120/api.php/Task/view/id/177 */
    // public static final String TaskEventDetails_path = NET_URL
    // + "Task/getDetail";
    public static final String TASKEVENTDETAILS_PATH = "api.php/Task/view";
    /**
     * 任务初审，定审提交
     */
    public static final String TASKEVENTCONFIRMTASK_PATH = "api.php/Task/confirmTask";

    /**
     * 任务列表接口 例子： http://192.168.2.24/Loa/api.php/Task/index/ftoken/
     * NBjTcX2UMNjKJECNjIzNjIzNUM0NQO0O0OO0O0O/
     * 
     * @param modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     * @param isall 是否管理者（1是，0否）
     * @param userid 用户ID
     * @param statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    /** myfb:我发布的 不传则为BOSS列表 */
    public static final String TASKEVENTLIST_PATH_TYPE_VALUE1 = "myfb";
    /**
     * 任务列表接口 modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     */
    public static final String TASKEVENTLIST_PATH_MODEID0 = "0";
    /**
     * 任务列表接口 modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     */
    public static final String TASKEVENTLIST_PATH_MODEID1 = "1";
    /** mygj:我跟进的 **/
    public static final String TASKEVENTLIST_PATH_TYPE_VALUE2 = "mygj";
    /**
     * 任务列表接口 modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     */
    public static final String TASKEVENTLIST_PATH_MODEID2 = "2";
    /** myfz:我负责的 **/
    public static final String TASKEVENTLIST_PATH_TYPE_VALUE3 = "myfz";
    /**
     * 任务列表接口 modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     */
    public static final String TASKEVENTLIST_PATH_MODEID3 = "3";
    /** myxg:知会我的 **/
    public static final String TASKEVENTLIST_PATH_TYPE_VALUE4 = "myxg";
    /**
     * 任务列表接口 modeid 类型（0,1,2,3,4）0全部 1我发布的 2我发布的 3我跟进的 4知会我的
     */
    public static final String TASKEVENTLIST_PATH_MODEID4 = "4";

    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID0 = "0";
    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID1 = "1";
    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID2 = "2";
    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID3 = "3";
    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID4 = "4";
    /**
     * 任务列表接口 statusid 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    public static final String TASKEVENTLIST_STATUSID5 = "5";

    /**
     * 任务 编辑任务结束时间权限 http://cp.beisheng.wang//api.php/Task/changeTime?alid=127&ftoken
     * =RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O&time=2015-06-05&userid=38
     */
    public static final String TASK_CHANGETIME_PATH = "/api.php/Task/changeTime";

    /** 更改任务进度 http://cp.beisheng.wang/api.php/Task/addSchedule */
    // public static final String TaskEventPercent_path = NET_URL
    // + "TaskSchedule/insert";
    public static final String TASKEVENT_PERCENT_PATH = "/api.php/Task/addSchedule";

    /** 任务评论 */
    public static final String TASKEVENT_GETCOMMENT_PATH = "/api.php/Task/getComment/";

}
