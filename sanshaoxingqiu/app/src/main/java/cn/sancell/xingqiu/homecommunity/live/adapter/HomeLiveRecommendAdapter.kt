package cn.sancell.xingqiu.homecommunity.live.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.OnLiveFowlloClcikLInsenr
import cn.sancell.xingqiu.util.AppUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.TimeUtil

/**
 * Created by zj on 2020/5/9.
 */
class HomeLiveRecommendAdapter(data: MutableList<LiveFollowInfo>, mOnLiveFowlloClcikLInsenr: OnLiveFowlloClcikLInsenr) : BaseMultiItemQuickAdapter<LiveFollowInfo, BaseViewHolder>(data) {
    private var mOnLiveFowlloClcikLInsenr: OnLiveFowlloClcikLInsenr? = null
    private var mUserId = ""

    init {
        this.mOnLiveFowlloClcikLInsenr = mOnLiveFowlloClcikLInsenr
        /**1 未开始（预告）， 2直播中， 3 已结束（回放） **/
        addItemType(1, R.layout.view_home_live_notice)
        addItemType(2, R.layout.view_home_live_cancer)
        addItemType(3, R.layout.view_home_live_replay)
        mUserId = AppUtils.getUserId()
    }


    override fun convert(helper: BaseViewHolder, item: LiveFollowInfo) {
        var iv_live_icon: ImageView? = null
        var uci_icon: ImageView? = null
        when (item.liveStatus) {//        /**1 未开始（预告）， 2直播中， 3 已结束（回放） **/
            "1" -> {//预告
                iv_live_icon = helper.getView(R.id.iv_live_icon)
                helper.setText(R.id.tv_live_title, item.liveName)
                helper.setText(R.id.tv_name, item.tvUserName)
                helper.setText(R.id.tv_audience_num, item.reserveCount + "人预约")
                helper.setText(R.id.tv_start_time, TimeUtil.getDateFromMMDD(item.reserveStartLiveTime * 1000))
                uci_icon = helper.getView(R.id.uci_icon)
                val tv_give_f = helper.getView<TextView>(R.id.tv_give_f) //live_yi_list_give_shape

                //同一个用户不显示预约
                //同一个用户不显示预约
                if (TextUtils.equals(item.userId, mUserId)) {
                    tv_give_f.visibility = View.GONE
                } else {
                    tv_give_f.visibility = View.VISIBLE
                    if (item.isReserve == "1") {
                        tv_give_f.text = "预约成功"
                        tv_give_f.setTextColor(Color.parseColor("#BABCBF"))
                        tv_give_f.setBackgroundResource(R.drawable.live_yi_list_give_shape)
                    } else {
                        tv_give_f.text = "立即预约"
                        tv_give_f.setTextColor(Color.parseColor("#FA1905"))
                        tv_give_f.setBackgroundResource(R.drawable.live_list_give_shape)
                    }
                }
                tv_give_f.setOnClickListener { v: View? ->
                    if (item.isReserve == "0") {
                        item.isReserve = "1"
                        mOnLiveFowlloClcikLInsenr?.onMakeLinser("1", item)
                    } else {
                        item.isReserve = "0"
                        mOnLiveFowlloClcikLInsenr?.onMakeLinser("2", item)
                    }
                    notifyItemChanged(helper.layoutPosition)
                }
            }
            "2" -> {//直播中
                iv_live_icon = helper.getView(R.id.iv_live_icon)
                val iv_foos = helper.getView<ImageView>(R.id.iv_foos)
                Glide.with(mContext).load(R.drawable.cancer_zan).into(iv_foos)
                helper.setText(R.id.tv_live_title, item.liveName)
                uci_icon = helper.getView(R.id.uci_icon)

                helper.setText(R.id.tv_name, item.tvUserName)
                val iv_coupon = helper.getView<ImageView>(R.id.iv_coupon)
                if (item.isBindingCoupon) {
                    iv_coupon.visibility=View.VISIBLE
                } else {
                    iv_coupon.visibility=View.GONE
                }
                helper.setText(R.id.tv_live_coutn, (item.onlineUser.toString() + "在看"))
                val iv_play_icon = helper.getView<ImageView>(R.id.iv_play_icon)
                Glide.with(mContext).load(R.drawable.living).into(iv_play_icon)

                showCommList(item, helper.getView(R.id.cl_bom), helper.getView(R.id.iv_img_1), helper.getView(R.id.iv_img_2), helper.getView(R.id.iv_img_3), helper.getView(R.id.tv_com_count))

            }
            "3" -> {//回放
                iv_live_icon = helper.getView(R.id.iv_live_icon)
                helper.setText(R.id.tv_live_title, item.liveName)
                helper.setText(R.id.tv_name, item.tvUserName)
                helper.setText(R.id.tv_audience_num, item.replayWatchUser.toString() + "看过")
                helper.setText(R.id.tv_re_time, TimeUtil.getDateFromMMDD(item.replayDate * 1000))
                showCommList(item, helper.getView(R.id.cl_bom), helper.getView(R.id.iv_img_1), helper.getView(R.id.iv_img_2), helper.getView(R.id.iv_img_3), helper.getView(R.id.tv_com_count))
                uci_icon = helper.getView(R.id.uci_icon)

            }
        }
        iv_live_icon?.apply {
            Glide.with(mContext).load(item.icon).into(this)
            // ImageLoaderUtils.loadImage(mContext, item.icon, this)
        }
        uci_icon?.apply {
            ImageLoaderUtils.loadImage(mContext, item.tvUserGravatar, this)
        }
        helper.itemView.setOnClickListener {
            mOnLiveFowlloClcikLInsenr?.onItemClickLinsener(item)
        }


    }

    fun showCommList(item: LiveFollowInfo, cl_bom: View, img_1: ImageView, img_2: ImageView, img_3: ImageView, remCount: TextView) {
        val list = item.relationGoodsList
        if (list == null) {
            cl_bom.visibility = View.GONE
            return
        }
        if (list.size >= 1) {
            cl_bom.visibility = View.VISIBLE
            img_1.visibility = View.VISIBLE
            Glide.with(mContext).load(list.get(0).coverPicThumb).into(img_1)

        } else {
            cl_bom.visibility = View.GONE
            img_1.visibility = View.GONE
        }
        if (list.size >= 2) {
            img_2.visibility = View.VISIBLE
            Glide.with(mContext).load(list.get(1).coverPicThumb).into(img_2)
        } else {
            img_2.visibility = View.GONE
        }
        if (list.size >= 3) {
            img_3.visibility = View.VISIBLE
            remCount.visibility = View.VISIBLE
            remCount.text = "+" + list.size
            Glide.with(mContext).load(list.get(2).coverPicThumb).into(img_3)
        } else {
            img_3.visibility = View.GONE
            remCount.visibility = View.GONE
        }
    }
}