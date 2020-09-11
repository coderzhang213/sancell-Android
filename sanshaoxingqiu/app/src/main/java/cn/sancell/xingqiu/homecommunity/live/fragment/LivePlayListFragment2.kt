package cn.sancell.xingqiu.homecommunity.live.fragment

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveStatus
import cn.sancell.xingqiu.bean.LiveStatusEnum
import cn.sancell.xingqiu.bean.RecomLiveInfo
import cn.sancell.xingqiu.bean.RecomLiveParInfo
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.adapter.LivePlayItemAdapter2
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.util.RxTimerUtil
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import kotlinx.android.synthetic.main.activit_live_list_layout2.*

/**
 * 直播播放列表
 */
class LivePlayListFragment2 : BaseNotDataFragmentKt<LiveViewModel>(), OnAddRecommendLinsener {
    private var mCurrentPage = 1
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    private var mCurrentPagerIndex = 0
    private val mAllList = ArrayList<RecomLiveInfo>()
    private var mVideoPlayItemAdapter: LivePlayItemAdapter2? = null
    private var type = ""
    private var batchId = ""
    private var anchorId = ""
    private val idList = ArrayList<String>()
    var mRxTimerUtil: RxTimerUtil? = null

    //是否上啦刷新
    private var isLoadMode = false
    override fun onReloadData() {
    }

    init {
        mRxTimerUtil = RxTimerUtil()
    }

    override val isShowTitle: Boolean
        get() = false

    companion object {
        fun startIntent(mType: String?, batchId: String?, anchorId: String): LivePlayListFragment2 {
            val framgent = LivePlayListFragment2()
            val mBundle = Bundle()
            mBundle.putString(LiveConstant.LIVE_TYPE, mType)
            mBundle.putString(LiveConstant.LIVE_BIACTH, batchId)
            mBundle.putString(LiveConstant.ANDID_ID, anchorId)
            framgent.arguments = mBundle
            return framgent
        }
    }

    override fun getLayoutResId(): Int = R.layout.activit_live_list_layout2

