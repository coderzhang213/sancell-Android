package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.BannerRepository
import cn.sancell.xingqiu.Repository.LiveRepository
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.Repository.UserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.*
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homecommunity.entity.res.LiveData
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.homepage.bean.HomeRecommendLiverBean
import cn.sancell.xingqiu.homeuser.repository.VoucherRepository
import cn.sancell.xingqiu.im.entity.LiveCountInfo
import cn.sancell.xingqiu.im.entity.res.YueRes
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.login.bean.UserBean
import cn.sancell.xingqiu.usermember.bean.UserMemberRes
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RSAUtils
import cn.sancell.xingqiu.util.StringUtils
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2019/12/23.
 */
class LiveViewModel : BaseViewModel() {
    val mLiveRepository by lazy { LiveRepository() }
    val mLiveUserRepository by lazy { LiveUserRepository() }
    val mVoucherRepository by lazy { VoucherRepository() }
    val mBannerRepository by lazy { BannerRepository() }
    val liveList: MutableLiveData<LiveData> = MutableLiveData()
    val liveLogin: MutableLiveData<LiveLoginInfo> = MutableLiveData()
    val liveUpRoom: MutableLiveData<LiveRoomUpInfo> = MutableLiveData()
    val liveV1List: MutableLiveData<RecomLiveParInfo> = MutableLiveData()
    val livePlayInfo: MutableLiveData<LivePlayInfo> = MutableLiveData()
    val upLiveFollow = MutableLiveData<DefalutResultInfo>()
    val mLiveSum: MutableLiveData<LiveCountInfo> = MutableLiveData()
    val mLiveRes: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    val mLiveUpLink: MutableLiveData<DefalutResultInfo> = MutableLiveData()

