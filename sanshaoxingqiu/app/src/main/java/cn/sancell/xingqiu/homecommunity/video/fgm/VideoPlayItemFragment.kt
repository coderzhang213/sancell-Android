package cn.sancell.xingqiu.homecommunity.video.fgm

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity
import cn.sancell.xingqiu.homecommunity.adapter.VideoPlayRemAdapter
import cn.sancell.xingqiu.homecommunity.bean.CommunityVideoListBean
import cn.sancell.xingqiu.homecommunity.video.VideoListActivity
import cn.sancell.xingqiu.viewmodel.VideoPlayViewModel
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm
import cn.sancell.xingqiu.im.dialog.VideoRelDialogFgm
import cn.sancell.xingqiu.im.dialog.VideoRelDialogFgm.OnGoodsListener
import cn.sancell.xingqiu.im.dialog.VideoRelDialogFgm.OnTeamClickListener
import cn.sancell.xingqiu.im.entity.req.AddCartReq
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener
import cn.sancell.xingqiu.im.ui.red.call.ScClient
import cn.sancell.xingqiu.kt.BaseNotDataFragmentKt
import cn.sancell.xingqiu.util.BitmapUtils
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.util.RenderScriptGaussianBlur
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import kotlinx.android.synthetic.main.fragment_video_play_item_layout.*
import kotlinx.android.synthetic.main.fragment_video_play_item_layout.tv_title

/**
 * Created by zj on 2019/12/20.
 */
class VideoPlayItemFragment : BaseNotDataFragmentKt<VideoPlayViewModel>() {
    private var isVisibleToUser: Boolean = false
    private var postion: Int = 0
    private var mInfo: CommunityVideoListBean.VideoBean? = null
    override fun getLayoutResId(): Int = R.layout.fragment_video_play_item_layout
    override fun providerVMClass(): Class<VideoPlayViewModel>? {
        return VideoPlayViewModel::class.java
    }

    override fun initView() {
        btn_good.setOnClickListener(mOnClickLinsener)
        btn_team.setOnClickListener(mOnClickLinsener)
        btn_group.setOnClickListener(mOnClickLinsener)
        iv_close.setOnClickListener(mOnClickLinsener)
    }

    override fun initData() {
        arguments?.apply {
            getInt("postion").apply {
                postion = this
                mViewModel.getVideoList(this, 1)
            }
        }

    }

    private fun getDefaultData(datas: VideoRelationRes) {
        datas.dataList?.apply {
            recommend_list.visibility = View.VISIBLE
            recommend_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val mVideoPlayRemAdapter = VideoPlayRemAdapter(this)
            mVideoPlayRemAdapter.setOnItemChildClickListener { adapter, view, position ->
                val mInfo = adapter.data.get(position) as VideoRelationRes.InfoList
                when (view.id) {
                    R.id.ll_par_conet -> {//查看详情
                        mOnGoodsListener.goodDetail(mInfo.goodsId.toString())
                    }
                    R.id.iv_add_cart -> {//添加到购物车
                        //mOnGoodsListener.addCart(mInfo.goodsId.toString())
                        mOnGoodsListener.goodDetail(mInfo.goodsId.toString())
                    }
                }

            }
            recommend_list.adapter = mVideoPlayRemAdapter
        }

    }

    /**
     * 获取默认关联商品
     */
    fun getGlComData() {
        mInfo?.apply {
            mViewModel.getVideoRelation("a", this.id, true)
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            mVideoList.observe(this@VideoPlayItemFragment, Observer {
                it.dataList?.apply {
                    mInfo = get(0)
                    bindViewData(mInfo!!)
                    getGlComData()

                }

            })
            mVideoInfo.observe(this@VideoPlayItemFragment, Observer {
                setClickTru()
                if (it.dataList.size > 0) {
                    if (it.type.equals("a")) {
                        if (it.isLastGet) {
                            getDefaultData(it)
                        } else {
                            getVideoGoodsSuccess(it)
                        }

                    } else {
                        getVideoTeamSuccess(it)
                    }
                }

            })
            addCar.observe(this@VideoPlayItemFragment, Observer {
                ToastHelper.showToast(context, "已加入购物车")

            })
            errMsg.observe(this@VideoPlayItemFragment, Observer {
                ToastHelper.showToast(context, it)

            })
        }
    }

    private val mOnGoodsListener = object : OnGoodsListener {
        override fun addCart(goodId: String) {
            //  mViewModel.addCart(goodId, "1")

//            val intent = Intent(activity, ProductInfoActivity::class.java)
//            intent.putExtra(Constants.Key.KEY_1, goodId)
//            startActivity(intent)
            GoodsDetailActivity.start(activity, goodId.toInt())
        }

        override fun goodDetail(goodsId: String?) {
//            val intent = Intent(activity, ProductInfoActivity::class.java)
//            intent.putExtra(Constants.Key.KEY_1, goodsId)
//            startActivity(intent)
            GoodsDetailActivity.start(activity, goodsId!!.toInt())
        }
    }

