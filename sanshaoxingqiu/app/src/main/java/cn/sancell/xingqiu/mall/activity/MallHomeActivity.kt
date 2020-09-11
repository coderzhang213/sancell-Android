package cn.sancell.xingqiu.mall.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.mall.fragment.MallHomeFragment
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.MarketViewModel

/**
 * Created by zj on 2020/1/6.
 */
class MallHomeActivity : DefaultFragmetnAttachActivity<MarketViewModel>() {
    companion object {
        fun startIntent(content: Context, module: String) {
            val intent = Intent(content, MallHomeActivity::class.java)
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

    override fun bindIntent(intent: Intent?, mLoadFragment: Fragment) {
        super.bindIntent(intent, mLoadFragment)
        val module = intent?.getStringExtra("module")
        val mBundle = Bundle()
        mBundle.putString("module", module)
        mLoadFragment.arguments = mBundle
    }

    override fun initData() {
    }

    override val loadFragment: Fragment?
        get() = MallHomeFragment()
}