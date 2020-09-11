package cn.sancell.xingqiu.live.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.bean.LiveParFollowInfo
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity
import cn.sancell.xingqiu.homecommunity.live.adapter.HomeLiveRecommendAdapter
import cn.sancell.xingqiu.interfaces.OnLiveFowlloClcikLInsenr
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveOtherInfoActivity
import cn.sancell.xingqiu.viewmodel.LiveUserViewModel
import handbank.hbwallet.BaseFragment
import kotlinx.android.synthetic.main.fgm_user_info_page.*

class LiveUserInfoPageFgm : BaseFragment<LiveUserViewModel>() {

    private var mType = 0
    private var mPage: Int = 1
    private var mUserId: String? = ""
    private var mAdapter: HomeLiveRecommendAdapter? = null
    private var mLiveList: MutableList<LiveFollowInfo> = ArrayList()
    private var mListenr:OnFreshListener? = null
    private var mFreshTag = false

    companion object {
        fun newInstance(type: Int, userId: String) = LiveUserInfoPageFgm().apply {
            arguments = Bundle(2).apply {
                putInt(UiHelper.FRAGMENT_TYPE, type)
                putString(UiHelper.LIVE_USER_ID, userId)
            }
        }
    }

    override fun providerVMClass(): Class<LiveUserViewModel>? {
        return LiveUserViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fgm_user_info_page

    override fun initView() {
        rv_info.layoutManager = LinearLayoutManager(context!!)
        initFresh()
    }


    fun refresh(){
        mPage = 1
        getData()
    }

    private fun initFresh() {
        mFresh.setEnableRefresh(false)
        mFresh.setOnLoadMoreListener {
            mPage++
            getData()
            mFresh.finishLoadMore()
        }
    }

    fun getData() {
        if (mType == 0) {
            mViewModel.getLiveList(mUserId!!, mPage)
        } else {
            mViewModel.getUserLikeVideo(mUserId!!,mPage)
        }
    }

    override fun initData() {
        arguments.let {
            mType = it!!.getInt(UiHelper.FRAGMENT_TYPE, 0)
            mUserId = it.getString(UiHelper.LIVE_USER_ID)
        }

        getData()
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mLiveDataList.observe(this@LiveUserInfoPageFgm, Observer {
                setLiveAdapter(it)
            })
            mUserLikeVideo.observe(this@LiveUserInfoPageFgm, Observer {
                setLiveAdapter(it)
            })
            mException.observe(this@LiveUserInfoPageFgm, Observer {
                Log.i("===page==",it.toString())
            })
            errMsg.observe(this@LiveUserInfoPageFgm, Observer {
                SCApp.getInstance().showSystemCenterToast(it)
            })
        }
    }

    private fun setLiveAdapter(data: LiveParFollowInfo) {

        if (mPage == 1) {
            mLiveList.clear()
            if (data.dataList.isNotEmpty()) {
                setEmptyView(false)
                mLiveList.addAll(data.dataList)
            } else {
                setEmptyView(true)
            }
        } else {
            if (data.dataList.isNotEmpty()) {
                mLiveList.addAll(data.dataList)
            }
        }

        if (mAdapter == null) {
            mAdapter = HomeLiveRecommendAdapter(mLiveList, object : OnLiveFowlloClcikLInsenr {
                override fun onItemClickLinsener(data: LiveFollowInfo) {
                    data.apply {
                        if (mType == 0){
                            LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.USER_CANER_TO_TYPE.type, batchId, mUserId!!)
                        }else{
                            LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.AWESOME_TYPE.type, batchId, mUserId!!)
                        }
                    }
                }

                override fun onMakeLinser(type: String, data: LiveFollowInfo) {
                    mViewModel.liveAppointment(type, data.batchId).observe(this@LiveUserInfoPageFgm, Observer {
                        if (type == "1") {
                            SCApp.getInstance().showSystemCenterToast("设置成功\n" +
                                    "主播开播我们会提醒您")
                        } else {
                            SCApp.getInstance().showSystemCenterToast("您已取消预约")
                        }
                    })
                }

            })
            rv_info.adapter = mAdapter
        } else {
            mAdapter!!.setNewData(mLiveList)
        }
        if (data.dataCount > mLiveList.size) {
            mFresh.setEnableLoadMore(true)
        } else {
            mFresh.setEnableLoadMore(false)
        }
    }


    private fun setEmptyView(show: Boolean) {
        if (show) {
            rv_info.visibility = View.GONE
            if (mType == 0){
                if (activity is LiveOtherInfoActivity){
                    empty_page_view.visibility = View.VISIBLE
                    empty_page_view.showImg(true)
                    empty_page_view.showLive(false)
                }else{
                    empty_page_view.visibility = View.GONE
                    if (mListenr != null){
                        mListenr!!.showEmpty(true)
                    }
                }
            }else{
                tv_noLike.visibility = View.VISIBLE
            }
        } else {
            rv_info.visibility = View.VISIBLE
            empty_page_view.visibility = View.GONE
            if (mType == 0){
                empty_page_view.visibility = View.GONE
                empty_page_view.cancelAnim()
            }else{
                tv_noLike.visibility = View.GONE
            }
        }
    }

    fun setOnFreshListener(listenr:OnFreshListener){
        mListenr = listenr
    }
     interface OnFreshListener{
         fun showEmpty(show: Boolean)

     }


}