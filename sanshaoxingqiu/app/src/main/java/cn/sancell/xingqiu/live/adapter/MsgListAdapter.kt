package cn.sancell.xingqiu.live.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.ChatRes
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.util.DateUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lcw.library.imagepicker.utils.DataUtil

class MsgListAdapter(data: MutableList<ChatRes>) : BaseQuickAdapter<ChatRes, BaseViewHolder>(R.layout.recycle_letter_mian, data) {

    private var mListener: OnItemClickListener? = null
    override fun convert(helper: BaseViewHolder, item: ChatRes) {

        val ivHead = helper.getView<AppCompatImageView>(R.id.iv_img)
        ImageLoaderUtils.loadCircleImage(mContext, item.friendGravatar, ivHead)
        helper.setText(R.id.tv_user_name, item.friendNickName)
        helper.setText(R.id.tv_msg, item.msgContent)
        helper.setText(R.id.tv_msg_time, DateUtils.getTimeByStampWithYear(item.sendTime))
        val dot = helper.getView<View>(R.id.red_dot)
        if (item.readStatus == 1) {
            dot.visibility = View.VISIBLE
        } else {
            dot.visibility = View.GONE
        }
        helper.itemView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(item,helper.adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(data: ChatRes,position:Int)
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

}