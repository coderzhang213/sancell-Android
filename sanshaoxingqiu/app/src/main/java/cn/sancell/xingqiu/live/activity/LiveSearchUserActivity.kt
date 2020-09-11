package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.live.adapter.FocusAdapter
import cn.sancell.xingqiu.bean.LiveSearchRes
import cn.sancell.xingqiu.bean.LiverBean
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LiveSearchUserViewModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.activity_search_user.refreshLayout
import kotlinx.android.synthetic.main.include_search_bar.*

/**
 * 搜索的全部用户
 */
class LiveSearchUserActivity : BaseActivity<LiveSearchUserViewModel>() {

    private var mAdapter: FocusAdapter? = null
    private var mKeyWord = ""
    private var mPage: Int = 1
    private val mLiverList: MutableList<LiverBean> = ArrayList()

    private var mFocusPosition = 0

    companion object {
        fun start(context: Context, keyWord: String) {
            val intent = Intent(context, LiveSearchUserActivity::class.java)
            intent.putExtra(UiHelper.SEARCH_LABEL, keyWord)
            context.startActivity(intent)
        }
    }

    override fun providerVMClass(): Class<LiveSearchUserViewModel>? {
        return LiveSearchUserViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.activity_search_user


    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        rv_user.layoutManager = LinearLayoutManager(this)
        tv_cancel.setOnClickListener { finish() }
        et_search.hint = "请输入主播昵称/主播id/直播标题搜索"
        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == 0 || actionId == 3) {
                    event.let {
                        if (et_search.text.toString().isNotEmpty()) {
                            mKeyWord = et_search.text.toString()
                            mPage = 1
                            getData()
                        }
                        return true
                    }
                }
                return false
            }
        })
        initFreshView()
    }

    private fun initFreshView() {
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
        mViewModel.searchValue(mKeyWord, mPage, 1)
    }

    override fun initData() {
        mKeyWord = intent.getStringExtra(UiHelper.SEARCH_LABEL)
        et_search.setText(mKeyWord)
        getData()

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            mSearchData.observe(this@LiveSearchUserActivity, Observer {
                if (it.lives != null) {
                    setLiveView(it)
                }
            })
            mSetFocusState.observe(this@LiveSearchUserActivity, Observer {
                when {
                    it.mutual == 0 -> {
                        mLiverList[mFocusPosition].isFollow = 0
                        SCApp.getInstance().showSystemCenterToast("已取消关注")
                    }
                    it.mutual == 1 -> {
                        mLiverList[mFocusPosition].isFollow = 2
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                    else -> {
                        mLiverList[mFocusPosition].isFollow = 1
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                }
                mAdapter!!.notifyItemChanged(mFocusPosition)

            })

            errMsg.observe(this@LiveSearchUserActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(errMsg.value)
            })

            mException.observe(this@LiveSearchUserActivity, Observer {
                Log.i("====777 ===", mException.value.toString())
            })

        }
    }

    private fun setLiveView(data: LiveSearchRes) {

        if (mPage == 1) {
            mLiverList.clear()
        }
        if (data.tvUserList.dataList != null && data.tvUserList.dataList.size > 0) {
            mLiverList.addAll(data.tvUserList.dataList)
            if (data.tvUserList.dataCount > mLiverList.size) {
                refreshLayout.setEnableLoadMore(true)
            } else {
                refreshLayout.setEnableLoadMore(false)
            }
            if (mAdapter == null) {
                mAdapter = FocusAdapter(mLiverList)
                mAdapter!!.setFocusListener(object : FocusAdapter.OnFocusListener {
                    override fun onItemClick(data: LiverBean) {
                        LiveOtherInfoActivity.start(this@LiveSearchUserActivity, data.tvUserId)
                    }

                    override fun onFocus(position: Int, data: LiverBean) {
                        mFocusPosition = position
                        if (data.isFollow == 0) {
                            mViewModel.setFocusLiver(data.tvUserId, 1)
                        } else {
                            mViewModel.setFocusLiver(data.tvUserId, 2)
                        }
                    }

                })
                rv_user.adapter = mAdapter
            } else {
                mAdapter!!.setNewData(mLiverList)
            }
        } else {
            if (mPage == 1) {
                setEmptyView(true)
            } else {
                setEmptyView(false)
                refreshLayout.setEnableLoadMore(false)
            }
        }


    }

    private fun setEmptyView(show: Boolean) {
        if (show) {
            view_liver_empty.visibility = View.VISIBLE
            tv_head.visibility = View.GONE
            rv_user.visibility = View.GONE

        } else {
            view_liver_empty.visibility = View.GONE
            tv_head.visibility = View.VISIBLE
            rv_user.visibility = View.VISIBLE
        }

    }

}