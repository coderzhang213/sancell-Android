package cn.sancell.xingqiu.constant;


import cn.sancell.xingqiu.BuildConfig;

public class Constants {

    // true 各种log开启，RELEASE_MODE_ONLINE时要设置为false
    public static boolean DEBUG = BuildConfig.DEBUG;

    public static final int REQ_TIMEOUT = 35000;
    public static String APP_ID = "wxb42cb41315f88904";
    public static int MAX_IMAGE_SIZE = 6;
    public static String MIN_PROGRAM_NAME = "gh_31c814cae51d";


    public static class Link {
        public static String SERVER_ENV = BuildConfig.ENVIRONMENT;
        // 是否启用调试环境的URL，false是正式，或者预发发布环境,true是DEBUG 测试环境
        public static boolean IS_HOST_DEBUG;
        private final static String HOST_RELEASE_RDC = "http://devm.sanshaoxingqiu.cn/";//研发环境
        private final static String HOST_RELEASE = "https://mapi.sanshaoxingqiu.cn/";// 正式环境
        private final static String HOST_RELEASE_TEST = "http://testmapi1.sanshaoxingqiu.cn/";// 测试环境
        private final static String HOST_RELEASE_TEST_RDC = "http://testmapi.sancell.top/";// 测试环境
        private final static String HOST_PRE = "https://premapi.sanshaoxingqiu.cn/";//等保环境
        private final static String PRE_HOST_RELEASE = "https://mapi3.sanshaoxingqiu.cn/"; //预生产
        private final static String PRE_HOST_MIN = "https://alanmapi.sanshaoxingqiu.cn/"; //研发资金电脑
        //private final static String HOST_RELEASE_TEST = "http://testmapi.sancell.top/";// 测试环境


        public static String getHost() {
            return getServerHostByEnv(SERVER_ENV, HOST_RELEASE_TEST,
                    HOST_RELEASE, HOST_RELEASE_RDC, HOST_PRE, HOST_RELEASE_TEST_RDC, PRE_HOST_RELEASE, PRE_HOST_MIN);
        }

        public static String getServerHostByEnv(String env, String test,
                                                String off, String rdc, String pre, String rp, String pre_re, String min) {
            if ("test".equals(env)) {    //研发环境
                return test;
            }
            if ("rdc".equals(env)) {  //测试环境
                return rdc;
            }
            if ("pre".equals(env)) {  //等保环境
                return pre;
            }
            if ("rp".equals(env)) {
                return rp;
            }
            if ("pre_re".equals(env)) {
                return pre_re;
            }
            if ("min".equals(env)) {
                return min;
            }
            return off;             //正式环境
        }
    }


