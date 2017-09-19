
package com.bs.bsims.constant;

import android.os.Environment;

import org.apache.http.protocol.HTTP;

public class Constant {
    public static final String FIRSTID = "firstid";
    public static final String LASTID = "lastid";
    public static final String SEX_MAN = "男";
    public static final String SEX_WOMAN = "女";
    public static final String ENCODING = HTTP.UTF_8;

    public static final String TYPE_DOCUMENT = "OD";// 公文
    public static final String TYPE_NOTICE = "ON";// 通知
    public static final String TYPE_ZHIDU = "OS";// 制度

    // 广播常量
    public static final String HOME_MSG = "home_msg";
    public static final String DISCUSS_MSG = "discuss_msg";
    public static final String ATTENTION_USER_MSG = "attention_user_msg";
    public static final String UPLOAD_HEAD_ICON_MSG = "upload_head_icon_msg";

    // public static String HTTP_TITLE =
    public static final String FTOKEN = "/ftoken/";
    public static final String FTOKEN_PARAMS = "ftoken";
    public static final String COMPANY = "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O";

    public static final String USER_ID = "/logid/309";
    public static final String RESULT_CODE = "200";
    public static final String RESULT_CODE400 = "400";

    // 评论对应的的code
    public static final int DISCUSS_CODE = 301;
    public static final int DISCUSS_AGREE_CODE = 302;
    public static final int DISCUSS_OPPOSE_CODE = 303;

    public static final String NET_URL = "http://cp.beisheng.wang/api.php/";
    public static final String CP_NET_URL = "http://cp.beisheng.wang/api.php/";

    /** 暂无 */
    public static final String nil = "暂无";

    /** 传递给fragment时候的key值 */
    public static final String DocKey = "DocReceiverkey";
    /** DocDetialsActivity页面的广播action */
    /** PublicNoticeDetailsActivity页面的广播action */
    public static final String NoticeDetailsAction = "NoticeDetailsAction";
    public static final String COPY_IMAGE = "EASEMOBIMG";

    /** 语音目录 */

    /** 根目录 */
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String APP_NAME_PATH = "/bs";
    /** 语音目录 */
    public static final String AUDIO_PATH = ROOT_PATH + APP_NAME_PATH + "/audio/";

    public static class FileInfo {

        /** 根目录 */
        public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

        public static final String APP_NAME_PATH = "/oa";

        /** 错误日志保存的地址 */
        public static final String EXCEPTION_LOG = ROOT_PATH + "/OAUnknowError.log";

        /** 保存的头像目录 */
        public static final String SAVE_PIC_PATH = APP_NAME_PATH + "/pic/";

        /** 保存的文件目录 */
        public static final String SAVE_FILE_PATH = APP_NAME_PATH + "/download/";

        /** 语音目录 */
        public static final String AUDIO_PATH = ROOT_PATH + APP_NAME_PATH + "/audio/";

        /** apk目录 */
        public static final String UPDATE_PATH = ROOT_PATH + APP_NAME_PATH + "/update/";

        /** db目录 */
        public static final String DB_PATH = ROOT_PATH + APP_NAME_PATH + "/db/";
    }

    public static class SignIn {
        /** 签到列表 */
        // public static final String SIGN_IN_LIST = NET_URL +
        // "Attendance/getList";
        // http:// cp.beisheng.wang /api.php/Attendance/ getMoveList
        public static final String SIGN_IN_LIST = "api.php/Attendance/getMoveList";

        /**
         * 移动打卡签到 // http://cp.beisheng.wang/api.php/Attendance/ insert
         */
        // public static final String SIGN_IN = NET_URL + "Attendance/insert";
        public static final String SIGN_IN = "api.php/Attendance/insert";
    }

    /**
     * 请求后台数据，默认一次请求多少条 15
     */
    public static final int PAGENUM = 15;
    /**
     * 测试用户数据
     */
    public static final int USERIDTEST = 13;

    // http://manager.beisheng.wang/api.php/Login/index/"
    // public static final String LOGIN_PATH =
    // "http://manager.beisheng.wang/api.php/Login/index/";
    public static final String LOGIN_PATH = "http://manager.beisheng.wang/api.php/Login/index/";//老的登陆接口1.26以前的（包含1.26）
    public static final String CP_LOGIN_PATH = "http://cp.beisheng.wang/api.php/Login/index/";

    // 新的登录接口 2.0
    public static final String LOGIN_URL = "http://manager.beisheng.wang/api.php/Login/testLogin";
    /**
     * 第一次登录成功后，当前用户的登录地址
     */
    public static String LOGIN_MY_HOST = "http://manager.beisheng.wang/api.php/Login/index/";
    public static String LOGIN_MY_HOST_ADD = "api.php/Login/index/";
    /**
     * 当前用户的 token值
     */
    public static String LOGIN_MY_FTOKEN = "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O";
    /**
     * 工作界面 http://cp.beisheng.wang/api.php
     */
    public static String WORKFRAGMENT;
    public static final String WORKGENERALFRAGMENT = "WorkGeneralFragment";
    public static final String WORKBOSSFRAGMENT = "WorkBossFragment";

    /**
     * 考勤详情
     */
    public static final String WORK_ATTENDANCE_DETAIL = "api.php/Attendance/getUserGather";

    public static final String SDCARD_IMG = "/sdcard/bs/IMG";

    /** 附件广播 */
    public static final String ACTION_NAME_FUJIAN = "FUJIAN";

    /** 上传文件 */
    public static final String UPLOAD_FILEPATH = NET_URL + "Sharedfiles/insert";

