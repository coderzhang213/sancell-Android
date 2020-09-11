package cn.sancell.xingqiu.live.adapter

import androidx.appcompat.widget.AppCompatImageView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiverBean
import cn.sancell.xingqiu.bean.ProfileRes
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.util.DateUtils
import cn.sancell.xingqiu.util.PriceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 用户收益adapter
 */
class UserEarnAdapter(data: MutableList<ProfileRes.ProfileBean>) : BaseQuickAdapter<ProfileRes.ProfileBean, BaseViewHolder>(R.layout.recycle_live_earn, data) {
    override fun convert(helper: BaseViewHolder, item: ProfileRes.ProfileBean) {
        val image = helper.getView<AppCompatImageView>(R.id.iv_goods)
        ImageLoaderUtils.loadCircleImage(mContext, item.userGravatar, image)
        helper.setText(R.id.tv_goods_name, item.str)
        helper.setText(R.id.tv_time, DateUtils.getTimeByStampWithYear(item.createdAt))
        helper.setText(R.id.tv_profile, "+" + PriceUtils.getInstance().getPrice(item.amountOfMoneyE2))
    }

}