package cn.sancell.xingqiu.mall.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homeclassify.HomeClassifyFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.MarketViewModel

/**
 * Created by zj on 2020/1/6.
 */
class HomeClassifyActivity : DefaultFragmetnAttachActivity<MarketViewModel>() {
    companion object {
        fun startIntent(content: Context, module: String) {
            val intent = Intent(content, HomeClassifyActivity::class.java)
            intent.putExtra("module", module)
            content.startActivity(intent)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }

    override val loadFragment: Fragment?
        get() = HomeClassifyFragment()

    override fun initData() {
    }
}