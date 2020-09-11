package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.im.entity.TabEntity
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter
import cn.sancell.xingqiu.live.fragment.FocusListFragment
import cn.sancell.xingqiu.live.listener.OnFocusListener
import cn.sancell.xingqiu.util.FansUtils
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.UserFocusViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_focus_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*


/**
 * 用户关注信息
 */
class UserFocusActivity : BaseActivity<UserFocusViewModel>() {

    val tabs = ArrayList<TabEntity>()
    private val mFragments = ArrayList<Fragment>()
    var mAdapter: NormalPageAdapter? = null

    companion object {
        fun start(context: Context, tabs: Int, userId: String, userName: String) {
            val intent = Intent(context, UserFocusActivity::class.java)
            intent.putExtra(UiHelper.FRAGMENT_TYPE, tabs)
            intent.putExtra(UiHelper.LIVE_USER_ID, userId)
            intent.putExtra(UiHelper.LIVE_USER_NAME, userName)
            context.startActivity(intent)
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_focus_list

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        btn_back.setOnClickListener {
            finish()
        }
    }

    override fun providerVMClass(): Class<UserFocusViewModel>? {
        return UserFocusViewModel::class.java
    }

    override fun initData() {
        val selectTab = intent.getIntExtra(UiHelper.FRAGMENT_TYPE, 0)
        val userId = intent.getStringExtra(UiHelper.LIVE_USER_ID)
        tv_title.text = intent.getStringExtra(UiHelper.LIVE_USER_NAME)
        tabs.add(TabEntity("关注", 0))
        tabs.add(TabEntity("粉丝", 1))

        val focusFgm = FocusListFragment.newInstance(0, userId)
        focusFgm.setFocusListener(object : OnFocusListener {
            override fun updateFansCount(num: Int) {
                changeTitle(1, num)
            }

            override fun updateFollowCount(num: Int) {
                changeTitle(0, num)
            }


        })
        mFragments.add(focusFgm)

        val fansFgm = FocusListFragment.newInstance(1, userId)
        fansFgm.setFocusListener(object : OnFocusListener {
            override fun updateFollowCount(num: Int) {
                changeTitle(0, num)
            }

            override fun updateFansCount(num: Int) {
                changeTitle(1, num)
            }

        })
        mFragments.add(fansFgm)

        mAdapter = NormalPageAdapter(supportFragmentManager, mFragments, tabs)
        vp_focus.adapter = mAdapter
        vp_focus.currentItem = selectTab

    }

    fun changeTitle(position: Int, num: Int) {
        if (mAdapter != null) {
            if (position == 0) {
                mAdapter!!.setPageTitle(position, FansUtils.getFansOrFocusStrByList("关注", num))

            } else {
                mAdapter!!.setPageTitle(position, FansUtils.getFansOrFocusStrByList("粉丝", num))

            }

            mAdapter!!.notifyDataSetChanged()
        }
    }

}