    public class Api {
        //初始化
        public static final String START_UP = "api/start-up";
        //验证码登录
        public static final String GET_CODE_LOGIN = "auth/mobile-code-login";
        //微信登录
        public static final String GET_WEIXIN_LOGIN = "auth/weixin-login";
        //账号密码登录
        public static final String GET_PWD_LOGIN = "auth/mobile-pass-login";
        //退出登录
        public static final String GET_OUT_LOGIN = "user/login-out";
        //短信验证码
        public static final String GET_CODE = "common/sms-code";
        //绑定手机号
        public static final String BIND_PHONE = "auth/bind-mobile";
        //更换手机号
        public static final String REPLACE_PHONE = "user/update-mobile";
        //检测验证码是否正确
        public static final String CHECK_CODE = "auth/check-change-password-sms-code";
        //修改登录密码
        public static final String MODIFY_PWD = "user/update-password";
        //重置密码
        public static final String RESET_PWD = "auth/change-password";
        //用户信息
        public static final String GET_USERINFO = "user/user-info";
        //用户订单数量信息
        public static final String GET_ORDERNUM = "user/order-num-data";
        //修改用户信息
        public static final String MODIFY_USERINFO = "user/update";
        //获取上传图片所需要参数
        public static final String UPLOAD_PHOTO = "user/up-image";
        //首页轮播图数据
        public static final String GET_BANNER_LIST = "ad/list";
        //首页精品推荐数据
        public static final String GET_RECOMM_LIST = "site/recomm-list";
        //首页热销排行数据
        public static final String GET_HOTSELL_LIST = "site/hot-sale";
        //猜你喜欢
        public static final String GET_LIKE_LIST = "site/guess-you-like";
        //一二三级分类数据
        public static final String GET_CLASSIFY = "goods-type/list";
        //三级分类商品列表数据
        public static final String GET_Third_CLASSIFY_PRODUCT_LIST = "goods/groupon-goods-list"; //
        //分类筛选商品特色分类数据
        public static final String GET_Third_CLASSIFY_SCREENING = "goods/tag-list";
        //购物车列表
        public static final String GET_SHOPPINGCAR_LIST = "car/list";
        //添加购物车
        public static final String GET_ADD_SHOPPING = "car/add-car-detail";
        //删除购物车
        public static final String GET_DELETE_SHOPPING = "car/delete";
        //批量删除购物车
        public static final String GET_BATCH_DELETE_SHOPPING = "car/batch-delete";
        //修改购物车数量
        public static final String GET_MODIFY_SHOPPING_NUM = "car/update-car-goods-num";
        //修改购物车选中状态
        public static final String GET_MODIFY_SHOPPING_SELECT_STATUS = "car/change-selected";
        //全选修改购物车选中状态
        public static final String GET_MODIFY_ALLSHOPPING_SELECT_STATUS = "car/change-all-selected";
        //清空库存不足商品
        public static final String CLEAR_UNDER_STOCK = "car/delete-under-stock";
        //清空失效商品
        public static final String CLEAR_INVALID = "car/delete-failure";
        //商品详情
        public static final String GET_PRODUCT_INFO = "goods/detail";
        //获取订单生成预确定信息（默认地址和红包）
        public static final String GET_DEFAULT_ORDER = "order/user-order-pre";
        //生成订单
        public static final String CREATE_ORDER = "order/create";
        //单个商品生成订单
        public static final String CREATE_SINGLE_PRODUCT_ORDER = "order/directly-create";
        //获取微信支付信息
        public static final String GET_WEIXIN_PAYINFO = "order/wechat-pre-order";
        //获取支付宝支付信息
        public static final String GET_ALIPAY_PAYINFO = "order/alipay-pre-order";
        //生成会员升级订单
        public static final String CREATE_MEMBERORDER = "member-level/create-order";
        //获取微信支付信息
        public static final String GET_MEMBER_WEIXIN_PAYINFO = "member-level/wechat-pre-order";
        //获取微信支付信息
        public static final String GET_MEMBER_ALIPAY_PAYINFO = "member-level/alipay-pre-order";
        //确认支付信息
        public static final String CHECK_PAYINFO = "order/confirm-pay";
        //商品列表
        public static final String GET_PRODUCT_LIST = "";
        //地址列表
        public static final String GET_ADDRESS_LIST = "address/list";
        //删除地址
        public static final String DELETE_ADDRESS = "address/delete";
        //省市区
        public static final String GET_AREA_LIST = "address/all-address-info";
        //新建地址
        public static final String New_ADDRESS = "address/publish";
        //修改地址
        public static final String Modify_ADDRESS = "address/update";
        //全部订单列表
        public static final String GET_ORDERALL_LIST = "order/list";
        //待付款订单列表
        public static final String GET_ORDERNOPAY_LIST = "order/list";
        //待发货订单列表
        public static final String GET_ORDERUNDELIVERED_LIST = "order/parcel-list";
        //已发货订单列表
        public static final String GET_ORDERDELIVERED_LIST = "order/parcel-list";
        //已完成订单列表
        public static final String GET_ORDEREFINISHED_LIST = "order/parcel-list";
        //取消订单
        public static final String CANCEL_ORDER = "order/cancel";
        //确认收货
        public static final String CONFIRM_RECEIPT = "order/confirm-receipt";
        //删除订单
        public static final String DELETE_ORDER = "order/delete";
        //再次购买
        public static final String BATCH_ADD_CAR = "car/batch-add";
        //订单详情
        public static final String GET_ORDERINFO = "order/detail";
        //包裹详情
        public static final String GET_PACKINFO = "order/parcel-detail";
        //订单取消原因
        public static final String GET_ORDERCANCELREASON = "order/reason-info";
        //物流信息
        public static final String GET_LOGISTICSINFO = "order/logistics-detail";

        //提交意见反馈
        public static final String COMMIT_FEED = "user/feedback";

        //提交评价文本信息
        public static final String COMMIT_EVALUATE_CONTENT = "evaluation/add";
        //获取上传评价图片所需要参数
        public static final String UPLOAD_PICS = "evaluation/pic-up-token-arr";

