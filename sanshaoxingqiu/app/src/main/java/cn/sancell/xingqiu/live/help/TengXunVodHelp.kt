package cn.sancell.xingqiu.live.help

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import cn.sancell.xingqiu.R
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * Created by zj on 2020/4/13.
 * 直播拉流
 */
class TengXunVodHelp(mContext: Context, mPlayerView: TXCloudVideoView, mLoadingView: ImageView) : TengXunPlayeBaseHelp(mContext, mLoadingView), ITXVodPlayListener {

    /**
     * SDK player 相关
     */
    private var mLivePlayer: TXVodPlayer? = null
    private var mPlayConfig: TXVodPlayConfig? = null
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

    /**
     * 初始化播放配置
     */
    fun initPlayConfig() {
        //mRootView.setBackgroundColor(-0x1000000)

        mLivePlayer?.setPlayerView(mPlayerView)

        mLivePlayer?.setVodListener(this)
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
        mLivePlayer?.setConfig(mPlayConfig)
    }

    override fun onStartPlay(mVideoPath: String) {
        if (!checkPlayUrl(mVideoPath)) {
            return
        }

        initPlayConfig()

        val result = mLivePlayer?.startPlay(mVideoPath) // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;

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
        Log.i("keey", "stopPlay")
        mRootView?.setBackgroundResource(R.drawable.main_bkg)
        stopLoadingAnimation()
        if (mLivePlayer != null) {
            mLivePlayer?.stopPlay(true)
            mLivePlayer?.setVodListener(null)
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
            mLivePlayer = TXVodPlayer(mContext)
        }
        mPlayConfig = TXVodPlayConfig()

        mPlayerView?.apply {
            setLogMargin(0f, 0f, 0f, 0f)
            showLog(false)
        }
        setCacheStrategy(CACHE_STRATEGY_AUTO)
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
                mLivePlayer?.setConfig(mPlayConfig)
            }
            CACHE_STRATEGY_SMOOTH -> {
                mLivePlayer?.setConfig(mPlayConfig)
            }
            CACHE_STRATEGY_AUTO -> {
                mLivePlayer?.setConfig(mPlayConfig)
            }
            else -> {
            }
        }
    }

    override fun onPlayEvent(p1: TXVodPlayer?, event: Int, param: Bundle?) {
        when (event) {
            TXLiveConstants.PLAY_EVT_PLAY_PROGRESS -> {
                if (!isShow) {
                    mLivePlayer?.apply {
                        if (isPlaying) {
                            Log.i("keey", "当前界面不可见了，暂停voidestopPlay")
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
                mOnGetCurrPagerIsShowLinsener?.onPlayError("播放异常，请刷新！")
                stopPlay()
            }
            TXLiveConstants.PLAY_EVT_PLAY_END -> {
                mOnLiveStatusLinsener?.onLivePalyEnd()

            }
            TXLiveConstants.PLAY_EVT_PLAY_LOADING -> startLoadingAnimation()
            TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
                mOnGetCurrPagerIsShowLinsener?.apply {
                    Log.i("keey", "第一只帧")
                    if (!onGetCurrPagerIsShow()) {
                        Log.i("keey", "第一只帧pause")
                        pause()
                    }
                }
                stopLoadingAnimation()
            }
            TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION -> {
                // Log.d("keey", "size " + param?.getInt(TXLiveConstants.EVT_PARAM1) + "x" + param?.getInt(TXLiveConstants.EVT_PARAM2))

            }
            TXLiveConstants.PLAY_EVT_CHANGE_ROTATION -> {
            }
            else -> if (param != null) {
                val data = param?.getByteArray(TXLiveConstants.EVT_GET_MSG)

                //Toast.makeText(getContext(), seiMessage, Toast.LENGTH_SHORT).show();
            }
        }

        if (event < 0) {
            //  ToastHelper.showToast(param?.getString(TXLiveConstants.EVT_DESCRIPTION))
        }
    }

    override fun onNetStatus(p0: TXVodPlayer?, p1: Bundle?) {
    }

}