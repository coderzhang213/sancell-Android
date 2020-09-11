package cn.sancell.xingqiu.homecommunity.live.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.bean.LiveRemmInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.OnAdapterItenClickLinseer
import cn.sancell.xingqiu.util.StringUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import com.netease.nim.uikit.common.util.sys.TimeUtil

class LiveRemAdapter(data: List<LiveRemmInfo>) : BaseQuickAdapter<LiveRemmInfo, BaseViewHolder>(R.layout.view_live_rem_layout, data) {
    private var mOnAdapterItenClickLinseer: OnAdapterItenClickLinseer? = null


    fun setOnAdapterItenClickLinseer(mOnAdapterItenClickLinseer: OnAdapterItenClickLinseer) {
        this.mOnAdapterItenClickLinseer = mOnAdapterItenClickLinseer
    }

    override fun convert(helper: BaseViewHolder, item: LiveRemmInfo) {
        val ll_conent = helper.getView<RelativeLayout>(R.id.ll_conent)
        if (helper.layoutPosition < itemCount - 1) {
            val params = ll_conent.layoutParams as RecyclerView.LayoutParams
            params.rightMargin = ScreenUtil.dip2px(10f)
        }
        ll_conent.setOnClickListener {
            mOnAdapterItenClickLinseer?.onCliclLinsener(item, helper.layoutPosition)
        }
        val ll_play_cancer = helper.getView<LinearLayout>(R.id.ll_play_cancer)
        val ll_re_play = helper.getView<LinearLayout>(R.id.ll_re_play)
        val ll_give = helper.getView<RelativeLayout>(R.id.ll_give)
        val uci_icon = helper.getView<ImageView>(R.id.uci_icon)
        val iv_commen_icon = helper.getView<ImageView>(R.id.iv_commen_icon)
        val ll_comm = helper.getView<LinearLayout>(R.id.ll_comm)
        if (item.dataType.equals("1")) {//直播数据
            helper.setText(R.id.tv_name, item.tvUserName)
            ll_comm.visibility = View.GONE
            ImageLoaderUtils.loadImage(mContext, item.tvUserGravatar, uci_icon)
            uci_icon.visibility = View.VISIBLE
            when (item.liveStatus) {//1 未开始（预告）， 2直播中， 3 已结束（回放）
                "1" -> {
                    ll_give.visibility = View.VISIBLE
                    ll_play_cancer.setVisibility(View.GONE)
                    ll_re_play.setVisibility(View.GONE)
                    ll_give.setBackgroundResource(0)
                }
                "2" -> {
                    val iv_play_icon = helper.getView<ImageView>(R.id.iv_play_icon)
                    Glide.with(mContext).load(R.drawable.living).into(iv_play_icon)
                    ll_give.visibility = View.GONE
                    ll_play_cancer.visibility = View.VISIBLE
                    ll_re_play.visibility = View.GONE
                }
                "3" -> {
                    val tv_re_time = helper.getView<TextView>(R.id.tv_re_time)
                    tv_re_time.visibility = View.GONE
                    //   tv_re_time.text = TimeUtil.getDateFromMMDD(item.replayDate * 1000)
                    ll_give.visibility = View.GONE
                    ll_play_cancer.visibility = View.GONE
                    ll_re_play.visibility = View.VISIBLE
                }
            }
            ImageLoaderUtils.loadImage(mContext, item.icon, iv_commen_icon)
            helper.setText(R.id.tv_gves, item.liveName)
        } else {//商品数据
            ll_comm.visibility = View.VISIBLE
            ImageLoaderUtils.loadImage(mContext, item.goodsCoverPic, iv_commen_icon)
            uci_icon.visibility = View.GONE
            helper.setText(R.id.tv_name, String.format(mContext.resources.getString(R.string.price_rmb), StringUtils.getAllPrice(item.grouponPriceE2)))
            helper.setText(R.id.tv_gves, item.goodsTitle)
        }
    }

}