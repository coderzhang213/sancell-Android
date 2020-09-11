package cn.sancell.xingqiu.live.base

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LivePlayInfo
import cn.sancell.xingqiu.bean.LiveShareBean
import cn.sancell.xingqiu.dialog.LiveShareDialogFgm
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.live.help.ClickLikeHelp
import cn.sancell.xingqiu.live.activity.ReportActivity
import cn.sancell.xingqiu.live.help.LiveImHelp
import cn.sancell.xingqiu.util.BitmapUtils
import cn.sancell.xingqiu.util.RxTimerUtil
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.netease.nim.uikit.common.ToastHelper
import com.umeng.socialize.media.UMImage
import kotlinx.android.synthetic.main.fragment_live_tool_layout.*
import pl.droidsonroids.gif.GifImageView
import pl.droidsonroids.gif.GifTextView
import java.util.*

abstract class LiveCommFragment : BaseLiveFragment() {
    var mLiveViewModel: LiveViewModel? = null
    private var showPostion = 0
    var mLivePlayInfo: LivePlayInfo? = null
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    var mLiveImHelp: LiveImHelp? = null

    //直播是否结束
    var playIsEnd = false

    //生成用来加直播人数的随机数
    var mLiveSumCoef = 0
    var mRxTimerUtil: RxTimerUtil? = null

    //是否可以播放点赞动画
    var isGlivLink: Boolean = true
    var inviteSwitch = 0
    private var mClickLikeHelp: ClickLikeHelp? = null
    abstract val getCurrType: String
    abstract val getRoomId: String
    abstract val layoutId: Int

    //小程序路径
    private var minPath: String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val mRandom = Random()
        mLiveSumCoef = mRandom.nextInt(100)
        mRxTimerUtil = RxTimerUtil()
        super.onActivityCreated(savedInstanceState)
        val mactivity = activity

        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
        mLiveViewModel = ViewModelProviders.of(this@LiveCommFragment).get(LiveViewModel::class.java)

    }

    open fun getLiveInfoData() = mLivePlayInfo

    open fun isShowStreamline(isShow: Boolean) {

    }
    //设置显示优惠券
    open fun setShowCoupon(){

    }

    fun getShowPoston(): Int = showPostion

    //获取viewmodel
    fun getLivieModel(): LiveViewModel {
        return mLiveViewModel!!
    }

    open fun onSetViewPlayStatus(isPlay: Boolean) {

    }

    /**
     * 开始循环检查直播人数
     */
    fun startLoopCheckLive() {
//        getLivewSum()
//        mRxTimerUtil?.interval(1000 * 60 * 1, object : RxTimerUtil.IRxNext {
//            override fun doNext(number: Long) {
//                getLivewSum()
//            }
//        })
    }

    open fun onRePlay() {

    }

    fun getLiveInfo(batchId: String, mType: String) {

        mLiveViewModel?.getLivePlayInfo(batchId, mType)?.observe(this@LiveCommFragment, Observer {
            it?.apply {
                //设置主播ID
                mLiveImHelp?.setmAnchorId(batchInfo.liveUserId.toString())
                inviteSwitch = liveInviteData.inviteSwitch
                bingParViewText(this)
                bingView(this)
                minPath = it.liveInviteData.path


            }
        })
        setInPageTypeView(mType)
    }

    open fun setInPageTypeView(mType: String) {

    }

    open fun bingParViewText(mLivePlayInfo: LivePlayInfo) {

    }

    open fun bingView(mLivePlayInfo: LivePlayInfo) {

    }

    /**
     * 直播开始
     */
    open fun livePlayStart() {

    }

    /**
     * 关闭当前界面
     */
    open fun onPagerCloce() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mRxTimerUtil?.cancel()
        mRxTimerUtil = null
    }

    /**
     * 更新主播观看人数
     *
     */
    open fun onUpLiveWahtSum(sum: Int) {
        //   getLivewSum()
    }

    /**
     * 直播点赞
     */
    open fun onUpLikeSum(sum: Int) {

    }

    /**
     * 当前界面是否可见
     */
    fun currPagerIsShow(): Boolean {
        if (mOnGetLivePalyStatusLinsener != null) {
            val isShow = mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.currPageIsShow(showPostion)
            return isShow!!
        } else {
            return true
        }
    }

    /**
     * 调用接口去获取直播人数
     */
    fun getLivewSum() {
        val roomId = mLivePlayInfo?.liveRoomInfo?.roomId
        val batchId = mLivePlayInfo?.batchInfo?.liveBatchId
        val type = getCurrType
        mLiveViewModel?.getLiveSums(type, roomId!!, batchId!!)?.observe(this@LiveCommFragment, Observer {
            tv_qery_sum.setText(it.count.toString() + "人观看")
        })
    }

    /**
     * 是否是关注
     */
    fun currPagerAttention(): Boolean {
        if (mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.getPlayBaseShowIndex() == 1) {
            return true
        }
        return false
    }

    /**
     * 分享
     */
    fun sharLive(mLivePlayInfo: LivePlayInfo?) {
        if (inviteSwitch == 0) {
            ToastHelper.showToast("内部会议直播，无法分享")
            return
        }
        mLivePlayInfo?.apply {
            Glide.with(context!!).asBitmap().load(batchInfo.iconThumb).listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    toShar(mLivePlayInfo, null)
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    toShar(mLivePlayInfo, resource)
                }
            })
        }

    }

    fun toShar(mLivePlayInfo: LivePlayInfo?, bitmap: Bitmap?) {
        mLivePlayInfo?.apply {

            val info = LiveShareBean(this.liveInviteData.link, this.liveInviteData.title, this.liveInviteData.link)
            if (bitmap != null) {
                info.bitmap = bitmap
                info.image = UMImage(context!!, bitmap)
            } else {
                info.bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo)
                info.image = UMImage(context!!, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo))
            }
            info.path = minPath
            val dialog = LiveShareDialogFgm()
            dialog.setShareInfo(info)
            dialog.setListener(object : LiveShareDialogFgm.OnLiveShareListener {
                override fun onReport() {
                    ReportActivity.start(activity!!)
                }

            })
            dialog.show(childFragmentManager, "share")
        }
    }

    /**
     * 设置当前界面的索引
     */
    fun setShowPostion(showPostion: Int) {
        this.showPostion = showPostion

    }

    /**
     * 设置点赞后的显示
     */
    fun setPraise(iv_click_praise: GifImageView, iv_df_zan: ImageView) {
//        if (mClickLikeHelp == null) {
//            mClickLikeHelp = ClickLikeHelp(context!!, iv_click_praise)
//            mClickLikeHelp?.setPlayAinJsonName("like_chilc.json")
//        }
        if (!isGlivLink) {
            return
        }
        //用来防止一直点
        isGlivLink = false
        iv_click_praise.visibility = View.VISIBLE
        iv_df_zan.visibility = View.GONE
        mRxTimerUtil?.timer(1800, {
            iv_click_praise.visibility = View.GONE
            iv_df_zan.visibility = View.VISIBLE
            isGlivLink = true
        })

    }
}