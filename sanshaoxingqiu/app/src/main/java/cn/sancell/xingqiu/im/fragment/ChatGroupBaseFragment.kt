package cn.sancell.xingqiu.im.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.im.entity.TabEntity
import cn.sancell.xingqiu.im.ui.addressBook.AddressBookActivity
import cn.sancell.xingqiu.im.ui.addressBook.NormalPageAdapter
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.help.MarginsUtils
import cn.sancell.xingqiu.mall.help.BannerHelp
import cn.sancell.xingqiu.util.BackPressedUtils
import cn.sancell.xingqiu.util.BannerJumpUtils
import cn.sancell.xingqiu.viewmodel.ChatGroupViewModel
import kotlinx.android.synthetic.main.fragment_chat_group_base_layout.*

/**
 * Created by zj on 2019/12/25.
 */
class ChatGroupBaseFragment : BaseNotDataFragmentKt<ChatGroupViewModel>(), OnBackPressedLinsener {
    private var mBannerBeanList: MutableList<HomeBannerDataBean.BannerBean>? = null
    var mBannerHelp: BannerHelp? = null

    //当前界面上方可见
    var mCurrPagerShow = true
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = true

    override fun providerVMClass(): Class<ChatGroupViewModel>? {
        return ChatGroupViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fragment_chat_group_base_layout

    override fun initView() {
        toolbar_navigation.setNavigationIcon(R.mipmap.icon_white_back)
        setSerHeight()
        setTitleName("社群")
        setRightIcon(R.mipmap.chat_group_icon, object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(context, AddressBookActivity::class.java))
            }
        })
        mBannerHelp = BannerHelp(this, iv_avd, true)
        val mFragmetns = ArrayList<Fragment>()
        val mTabs = ArrayList<TabEntity>()
        mFragmetns.add(RecentMessageFragment())
        mFragmetns.add(RecommendGroupFragment())
        mTabs.add(TabEntity("我的消息", 111))
        mTabs.add(TabEntity("推荐群组", 112))
        tb_video.addTab(tb_video.newTab())
        tb_video.addTab(tb_video.newTab())

        val mAdapter = NormalPageAdapter(activity!!.supportFragmentManager, mFragmetns, mTabs)
        tb_video.setupWithViewPager(vp_video)
        vp_video.adapter = mAdapter
        BackPressedUtils.bindOnBack(getActivity(), this);

    }

    override fun onDestroy() {
        super.onDestroy()
        mBannerHelp?.stopLoop()
        BackPressedUtils.unBindOnBack(getActivity(), this);

    }

    override fun initData() {
        mViewModel.getBannerData()
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mBannerData.observe(this@ChatGroupBaseFragment, Observer {

                it.dataList?.apply {
                    if (this.size > 0) {
                        iv_avd.visibility = View.VISIBLE
                        mBannerBeanList = this

                        it?.apply {
                            mBannerHelp?.setLoopData(dataList)
                        }

                    }


                }
            })
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mCurrPagerShow = !hidden

    }

    override fun onBackPressedLinsener(): Boolean {
        if (mCurrPagerShow) {
            activity?.finish()
        }

        return mCurrPagerShow
    }
}