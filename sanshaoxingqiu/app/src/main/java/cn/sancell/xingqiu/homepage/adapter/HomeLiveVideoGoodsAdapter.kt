package cn.sancell.xingqiu.homepage.adapter

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveFollowComInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 关联商品列表
 */
class HomeLiveVideoGoodsAdapter(data: List<LiveFollowComInfo>) : BaseQuickAdapter<LiveFollowComInfo, BaseViewHolder>(R.layout.recycle_live_video_goods, data) {

    private var mItemMargin = 0
    private var mParentWidth = 0
    private var mGoodsCount = 0


    override fun convert(helper: BaseViewHolder, item: LiveFollowComInfo) {
        val rl_goods_img = helper.getView<RelativeLayout>(R.id.rl_goods_img)
        val iv_video_goods = helper.getView<AppCompatImageView>(R.id.iv_video_goods)
        val tv_goods_count = helper.getView<AppCompatTextView>(R.id.tv_goods_count)

        val params = rl_goods_img.layoutParams
        params.width = (mParentWidth - mItemMargin) / 3
        params.height = params.width
        Log.i("HomeLiveVideoGoods", "params = ${params.width}")
        rl_goods_img.layoutParams = params

        ImageLoaderUtils.loadRoundImage(mContext, item.coverPicThumb, iv_video_goods, ScreenUtils.dip2px(mContext,4f))

        if (helper.adapterPosition == 2) {
            tv_goods_count.visibility = View.VISIBLE
            tv_goods_count.text = String.format(mContext.resources.getString(R.string.add), mGoodsCount)
        } else {
            tv_goods_count.visibility = View.GONE
        }
    }

    //recyclerView 的宽 px单位！！！
    fun setViewWidth(width: Int): HomeLiveVideoGoodsAdapter {
        mParentWidth = width
        Log.i("HomeLiveVideoGoods", "width = $width")
        return this
    }

    //元素的总间隔 px单位！！！
    fun setItemMargin(itemMargin: Int): HomeLiveVideoGoodsAdapter {
        mItemMargin = itemMargin
        Log.i("HomeLiveVideoGoods", "margin = $itemMargin")
        return this
    }

    //商品数量
    fun setGoodsCount(count: Int): HomeLiveVideoGoodsAdapter {
        mGoodsCount = count
        Log.i("HomeLiveVideoGoods", "count = $count")
        return this
    }
}