        //评论列表
        public static final String GET_EVALUATE_LIST = "evaluation/goods-evaluation-list";
        //待评价商品列表
        public static final String GET_EVALUATE_PRODUCT_LIST = "evaluation/wait-evaluate-list";


        //检测更换手机号中验证码是否正确
        public static final String CHECK_REPLACE_PHONE_CODE = "user/check-old-mobile-for-update-user-mobile";

        //添加历史搜索记录
        public static final String ADD_HISTROY = "search/add-history";
        //删除历史搜索记录
        public static final String DELETE_HISTROY = "search/delete-history";
        //历史搜索记录
        public static final String GET_HISTROYLIST = "search/user-history";
        //热门搜索
        public static final String GET_HOTKEYLIST = "search/hot-v3";
        //检测设置和忘记支付密码中验证码是否正确
        public static final String PAY_PWD_PHONE_CODE = "user/check-trade-password-sms-code";
        //创建支付密码
        public static final String CREATE_PAY_PWD = "user/set-trade-password";
        //创建支付密码
        public static final String FIND_PAY_PWD = "user/find-trade-password-by-sms-code";
        //验证旧支付密码
        public static final String CHECK_OLD_PAY_PWD = "user/check-trade-password";
        //修改支付密码
        public static final String MODIFY_PAY_PWD = "user/update-trade-password";
        //会员开通记录
        public static final String GET_MEMBER_RECORDS_LIST = "v2/member-level/member-vip-order-list";
        //会员中心界面权益详情
        public static final String GET_MEMBER_PRIVILEGE = "goods-gift/member-level-interests";
        //会员vip礼包信息
        public static final String GET_MEMBER_VIPGIGTFT_LIST = "goods-gift/index";
        //会员等级信息
        public static final String GET_MEMBER_LEVEL_LIST = "goods-gift/get-member-vip-info";
        //生成会员礼包购买订单
        public static final String CREATE_MEMBER_GIFT_ORDER = "v2/member-level/create";

        //邀请人信息
        public static final String GET_INVITER_INFO = "user/get-invite-user-info";
        //邀请人信息
        public static final String BIND_INVITER_ID = "user/bind-invite-user-id";

        //最新消息
        public static final String GET_NEWEST_MESSAGES = "notice/all-notice";

        //红包消息
        public static final String GET_MESSAGE_REDPACKET_LIST = "notice/point-notice-list";
        //消息通知
        public static final String GET_MESSAGE_NOTICE_LIST = "notice/list";
        //我的红包收支明细
        public static final String GET_USER_RED_PACKET_LIST = "point/list";
        //待结算红包收支明细
        public static final String GET_USER_RED_PACKET_SETTLED_LIST = "";

        //交易物流消息列表
        public static final String GET_MESSAGE_TRANSACTION_LOGISTICS_LIST = "notice/logistics-notice-list";

        //节省金额列表
        public static final String GET_MEMBER_SAVE_PRICE = "user/member-economy";

        //秒杀场次列表
        public static final String GET_SECKILLSESSIONLIST = "seckill-goods/round-list";
        //秒杀单个场次商品列表
        public static final String GET_SECKILLSESSIONPRODUCTLIST = "seckill-goods/list";
        //秒杀提醒
        public static final String GET_SECKILLREMIND = "seckill-goods/remind-seckill-goods";
        //补开发票
        public static final String OPEN_INVOICE = "order/repair-order-invoice";
        //首页新活动（金刚区,头条）
        public static final String GET_ACTIVEINFO = "site/activity";
        //首页新活动（秒杀）
        public static final String GET_SECKILLINFO = "site/to-day-seckill-data";
        //限时礼包列表
        public static final String GET_LIMIT_TIME_GIFT_LIST = "seckill-gift/index";
        //会员权益
        public static final String GET_MEMBER_PRIVILEGE_LIST = "goods-gift/get-vip-interests-info";

        //物流列表数据
        public static final String GET_LOGISTICS_LIST = "order/logistics-list";

        //一元购列表头部相关信息数据
        public static final String GET_ONE_YUAN_TOP_INFO = "one-shop/list";
        //一元购列表
        public static final String GET_ONE_YUAN_List = "one-shop/get-list";

        //邀请好友记录
        public static final String GET_InviteFriends_List = "user/get-user-binding-info";

        //社群推荐群组
        public static final String GET_COMMUNITY_RECOMM_GROUP_LIST = "community/v1/group/rec-home";

