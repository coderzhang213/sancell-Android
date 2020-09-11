package cn.sancell.xingqiu.service

import cn.sancell.xingqiu.base.entity.EmptyBean
import cn.sancell.xingqiu.bean.*
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData
import cn.sancell.xingqiu.homepage.bean.*
import cn.sancell.xingqiu.homeuser.bean.InviteFriendsListBean
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean
import cn.sancell.xingqiu.im.entity.LiveCountInfo
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes
import cn.sancell.xingqiu.im.entity.res.YueRes
import cn.sancell.xingqiu.order.entity.res.PinInviteRes
import cn.sancell.xingqiu.usermember.bean.UserMemberRes
import handbank.hbwallet.ResResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by zj on 2019/12/20.
 */
interface Api {

    @FormUrlEncoded
    @POST(Constants.Api.IM_VIDEO_DETAIL)
    suspend fun getVidoDetailKt(@FieldMap params: Map<String, String>): ResResponse<VideoDetailRes>

    /**
     * 直播列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_LIVING_LIST)
    suspend fun getLiveList(@FieldMap params: Map<String, String>): ResResponse<LiveData>

    /**
     * 视频列表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_VIDEO_LIST)
    suspend fun getVideoList(@FieldMap params: Map<String, String>): ResResponse<CommunityVideoListBean>

    /**
     * 视频关联的群组或商品
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.IM_GET_VIDEO_RELATION)
    suspend fun getVideoRelation(@FieldMap params: Map<String, String>): ResResponse<VideoRelationRes>

    /**
     * 加入购物车
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ADD_SHOPPING)
    suspend fun addCart(@FieldMap params: Map<String, String>): ResResponse<Any>

    /**
     * 首页轮播图数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_BANNER_LIST)
    suspend fun getHomeBannerData(@FieldMap maps: Map<String, String>): ResResponse<HomeBannerDataBean>

    /**
     * 首页活动模块(金刚区，秒杀，头条)
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_ACTIVEINFO)
    suspend fun getHomeActiveInfoData(@FieldMap maps: Map<String, String>): ResResponse<HomeActiveInfoBean>

    /**
     * 社群首页推荐群组
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_COMMUNITY_RECOMM_GROUP_LIST)
    suspend fun getCommunityRecommGroupListData(@FieldMap maps: Map<String, String>): ResResponse<RecommendGroupListBean>

    /**
     * 社群首页九宫格
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_HOME_MENU)
    suspend fun getHomeMenu(@FieldMap maps: Map<String, String>): ResResponse<HomeMenuInfo>

    /**
     * 获取商城数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_MALL_LIST)
    suspend fun getMallList(@FieldMap maps: Map<String, String>): ResResponse<CommdoityResData>

    /**
     * 获取商城列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_COM_INFP)
    suspend fun getMallInfoList(@FieldMap maps: Map<String, String>): ResResponse<ComInfoList>


    /**
     * 邀请好友记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_InviteFriends_List)
    suspend fun getInviteFriendsListData(@FieldMap maps: Map<String, String>): ResResponse<InviteFriendsListBean>

    /**
     * 邀请好友记录
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_GIGHT_ODER_LIST)
    suspend fun getFightDataList(@FieldMap maps: Map<String, String>): ResResponse<FightBaseData>

    /**
     * 拼团邀请数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.PIN_INVITE_USER)
    suspend fun getPinInviteInfo(@FieldMap maps: Map<String, String>): ResResponse<PinInviteRes>

    /**
     * 拼团邀请数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.TEST_API)
    suspend fun getTest(@FieldMap maps: Map<String, String>): ResResponse<String>

    /**
     * 直播登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_LOGIN)
    suspend fun liveLogin(@FieldMap maps: Map<String, String>): ResResponse<LiveLoginInfo>

    /**
     * 直播登录
     */
    @FormUrlEncoded
    @POST(Constants.Api.UP_ROOM_STATES)
    suspend fun upRoomStatus(@FieldMap maps: Map<String, String>): ResResponse<LiveRoomUpInfo>

