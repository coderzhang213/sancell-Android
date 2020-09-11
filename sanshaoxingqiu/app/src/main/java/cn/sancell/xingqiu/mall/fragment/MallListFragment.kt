package cn.sancell.xingqiu.mall.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ComInfoList
import cn.sancell.xingqiu.bean.ComItenInfo
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.mall.adapter.ComListAdapter
import cn.sancell.xingqiu.viewmodel.MarketViewModel
import cn.sancell.xingqiu.widget.decoration.ComSpacesDecoration
import com.facebook.common.internal.Objects
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.fragment_add_com_list_layout.*
import kotlinx.android.synthetic.main.fragment_mall_list_layout.*
import kotlinx.android.synthetic.main.fragment_mall_list_layout.refreshLayout
import kotlinx.android.synthetic.main.fragment_mall_list_layout.rl_com_list

/**
 * Created by zj on 2019/12/30.
 */
class MallListFragment : BaseNotDataFragmentKt<MarketViewModel>() {
    var page: Int = 1
    var mComListAdapter: ComListAdapter? = null
    protected val mAllList: MutableList<ComItenInfo> = ArrayList()
    var module: String? = null
    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<MarketViewModel>? {
        return MarketViewModel::class.java
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.fragment_mall_list_layout
    override fun initView() {
        val title = activity?.intent?.getStringExtra("title")
        title?.apply {
            setTitleName(title)
        }
        refreshLayout.setOnRefreshListener {
            page = 1
            getData()

        }
        refreshLayout.setOnLoadMoreListener {

            page++
            getData()
        }

    }

    fun getData() {
        module?.apply {
            mViewModel.getMallInfoList(page, this)
        }

    }

    override fun initData() {
        module = activity?.intent?.getStringExtra("module")
        getData()
    }

    fun bindViewData(mComInfoList: ComInfoList) {
        if (mComInfoList.dataList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (page == 1) {//显示暂无数据
                setNotData()
            }
            return

        }
        if (page == 1) {
            mAllList.clear()
        }
        mAllList.addAll(mComInfoList.dataList)
        if (mComListAdapter == null) {
            mComListAdapter = ComListAdapter(mAllList,1)
            rl_com_list.addItemDecoration(ComSpacesDecoration())
            rl_com_list.layoutManager = GridLayoutManager(context, 2)
            rl_com_list.adapter = mComListAdapter
        } else {
            mComListAdapter?.setNewData(mAllList)
        }

    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            mComInfoListData.observe(this@MallListFragment, Observer {
                hindLoadStatus()
                it?.apply {
                    bindViewData(this)
                }
            })
            errMsg.observe(this@MallListFragment, Observer {
                hindLoadStatus()
                ToastHelper.showToast(context, it)
            })
            mException.observe(this@MallListFragment, Observer {
                hindLoadStatus()
                ToastHelper.showToast("网络异常")
            })
        }
    }
}