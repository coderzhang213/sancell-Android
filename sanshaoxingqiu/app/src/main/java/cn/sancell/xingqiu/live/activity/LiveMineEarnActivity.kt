package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.ProfileRes
import cn.sancell.xingqiu.live.adapter.UserEarnAdapter
import cn.sancell.xingqiu.util.PriceUtils
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LiveMineEarnViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_user_mine_earn.*
import kotlinx.android.synthetic.main.common_recycleview.refreshLayout
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * 我的收益
 */
class LiveMineEarnActivity : BaseActivity<LiveMineEarnViewModel>() {


    private var mPage = 1
    var mAdapter: UserEarnAdapter? = null
    private var mData: MutableList<ProfileRes.ProfileBean> = ArrayList()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LiveMineEarnActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_user_mine_earn

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        tv_title.text = "我的收益"
        btn_back.setOnClickListener { finish() }
        rv_earn.layoutManager = LinearLayoutManager(this)
        initRefresh()

    }

    override fun providerVMClass(): Class<LiveMineEarnViewModel>? {
        return LiveMineEarnViewModel::class.java
    }

    override fun initData() {
        getData()
    }

    private fun initRefresh() {

        refreshLayout.setOnRefreshListener {
            mPage = 1
            getData()
            refreshLayout.finishRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            mPage++
            getData()
            refreshLayout.finishLoadMore()
        }
    }

    fun getData() {
        mViewModel.getProfile(mPage)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mProfileData.observe(this@LiveMineEarnActivity, Observer {
                setAdapter(it)

            })
            errMsg.observe(this@LiveMineEarnActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
        }
    }


    fun setAdapter(data: ProfileRes) {
        if (mPage == 1) {
            mData.clear()
            tv_total.text = PriceUtils.getInstance().getPriceWithSypAndSpace(data.sumMoney)
        }
        if (data.dataList != null && data.dataList.isNotEmpty()) {
            showNoEarn(false)
            mData.addAll(data.dataList)
        } else {
            if (mPage == 1) {
                showNoEarn(true)
            }
        }
        if (mAdapter == null) {
            mAdapter = UserEarnAdapter(mData)
            rv_earn.adapter = mAdapter
        } else {
            mAdapter!!.setNewData(mData)
        }

        if (mData.size < data.dataCount) {
            refreshLayout.setEnableLoadMore(true)
        } else {
            refreshLayout.setEnableLoadMore(false)
        }

    }

    private fun showNoEarn(show:Boolean){
        if (show){
            tv_noEarn.visibility = View.VISIBLE
            rv_earn.visibility = View.GONE
        }else{
            tv_noEarn.visibility = View.GONE
            rv_earn.visibility = View.VISIBLE
        }
    }
}