package cn.sancell.xingqiu.homecommunity.video

import android.os.Bundle
import androidx.lifecycle.Observer
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homecommunity.adapter.VideoPlayItemAdapter
import cn.sancell.xingqiu.viewmodel.VideoPlayViewModel
import cn.sancell.xingqiu.util.StatusBarUtil
import com.netease.nim.uikit.common.ToastHelper
import cn.sancell.xingqiu.kt.BaseActivity
import kotlinx.android.synthetic.main.activit_video_list_layout.*

/**
 * Created by zj on 2019/12/20.
 */
class VideoPlayListActivity : BaseActivity<VideoPlayViewModel>() {
    private var position = 0
    override fun getLayoutResId() = R.layout.activit_video_list_layout

    override fun providerVMClass(): Class<VideoPlayViewModel>? {
        return VideoPlayViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
    }

    override fun initView() {
    }

    override fun initData() {
        position = intent.getIntExtra("position", 0)
        mViewModel.getVideoList(1, 1)

    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mVideoList.observe(this@VideoPlayListActivity, Observer {
                it?.apply {
                    val mVideoPlayItemAdapter = VideoPlayItemAdapter(supportFragmentManager, dataCount)
                    view_pager.adapter = mVideoPlayItemAdapter
                    view_pager.currentItem = position
                }


            })
            errMsg.observe(this@VideoPlayListActivity, Observer {

                ToastHelper.showToast(this@VideoPlayListActivity, it)
            })
        }
    }

}