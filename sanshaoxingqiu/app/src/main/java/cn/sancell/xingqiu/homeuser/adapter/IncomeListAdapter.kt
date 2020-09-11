package cn.sancell.xingqiu.homeuser.adapter

import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.IncomeChiledInfo
import cn.sancell.xingqiu.util.PriceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.TimeUtil

/**
 * Created by zj on 2020/6/17.
 */
class IncomeListAdapter(data: List<IncomeChiledInfo>) : BaseQuickAdapter<IncomeChiledInfo, BaseViewHolder>(R.layout.view_income_item_layout, data) {
    override fun convert(helper: BaseViewHolder, item: IncomeChiledInfo) {
        if (item.shareType.equals("1")) {
            helper.setText(R.id.tv_name, "粉丝分润")
        } else {
            helper.setText(R.id.tv_name, "")
        }

        helper.setText(R.id.tv_money, PriceUtils.getInstance().getPrice(item.fansSharing2))
        helper.setText(R.id.tv_nike_name, item.fansUserName)
        helper.setText(R.id.tv_id, "ID:" + item.fansUserId)
        helper.setText(R.id.tv_time, TimeUtil.getDateFromYYYYMMDDHHMMSS(System.currentTimeMillis()))
    }
}