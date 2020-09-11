package cn.sancell.xingqiu.live.adapter

import android.view.View
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ReportItemBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 举报
 */
class ReportItemAdapter(data: List<ReportItemBean>) : BaseQuickAdapter<ReportItemBean, BaseViewHolder>(R.layout.recycle_report_item, data) {
    override fun convert(helper: BaseViewHolder, item: ReportItemBean) {
        helper.setText(R.id.tv_name, item.name)
        val line = helper.getView<View>(R.id.iv_line)
        if (helper.adapterPosition == data.size - 1) {
            line.visibility = View.GONE
        } else {
            line.visibility = View.VISIBLE
        }
    }

}