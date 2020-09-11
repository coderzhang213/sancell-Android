package cn.sancell.xingqiu.live.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.FansRes
import cn.sancell.xingqiu.bean.FocusBean
import cn.sancell.xingqiu.bean.LiveFocusRes
import cn.sancell.xingqiu.bean.LiverBean
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.live.activity.LiveOtherInfoActivity
import cn.sancell.xingqiu.live.adapter.FocusListAdapter
import cn.sancell.xingqiu.live.listener.OnFocusListener
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.viewmodel.FocusListViewModel
import handbank.hbwallet.BaseFragment
import kotlinx.android.synthetic.main.common_recycleview.refreshLayout
import kotlinx.android.synthetic.main.common_recycleview.rv_common
import kotlinx.android.synthetic.main.fragment_live_follow.*

/**
 * 关注/粉丝列表
 */
class FocusListFragment : BaseFragment<FocusListViewModel>() {

    private var mListener: OnFocusListener? = null
    private var mType: Int = 0  // 0 fans 1 focus
    private var mPage: Int = 1
    private var mUserId: String? = ""
    private val mDataList: MutableList<FocusBean> = ArrayList()
    private var mAdapter: FocusListAdapter? = null

    private var mDataCount: Int = 0 //人数
    private var mSelectItemPosition = 0 //选中的item
    private var isMine = false

    companion object {
        fun newInstance(type: Int, userId: String) = FocusListFragment().apply {
            arguments = Bundle(2).apply {
                putInt(UiHelper.FRAGMENT_TYPE, type)
                putString(UiHelper.LIVE_USER_ID, userId)
            }
        }
    }

    override fun providerVMClass(): Class<FocusListViewModel>? {
        return FocusListViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fragment_live_follow

    override fun initView() {
        rv_common.layoutManager = LinearLayoutManager(context)
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

    override fun initData() {
        arguments.let {
            mType = it!!.getInt(UiHelper.FRAGMENT_TYPE, 0)
            mUserId = it.getString(UiHelper.LIVE_USER_ID)
        }
        getData()
    }

    fun getData() {
        isMine = AppUtils.getUserId() == mUserId
        if (mType == 0) {
            mViewModel.getFollowData(mUserId!!, mPage)
        } else {
            mViewModel.getFansData(mUserId!!, mPage)
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mFollowData.observe(this@FocusListFragment, Observer {
                setViewData(it)
            })
            mFanData.observe(this@FocusListFragment, Observer {
                setViewData(it)
            })

            mFocusData.observe(this@FocusListFragment, Observer {
                setFocusChange(it)
            })
            errMsg.observe(this@FocusListFragment, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
        }
    }

    private fun setViewData(data: FansRes) {
        mDataCount = data.dataCount
        setTabCount()
        if (mPage == 1) {
            if (data.dataList != null && data.dataList.size > 0) {
                setEmptyView(false)
                mDataList.clear()
                mDataList.addAll(data.dataList)
                if (mAdapter == null) {
                    mAdapter = FocusListAdapter(mDataList)
                    mAdapter!!.setFocusListener(object : FocusListAdapter.OnFocusListener {
                        override fun onFocus(position: Int, data: FocusBean) {
                            mSelectItemPosition = position
                            if (data.followStatus == 0){
                                mViewModel.focusLiver(data.tvUserId, 1)
                            }else{
                                mViewModel.focusLiver(data.tvUserId, 2)
                            }
                        }

                        override fun onItemClick(data: FocusBean) {
                            LiveOtherInfoActivity.start(context!!, data.tvUserId)
                        }

                    })
                    rv_common.adapter = mAdapter
                } else {
                    mAdapter!!.setNewData(mDataList)
                }

            } else {
                setEmptyView(true)
            }

        } else {
            if (data.dataList != null && data.dataList.size > 0) {
                mDataList.addAll(data.dataList)
            }
            mAdapter!!.setNewData(mDataList)
        }

        if (mDataList.size < data.dataCount) {
            refreshLayout.setEnableLoadMore(true)
        } else {
            refreshLayout.setEnableLoadMore(false)
        }
    }

    private fun setFocusChange(it: LiveFocusRes) {
        when {
            it.mutual == 0 -> {
                mDataCount--
                mDataList[mSelectItemPosition].followStatus = 0
            }
            it.mutual == 1 -> {
                //互关
                mDataCount++
                mDataList[mSelectItemPosition].followStatus = 2
            }
            it.mutual == 2 -> {
                //单关
                mDataCount++
                mDataList[mSelectItemPosition].followStatus = 1

            }
        }
        mAdapter!!.notifyItemChanged(mSelectItemPosition)
        if (mType == 0 && isMine){ //只有看我自己时候才会更新关注
            setTabCount()
        }
    }

    private fun setTabCount() {
        if (mListener != null) {
            if (mType == 0) {
                mListener!!.updateFollowCount(mDataCount)
            } else {
                mListener!!.updateFansCount(mDataCount)
            }
        }
    }


    fun setFocusListener(listener: OnFocusListener) {
        mListener = listener
    }

    private fun setEmptyView(show: Boolean) {
        if (show) {
            rv_common.visibility = View.GONE
            view_common_empty.visibility = View.VISIBLE
            view_common_empty.setEmptyImg(resources.getDrawable(R.mipmap.icon_empty_fans, null))
            if (mType == 0) {
                view_common_empty.setEmptyDesc("您还没有关注任何人")
            } else {
                view_common_empty.setEmptyDesc("您还没有粉丝")
            }
        } else {
            rv_common.visibility = View.VISIBLE
            view_common_empty.visibility = View.GONE
        }
    }


}