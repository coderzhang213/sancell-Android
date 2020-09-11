package cn.sancell.xingqiu.live.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.live.fragment.LiveAddCommodityListFragment
import handbank.hbwallet.BaseViewModel

class LiveAddCommodityListAcitivy: DefaultFragmetnAttachActivity<BaseViewModel>()  {
    override val loadFragment: Fragment?
        get() = LiveAddCommodityListFragment()

    override fun initData() {
    }
}