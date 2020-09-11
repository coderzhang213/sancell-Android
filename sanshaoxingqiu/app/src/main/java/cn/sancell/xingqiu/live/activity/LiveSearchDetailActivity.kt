package cn.sancell.xingqiu.live.activity

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.live.adapter.FocusAdapter
import cn.sancell.xingqiu.bean.LiveSearchRes
import cn.sancell.xingqiu.bean.LiverBean
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity
import cn.sancell.xingqiu.homecommunity.live.adapter.HomeLiveRecommendAdapter
import cn.sancell.xingqiu.interfaces.OnLiveFowlloClcikLInsenr
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.util.StatusBarUtil
import cn.sancell.xingqiu.viewmodel.LiveSearchDetailModel
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activity_live_search_detail.*
import kotlinx.android.synthetic.main.include_search_bar.*

/**
 * 搜索详情页
 */
class LiveSearchDetailActivity : BaseActivity<LiveSearchDetailModel>() {


    private var mFocusAdapter: FocusAdapter? = null
    private var mLiveAdapter: HomeLiveRecommendAdapter? = null

    private var mSearchLabel: String = ""
    private var mPage = 1
    private val mLiveDataList: MutableList<LiveFollowInfo> = ArrayList()
    private val mUserDataList: MutableList<LiverBean> = ArrayList()

    private var mFocusPosition = 0

    companion object {
        fun start(context: Context, label: String) {
            val intent = Intent(context, LiveSearchDetailActivity::class.java)
            intent.putExtra(UiHelper.SEARCH_LABEL, label)
            context.startActivity(intent)
        }
    }

    override fun providerVMClass(): Class<LiveSearchDetailModel>? {
        return LiveSearchDetailModel::class.java
    }


    override fun getLayoutResId(): Int = R.layout.activity_live_search_detail