        //社群首页视频列表
        public static final String GET_COMMUNITY_VIDEO_LIST = "community/v1/video/list";
        //推荐群组列表
        public static final String GET_RECOMM_GROUP_LIST = "community/v1/group/rec-list";
        //群组创建
        public static final String IM_CREATE_TEAM = "community/v1/group/create";
        //发送红包
        public static final String IM_SEND_RP = "community/v1/red/created";
        //抢红包
        public static final String IM_RP_GRAB = "community/v1/red/grab";

        //红包详情
        public static final String IM_RP_DETAIL = "community/v1/red/detail";

        //红包有效性检查
        public static final String IM_CHECK_RP_VAILABLE = "community/v1/red/check";

        //检查是否设置了交易密码
        public static final String IM_CHECK_PAY_PASS = "community/v1/red/account-password";

        //获取我的群组
        public static final String IM_GET_MY_TEAM = "community/v1/group/my-groups";

        //视频关联群组或商品
        public static final String IM_GET_VIDEO_RELATION = "community/v1/video/relations-list";

        public static final String IM_JOIN_GROUP = "community/v1/group/join-group";

        public static final String IM_RP_YU_E = "community/v1/red/account";

        public static final String IM_VIDEO_DETAIL = "community/v1/video/detail";

        public static final String IM_LOGIN = "community/v1/group/create-account";
        //获取直播关联商品
        public static final String LIVE_GET_COMMINT = "community/v1/live/relations-list";

        public static final String IM_LIVING_LIST = "community/v1/live/list";

        public static final String IM_VIDEO_LIST = "community/v1/video/list";
        /**
         * 获取直播房间信息
         */
        public static final String LIVE_ROOM_INFO = "/community/v1/live/detail";

        public static final String GET_MODIFY_ADDRESS = "address/edit-address";

        public static final String GET_ORDER_LIST = "order/new-parcel-list";

        public static final String GET_ACTIVITY_CONFIG = "site/activity-config";
        /**
         * 记录用户最后一次选择地址
         */
        public static final String GET_CHOOSE_ADDRESS = "address/choose-address";
        /**
         * 获取收货地址，可以传商品ID，或者包裹ID。返回当前地址是否可以送达
         */
        public static final String GET_ADDRESS_REGION = "address/region-list";

        //商品支持配送区域
        public static final String GET_GOODS_AREA = "goods/goods-region";

        //修改群名
        public static final String IM_TEAM_SAVE_NAME = "/community/v1/group/group-name-filter";

        /**
         * 获取直播人数
         */
        public static final String GET_LIVE_SUMS = "/community/v1/live/online-user";
        /**
         * 获取首页九宫格
         */
        public static final String GET_HOME_MENU = "module-index/module-list";
        /**
         * 获取商城数据
         */
        public static final String GET_MALL_LIST = "module-index/module-sub-list";
        /**
         * 获取商品详情列表
         */
        public static final String GET_COM_INFP = "module-index/module-goods-list";
        /**
         * 获取拼团订单
         */
        public static final String GET_GIGHT_ODER_LIST = "/groupon/order-list";


        //团购下单
        public static final String CREATE_PIN_ORDER = " groupon/deal";

        public static final String CREATE_ORDER_V2 = "v2/groupon/deal";

        public static final String PIN_WECHAT_PAY = "groupon-pay/wechat-pre-order";

        public static final String PIN_ALIPAY = "groupon-pay/alipay-pre-order";

        public static final String PIN_ORDER_DETAIL = "groupon/order-detail";

        public static final String PIN_DETAIL_INFO = "groupon/groupon-detail";

        public static final String PIN_CANCEL_ORDER = "groupon/order-cancel";

        public static final String PIN_INVITE_USER = "groupon/invite";

        //是否加入别的群
        public static final String IM_CHECK_IN_OTHRER_TEAM = "community/v1/group/check-user-in-group";
        public static final String TEST_API = "test/test10/test9";
        //直播登录
        public static final String LIVE_LOGIN = "/community/v1/live/login";
        //提交直播状态
        public static final String UP_ROOM_STATES = "/community/v2/live/edit-status";

        //直播推荐列表
        public static final String LIVE_V1_LIST = "/community/v1/live/get-rec-list";
        //获取直播详情
        public static final String GET_LIVE_INF = "/community/v1/live/get-live-batch";
        /**
         * 直播关注
         */
        public static final String LIVE_UP_FOLLOW = "/community/v1/live/follow";
        /**
         * 直播预约
         */
        public static final String LIVE_RESER = "/community/v1/live/reserve";

