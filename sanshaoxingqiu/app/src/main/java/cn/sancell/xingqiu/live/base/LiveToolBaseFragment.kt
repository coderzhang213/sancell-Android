package cn.sancell.xingqiu.live.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CouponItemInfo
import cn.sancell.xingqiu.bean.LivePlayInfo
import cn.sancell.xingqiu.bean.ObserverBuild
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.dialog.CouponDialog
import cn.sancell.xingqiu.dialog.GiveARewardDialog
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckPhoneActivity
import cn.sancell.xingqiu.interfaces.OnCoupnGetLinener
import cn.sancell.xingqiu.interfaces.OnGiveReadLinsenr
import cn.sancell.xingqiu.interfaces.OnPlayPasswordLinsenr
import cn.sancell.xingqiu.ktenum.LivePagerType
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveOtherInfoActivity
import cn.sancell.xingqiu.live.help.LiveImHelp
import cn.sancell.xingqiu.login.bean.UserBean
import cn.sancell.xingqiu.usermember.model.UserModel
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.NumberUtils
import cn.sancell.xingqiu.util.PlayDialogUtils
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import kotlinx.android.synthetic.main.fragment_live_tool_layout.*

/**
 * 客户端
 */
abstract class LiveToolBaseFragment : LiveCommFragment() {

    //是否简洁模式
    var isConciseModel = false

    //点赞数
    private var mlikeCount = 0