    /**
     * 关联商品
     */
    fun getVideoGoodsSuccess(datas: VideoRelationRes) { //   可以点击
        setClickTru()
        val dialogFgm = VideoRelDialogFgm.newInstance("1", datas.dataList)
        dialogFgm.setDialogHeight(getDialogHeight())
        dialogFgm.setGoodsListener(mOnGoodsListener)
        dialogFgm.show(activity!!.supportFragmentManager, "goods")
    }

    /**
     * 关联群组
     */
    fun getVideoTeamSuccess(datas: VideoRelationRes) { //   可以点击
        val dialogFgm = VideoRelDialogFgm.newInstance("3", datas.dataList)
        dialogFgm.setDialogHeight(getDialogHeight())
        dialogFgm.setTeamClickListener(object : OnTeamClickListener {
            override fun onApplyJoin(teamId: String) {
                ScClient.applyJoinWithVer(activity, teamId, PreferencesUtils.getString(Constants.Key.key_im_accid, "")
                        , object : TeamApplyListener {
                    override fun showInputDialog() {
                        val applyDialog = ApplyJoinTeamInputDialogFgm()
                        applyDialog.setOnApplyListener { str: String? ->
                            ScClient.applyJoinTeam(activity, teamId, null,
                                    PreferencesUtils.getString(Constants.Key.key_im_accid, str), null)
                        }
                        applyDialog.show(activity!!.supportFragmentManager, "video")
                    }

                    override fun onSuccess(o: Any) {}
                    override fun onFailed(i: Int) {}
                    override fun onException(throwable: Throwable) {}
                })
            }

            override fun onChat(teamId: String) {
                ScClient.enterTeamChat(activity, teamId)
            }
        })
        dialogFgm.show(activity!!.supportFragmentManager, "team")
    }

    fun getDialogHeight(): Int {
        return (ScreenUtil.screenHeight * 0.6).toInt()
    }

    private val mOnClickLinsener = View.OnClickListener {

        when (it.id) {
            R.id.btn_good -> {//商品
                btn_good.isEnabled = false
                mInfo?.apply {
                    mViewModel.getVideoRelation("a", this.id, false)
                }

            }
            R.id.btn_team -> {//群组
                btn_team.isEnabled = false
                mInfo?.apply {
                    mViewModel.getVideoRelation("b", this.id, false)
                }

            }
            R.id.btn_group -> {
                startActivity(Intent(context, VideoListActivity::class.java))
            }
            R.id.iv_close -> {
                activity?.onBackPressed()
            }
        }
    };

    /**
     * 设置数据
     */
    private fun bindViewData(mVideoBean: CommunityVideoListBean.VideoBean) {
        //播放框架
        //detail_player.setUp(mVideoBean.videoUrl, true, "")
        tv_title.text = mVideoBean.title
        tv_desc.text = mVideoBean.intro
        val thumb = ImageView(context)
        thumb.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoaderUtils.loadImage(context, mVideoBean.icon, thumb)

        Glide.with(context!!).load(mVideoBean.icon).addListener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                iv_video_bg.setImageBitmap(RenderScriptGaussianBlur(context!!).gaussianBlur(20, BitmapUtils.drawableToBitmap(resource)))
                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }
        }).preload()
//        ImageLoaderUtils.loadImage(context, mVideoBean.icon, iv_video_bg)
    //    detail_player.setThumbImageView(thumb)
       // detail_player.setAutoFullWithSize(true)
        //detail_player.fullscreenButton.setOnClickListener {
            // detail_player.startWindowFullscreen(context, false, true)
        //}
        //startVideoPlay()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        //去判断是否播放
        startVideoPlay()
    }

    /**
     * 视频播放
     */
    private fun startVideoPlay() {
//        if (postion <= 0) {
//            return
//        }
//        if (isVisibleToUser) {//如果当前不可见
//            if (detail_player.currentState == CURRENT_STATE_NORMAL) {
//                detail_player.startPlayLogic()
//            }
//
//        } else {
//            if (detail_player.currentState == CURRENT_STATE_PLAYING) {
//                detail_player?.onVideoPause()
//            }
//        }

    }

    override fun onPause() {
        super.onPause()
        //detail_player?.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        //detail_player?.onVideoResume()
    }

    /**
     * 设置可以点击
     */
    private fun setClickTru() {
       // btn_good.isEnabled = true
        //btn_team.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
//        detail_player?.apply {
//            GSYVideoManager.releaseAllVideos()
//        }

    }

    override fun onReloadData() {

    }

    override val isLoadNotDat: Boolean
        get() = true
    override val isShowTitle: Boolean
        get() = false
}