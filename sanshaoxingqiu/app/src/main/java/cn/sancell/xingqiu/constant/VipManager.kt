package cn.sancell.xingqiu.constant

import cn.sancell.xingqiu.login.bean.UserBean
import cn.sancell.xingqiu.util.PreferencesUtils

/**
 * Created by zj on 2020/1/2.
 */
class VipManager {
    companion object {
        const val VIP_LEVE = 2
        //判断当前是否VIP
        fun isVipCheck(): Boolean {
            val userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean::class.java)
            if (userBean != null && userBean.realMemberLevel >= VIP_LEVE) {
                return true
            } else {
                return false
            }
        }
    }


}