    override fun initView() {
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //把当前显示的索引保存起来
                mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.setCurrShowIndoex(position)
                mCurrentPagerIndex = position
                setRefView()
            }
        })
        setRefView()

        refreshLayout.setOnLoadMoreListener({
            if (mAllList.size >= 10) {
                mCurrentPage++
            }
            isLoadMode = false
            getData()
        })
        //下拉
        refreshLayout.setOnRefreshListener({
            mCurrentPage = 1
            isLoadMode = true
            getData()
        })
    }

    /**
     * 来控制分页问题
     */
    fun setRefView() {
        if (mCurrentPagerIndex == 0) {
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setEnableLoadMore(false)
        } else if (mCurrentPagerIndex == mAllList.size - 1) {
            refreshLayout.setEnableRefresh(false)
            //keey
            refreshLayout.setEnableLoadMore(true)
        } else {
            refreshLayout.setEnableRefresh(false)
            refreshLayout.setEnableLoadMore(false)
        }
    }

    override fun onResume() {
        super.onResume()
        upLiveStatus(LiveStatusEnum.ON_RESUME, 0)
    }

    override fun onPause() {
        super.onPause()
        upLiveStatus(LiveStatusEnum.ON_PAUSE, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        upLiveStatus(LiveStatusEnum.ON_DESTROY, 0)
        mRxTimerUtil?.cancel()
    }

    fun upLiveStatus(mType: LiveStatusEnum, postion: Int) {
        val mLiveStatus = LiveStatus(mType, postion)
        // ObserverManger.getInstance(ObserverKey.LIVE_LIFE_STAUS).notifyObserver(mLiveStatus)
    }

    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        ObserverManger.getInstance(ObserverKey.LIVE_Hidden).notifyObserver(hidden)
    }

    override fun initData() {
        showLoadData()
        type = arguments?.getString(LiveConstant.LIVE_TYPE)!!
        batchId = arguments?.getString(LiveConstant.LIVE_BIACTH)!!
        //主播ID
        anchorId = arguments?.getString(LiveConstant.ANDID_ID)!!

        getData()
    }

    fun getData() {
        mViewModel.getLiveV1List(mCurrentPage.toString(), type, batchId, anchorId)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {

            liveV1List.observe(this@LivePlayListFragment2, Observer {

                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                val mData = it
                mRxTimerUtil?.timer(1000, {
                    hideLoadData()
                    setAdapter(mData)

                })

            })
            mException.observe(this@LivePlayListFragment2, Observer {
                Log.i("keey", "e22:" + it.message)
            })
            errMsg.observe(this@LivePlayListFragment2, Observer {
                hideLoadData()
                setNotData()
            })
        }
    }

    fun setAdapter(mInfo: RecomLiveParInfo) {
        if (mInfo.dataList.size <= 0 && mCurrentPage > 1) {//用来控制下标一直累加
            mCurrentPage--
        }
        if (mCurrentPage == 1 && mInfo.dataList.size <= 0) {
            setNotData("暂无直播场次")
            activity?.finish()
            return
        }


//        if (mVideoPlayItemAdapter != null && isLoadMode) { //要把原有的frament清除
//            isLoadMode = false
//            view_pager.removeAllViews()
//            val fragments = mVideoPlayItemAdapter?.getFragments()
//            if (fragments!!.size > 0) {
//                val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
//                for (mFragment in fragments) {
//                    fragmentTransaction.remove(mFragment)
//                }
//            }
//            mVideoPlayItemAdapter = null
//        }
        if (mCurrentPage == 1 && isLoadMode) {
            mAllList.clear()
            idList.clear()
        }
        //把数据去重,并添加
        dataDeduplication(mInfo.dataList)
        if (mVideoPlayItemAdapter == null) {
            mVideoPlayItemAdapter = LivePlayItemAdapter2(this@LivePlayListFragment2, type, mAllList, this)
            view_pager.adapter = mVideoPlayItemAdapter
            view_pager.offscreenPageLimit = 2
        } else {
            mVideoPlayItemAdapter?.setmList(mAllList)
        }

//        if (!TextUtils.isEmpty(batchId)) {
//            view_pager.setCurrentItem(checkSelectLive(), true)
//            batchId = ""
//        }
        setRefView()
    }

    /**
     * 数据去重
     */
    fun dataDeduplication(dataList: List<RecomLiveInfo>) {
        if (idList.size >= 0) {
            for (info in dataList) {
                if (!idList.contains(info.recId)) {//如果本地数据没有，就添加，有就不加入
                    mAllList.add(info)
                    idList.add(info.recId)
                }
            }

        } else {
            mAllList.addAll(dataList)
            for (info in mAllList) {
                idList.add(info.recId)
            }

        }

    }

    fun checkSelectLive(): Int {
        if (TextUtils.isEmpty(batchId)) {
            return 0
        }
        for (i in 0..mAllList.size - 1) {
            if (mAllList.get(i).recId.equals(batchId)) {
                return i
            }
        }

        return 0
    }

    override val isLoadNotDat: Boolean
        get() = true

    override fun onAddRecommendData(dataList: List<RecomLiveInfo>?) {
//        dataList?.apply {
//            val mEmpRemList = ArrayList<RecomLiveInfo>()
//            for (info in this) {
//                for (mEmp in mAllList) {
//                    if (info.recId.equals(mEmp.recId)) {
//                        mEmpRemList.add(info)
//                    }
//                }
//            }
//            if (mEmpRemList.size > 0) {
//                val mList = ArrayList<RecomLiveInfo>()
//                mList.addAll(this)
//                //移除重复的
//                mList.removeAll(mEmpRemList)
//                if (mList.size > 0) {//如果是新增的，在加入后面
//                    mAllList.addAll(mList)
//                    mVideoPlayItemAdapter?.setmList(mAllList)
//                }
//            }
//
//        }

    }

}