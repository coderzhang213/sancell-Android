package cn.sancell.xingqiu.live.help

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import cn.sancell.xingqiu.live.listener.OnGetCurrPagerIsShowLinsener
import cn.sancell.xingqiu.live.listener.OnLivePlayManager
import cn.sancell.xingqiu.live.listener.OnLiveStatusLinsener
import com.netease.nim.uikit.common.ToastHelper
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayer

/**
 * Created by zj on 2020/4/13.
 */
abstract class TengXunPlayeBaseHelp : OnLivePlayManager {
    val CACHE_STRATEGY_FAST = 1 //极速
    val CACHE_STRATEGY_SMOOTH = 2 //流畅
    val CACHE_STRATEGY_AUTO = 3 //自动

    val CACHE_TIME_FAST = 1.0f
    val CACHE_TIME_SMOOTH = 5.0f
    private var mLoadingView: ImageView? = null
    var mActivityType = 0
    var mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP // player 播放链接类型
    var mContext: Context? = null
    var mStartPlayTS: Long = 0
    var mRootView: RelativeLayout? = null
    var mIsPlaying = false
    var mOnLiveStatusLinsener: OnLiveStatusLinsener? = null
    var mOnGetCurrPagerIsShowLinsener: OnGetCurrPagerIsShowLinsener? = null

    //当前界面上方课件
    var isShow = true

    companion object {
        const val ACTIVITY_TYPE_PUBLISH = 1

        //直播拉流
        const val ACTIVITY_TYPE_LIVE_PLAY = 2

        //视频播放
        const val ACTIVITY_TYPE_VOD_PLAY = 3
        const val ACTIVITY_TYPE_LINK_MIC = 4
        const val ACTIVITY_TYPE_REALTIME_PLAY = 5
    }

    override fun regOnLiveStatusLinsener(mOnLiveStatusLinsener: OnLiveStatusLinsener) {
        this.mOnLiveStatusLinsener = mOnLiveStatusLinsener
    }

    override fun setOnGetCurrPagerIsShowLinsener(mOnGetCurrPagerIsShowLinsener: OnGetCurrPagerIsShowLinsener) {
        this.mOnGetCurrPagerIsShowLinsener = mOnGetCurrPagerIsShowLinsener
    }

    constructor(mContext: Context, mLoadingView: ImageView) {
        this.mLoadingView = mLoadingView
        this.mContext = mContext
    }

    fun setBackRootView(mRootView: RelativeLayout) {
        this.mRootView = mRootView
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      Player 相关
    //
    /////////////////////////////////////////////////////////////////////////////////
    fun checkPlayUrl(playUrl: String): Boolean {
        when (mActivityType) {
            ACTIVITY_TYPE_LIVE_PLAY -> {
                if (playUrl.startsWith("rtmp://")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV
                } else {
                    ToastHelper.showToast("播放地址不合法，直播目前仅支持rtmp,flv播放方式!")
                    return false
                }
            }
            ACTIVITY_TYPE_VOD_PLAY -> {
                if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".mp4")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".hls")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".m3u8")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS
                } else {
                    ToastHelper.showToast("播放地址不合法，视频目前仅支持.mp4播放方式!")
                    return false
                }
            }
            ACTIVITY_TYPE_REALTIME_PLAY -> {
                if (!playUrl.startsWith("rtmp://")) {
                    ToastHelper.showToast("低延时拉流仅支持rtmp播放方式")
                    return false
                } else if (!playUrl.contains("txSecret")) {
                    AlertDialog.Builder(mContext!!)
                            .setTitle("播放出错")
                            .setMessage("低延时拉流地址需要防盗链签名，详情参考 https://cloud.tencent.com/document/product/454/7880#RealTimePlay!")
                            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }.setPositiveButton("确定") { dialog, which ->
                                val uri = Uri.parse("https://cloud.tencent.com/document/product/454/7880#RealTimePlay!")
                                dialog.dismiss()
                            }.show()
                    return false
                }
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC
            }
            else -> {
                ToastHelper.showToast("播放地址不合法，直播目前仅支持rtmp,flv播放方式!")

                return false
            }
        }
        return true
    }

    fun startLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView?.visibility = View.VISIBLE
            if (mLoadingView?.drawable is AnimationDrawable) {
                (mLoadingView?.drawable as AnimationDrawable).start()
            }

        }
    }

    fun stopLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView?.visibility = View.GONE
            if (mLoadingView?.drawable is AnimationDrawable) {
                (mLoadingView?.drawable as AnimationDrawable).stop()
            }
        }
    }

    //公用打印辅助函数
    protected open fun getNetStatusString(status: Bundle): String? {
        return String.format("%-14s %-14s %-12s\n%-8s %-8s %-8s %-8s\n%-14s %-14s\n%-14s %-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "GOP:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_GOP) + "s",
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_CACHE)
                        + "|" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_CACHE)
                        + "," + status.getInt(TXLiveConstants.NET_STATUS_V_SUM_CACHE_SIZE)
                        + "," + status.getInt(TXLiveConstants.NET_STATUS_V_DEC_CACHE_SIZE)
                        + "|" + status.getInt(TXLiveConstants.NET_STATUS_AV_RECV_INTERVAL)
                        + "," + status.getInt(TXLiveConstants.NET_STATUS_AV_PLAY_INTERVAL)
                        + "," + String.format("%.1f", status.getFloat(TXLiveConstants.NET_STATUS_AUDIO_CACHE_THRESHOLD)),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AUDIO:" + status.getString(TXLiveConstants.NET_STATUS_AUDIO_INFO))
    }

    override fun onSetViewPlayStatus(isPlay: Boolean) {
        isShow = isPlay
    }
}