package cn.sancell.xingqiu.mall

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.mall.fragment.MallListFragment
import cn.sancell.xingqiu.viewmodel.MarketViewModel

/**
 * Created by zj on 2019/12/30.
 */
class MallListActivity : DefaultFragmetnAttachActivity<MarketViewModel>() {
    companion object {
        fun startInenet(content: Context, module: String, title: String) {
            val inent = Intent(content, MallListActivity::class.java)
            inent.putExtra("module", module)
            inent.putExtra("title", title)
            content.startActivity(inent)
        }
    }

    override val loadFragment: Fragment?
        get() = MallListFragment()

    override fun initData() {
    }
}