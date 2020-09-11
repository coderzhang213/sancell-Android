package cn.sancell.xingqiu.homecommunity.live.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.bean.LiveRemmInfo
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity
import cn.sancell.xingqiu.homecommunity.live.adapter.HomeLiveRecommendAdapter
import cn.sancell.xingqiu.homecommunity.live.adapter.LiveRemAdapter
import cn.sancell.xingqiu.homecommunity.video.VideoPlayListActivity
import cn.sancell.xingqiu.im.activity.ChatGroupActivity
import cn.sancell.xingqiu.interfaces.*
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveSearchActivity
import cn.sancell.xingqiu.live.dialog.ComfirmDialog
import cn.sancell.xingqiu.mall.activity.MallHomeActivity
import cn.sancell.xingqiu.util.SeekBarUtils
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.fragment_livew_list_layout.*


/**
 * Created by zj on 2019/12/23.
 * 关注界面
 */
class LiveAttenListFragment : BaseNotDataFragmentKt<LiveViewModel>(), OnBackPressedLinsener {
    private var mCurrentPage = 1
    private val mLists = ArrayList<LiveFollowInfo>()
    private var mAdapter: HomeLiveRecommendAdapter? = null
    protected var mLiveRemAdapter: LiveRemAdapter? = null
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null

    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean = false
    override fun providerVMClass(): Class<LiveViewModel>? = LiveViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_livew_list_layout
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
        ObserverManger.getInstance(ObserverKey.LIVE_ATTEN_CLOSE).registerObserver(liveAttCloseObeser)
    }

    override fun onResume() {
        super.onResume()
        //每次都需要获取最新的数据
        mCurrentPage = 1
        getLiveList()
    }

    override fun initView() {
        // setTitleName("直播")
        //上拉
        refreshLayout.setOnLoadMoreListener({
            mCurrentPage++
            getLiveList()
        })
        //下拉
        refreshLayout.setOnRefreshListener({
            mCurrentPage = 1
            getLiveList()
        })
        iv_group.setOnClickListener {
            //群组
            startActivity(Intent(context, ChatGroupActivity::class.java))
        }
        iv_video.setOnClickListener {
            //视频
            startActivity(Intent(context, VideoPlayListActivity::class.java))
        }
        iv_haired.setOnClickListener {
            //美容
            MallHomeActivity.startIntent(context!!, "7")
        }
        ll_top_sr.findViewById<LinearLayout>(R.id.ll_top_sr).setOnClickListener {
            LiveSearchActivity.start(context!!)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindOnBack()
    }


    private val liveAttCloseObeser = OnObserver {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unBindOnBack()
        ObserverManger.getInstance(ObserverKey.LIVE_ATTEN_CLOSE).removeObserver(liveAttCloseObeser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {//刷新数据
            mCurrentPage = 1
            getLiveList()
            mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.setPlayBaseShowIndex(1)
        } else {
            mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.setPlayBaseShowIndex(0)
        }
    }

    /**
     * 绑定返回键监听
     */
    private fun bindOnBack() {
        val activity = activity
        if (activity is OnBackPressedRegLinsenr) {
            (activity as OnBackPressedRegLinsenr).onRegitsOnBackPressend(this)
        }
    }

    //解除回调监听
    private fun unBindOnBack() {
        val activity = activity
        if (activity is OnBackPressedRegLinsenr) {
            (activity as OnBackPressedRegLinsenr).unRegitsOnBackPressend(this)
        }
    }

    override fun initData() {
        //showLoadData()
        //mCurrentPage = 1
        // getLiveList()
    }

    /**
     * 获取直播列表
     */
    fun getLiveList() {
        mViewModel.getLiveFollowList(mCurrentPage)
        if (mCurrentPage == 1) {
            mViewModel.getLiveRecommendList()
        }

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            mFollowList.observe(this@LiveAttenListFragment, Observer {
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                // hideLoadData()
                it.dataList.apply {
                    bindViewData(this)
                }
            })
            mFollowRemmList.observe(this@LiveAttenListFragment, Observer {
                if (it.dataList.size > 0) {
                    mLiveRemAdapter = LiveRemAdapter(it.dataList)
                    if (it.dataList.size > 2) {
                        //控制进度
                        SeekBarUtils.helpSeekBarRe(rv_rem_list, slide_indicator_point)
                        slide_indicator_point.visibility = View.VISIBLE
                    } else {
                        slide_indicator_point.visibility = View.GONE
                    }

                    mLiveRemAdapter?.setOnAdapterItenClickLinseer(object : OnAdapterItenClickLinseer {
                        override fun onCliclLinsener(info: Any, postion: Int) {
                            if (info is LiveRemmInfo) {
                                if (info.dataType == "1") {//直播
                                    LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.ATTEN_TJ.type, info.batchId)
                                } else {//商品
                                    GoodsDetailActivity.start(context!!, info.goodsId)
                                }

                            }
                        }
                    })


                    //rv_rem_list.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    rv_rem_list.setLayoutManager(object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    })
                    rv_rem_list.adapter = mLiveRemAdapter
                }

            })
            errMsg.observe(this@LiveAttenListFragment, Observer {
                ToastHelper.showToast(it)
            })
            mException.observe(this@LiveAttenListFragment, Observer {

            })
        }
    }

    fun bindViewData(dataList: List<LiveFollowInfo>?) {

        if (dataList == null || dataList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            showNoteData()
            return
        } else {
            if (mCurrentPage == 1) { //如果暂无数据，这样写是为了适配滑动
                rl_not_data.visibility = View.GONE
            }
        }
        if (mCurrentPage == 1) {
            mLists.clear()
        }
        mLists.addAll(dataList)

        setAdapter()

    }

    private fun showNoteData() {
        if (mCurrentPage == 1) { //如果暂无数据，这样写是为了适配滑动
            rl_not_data.visibility = View.VISIBLE
        }
    }

    fun setAdapter() {
        if (mAdapter == null) {

            mAdapter = HomeLiveRecommendAdapter(mLists, mOnClinener)
            rv_common.setLayoutManager(object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            })
            rv_common.adapter = mAdapter
        } else {
            mAdapter?.setNewData(mLists)
        }

    }

    private val mOnClinener = object : OnLiveFowlloClcikLInsenr {
        override fun onItemClickLinsener(data: LiveFollowInfo) {
            data.apply {
                if (liveStatus == "2") {
                    LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.LIVE_LIST.type, batchId)

                } else {
                    LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.RE_PLAY.type, batchId)

                }
            }

        }

        override fun onMakeLinser(type: String, data: LiveFollowInfo) {
            mViewModel.upLiveRescer(data.batchId, type).observe(this@LiveAttenListFragment, Observer {
                if (type == "1") {
                    SCApp.getInstance().showSystemCenterToast("设置成功\n" +
                            "主播开播我们会提醒您")
                } else {
                    SCApp.getInstance().showSystemCenterToast("您已取消预约")
                }

            })
        }
    }


    override val isShowTitle: Boolean
        get() = false

    override fun onBackPressedLinsener(): Boolean {
        mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.attentionIsShow().apply {
            if (this!!) {
                val mComfirmDialog = ComfirmDialog(activity!!)
                mComfirmDialog.setCommitMsg("确定")
                mComfirmDialog.setMsg("确定退出吗?")
                mComfirmDialog.setCancelable(false)

                mComfirmDialog.setOnCutCityLinsener(object : ComfirmDialog.OnCutCityLinsener {
                    override fun onCancerLinsener() {
                    }

                    override fun onConfirmLinsener() {
                        activity?.finish()
                    }
                })
                mComfirmDialog.show()
            }

        }


        return true
    }
}