        //直播点赞
        public static final String LIVE_GIVE_LINK = "/community/v1/live/like";

        //打赏
        public static final String LIVE_READ_LINE = "/community/v1/live/reward";

        //获取直播关注列表
        public static final String LIVE_FOLLEW_LIST = "/community/v1/live/home-live-list";
        //获取直播推荐
        public static final String LIVEW_RECOMMEND_LIST = "/community/v1/live/head-rec-list";
        /**
         * 直播间设置初始化
         */
        public static final String LIVE_SET_INIT = "/community/v2/live/setting-init";

        //添加商品或群组
        public static final String ADD_REMM_AND_GROUP = "/community/v1/live/add-relation";

        //删除直播群组或商品
        public static final String DELETE_REMM_AND_GROPU = "/community/v1/live/del-relation";

        //修改商品别名
        public static final String UP_REMMODIT_NAME = "/community/v1/live/relation-alias";

        //图片上传接口
        public static final String IMG_UP_LOAD = " /community/v1/live/upload";

        //直播设置保存
        public static final String LIVE_SET_SAVE = "/community/v2/live/setting";
        //直播热门搜索
        public static final String LIVE_HOT_SEARCH = "/community/v1/live/hot-search";
        //直播历史搜索
        public static final String LIVE_HISTORY_SEARCH = "/community/v1/live/search-history";
        //删除历史记录
        public static final String LIVE_HISTORY_CLEAR = "/community/v1/live/clear-history";
        //直播搜索
        public static final String LIVE_SEARCH_WORD = " /community/v1/live/search";
        //主播认证
        public static final String LIVER_VERIFY = "/community/v1/live/tv-user-verify";
        //身份证图片上传
        public static final String LIVER_CARD_UP = " /community/v1/live/upload";
        //认证审核结果
        public static final String LIVER_VERIFY_RESULT = "/community/v1/live/tv-user-verify-status";
        //用户页背景修改上传
        public static final String UP_USER_INFO_BG = "/community/v1/live/background-img-upload";
        //用户简介修改
        public static final String MODIFY_USER_INTRO = "/community/v1/live/edit-intro";
        //直播预约
        public static final String LIVE_APPOINTMENT = "/community/v1/live/reserve";
        //人员基本信息
        public static final String LIVE_USER_INFO = "/community/v1/live/user-center-info";
        //用户关注列表
        public static final String LIVE_USER_FOLLOW = "/community/v1/live/follow-user-list";
        //用户粉丝列表
        public static final String LIVE_USER_FANS = "/community/v1/live/fans-user-list";
        //用户主页直播列表
        public static final String LIVE_USER_LIVE_LIST = "/community/v1/live/user-center-live-list";
        //用户消息列表
        public static final String USER_MSG_LIST = "/community/v1/private-msg/msg-list";

        public static final String USER_MSG_RECORD = "/community/v1/private-msg/msg-record";

        public static final String USER_SEND_MSG = "/community/v1/private-msg/send";

        public static final String USER_PROFILE = "community/v1/live/income-list";

        public static final String USER_LETTER_RED = "/community/v1/private-msg/have-read";

        //检查是否有直播中的
        public static final String CHECK_LIVE_STATUS = "/community/v1/live/has-unfinished";

        public static final String LIKE_VIDEO = "/community/v1/live/user-like-live-list";

        //离开直播间
        public static final String GO_ANAY_LIVE = "community/v1/live/leave-live";

        //复用上一场信息
        public static final String UO_LAST_DATA = "community/v1/live/setting-use-relations";
        /**
         * 获取直播间状态
         */
        public static final String CHECK_LIVE_ROOM_STATUS = "/community/v1/live/query-batch-status";

        //订单关联的代金券
        public static final String VOUCHER_ASSOCIATE_GOOD = "/coupon/can-use-list";

        public static final String VOUCHER_CENTER = "coupon/user-owned-list";

        //获取直播间优惠券
        public static final String LIVE_ROOM_COUPONS = "coupon/get-live-coupons-list";

        //领取优惠券
        public static final String GET_LIVE_ROOM_COUPONS = "/coupon/receive";
        //增加库存
        public static final String ADD_STOCK = "shop-test/stock";

        public static final String GET_RECOMMEND_LIVER = "/shop-test/rec-broadcaster";

        public static final String GET_USER_MEMBER = "user/member-level";

        //添加银行卡
        public static final String ADD_BANK = "user/bind-bank-card";

