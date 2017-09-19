
package com.bs.bsims.constant;

/**
 * @author peck
 * @Description: 统计接口
 * @date 2015-6-13 下午4:16:02
 * @email 971371860@qq.com
 * @version V1.0
 */

public class Constant4Statistics {

    /** 任务 */
    public static final String TASK_PATH = "api.php/Boss/taskStatistics";
    /** 1. 任务统计首页接口 */
    public static final String TASK_BOSS_INDEX_PATH = "api.php/Boss/taskIndex";
    /** 2. 任务点击具体某月时下方展示接口 */
    public static final String TASK_BOSS_INDEX_MONTH_PATH = "api.php/Boss/taskMonth";
    /** 3. 任务饼形图接口 */
    public static final String TASK_BOSS_PIECHART_PATH = "api.php/Boss/taskMonthPie";
    /** 6. 考勤统计首页接口 */
    public static final String ATTENDANCES_BOSS_INDEX_PATH = "api.php/Boss/attendanceStatistics";
    /** 7. 考勤统计首页点击具体月份时下方展示接口 */
    public static final String ATTENDANCES_BOSS_INDEX_MONTH_PATH = "api.php/Boss/attendanceMonth";
    /** 5. 考勤统计获取部门列表接口 */
    public static final String ATTENDANCES_BOSS_INDEX_GETDEP_PATH = "api.php/Boss/getDep";
    /** 8. 考勤统计饼形图接口 */
    public static final String ATTENDANCES_BOSS_MONTHPIE_PATH = "api.php/Boss/attendanceMonthPie";
    /** 10. 各部门下详细的考勤统计接口 */
    public static final String ATTENDANCES_BOSS_DEPARTMENTE_PATH = "api.php/BossStatistics/attendanceList";
    /** 9.各部门各月考工统计详情接口 */
    public static final String ATTENDANCES_BOSS_DEPARTMENT_DETAIL_PATH = "api.php/Boss/testWork";
    /** 获取出勤情况各项 */
    public static final String DANGAN_CHUQING_INFOL_PATH = "api.php/Archives/getAttendance";

}
