package cn.sancell.xingqiu.live.listener

/**
 * Created by zj on 2020/4/13.
 */
interface OnLivePlayManager {
    //开始直播
    fun onStartPlay(mVideoPath: String)

    //停止直播
    fun stopPlay()

    //暂停
    fun pause()

    //是否正在直播中
    fun isPlaying(): Boolean

    //结束
    fun onDestroy()

    //从新开始
    fun resume()

    //初始化参数
    fun initPlay()

    //设置播放类型
    fun setActivityType(mActType: Int)

    fun regOnLiveStatusLinsener(mOnLiveStatusLinsener: OnLiveStatusLinsener)

    fun setOnGetCurrPagerIsShowLinsener(mOnGetCurrPagerIsShowLinsener: OnGetCurrPagerIsShowLinsener)

    //设置当前界面是否可见
    fun onSetViewPlayStatus(isPlay: Boolean)
}