package cn.sancell.xingqiu.im.activity

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.im.fragment.ChatGroupBaseFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.viewmodel.ChatGroupViewModel

/**
 * Created by zj on 2019/12/25.
 */
class ChatGroupActivity : DefaultFragmetnAttachActivity<ChatGroupViewModel>() {
    override val loadFragment: Fragment?
        get() = ChatGroupBaseFragment()

    override fun initData() {
    }
}