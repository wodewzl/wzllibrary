package com.xiaojing.shop.constant;

import cz.msebera.android.httpclient.protocol.HTTP;

public class Constant {

    public static final String FIRSTID = "firstid";
    public static final String LASTID = "lastid";
    public static final String ENCODING = HTTP.UTF_8;
    public static final String SDCARD_CACHE = "com.xiaojing/files/"; // 文件sdk缓存
    //get 最后加 &
    public static final String LOGIN_URL = "c=login&a=index";// 登录接口
    public static final String GET_CODE = "c=connect&a=get_sms_captcha";// 获取验证码
    public static final String REGIST_URL = "c=connect&a=sms_register";// 注册
    public static final String CATEGORY_URL = "c=goods_class&a=index&";// 商品分类
    public static final String GET_ADDRESS_URL = "c=member_address&a=address_list&";// 获取地址
    public static final String GET_CITY_URL = "c=index&a=get_area_list&";// 获取城市列表
    public static final String GET_CITY_ALL_URL = "c=index&a=_get_area_list&";// 获取城市列表每级带有全部
    public static final String ADD_ADDRESS_URL = "c=member_address&a=address_add";// 添加地址
    public static final String DELETE_ADDRESS_URL = "c=member_address&a=address_del&";// 删除地址
    public static final String EDIT_ADDRESS_URL = "c=member_address&a=address_edit";// 编辑地址
    public static final String DEFALULT_ADDRESS_URL = "c=member_address&a=set_default&";//收货地址设置默认
    public static final String KEYWORD_URL = "c=index&a=search_key_list&";//关键字
    public static final String SHOP_LIST_URL = "c=goods&a=goods_list&";//商品列表
    public static final String HOME_URL = "c=index&a=index&";//首页界面
    public static final String HISTORY_SHOP_URL = "c=member_goodsbrowse&a=browse_list&";//我的足迹
    public static final String FAVORITES_SFHOP_URL = "c=member_favorites&a=favorites_list&";//我的收藏
    public static final String MY_URL = "c=member_index&a=index&";//个人中心
    public static final String MY_OVER_URL = "c=member_fund&a=predepositlog&";//我的余额
    public static final String SHOP_CAT_URL = "c=member_cart&a=cart_list&";//购物车
    public static final String COMMIT_SHOP_CAT_URL = "c=member_cart&a=update_all_cart";//购物车
    public static final String ORDER_URL = "c=member_order&a=order_list&";//订单列表
    public static final String CANCEL_ORDER_URL = "c=member_order&a=order_cancel&";//取消列表
    public static final String ORDER_SURE_URL = "c=member_buy&a=buy_step1&";//确认订单
    public static final String ORDER_PAY_URL = "c=member_buy&a=buy_step2";//支付
    public static final String ONE_SHOP_ORDER_SURE_URL = "c=member_buy&a=one_dollar_buy_step1&";//一元购确认订单
    public static final String ONE_SHOP_ORDER_PAY_URL = "c=member_buy&a=one_dollar_buy_step2";//支付
    public static final String NERBY_SHOP_URL = "c=merchant&a=get_list&";//附近商城
    public static final String OPTION_URL = "c=merchant&a=filter_options&";//附近商城条件
    public static final String NERBY_SHOP_DETAIL_URL = "c=merchant&a=detail&";//附近商城详情
    public static final String JING_BI_ULR_URL = "c=member_fund&a=goldlog&";//鲸币
    public static final String JING_DOU_ULR_URL = "c=member_fund&a=beanlog&";//鲸豆
    public static final String ONE_SHOP_URL = "c=one_dollar&a=index&";//一元购订单
    public static final String ONE_SHOP_DETAIL_URL = "c=one_dollar&a=detail&";//一元购详情
    public static final String ONE_SHOP_LOTTERYING_URL = "c=one_dollar&a=get_list&";//揭晓中，已揭晓
    public static final String ONE_SHOP_HISTORY_LOTTERY_URL = "c=one_dollar&a=history_publish_list&";//往期揭晓
    public static final String ONE_SHOP_ORDER_URL = "c=member_order&a=one_dollar_order_list&";//往期揭晓
    public static final String ONE_SHOP_SHOW_LIST_URL = "c=one_dollar&a=share_list&";//一元购晒单列表
    public static final String ONE_SHOP_SHOW_URL = "c=member_evaluate&a=one_dollar_share&";//晒单
    public static final String SHOP_PROMOTIONS_URL = "c=article&a=article_list&ac_id=1&";//商家活动列表
    public static final String SHOP_RECIVER_URL = "c=member_order&a=order_receive&";//确认收货
    public static final String ORDER_DELETE_URL = "c=member_order&a=order_recycle&";//删除订单
    public static final String SHOP_DELIVER_URL = "c=member_order&a=get_current_deliver&";//查看物流
    public static final String UPLOAD_HEAD_URL = "c=member_account&a=app_save_avatar";//上传头像
    public static final String UPDATE_NICK_URL = "c=member_account&a=save_nickname";//上传头像
    public static final String SHOP_BACK_URL = "c=member_refund&a=get_refund_list&";//订单列表
    public static final String ONE_SHOP_LIST_PAY_URL = "c=member_buy&a=one_dollar_pay_from_list";//一元购订单列表去支付
    public static final String SHOP_LIST_PAY_URL = "c=member_buy&a=pay_from_list";//单列表去支付
    public static final String SHOP_BACK_ACTION_TWO_URL = "c=member_return&a=delay_post";//退款 延迟
    public static final String PAY_TYPE_URL = "c=payment&a=payment_list";//支付方式
    public static final String RECHARGE_URL = "c=member_buy&a=recharge";//充值
    public static final String BLANK_TYPE_URL = "c=index&a=get_bank_list";//银行卡类型
    public static final String ADD_BLANK_URL = "c=member_bank_card&a=card_add";//添加银行卡
    public static final String MY_BLANK_URL = "c=member_bank_card&a=card_list&";//我的银行卡
    public static final String CHECK_PAY_PASSWROD_URL = "c=member_buy&a=check_password";//校验支付密码
    public static final String WITHDRAW_URL = "c=member_fund&a=pd_cash_add";//提现
    public static final String GAME_LIST_URL = "c=games&a=get_list";//游戏列表
    public static final String GAME_DETAIL_URL = "c=games&a=detail&";//游戏详情
    public static final String GAME_ORDER_URL = "c=member_buy&a=game_buy_step1&";//游戏支付1
    public static final String GAME_ORDER_PAY_URL = "c=member_buy&a=game_buy_step2";//游戏支付2
    public static final String CHECK_PAY_PASSWORD_URL = "c=member_index&a=member_state&";//检测是否有支付密码
    public static final String GAME_ORDER_LIST_URL = "c=member_order&a=game_order_list&";//游戏订单
    public static final String GAME_ORDER_LIST_PAY_URL = "c=member_buy&a=game_pay_from_list";//游戏订单列表去支付
    public static final String ONE_SHOP_DILIVER_URL = "c=member_order&a=get_one_dollar_deliver&";//一元购订单物流
    public static final String APPROVE_NAME_URL = "c=member_account&a=apply_authentic";//实名认证
    public static final String SHOP_TYPE_URL = "c=merchant&a=get_class_list";//商铺经营类型
    public static final String SHOP_JOIN_URL = "c=member_account&a=apply_merchant";//申请加盟
    public static final String PRIVILEGE_URL = "c=member_index&a=welfare&";//会员特权
    public static final String PRIVILEGE_SIGIN_URL = "c=member_signin&a=signin_add";//会员特权敲到
    public static final String CHECK_UPDATE_URL = "c=index&a=get_version";//自动更新
}
