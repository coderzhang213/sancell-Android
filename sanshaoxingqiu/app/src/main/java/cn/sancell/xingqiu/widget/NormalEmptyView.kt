package cn.sancell.xingqiu.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.DrawableCompat
import cn.sancell.xingqiu.R
import kotlinx.android.synthetic.main.view_normal_empty.view.*

/**
 * 通用空页面
 */
class NormalEmptyView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var desc: AppCompatTextView
    private var emptyImg: AppCompatImageView

    init {

        val view = LayoutInflater.from(getContext()).inflate(R.layout.view_normal_empty, rootView as ViewGroup, false)
        desc = view.findViewById(R.id.tv_empty_desc)
        emptyImg = view.findViewById(R.id.iv_empty)
        addView(view)
    }

    fun setEmptyDesc(string: String) {
        desc.text = string
    }

    fun setEmptyImg(drawable: Drawable) {
        emptyImg.setImageDrawable(drawable)
    }

    fun setEmptyDescColor(color: Int) {
        desc.setTextColor(color)
    }

    fun setSecondFun(show: Boolean, str: String, rightDrawable: Drawable,mListener:OnFunClickListener) {
        if (show) {
            tv_second_fun.visibility = View.VISIBLE
            tv_second_fun.text = str
            if (rightDrawable != null) {
                rightDrawable.setBounds(0, 0, rightDrawable.intrinsicWidth, rightDrawable.intrinsicHeight)
                tv_second_fun.setCompoundDrawables(null, null, rightDrawable, null)
            }
            tv_second_fun.setOnClickListener {
                mListener.onSecondFun()
            }
        } else {
            tv_second_fun.visibility = View.GONE
        }

    }

     interface OnFunClickListener{
        fun onSecondFun()
    }


}