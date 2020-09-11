package cn.sancell.xingqiu.mall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.TabDb
import cn.sancell.xingqiu.bean.TabObject
import cn.sancell.xingqiu.homeclassify.HomeClassifyFragment
import cn.sancell.xingqiu.mall.fragment.MallHomeFragment
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.MarketViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_home_tab_layout.homebtn_tab_cart
import kotlinx.android.synthetic.main.activity_home_tab_layout.homebtn_tab_home
import kotlinx.android.synthetic.main.activity_home_tab_layout.iv_tab_home
import kotlinx.android.synthetic.main.activity_home_tab_layout.tv_tab_home
import kotlinx.android.synthetic.main.activity_market_tab_layout.*

/**
 * Created by zj on 2019/12/26.
 */
class MarketTabsActivity : BaseActivity<MarketViewModel>() {
    var lastFragment: Fragment? = null
    val mfragments = ArrayList<Fragment>()
    val FRAGMENT_CONTAINER_ID = R.id.container
    val mTabList = ArrayList<TabObject>()

    override fun getLayoutResId(): Int = R.layout.activity_market_tab_layout

    override fun initView() {
        homebtn_tab_home.setOnClickListener(onClickLinsenr)
        home_class.setOnClickListener(onClickLinsenr)
        homebtn_tab_cart.setOnClickListener(onClickLinsenr)
        initFragment()

    }

    companion object {
        fun startIntent(content: Context, module: String) {
            val intent = Intent(content, MarketTabsActivity::class.java)
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

    override fun initData() {
        whileFragment(TabDb.TAB_HOME)
    }

    fun initFragment() {
        // View.inflate(this,R.layout.view_tab_item_layout,null)
        mTabList.add(TabObject(iv_tab_home, tv_tab_home, TabDb.TAB_HOME, MallHomeFragment()))
        mTabList.add(TabObject(iv_tab_class, tv_tab_class, TabDb.TAB_CLASS, HomeClassifyFragment()))
        //  mTabList.add(TabObject(iv_tab_cart, tv_tab_cart, TabDb.TAB_CART, HomeShoppingCarFragment()))
    }

    fun whileFragment(mTag: String) {
        val mFragment = getFragemtn(mTag)
        mFragment?.apply {
            showFragment(this)
        }
        showTabStates(mTag)
    }

    fun getFragemtn(tag: String): Fragment? {
        for (mTab in mTabList) {
            if (tag.equals(mTab.tag)) {
                return mTab.fragemtn
            }
        }
        return null
    }

    fun showTabStates(tag: String) {
        for (mTab in mTabList) {
            if (tag.equals(mTab.tag)) {
                mTab.ivImg.isSelected = true
                mTab.tvText.isSelected = true
            } else {
                mTab.ivImg.isSelected = false
                mTab.tvText.isSelected = false
            }
        }
    }

    fun showFragment(mShowFragment: Fragment) {
        if (!mShowFragment.isVisible) {
            removeAllTopFragment()
            val mFragmentTransaction = supportFragmentManager.beginTransaction()
            if (!mfragments.contains(mShowFragment)) {
                mfragments.add(mShowFragment)
                mFragmentTransaction.add(FRAGMENT_CONTAINER_ID, mShowFragment)
            }
            lastFragment?.apply {
                mFragmentTransaction.hide(this)
            }

            mFragmentTransaction.show(mShowFragment)
            mFragmentTransaction.commitAllowingStateLoss()
            lastFragment = mShowFragment
        }
    }

    /**
     *
     */
    fun removeAllTopFragment() {
        val count = supportFragmentManager.backStackEntryCount
        for (index in 1..count) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private val onClickLinsenr = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.homebtn_tab_home -> {//首页
                    whileFragment(TabDb.TAB_HOME)
                }
                R.id.home_class -> {//分类
                    whileFragment(TabDb.TAB_CLASS)
                }
                R.id.homebtn_tab_cart -> {//购物车
                    whileFragment(TabDb.TAB_CART)
                }
            }

        }
    }

}