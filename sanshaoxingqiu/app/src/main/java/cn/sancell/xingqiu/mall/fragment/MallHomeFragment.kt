package cn.sancell.xingqiu.mall.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CommdoityData
import cn.sancell.xingqiu.bean.CommdoityResData
import cn.sancell.xingqiu.interfaces.OnBackPressedLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.mall.activity.HomeClassifyActivity
import cn.sancell.xingqiu.mall.adapter.CommodityAdapter
import cn.sancell.xingqiu.mall.help.BannerHelp
import cn.sancell.xingqiu.mall.help.HomeToolsHelp
import cn.sancell.xingqiu.util.BackPressedUtils
import cn.sancell.xingqiu.viewmodel.MarketViewModel
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.frament_mall_home_layout.*
import kotlinx.android.synthetic.main.view_search_layout.*

/**
 * Created by zj on 2019/12/27.
 */
class MallHomeFragment : BaseNotDataFragmentKt<MarketViewModel>(), OnBackPressedLinsener {
    var mBannerHelp: BannerHelp? = null
    var page: Int = 1
    private var mCommodityAdapter: CommodityAdapter? = null
    protected val mAllList: MutableList<CommdoityData> = ArrayList()
    var module: String? = ""
    var isShowBack = true

    //当前界面上方可见
    var mCurrPagerShow = true
    override fun onReloadData() {
        getData()
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.frament_mall_home_layout

    companion object {
        fun newInsener(type: String, isShowBack: Boolean): MallHomeFragment {
            val mMallHomeFragment = MallHomeFragment()
            val mBundle = Bundle()
            mBundle.putString("module", type)
            mBundle.putBoolean("isShowBack", isShowBack)
            mMallHomeFragment.arguments = mBundle
            return mMallHomeFragment
        }
    }

    override fun initView() {
        module = arguments?.getString("module")
        isShowBack = arguments?.getBoolean("isShowBack", true)!!
        if (isShowBack) {
            iv_back.visibility = View.VISIBLE
        } else {
            iv_back.visibility = View.GONE
        }
        val mHomeToolsHelp = HomeToolsHelp(this)
        mHomeToolsHelp.setMoudleId(module!!)
        mHomeToolsHelp.setSearchText("请输入搜索内容")
        mBannerHelp = BannerHelp(this, loop_banner, true)

        refreshLayout.setOnRefreshListener {
            page = 1
            getData()

        }
        refreshLayout.setOnLoadMoreListener {
            page++
            getData()

        }
        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
        iv_class.setOnClickListener {
            HomeClassifyActivity.startIntent(context!!, module!!)
        }

    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    override fun providerVMClass(): Class<MarketViewModel>? {
        return MarketViewModel::class.java
    }

    override fun initData() {

        setHeadBg()
        BackPressedUtils.bindOnBack(activity, this)
        mViewModel.getBannerData(module.toString())
        showLoadData()
        getData()
    }

    /**
     * 不同模块设置不同的背景图
     */
    fun setHeadBg() {
        when (module) {
            "1" -> {//商城
                iv_top_bg.setBackgroundResource(R.mipmap.mall_bg)
            }
            "6" -> {//整形
                iv_top_bg.setBackgroundResource(R.mipmap.plastic_icon)
            }
            "7" -> {//美容
                iv_top_bg.setBackgroundResource(R.mipmap.beauty_icon)
            }
            "8" -> {//体检
                iv_top_bg.setBackgroundResource(R.mipmap.checkup_icon)
            }
            else -> {
                iv_top_bg.setBackgroundResource(R.mipmap.mall_bg)
            }


        }

    }

    fun getData() {
        mViewModel.getMallList(module!!, page)
    }

    override fun onDestroy() {
        super.onDestroy()
        BackPressedUtils.unBindOnBack(activity, this)
        mBannerHelp?.stopLoop()
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mBannerData.observe(this@MallHomeFragment, Observer {
                it?.apply {
                    mBannerHelp?.setLoopData(dataList)
                }
            })
            mMallListData.observe(this@MallHomeFragment, Observer {
                hindLoadStatus()
                it?.apply {

                    setViewData(this)

                }

            })
            errMsg.observe(this@MallHomeFragment, Observer {
                hindLoadStatus()
                ToastHelper.showToast(context, it)
            })
            mException.observe(this@MallHomeFragment, Observer {
                hindLoadStatus()
                ToastHelper.showToast("网络异常")
            })

        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mCurrPagerShow = !hidden

    }

    /**
     * 处理返回数据
     */
    private fun setViewData(mCommdoityData: CommdoityResData) {
        if (mCommdoityData.dataList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (page == 1) {//显示暂无数据
                // setNotData()
            }
            return

        }
        if (page == 1) {
            mAllList.clear()
        }
        mAllList.addAll(mCommdoityData.dataList)
        if (mCommodityAdapter == null) {
            mCommodityAdapter = CommodityAdapter(mAllList)
            rl_mall_list.layoutManager = LinearLayoutManager(context)
            rl_mall_list.adapter = mCommodityAdapter
        } else {
            mCommodityAdapter?.setNewData(mAllList)
        }

    }

    override fun onBackPressedLinsener(): Boolean {
        if (mCurrPagerShow) {
            activity?.finish()
        }

        return mCurrPagerShow
    }
}