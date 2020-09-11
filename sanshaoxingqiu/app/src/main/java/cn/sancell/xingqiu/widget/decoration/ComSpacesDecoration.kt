package cn.sancell.xingqiu.widget.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class ComSpacesDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right= ScreenUtil.dip2px(5f)
        } else {
            outRect.left= ScreenUtil.dip2px(5f)
        }
    }
}