    //val mLiveReadGive: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    //  val mJyPass: MutableLiveData<IsSetPassInfo> = MutableLiveData()
    val mFollowList: MutableLiveData<HomeLiveListInfo> = MutableLiveData()
    val mFollowRemmList: MutableLiveData<LiveRemmParInfo> = MutableLiveData()
    val mInitSet: MutableLiveData<LiveInitData> = MutableLiveData()
    val mInitRemmList: MutableLiveData<LiveRelevPar> = MutableLiveData()
    val mLiveAddRemGroup: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    val mLiveDeleteRemGroup: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    val mLiveUpNameRemGroup: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    val mLiveSetSave: MutableLiveData<LiveSetResultParInfo> = MutableLiveData()
    val mLiveCheckStatus: MutableLiveData<VerifyResultRes> = MutableLiveData()
    val mLiveCheckStaus: MutableLiveData<LiveBaseStatusInfo> = MutableLiveData()
    val mLiveUpLastData: MutableLiveData<DefalutResultInfo> = MutableLiveData()
    val mBannerData: MutableLiveData<HomeBannerDataBean> = MutableLiveData()
    val mRecommendLiverList: MutableLiveData<HomeRecommendLiverBean> = MutableLiveData()
    val mRemAutche: MutableLiveData<HomeRecommendLiverBean> = MutableLiveData()
    fun getRecommendLiver() {
        val params = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getRecommendLiver(params)
            }
            executeResponse(response, { mRecommendLiverList.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 获取直播列表
     */
    fun getLiveList(mCurrPage: Int, mPageSize: Int) {
        val par = ConvertUtils.getRequest()
        par["page"] = mCurrPage.toString()
        par["pageSize"] = mPageSize.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveList(par)
            }
            executeResponse(response, { liveList.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }

    /**
     * 更新房间状态
     */
    fun upRoomStatus(status: String): MutableLiveData<LiveRoomUpInfo> {
        val mEmpValue = MutableLiveData<LiveRoomUpInfo>()
        val par = ConvertUtils.getRequest()
        par["liveStatus"] = status
        //par["accid"] = RSAUtils.encryptByPublic(PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean::class.java).userId.toString())
        par["accid"] = PreferencesUtils.getString(Constants.Key.key_im_accid, "")
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upRoomStatus(par)
            }
            executeResponse(response, {
                liveUpRoom.value = response.retData
                mEmpValue.value = response.retData

            }, { errMsg.value = response.retMsg })
        }
        return mEmpValue
    }

    /**
     * 直播登录
     */
    fun liveLogin(accid: String, password: String): MutableLiveData<LiveLoginInfo> {
        val par = HashMap<String, String>()
        par["accid"] = accid
        par["token"] = RSAUtils.encryptByPublic(password)
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.liveLogin(par)
            }
            executeResponse(response, { liveLogin.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return liveLogin
    }

    /**
     * 获取直播推荐列表
     */
    fun getLiveV1List(page: String, type: String, batchId: String, anchorId: String) {

        val par = ConvertUtils.getRequest()
        par["page"] = page
        par["pageSize"] = "10"
        par["type"] = type
        par["batchId"] = batchId
        if (type == LivePlayType.USER_CANER_TO_TYPE.type) {
            par["userId"] = anchorId
        } else {
            val userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean::class.java)
            par["userId"] = userBean.userId.toString()
        }

        par["otherUserId"] = anchorId
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveV1PlayList(par)
            }
            executeResponse(response, { liveV1List.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 获取直播详情
     */
    fun getLivePlayInfo(batchId: String, mType: String): MutableLiveData<LivePlayInfo> {
        var mEmpType = ""
        if (mType == LivePlayType.RE_PLAY.type || mType == LivePlayType.LIVE_LIST.type) {//只有推荐的需要
            mEmpType = mType
        } else {
            mEmpType = ""
        }
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = mEmpType
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLivePlayInfo(par)
            }
            executeResponse(response, { livePlayInfo.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return livePlayInfo
    }

    /**
     * 直播关注
     */
    fun upLiveFollow(followedId: String, type: String): MutableLiveData<DefalutResultInfo> {
        val par = ConvertUtils.getRequest()
        par["followedId"] = followedId
        par["type"] = type
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upLiveFollow(par)
            }
            executeResponse(response, { upLiveFollow.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return upLiveFollow
    }

    //获取直播人数
    fun getLiveSums(type: String, roomId: String, batchId: String): MutableLiveData<LiveCountInfo> {
        val reqTime = StringUtils.getCurrentTime()
        val skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")

        val par = HashMap<String, String>()
        par["reqTime"] = reqTime
        par["skey"] = skey
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        par["roomId"] = roomId
        par["type"] = type
        par["batchId"] = batchId
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveSums(par)
            }
            executeResponse(response, { mLiveSum.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mLiveSum
    }

    //直播预约
    fun upLiveRescer(batchId: String, type: String): MutableLiveData<DefalutResultInfo> {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = type
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upLiveRescer(par)
            }
            executeResponse(response, { mLiveRes.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mLiveRes
    }

    //直播点赞
    fun upLiveGilveLink(liveId: String, type: String): MutableLiveData<DefalutResultInfo> {
        val mEmpLiveUpLink: MutableLiveData<DefalutResultInfo> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        par["liveId"] = liveId
        par["type"] = type
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upLiveGilveLink(par)
            }
            executeResponse(response, {
                mLiveUpLink.value = response.retData
                mEmpLiveUpLink.value = response.retData
            }, { errMsg.value = response.retMsg })
        }
        return mEmpLiveUpLink
    }

    //直播打赏
    fun readPackLink(batchId: String, money: String, accountPassword: String): MutableLiveData<DefalutResultInfo> {
        val mLiveReadGive: MutableLiveData<DefalutResultInfo> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["money"] = money
        par["accountPassword"] = RSAUtils.encryptByPublic(accountPassword)
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.readPackLink(par)
            }
            executeResponse(response, { mLiveReadGive.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mLiveReadGive
    }

    //是否设置交易密码
    fun checkPayPass(): MutableLiveData<IsSetPassInfo> {
        val mJyPass: MutableLiveData<IsSetPassInfo> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.checkPayPass(par)
            }
            executeResponse(response, { mJyPass.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mJyPass
    }

    //获取关注列表
    fun getLiveFollowList(mCurrPage: Int) {

        val par = ConvertUtils.getRequest()
        par["page"] = mCurrPage.toString()
        par["pageSize"] = "10"
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveFollowList(par)
            }
            executeResponse(response, { mFollowList.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    //直播推荐
    fun getLiveRecommendList() {
        val par = ConvertUtils.getRequest()
        par["page"] = "1"
        par["pageSize"] = "100"
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveRecommendList(par)
            }
            executeResponse(response, { mFollowRemmList.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 直播设置
     */
    fun initLiveSet() {
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.initLiveSet(par)
            }
            executeResponse(response, { mInitSet.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    //获取直播关联商品
    fun getLiveCommentList(batchId: String, type: String) {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = type
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveCommentList(par)
            }
            executeResponse(response, { mInitRemmList.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun getLiveCommentListSum(batchId: String, type: String): MutableLiveData<LiveRelevPar> {
        val mInitRemmListSum: MutableLiveData<LiveRelevPar> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = type
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getLiveCommentList(par)
            }
            executeResponse(response, {
                mInitRemmListSum.value = response.retData
            }, { errMsg.value = response.retMsg })
        }
        return mInitRemmListSum;
    }

    //直播设置添加商品或直播
    fun addLiveReemAndGroup(batchId: String, type: String, sku: String, tid: String) {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = type
        par["sku"] = sku
        par["tid"] = tid
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.addLiveReemAndGroup(par)
            }
            executeResponse(response, { mLiveAddRemGroup.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    //删除群组或商品
    fun deleteReemAndGroup(batchId: String, type: String, id: String) {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["type"] = type
        par["id"] = id
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.deleteReemAndGroup(par)
            }
            executeResponse(response, { mLiveDeleteRemGroup.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 修改商品别名
     */
    fun upRemmoidtyName(batchId: String, alias: String, sku: String) {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["alias"] = alias
        par["sku"] = sku
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upRemmoidtyName(par)
            }
            executeResponse(response, { mLiveUpNameRemGroup.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    //直播设置保存
    fun liveSetSave(batchId: String, liveTitle: String, sharpness: String, preStartTime: String, isUse: Int) {
        val par = ConvertUtils.getRequest()
        par["batchId"] = batchId
        par["liveTitle"] = liveTitle
        par["sharpness"] = sharpness
        par["preStartTime"] = preStartTime
        par["isUse"] = isUse.toString()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.liveSetSave(par)
            }
            executeResponse(response, { mLiveSetSave.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 检查当前主播是否认证通过
     */
    fun checkVerifyStatus() {
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveUserRepository.getLiverVerifyResult(par)
            }
            executeResponse(response, { mLiveCheckStatus.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }


    //获取拥抱余额
    fun getReadPackAccout(): MutableLiveData<YueRes> {
        val mReadPackValue: MutableLiveData<YueRes> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.getReadPackAccout(par)
            }
            executeResponse(response, { mReadPackValue.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mReadPackValue
    }

    //获取是否有直播中的数据
    fun checkLiveStatus(): MutableLiveData<LiveBaseStatusInfo> {
        val mReadPackValue: MutableLiveData<LiveBaseStatusInfo> = MutableLiveData()
        val par = ConvertUtils.getRequest()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.checkLiveStatus(par)
            }
            executeResponse(response, {
                mReadPackValue.value = response.retData
                mLiveCheckStaus.value = response.retData
            }, { errMsg.value = response.retMsg })
        }
        return mReadPackValue
    }

    //通知服务端复用上一次信息
    fun upLastLiveData(newBatchId: String) {
        val params = ConvertUtils.getRequest()
        params["newBatchId"] = newBatchId
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.upLastLiveData(params)
            }
            executeResponse(response, { mLiveUpLastData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 检查直播间状态
     */
    fun checkLiveRoomStatus(batchId: String): MutableLiveData<LiveRoomStatus> {
        val mCheckRoomStatus: MutableLiveData<LiveRoomStatus> = MutableLiveData()

        val params = ConvertUtils.getRequest()
        params["batchId"] = batchId
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.checkLiveRoomStatus(params)
            }
            executeResponse(response, { mCheckRoomStatus.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mCheckRoomStatus
    }

    //
    fun getLiveRoomCoupon(liveBatchId: String): MutableLiveData<CouponInfo> {
        val mCoupon: MutableLiveData<CouponInfo> = MutableLiveData()
        val params = ConvertUtils.getRequest()
        params["liveBatchId"] = liveBatchId
        launch {
            val response = withContext(Dispatchers.IO) {
                mVoucherRepository.getLiveRoomCoup(params)
            }
            executeResponse(response, { mCoupon.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mCoupon
    }

    /**
     * 领取优惠券
     */
    fun receiveLiveRoomCoupon(couponId: String): MutableLiveData<DefalutResultInfo> {
        val mCoupon: MutableLiveData<DefalutResultInfo> = MutableLiveData()
        val params = ConvertUtils.getRequest()
        params["couponId"] = couponId
        launch {
            val response = withContext(Dispatchers.IO) {
                mVoucherRepository.receiveLiveRoomCoupon(params)
            }
            executeResponse(response, { mCoupon.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mCoupon
    }

    //添加库存
    fun addStock(goodsId: String): MutableLiveData<DefalutResultInfo> {
        val mCoupon: MutableLiveData<DefalutResultInfo> = MutableLiveData()
        val params = ConvertUtils.getRequest()
        params["goodsId"] = goodsId
        launch {
            val response = withContext(Dispatchers.IO) {
                mVoucherRepository.addStock(params)
            }
            executeResponse(response, { mCoupon.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mCoupon
    }

    fun getBannerData(bannerAdType: String) {
        val par = HashMap<String, String>()
        par["bannerAdType"] = bannerAdType
        par["reqTime"] = StringUtils.getCurrentTime()
        par["skey"] = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "")
        par["hashToken"] = RSAUtils.encryptByPublic(par)
        launch {
            val response = withContext(Dispatchers.IO) {
                mBannerRepository.getHomeBannerData(par)
            }

            executeResponse(response, { mBannerData.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    /**
     * 首页推荐主播
     */
    fun remAncher() {
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveRepository.remAncher(ConvertUtils.getRequest())
            }
            executeResponse(response, { mRemAutche.value = response.retData }, { errMsg.value = response.retMsg })
        }
    }

    fun getUserMember(): MutableLiveData<UserMemberRes> {
        val mMember: MutableLiveData<UserMemberRes> = MutableLiveData()
        launch {
            val response = withContext(Dispatchers.IO) {
                mLiveUserRepository.getUsreLevelInfo(ConvertUtils.getRequest())
            }
            executeResponse(response, { mMember.value = response.retData }, { errMsg.value = response.retMsg })
        }
        return mMember
    }
}