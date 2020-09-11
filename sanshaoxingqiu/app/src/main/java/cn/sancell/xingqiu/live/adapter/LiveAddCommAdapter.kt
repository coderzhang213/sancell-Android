package cn.sancell.xingqiu.live.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveRelevCommInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.util.PriceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class LiveAddCommAdapter(data: List<LiveRelevCommInfo>) : BaseQuickAdapter<LiveRelevCommInfo, BaseViewHolder>(R.layout.view_livew_add_comm_layout, data) {

    override fun convert(helper: BaseViewHolder, item: LiveRelevCommInfo) {

        val udl_group_icon = helper.getView<ImageView>(R.id.udl_group_icon)
        val tv_aigs = helper.getView<TextView>(R.id.tv_aigs)
        val rl_left = helper.getView<RelativeLayout>(R.id.rl_left)
        val rl_top_layout = helper.getView<RelativeLayout>(R.id.rl_top_layout)
        ImageLoaderUtils.loadImage(mContext, item.coverPicThumb, udl_group_icon)
        if (TextUtils.isEmpty(item.titleAlias)) {
            tv_aigs.visibility = View.GONE
            helper.setText(R.id.tv_title, item.title)
            setViewHeiaght(rl_left, 100)
            setViewHeiaght(rl_top_layout, 70)

        } else {
            tv_aigs.visibility = View.VISIBLE
            helper.setText(R.id.tv_title, item.titleAlias)
            helper.setText(R.id.tv_aigs, "原品名：" + item.title)
            setViewHeiaght(rl_left, 159)
            setViewHeiaght(rl_top_layout, 100)
        }

        helper.setText(R.id.tv_curr_price, PriceUtils.getInstance().getPriceWithSyp(item.sellingPriceE2))
        helper.setText(R.id.tv_oder_price, PriceUtils.getInstance().getPriceWithSyp(item.newMarketPriceE2))

        helper.getView<View>(R.id.tv_delete).setOnClickListener {

            onItemClickListener?.onItemClick(null, null, helper.layoutPosition)
        }
        helper.getView<TextView>(R.id.tv_up_com_name).setOnClickListener {

            onItemChildClickListener?.onItemChildClick(null, null, helper.layoutPosition)
        }


    }

    fun setViewHeiaght(view: RelativeLayout, height: Int) {
        val laypar = view.layoutParams
        laypar.height = ScreenUtil.dip2px(height.toFloat())

    }
}