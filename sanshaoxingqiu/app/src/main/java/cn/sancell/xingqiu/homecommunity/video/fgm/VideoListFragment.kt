package cn.sancell.xingqiu.homecommunity.video.fgm

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homecommunity.adapter.CommunityVideoListAdapter
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean
import cn.sancell.xingqiu.homecommunity.video.VideoPlayListActivity
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.viewmodel.VideoPlayViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.common_recycleview.*

/**
 * Created by zj on 2019/12/26.
 */
class VideoListFragment : BaseNotDataFragmentKt<VideoPlayViewModel>() {
    private val mPagerSize = 10
    private var mPagerIndex = 1
    private var mCommunityVideoListAdapter: CommunityVideoListAdapter? = null

    protected val mAllList: MutableList<CommunityVideoListBean.VideoBean> = ArrayList()

    override fun onReloadData() {
    }

    override fun providerVMClass(): Class<VideoPlayViewModel>? {
        return VideoPlayViewModel::class.java
    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = true

    override fun getLayoutResId(): Int = R.layout.common_recycleview

    override fun initView() {
        setTitleName("视频")
        refreshLayout.setOnRefreshListener {
            mPagerIndex = 1
            getData()
        }
        refreshLayout.setOnLoadMoreListener {
            mPagerIndex++
            getData()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mVideoList.observe(this@VideoListFragment, Observer {
                hindLoadStatus()
                setAdapter(it)

            })

        }
    }

    override fun hindLoadStatus() {
        super.hindLoadStatus()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
    }

    private fun setAdapter(mInfo: CommunityVideoListBean) {
        if (mInfo.dataList == null || mInfo.dataList.size <= 0) {
            refreshLayout.finishLoadMoreWithNoMoreData()
            if (mPagerIndex == 1) {//显示暂无数据
                setNotData()
            }
            return
        }
        if (mPagerIndex == 1) {
            mAllList.clear()
        }
        mAllList.addAll(mInfo.dataList)
        if (mCommunityVideoListAdapter == null) {
            mCommunityVideoListAdapter = CommunityVideoListAdapter(mAllList)
            rv_common.layoutManager = LinearLayoutManager(context)
            rv_common.adapter = mCommunityVideoListAdapter
            mCommunityVideoListAdapter?.setOnItemChildClickListener(object : BaseQuickAdapter.OnItemChildClickListener{
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    val videoBean: CommunityVideoListBean.VideoBean = mAllList.get(position)
                    val intent = Intent(context, VideoPlayListActivity::class.java)
                    intent.putExtra("playId", videoBean.id)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }
            })
        } else {
            mCommunityVideoListAdapter?.setNewData(mAllList)
        }
    }

    override fun initData() {
        showLoadData()
        getData()
    }

    fun getData() {
        mViewModel.getVideoList(mPagerIndex, mPagerSize)
    }
}