    /**
     * 三级分类商品列表数据
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_Third_CLASSIFY_PRODUCT_LIST)
    suspend fun getThirdClassifyListData(@FieldMap maps: Map<String, String>): ResResponse<HomePageLikeListDataBean.LikeListDataBean>

    /**
     * 直播推荐列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_V1_LIST)
    suspend fun getLiveV1PlayList(@FieldMap maps: Map<String, String>): ResResponse<RecomLiveParInfo>

    /**
     * 获取直播详情
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIVE_INF)
    suspend fun getLivePlayInfo(@FieldMap maps: Map<String, String>): ResResponse<LivePlayInfo>

    /**
     * 直播关注
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_UP_FOLLOW)
    suspend fun upLiveFollow(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    /**
     * 获取直播人数
     */
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIVE_SUMS)
    suspend fun getLiveSums(@FieldMap maps: Map<String, String>): ResResponse<LiveCountInfo>

    //直播预约
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_RESER)
    suspend fun upLiveRescer(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    //直播点赞
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_GIVE_LINK)
    suspend fun upLiveGilveLink(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    //直播打赏
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_READ_LINE)
    suspend fun readPackLink(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>


    //检查红包交易密码
    @FormUrlEncoded
    @POST(Constants.Api.IM_CHECK_PAY_PASS)
    suspend fun checkPayPass(@FieldMap maps: Map<String, String>): ResResponse<IsSetPassInfo>


    //直播关注列表
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_FOLLEW_LIST)
    suspend fun getLiveFollowList(@FieldMap maps: Map<String, String>): ResResponse<HomeLiveListInfo>

    //直播推荐
    @FormUrlEncoded
    @POST(Constants.Api.LIVEW_RECOMMEND_LIST)
    suspend fun getLiveRecommendList(@FieldMap maps: Map<String, String>): ResResponse<LiveRemmParInfo>

    //直播间初始化
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_SET_INIT)
    suspend fun initLiveSet(@FieldMap maps: Map<String, String>): ResResponse<LiveInitData>

    //获取直播关联商品
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_GET_COMMINT)
    suspend fun getLiveCommentList(@FieldMap maps: Map<String, String>): ResResponse<LiveRelevPar>

    //直播添加商品和群组
    @FormUrlEncoded
    @POST(Constants.Api.ADD_REMM_AND_GROUP)
    suspend fun addLiveReemAndGroup(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.DELETE_REMM_AND_GROPU)
    suspend fun deleteReemAndGroup(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.UP_REMMODIT_NAME)
    suspend fun upRemmoidtyName(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.IMG_UP_LOAD)
    suspend fun imgUpLoadInfo(@FieldMap maps: Map<String, String>): ResResponse<UpLoadPhotoInfoBean>


    /**
     * 直播热门搜索
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_HOT_SEARCH)
    suspend fun liveHotSearch(@FieldMap params: Map<String, String>): ResResponse<LiveHotRes>

    /**
     * 直播历史搜索
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_HISTORY_SEARCH)
    suspend fun liveHistorySearch(@FieldMap params: Map<String, String>): ResResponse<LiveHotRes>

    /**
     * 清除直播历史搜索
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_HISTORY_CLEAR)
    suspend fun liveHistoryClear(@FieldMap params: Map<String, String>): ResResponse<EmptyBean>

    /**
     * 主播搜索
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_SEARCH_WORD)
    suspend fun liveSearch(@FieldMap params: Map<String, String>): ResResponse<LiveSearchRes>

    /**
     * 主播认证
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVER_VERIFY)
    suspend fun liverVerify(@FieldMap params: Map<String, String>): ResResponse<VerifyRes>

    /**
     * 身份证上传
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVER_CARD_UP)
    suspend fun liverUpCard(@FieldMap params: Map<String, String>): ResResponse<UpIdCardBean>

    /**
     * 身份证上传
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVER_CARD_UP)
    suspend fun liverUpBg(@FieldMap params: Map<String, String>): ResResponse<UpLoadPhotoInfoBean>

    /**
     * 认证审核结果
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVER_VERIFY_RESULT)
    suspend fun liverVerifyResult(@FieldMap params: Map<String, String>): ResResponse<VerifyResultRes>

    /**
     * 关注主播
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_UP_FOLLOW)
    suspend fun liveFocus(@FieldMap params: Map<String, String>): ResResponse<LiveFocusRes>

    /**
     * 上传用户页背景
     */
    @FormUrlEncoded
    @POST(Constants.Api.UP_USER_INFO_BG)
    suspend fun upUserBg(@FieldMap params: Map<String, String>): ResResponse<UpLoadPhotoInfoBean>

    /**
     * 修改用户简介
     */
    @FormUrlEncoded
    @POST(Constants.Api.MODIFY_USER_INTRO)
    suspend fun modifyUserIntro(@FieldMap params: Map<String, String>): ResResponse<EmptyBean>

    /**
     * 直播预约
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_APPOINTMENT)
    suspend fun liveAppointment(@FieldMap params: Map<String, String>): ResResponse<EmptyBean>

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_USER_INFO)
    suspend fun getUserInfo(@FieldMap params: Map<String, String>): ResResponse<LiveUserInfoBean>

    /**
     * 我关注的
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_USER_FOLLOW)
    suspend fun getUserFollow(@FieldMap params: Map<String, String>): ResResponse<FansRes>

    /**
     * 我的粉丝
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_USER_FANS)
    suspend fun getUserFans(@FieldMap params: Map<String, String>): ResResponse<FansRes>

    /**
     * 用户直播列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_USER_LIVE_LIST)
    suspend fun getUserLiveList(@FieldMap params: Map<String, String>): ResResponse<LiveParFollowInfo>

    /**
     * 用户消息列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.USER_MSG_LIST)
    suspend fun getUserMsgList(@FieldMap params: Map<String, String>): ResResponse<UserMsgRes>

    /**
     * 用户消息对话列表
     */
    @FormUrlEncoded
    @POST(Constants.Api.USER_MSG_RECORD)
    suspend fun getUserMsgRecordList(@FieldMap params: Map<String, String>): ResResponse<UserMsgRes>

    /**
     * 发送消息
     */
    @FormUrlEncoded
    @POST(Constants.Api.USER_SEND_MSG)
    suspend fun sendUserMsg(@FieldMap params: Map<String, String>): ResResponse<EmptyBean>

    /**
     * 用户收益
     */
    @FormUrlEncoded
    @POST(Constants.Api.USER_PROFILE)
    suspend fun getUserProfile(@FieldMap params: Map<String, String>): ResResponse<ProfileRes>

    /**
     * 消息读取
     */
    @FormUrlEncoded
    @POST(Constants.Api.USER_LETTER_RED)
    suspend fun readMsg(@FieldMap params: Map<String, String>): ResResponse<EmptyBean>


    //直播设置保存
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_SET_SAVE)
    suspend fun liveSetSave(@FieldMap maps: Map<String, String>): ResResponse<LiveSetResultParInfo>

    //获取红包余额
    @FormUrlEncoded
    @POST(Constants.Api.IM_RP_YU_E)
    suspend fun getReadPackAccout(@FieldMap maps: Map<String, String>): ResResponse<YueRes>

    //检查是否有直播中的
    @FormUrlEncoded
    @POST(Constants.Api.CHECK_LIVE_STATUS)
    suspend fun checkLiveStatus(@FieldMap maps: Map<String, String>): ResResponse<LiveBaseStatusInfo>

    //用户点赞的列表
    @FormUrlEncoded
    @POST(Constants.Api.LIKE_VIDEO)
    suspend fun getUserlikeVideo(@FieldMap maps: Map<String, String>): ResResponse<LiveParFollowInfo>

    @FormUrlEncoded
    @POST(Constants.Api.UO_LAST_DATA)
    suspend fun upLastLiveData(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.CHECK_LIVE_ROOM_STATUS)
    suspend fun checkLiveRoomStatus(@FieldMap maps: Map<String, String>): ResResponse<LiveRoomStatus>

    @FormUrlEncoded
    @POST(Constants.Api.VOUCHER_CENTER)
    suspend fun getVoucherCenter(@FieldMap maps: Map<String, String>): ResResponse<VoucherCenterRes>

    //获取直播间优惠券
    @FormUrlEncoded
    @POST(Constants.Api.LIVE_ROOM_COUPONS)
    suspend fun getLiveRoomCoup(@FieldMap maps: Map<String, String>): ResResponse<CouponInfo>

    //领取直播间优惠券
    @FormUrlEncoded
    @POST(Constants.Api.GET_LIVE_ROOM_COUPONS)
    suspend fun receiveLiveRoomCoupon(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    //领取直播间优惠券
    @FormUrlEncoded
    @POST(Constants.Api.ADD_STOCK)
    suspend fun addStock(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.GET_RECOMMEND_LIVER)
    suspend fun getRecommendLiver(@FieldMap maps: Map<String, String>): ResResponse<HomeRecommendLiverBean>

    //添加银行卡
    @FormUrlEncoded
    @POST(Constants.Api.ADD_BANK)
    suspend fun addBankCard(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.UP_USER_IMG)
    suspend fun userImgUpLoadInfo(@FieldMap maps: Map<String, String>): ResResponse<UpLoadPhotoInfoBean>

    //用户体现
    @FormUrlEncoded
    @POST(Constants.Api.USER_WITHRAW)
    suspend fun userWitraw(@FieldMap maps: Map<String, String>): ResResponse<DefalutResultInfo>

    @FormUrlEncoded
    @POST(Constants.Api.USER_INCOME)
    suspend fun useriNCOME(@FieldMap maps: Map<String, String>): ResResponse<IncomeParInfo>

    @FormUrlEncoded
    @POST(Constants.Api.GET_USER_MEMBER)
    suspend fun getUsreLevelInfo(@FieldMap maps: Map<String, String>): ResResponse<UserMemberRes>

    //首页推荐主播
    @FormUrlEncoded
    @POST(Constants.Api.RECOMMENDED_ANCHOR)
    suspend fun remAncher(@FieldMap maps: Map<String, String>): ResResponse<HomeRecommendLiverBean>


}