    override fun initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        tv_total.setOnClickListener {
            LiveSearchUserActivity.start(this, mSearchLabel)
        }
        tv_cancel.setOnClickListener { finish() }
        et_search.hint = "请输入主播昵称/主播id/直播标题搜索"
        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == 0 || actionId == 3) {
                    event.let {
                        if (et_search.text.toString().isNotEmpty()) {
                            mSearchLabel = et_search.text.toString()
                            mPage = 1
                            getData()
                        }
                        return true
                    }
                }
                return false
            }
        })
        rv_focus.layoutManager = LinearLayoutManager(this)
        rv_living.layoutManager = LinearLayoutManager(this)

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

    private fun getData() {
        mViewModel.searchValue(mSearchLabel, mPage, 0)
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mSearchData.observe(this@LiveSearchDetailActivity, Observer {
                setSearchData(it)
            })
            mSetFocusState.observe(this@LiveSearchDetailActivity, Observer {
                when {
                    it.mutual == 0 -> {
                        mUserDataList[mFocusPosition].isFollow = 0
                        SCApp.getInstance().showSystemCenterToast("已取消关注")
                    }
                    it.mutual == 1 -> {
                        mUserDataList[mFocusPosition].isFollow = 2
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                    else -> {
                        mUserDataList[mFocusPosition].isFollow = 1
                        SCApp.getInstance().showSystemCenterToast("已关注")
                    }
                }
                mFocusAdapter!!.notifyItemChanged(mFocusPosition)

            })
            errMsg.observe(this@LiveSearchDetailActivity, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
        }
    }

    private fun setSearchData(data: LiveSearchRes) {
        if (mPage == 1) { //第一页含主播
            if ((data.tvUserList == null || data.tvUserList.dataList.isEmpty()) && (data.lives == null || data.lives.dataList.isEmpty())) {
                setEmptyView(true)
            } else {
                setEmptyView(false)
                //主播
                if (data.tvUserList != null && data.tvUserList.dataList.size > 0) {
                    if (data.tvUserList.dataList.size >= 3){
                        tv_total.visibility = View.VISIBLE
                    }else{
                        tv_total.visibility = View.GONE
                    }
                    rv_focus.visibility = View.VISIBLE
                    tv_no_liver.visibility = View.GONE
                    mUserDataList.clear()
                    mUserDataList.addAll(data.tvUserList.dataList)
                    if (mFocusAdapter == null) {

                        mFocusAdapter = FocusAdapter(mUserDataList)
                        mFocusAdapter!!.setLimitDisplayCount(3)
                        mFocusAdapter!!.setFocusListener(object : FocusAdapter.OnFocusListener {
                            override fun onItemClick(data: LiverBean) {
                                LiveOtherInfoActivity.start(this@LiveSearchDetailActivity, data.tvUserId)
                            }

                            override fun onFocus(position: Int, data: LiverBean) {
                                //关注
                                mFocusPosition = position
                                if (data.isFollow == 0) {
                                    mViewModel.setFocusLiver(data.tvUserId, 1)
                                } else {
                                    mViewModel.setFocusLiver(data.tvUserId, 2)
                                }
                            }
                        })
                        rv_focus.adapter = mFocusAdapter
                    } else {
                        mFocusAdapter!!.setNewData(data.tvUserList.dataList)
                    }
                } else {
                    tv_total.visibility = View.GONE
                    rv_focus.visibility = View.GONE
                    tv_no_liver.visibility = View.VISIBLE
                }
                //直播
                if (data.lives != null && data.lives.dataList.isNotEmpty()) {
                    mLiveDataList.clear()
                    mLiveDataList.addAll(data.lives.dataList)
                    tv_no_living.visibility = View.GONE
                    rv_living.visibility = View.VISIBLE
                    setLivingAdapter()
                } else {
                    rv_living.visibility = View.GONE
                    tv_no_living.visibility = View.VISIBLE
                }
            }
        } else {
            //其他页不含主播
            if (data.lives != null && data.lives.dataList.isNotEmpty()) {
                mLiveDataList.addAll(data.lives.dataList)
                setLivingAdapter()
            }
        }

        if (data.lives.dataCount > mLiveDataList.size) {
            refreshLayout.setEnableLoadMore(true)
        } else {
            refreshLayout.setEnableLoadMore(false)
        }

    }

    private fun setLivingAdapter() {
        if (mLiveAdapter == null) {
            mLiveAdapter = HomeLiveRecommendAdapter(mLiveDataList, object : OnLiveFowlloClcikLInsenr {
                override fun onItemClickLinsener(data: LiveFollowInfo) {
                    data.apply {
                        LivePlayBaseHoemActivity.startIntent(this@LiveSearchDetailActivity, LivePlayType.SERARCH_TYPE.type, batchId)
                    }
                }

                override fun onMakeLinser(type: String, data: LiveFollowInfo) {

                    mViewModel.liveAppointment(type, data.batchId).observe(this@LiveSearchDetailActivity, Observer {
                        if (type == "1") {
                            SCApp.getInstance().showSystemCenterToast("设置成功\n" +
                                    "主播开播我们会提醒您")
                        } else {
                            SCApp.getInstance().showSystemCenterToast("您已取消预约")
                        }
                    })
                }

            })
            rv_living.adapter = mLiveAdapter
        } else {
            mLiveAdapter!!.setNewData(mLiveDataList)
        }
    }


    override fun initData() {
        mSearchLabel = intent.getStringExtra(UiHelper.SEARCH_LABEL)!!
        et_search.setText(mSearchLabel)
        getData()
    }

    private fun setEmptyView(show: Boolean) {
        if (show) {
            rl_liver.visibility = View.GONE
            tv_living.visibility = View.GONE
            rv_focus.visibility = View.GONE
            rv_living.visibility = View.GONE
            tv_no_liver.visibility = View.GONE
            tv_no_living.visibility = View.GONE
            view_empty.visibility = View.VISIBLE
            view_empty.setEmptyDesc("暂未搜索到相关内容")
            view_empty.setEmptyImg(resources.getDrawable(R.mipmap.icon_empty_search))
        } else {
            rl_liver.visibility = View.VISIBLE
            tv_living.visibility = View.VISIBLE
            view_empty.visibility = View.GONE
        }

    }

}