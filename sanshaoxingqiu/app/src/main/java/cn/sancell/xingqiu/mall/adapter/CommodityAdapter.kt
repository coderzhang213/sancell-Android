package cn.sancell.xingqiu.mall.adapter

import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.CommdoityData
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.mall.MallListActivity
import cn.sancell.xingqiu.util.SeekBarUtils
import cn.sancell.xingqiu.widget.roundimageview.RoundedImageView
import cn.sancell.xingqiu.widget.roundimageview.UserDefinedCircleImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_livew_list_layout.*

/**
 * Created by zj on 2019/12/27.
 */
class CommodityAdapter(data: MutableList<CommdoityData>) : BaseMultiItemQuickAdapter<CommdoityData, BaseViewHolder>(data) {
    init {
        addItemType(0, R.layout.view_comment_item_layuot)

    }

    override fun convert(helper: BaseViewHolder, item: CommdoityData) {
        val rl_com_list = helper.getView<RecyclerView>(R.id.rl_com_list)
        if (item.goods.size > 0) {
            val adapter = rl_com_list.adapter
            if (adapter != null) {
                val mCommodityChileAdapter = adapter as CommodityChileAdapter
                mCommodityChileAdapter.setNewData(item.goods)
            } else {
                rl_com_list.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                rl_com_list.adapter = CommodityChileAdapter(item.goods)
            }
        }
        val iv_commen_icon = helper.getView<UserDefinedCircleImageView>(R.id.iv_commen_icon)
        ImageLoaderUtils.loadImage(mContext, item.coverPic, iv_commen_icon, R.drawable.default_bg_shape)
        helper.setText(R.id.tv_title, item.name)
        helper.setText(R.id.tv_desc, item.desc)
        val tl_int = helper.getView<View>(R.id.tl_int)
        if (item.goods.size > 3) {//商品大于3个才显示
            val slide_indicator_point = helper.getView<SeekBar>(R.id.slide_indicator_point)
            tl_int.visibility = View.VISIBLE
            SeekBarUtils.helpSeekBarRe(rl_com_list, slide_indicator_point)
        } else {
            tl_int.visibility = View.GONE
        }
        helper.getView<View>(R.id.tv_all_query).setOnClickListener {
            // 跳转到商品列表
            MallListActivity.startInenet(mContext, item.id, item.name)
        }

    }
}