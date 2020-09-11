package cn.sancell.xingqiu.live.help

import android.animation.Animator
import android.content.Context
import android.util.Log
import android.view.View
import cn.sancell.xingqiu.util.RxTimerUtil
import com.airbnb.lottie.LottieAnimationView

/**
 * Created by zj on 2020/4/3.
 */
class ClickLikeHelp(mContext: Context, iv_gift_icon: LottieAnimationView) {
    var iv_gift_icon: LottieAnimationView? = null
    var mContext: Context? = null
    private var playAimJsoName = "data.json"

    //是否可以播放点赞动画
    var isGlivLink: Boolean = true

    init {
        this.iv_gift_icon = iv_gift_icon
        this.mContext = mContext
        initLinsener()
    }

    /**
     * 设置需要播放的json动画名称
     */
    fun setPlayAinJsonName(playAimJsoName: String) {
        this.playAimJsoName = playAimJsoName
        iv_gift_icon?.setAnimation(playAimJsoName)
    }

    fun initLinsener() {
        iv_gift_icon?.setAnimation(playAimJsoName)
        iv_gift_icon?.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                isGlivLink = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isGlivLink = true
                iv_gift_icon?.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }


        })
    }

    /**
     * 播放点赞动画
     */
    fun clickLike() {
        if (!isGlivLink) {
            return
        }
        isGlivLink = false
        iv_gift_icon?.visibility = View.VISIBLE

        iv_gift_icon?.playAnimation()

    }

    fun onDestroy() {
    }

}