package cn.sancell.xingqiu.util

import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView

object SeekBarUtils {
    fun helpSeekBarRe(mRecyclerView: RecyclerView, mSeekBar: SeekBar) {
        mSeekBar.setPadding(0, 0, 0, 0)
        mSeekBar.setThumbOffset(0)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //显示区域的高度。
                //显示区域的高度。
                val extent: Int = mRecyclerView.computeHorizontalScrollExtent()
                //整体的高度，注意是整体，包括在显示区域之外的。
                //整体的高度，注意是整体，包括在显示区域之外的。
                val range: Int = mRecyclerView.computeHorizontalScrollRange()
                //已经向下滚动的距离，为0时表示已处于顶部。
                //已经向下滚动的距离，为0时表示已处于顶部。
                val offset: Int = mRecyclerView.computeHorizontalScrollOffset()
                /*//此处获取seekbar的getThumb，就是可以滑动的小的滚动游标
            GradientDrawable gradientDrawable =(GradientDrawable) seekBar.getThumb();
            //根据列表的个数，动态设置游标的大小，设置游标的时候，progress进度的颜色设置为和seekbar的颜色设置的一样的，所以就不显示进度了。
            gradientDrawable.setSize(extent/(strings.size()/2),5);*/
                /*//此处获取seekbar的getThumb，就是可以滑动的小的滚动游标
            GradientDrawable gradientDrawable =(GradientDrawable) seekBar.getThumb();
            //根据列表的个数，动态设置游标的大小，设置游标的时候，progress进度的颜色设置为和seekbar的颜色设置的一样的，所以就不显示进度了。
            gradientDrawable.setSize(extent/(strings.size()/2),5);*/
                mSeekBar.max = range - extent
                if (dx == 0) {
                    mSeekBar.progress = 0
                } else {
                    mSeekBar.progress = offset
                }
            }
        })
    }
}