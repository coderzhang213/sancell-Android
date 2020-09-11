package cn.sancell.xingqiu.live.activity

import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.kt.BaseNotDataActivityKt
import handbank.hbwallet.BaseViewModel

/**
 * 主播权限被关闭跳转界面
 */

class CaptureLimitsColseActivity : BaseNotDataActivityKt<BaseViewModel>() {
    override fun onReloadData() {
    }

    override val loadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.activity_capture_close_layuout
    override fun initView() {
    }

    override fun initData() {
    }

}