    /** 任务事件的url */
    public static class TaskEventPath {
        /** 任务要显示的分类 */
        public static final String TaskEventList_type_path = NET_URL + "Task/getTaskStyle";

        /** 任务事件列表 http://cp.beisheng.wang/api.php/Task/index */
        // public static final String TaskEventList_path = NET_URL
        // + "Task/getlist";
        public static final String TaskEventList_path = NET_URL + "Task/index";

        /** 任务事件详情 http:// 192.168.2.120/api.php/Task/view/id/177 */
        // public static final String TaskEventDetails_path = NET_URL
        // + "Task/getDetail";
        public static final String TaskEventDetails_path = NET_URL + "Task/view";

        /** 更改任务进度 http://cp.beisheng.wang/api.php/Task/addSchedule */
        // public static final String TaskEventPercent_path = NET_URL
        // + "TaskSchedule/insert";
        public static final String TaskEventPercent_path = NET_URL + "Task/addSchedule";

        /** 确认任务完成 */
        public static final String TaskEvenFinish_path = NET_URL + "Task/confirmTask";

        /** 发布任务 */
        public static final String TaskEventRelease_path = NET_URL + "Task/insert";
        public static final String TaskEventRelease_path_T = "api.php/Task/insert";
        /** 回复某个任务 http:// 192.168.2.120/api.php/Task/AddReply/id/177 */
        // public static final String TaskEventReply_path = NET_URL
        // + "TaskReply/insert";
        public static final String TaskEventReply_path = NET_URL + "/Task/AddReply/";
        public static final String TaskEventGetReply_path = NET_URL + "TaskReply/getReply";
        /** 回复某个任务 点衰 http:// 192.168.2.120/api.php/Task/AddReply/id/177 */
        public static final String TASKEVENTMAKEDECLINE_PATH = "api.php/Task/makeDecline/";
        /** 回复某个任务 赞美 http:// 192.168.2.120/api.php/Task/AddReply/id/177 */
        public static final String TASKEVENTMAKEPRAISE_PATH = "api.php/Task/makePraise/";
        public static final String TASKEVENTREPLY_PATH_NOSTART = "api.php/Task/AddReply/";

        /** 任务评论 */
        public static final String TASKEVENTGETCOMMENT_PATH = NET_URL + "/Task/getComment/";

        /** myfb:我发布的 不传则为BOSS列表 */
        public static final String TASKEVENTLIST_PATH_TYPE_VALUE1 = "myfb";
        /** mygj:我跟进的 **/
        public static final String TASKEVENTLIST_PATH_TYPE_VALUE2 = "mygj";
        /** myfz:我负责的 **/
        public static final String TASKEVENTLIST_PATH_TYPE_VALUE3 = "myfz";
        /** myxg:知会我的 **/
        public static final String TASKEVENTLIST_PATH_TYPE_VALUE4 = "myxg";
    }

    public static class DownloadPath {
        /** 下载首页面 */
        public static final String DOWNLOAD_ALLFILE_LIST = NET_URL + "Sharedfiles/index";
        // public static final String DOWNLOAD_ALLFILE_LIST = base_url_2 +
        // "Sharedfiles/index";

        /** 删除文件 */
        public static final String DELETE_FILE = NET_URL + "Sharedfiles/delete";

        /** 更改文件状态 */
        public static final String CHANGE_STATUS = NET_URL + "Sharedfiles/changeStatus";

        /** 我的上传列表 */
        public static final String UPLOAD_LIST = NET_URL + "Sharedfiles/index";
    }

    public static final String APP_UPDATE = "http://manager.beisheng.wang/api.php/Version/getVersion/";

    /** 推送数据类型 */
    public static class JPushTypeId {
        /** 通知 */
        public static final int JPUSHTYPEID1 = 1;
        /** 最新公文 公文抄送 */
        public static final int JPUSHTYPEID2 = 2;
        /** 企业动态 风采发布 */
        public static final int JPUSHTYPEID3 = 3;
        /** BOSS批注 */
        public static final int JPUSHTYPEID4 = 4;
        public static final int JPUSHTYPEID5 = 5;
        /** 任务管理 任务详情 */
        public static final int JPUSHTYPEID6 = 6;
        /** 创意 建议 */
        public static final int JPUSHTYPEID7 = 7;
        /** 请假 */
        public static final int JPUSHTYPEID8 = 8;
        /** 物资详情 */
        public static final int JPUSHTYPEID9 = 9;
        /** 加班详情 */
        public static final int JPUSHTYPEID10 = 10;
        /** 费用申请详情 */
        public static final int JPUSHTYPEID11 = 11;
        /** 考勤申请详情 */
        public static final int JPUSHTYPEID12 = 12;
        /** 未写日志提醒 */
        public static final int JPUSHTYPEID13 = 13;
        // /** 任务到期提醒 和任务管理 任务详情 去任务详情 6 */
        // public static final int JPUSHTYPEID14 = 14;
        /** 打卡提醒 */
        public static final int JPUSHTYPEID14 = 14;
        // /** 奖惩消息回执 和公文一样 去公文 */
        // public static final int JPUSHTYPEID15 = 15;
        /** 员工日程提醒 */
        public static final int JPUSHTYPEID15 = 15;
        /** 日程提醒 */
        public static final int JPUSHTYPEID21 = 21;

        /** 制度提醒 */
        public static final int JPUSHTYPEID22 = 22;

