package cn.sancell.xingqiu.interfaces

/**
 * Created by zj on 2020/4/7.
 */
interface OnChatLoginLisener {
    fun onLoginSucess()
    fun onLoginError(error: String)
}