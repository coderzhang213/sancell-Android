package cn.sancell.xingqiu.live.help

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import cn.sancell.xingqiu.R
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayConfig
import com.tencent.rtmp.TXLivePlayer
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * Created by zj on 2020/4/13.
 * 直播拉流
 */
class TengXunLiveHelp(mContext: Context, mPlayerView: TXCloudVideoView, mLoadingView: ImageView) : TengXunPlayeBaseHelp(mContext, mLoadingView), ITXLivePlayListener {

    /**
     * SDK player 相关
     */
    private var mLivePlayer: TXLivePlayer? = null
    private var mPlayConfig: TXLivePlayConfig? = null
    private var mPlayerView: TXCloudVideoView? = null


    private var mCurrentRenderMode // player 渲染模式
            = 0
    private var mCurrentRenderRotation // player 渲染角度
            = 0
    private var mHWDecode = false // 是否使用硬解码

    private var mCacheStrategy = 0 // player 缓存策略

    init {
        this.mPlayerView = mPlayerView
        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT
    }

    override fun setActivityType(mActivityType: Int) {
        this.mActivityType = mActivityType

    }


    override fun onStartPlay(mVideoPath: String) {
        if (!checkPlayUrl(mVideoPath)) {
            return
        }

        //mRootView.setBackgroundColor(-0x1000000)

        mLivePlayer?.setPlayerView(mPlayerView)

        mLivePlayer?.setPlayListener(this)
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer?.enableHardwareDecode(mHWDecode)
        mLivePlayer?.setRenderRotation(mCurrentRenderRotation)
        mLivePlayer?.setRenderMode(mCurrentRenderMode)
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        // HashMap<String, String> headers = new HashMap<>();
        // headers.put("Referer", "qcloud.com");
        // mPlayConfig.setHeaders(headers);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        // HashMap<String, String> headers = new HashMap<>();
        // headers.put("Referer", "qcloud.com");
        // mPlayConfig.setHeaders(headers);
        mPlayConfig?.setEnableMessage(true)
        mLivePlayer?.setConfig(mPlayConfig)
        val result = mLivePlayer?.startPlay(mVideoPath, mPlayType) // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;

        if (result != 0) {
            //mRootView.setBackgroundResource(R.drawable.main_bkg)
            return
        }

        Log.w("video render", "timetrack start play")

        //  startLoadingAnimation();


        //  startLoadingAnimation();
        mStartPlayTS = System.currentTimeMillis()

    }

    override fun stopPlay() {
        mRootView?.setBackgroundResource(R.drawable.main_bkg)

        stopLoadingAnimation()
        if (mLivePlayer != null) {
            mLivePlayer?.stopRecord()
            mLivePlayer?.setPlayListener(null)
            mLivePlayer?.stopPlay(true)
        }
        mIsPlaying = false
    }

    override fun pause() {
        mLivePlayer?.pause()
    }

    override fun isPlaying(): Boolean {
        if (mLivePlayer == null) {
            return false
        }
        return mLivePlayer!!.isPlaying
    }

    override fun onDestroy() {
        if (mLivePlayer != null) {
            mLivePlayer?.stopPlay(true)
            mLivePlayer = null
        }
        if (mPlayerView != null) {
            mPlayerView?.onDestroy()
            mPlayerView = null
        }
        mPlayConfig = null
    }

    override fun resume() {
        mLivePlayer?.resume()
    }


    override fun initPlay() {
        if (mLivePlayer == null) {
            mLivePlayer = TXLivePlayer(mContext)
        }
        mPlayConfig = TXLivePlayConfig()
        mPlayerView?.apply {
            setLogMargin(0f, 0f, 0f, 0f)
            showLog(false)
        }
        setCacheStrategy(CACHE_STRATEGY_AUTO)
    }

    override fun onPlayEvent(event: Int, param: Bundle?) {
        when (event) {
            TXLiveConstants.PLAY_EVT_PLAY_PROGRESS -> {
                if (!isShow) {
                    mLivePlayer?.apply {
                        if (isPlaying) {
                            Log.i("keey", "当前界面不可见了，暂停livestopPlay")

                            stopPlay()
                        }
                    }
                }
            }
            TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                stopLoadingAnimation()
                Log.d("AutoMonitor", "PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS))
            }
            TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                mOnGetCurrPagerIsShowLinsener?.onPlayError("主播开小差去了~马上就回来！")
                stopPlay()
            }
            TXLiveConstants.PLAY_EVT_PLAY_END -> Log.i("keey", "直播结束")
            TXLiveConstants.PLAY_EVT_PLAY_LOADING -> startLoadingAnimation()
            TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
                mOnGetCurrPagerIsShowLinsener?.apply {
                    if (!onGetCurrPagerIsShow()) {
                        pause()
                    }
                }
                stopLoadingAnimation()
            }
            TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION -> {
                Log.d("keey", "size " + param?.getInt(TXLiveConstants.EVT_PARAM1) + "x" + param?.getInt(TXLiveConstants.EVT_PARAM2))

            }
            TXLiveConstants.PLAY_EVT_CHANGE_ROTATION -> {
            }
            else -> if (param != null) {
                val data = param?.getByteArray(TXLiveConstants.EVT_GET_MSG)

                //Toast.makeText(getContext(), seiMessage, Toast.LENGTH_SHORT).show();
            }
        }

        if (event < 0) {
            //   ToastHelper.showToast(param?.getString(TXLiveConstants.EVT_DESCRIPTION))
        }
    }

    override fun onNetStatus(p0: Bundle?) {
    }

    /////////////////////////////////////////////////////////////////////////////////
    //
    //                      缓存策略配置
    //
    /////////////////////////////////////////////////////////////////////////////////
    fun setCacheStrategy(nCacheStrategy: Int) {
        if (mCacheStrategy == nCacheStrategy) return
        mCacheStrategy = nCacheStrategy
        when (nCacheStrategy) {
            CACHE_STRATEGY_FAST -> {
                mPlayConfig?.setAutoAdjustCacheTime(true)
                mPlayConfig?.setMaxAutoAdjustCacheTime(CACHE_TIME_FAST)
                mPlayConfig?.setMinAutoAdjustCacheTime(CACHE_TIME_FAST)
                mLivePlayer?.setConfig(mPlayConfig)
            }
            CACHE_STRATEGY_SMOOTH -> {
                mPlayConfig?.setAutoAdjustCacheTime(false)
                mPlayConfig?.setMaxAutoAdjustCacheTime(CACHE_TIME_SMOOTH)
                mPlayConfig?.setMinAutoAdjustCacheTime(CACHE_TIME_SMOOTH)
                mLivePlayer?.setConfig(mPlayConfig)
            }
            CACHE_STRATEGY_AUTO -> {
                mPlayConfig?.setAutoAdjustCacheTime(true)
                mPlayConfig?.setMaxAutoAdjustCacheTime(CACHE_TIME_SMOOTH)
                mPlayConfig?.setMinAutoAdjustCacheTime(CACHE_TIME_FAST)
                mLivePlayer?.setConfig(mPlayConfig)
            }
            else -> {
            }
        }
    }

}