        // CRM商机转移提醒
        public static final int JPUSHTYPEID23 = 23;
        // CRM合同到期提醒
        public static final int JPUSHTYPEID24 = 24;
        // CRM客户提醒
        public static final int JPUSHTYPEID25 = 25;
        // CRM回款审批
        public static final int JPUSHTYPEID26 = 26;
        // 日志评论
        public static final int JPUSHTYPEID27 = 27;
        // 日志抄送
        public static final int JPUSHTYPEID28 = 28;
        // 打卡机离线提醒
        public static final int JPUSHTYPEID29 = 29;
        // CRM联系人提醒
        public static final int JPUSHTYPEID30 = 30;
        // CRM跟单提醒
        public static final int JPUSHTYPEID31 = 31;
        // 合同审批
        public static final int JPUSHTYPEID32 = 32;
        // 离职
        public static final int JPUSHTYPEID33 = 33;
        // 修改
        public static final int JPUSHTYPEID34 = 34;
        // 红包推送
        public static final int JPUSHTYPEID35 = 35;

    }

    public static final String PREFERENCES = "AlarmClock";

    public static final String SDCARD_CACHE = "com.beiseng/files/"; // 文件sdk缓存
    // 发布权限对应的编号
    public static final String LIMITS_OFFICE001 = "Office001";// 打卡列表

    // php迭代变更接口参数
    public static final String PHPVERSIONPARMERS = "1.2.5";

    public static final String LIMITS_OFFICE007 = "Office007";// 云盘
    public static final String LIMITS_OFFICE008 = "Office008";// 日程
    public static final String LIMITS_OFFICE009 = "Office009";// 人资报告
    public static final String LIMITS_OFFICE010 = "Office010";// 合同审批
    public static final String LIMITS_OFFICE011 = "Office011";// 回款审批

    public static final String LIMITS_AFFAIR001 = "Affair001";// 审批
    public static final String LIMITS_AFFAIR002 = "Affair002";// 任务
    public static final String LIMITS_AFFAIR003 = "Affair003";// 创意建议
    public static final String LIMITS_AFFAIR004 = "Affair004";
    public static final String LIMITS_AFFAIR005 = "Affair005";// 行政通知
    public static final String LIMITS_AFFAIR006 = "Affair006";// 人事通知
    public static final String LIMITS_AFFAIR007 = "Affair007";// 考勤管理
    public static final String LIMITS_AFFAIR008 = "Affair008";// 企业动态
    public static final String LIMITS_AFFAIR009 = "Affair009";// 公文查阅
    public static final String LIMITS_AFFAIR010 = "Affair010";// 员工档案
    public static final String LIMITS_AFFAIR011 = "Affair011";// 用品统计
    public static final String LIMITS_AFFAIR012 = "Affair012";// 用品统计

    // 特殊查看权限
    public static final String LIMITS_SPECIAL001 = "Special001";// 全部审批
    public static final String LIMITS_SPECIAL002 = "Special002";// 全部任务
    public static final String LIMITS_SPECIAL003 = "Special003";// 全部意见
    public static final String LIMITS_SPECIAL004 = "Special004";// 公海权限

    // 发布权限
    public static final String LIMITS_PUBLISH001 = "Publish001";// 发起审批
    public static final String LIMITS_PUBLISH002 = "Publish002";// 发布任务
    public static final String LIMITS_PUBLISH003 = "Publish003";// 意见发布
    public static final String LIMITS_PUBLISH004 = "Publish004";// 行政通知
    public static final String LIMITS_PUBLISH005 = "Publish005";// 人事通知
    public static final String LIMITS_PUBLISH006 = "Publish006";// 发布公文
    public static final String LIMITS_PUBLISH007 = "Publish007";// 发布制度

    // crm菜单
    public static final String LIMITS_MARKETING001 = "Marketing001";// 跟单记录
    public static final String LIMITS_MARKETING002 = "Marketing002";// 上报位置
    public static final String LIMITS_MARKETING003 = "Marketing003";// 联系人
    public static final String LIMITS_MARKETING004 = "Marketing004";// 客户管理
    public static final String LIMITS_MARKETING005 = "Marketing005";// 销售商机
    public static final String LIMITS_MARKETING006 = "Marketing006";// 合同管理
    public static final String LIMITS_MARKETING007 = "Marketing007";// 销售目标

    public static final String LOGINFAST = "http://tiyan.bqixing.com/api.php/Login/index";
    public static final String APPREGISTERGETCODE = "http://manager.beisheng.wang/api.php/RegUser/getVerifyCode";// 注册信息获取验证码
    public static final String APPREGISTERPOSTPHONECODE = "http://manager.beisheng.wang/api.php/RegUser/verifyCode";// 提交手机和验证码
    public static final String APPREGISTERPOSTINFO = "http://manager.beisheng.wang/api.php/RegUser/regSave";// 提交全部注册信息

    public static final String JOURNAL_LISTW = "api.php/Log/getInfo";
    public static final String JOURNAL_LIST = "api.php/Log/getInfo" + FTOKEN;
    public static final String JOURNAL_DISCUSS = "api.php/Log/getcomment" + FTOKEN;
    public static final String JOURNAL_COMMIT = "api.php/Log/updateLog/";
    public static final String CREATIVE_IDEA_DETAIL = "api.php/Ideasexamine/view" + FTOKEN;
    public static final String USER_INFO = "api.php/AddressList/getDepartUsers" + FTOKEN;
    public static final String USER_INFO_DETAIL = "api.php/AddressList/index" + FTOKEN;
    public static final String CREATIVE_IDEA = "api.php/Ideasexamine/index" + FTOKEN;
    public static final String CREATIVE_IDEA_DISCUSS = "api.php/Ideasexamine/getComment";
    public static final String CREATIVE_IDEA_ADD = "api.php/Ideas/insert/";

