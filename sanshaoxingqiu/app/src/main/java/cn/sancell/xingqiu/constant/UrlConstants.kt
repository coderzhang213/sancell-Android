package cn.sancell.xingqiu.constant

import cn.sancell.xingqiu.BuildConfig

/**
 * Created by zj on 2020/1/7.
 */
object UrlConstants {
    val API_URL = BuildConfig.H5_API
    val VIP_INFO = API_URL + "sancell-shop-app-hybrid/memberCenter/memberCenter.html?skey="
    //红包使用规则
    val READ_GZ = API_URL + "sancell-shop-app-hybrid/memberCenter/redrule.html"
    //用户协议
    val USER_PROTOCOL = API_URL + "sancell-shop-app-hybrid/memberCenter/UserServiceAgreement.html"
    //隐私协议
    val USER_YS_PRO = API_URL + "sancell-shop-app-hybrid/memberCenter/PlatformPrivacyAgreement.html"
    //会员协议
    val VIP_YX = API_URL + "sancell-shop-app-hybrid/memberCenter/MemberAgreement.html"
    //客服URL
    const val SERVER_API = "sancell-shop-app-hybrid/activeproduct/CustomerService.html?"
}