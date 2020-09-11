package cn.sancell.xingqiu.mall.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.mall.fragment.StayTunedFragment
import cn.sancell.xingqiu.viewmodel.MarketViewModel

/**
 * Created by zj on 2020/1/6.
 */
class StayTunedActivity : DefaultFragmetnAttachActivity<MarketViewModel>() {
    companion object {
        fun startIntent(content: Context, title: String) {
            val intent = Intent(content, StayTunedActivity::class.java)
            intent.putExtra("title", title)
            content.startActivity(intent)

        }
    }

    override val loadFragment: Fragment?
        get() = StayTunedFragment()

    override fun initData() {
    }
}