package cn.sancell.xingqiu.live.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LivePlayInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.live.help.LiveImHelp
import cn.sancell.xingqiu.live.play.services.LivePlayExitServer
import cn.sancell.xingqiu.util.NumberUtils
import cn.sancell.xingqiu.util.ServerUtils
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import kotlinx.android.synthetic.main.fragment_live_capture_tool_layout.*

/**
 * 主播端
 */
abstract class LiveCaptureToolBaseFragment : LiveCommFragment() {
    var mLastClickTime = 0L
    var isOpenFilter = false

    abstract fun openCloseFilter(isOpenFilter: Boolean)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_live_capture_tool_layout, null)
        val fl_content = mView.findViewById<FrameLayout>(R.id.fl_content)
        fl_content.addView(inflater.inflate(layoutId, null))
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ObserverManger.getInstance(ObserverKey.LIVE_REM_CLOSE).registerObserver(liveStatusObeser)
        ObserverManger.getInstance(ObserverKey.LIVE_ATTEN_CLOSE).registerObserver(liveAttCloseObeser)
        mLiveImHelp = LiveImHelp(activity, getRoomId, "2")
        initView()
    }

    //用来监听状态
    private val liveStatusObeser = OnObserver {
        if (currPagerIsShow()) {
            onPagerCloce()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        ObserverManger.getInstance(ObserverKey.LIVE_REM_CLOSE).removeObserver(liveStatusObeser)
        ObserverManger.getInstance(ObserverKey.LIVE_ATTEN_CLOSE).removeObserver(liveAttCloseObeser)
    }

    private val liveAttCloseObeser = OnObserver {
        if (currPagerAttention()) {//关注界面不需要判断当前是否显示
            onPagerCloce()
        }
    }

    override fun bingParViewText(mLivePlayInfo: LivePlayInfo) {
        super.bingParViewText(mLivePlayInfo)
        this.mLivePlayInfo = mLivePlayInfo
        mLivePlayInfo.apply {
            tv_user_name.setText(this.batchInfo.liveUserName)
            ImageLoaderUtils.loadImage(context, this.batchInfo.gravatar, uci_user_icon)
            fase_sum.setText("粉丝" + fansCount + "人")
            tv_fash_sum.setText(NumberUtils.getNumberToWan(likeCount))
        }
    }

    /**
     * 直播开始
     */
    override fun livePlayStart() {
        super.livePlayStart()
        ll_rig_tool.visibility = View.VISIBLE
    }

    /**
     * 显示直播异常提示
     */
    fun showLivePalyError(errorMsg: String) {
        playIsEnd = true
        rl_live_end.visibility = View.VISIBLE
        ll_rig_tool.visibility = View.GONE
        tv_msg.setText(errorMsg)
    }

    /**
     * 显示直播结束
     */
    fun showLivePlayEnd() {
        playIsEnd = true
        rl_live_end.visibility = View.VISIBLE
        ll_rig_tool.visibility = View.GONE
    }

    open fun initView() {
        iv_colse.setOnClickListener(mOnClickLinsener)
        iv_switchover_photo.setOnClickListener(mOnClickLinsener)
        iv_cap_commodity.setOnClickListener(mOnClickLinsener)
        iv_filter.setOnClickListener(mOnClickLinsener)
        iv_shar.setOnClickListener(mOnClickLinsener)
        iv_video.setOnClickListener(mOnClickLinsener)
        uci_user_icon.setOnClickListener(mOnClickLinsener)
    }

    override fun onUpLikeSum(sum: Int) {
        super.onUpLikeSum(sum)
        tv_fash_sum.setText(NumberUtils.getNumberToWan(sum))
        //点赞动画
        //   setPraise(iv_click_praise, iv_df_zan)
    }

    override fun onUpLiveWahtSum(sum: Int) {
        super.onUpLiveWahtSum(sum)
        if (tv_qery_sum != null) {
            tv_qery_sum.setText(sum.toString() + "观看")
        }
    }

    fun setDefaultLiveSum() {
        mLivePlayInfo?.apply {
            onUpLiveWahtSum(onlineUserCount)

        }

    }

    /**
     * 这样写是因为通用的请求，绑定了 fragmetn,退出后去 发起http请求，会本默认取消掉
     */
    fun upServerLiveExit() {
        //if (!playIsEnd) {//如果是直播被后台停止，就不在去调用结束接口
        //去通知服务里面结束，在这里界面被销毁了，状态可能更新不到服务端
        if (ServerUtils.isServiceRunning(context!!, LivePlayExitServer::class.java.name)) {
            ObserverManger.getInstance(ObserverKey.LIVE_UP_PLAAY_EXIT).notifyObserver(null)
        } else {
            //启动服务并结束
            ServerUtils.startServer(activity!!, true)

        }
        // }
    }

    val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.uci_user_icon -> {//跳转到用户主页
                    // LiveOtherInfoActivity.start(context!!, mLivePlayInfo?.batchInfo?.liveUserId.toString())
                }
                R.id.iv_colse -> {//关闭
                    onPagerCloce()
                }

                R.id.iv_cap_commodity -> {//点击商品
                    mLiveImHelp?.click("a")
                }
                R.id.iv_filter -> {//点击滤镜
                    isOpenFilter = !isOpenFilter
                    openCloseFilter(isOpenFilter)
//                    val mFilterSelectDialog = FilterSelectDialog(context!!, isOpenFilter)
//                    mFilterSelectDialog.setOnFilterLinsenr(object : OnFilterLinsenr {
//                        override fun onSelectFilterlinsenr(postion: Int) {
//                            if (postion == 0) {//打开关闭滤镜
//                                isOpenFilter = false
//                                openCloseFilter(isOpenFilter)
//                            } else {
//                                isOpenFilter = true
//                                openCloseFilter(isOpenFilter)
//                            }
//                        }
//                    })
//                    mFilterSelectDialog.show()

                }
                R.id.iv_shar -> {//分享
                    sharLive(mLivePlayInfo)
                }
                R.id.iv_switchover_photo -> {//切换摄像头
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - mLastClickTime > 1000) {
                        switchCam()
                        mLastClickTime = currentTime
                    }

                }
                R.id.iv_video -> {//打开关闭声音
                    switchAudio()
                }
            }
        }
    }

    open fun switchCam() {

    }

    open fun switchAudio() {

    }
}
