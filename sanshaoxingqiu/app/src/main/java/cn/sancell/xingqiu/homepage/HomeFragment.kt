package cn.sancell.xingqiu.homepage

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayHomeActivity
import cn.sancell.xingqiu.homecommunity.video.VideoPlayListActivity
import cn.sancell.xingqiu.homepage.adapter.HomeMenuAdapter
import cn.sancell.xingqiu.homepage.bean.HomeActiveInfoBean.HeadlinesInfo.HeadlinesBean
import cn.sancell.xingqiu.homepage.bean.HomeMenuInfo
import cn.sancell.xingqiu.im.activity.ChatGroupActivity
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.ktenum.LivePlayType
import cn.sancell.xingqiu.mall.activity.MallHomeActivity
import cn.sancell.xingqiu.mall.help.BannerHelp
import cn.sancell.xingqiu.viewmodel.HoemViewModel
import cn.sancell.xingqiu.widget.MarqueeView
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader
import com.netease.nim.uikit.common.ToastHelper
import kotlinx.android.synthetic.main.framgent_new_home_layout.*
import kotlinx.android.synthetic.main.layout_homepage_announcement_module.*

/**
 * Created by zj on 2019/12/24.
 */
class HomeFragment : BaseNotDataFragmentKt<HoemViewModel>() {
    var mHomeMenuAdapter: HomeMenuAdapter? = null
    var mBannerHelp: BannerHelp? = null
    private var mMsgList: List<HeadlinesBean>? = null

    override fun onReloadData() {
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false

    override fun providerVMClass(): Class<HoemViewModel> = HoemViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.framgent_new_home_layout

    override fun initView() {

        refreshLayout.setRefreshHeader(SancellToobarClassicsHeader(activity))

        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setOnRefreshListener {
            getData()
        }
        mv_sancell_information.setOnItemClickListener(object : MarqueeView.OnItemClickListener {
            override fun onItemClick(position: Int, textView: TextView?) {
                mMsgList?.apply {
                    val intent = Intent(activity, UrlInfoActivity::class.java)
                    intent.putExtra(Constants.Key.KEY_1, this.get(position).getWebUrl())
                    intent.putExtra(Constants.Key.KEY_2, "")
                    startActivity(intent)

                }
            }
        })
        mBannerHelp = BannerHelp(this, home_loop_banner, false)
    }

    /**
     * 设置菜单九宫格
     */
    fun initMenu(mList: MutableList<HomeMenuInfo.MenuInfo>) {
        if (mHomeMenuAdapter == null) {
            rl_kong_kim.layoutManager = GridLayoutManager(context, 3)
            mHomeMenuAdapter = HomeMenuAdapter(mList)
            mHomeMenuAdapter?.setOnItemChildClickListener { adapter, view, position ->
                val mInf = adapter.data.get(position) as HomeMenuInfo.MenuInfo
                when (mInf.id) {
                    "2", "3" -> {//社群
                        startActivity(Intent(context, ChatGroupActivity::class.java))
                        //  startActivity(Intent(context, LiveAttenListActivity::class.java))
                        //  startActivity(Intent(context, LiveSubscribeActivity::class.java))
                    }
                    "4" -> {//直播 LivePlayListActivity
                        LivePlayHomeActivity.startLivePlay(context!!, LivePlayType.HOME_TO_TYPE.type, true)
                        //  startActivity(Intent(context, LiveListActivity::class.java))
                    }
                    "5" -> {//视频 VideoListActivity
                        //startActivity(Intent(context, LiveAnchorActivity::class.java))
                        startActivity(Intent(context, VideoPlayListActivity::class.java))
                    }
                    "1", "6", "7", "8", "9", "10", "11" -> {//商城 MarketTabsActivity
                        if (mInf.show == 1) {//是否显示 1显示 2不显示
                            MallHomeActivity.startIntent(context!!, mInf.id.toString())
                        } else {
                            ToastHelper.showToast("敬请期待")
                            // StayTunedActivity.startIntent(context!!, mInf.name)
                        }

                    }


                }

            }
            rl_kong_kim.adapter = mHomeMenuAdapter
        } else {
            mHomeMenuAdapter?.setNewData(mList)
        }
        rl_kong_kim.adapter = mHomeMenuAdapter

    }


    fun getData() {
        mViewModel.getHomeBannerData()
        mViewModel.getHomeActiveInfoData()
        mViewModel.getHomeMenu()

    }


    override fun initData() {
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBannerHelp?.stopLoop()
        mv_sancell_information?.stopFlipping()
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mException.observe(this@HomeFragment, Observer {

            })

            mBannerData.observe(this@HomeFragment, Observer {
                refreshLayout.finishRefresh()


                it?.apply {
                    mBannerHelp?.setLoopData(dataList)
                }


            })
            activityData.observe(this@HomeFragment, Observer {
                if (it.headlinesInfo != null && it.headlinesInfo.dataList != null) {
                    rl_sancell_information.visibility = View.VISIBLE
                    val dataList = ArrayList<String>()
                    mMsgList = it.headlinesInfo.dataList
                    for (empInfo in it.headlinesInfo.dataList) {
                        dataList.add(empInfo.title)
                    }
                    mv_sancell_information.stopFlipping()
                    mv_sancell_information.startWithList(dataList)
                } else {
                    rl_sancell_information.visibility = View.GONE

                }

            })
            menuData.observe(this@HomeFragment, Observer {
                it.dataList.apply {
                    initMenu(this)
                }

            })

        }
    }


}

