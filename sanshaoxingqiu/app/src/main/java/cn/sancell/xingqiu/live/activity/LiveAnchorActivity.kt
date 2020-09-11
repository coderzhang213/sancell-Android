package cn.sancell.xingqiu.live.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.live.fragment.LiveAnchorFragment
import cn.sancell.xingqiu.viewmodel.LiveViewModel

class LiveAnchorActivity : DefaultFragmetnAttachActivity<LiveViewModel>() {
    override val loadFragment: Fragment?
        get() = LiveAnchorFragment()

    override fun initData() {
    }
}