    public static final String ATTENDANCE_SUMMARY_USER = "api.php/Attendance/getUsers" + FTOKEN;
    public static final String APPROVAL_LIST = "api.php/Approval/myRelease" + FTOKEN;
    public static final String APPROVAL_INFORM = "api.php/Approval/calculationTime_1_2_1" + FTOKEN;
    public static final String APPROVAL_PUSH = "api.php/Approval/askleaveAdd/";
    public static final String APPROVAL_LEAVE_DETAIL = "api.php/Approval/askleaveView" + FTOKEN;
    public static final String APPROVAL_OVERTIME = "api.php/Approval/overtimeUser" + FTOKEN;
    public static final String APPROVAL_OVERTIME_PUSH = "api.php/Approval/overtimeAdd/";
    public static final String APPROVAL_OVERTIME_DETAIL = "api.php/Approval/overtimeView" + FTOKEN;

    public static final String APPROVAL_ATTENDANCE = "api.php/Approval/appealIndex" + FTOKEN;
    public static final String APPROVAL_ATTENDANCE_PROOF = "api.php/Approval/appealUser" + FTOKEN;
    public static final String APPROVAL_ATTENDANCE_PUSH = "api.php/Approval/appealAdd/" + FTOKEN;
    public static final String APPROVAL_ATTENDANCE_DETAIL = "api.php/Approval/appealView" + FTOKEN;
    public static final String APPROVAL_FEE_APPLY = "api.php/Approval/feeUser" + FTOKEN;
    public static final String APPROVAL_FEE_APPLY_PUSH = "api.php/Approval/feeAdd/" + FTOKEN;
    public static final String APPROVAL_FEE_APPLY_DETAIL = "api.php/Approval/feeView" + FTOKEN;

    public static final String NOTICE_OBJECT_POSTS = "api.php/Deploy/getPosts" + FTOKEN;
    public static final String NOTICE_OBJECT_POSITIONS = "api.php/Deploy/getPositions" + FTOKEN;
    public static final String NOTICE_OBJECT_PPOSTS = "api.php/Deploy/getPposts" + FTOKEN;

    public static final String NOTICE_PUBILSH = "api.php/Articles/insert/";
    public static final String PUBLISH_LIST = "api.php/Articles/getListOne" + FTOKEN;// 通知、公文、制度接口
    public static final String PUBLISH_DETAIL = "api.php/Articles/getDetail" + FTOKEN;
    public static final String NOTIFY_URL = "api.php/Message/index" + FTOKEN;
    public static final String JOURNAL_AGREE_URL = "api.php/Log/logPraise/";
    public static final String JOURNAL_OPPOSE_URL = "api.php/Log/logDecline/";
    public static final String JOURNAL_COMMIT_DISSCUSS_URL = "api.php/Log/updateComment/";
    public static final String NOTICE_DISCUSS_URL = "api.php/Articles/getReply" + FTOKEN;
    public static final String NOTICE_COMMIT_DISCUSS_URL = "api.php/Articles/insertReply/";
    public static final String NOTICE_DISCUSS_OPP_ADD_AGE_URL = "api.php/Articles/Attitude/";

    public static final String APPROVAL_IDEA_COMMIT_URL = "api.php/Approval/examine/";
    public static final String APPROVAL_SUPPLIES_URL = "api.php/Approval/materialUser" + FTOKEN;
    public static final String APPROVAL_SUPPLIES_ADD_URL = "api.php/Approval/materialAdd/";
    public static final String APPROVAL_SUPPLIES_DETAIL_URL = "api.php/Approval/materialView" + FTOKEN;
    public static final String CREATIVE_AGREE_URL = "api.php/Ideas/makePraise/";
    public static final String CREATIVE_OPPOSE_URL = "api.php/Ideas/makeDecline/";
    public static final String CREATIVE_ADOPTION_URL = "api.php/Ideasexamine/Adopt/";
    public static final String CREATIVE_CHECK_URL = "api.php/Ideasexamine/examine/";
    public static final String CREATIVE_DISCUSS_COMMIT_URL = "api.php/Ideas/AddReply/";
    public static final String MAIN_MESSAGE_URL = "api.php/Index/index_1_2_4";
    public static final String MAIN_MESSAGE_DELETE_URL = "api.php/IndexMsg/remove";// 首页消息侧滑删除
    public static final String MAIN_MESSAGE_TOP_URL = "api.php/IndexMsg/top";// 首页消息置顶
    public static final String MAIN_MESSAGE_REMOVE_TOP_URL = "api.php/IndexMsg/removeTop";// 首页消息取消置顶

    public static final String MY_APPROVAL_URL = "api.php/Approval/getAllList" + FTOKEN;

    public static final String APPROVAL_MYAPPROVAL_URL = "api.php/Approval/myApproval" + FTOKEN;
    public static final String APPROVAL_MYNOTIFYL_URL = "api.php/Approval/myNotify" + FTOKEN;
    public static final String APPROVAL_MYRELEASE_URL = "api.php/Approval/myRelease" + FTOKEN;
    public static final String UPDATE_PASSWORD = "api.php/Users/changePwd/";

    public static final String PROVENED_URL = "api.php/Approval/appealWitness/";

    public static final String JOURNAL_BOSS_CC = "api.php/Log/makeCopy/";

    public static final String BOSS_INDEX = "api.php/Boss/index/";
    public static final String BOSS_SCHEDULE = "api.php/Schedule/index/";
    public static final String BOSS_INDEX_AATTENDANCE = "api.php/Boss/bossList/";

    public static final String BOSS_WORK = "api.php/Boss/work/";
    public static final String SCHEDULE_ADD = "api.php/Schedule/insert/";
    public static final String SCHEDULE_DETAIL = "api.php/Schedule/view/";
    public static final String STATISTICS_ARTICLES = "api.php/Goods/statistics";

