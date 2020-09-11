package cn.sancell.xingqiu.homepage

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.dialog.MakeFansDialogFgm
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity
import cn.sancell.xingqiu.homepage.adapter.BannerImageAdapter
import cn.sancell.xingqiu.homepage.adapter.HomeLiveVideoAdapter
import cn.sancell.xingqiu.homepage.adapter.HomeLiverRecommendAdapter
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.homepage.bean.HomeRecommendLiverBean
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.live.activity.LiveOtherInfoActivity
import cn.sancell.xingqiu.live.activity.LiveSearchActivity
import cn.sancell.xingqiu.mall.help.BannerHelp
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.viewmodel.LiveViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.netease.nim.uikit.common.ToastHelper
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.transformer.RotateUpPageTransformer
import com.youth.banner.util.BannerUtils
import handbank.hbwallet.BaseFragment
import kotlinx.android.synthetic.main.fragment_live_home_page.*

/**
 * 新直播首页
 */
class LiveHomePageFgm : BaseFragment<LiveViewModel>() {

    private var mLiverRecommendAdapter: HomeLiverRecommendAdapter? = null
    private var mLiveVideoAdapter: HomeLiveVideoAdapter? = null
    private var mCurrentPage = 1
    var mBannerHelp: BannerHelp? = null
    override fun providerVMClass(): Class<LiveViewModel>? {
        return LiveViewModel::class.java
    }

    override fun getLayoutResId(): Int = R.layout.fragment_live_home_page

    override fun initView() {
        var height = 0
        val resourceId = context!!.applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = this.context!!.applicationContext.resources.getDimensionPixelSize(resourceId)
        }
        //设置view 的padding状态栏高度
        rl_home_title.setPadding(0, height, 0, 0)
        rv_liver_recommend.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_video_recommend.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_video_recommend.setHasFixedSize(true)
        val decoration = DividerItemDecoration(context, RecyclerView.HORIZONTAL)
        ContextCompat.getDrawable(context!!, R.drawable.shape_live_video_divider_7)?.let { decoration.setDrawable(it) }
        rv_video_recommend.addItemDecoration(decoration)
        iv_home_search.setOnClickListener {
            LiveSearchActivity.start(context!!)
        }
        iv_home_user.setOnClickListener {
            LiveOtherInfoActivity.start(context!!, AppUtils.getUserId())
        }
        initFreshListener()
    }

    /**
     * 刷新的监听
     */
    private fun initFreshListener() {
        home_fresh.setOnRefreshListener {
            mCurrentPage = 1
            initData()
            home_fresh.finishRefresh()
        }
        home_fresh.setEnableLoadMore(false)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mFollowList.observe(this@LiveHomePageFgm, Observer {
                it.dataList.apply {
                    Log.i("mFollowList", "总共item: ${it.dataCount}")
                    setVideoAdapter(it.dataList, it.dataCount)
                }
            })
            mBannerData.observe(this@LiveHomePageFgm, Observer {
                setBanner(it.dataList)
            })

            mRemAutche.observe(this@LiveHomePageFgm, Observer {
                setLiverAdapter(it.dataList)
            })
            errMsg.observe(this@LiveHomePageFgm, Observer {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
            mException.observe(this@LiveHomePageFgm, Observer {
                Log.i("LiveHomePageFgm", it.toString())
            })
        }

        mBannerHelp = BannerHelp(this@LiveHomePageFgm, home_banner, true)
    }

    private fun setBanner(data: MutableList<HomeBannerDataBean.BannerBean>) {
        mBannerHelp?.setLoopData(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBannerHelp?.stopLoop()
    }



    //设置videoAdapter
    private fun setVideoAdapter(data: List<LiveFollowInfo>?, total: Int) {
        if (data.isNullOrEmpty()) {
            //无直播
            if (mCurrentPage == 1) {
                //todo 空页面
            }
            Toast.makeText(context, "暂无直播", Toast.LENGTH_SHORT).show()
            return
        }
        //预加载结束
        if (mCurrentPage > 1) {
            mLiveVideoAdapter!!.loadMoreComplete()
        }

        if (mLiveVideoAdapter == null) {
            mLiveVideoAdapter = HomeLiveVideoAdapter(data)
            mLiveVideoAdapter?.setPreLoadNumber(4)
            mLiveVideoAdapter?.setOnLoadMoreListener {
                try {
                    mCurrentPage++
                    mViewModel.getLiveFollowList(mCurrentPage)
                } catch (e: Exception) {
                    e.printStackTrace()
                    mLiveVideoAdapter?.loadMoreFail()
                }
            }
            mLiveVideoAdapter?.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    val info = mLiveVideoAdapter?.data?.get(position)
                    if (info?.liveStatus.equals("2")) {
                        info?.batchId?.let { LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.LIVE_LIST.type, it) }
                    } else {
                        info?.batchId?.let { LivePlayBaseHoemActivity.startIntent(context!!, LivePlayType.RE_PLAY.type, it) }
                    }
                }

            })

            rv_video_recommend.adapter = mLiveVideoAdapter
        } else {
            if (mCurrentPage == 1) {
                mLiveVideoAdapter?.setNewData(data)
            } else {
                mLiveVideoAdapter?.addData(data)
            }
        }
        //是否关闭下拉刷新
        if (mLiveVideoAdapter!!.data.size >= total) {
            mLiveVideoAdapter!!.setEnableLoadMore(false)
            mLiveVideoAdapter!!.loadMoreEnd()
        } else {
            mLiveVideoAdapter!!.setEnableLoadMore(true)
        }
    }

    //获取直播推荐视频列表
    private fun getRecommendVideoList() {
        mViewModel.getLiveFollowList(mCurrentPage)
    }

    override fun initData() {
        getRecommendVideoList()
        mViewModel.getBannerData("11")
        mViewModel.remAncher()

        //获取会员等级存储
        mViewModel.getUserMember().observe(this@LiveHomePageFgm, Observer {
            PreferencesUtils.put(UiHelper.KEY_USER_MEMBER,it)
        })
    }

    private fun setLiverAdapter(data: List<HomeRecommendLiverBean.LiverBean>?) {
        if (data.isNullOrEmpty()) {
            //Toast.makeText(context, "暂无直播", Toast.LENGTH_SHORT).show()
            return
        }
        if (mLiverRecommendAdapter == null) {
            mLiverRecommendAdapter = HomeLiverRecommendAdapter(data)
            mLiverRecommendAdapter?.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
                override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    LiveOtherInfoActivity.start(context!!, data[position].userId!!)
                }

            })
            rv_liver_recommend.adapter = mLiverRecommendAdapter
        } else {
            mLiverRecommendAdapter!!.setNewData(data)
        }

    }


}