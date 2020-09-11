package cn.sancell.xingqiu.mall.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CommdoityChildData
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity
import cn.sancell.xingqiu.util.PriceUtils
import cn.sancell.xingqiu.widget.RelativeSizeTextView
import cn.sancell.xingqiu.widget.roundimageview.RoundedImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil

/**
 * Created by zj on 2019/12/27.
 */
class CommodityChileAdapter(data: MutableList<CommdoityChildData>) : BaseMultiItemQuickAdapter<CommdoityChildData, BaseViewHolder>(data) {
    private var itemWidth: Int = 0

    init {
        itemWidth = (ScreenUtil.screenWidth - ScreenUtil.dip2px(50f)) / 3

        addItemType(0, R.layout.view_comm_child_layout)

    }

    override fun convert(helper: BaseViewHolder, item: CommdoityChildData) {
        val ll_conent = helper.getView<LinearLayout>(R.id.ll_conent)
        val rl_icon = helper.getView<RoundedImageView>(R.id.rl_icon)
        val tv_price = helper.getView<RelativeSizeTextView>(R.id.tv_price)

        ll_conent.layoutParams.width = itemWidth
        rl_icon.layoutParams.width = itemWidth
        ImageLoaderUtils.loadImage(mContext, item.coverPic, rl_icon, R.drawable.default_bg_shape)
        if (helper.layoutPosition < itemCount - 1) {
            val params = ll_conent.layoutParams as RecyclerView.LayoutParams
            params.rightMargin = ScreenUtil.dip2px(5f)
        }
        val tv_marke_price = helper.getView<TextView>(R.id.tv_marke_price)
        helper.setText(R.id.tv_name, item.title)
        tv_price.setTagText(PriceUtils.getInstance().getPrice(item.sellPrice))
        tv_marke_price.setText(PriceUtils.getInstance().getPriceWithSyp(item.marketPrice))
        tv_marke_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        helper.getView<View>(R.id.ll_conent).setOnClickListener {
            //跳转到商品详情
//            val intent = Intent(mContext, ProductInfoActivity::class.java)
//            intent.putExtra(Constants.Key.KEY_1, item.goodsId + "")
//            mContext.startActivity(intent)
            GoodsDetailActivity.start(mContext,item.goodsId.toInt())
        }
    }
}