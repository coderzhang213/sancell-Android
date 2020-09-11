package cn.sancell.xingqiu.live.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.live.fragment.LiveAddGroupFragment
import handbank.hbwallet.BaseViewModel

/**
 * 直播设置添加群组
 */
class LiveAddGroupActivity : DefaultFragmetnAttachActivity<BaseViewModel>() {
    override val loadFragment: Fragment?
        get() = LiveAddGroupFragment()

    override fun initData() {
    }
}