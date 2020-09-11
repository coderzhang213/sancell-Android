package cn.sancell.xingqiu.homecommunity.video

import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.homecommunity.video.fgm.HomeVideoFgm
import cn.sancell.xingqiu.homecommunity.video.fgm.VideoListFragment
import cn.sancell.xingqiu.kt.DefaultFragmetnAttachActivity
import cn.sancell.xingqiu.viewmodel.VideoPlayViewModel

/**
 * Created by zj on 2019/12/26.
 */
class VideoListActivity : DefaultFragmetnAttachActivity<VideoPlayViewModel>() {
    override val loadFragment: Fragment?
        get() = VideoListFragment()

    override fun initData() {
    }
}