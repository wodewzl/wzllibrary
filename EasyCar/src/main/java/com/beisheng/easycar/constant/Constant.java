package com.beisheng.easycar.constant;

import com.wuzhanglong.library.constant.BaseConstant;

public class Constant extends BaseConstant{

    public static final String SDCARD_CACHE = "com.beisheng.easycar/files/"; // 文件sdk缓存
	public static final String DOMAIN_NAME = "http://zuche.test.beisheng.wang/index.php/";// 租车域名域名
    //get 最后加 ?
    public static final String REGIST_URL = "Api/Public/register.html";//注册接口
    public static final String GET_CODE = "Api/Public/sendMsg.html?";// 获取验证码
    public static final String LOGIN_URL = "Api/Public/login.html";//登录接口
    public static final String HOME_URL = "Api/Index/index.html?";//主页界面
    public static final String USER_CAR_URL = "Api/Index/detail.html?";//用车详细接口
    public static final String USER_INFO_URL = "Api/User/userinfo.html";//用车详细接口
    public static final String APPROVE_NAME_URL = "Api/User/upload.html";//实名认证
    public static final String USER_CAR_LIST_URL = "Api/Index/nearest.html?";//用车列表
    public static final String NEARBY_LIST_URL = "Api/Index/shop.html?";//附近列表
    public static final String MY_ROUTE_URL = "Api/User/myTrip.html?";//我的行程
    public static final String MY_COUPON_URL = "Api/User/myTrip.html?";//我的优惠劵
    public static final String MY_MONEY_URL = "Api/User/myWallet.html?";//我的钱包
    public static final String MY_INVOICE_COMMIT_URL = "Api/User/invoice.html";//发票提交
    public static final String HELP_URL = "Api/News/help.html";//发票提交
    public static final String BACK_CAR_URL = "Api/User/endCar.html?";//确认还车
    public static final String USE_CAR_URL = "Api/User/useCar.html";//确认还车
    public static final String RECHARGE_URL = "Api/User/recharge.html";//充值


    public static final String WAIT_CAR_URL = "Api/User/waitCar.html";//等待用车
    public static final String START_USER_CAR_URL = "Api/User/startCar.html";//开始用车
    public static final String COST_CAR_URL = "Api/User/costCar.html?";//用车计费
    public static final String SURE_BACK_CAR_URL = "Api/User/costCarSubmit.html";//确认还车
    public static final String PROMOTIONS_URL = "Api/News/activity.html";//活动
}
