package cn.sancell.xingqiu.live.listener

/**
 * Created by zj on 2020/4/15.
 */
interface OnGetCurrPagerIsShowLinsener {
    fun onGetCurrPagerIsShow(): Boolean

    fun onPlayError(eror: String)
}