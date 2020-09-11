package cn.sancell.xingqiu.live.interfacep

import cn.sancell.xingqiu.bean.LivePlayInfo

/**
 * Created by zj on 2020/5/25.
 */
interface OnLiveToolLinsener : OnPlayLinsenr {
    fun onToolShowCoupon()
    fun getToolLiveInfoData(): LivePlayInfo
    fun onToolPagerCloce()
    fun onToolUpLikeSum(sum: Int)
    fun onToolUpLiveWahtSum(sum: Int)
    fun onToolSendMsgIsShow(isShow: Boolean)
}