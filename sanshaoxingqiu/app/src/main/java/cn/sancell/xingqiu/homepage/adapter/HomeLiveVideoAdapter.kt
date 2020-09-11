package cn.sancell.xingqiu.homepage.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.entity.BaseEntity
import cn.sancell.xingqiu.bean.LiveFollowInfo
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.util.DateUtils
import cn.sancell.xingqiu.util.FansUtils
import cn.sancell.xingqiu.util.ScreenUtils
import cn.sancell.xingqiu.widget.verticalviewpager.VerticalViewPager2
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 首页推荐列表
 */
class HomeLiveVideoAdapter(data: List<LiveFollowInfo>) : BaseQuickAdapter<LiveFollowInfo, BaseViewHolder>(R.layout.recycle_home_live_video_living, data) {


    override fun convert(helper: BaseViewHolder, item: LiveFollowInfo) {

        val iv_video_thumb = helper.getView<AppCompatImageView>(R.id.iv_video_thumb)
        val iv_live_status = helper.getView<AppCompatImageView>(R.id.iv_live_status)
        val tv_looking_num = helper.getView<AppCompatTextView>(R.id.tv_looking_num)

        val iv_take_voucher = helper.getView<AppCompatImageView>(R.id.iv_take_voucher)
        val iv_like_gif = helper.getView<AppCompatImageView>(R.id.iv_like_gif)
        val tv_video_title = helper.getView<AppCompatTextView>(R.id.tv_video_title)
        val tv_video_tag = helper.getView<AppCompatTextView>(R.id.tv_video_tag)
        val iv_liver_avatar = helper.getView<AppCompatImageView>(R.id.iv_liver_avatar)
        val tv_liver_name = helper.getView<AppCompatTextView>(R.id.tv_liver_name)
        //预告 人数
        val tv_notice_count = helper.getView<AppCompatTextView>(R.id.tv_notice_count)
        //回放 喜欢
        val tv_replay_like = helper.getView<AppCompatTextView>(R.id.tv_replay_like)

        //商品列表
        val rv_goods = helper.getView<RecyclerView>(R.id.rv_goods)


        //标题
        tv_video_title.text = item.liveName
        //标签
        if (helper.adapterPosition % 3 == 0){
            tv_video_tag.visibility = View.VISIBLE
        }else{
            tv_video_tag.visibility = View.GONE
        }
        item.tvUserGravatar.let {
            ImageLoaderUtils.loadCircleImage(mContext, it, iv_liver_avatar)
        }
        tv_liver_name.text = item.tvUserName
        item.icon.let {
            ImageLoaderUtils.loadRound2Image(mContext, it, iv_video_thumb, 12)
        }

        //关联的商品列表
        if (item.relationGoodsList.isNullOrEmpty()) {
            rv_goods.visibility = View.GONE
        } else {
            rv_goods.visibility = View.VISIBLE
            //防止滑动
            val mLinearLayoutManager: LinearLayoutManager = object : LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            rv_goods.layoutManager = mLinearLayoutManager


            if (item.relationGoodsList.size >= 3) {
                item.relationGoodsList = item.relationGoodsList.subList(0, 3)
            }
            //分割线，防止重复绑定
            if (rv_goods.itemDecorationCount == 0) {
                val decoration = DividerItemDecoration(mContext, RecyclerView.HORIZONTAL)
                ContextCompat.getDrawable(mContext, R.drawable.shape_live_video_goods_divider_6)?.let { decoration.setDrawable(it) }
                rv_goods.addItemDecoration(decoration)
            }
            val mGoodsAdapter = HomeLiveVideoGoodsAdapter(item.relationGoodsList)

            //数据测量出来后
            iv_video_thumb.post {
                mGoodsAdapter.setViewWidth(iv_video_thumb.width - ScreenUtils.dip2px(mContext, 17.5f))//减去设计图的margin
                        .setItemMargin(ScreenUtils.dip2px(mContext, 12f))
                        .setGoodsCount(item.relationGoodsCount.toInt())
                rv_goods.adapter = mGoodsAdapter
            }

        }
//        if (helper.adapterPosition % 3 == 2) {
//            item.liveStatus = "2"
//        } else if (helper.adapterPosition % 3 == 1) {
//            item.liveStatus = "1"
//        }

        //1 未开始（预告）， 2直播中， 3 已结束（回放）
        when (item.liveStatus) {
            "1" -> {
                //无点赞，无领券
                val params = iv_live_status.layoutParams
                params.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                params.height = params.width
                iv_live_status.layoutParams = params
                iv_live_status.setImageResource(R.mipmap.icon_home_live_video_status_notice)

                iv_like_gif.visibility = View.GONE
                Glide.with(mContext).load(R.drawable.cancer_zan).into(iv_like_gif)

                iv_take_voucher.visibility = View.GONE
                tv_looking_num.text = DateUtils.getTimeWithMonthDayHourMinute(item.reserveStartLiveTime)
                tv_replay_like.visibility = View.GONE
                tv_notice_count.visibility = View.VISIBLE
                tv_notice_count.text = FansUtils.getFansOrFocusStr(mContext.getString(R.string.video_already_appointment), item.reserveCount.toInt())
            }
            "2" -> {
                //直播状态 显示动态心，领券
                iv_like_gif.visibility = View.VISIBLE
                iv_take_voucher.visibility = View.VISIBLE

                //修改图标尺寸
                val params = iv_live_status.layoutParams
                params.width = ScreenUtils.dip2px(mContext, 18f)
                params.height = params.width
                iv_live_status.layoutParams = params

                Glide.with(mContext).load(R.drawable.icon_home_live_video_status_living).into(iv_live_status)
                tv_looking_num.text = FansUtils.getFansOrFocusStr(mContext.getString(R.string.video_is_looking), item.onlineUser)
                if (item.isBindingCoupon) {
                    iv_take_voucher.visibility = View.VISIBLE
                } else {
                    iv_take_voucher.visibility = View.GONE
                }
                tv_replay_like.visibility = View.GONE
                tv_notice_count.visibility = View.GONE
            }
            "3" -> {
                //回放 显示点赞人数
                val params = iv_live_status.layoutParams
                params.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                params.height = params.width
                iv_live_status.layoutParams = params

                iv_live_status.setImageResource(R.mipmap.icon_home_live_video_status_replay)
                iv_like_gif.visibility = View.GONE
                Glide.with(mContext).load(R.drawable.cancer_zan).into(iv_like_gif)
                iv_take_voucher.visibility = View.GONE
                tv_looking_num.text = FansUtils.getFansOrFocusStr(mContext.getString(R.string.video_already_look), item.replayWatchUser)
                tv_replay_like.visibility = View.VISIBLE
                tv_replay_like.text = FansUtils.getNumCount(item.likeNumber)
                tv_notice_count.visibility = View.GONE
            }
        }
    }

}