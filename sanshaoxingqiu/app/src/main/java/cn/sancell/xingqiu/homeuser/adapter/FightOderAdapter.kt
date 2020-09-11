package cn.sancell.xingqiu.homeuser.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.FightItemInfo
import cn.sancell.xingqiu.constant.CountdownManager
import cn.sancell.xingqiu.constant.FightCountdownHelp
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.goods.GoodsDetailActivity
import cn.sancell.xingqiu.util.DateUtils
import cn.sancell.xingqiu.util.PriceUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zj on 2020/1/3.
 */
class FightOderAdapter(data: MutableList<FightItemInfo>) : BaseMultiItemQuickAdapter<FightItemInfo, BaseViewHolder>(data) {
    private val timeMap = HashMap<TextView, FightCountdownHelp>()
    private var mOnFigheClickLinsern: OnFigheClickLinsern? = null

    init {
        //1：团购中；2已完成；3拼团失败(不足要求）4已取消；
        addItemType(1, R.layout.view_fight_cencar_layout)
    }

    fun setOnClickLinsener(lins: OnFigheClickLinsern) {
        this.mOnFigheClickLinsern = lins
    }

    /**
     * 取消所有倒计时
     */
    fun cancerCountdown() {
        if (timeMap.size <= 0) {
            return
        }
        var allTime: Set<MutableMap.MutableEntry<TextView, FightCountdownHelp>>? = timeMap.entries
        var it: Iterator<*>? = allTime!!.iterator()
        it?.apply {
            while (this.hasNext()) {
                try {
                    val pairs = this.next() as MutableMap.MutableEntry<*, *>
                    val cdt: FightCountdownHelp? = pairs.value as FightCountdownHelp
                    cdt?.apply {
                        cencar()
                    }
                    it = null
                } catch (e: Exception) {
                }
            }
            timeMap.clear()
        }

    }


    override fun convert(helper: BaseViewHolder, item: FightItemInfo) {
        val grouponOrderStatus = item.grouponOrderStatus
        val rl_other = helper.getView<View>(R.id.rl_other)
        val rl_processing = helper.getView<View>(R.id.rl_processing)
        val tv_fri_time = helper.getView<TextView>(R.id.tv_fri_time)
        helper.getView<View>(R.id.tv_query_oder).setOnClickListener {
            mOnFigheClickLinsern?.onQueryOderLinsener(item)

        }
        helper.getView<View>(R.id.tv_yq_first).setOnClickListener {
            mOnFigheClickLinsern?.onInviteFriendsLinsern(item)
        }
        helper.itemView.setOnClickListener {
            mOnFigheClickLinsern?.onItemClickListener(item)
        }
        //如果有这个值要先取消
        timeMap.get(tv_fri_time)?.apply {
            cencar()
            timeMap.remove(tv_fri_time)
        }
        if (grouponOrderStatus == 1) {
            rl_processing.visibility = View.VISIBLE
            rl_other.visibility = View.GONE
        } else {
            rl_processing.visibility = View.GONE
            rl_other.visibility = View.VISIBLE
        }
        val tv_status = helper.getView<TextView>(R.id.tv_status)
        helper.setText(R.id.tv_title, item.goodsTitle)
        helper.setText(R.id.tv_price, PriceUtils.getInstance().getPriceWithSyp(item.grouponPriceE2))
        val iv_come_sp = helper.getView<ImageView>(R.id.iv_come_sp)
        val tv_price_status = helper.getView<TextView>(R.id.tv_price_status)
        ImageLoaderUtils.loadImage(mContext, item.goodsIcon, iv_come_sp)
        helper.setText(R.id.tv_copy, item.grouponNum.toString() + "人团")
        val tv_other_time = helper.getView<TextView>(R.id.tv_other_time)
        iv_come_sp.setOnClickListener {
            GoodsDetailActivity.start(mContext, item.goodsId.toInt())
        }
        when (grouponOrderStatus) {
            1 -> {//团购中
                tv_status.setText("直拼中")
                tv_status.setTextColor(Color.parseColor("#FFFFAA00"))
                tv_price_status.visibility = View.GONE
                helper.setText(R.id.tv_pt_cr, (item.lastUserNum.toString() + "人"))
                if (CountdownManager.get().getCurrTime() == 0L) {
                    CountdownManager.get().initTime(item.currentTime)
                }
                if (item.grouponEndTime < item.currentTime || item.grouponEndTime < CountdownManager.get().getCurrTime()) {
                    tv_fri_time.setText("已结束")
                } else {
                    //启动一个倒计时
                    val mTimeCountdown = FightCountdownHelp(tv_fri_time, CountdownManager.get().getCurrTime() * 1000, item.grouponEndTime * 1000)
                    mTimeCountdown.start()
                    timeMap.put(tv_fri_time, mTimeCountdown)
                }


            }
            2 -> {//已完成
                tv_status.setText("直拼成功")
                tv_status.setTextColor(Color.parseColor("#FFFA1905"))
                tv_price_status.visibility = View.GONE
            }
            3 -> {//拼团失败(不足要求
                tv_status.setText("直拼失败")
                tv_status.setTextColor(Color.parseColor("#FF242526"))
                tv_price_status.visibility = View.VISIBLE
                setTkStatus(tv_price_status, item.payStatus)
                tv_other_time.visibility = View.VISIBLE
                tv_other_time.setText("该团于 " + DateUtils.getStrTime1F(item.grouponEndTime.toString()) + " 解散")
            }
            4 -> {//取消
                tv_status.setText("直拼取消")
                tv_status.setTextColor(Color.parseColor("#FF242526"))
                tv_price_status.visibility = View.VISIBLE
                tv_other_time.visibility = View.GONE
                setTkStatus(tv_price_status, item.payStatus)
            }
        }
    }

    fun setTkStatus(tv_price_status: TextView, payStatus: Int) {
        when (payStatus) {
            4 -> {
                tv_price_status.setText("退款中")
                tv_price_status.setTextColor(Color.parseColor("#FFFA1905"))
            }
            5 -> {
                tv_price_status.setText("已退款")
                tv_price_status.setTextColor(Color.parseColor("#FF87898C"))
            }
        }

    }

    public interface OnFigheClickLinsern {
        fun onInviteFriendsLinsern(mFightItemInfo: FightItemInfo)
        fun onQueryOderLinsener(mFightItemInfo: FightItemInfo)
        fun onItemClickListener(mFightItemInfo: FightItemInfo)

    }
}