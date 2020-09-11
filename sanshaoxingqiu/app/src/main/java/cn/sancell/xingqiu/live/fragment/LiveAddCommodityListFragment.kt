package cn.sancell.xingqiu.live.fragment

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ComItenInfo
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.activity.LiveAddCommodityActivity
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.mall.adapter.ComListAdapter
import cn.sancell.xingqiu.util.ScreenUtils
import cn.sancell.xingqiu.widget.decoration.ComSpacesDecoration
import cn.sancell.xingqiu.viewmodel.CommodityViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_add_com_list_layout.*
import kotlinx.android.synthetic.main.fragment_add_com_list_layout.refreshLayout
import kotlinx.android.synthetic.main.fragment_add_com_list_layout.rl_com_list

class LiveAddCommodityListFragment : BaseNotDataFragmentKt<CommodityViewModel>() {
    var mComListAdapter: ComListAdapter? = null
    protected val mAllList: MutableList<ComItenInfo> = ArrayList()
    var page: Int = 1
    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = false
    override val isShowTitle: Boolean
        get() = false

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            mComListData.observe(this@LiveAddCommodityListFragment, Observer {
                hindLoadStatus()
                bingView(it)
            })
        }
    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishLoadMore()
        refreshLayout.finishRefresh()

    }

    fun bingView(mInfo: HomePageLikeListDataBean.LikeListDataBean?) {
        if (mInfo == null || mInfo.dataList == null || mInfo.dataList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (page == 1) {
                rl_not_data.visibility = View.VISIBLE
            }

            return
        }
        if (rl_not_data.visibility == View.VISIBLE && page == 1) {
            rl_not_data.visibility = View.GONE
        }


        mInfo.dataList?.apply {
            if (page == 1) {
                mAllList.clear()
            }

            for (info in this) {
                mAllList.add(ComItenInfo(info.id.toString(), info.skuId, info.sort, info.title, info.userRealPriceE2, info.sellingPriceE2, info.coverPic))
            }
            if (mAllList.size > 0) {
                if (mComListAdapter == null) {
                    mComListAdapter = ComListAdapter(mAllList, 2)
                    mComListAdapter?.setOnItemClickListener(mOnItemClickListener)
                    rl_com_list.layoutManager = GridLayoutManager(context, 2)
                    rl_com_list.addItemDecoration(ComSpacesDecoration())
                    rl_com_list.adapter = mComListAdapter
                } else {
                    mComListAdapter?.setNewData(mAllList)
                }
            }
        }
    }

    private val mOnItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
        override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            val mInfo = mAllList.get(position)
            val intent = Intent(context, LiveAddCommodityActivity::class.java)
            intent.putExtra("skuId", mInfo.skuId)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

    }

    override fun providerVMClass(): Class<CommodityViewModel>? {
        return CommodityViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fragment_add_com_list_layout

    override fun initView() {
        iv_back.setOnClickListener(mOnClickLinsener)
        tv_right.setOnClickListener(mOnClickLinsener)
        refreshLayout.setOnRefreshListener {
            page = 1
            getData()

        }
        refreshLayout.setOnLoadMoreListener {

            page++
            getData()
        }

        et_sor.setOnEditorActionListener(
                object : TextView.OnEditorActionListener {

                    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            comSearch()
                            return true
                        }
                        return false
                    }
                })
    }

    fun comSearch() {
        page = 1
        ScreenUtils.hideKeyboard(et_sor)
        getData()
    }

    fun getData() {
        mViewModel.getThirdClassifyListData(et_sor.text.toString().trim(), page)
    }

    override fun initData() {
    }

    private val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View) {
            when (v.id) {
                R.id.iv_back -> {
                    activity?.onBackPressed()
                }
                R.id.tv_right -> {//搜索
                    comSearch()
                }
            }
        }
    }
}