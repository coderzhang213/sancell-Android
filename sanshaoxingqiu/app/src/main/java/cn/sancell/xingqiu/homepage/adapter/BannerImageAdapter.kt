package cn.sancell.xingqiu.homepage.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import com.youth.banner.adapter.BannerAdapter

/**
 * banner adapter
 */
class BannerImageAdapter(val mContext:Context, data: List<String>) : BannerAdapter<String, BannerImageAdapter.BannerViewHolder>(data) {

    class BannerViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder?, data: String?, position: Int, size: Int) {
        ImageLoaderUtils.loadImage(mContext,data, holder?.imageView)
    }
}