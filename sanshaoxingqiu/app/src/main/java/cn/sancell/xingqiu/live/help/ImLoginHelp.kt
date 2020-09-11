package cn.sancell.xingqiu.live.help

import android.text.TextUtils
import android.util.Log
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.im.sys.ImCache
import cn.sancell.xingqiu.interfaces.OnChatLoginLisener
import cn.sancell.xingqiu.live.user.LiveCache
import cn.sancell.xingqiu.util.PreferencesUtils
import com.alibaba.fastjson.JSON
import com.netease.nim.uikit.api.NimUIKit
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo

/**
 * Created by zj on 2020/4/7.
 */
object ImLoginHelp {
    fun imLogin(mOnChatLoginLisener: OnChatLoginLisener?) {
        val accId = PreferencesUtils.getString(Constants.Key.key_im_accid, "")
        val token = PreferencesUtils.getString(Constants.Key.key_im_token, "")
        if (TextUtils.isEmpty(accId)) {
            return
        }
        val info = LoginInfo(accId, token)

        NimUIKit.login(info, object : RequestCallback<LoginInfo> {
            override fun onSuccess(info: LoginInfo) {
                ImCache.setAccount(info.account)
                LiveCache.setAccount(info.account)
                mOnChatLoginLisener?.onLoginSucess()
            }

            override fun onFailed(code: Int) {
                Log.i("keey", "IMcode:" + code)
                mOnChatLoginLisener?.onLoginError("")
            }

            override fun onException(throwable: Throwable) {
                mOnChatLoginLisener?.onLoginError("")

            }
        })
    }
}