package cn.sancell.xingqiu.mall.adapter

import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ComItenInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.util.PriceUtils
import cn.sancell.xingqiu.widget.RelativeSizeTextView
import cn.sancell.xingqiu.widget.roundimageview.RoundedImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil

/**
 * Created by zj on 2019/12/30.
 */
class ComListAdapter(data: MutableList<ComItenInfo>, type: Int) : BaseMultiItemQuickAdapter<ComItenInfo, BaseViewHolder>(data) {
    //1 为商城的查看全部， 2. 为直播的搜索
    private var mType = 1
    private var itemWidth: Int = 0
    private var selectIndex = -1

    init {
        mType = type
        itemWidth = (ScreenUtil.screenWidth - ScreenUtil.dip2px(30f)) / 2
        addItemType(0, R.layout.item_com_i_like_list)
    }

    override fun convert(helper: BaseViewHolder, item: ComItenInfo) {
        val rl_item = helper.getView<RelativeLayout>(R.id.rl_item)
        val rl_select = helper.getView<RelativeLayout>(R.id.rl_select)
        val rl_icon = helper.getView<RoundedImageView>(R.id.rl_icon)
        ImageLoaderUtils.loadImage(mContext, item.coverPic, rl_icon)
        val tv_title = helper.getView<TextView>(R.id.tv_title)
        val tv_price = helper.getView<RelativeSizeTextView>(R.id.tv_price)
        val tv_price_orig = helper.getView<TextView>(R.id.tv_price_orig)
        tv_title.setText(item.title)
        tv_price.setTagText(PriceUtils.getInstance().getPrice(item.sellPrice))
        tv_price_orig.setText(PriceUtils.getInstance().getPriceWithSyp(item.marketPrice))
        tv_price_orig.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        if (selectIndex == helper.layoutPosition) {
            rl_select.visibility = View.VISIBLE
        } else {
            rl_select.visibility = View.GONE
        }
        rl_select.setOnClickListener {
            onItemClickListener?.onItemClick(null, null, helper.layoutPosition)
        }
        rl_item.setOnClickListener {
            if (mType == 1) {
                //跳转到商品详情
                GoodsDetailActivity.start(mContext, item.goodsId.toInt())
            } else {
                selectIndex = helper.layoutPosition
                notifyDataSetChanged()
            }

        }

    }
}