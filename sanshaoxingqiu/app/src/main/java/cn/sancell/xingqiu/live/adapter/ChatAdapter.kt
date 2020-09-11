package cn.sancell.xingqiu.live.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.bean.ChatRes
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 聊天
 */
class ChatAdapter(data: MutableList<ChatRes>) : BaseMultiItemQuickAdapter<ChatRes, BaseViewHolder>(data) {

    init {
        addItemType(UiHelper.chat_left_msg, R.layout.recycle_chat_left_msg)
        addItemType(UiHelper.chat_right_msg, R.layout.recycle_chat_right_msg)
        addItemType(UiHelper.chat_no_response, R.layout.recycle_chat_no_response)
        addItemType(UiHelper.chat_day_time, R.layout.recycle_chat_time)
    }

    override fun convert(helper: BaseViewHolder, item: ChatRes?) {
        if (item == null) {
            return
        }
        when {
            item.itemType == UiHelper.chat_left_msg -> {

                val img = helper.getView<AppCompatImageView>(R.id.iv_img)
                ImageLoaderUtils.loadCircleImage(mContext,item.friendGravatar,img)
                helper.getView<AppCompatTextView>(R.id.tv_left_chat).text = item.msgContent


            }
            item.itemType == UiHelper.chat_right_msg -> {
                val img = helper.getView<AppCompatImageView>(R.id.iv_img)
                ImageLoaderUtils.loadCircleImage(mContext,item.userGravatar,img)
                helper.getView<AppCompatTextView>(R.id.tv_right_chat).text = item.msgContent

            }
            item.itemType == UiHelper.chat_day_time -> helper.getView<AppCompatTextView>(R.id.tv_chat_time).text = "2020年2月24日"
            else -> {

            }
        }
    }

}