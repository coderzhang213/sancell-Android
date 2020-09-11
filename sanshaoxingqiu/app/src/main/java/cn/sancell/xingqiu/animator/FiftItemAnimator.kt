package cn.sancell.xingqiu.animator

import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.BaseItemAnimator

/**
 * Created by zj on 2020/4/3.
 */
class FiftItemAnimator : BaseItemAnimator() {


    override fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        ViewCompat.animate(holder.itemView)
                .translationX(-holder.itemView.getRootView().getWidth().toFloat())
                .setDuration(removeDuration)
                .setInterpolator(mInterpolator)
                .setListener(DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start()
    }

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
        ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth().toFloat())
    }

    override fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        ViewCompat.animate(holder.itemView)
                .translationX(0f)
                .setDuration(addDuration)
                .setInterpolator(mInterpolator)
                .setListener(DefaultAddVpaListener(holder))
                .setStartDelay(getAddDelay(holder))
                .start()
    }

    override fun endAnimations() {
        super.endAnimations()

    }
}