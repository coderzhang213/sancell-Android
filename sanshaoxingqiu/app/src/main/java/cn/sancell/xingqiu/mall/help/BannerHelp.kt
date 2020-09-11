package cn.sancell.xingqiu.mall.help

import android.app.Activity
import android.widget.ImageView
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean
import cn.sancell.xingqiu.util.BannerJumpUtils
import com.wikikii.bannerlib.banner.IndicatorLocation
import com.wikikii.bannerlib.banner.LoopLayout
import com.wikikii.bannerlib.banner.LoopStyle
import com.wikikii.bannerlib.banner.OnHomeDefaultImageViewLoader
import com.wikikii.bannerlib.banner.bean.BannerInfo
import com.wikikii.bannerlib.banner.listener.OnBannerItemClickListener

/**
 * Created by zj on 2019/12/27.
 */
class BannerHelp {
    private var mLoopLayout: LoopLayout? = null
    private var mFragment: Fragment? = null
    private var activity: Activity? = null
    private var isCircle = false
    var mBannerList = ArrayList<HomeBannerDataBean.BannerBean>()

    constructor(mFragment: Fragment, mLoopLayout: LoopLayout, empIsCircle: Boolean) {
        this.mLoopLayout = mLoopLayout
        this.mFragment = mFragment
        this.activity = mFragment.activity
        this.isCircle = empIsCircle
        initView()
        initBan()
    }

    fun initView() {

    }

    fun initBan() {
        mLoopLayout?.apply {
            loop_ms = 5000 //轮播的速度(毫秒)

            setLoop_duration(400) //滑动的速率(毫秒)

            setScaleAnimation(false) // 设置是否需要动画
           // setScaleAnimation(true) // 设置是否需要动画

            setLoop_style(LoopStyle.Empty) //轮播的样式-默认empty

            setIndicatorLocation(IndicatorLocation.Center) //指示器位置-中Center

            initializeData(activity)
            setOnLoadImageViewListener(object : OnHomeDefaultImageViewLoader() {
                override fun onLoadImageView(imageView: ImageView?, parameter: Any?) {
                    if (isCircle) {
                        ImageLoaderUtils.loadRoundImage(context!!, parameter.toString(), imageView!!, 30, R.drawable.default_loop_bg_shape)

                    } else {
                        ImageLoaderUtils.loadImage(context!!, parameter.toString(), imageView!!, R.drawable.default_loop_bg_shape)

                    }
                }
            })
            setOnBannerItemClickListener(mOnBannerItemClickListener)
        }

    }

    fun stopLoop() {
        mLoopLayout?.stopLoop()
    }

    fun setLoopData(mList: MutableList<HomeBannerDataBean.BannerBean>) {
        // 准备数据
        val bannerInfos = ArrayList<BannerInfo<*>>()
        for (info in mList) {
            bannerInfos.add(BannerInfo(info.coverPic, "pic" + info.id))
        }
        setBannerListData(mList)
        mLoopLayout?.stopLoop()
        mLoopLayout?.setLoopData(bannerInfos)
        mLoopLayout?.startLoop()
    }

    /**
     * 设置数据
     */
    private fun setBannerListData(mList: MutableList<HomeBannerDataBean.BannerBean>?) {
        mList?.apply {
            mBannerList.clear()
            mBannerList.addAll(mList)
        }
    }

    /**
     * 轮播图
     */
    private var mOnBannerItemClickListener = object : OnBannerItemClickListener {
        override fun onBannerClick(index: Int, banner: java.util.ArrayList<BannerInfo<Any>>?) {
            BannerJumpUtils.bannerJump(activity, mBannerList, index)

        }
    }
}