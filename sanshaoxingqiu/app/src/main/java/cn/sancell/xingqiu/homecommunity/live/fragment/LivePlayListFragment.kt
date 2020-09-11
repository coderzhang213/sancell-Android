package cn.sancell.xingqiu.homecommunity.live.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveStatus
import cn.sancell.xingqiu.bean.LiveStatusEnum
import cn.sancell.xingqiu.bean.RecomLiveInfo
import cn.sancell.xingqiu.bean.RecomLiveParInfo
import cn.sancell.xingqiu.interfaces.OnAddRecommendLinsener
import cn.sancell.xingqiu.interfaces.OnGetLivePalyStatusLinsener
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.live.adapter.LivePlayItemAdapter
import cn.sancell.xingqiu.live.constant.LiveConstant
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import kotlinx.android.synthetic.main.activit_video_list_layout.*
import kotlinx.android.synthetic.main.fragment_livew_list_layout.*

/**
 * 直播播放列表
 */
class LivePlayListFragment : BaseNotDataFragmentKt<LiveViewModel>(), OnAddRecommendLinsener {
    private var mCurrentPage = 1
    var mOnGetLivePalyStatusLinsener: OnGetLivePalyStatusLinsener? = null
    private var mCurrentPagerIndex = 0
    private val mAllList = ArrayList<RecomLiveInfo>()
    private var mVideoPlayItemAdapter: LivePlayItemAdapter? = null
    private var type = ""
    private var batchId = ""
    private var anchorId = ""
    private val idList = ArrayList<String>()

    //是否上啦刷新
    private var isLoadMode = false
    override fun onReloadData() {
    }

    override val isShowTitle: Boolean
        get() = false

    companion object {
        fun startIntent(mType: String?, batchId: String?, anchorId: String): LivePlayListFragment {
            val framgent = LivePlayListFragment()
            val mBundle = Bundle()
            mBundle.putString(LiveConstant.LIVE_TYPE, mType)
            mBundle.putString(LiveConstant.LIVE_BIACTH, batchId)
            mBundle.putString(LiveConstant.ANDID_ID, anchorId)
            framgent.arguments = mBundle
            return framgent
        }
    }

    override fun getLayoutResId(): Int = R.layout.activit_live_list_layout

    override fun initView() {
        val mactivity = activity
        if (mactivity is OnGetLivePalyStatusLinsener) {
            mOnGetLivePalyStatusLinsener = mactivity
        }
        view_pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                //upLiveStatus(LiveStatusEnum.ON_VIEW_PAGER_SELECT, position)
                //把当前显示的索引保存起来
                mOnGetLivePalyStatusLinsener?.getLiveStatusManger()?.setCurrShowIndoex(position)
                mCurrentPagerIndex = position
                setRefView()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        setRefView()

        refreshLayout.setOnLoadMoreListener({
            if (mAllList.size >= 10) {
                mCurrentPage++
            }
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

            liveV1List.observe(this@LivePlayListFragment, Observer {
                hideLoadData()
                refreshLayout.finishLoadMore()
                refreshLayout.finishRefresh()
                setAdapter(it)


            })
            mException.observe(this@LivePlayListFragment, Observer {
                Log.i("keey", "e22:" + it.message)
            })
            errMsg.observe(this@LivePlayListFragment, Observer {
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


        if (mVideoPlayItemAdapter != null && isLoadMode) { //要把原有的frament清除
            isLoadMode = false
            view_pager.removeAllViews()
            val fragments = mVideoPlayItemAdapter?.getFragments()
            if (fragments!!.size > 0) {
                val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                for (mFragment in fragments) {
                    fragmentTransaction.remove(mFragment)
                }
            }
            mVideoPlayItemAdapter = null
        }
        if (mCurrentPage == 1 && !isLoadMode) {
            mAllList.clear()
            idList.clear()
        }
        //把数据去重,并添加
        dataDeduplication(mInfo.dataList)
        if (mVideoPlayItemAdapter == null) {
            mVideoPlayItemAdapter = LivePlayItemAdapter(activity!!.supportFragmentManager, type, mAllList, this)
            view_pager.adapter = mVideoPlayItemAdapter
        } else {
            mVideoPlayItemAdapter?.setmList(mAllList)
            mVideoPlayItemAdapter?.notifyDataSetChanged()
        }

        if (!TextUtils.isEmpty(batchId)) {
            view_pager.setCurrentItem(checkSelectLive(), true)
            batchId = ""
        }
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