    public static final String STATISTICS_ARTICLES_DETAIL = "api.php/Goods/detail";
    public static final String STATISTICS_INDEX = "api.php/Boss/statisticsIndex";

    public static final String MESSAGE_BOSS = "api.php/Boss/weather";
    public static final String WARN_URL = "api.php/Warning/getWarn";
    public static final String STATISTICS_APPROVAL = "api.php/ApprovalStatistics/index";
    public static final String STATISTICS_APPROVAL_DETAIL = "api.php/BossStatistics/detail";
    public static final String STATISTICS_APPROVAL_PIE = "api.php/ApprovalStatistics/monthpie";

    public static final String DANGANARCHIVESWORKING = "api.php/Archives/Working";// 档案工作详情接口
    public static final String DANGANARCHIVESWORKINGINDEX = "api.php/Archives/index";// 档案工作详情接口

    public static final String DANGANARCHIVESWORKINGATTEN = "api.php/Archives/getAttendance";// 档案出情详情接口

    public static final String DANGANARCHIVESALLUSERINFO = "api.php/Archives/index";// 档案查询全部员工的接口

    public static final String DANGANARCHIVESALLMORESELECT = "api.php/Archives/getCondition";// 档案第三个筛选条件的接口

    public static final String HUMANRESOUREREPORT = "api.php/Personnelstatistics/Personnel";// 人力资源报告的接口
    public static final String HUMANRESOURERERECRUITMENT = "api.php/Personnelstatistics/NewHires";// 招聘资源报告的接口
    public static final String HUMANRESOURERELEAVEWORK = "api.php/Personnelstatistics/Resignation";// 离职资源报告的接口
    public static final String HUMANRESOURERESALARYCHANGE = "api.php/Personnelstatistics/Trains";// 培训资源报告的接口
    public static final String HUMANRESOUREREMONEYCHANGE = "api.php/Personnelstatistics/UserPay";// 薪资资源报告的接口
    public static final String HUMANRESOUREREURLNULLPOTIONT = "api.php/Personnelstatistics/blank";// 资源跳转的空白指向页面

    public static final String APPMARKETONLINEYAN = "api.php/Modules/demo";// 2.0商店的在线演示
    public static final String APPMARKETONLINEDETAIL = "api.php/Modules/detail";// 2.0商店的详细介绍

    public static final String UPLOAD_LOG_URL = "http://manager.beisheng.wang/api.php/WebServer/crashAndroid"; // 上传log到服务器
    public static final String CUSTOM_APPROVAL_LIST = "api.php/Approval/customType"; // 自定义审批列表
    public static final String APPROVAL_PUBLIC_LIST = "api.php/Approval/getApprovalType"; // 发布审批列表
    public static final String CUSTOM_APPROVAL = "api.php/Approval/customShow/";// 自定义审批内容接口
    public static final String CUSTOM_APPROVAL_DETAIL = "api.php/Approval/customView/";// 自定义审批内容接口
    public static final String CUSTOM_APPROVAL_ADD = "api.php/Approval/customAdd" + FTOKEN;// 自定义审批发布接口
    public static final String APPROVAL_FEEL_BORROW_URL = "api.php/Approval/feeRelation";// 自定义审批发布接口
    public static final String GAODE_LOACION_INDEX_BYDEPART = "api.php/Trajectory/getTrajectoryMap";// 员工定位首页
    public static final String GAODE_LOACION_INDEX_GETUSERTRA = "api.php/Trajectory/getUserTrajectory";// 员工轨迹的接口
    public static final String GAODE_LOACION_INDEX_CLEADENRDATERED = "api.php/Trajectory/getCalendar";// 员工轨迹日历红点获取
    public static final String MAIN_ONEKEY_CANCEL = "api.php/OneKey/index/";// 首界面一键消除接口
    public static final String GAODE_LOACION_INDEX_POST_LOCATION = "api.php/Trajectory/insert2";// 定位数据提交的接口
    public static final String CRM_CLIENT_OPTION = "api.php/Customer/option/";// 客户列表筛选条件
    public static final String CRM_CLIENT_LIST = "api.php/Customer/index/";// 客户列表
    public static final String CRM_CLIENT_DETAIL = "api.php/Customer/customerView/";// 客户详情
    public static final String CRM_CLIENT_OPTIONS = "api.php/Customer/getClassification/";// 客户分类级别行业
    public static final String CRM_CLIENT_ADD = "api.php/Customer/insert/";// 客户添加
    public static final String CRM_CLIENT_DISSCUSS_AGREE = "api.php/Visit/makePraise/";// 客户评论点赞
    public static final String CRM_CLIENT_DISSCUSS_OPPOSE = "api.php/Visit/makeDecline/";// 客户评论点衰

