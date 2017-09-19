package com.beisheng.synews.constant;

public class Constant {
	public static final String RESULT_SUCCESS_CODE = "200";
	public static final String RESULT_FAIL_CODE400 = "400";
	public static final String DOMAIN_NAME = "http://syrb.test.beisheng.wang/";// 接口前缀域名
	// public static final String DOMAIN_NAME =
	// "http://app.site.10yan.com.cn/";// 接口前缀域名
	public static final String LIFE_FRAGMENT_URL = "index.php?s=/Home/Life/index/";// 生活接口
	public static final String LIFE_FRAGMENT_URL1 = "index.php?s=/Home/Life/indexv1/";// 生活接口
	public static final String LIFE_FRAGMENT_URL2 = "http://www.hbyoo.com/api/toutiao_life_api.php";// 生活接口

	public static final String LIVE_FRAGMENT_URL = "index.php?s=/Home/Liveplay/index/";// 直播列表接口
	public static final String LIVE_DETAIL_URL = "index.php?s=/Home/Liveplay/getLive/";// 直播详情
	public static final String KEY_WORD_ULR = "index.php?s=/Home/Search/getKeywords/";// 收索接口
	public static final String KEY_WORD_RESULT_ULR = "index.php?s=/Home/Search/index/";// 收索结果
	public static final String CHANNEL_CATEGORY_ULR = "http://app.site.10yan.com.cn/index.php?s=/Home/Category/index";// 栏目分类
	public static final String HOME_NEWS_URL = "index.php?s=/Home/News/index/";// 首页新闻消息列表
	public static final String COMMUNITY_URL = "index.php?s=/Home/Forum/index/";// 社区首页
	public static final String COMMUNITY_DETAIL_URL = "index.php?s=/Home/Forum/detail/";// 社区首页的列表详情
	public static final String COMMUNITY_COMMENT_URL = "http://111.47.111.248/topicpost.php?type=reply";// 回帖
	public static final String COMMUNITY_ADD_URL = "http://111.47.111.248/topicpost.php?type=post";// 发帖
	public static final String COMMUNITY_SEARCH = "index.php?s=/Home/Forum/search";// 社区收索
	public static final String LOGO_URL = "index.php?s=/Home/Index/index/";// 启动的广告
	public static final String NEWS_DETAIL_URL = "index.php?s=/Home/News/detail/";// 新闻详情
	public static final String SPECIAL_TOPIC_URL = "index.php?s=/Home/Special/index/";// 专题首页
	public static final String SPECIAL_TOPIC_TYPE_URL = "index.php?s=/Home/Special/getList/";// 专题分类列表
	public static final String REGISTER_URL = "index.php?s=/Home/Login/register/";// 注册接口
	public static final String LOGIN_URL = "index.php?s=/Home/Login/signIn/";// 登录接口
	public static final String LIVE_ADD_THREME_URL = "index.php?s=/Home/Liveplay/setLive/";// 添加直播间
	public static final String LIVE_ADD_URL = "index.php?s=/Home/Liveplay/setLiveContent/";// 添加直播间详情
	public static final String DISSCUSS_URL = "index.php?s=/Home/Article/artReply/";// 评论
	public static final String VIEW_DISSCUSS_URL = "index.php?s=/Home/Article/index/";// 查看更多评论
	public static final String CHANNEL_SORT_URL = "index.php?s=/Home/Category/setCate/";// 栏目排序
	public static final String SHOP_URL = "index.php?s=/Home/Shop/index/";// 积分商成列表
	public static final String SHOP_DETAIL_URL = "index.php?s=/Home/Shop/detail/";// 商品详情
	public static final String SHOP_RECORD_URL = "index.php?s=/Home/Shop/scoreList/";// 积分记录
	public static final String SHOP_REDEEM_URL = "index.php?s=/Home/Shop/setScore";// 积分兑换
	public static final String SHOP_ADD_URL = "index.php?s=/Home/User/index";// 积分累加
	public static final String IDEA_FEEDBACK_URL = "index.php?s=/Home/Feed/back/";// 意见反馈
	public static final String ABOUT_US_URL = "index.php?s=/Home/Article/detail/id/13";// 关于我们
	public static final String COPYRIGHT_STATEMENT_URL = "index.php?s=/Home/Article/detail/id/12";// 版权申明
	public static final String PRAISE_URL = "index.php?s=/Home/Article/praise/";// 点赞接口;
	public static final String LOCATION_URL = "index.php?s=/Home/Category/city";// 切换城市
	public static final String DISSCUSS_MYSELF = "index.php?s=/Home/Mine/replyList";// 我的评论
	public static final String MYSELF_URL = "index.php?s=/Home/Mine/index";// 我的页面接口
	public static final String MYSELF_MESSAGE_URL = "index.php?s=/Home/Index/getMessage";// 我的页面消息
	public static final String UPDATE_PASSWORD_URL = "index.php?s=/Home/Login/modifypw/";// 修改密码
	public static final String CHECK_UPDATE_URL = "http://app.10yan.com.cn/appapi/qcw_version.php";// 检查更新
	public static final String CHECK_OTHER_LOGIN_URL = "index.php?s=/Home/Login/quickLogin";// 检查第三方帐号是否登录过
	public static final String UPLAOD_HEAD_ICON_URL = "index.php?s=/Home/Login/headpic";// 更换头像
	public static final String COMMUNITY_ITEM = "index.php?s=/Home/Forum/cate";// 社区栏目
	public static final String ZHENG_WU_ITEM = "index.php?s=/Home/Special/zw_index";// 政务列表
	public static final String BROKE_LIST = "index.php?s=/Home/Broken/index";// 爆料列表
	public static final String BROKE_ADD = "index.php?s=/Home/Broken/addBroken";// 爆料添加
	public static final String INVITAT_CODE_COMMIT = "index.php?s=/Home/Mine/inputCode";// 提交邀请码
	public static final String INVITAT_CODE_MY = "index.php?s=/Home/Mine/getCode";// 我的邀请码
	public static final String INVITAT_CODE_IS_INPUT = "index.php?s=/Home/Mine/checkInputCode";// 是否输入过邀请码
	public static final String INVITAT_CODE_LIST = "index.php?s=/Home/Mine/getCodeList";// 获取邀请列表
	public static final String USER_INFO_URL = "index.php?s=/Home/User/userInfo";// 个人信息
	public static final String USER_INFO_MODIFY = "index.php?s=/Home/Login/modifyNickname";// 修改个人信息
	public static final String USER_INFO_GET = "index.php?s=/Home/User/userInfo";// 获取个人信息
	public static final String MY_DISSCUSS_DELETE = "index.php?s=/Home/Article/delReply";// 删除我的评论
	public static final String BROKE_NOTE_URL = "index.php?s=/Home/Article/detail";// 删除我的评论
	public static final String REDEEM_RECORDS_URL = "index.php?s=/Home/Shop/getOrder/";// 商品兑换记录
	public static final String POINTS_RULE_URL = "index.php?s=/Home/Article/detail";// 积分规则
	public static final String RIBAO_URL = "http://sywb.10yan.com/appapi/time.asp?";// 十堰日报

}
