package cn.sancell.xingqiu.live.adapter

import android.view.View
import android.widget.ImageView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveRelevCommInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class LiveAddGroupAdapter(data: List<LiveRelevCommInfo>) : BaseQuickAdapter<LiveRelevCommInfo, BaseViewHolder>(R.layout.view_livew_add_group_layout, data) {
    override fun convert(helper: BaseViewHolder, item: LiveRelevCommInfo) {
        helper.getView<View>(R.id.tv_delete).setOnClickListener {

            onItemClickListener?.onItemClick(null, null, helper.layoutPosition)
        }
        val udl_group_icon = helper.getView<ImageView>(R.id.udl_group_icon)
        ImageLoaderUtils.loadImage(mContext, item.icon, udl_group_icon)
        helper.setText(R.id.tv_title, item.groupName)
        helper.setText(R.id.tv_group_code, "群号：" + item.tid)
        helper.setText(R.id.tv_group_name, "群主：" + item.ownerUserName)
    }
}