    /** Crm获取联系人接口地址 */
    public static final String CRM_GETDEPATERMENTALLPERSON = "api.php/AddressList/getCrmUsers";// 获取CRM部门人员列表
    public static final String CRM_GETPEROSONTOBUSSINESTREE = "api.php/Customer/getUser";// 获取负责人类似单选接口地址
    public static final String CRM_GETPEROSONTOBUSSINES = "api.php/Business/getAddClass";// 获取联系人接口地址
    public static final String CRM_BUSSINES_GETLIST = "api.php/Business/getlist";// 商机列表首页
    public static final String CRM_BUSSINES_HOMEINDEXANDDETAILS = "api.php/Business/view";// 商机详情和主页头部数据
    public static final String CRM_BUSSINES_HOMEINDEXANDDETAILSOFVISITOR = "api.php/Business/visit";// 商机详情和主页下拜访记录的详情
    public static final String CRM_BUSSINES_HOMEINDEXANDDETAILSOFCONTANS = "api.php/Customer/contacts";// 商机详情和主页下联系人的详情
    public static final String CRM_BUSSINES_HOMEINDEXANDDETAILSOFTRANTS = "api.php/Contract/getlist";// 商机详情和主页下合同的详情
    public static final String CRM_BUSSINES_HOMEINDEXANDDETAILSOPRODUCT = "api.php/Product/detail";// 商机详情和主页下产品的详情
    public static final String CRM_BUSSINES_HOMEBUSSINESEDIT = "api.php/Business/edit";// 商机详情下的权限转移的接口删除转移他人
    public static final String CRM_BUSSINES_HOMEBUSSGIVEUP = "api.php/Business/drop";// 商机详情下的放弃和启动商机
    public static final String CRM_BUSSINES_HOMEBUSSINESINDEX = "api.php/Business/view";// 商机详情
    public static final String CRM_BUSSINES_BUSSISINFOADD = "api.php/Business/add";// 商机详情
    public static final String CRM_GETVISTITORTOBUSSINES = "api.php/Visit/getAddClass";// 获取拜访方式列表
    public static final String CRM_VISTIORADDINFO = "api.php/Visit/add";// 添加拜访记录
    public static final String CRM_BUSSINESGETLISTSEARCH = "api.php/Visit/add";// 获取商机列表的筛选条件

