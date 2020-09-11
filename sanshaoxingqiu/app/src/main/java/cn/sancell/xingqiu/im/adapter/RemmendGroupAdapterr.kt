package cn.sancell.xingqiu.homecommunity.live

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.homecommunity.bean.RecommendGroupListBean
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author Alan_Xiong
 * @desc: 直播列表适配器
 * @time 2019-11-27 13:30
 */
class RemmendGroupAdapterr(data: List<RecommendGroupListBean.RecommGroupBean?>?) : BaseMultiItemQuickAdapter<RecommendGroupListBean.RecommGroupBean?, BaseViewHolder?>(data) {


    init {
        addItemType(0, R.layout.view_rem_group_layout)
    }

    override fun convert(helper: BaseViewHolder?, item: RecommendGroupListBean.RecommGroupBean?) {
        val iv_group_icon = helper!!.getView<ImageView>(R.id.iv_group_icon)
        if (TextUtils.isEmpty(item?.icon)) {
            iv_group_icon.setImageResource(R.mipmap.icon_def_team)
        } else {
            ImageLoaderUtils.loadRoundImage(mContext, item?.icon, iv_group_icon, 10)
        }
        helper.getView<TextView>(R.id.tv_group_name).setText(item?.groupName)
        helper.getView<TextView>(R.id.tv_group_aved).setText(item?.intro)
        val tv_statues = helper.getView<TextView>(R.id.tv_statues)
        if (item?.inGroup == 0) {
            tv_statues.setBackgroundResource(R.mipmap.icon_apply_join)
        } else {
            tv_statues.setBackgroundResource(R.mipmap.icon_join_group_chat)
        }
        helper.addOnClickListener(R.id.tv_statues)

    }
}