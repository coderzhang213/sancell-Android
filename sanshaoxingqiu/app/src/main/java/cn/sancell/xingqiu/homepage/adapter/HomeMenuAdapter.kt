package cn.sancell.xingqiu.homepage.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homepage.bean.HomeMenuInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil

/**
 * Created by zj on 2019/12/26.
 */
class HomeMenuAdapter(data: MutableList<HomeMenuInfo.MenuInfo>) : BaseQuickAdapter<HomeMenuInfo.MenuInfo, BaseViewHolder>(R.layout.home_menu_item_layout, data) {
    private var itemWidth: Int = 0

    init {
        itemWidth = (ScreenUtil.screenWidth - ScreenUtil.dip2px(5f) * 2) / 3
    }

    override fun convert(helper: BaseViewHolder, item: HomeMenuInfo.MenuInfo) {
        val ll_conent = helper.getView<LinearLayout>(R.id.ll_conent)
        ll_conent?.layoutParams?.width = itemWidth
        val iv_icon = helper.getView<ImageView>(R.id.iv_icon)
        item.coverPic?.apply {
            ImageLoaderUtils.loadRoundImage(mContext, this, iv_icon, 20, R.drawable.default_bg_shape)
        }
        helper.addOnClickListener(R.id.ll_conent)
    }
}