    public static final String CRM_BUSSINES_OPTION = "api.php/Business/option";// 商机筛选
    public static final String CRM_HIGHSEAS_CLIENT = "api.php/Customer/customerView/";// 公海客户详情
    public static final String CRM_HIGHSEAS_GET_CLIENT = "api.php/Customer/pickup/";// 从公海中捡客户
    public static final String CRM_PRODUCT_MANAGEMENT_LIST = "api.php/Product/index/";// 产品管理
    public static final String CRM_PRODUCT_MANAGEMENT_ADDINFO = "api.php/Product/insert/";// 产品添加
    public static final String CRM_VISIT_RECORD_DISSCUSS = "api.php/Visit/addReply/";// 产拜访记录评论
    public static final String CRM_VISIT_RECORD_DETAIL = "api.php/Visit/view/";// 拜访记录详情
    public static final String CRM_VISIT_RECORD_LISTINDEX = "api.php/Visit/getlist";// 拜访记录列表
    public static final String CRM_VISIT_RECORD_DISSCUSS_LIST = "api.php/Visit/getComment/";// 拜访记录评论列表
    public static final String CRM_VISIT_SIGNGETDATA = "api.php/Visit/getSigninData";// 拜访记录评论列表
    public static final String CRM_VISIT_GETCUSTOMER = "api.php/Customer/getRelateCustomer";// 拜访记录添加获取客户
    public static final String CRM_CLIENT_ADD_CONTACTS = "api.php/Customer/contactAdd/";// 添加联系人
    public static final String CRM_CONTACT_LIST = "api.php/Customer/contactsList/";// 联系人列表
    public static final String CRM_CONTACT_PHONE_LIST = "api.php/Customer/batchImport/";// 通讯录联系人批量导入
    public static final String CRM_CONTACT_SHARE = "api.php/Customer/shareContacts/";// 共享联系人
    public static final String CRM_TRANDETRACNT_OPTION = "api.php/Contract/option";// 合同筛选
    public static final String CRM_APPROVAL_LIST = "api.php/Payment/index/";// crm审批列表
    public static final String CRM_APPROVAL_DETAIL = "api.php/Payment/view/";// crm审批详情
    public static final String CRM_APPROVAL = "api.php/Payment/examine/";// crm审批
    public static final String CRM_CONTACTS_DETAIL = "api.php/Customer/contactDetail/";// 联系人详情
    public static final String CRM_CONTACTS_FRAGMENT = "api.php/Customer/contactsList/";// 客户通讯录
    public static final String CRM_RECORDE_LIST = "api.php/Contract/paymentList";// 合同下的汇款记录列表
    public static final String CRM_BOSS_INDEX = "api.php/Crm/bossIndex/";// boss
                                                                         // crm首界面
    public static final String CRM_STATICTIS_INDEX = "api.php/Crm/dashboard";// boss
                                                                             // crm仪表盘首页
    public static final String CRM_RECORDE_EDIT = "api.php/Contract/edit";// 合同下转移给其他人
    public static final String CRM_RECORDE_DETAILS = "api.php/Contract/view";// 合同下详情
    public static final String CRM_TRADE_ADDRELATION = "api.php/Contract/addRelation";// 合同下添加相关人
    public static final String CRM_TRADE_EDIT = "api.php/Contract/edit";// 合同下变更负责人、改变合同状态（终止、结束）
    public static final String CRM_TRADE_DONGTAI = "api.php/Contract/affairs";// 合同动态
    public static final String CRM_RECORDE_PERSONCIINFO = "api.php/Contract/payment";// 回款记录获取其次和审批人
    public static final String CRM_RECORDE_ADDINFOMSG = "api.php/Contract/paymentAdd";// 回款记录获取其次和负责人
    public static final String CRM_SALEDETAIL = "api.php/Target/index";// 回款记录获取其次和负责人
    public static final String CRM_TRADECONTANT_PLAN_ADD = "api.php/Contract/planAdd";// 添加回款计划
    public static final String CRM_TRADECONTANT_APPROVAL = "api.php/Contract/contractExamine";// 添加回款计划
    public static final String CRM_TRADEMSGINFOADD = "api.php/Contract/contractAdd";// 合同添加
    public static final String CRM_APPROVAL_TRADECONTACT_LIST = "api.php/Contract/approvalList";// 合同审批列表
    public static final String CRM_CLIENT_ISEXIST = "api.php/Customer/checkDuplicate";// 查看客户是否存在
    public static final String CRM_CREATE_CLIENT_FROM_CONTACTS = "api.php/Customer/getUnrelatedContacts";// 从联系人创建客户
    public static final String CRM_CLIENT_TREND_LIST = "api.php/Customer/affairs";// 客户动向列表
    public static final String CRM_CLIENT_GIVEUP = "api.php/Customer/drop";// 客户放弃
    public static final String CRM_CLIENT_USER_SELET_ONE = "api.php/Customer/getUser";// 合同获取责任人
    public static final String CRM_FOLLOW_UPDATE = "api.php/Business/addFollow";// 商机提交的负责人联合跟进人和相关人
    public static final String CRM_CLIENT_CHARGE_USER = "api.php/Customer/getChargeUsers";// 客户已有的联合跟进人与负责人
    public static final String CRM_CLIENT_CHANGE_CHARGE_USER = "api.php/Customer/addFollow";// 客户变更负责人与跟进人
    public static final String CRM_BUSSINES_ZONE = "api.php/Business/affairs";// 商机动态
    public static final String CRM_CONTACTS_ADD_INFO = "api.php/Customer/getAddClass";// 添加联系人需要从接口获取的
    public static final String CRM_VISITOR_SIGNSUBMIT = "api.php/Visit/signin";// 外勤签到接口
    public static final String CRM_TRADECONTACT_TYPE = "api.php/Contract/getOption";// 合同类型
    public static final String CRM_SCHEDULE_TREND = "api.php/Schedule/message";// 合同类型
    public static final String CRM_STATIC_SALE_VALUE = "api.php/Crm/performance";// 仪表盘销售业绩统计
    public static final String CRM_STATISTICS_VISITOR = "api.php/Crm/visit";// 跟单统计
    public static final String APP_ZONECONTENT = "api.php/Message/home";// 动态
    public static final String CRM_STATIC_NEW_ADD_TRADE = "api.php/Crm/contract";// 仪表盘新增合同
    public static final String CRM_STATIC_NEW_ADD_CLIENT = "api.php/Crm/customer";// 仪表盘新增客户
    public static final String CRM_STATIC_NEW_ADD_BUSSINESS = "api.php/Crm/business";// 仪表盘新增商机
    public static final String NOTICE_SECOND_REMINDER = "api.php/Articles/againMessage";// 通知中再次提醒未读人员
    public static final String BOSS_STATISTICS_DOCUMENTARY = "api.php/BossStatistics/visit";// boss跟单分析饼子图
    public static final String BOSS_STATISTICS_DOCUMENTARY_CHIDREN = "api.php/BossStatistics/visitRank";// boss跟单排行
    public static final String BOSS_STATISTICS_INDEX = "api.php/BossStatistics/index";// boss统计首页
    public static final String BOSS_STATISTICS_INDEX_MENU = "api.php/BossStatistics/saveStatisticsConfig";// boss统计首页菜单配置
    public static final String BOSS_STATISTICS_ATTENDANCE = "api.php/BossStatistics/attendance";// boss考勤统计
    public static final String BOSS_STATISTICS_ATTENDANCE_ALL = "api.php/BossStatistics/attendanceMonth";// boss考勤统计
    public static final String BOSS_STATISTICS_ATTENDANCE_RANK = "api.php/BossStatistics/attendanceRanking";// boss考勤统计排行
    public static final String BOSS_STATISTICS_ATTENDANCE_DISTRIBUTED = "api.php/BossStatistics/attendancePie";// boss考勤统计分布
    public static final String BOSS_STATISTICS_EXPENSE_INDEX = "api.php/BossStatistics/fee";// boss费用统计首页折线图
    public static final String BOSS_STATISTICS_EXPENSE_INDEXE_ALL = "api.php/BossStatistics/feeMonth";// boss考勤统计
    public static final String BOSS_STATISTICS_EXPENSE_INDEXOPTIONS = "api.php/BossStatistics/feeType";// boss费用统计首页折线图类型筛选菜单
    public static final String BOSS_STATISTICS_EXPENSE_PIECHART_TABLEONE = "api.php/BossStatistics/feePie";// boss费用统计饼子图第一个选项卡的接口
    public static final String BOSS_STATISTICS_EXPENSE_COUML_TABLETWO = "api.php/BossStatistics/feeDep";// boss费用统计柱状第二个选项卡的接口

