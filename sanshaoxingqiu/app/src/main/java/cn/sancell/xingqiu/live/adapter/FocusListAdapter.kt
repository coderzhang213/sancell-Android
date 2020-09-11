package cn.sancell.xingqiu.live.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.FocusBean
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.bean.LiverBean
import cn.sancell.xingqiu.util.AppUtils
import cn.sancell.xingqiu.util.FansUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 关注/粉丝列表
 */
class FocusListAdapter(data: MutableList<FocusBean>) : BaseQuickAdapter<FocusBean, BaseViewHolder>(R.layout.recycle_live_liver, data) {

    private var mListener: OnFocusListener? = null

    private var limitCount = 0

    private var userId = ""

    fun setLimitDisplayCount(count: Int) {
        limitCount = count
    }

    init {
        userId = AppUtils.getUserId()
    }

    override fun getItemCount(): Int {
        if (limitCount > 0) {
            return if (limitCount >= data.size) {
                super.getItemCount()
            } else {
                limitCount
            }
        }
        return super.getItemCount()
    }

    override fun convert(helper: BaseViewHolder, item: FocusBean) {

        val btn = helper.getView<AppCompatTextView>(R.id.btn_focus)

        if (userId == item.tvUserId) {
            btn.visibility = View.GONE
        } else {
            btn.visibility = View.VISIBLE
        }

        when {
            item.followStatus == 0 -> {
                btn.isSelected = true
                btn.text = "+关注"
            }
            item.followStatus == 1 -> {
                btn.isSelected = false
                btn.text = "已关注"
            }
            else -> {
                btn.isSelected = false
                btn.text = "相互关注"
            }
        }
        val tvName = helper.getView<AppCompatTextView>(R.id.tv_name)
        tvName.text = item.tvUserName

        val img = helper.getView<AppCompatImageView>(R.id.iv_profile)
        ImageLoaderUtils.loadCircleImage(mContext, item.tvUserGravatar, img)

        helper.getView<AppCompatTextView>(R.id.tv_fans_num).text =
                FansUtils.getFansOrFocusStr(mContext.getString(R.string.fans), item.fansCount)

        if (item.isVip == 1) {
            val iconVip = mContext.resources.getDrawable(R.mipmap.vip_icon, null)
            iconVip.setBounds(0, 0, iconVip.minimumWidth, iconVip.minimumHeight)
            tvName.setCompoundDrawables(null, null, iconVip, null)
        } else {
            tvName.setCompoundDrawables(null, null, null, null)
        }

        btn.setOnClickListener {
            if (mListener != null) {
                mListener!!.onFocus(helper.adapterPosition, item)
            }
        }
        helper.itemView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(item)
            }
        }

    }

    interface OnFocusListener {
        fun onFocus(position: Int, data: FocusBean)
        fun onItemClick(data: FocusBean)
    }

    fun setFocusListener(listener: OnFocusListener) {
        mListener = listener
    }

}