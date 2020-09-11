package cn.sancell.xingqiu.homeuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.FightBaseData
import cn.sancell.xingqiu.bean.FightItemInfo
import cn.sancell.xingqiu.constant.CountdownManager
import cn.sancell.xingqiu.goods.fragment.listener.UmShareListener
import cn.sancell.xingqiu.homeuser.OrderNewPackInfoActivity
import cn.sancell.xingqiu.homeuser.OrderPackInfoActivity
import cn.sancell.xingqiu.homeuser.adapter.FightOderAdapter
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.order.detail.PinDetailActivity
import cn.sancell.xingqiu.order.orderInfo.PinOrderPackInfoActivity
import cn.sancell.xingqiu.util.DialogUtil
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import cn.sancell.xingqiu.viewmodel.FightViewModel
import com.umeng.socialize.media.UMImage
import kotlinx.android.synthetic.main.fragment_fight_child_layout.*

/**
 * Created by zj on 2020/1/3.
 */
class FightChildFragmetn : BaseNotDataFragmentKt<FightViewModel>() {
    private var type: String? = ""
    var page = 1
    val mAllList: MutableList<FightItemInfo> = ArrayList()
    var mFightOderAdapter: FightOderAdapter? = null
    private var mCountdownManager: CountdownManager? = null

    companion object {
        fun newFragment(type: String): Fragment {
            val mFragment = FightChildFragmetn()
            val mBundle = Bundle()
            mBundle.putString("type", type)
            mFragment.arguments = mBundle
            return mFragment

        }
    }

    override fun providerVMClass(): Class<FightViewModel>? {
        return FightViewModel::class.java
    }

    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun getLayoutResId(): Int = R.layout.fragment_fight_child_layout

    override fun initView() {
        refreshLayout.setOnRefreshListener {
            page = 1
            getData()

        }
        refreshLayout.setOnLoadMoreListener {
            page++
            getData()
        }
    }

    override fun initData() {
        ObserverManger.getInstance(ObserverKey.UP_FIGHT_LIST).registerObserver(mUpListObserver)
        type = arguments?.getString("type")
        showLoadData()
        getData()
    }

    /**
     * 刷新列表
     */
    private val mUpListObserver = OnObserver {
        getData()
    }

    override fun onDestroy() {
        mFightOderAdapter?.cancerCountdown()
        super.onDestroy()
        ObserverManger.getInstance(ObserverKey.UP_FIGHT_LIST).removeObserver(mUpListObserver)

    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mFightBaseData.observe(this@FightChildFragmetn, Observer {
                hindLoadStatus()
                bingDataView(it)

            })
        }
        //FightOderAdapter
    }

    fun bingDataView(mInfo: FightBaseData) {
        val mList = mInfo.dataList
        if (mList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (page == 1) {//显示暂无数据
                setNotData(resources.getString(R.string.empty_order_zp), R.mipmap.icon_empty_order)
            }
            return
        }
        if (mCountdownManager == null) {
            mCountdownManager = CountdownManager.get()
            mCountdownManager?.initTime(mList.get(0).currentTime)
        }

        if (page == 1) {
            mAllList.clear()
        }
        mAllList.addAll(mList)
        // val empList = ArrayList<FightItemInfo>()
        // empList.add(mAllList.get(0))
        if (mFightOderAdapter == null) {

            mFightOderAdapter = FightOderAdapter(mAllList)
            mFightOderAdapter?.setOnClickLinsener(object : FightOderAdapter.OnFigheClickLinsern {
                override fun onItemClickListener(mFightItemInfo: FightItemInfo) {
                    PinDetailActivity.start(context, mFightItemInfo.grouponOrderId)
                }

                override fun onInviteFriendsLinsern(mFightItemInfo: FightItemInfo) {
                    DialogUtil.getShareDialog(activity, UMImage(context, mFightItemInfo.grouponInviteData.logoUrl), mFightItemInfo.grouponInviteData.link,
                            mFightItemInfo.grouponInviteData.title, mFightItemInfo.grouponInviteData.desc, UmShareListener())

                }

                override fun onQueryOderLinsener(mFightItemInfo: FightItemInfo) {
                    if (mFightItemInfo.grouponOrderStatus == 2) {
                        OrderNewPackInfoActivity.start(context, mFightItemInfo.parcelId, mFightItemInfo.id)
                    } else {
                        PinOrderPackInfoActivity.start(context, mFightItemInfo.id)

                    }
                }
            })
            rl_list.layoutManager = LinearLayoutManager(context)
            rl_list.adapter = mFightOderAdapter
        } else {
            mFightOderAdapter?.setNewData(mAllList)
        }


    }

    fun getData() {
        mViewModel.getFightDataList(type!!, page)
    }

}