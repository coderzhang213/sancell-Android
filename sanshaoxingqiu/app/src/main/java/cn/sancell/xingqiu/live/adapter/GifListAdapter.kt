package cn.sancell.xingqiu.live.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.glide.ImageLoaderUtils
import cn.sancell.xingqiu.interfaces.HolederBindDatLinsener
import cn.sancell.xingqiu.live.adapter.viewholder.GiftHolder
import cn.sancell.xingqiu.live.widget.extension.RedEnvelopeInfo
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by zj on 2020/4/2.
 */
class GifListAdapter(mContext: Context, data: List<RedEnvelopeInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mList: List<RedEnvelopeInfo>? = null
    var mContext: Context? = null

    init {
        this.mList = data
        this.mContext = mContext
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GiftHolder(mContext!!, View.inflate(mContext, R.layout.view_gif_list_item_layout, null))
    }

    override fun getItemCount(): Int {
        mList?.apply {

            return size
        }
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mList?.apply {
            if (holder is GiftHolder) {
                val info = get(position)
                holder.onBindViewData(info)
            }
        }

    }


}