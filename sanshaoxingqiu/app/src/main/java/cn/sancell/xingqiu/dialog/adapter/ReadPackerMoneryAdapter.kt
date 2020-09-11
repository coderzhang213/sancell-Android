package cn.sancell.xingqiu.dialog.adapter

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ReadPackInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class ReadPackerMoneryAdapter(data: List<ReadPackInfo>) : BaseQuickAdapter<ReadPackInfo, BaseViewHolder>(R.layout.dialog_read_pack_layout, data) {
    private var itemWidth: Int = 0
    private var selectIndex = 0

    init {
        itemWidth = (ScreenUtil.screenWidth - ScreenUtil.dip2px(10f) * 2) / 4
    }

    override fun convert(helper: BaseViewHolder, item: ReadPackInfo) {//read_pack_bg
        val ll_conent = helper.getView<LinearLayout>(R.id.ll_conent)
        ll_conent?.layoutParams?.width = itemWidth
        ll_conent?.layoutParams?.height = itemWidth
        val ll_click = helper.getView<LinearLayout>(R.id.ll_click)
        val tv_text = helper.getView<TextView>(R.id.tv_text)
        tv_text.setText("¥" + item.money)
        if (selectIndex == helper.layoutPosition) {
            ll_click.setBackgroundResource(R.drawable.read_pack_bg)
            tv_text.setTextColor(Color.parseColor("#FA1905"))
        } else {
            ll_click.setBackgroundResource(R.drawable.live_read_pack_shape)
            tv_text.setTextColor(Color.parseColor("#242526"))
        }

        ll_click.setOnClickListener {

            selectIndex = helper.layoutPosition
            notifyDataSetChanged()
        }
    }

    /**
     * 获取选择红包
     */
    fun getSelectReadPack(): ReadPackInfo {
        return data.get(selectIndex)
    }
}