    //控制双击点赞
    private var mLikeLastTime = 0L
    var mCouponDialog: CouponDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_live_tool_layout, null)
        val fl_content = mView.findViewById<FrameLayout>(R.id.fl_content)
        fl_content.addView(inflater.inflate(layoutId, null))
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLiveImHelp = LiveImHelp(activity, getRoomId, "1")
        ObserverManger.getInstance(ObserverKey.LIVE_REM_CLOSE).registerObserver(liveStatusObeser)

        initView()
        initBaseData()


    }


    override fun onDestroy() {
        super.onDestroy()
        ObserverManger.getInstance(ObserverKey.LIVE_REM_CLOSE).removeObserver(liveStatusObeser)


    }


    override fun setInPageTypeView(mType: String) {
        super.setInPageTypeView(mType)
        val layPar = v_top.layoutParams
        if (mType == LivePlayType.HOME_TO_TYPE.type) {//推荐进入
            layPar.height = ScreenUtil.dip2px(50f)
        } else {// 其他
            layPar.height = ScreenUtil.dip2px(50f)
        }
    }


    /**
     * 开始直播
     */
    fun onPlayStart() {
        setRePlayStatus(true, false)
    }

    /**
     * 暂停
     */
    fun onPlayPause() {
        setRePlayStatus(false, false)
    }

    /**
     * 显示直播结束
     */
    fun showLivePlayEnd() {
        rl_live_end.visibility = View.VISIBLE
        ll_rig_tool.visibility = View.GONE
        tv_msg.text = "直播已结束"
        playIsEnd = true
    }

    /**
     * 显示播放异常
     */
    open fun showLivePlayError(error: String) {
        if (playIsEnd) {
            return
        }
        if (rl_live_end == null) {
            return
        }
        rl_live_end.visibility = View.VISIBLE
        tv_re_start.visibility = View.VISIBLE
        ll_rig_tool.visibility = View.GONE
        tv_msg.setText(error)
    }

    fun initView() {
        tv_re_start.setOnClickListener(mOnClickLinsener)
        tv_concise_model.setOnClickListener(mOnClickLinsener)
        tv_attention.setOnClickListener(mOnClickLinsener)

        iv_df_zan.setOnClickListener(mOnClickLinsener)
        iv_commodity.setOnClickListener(mOnClickLinsener)
        iv_group.setOnClickListener(mOnClickLinsener)
        iv_give.setOnClickListener(mOnClickLinsener)
        iv_shar.setOnClickListener(mOnClickLinsener)
        uci_user_icon.setOnClickListener(mOnClickLinsener)
        if (getCurrType == LivePagerType.REPLAY.type) {//只要回放才有暂停功能
            tv_concise_model.visibility = View.INVISIBLE
            rl_all_conent.setOnClickListener(mOnClickLinsener)
            iv_play.setOnClickListener(mOnClickLinsener)

        } else if (getCurrType == LivePagerType.LIVE_PLAY.type) {//直播双击点赞
            tv_concise_model.visibility = View.VISIBLE

            rl_all_conent.setOnClickListener {
//                if (System.currentTimeMillis() - mLikeLastTime <= 1000) {
//
//                }
                //   mLikeLastTime = System.currentTimeMillis()

            }
        }

        setViewVisibility(tv_concise_model, ll_rig_tool, rl_user_info_par)

    }

    override fun setShowCoupon() {
        if (getCurrType == LivePagerType.LIVE_PLAY.type) {
            iv_coupon_icon.visibility = View.VISIBLE
            iv_coupon_icon.setOnClickListener(mOnClickLinsener)
        }

    }

    fun initBaseData() {
        mLiveViewModel?.errMsg?.observe(viewLifecycleOwner, Observer {
            ToastHelper.showToast(it)
        })
    }

    /**
     * 设置点赞数
     */
    fun setLikeCout() {
        tv_praise_sum.setText(NumberUtils.getNumberToWan(mlikeCount))
    }


    override fun bingParViewText(mLivePlayInfo: LivePlayInfo) {
        super.bingParViewText(mLivePlayInfo)
        this.mLivePlayInfo = mLivePlayInfo
        mLivePlayInfo.apply {
            mlikeCount = likeCount
            if (getCurrType == LivePagerType.REPLAY.type) {//回放
                ll_rep_name.visibility = View.VISIBLE
                tv_repl_name.setText(batchInfo.liveTitle)
            }
            if (getCurrType == LivePagerType.LIVE_PLAY.type) {
                if (isBindingCoupon) {//有优惠券才显示
                    setShowCoupon()
                }
            }

            onUpLiveWahtSum(onlineUserCount)
            setLikeCout()
            tv_user_name.setText(this.batchInfo.liveUserName)
            val userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean::class.java)

            if (this.isFollow == "1" || userBean.userId == this.batchInfo.liveUserId) {//如果是主播本人也不需要显示
                tv_attention.visibility = View.GONE
            }

            ImageLoaderUtils.loadImage(context, this.batchInfo.gravatar, uci_user_icon)

            if (getCurrType == LivePagerType.RESERVATION.type) {//预约状态显示预约人数
                tv_qery_sum.setText(this.reserveCount.toString() + "人预约")
            } else if (getCurrType == LivePagerType.REPLAY.type) {//回放
                getLivewSum()
            } else if (getCurrType == LivePagerType.LIVE_PLAY.type) {//直播要循环检查
                startLoopCheckLive()
            }
        }
    }

    override fun onDestroyView() {
        if (getCurrType == LivePagerType.LIVE_PLAY.type) { //只有直播才有
            goAwayLive()
        }
        super.onDestroyView()
        mRxTimerUtil?.cancel()
    }

    /**
     * 离开直播间
     */
    fun goAwayLive() {
        ObserverManger.getInstance(ObserverKey.GO_AWAY_LIVE).notifyObserver(getRoomId)
    }


    /**
     * 设置回放暂停
     */
    fun setRePlayStatus(isPlay: Boolean, isNotf: Boolean) {
        if (getCurrType == LivePagerType.REPLAY.type) {//只要回放才能暂停
            if (isPlay) {
                rl_content_play.visibility = View.GONE
            } else {
                rl_content_play.visibility = View.VISIBLE
            }
            if (isNotf) {
                onPlayStatus(isPlay)
            }

        }

    }

    override fun onUpLikeSum(sum: Int) {
        super.onUpLikeSum(sum)
        mlikeCount = sum
        setLikeCout()
        //点赞动画
        setPraise(iv_click_praise, iv_df_zan)
    }

    override fun onUpLiveWahtSum(sum: Int) {
        super.onUpLiveWahtSum(sum)
        if (getCurrType == LivePagerType.LIVE_PLAY.type) {
            if (tv_qery_sum != null) {
                tv_qery_sum.setText(sum.toString() + "观看")
            }
        }
    }

    open fun onPlayStatus(isPlay: Boolean) {


    }

    /**
     * 预约模式下不显示这几个
     */
    open fun setViewVisibility(view1: TextView, view2: View, view3: LinearLayout) {

    }

    val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View) {

            when (v.id) {
                R.id.uci_user_icon -> {//跳转到用户主页
                    LiveOtherInfoActivity.start(context!!, mLivePlayInfo?.batchInfo?.liveUserId.toString())
                }
                R.id.tv_concise_model -> {//简洁模式
                    if (isConciseModel) {
                        isConciseModel = false
                        tv_concise_model.setText("简洁模式")
                    } else {
                        isConciseModel = true
                        tv_concise_model.setText("互动模式")
                    }
                    if (getCurrType == LivePagerType.REPLAY.type) {
                        setVisStatus(ll_rep_name)
                    }
                    setVisStatus(rl_user_info_par)
                    setVisStatus(ll_jj_mod)
                    val data = ObserverBuild("1", isConciseModel)
                    isShowStreamline(isConciseModel)
                    //去同事title改变状态
                    ObserverManger.getInstance(ObserverKey.LIVE_CONCISE_OPEN).notifyObserver(data)

                }
                R.id.tv_attention -> {//关注
                    mLivePlayInfo?.apply {
                        mLiveViewModel?.upLiveFollow(this.batchInfo.liveUserId.toString(), "1")?.observe(this@LiveToolBaseFragment, Observer {
                            ToastHelper.showToast("关注成功")
                            tv_attention.visibility = View.GONE
                        })
                    }

                }

                R.id.iv_df_zan -> {//赞点击
                    mLivePlayInfo?.apply {
                        //点赞动画
                        setPraise(iv_click_praise, iv_df_zan)
                        //可以一直点赞，但必须等接口回调成功后
                        iv_df_zan.setEnabled(false)
                        mLiveViewModel?.upLiveGilveLink(this.batchInfo.liveBatchId, "1")?.observe(this@LiveToolBaseFragment, Observer {
                            iv_df_zan.setEnabled(true)
                            mlikeCount++
                            setLikeCout()
                            // ToastHelper.showToast("点赞成功")

                        })
                    }
                }
                R.id.iv_commodity -> {//点击商品
                    mLiveImHelp?.click("a")
                }
                R.id.iv_group -> {//点击群组
                    mLiveImHelp?.click("b")
                }
                R.id.iv_give -> {//打赏
                    if (activity == null) {
                        return
                    }
                    mLiveViewModel?.getUserMember()?.observe(this@LiveToolBaseFragment, Observer {
                        if (it.useCouponPoint == 1) {
                            val mGiveARewardDialog = GiveARewardDialog(activity!!, mToReadLinsenr)
                            mGiveARewardDialog.setReadPackValue(mLiveViewModel, this@LiveToolBaseFragment)
                            mGiveARewardDialog.show()
                        } else {
                            ToastHelper.showToast("暂不能打赏红包")
                        }

                    })


                }
                R.id.iv_shar -> {//分享
                    sharLive(mLivePlayInfo)
                }
                R.id.rl_all_conent -> {//点击整个播放界面
                    setRePlayStatus(false, true)
                }
                R.id.iv_play -> {
                    setRePlayStatus(true, true)
                }
                R.id.tv_re_start -> {//从新开始
                    tv_re_start.visibility = View.GONE
                    rl_live_end.visibility = View.GONE
                    ll_rig_tool.visibility = View.VISIBLE
                    //从新去加载
                    onRePlay()
                }
                R.id.iv_coupon_icon -> {//优惠券
                    mLiveViewModel?.getLiveRoomCoupon(getRoomId)?.observe(this@LiveToolBaseFragment, Observer {
                        it?.apply {
                            this.dataList.apply {
                                if (this.size > 0) {
                                    mCouponDialog?.apply {
                                        if (isShowing) {
                                            dismiss()
                                        }
                                    }
                                    mCouponDialog = CouponDialog(activity!!, object : OnCoupnGetLinener {
                                        override fun onGetCounGet(mInfo: CouponItemInfo) {
                                            receiveCoupon(mInfo)
                                        }
                                    }, this)
                                    mCouponDialog?.show()
                                } else {
                                    ToastHelper.showToast("暂无优惠券")
                                }


                            }

                        }
                    })

                }
            }
        }
    }

    /**
     * 领取优惠券
     */
    fun receiveCoupon(mInfo: CouponItemInfo) {
        mLiveViewModel?.receiveLiveRoomCoupon(mInfo.id)?.observe(this@LiveToolBaseFragment, Observer {
            ToastHelper.showToast("领取成功")

        })
    }

    private val mToReadLinsenr = object : OnGiveReadLinsenr {

        override fun onClcikGive(money: String) {
            toCheacPlayPasswork(money)
        }
    }

    fun showPlayPassDialog(money: String) {
        val mDial = PlayDialogUtils()
        mDial.showCheckPayPwdDialog(context, object : OnPlayPasswordLinsenr {
            override fun onComrinPlayPaasord(pass: String) {
                mDial.dismiss()
                toReadPack(money, pass)
            }
        })
    }

    //用来监听状态
    private val liveStatusObeser = OnObserver {
        if (currPagerIsShow()) {
            onPagerCloce()
        }
    }

    /**
     * 检查支付 密码是否设置
     */
    fun toCheacPlayPasswork(money: String) {
        mLivePlayInfo?.apply {
            mLiveViewModel?.checkPayPass()?.observe(this@LiveToolBaseFragment, Observer {
                if (it.check == "1") {
                    showPlayPassDialog(money)
                } else {//跳转到设置界面
                    //跳转密码设置
                    ModifyPayPwdCheckPhoneActivity.start(context)
                }

            })

        }
    }

    /**
     * 去打赏
     */
    fun toReadPack(money: String, pass: String) {
        mLivePlayInfo?.apply {
            mLiveViewModel?.readPackLink(this.batchInfo.liveBatchId, money, pass)?.observe(this@LiveToolBaseFragment, Observer {
                ToastHelper.showToast("打赏成功")

            })

        }
    }


    fun setVisStatus(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
        }


    }
}
