package cn.sancell.xingqiu.homecommunity.adapter

import android.widget.ImageView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes
import cn.sancell.xingqiu.login.bean.UserBean
import cn.sancell.xingqiu.util.FontUtils
import cn.sancell.xingqiu.util.PreferencesUtils
import cn.sancell.xingqiu.widget.roundimageview.UserDefinedCircleImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zj on 2019/12/26.
 */
class VideoPlayRemAdapter(data: List<VideoRelationRes.InfoList>) : BaseQuickAdapter<VideoRelationRes.InfoList, BaseViewHolder>(R.layout.view_video_rem_layout, data) {

    init {
        val userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean::class.java)


    }

    override fun convert(helper: BaseViewHolder, item: VideoRelationRes.InfoList) {
        val iv_commen_icon = helper.getView<ImageView>(R.id.iv_commen_icon)
        ImageLoaderUtils.loadImage(mContext, item.coverPicThumb, iv_commen_icon)
        helper.setText(R.id.tv_title, item.title)

        helper.setText(R.id.tv_price, FontUtils.getInstance().changeTextSize(mContext, 14, String.format(mContext.resources.getString(R.string.price_rmb), item.relPrice), 0, 1))

        helper.addOnClickListener(R.id.ll_par_conet)
        helper.addOnClickListener(R.id.iv_add_cart)

    }
}