        //上传身份证图片
        public static final String UP_USER_IMG = "/user/upload-pri";

        //用户体现
        public static final String USER_WITHRAW = "/user/withdraw-cash";

        //收益信息
        public static final String USER_INCOME = "/user/income-detail";
        //推荐主播
        public static final String RECOMMENDED_ANCHOR = "community/v1/live/rec-broadcaster";

    }

    public class Key {
        public static final String KEY_USERINFO = "key_userinfo";
        public static final String KEY_MEMBERLEVEL = "key_memberLevel";  //暂时无用
        public static final String KEY_SKEY = "key_skey";
        public static final String KEY_address = "key_address";
        public static final String KEY_mobileRule = "key_mobileRule";
        public static final String KEY_passwordRule = "key_passwordRule";
        public static final String KEY_orderId = "key_orderId";
        public static final String KEY_orderTag = "key_orderTag";
        public static final String KEY_orderParceId = "key_orderParceId";
        public static final String KEY_orderMemberLevel = "key_orderMemberLevel";
        public static final String KEY_weixinCode = "key_weixinCode";
        public static final String KEY_isOneYuanPurchase = "key_isOneYuanPurchase"; //是否是一元购订单（1：是  0：不是）

        public static final String KEY_isSecondGuide = "key_isSecondGuide";


        public static final String KEY_memberAgreementUrlData = "key_memberAgreementUrlData";
        public static final String KEY_memberAgreementUrl = "key_memberAgreementUrl";
        public static final String KEY_userAgreementUrl = "key_userAgreementUrl";
        public static final String KEY_privacyAgreementUrl = "key_privacyAgreementUrl";
        public static final String KEY_integralRightsUrl = "key_integralRightsUrl";

        public static final String KEY_NAVIGATIONINFO = "key_navigationInfo";

        public static final String KEY_searchKeyWord = "key_searchKeyWord";
        public static final String KEY_seckillStatus = "key_seckillStatus";

        public static final String KEY_isLinkApp = "key_isLinApp";

        public static final String HOSPITAL_INFO = "hospital_info";

        public static final String KEY_1 = "key1";
        public static final String KEY_2 = "key2";
        public static final String KEY_3 = "key3";
        public static final String KEY_4 = "key4";
        public static final String KEY_5 = "key5";
        public static final String KEY_6 = "key6";
        public static final String KEY_7 = "key7";

        //im相关
        public static final String key_im_accid = "key_im_accid";
        public static final String key_im_token = "key_im_token";
        public static final String key_im_user_name = "key_im_user_name";
        //是否弹出隐私提示
        public static final String IS_SHOW_PRIVATE = "is_show_private";
    }

    public class Url {
        public static final String URL_RedRules = "https://m.sanshaoxingqiu.cn/app-membership-interests/red_envelope_rules.html";  //红包规则
        public static final String URL_Silver_Privilege = "https://m.sanshaoxingqiu.cn/app-membership-interests/silver_interest_detail.html";
        public static final String URL_Golden_Privilege = "https://m.sanshaoxingqiu.cn/app-membership-interests/golden_interest_detail.html";
        public static final String URL_Red_Privilege = "https://m.sanshaoxingqiu.cn/app-membership-interests/red_interest_detail.html";
        public static final String URL_Normal_Privilege = "https://m.sanshaoxingqiu.cn/app-membership-interests/ordinary_interest_detail.html";
        public static final String URL_Red_Paket_Privilege = "https://m.sanshaoxingqiu.cn/newsRedBag/redbag.html";  //首页猩球红包
        public static final String URL_One_Yuan_Purchase_Rules = "https://m.sanshaoxingqiu.cn/app-membership-interests/purchase_rules.html";//一元购规则
       public static final String  URL_FANS_PROTOCOL = "https://m.sanshaoxingqiu.cn/sancell-shop-app-hybrid/memberCenter/FansAgreement.html";//芙蓉粉丝会

    }

    public class OrderShowStatus {
        public static final int KEY_nopay = 1;  //未支付
        public static final int KEY_finished = 2;  //已完成但未全部评价完
        public static final int KEY_evaluated = 7;//已完成且全部评价完
        public static final int KEY_undelivered = 3;    //  未发货
        public static final int KEY_delivered = 4;  //已发货
        public static final int KEY_closed = 5;  //订单关闭（可能是未支付失效，用户取消）
        public static final int KEY_processing = 6;  //订单处理中
        public static final int KEY_RECEIVE = 8;  //已签收
    }


}
