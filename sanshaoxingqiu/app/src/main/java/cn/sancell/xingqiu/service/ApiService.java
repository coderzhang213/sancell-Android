package cn.sancell.xingqiu.service;


import java.util.HashMap;
import java.util.Map;

import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.bean.AddressInfo;
import cn.sancell.xingqiu.bean.LiveBaseStatusInfo;
import cn.sancell.xingqiu.bean.OrderVoucherRes;
import cn.sancell.xingqiu.bean.VerifyResultRes;
import cn.sancell.xingqiu.bean.VoucherCenterRes;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.bean.ClassifyScreeningDataBean;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifyFirstDataBean;
import cn.sancell.xingqiu.homeclassify.bean.HomeClassifySecondDataBean;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.homeclassify.bean.PublishEvaluateContentBean;
import cn.sancell.xingqiu.homeclassify.bean.UpLoadPicListBean;
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean;
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean;
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData;
import cn.sancell.xingqiu.homepage.bean.HomeActiveInfoBean;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageHotSellDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.HomePageRecommDataBean;
import cn.sancell.xingqiu.homepage.bean.HomepageSeckillInfoBean;
import cn.sancell.xingqiu.homepage.bean.OneYuanPurchaseBean;
import cn.sancell.xingqiu.homepage.bean.OneYuanPurchaseListBean;
import cn.sancell.xingqiu.homepage.bean.SearchKeyListBean;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionListBean;
import cn.sancell.xingqiu.homepage.bean.SeckillSessionProductListBean;
import cn.sancell.xingqiu.homepage.bean.SerchBaseData;
import cn.sancell.xingqiu.homeshoppingcar.bean.AlipayPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.CreateOrderDefaultPreBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.WeiXinPayInfoBean;
import cn.sancell.xingqiu.homeshoppingcar.bean.res.GoodAreaRes;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressRegInfo;
import cn.sancell.xingqiu.homeuser.bean.EvaluatedProductListDataBean;
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean;
import cn.sancell.xingqiu.homeuser.bean.LogisticsInfoBean;
import cn.sancell.xingqiu.homeuser.bean.LogisticsListBean;
import cn.sancell.xingqiu.homeuser.bean.MessageNoticeListBean;
import cn.sancell.xingqiu.homeuser.bean.MessageRedPacketListBean;
import cn.sancell.xingqiu.homeuser.bean.MessageTransactionLogisticsListBean;
import cn.sancell.xingqiu.homeuser.bean.MessagesBean;
import cn.sancell.xingqiu.homeuser.bean.OderInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderAllListDataBean;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.homeuser.bean.PackInfoDataBean;
import cn.sancell.xingqiu.homeuser.bean.ReplacePhoneBean;
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean;
import cn.sancell.xingqiu.homeuser.bean.UserOrderNumBean;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketListBean;
import cn.sancell.xingqiu.homeuser.bean.UserRedPacketSettledListBean;
import cn.sancell.xingqiu.homeuser.bean.res.ActivityConfigRes;
import cn.sancell.xingqiu.im.entity.LiveCountInfo;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.entity.res.PayPassRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;
import cn.sancell.xingqiu.im.entity.res.SendRpRes;
import cn.sancell.xingqiu.im.entity.res.TeamCreateRes;
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.im.entity.res.YueRes;
import cn.sancell.xingqiu.login.bean.BindPhoneDataBean;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.login.bean.UserLoginDataBean;
import cn.sancell.xingqiu.order.entity.res.OrderRes;
import cn.sancell.xingqiu.order.entity.res.PinDetailRes;
import cn.sancell.xingqiu.order.entity.res.PinInviteRes;
import cn.sancell.xingqiu.order.entity.res.PinOrderPackRes;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;
import cn.sancell.xingqiu.usermember.bean.InviterBean;
import cn.sancell.xingqiu.usermember.bean.MemberLevelListBean;
import cn.sancell.xingqiu.usermember.bean.MemberLimitTimeGiftListBean;
import cn.sancell.xingqiu.usermember.bean.MemberOpenRecordsListBean;
import cn.sancell.xingqiu.usermember.bean.MemberPrivilegeListBean;
import cn.sancell.xingqiu.usermember.bean.MemberSavePriceListBean;
import cn.sancell.xingqiu.usermember.bean.MemberVIPPrivilegeBean;
import cn.sancell.xingqiu.usermember.bean.MemberVipGiftBean;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiService {


    /**
     * appstart
     */
    @FormUrlEncoded
    @POST(Constants.Api.START_UP)
    Observable<BaseEntry<StartupDataBean>>
    startUp(@FieldMap Map<String, String> maps);


    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_CODE_LOGIN)
    Observable<BaseEntry<UserLoginDataBean>>
    userCodeLogin(@FieldMap Map<String, String> maps);

    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_WEIXIN_LOGIN)
    Observable<BaseEntry<UserLoginDataBean>>
    userWeiXinLogin(@FieldMap Map<String, String> maps);

    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_PWD_LOGIN)
    Observable<BaseEntry<UserLoginDataBean>>
    userPwdLogin(@FieldMap Map<String, String> maps);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_OUT_LOGIN)
    Observable<BaseEntry>
    outLogin(@FieldMap HashMap<String, String> maps);

    /**
     * 获取短信验证码
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_CODE)
    Observable<BaseEntry>
    getCode(@FieldMap Map<String, String> maps);

    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST(Constants.Api.BIND_PHONE)
    Observable<BaseEntry<BindPhoneDataBean>>
    bindPhone(@FieldMap Map<String, String> maps);

    /**
     * 检测重置密码中短信验证码是否正确
     */
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_CODE)
    Observable<BaseEntry>
    checkCode(@FieldMap Map<String, String> maps);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.RESET_PWD)
    Observable<BaseEntry>
    resetPwd(@FieldMap Map<String, String> maps);


    /**
     * 用户信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_USERINFO)
    Observable<BaseEntry<UserBean>>
    getUserInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 用户订单数量信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERNUM)
    Observable<BaseEntry<UserOrderNumBean>>
    getUserOrderNum(@FieldMap HashMap<String, String> maps);

    /**
     * 修改用户信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.MODIFY_USERINFO)
    Observable<BaseEntry>
    modifyUserInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 获取上传图片的参数信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.UPLOAD_PHOTO)
    Observable<BaseEntry<UpLoadPhotoInfoBean>>
    getUpLoadPhotoInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 首页精品推荐数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_RECOMM_LIST)
    Observable<BaseEntry<HomePageRecommDataBean>>
    getHomeRecommData(@FieldMap Map<String, String> maps);

    /**
     * 首页热销排行数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_HOTSELL_LIST)
    Observable<BaseEntry<HomePageHotSellDataBean>>
    getHomeHotSellData(@FieldMap Map<String, String> maps);

    /**
     * 首页轮播图数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_BANNER_LIST)
    Observable<BaseEntry<HomeBannerDataBean>>
    getHomeBannerData(@FieldMap Map<String, String> maps);


    /**
     * 猜你喜欢
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIKE_LIST)
    Observable<BaseEntry<HomePageLikeListDataBean>>
    getLikeListData(@FieldMap HashMap<String, String> maps);

    /**
     * 一级分类数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_CLASSIFY)
    Observable<BaseEntry<HomeClassifyFirstDataBean>>
    getClassifyData(@FieldMap HashMap<String, String> maps);

    /**
     * 二三级分类数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_CLASSIFY)
    Observable<BaseEntry<HomeClassifySecondDataBean>>
    getSecondThirdClassifyData(@FieldMap HashMap<String, String> maps);


    /**
     * 三级分类商品列表数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_Third_CLASSIFY_PRODUCT_LIST)
    Observable<BaseEntry<HomePageLikeListDataBean.LikeListDataBean>>
    getThirdClassifyListData(@FieldMap HashMap<String, String> maps);

    /**
     * 分类筛选中商品特色分类数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_Third_CLASSIFY_SCREENING)
    Observable<BaseEntry<ClassifyScreeningDataBean>>
    getThirdClassifyScreeningData(@FieldMap HashMap<String, String> maps);

    /**
     * 购物车列表数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_SHOPPINGCAR_LIST)
    Observable<BaseEntry<HomeShoppingCarDataBean>>
    getShoppingCarListData(@FieldMap HashMap<String, String> maps);

    /**
     * 添加购物车
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ADD_SHOPPING)
    Observable<BaseEntry>
    addShoppingCar(@FieldMap HashMap<String, String> maps);

    /**
     * 删除购物车
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_DELETE_SHOPPING)
    Observable<BaseEntry>
    deleteShoppingCar(@FieldMap HashMap<String, String> maps);

    /**
     * 批量删除购物车
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_BATCH_DELETE_SHOPPING)
    Observable<BaseEntry>
    batchDeleteShoppingCar(@FieldMap HashMap<String, String> maps);

    /**
     * 修改购物车数量
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MODIFY_SHOPPING_NUM)
    Observable<BaseEntry>
    modifyShoppingCarNum(@FieldMap HashMap<String, String> maps);

    /**
     * 修改购物车选择状态
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MODIFY_SHOPPING_SELECT_STATUS)
    Observable<BaseEntry>
    modifyShoppingCarSelectStatus(@FieldMap HashMap<String, String> maps);

    /**
     * 全选修改购物车选择状态
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MODIFY_ALLSHOPPING_SELECT_STATUS)
    Observable<BaseEntry>
    modifyAllShoppingCarSelectStatus(@FieldMap HashMap<String, String> maps);

    /**
     * 清空库存不足商品
     */
    @FormUrlEncoded
    @POST(Constants.Api.CLEAR_UNDER_STOCK)
    Observable<BaseEntry>
    clearUnderStock(@FieldMap HashMap<String, String> maps);

    /**
     * 清空失效商品
     */
    @FormUrlEncoded
    @POST(Constants.Api.CLEAR_INVALID)
    Observable<BaseEntry>
    clearInvalid(@FieldMap HashMap<String, String> maps);

    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_PRODUCT_INFO)
    Observable<BaseEntry<ProductInfoDataBean>>
    getProductInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_PRODUCT_INFO)
    Observable<BaseEntry<ProductInfoDataBean>> getGoodsDetail(@FieldMap HashMap<String, String> maps);

    /**
     * 获取订单生成预确定信息（默认地址和红包）
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_DEFAULT_ORDER)
    Observable<BaseEntry<CreateOrderDefaultPreBean>>
    getDefualtOrderPre(@FieldMap HashMap<String, String> maps);

    /**
     * 生成订单
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_ORDER)
    Observable<BaseEntry<CreateOrderBean>>
    createOrder(@FieldMap HashMap<String, Object> maps);

    /**
     * 单个商品生成订单
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_SINGLE_PRODUCT_ORDER)
    Observable<BaseEntry<CreateOrderBean>>
    createSingleProductOrder(@FieldMap HashMap<String, String> maps);


    /**
     * 获取微信支付信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_WEIXIN_PAYINFO)
    Observable<BaseEntry<WeiXinPayInfoBean>>
    getWeiXinPayInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 获取支付宝支付信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ALIPAY_PAYINFO)
    Observable<BaseEntry<AlipayPayInfoBean>>
    getAlipayPayInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 获取会员升级微信支付信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_WEIXIN_PAYINFO)
    Observable<BaseEntry<WeiXinPayInfoBean>>
    getMemberWeiXinPayInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 获取会员升级支付宝支付信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_ALIPAY_PAYINFO)
    Observable<BaseEntry<AlipayPayInfoBean>>
    getMemberAlipayPayInfo(@FieldMap HashMap<String, String> maps);


    /**
     * 确认支付信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_PAYINFO)
    Observable<BaseEntry>
    checkPayInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 商品列表
     */
    @POST(Constants.Api.GET_PRODUCT_LIST)
    Observable<BaseEntry<HomePageLikeListDataBean>>
    getProductList(@Body HashMap<String, String> maps);

    /**
     * 我的地址列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ADDRESS_LIST)
    Observable<BaseEntry<AddressListDataBean>>
    getAddressList(@FieldMap HashMap<String, String> maps);

    /**
     * 删除地址
     */
    @FormUrlEncoded
    @POST(Constants.Api.DELETE_ADDRESS)
    Observable<BaseEntry>
    deleteAddress(@FieldMap HashMap<String, String> maps);

    /**
     * 省市区列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_AREA_LIST)
    Observable<BaseEntry>
    getAreaList(@FieldMap HashMap<String, String> maps);

    /**
     * 新建地址
     */
    @FormUrlEncoded
    @POST(Constants.Api.New_ADDRESS)
    Observable<BaseEntry<AddressInfo>>
    newAddress(@FieldMap HashMap<String, String> maps);

    /**
     * 新建地址
     */
    @FormUrlEncoded
    @POST(Constants.Api.Modify_ADDRESS)
    Observable<BaseEntry>
    modifyAddress(@FieldMap HashMap<String, String> maps);

    /**
     * 全部列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERALL_LIST)
    Observable<BaseEntry<OrderAllListDataBean>>
    getOrderAllList(@FieldMap HashMap<String, String> maps);

    /**
     * 待付款列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERNOPAY_LIST)
    Observable<BaseEntry<OrderAllListDataBean>>
    getOrderNoPayList(@FieldMap HashMap<String, String> maps);

    /**
     * 待发货列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERUNDELIVERED_LIST)
    Observable<BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean>>
    getOrderUndeliveredList(@FieldMap HashMap<String, String> maps);

    /**
     * 已发货列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERDELIVERED_LIST)
    Observable<BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean>>
    getOrderDeliveredList(@FieldMap HashMap<String, String> maps);

    /**
     * 已发货列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDEREFINISHED_LIST)
    Observable<BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean>>
    getOrderFinishedList(@FieldMap HashMap<String, String> maps);

    /**
     * 取消订单
     */
    @FormUrlEncoded
    @POST(Constants.Api.CANCEL_ORDER)
    Observable<BaseEntry>
    cancelOrder(@FieldMap HashMap<String, String> maps);

    /**
     * 确认收货
     */
    @FormUrlEncoded
    @POST(Constants.Api.CONFIRM_RECEIPT)
    Observable<BaseEntry>
    confirmReceipt(@FieldMap HashMap<String, String> maps);

    /**
     * 删除订单
     */
    @FormUrlEncoded
    @POST(Constants.Api.DELETE_ORDER)
    Observable<BaseEntry>
    deleteOrder(@FieldMap HashMap<String, String> maps);

    /**
     * 再次购买
     */
    @FormUrlEncoded
    @POST(Constants.Api.BATCH_ADD_CAR)
    Observable<BaseEntry>
    batchAddCar(@FieldMap HashMap<String, String> maps);

    /**
     * 订单详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERINFO)
    Observable<BaseEntry<OderInfoDataBean>>
    getOrderInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 包裹详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_PACKINFO)
    Observable<BaseEntry<PackInfoDataBean>>
    getPackInfo(@FieldMap HashMap<String, String> maps);


    /**
     * 订单取消原因
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDERCANCELREASON)
    Observable<BaseEntry<OrderCancelReasonListBean>>
    getOrderCancelReason(@FieldMap HashMap<String, String> maps);

    /**
     * 物流信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LOGISTICSINFO)
    Observable<BaseEntry<LogisticsInfoBean>>
    getLogisticsInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 提交意见反馈
     */
    @FormUrlEncoded
    @POST(Constants.Api.COMMIT_FEED)
    Observable<BaseEntry>
    commitFeed(@FieldMap HashMap<String, String> maps);

    /**
     * 提交评价文本信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.COMMIT_EVALUATE_CONTENT)
    Observable<BaseEntry<PublishEvaluateContentBean>>
    publishEvaluateContent(@FieldMap HashMap<String, String> maps);

    /**
     * 评论列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_EVALUATE_LIST)
    Observable<BaseEntry<EvaluateListDataBean>>
    getEvaluateListData(@FieldMap HashMap<String, String> maps);

    /**
     * 待评价商品列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_EVALUATE_PRODUCT_LIST)
    Observable<BaseEntry<EvaluatedProductListDataBean>>
    getEvaluateProductListData(@FieldMap Map<String, String> maps);

    /**
     * 检测更换手机中短信验证码是否正确
     */
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_REPLACE_PHONE_CODE)
    Observable<BaseEntry>
    checkReplacePhoneCode(@FieldMap Map<String, String> maps);

    /**
     * 更换手机号
     */
    @FormUrlEncoded
    @POST(Constants.Api.REPLACE_PHONE)
    Observable<BaseEntry<ReplacePhoneBean>>
    replacePhone(@FieldMap Map<String, String> maps);

    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.MODIFY_PWD)
    Observable<BaseEntry>
    modifyLoginPwd(@FieldMap Map<String, String> maps);

    /**
     * 获取上传评价图片的参数信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.UPLOAD_PICS)
    Observable<BaseEntry<UpLoadPicListBean>>
    getUpLoadPicsInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 添加历史搜索记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.ADD_HISTROY)
    Observable<BaseEntry>
    addHistroy(@FieldMap Map<String, String> maps);

    /**
     * 删除历史搜索记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.DELETE_HISTROY)
    Observable<BaseEntry>
    deleteHistroy(@FieldMap Map<String, String> maps);

    /**
     * 历史搜索记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_HISTROYLIST)
    Observable<BaseEntry<SearchKeyListBean>>
    getHistroyList(@FieldMap Map<String, String> maps);

    /**
     * 热门搜索
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_HOTKEYLIST)
    Observable<BaseEntry<SerchBaseData>>
    getHotKeyList(@FieldMap Map<String, String> maps);

    /**
     * 检测设置和忘记支付密码中短信验证码是否正确
     */
    @FormUrlEncoded
    @POST(Constants.Api.PAY_PWD_PHONE_CODE)
    Observable<BaseEntry>
    checkPayPwdPhoneCode(@FieldMap Map<String, String> maps);

    /**
     * 创建支付密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_PAY_PWD)
    Observable<BaseEntry>
    createPayPwd(@FieldMap Map<String, String> maps);

    /**
     * 忘记找回支付密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.FIND_PAY_PWD)
    Observable<BaseEntry>
    findPayPwd(@FieldMap Map<String, String> maps);

    /**
     * 验证旧支付密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_OLD_PAY_PWD)
    Observable<BaseEntry>
    checkOldPayPwd(@FieldMap Map<String, String> maps);

    /**
     * 修改支付密码
     */
    @FormUrlEncoded
    @POST(Constants.Api.MODIFY_PAY_PWD)
    Observable<BaseEntry>
    modifyPayPwd(@FieldMap Map<String, String> maps);

    /**
     * 会员开通记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_RECORDS_LIST)
    Observable<BaseEntry<MemberOpenRecordsListBean>>
    getMemberRecordsList(@FieldMap HashMap<String, String> maps);

    /**
     * 会员权益详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_PRIVILEGE)
    Observable<BaseEntry<MemberPrivilegeListBean>>
    getMemberPrivilege(@FieldMap HashMap<String, String> maps);

    /**
     * 会员vip礼包信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_VIPGIGTFT_LIST)
    Observable<BaseEntry<MemberVipGiftBean>>
    getMemberVipGiftList(@FieldMap HashMap<String, String> maps);

    /**
     * 会员等级信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_LEVEL_LIST)
    Observable<BaseEntry<MemberLevelListBean>>
    getMemberLevelList(@FieldMap HashMap<String, String> maps);

    /**
     * 生成购买会员礼包订单
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_MEMBER_GIFT_ORDER)
    Observable<BaseEntry<CreateOrderBean>>
    createMemberGiftOrder(@FieldMap HashMap<String, String> maps);

    /**
     * 邀请人信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_INVITER_INFO)
    Observable<BaseEntry<InviterBean>>
    getInviterInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 绑定邀请人id
     */
    @FormUrlEncoded
    @POST(Constants.Api.BIND_INVITER_ID)
    Observable<BaseEntry>
    bindnviterId(@FieldMap HashMap<String, String> maps);

    /**
     * 绑定邀请人id
     */
    @FormUrlEncoded
    @POST(Constants.Api.BIND_INVITER_ID)
    Observable<BaseEntry<EmptyBean>>
    bindnViterId(@FieldMap HashMap<String, String> maps);

    /**
     * 最新消息
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_NEWEST_MESSAGES)
    Observable<BaseEntry<MessagesBean>>
    getNewestMessagesData(@FieldMap Map<String, String> maps);

    /**
     * 红包消息列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MESSAGE_REDPACKET_LIST)
    Observable<BaseEntry<MessageRedPacketListBean>>
    getMessageRedPacketListData(@FieldMap Map<String, String> maps);

    /**
     * 消息通知列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MESSAGE_NOTICE_LIST)
    Observable<BaseEntry<MessageNoticeListBean>>
    getMessageNoticeListData(@FieldMap Map<String, String> maps);

    /**
     * 我的红包收支明细列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_USER_RED_PACKET_LIST)
    Observable<BaseEntry<UserRedPacketListBean>>
    getUserRedPacketListData(@FieldMap Map<String, String> maps);

    /**
     * 待结算红包收支明细列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_USER_RED_PACKET_SETTLED_LIST)
    Observable<BaseEntry<UserRedPacketSettledListBean>>
    getUserRedPacketSettledListData(@FieldMap Map<String, String> maps);

    /**
     * 交易物流消息列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MESSAGE_TRANSACTION_LOGISTICS_LIST)
    Observable<BaseEntry<MessageTransactionLogisticsListBean>>
    getMessageTransactionLogisticsListData(@FieldMap Map<String, String> maps);

    /**
     * 节省金额列表数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_SAVE_PRICE)
    Observable<BaseEntry<MemberSavePriceListBean>>
    getMemberPSavePriceList(@FieldMap HashMap<String, String> maps);

    /**
     * 秒杀场次列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_SECKILLSESSIONLIST)
    Observable<BaseEntry<SeckillSessionListBean>>
    getSeckillSessionList(@FieldMap Map<String, String> maps);

    /**
     * 单个秒杀场次商品列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_SECKILLSESSIONPRODUCTLIST)
    Observable<BaseEntry<SeckillSessionProductListBean>>
    getSeckillSessionProductList(@FieldMap Map<String, String> maps);

    /**
     * 秒杀提醒
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_SECKILLREMIND)
    Observable<BaseEntry>
    getSeckillRemind(@FieldMap Map<String, String> maps);

    /**
     * 补开发票
     */
    @FormUrlEncoded
    @POST(Constants.Api.OPEN_INVOICE)
    Observable<BaseEntry>
    invoiceOpen(@FieldMap HashMap<String, String> maps);

    /**
     * 首页活动模块(金刚区，秒杀，头条)
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ACTIVEINFO)
    Observable<BaseEntry<HomeActiveInfoBean>>
    getHomeActiveInfoData(@FieldMap Map<String, String> maps);

    /**
     * 限时礼包列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIMIT_TIME_GIFT_LIST)
    Observable<BaseEntry<MemberLimitTimeGiftListBean>>
    getLimitTimeGiftListData(@FieldMap HashMap<String, String> maps);

    /**
     * 会员权益
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MEMBER_PRIVILEGE_LIST)
    Observable<BaseEntry<MemberVIPPrivilegeBean>>
    getPrivilegeListData(@FieldMap HashMap<String, String> maps);


    /**
     * 首页活动模块(秒杀)
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_SECKILLINFO)
    Observable<BaseEntry<HomepageSeckillInfoBean>>
    getHomeSeckillInfoData(@FieldMap Map<String, String> maps);

    /**
     * 物流列表数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LOGISTICS_LIST)
    Observable<BaseEntry<LogisticsListBean>>
    getLogisticsListData(@FieldMap HashMap<String, String> maps);

    /**
     * 一元购列表头部相关信息数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ONE_YUAN_TOP_INFO)
    Observable<BaseEntry<OneYuanPurchaseBean>>
    getOneYuanPurchaseTopInfoData(@FieldMap Map<String, String> maps);

    /**
     * 一元购列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ONE_YUAN_List)
    Observable<BaseEntry<OneYuanPurchaseListBean>>
    getOneYuanPurchaseListData(@FieldMap Map<String, String> maps);

    /**
     * 邀请好友记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_InviteFriends_List)
    Observable<BaseEntry<InviteFriendsListBean>>
    getInviteFriendsListData(@FieldMap HashMap<String, String> maps);

    /**
     * 社群首页推荐群组
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_COMMUNITY_RECOMM_GROUP_LIST)
    Observable<BaseEntry<RecommendGroupListBean>>
    getCommunityRecommGroupListData(@FieldMap Map<String, String> maps);

    /**
     * 社群视频列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_COMMUNITY_VIDEO_LIST)
    Observable<BaseEntry<CommunityVideoListBean>>
    getCommunityVideoListData(@FieldMap Map<String, String> maps);

    /**
     * 推荐群组列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_RECOMM_GROUP_LIST)
    Observable<BaseEntry<RecommendGroupListBean>>
    getRecommGroupListData(@FieldMap Map<String, String> maps);

    /**
     * 创建群组
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_CREATE_TEAM)
    Observable<BaseEntry<TeamCreateRes>> createTeam(@FieldMap Map<String, String> params);

    /**
     * 发送红包
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_SEND_RP)
    Observable<BaseEntry<SendRpRes>> sendRp(@FieldMap Map<String, String> params);

    /**
     * 抢红包
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_RP_GRAB)
    Observable<BaseEntry<RpGrabRes>> rpGrab(@FieldMap Map<String, String> maps);

    /**
     * 红包详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_RP_DETAIL)
    Observable<BaseEntry<RpDetailRes>> getRpDetail(@FieldMap Map<String, String> params);

    /**
     * 检查红包是否有效
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_CHECK_RP_VAILABLE)
    Observable<BaseEntry<CheckRpRes>> checkRp(@FieldMap Map<String, String> params);

    /**
     * 支付密码检查
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_CHECK_PAY_PASS)
    Observable<BaseEntry<PayPassRes>> checkPayPass(@FieldMap Map<String, String> params);

    /**
     * 我的群组
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_GET_MY_TEAM)
    Observable<BaseEntry<AddressRes>> getMyTeam(@FieldMap Map<String, String> params);

    /**
     * 视频关联的群组或商品
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_GET_VIDEO_RELATION)
    Observable<BaseEntry<VideoRelationRes>> getVideoRelation(@FieldMap Map<String, String> params);

    /**
     * 加入购物车
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ADD_SHOPPING)
    Observable<BaseEntry> addCart(@FieldMap Map<String, String> params);

    /**
     * 加入群聊
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_JOIN_GROUP)
    Observable<BaseEntry<EmptyBean>> joinTeam(@FieldMap Map<String, String> params);

    /**
     * 红包余额
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_RP_YU_E)
    Observable<BaseEntry<YueRes>> getRpYue(@FieldMap Map<String, String> params);

    /**
     * 视频详情
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_VIDEO_DETAIL)
    Observable<BaseEntry<VideoDetailRes>> getVidoDetail(@FieldMap Map<String, String> params);

    /**
     * im 登陆
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_LOGIN)
    Observable<BaseEntry<ImAccountRes>> loginIm(@FieldMap Map<String, String> params);

    /**
     * 直播列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_LIVING_LIST)
    Observable<BaseEntry<LiveData>> getLiveList(@FieldMap Map<String, String> params);

    /**
     * 视频列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_VIDEO_LIST)
    Observable<BaseEntry<CommunityVideoListBean>> getVideoList(@FieldMap Map<String, String> params);

    /**
     * 获取直播关联商品
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_GET_COMMINT)
    Observable<BaseEntry<VideoRelationRes>> getLiveCommentList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Constants.Api.LIVE_ROOM_INFO)
    Observable<BaseEntry<String>> getLiveRoomInfo(@FieldMap Map<String, String> params);

    /**
     * 修改地址
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MODIFY_ADDRESS)
    Observable<BaseEntry<EmptyBean>> getModifyAddress(@FieldMap HashMap<String, String> maps);

    /**
     * 获取首页活动
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ACTIVITY_CONFIG)
    Observable<BaseEntry<ActivityConfigRes>> getActivityConfig(@FieldMap HashMap<String, String> maps);

    /**
     * 记录用户最后一次选择地址
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_CHOOSE_ADDRESS)
    Observable<BaseEntry<EmptyBean>> chooseAddress(@FieldMap HashMap<String, String > maps);

    /**
     * 记录用户最后一次选择地址
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ADDRESS_REGION)
    Observable<BaseEntry<AddressRegInfo>> getAddressReggList(@FieldMap HashMap<String, String > maps);

    /**
     * 商品支持配送区域
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_GOODS_AREA)
    Observable<BaseEntry<GoodAreaRes>> getGoodsArea(@FieldMap HashMap<String, String> maps);

    /**
     * 修改群名称
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_TEAM_SAVE_NAME)
    Observable<BaseEntry<EmptyBean>> saveTeamName(@FieldMap HashMap<String, String> maps);

    /**
     * 检查是否在其他群
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_CHECK_IN_OTHRER_TEAM)
    Observable<BaseEntry<EmptyBean>> checkInOtherTeam(@FieldMap HashMap<String, String> maps);

    /**
     * 获取直播人数
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIVE_SUMS)
    Observable<BaseEntry<LiveCountInfo>> getLiveSums(@FieldMap HashMap<String, String> maps);

    /**
     * 创建拼团订单
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_PIN_ORDER)
    Observable<BaseEntry<OrderRes>> createPinOrder(@FieldMap HashMap<String, String> maps);

    /**
     * 创建拼团订单
     *
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.CREATE_ORDER_V2)
    Observable<BaseEntry<OrderRes>> createPinNewOrder(@FieldMap HashMap<String, String> maps);

    /**
     * 拼团调起微信支付
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.PIN_WECHAT_PAY)
    Observable<BaseEntry<WeiXinPayInfoBean>> getPinWeChatPayInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 拼团调起支付宝支付
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.PIN_ALIPAY)
    Observable<BaseEntry<AlipayPayInfoBean>> getPinAliPayInfo(@FieldMap HashMap<String, String> maps);

    @FormUrlEncoded
    @POST(Constants.Api.PIN_ORDER_DETAIL)
    Observable<BaseEntry<PinOrderPackRes>> getPinOrderDetail(@FieldMap HashMap<String, String> maps);

    @FormUrlEncoded
    @POST(Constants.Api.PIN_DETAIL_INFO)
    Observable<BaseEntry<PinDetailRes>> getPinDetailInfo(@FieldMap HashMap<String, String> maps);

    /**
     * 取消订单
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.PIN_CANCEL_ORDER)
    Observable<BaseEntry<EmptyBean>> pinOrderCancel(@FieldMap HashMap<String, String> maps);

    /**
     * 邀请好友
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.PIN_INVITE_USER)
    Observable<BaseEntry<PinInviteRes>> pinInviteUser(@FieldMap HashMap<String, String> maps);

    /**
     * 订单列表
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ORDER_LIST)
    Observable<BaseEntry<OrderAllListDataBean.OrderAllBean.OrderPackListBean>> getOrderList(@FieldMap HashMap<String, String> maps);

    /**
     * 直播认证
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVER_VERIFY_RESULT)
    Observable<BaseEntry<VerifyResultRes>> getLiveVerifyResult(@FieldMap Map<String,String> params);

    /**
     * 直播检查
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_LIVE_STATUS)
    Observable<BaseEntry<LiveBaseStatusInfo>> checkLive(@FieldMap Map<String,String> params);

    /**
     * 代金券
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.VOUCHER_ASSOCIATE_GOOD)
    Observable<BaseEntry<OrderVoucherRes>> getOrderVoucher(@FieldMap Map<String,String> params);

    /**
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.VOUCHER_CENTER)
    Observable<BaseEntry<VoucherCenterRes>> getVoucherCenter(@FieldMap Map<String,String> params);


    /**
     * 会员信息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_USER_MEMBER)
    Observable<BaseEntry<UserMemberRes>> getUserMember(@FieldMap Map<String,String> params);
}
