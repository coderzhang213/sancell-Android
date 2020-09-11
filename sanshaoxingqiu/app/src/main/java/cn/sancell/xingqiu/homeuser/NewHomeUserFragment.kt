package cn.sancell.xingqiu.homeuser

import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2020/6/15.
 */
class NewHomeUserFragment : BaseNotDataFragmentKt<BaseViewModel>() {
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() =false

    override fun getLayoutResId(): Int = R.layout.fragment_new_home_user_layout
    override fun initView() {
        setSerHeight()
    }

    override fun initData() {
    }
}