    public static final String BOSS_STATISTICS_CLIENT = "api.php/BossStatistics/customers";// boss客户分析
    public static final String BOSS_STATISTICS_HIGHSEAS_CLIENT = "api.php/BossStatistics/publicCustomer";// boss公海客户分析
    public static final String BOSS_STATISTICS_NEW_ADD_CLIENT = "api.php/BossStatistics/newCustomer";// boss新增客户分析
    public static final String BOSS_STATISTICS_TRADE = "api.php/BossStatistics/contract";// boss合同分析
    public static final String BOSS_STATISTICS_TRADE_SIGN_RANK = "api.php/BossStatistics/signRank";// boss合同签单排名
    public static final String BOSS_STATISTICS_BUSSINESS = "api.php/BossStatistics/business";// boss商机分析
    public static final String BOSS_STATISTICS_SALE_VALUE = "api.php/BossStatistics/performance";// boss销售业绩
    public static final String BOSS_STATISTICS_TASK_STATISTICS = "api.php/BossStatistics/taskIndex";// boss任务统计
    public static final String BOSS_STATISTICS_TASK_MONTH_STATISTICS = "api.php/BossStatistics/taskMonth";// boss获取某月任务统计
    public static final String BOSS_STATISTICS_LEAVE = "api.php/BossStatistics/askleave";// boss请假首页
    public static final String BOSS_STATISTICS_LEAVE_ALL = "api.php/BossStatistics/askleaveMonth";// boss请假统计某个月的
    public static final String BOSS_STATISTICS_LEAVE_DISTRIBUTED = "api.php/BossStatistics/askleavePie";// boss请假分布
    public static final String BOSS_STATISTICS_LEAVE_RANK = "api.php/BossStatistics/askleaveRanking";// boss请假排行
    public static final String BOSS_STATISTICS_SALES_FUNNEL = "api.php/BossStatistics/funnel";// boss销售漏斗
    public static final String BOSS_STATISTICS_CRMOPTIONSBYDP = "api.php/BossStatistics/getCrmDepartments";// crm部门筛选菜单
    public static final String UPLOAD_USER_IDEA_URL = "http://manager.beisheng.wang/api.php/WebServer/feedback"; // 提交用户反馈的建议
    public static final String BOSS_STATISTICS_OVERTIME = "api.php/BossStatistics/overtime";// boss加班统计首页
    public static final String BOSS_STATISTICS_OVERTIME_ALL = "api.php/BossStatistics/overtimeMonth";// boss加班统计某个月的
    public static final String BOSS_STATISTICS_OVERTIME_DISTRIBUTED = "api.php/BossStatistics/overtimePie";// boss加班统计分布
    public static final String BOSS_STATISTICS_OVERTIME_RANK = "api.php/BossStatistics/overtimeRanking";// boss加班统计排行
    public static final String JOURNAL_LIST_NEW = "api.php/Log/loglist/";// 日志列表
    public static final String JOURNAL_LIST_ITEM_AGREE = "api.php/Log/makePraise/";// 日志列表点赞
    public static final String ATTENTION_USERS = "api.php/Log/getFavorUsers/";// 关注用户
    public static final String ATTENTION_ADD_USERS = "api.php/Log/addFavor/ftoken/";// 添加关注用户
    public static final String HEAD_ICON_UPLAOD = "api.php/Headpic/uploads";// 头像上传
    public static final String JOURNAL_PUBLISH = "api.php/Log/updateLog_1_2_3";// 最新日志接口
    public static final String JOURNAL_PUBLISH_DETAIL = "api.php/Log/logDetail";// 最新日志详情
    public static final String JOURNAL_PRAISE = "api.php/Log/makePraise";// 最新日志详情点赞
    public static final String JOURNAL_Attention = "api.php/Log/favorUsers";// 最新日志详情关注
    public static final String JOURNAL_Calender = "api.php/Log/getUserDates";// 最新日志详情日历筛选
    public static final String JOURNAL_FOOTERPRINT = "api.php/OperationLog/operationLog";// 个人足迹
    public static final String JOURNAL_PERSON_DETAIL = "api.php/Log/getUserDates";// 个人日志详情
    public static final String LOCUS_WORKING_TRACK_LIST = "api.php/Visit/workingTrack";// 轨迹列表
    public static final String PERSONGETINFO = "api.php/Index/userCenter";// 普通员工个人接口
    public static final String BOSS_NEED_DO = "api.php/Index/gtasksList";// boss首页代办事项

    public static final String BOSS_NOTIFY = "api.php/Index/getReminds";// boss首页系统消息
    public static final String CRM_TRADECONTANT_REMIND = "api.php/Contract/remindAdd";// 合同提醒时间
    public static final String MESSAGE_LIST = "api.php/Message/unread/";// 首页消息点击后跳转的list列表
    public static final String SALE_PERSONINFO = "api.php/Archives/salesman/";// 业务员首页

    public static final String CRM_STATICS_SALE = "api.php/Crm/performance_1_2_5";// 业务员销售业绩统计
    public static final String CRM_STATICS_CLIENT = "api.php/Crm/customerAnalysis";// 业务员客户绩统计
    public static final String CRM_STATICS_BUESSNESS = "api.php/Crm/BusinessAnalysis";// 业务员商机绩统计
    public static final String CRM_STATICS_TRADE = "api.php/Crm/contractAnalysis";// 业务员合同分析
    public static final String CRM_STATICS_VISTED = "api.php/Crm/visitAnalysis";// 业务员跟单分析
    public static final String CRM_DEPARTMENT_URL = "api.php/Customer/getCrmDepartments";// 业务员跟单分析
    public static final String CONTACTS_LIKE = "api.php/AddressList/collecUids";// 通讯录收藏
    public static final String CONTACTS_DISLIKE = "api.php/AddressList/cancelCollecUids";// 通讯录取消收藏
    public static final String CONTACTS_INFO = "api.php/AddressList/getDepartUsers";// 通讯录不加token获取收藏的
    public static final String IMLOGIN = "/api.php/Ucpaas/IMlogin";// IM聊天登陆接口
    public static final String IMGETUSETINFO = "api.php/Ucpaas/getUserInfo";// IM消息流获取头像姓名
    public static final String MYATTENDEANCE = "api.php/Index/userCenter_1_2_6";// 126版本新增我的考勤
    public static final String APPROVAL_REMIND = "api.php/Approval/remind";// 2.0审批催办
    public static final String CRM_REMIND = "api.php/Contract/remind";// 2.0CRM催办
    public static final String APPLACTIONLIST_INDEX = "api.php/Modules/getModeles";// 应用商店列表

}
