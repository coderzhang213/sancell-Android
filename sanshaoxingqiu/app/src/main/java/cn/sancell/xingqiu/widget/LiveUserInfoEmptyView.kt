package cn.sancell.xingqiu.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R

/**
 * 通用空页面
 */
class LiveUserInfoEmptyView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var emptyImg: AppCompatImageView
    private var ivStart: AppCompatImageView
    private var anim: Animation? = null

    init {

        val view = LayoutInflater.from(getContext()).inflate(R.layout.view_user_info_empty, rootView as ViewGroup, false)
        emptyImg = view.findViewById(R.id.iv_empty)
        ivStart = view.findViewById(R.id.iv_start)
        addView(view)
    }

    fun showImg(show: Boolean){
       if (show){
           emptyImg.visibility = View.VISIBLE
       }else{
           emptyImg.visibility = View.GONE
       }
    }

    fun getLiveImgHeight():Int{
        return ivStart.height
    }

    fun showLive(show: Boolean){
        if (show){
            ivStart.visibility = View.VISIBLE
        }else{
            ivStart.visibility = View.GONE
        }

    }

    fun startMove() {
        anim = AnimationUtils.loadAnimation(context, R.anim.start_live_translate)
        anim?.duration = 600
        anim?.repeatMode = Animation.REVERSE
        anim?.repeatCount = -1
        ivStart.startAnimation(anim)
    }

    fun cancelAnim() {
        if (anim != null && anim!!.isInitialized) {
            anim!!.cancel()
        }
    }

    fun setmY(y:Int){
        val margin =MarginLayoutParams(ivStart.getLayoutParams())

        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height)

       val layoutParams =RelativeLayout.LayoutParams(margin)

        ivStart.setLayoutParams(layoutParams)
    }


}