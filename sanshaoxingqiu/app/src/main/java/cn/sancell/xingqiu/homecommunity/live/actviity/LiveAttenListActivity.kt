package cn.sancell.xingqiu.homecommunity.live.actviity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homecommunity.live.fragment.LiveAttenListFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import handbank.hbwallet.BaseViewModel

/**
 * Created by zj on 2019/12/23.
 */
class LiveAttenListActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {
    override val loadFragment: Fragment?
        get() = LiveAttenListFragment()//To change initializer of created properties use File | Settings | File Templates.

    override fun initData() {
    }

}