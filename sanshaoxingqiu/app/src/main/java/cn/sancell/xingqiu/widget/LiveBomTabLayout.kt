package cn.sancell.xingqiu.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.interfaces.OnLiveBomTabLinsener

class LiveBomTabLayout : RelativeLayout {
    private var mOnLiveBomTabLinsener: OnLiveBomTabLinsener? = null
    private var mTextViewList = ArrayList<TextView>()
    private var mImageViewList = ArrayList<ImageView>()
    private val ic_tab_ids = intArrayOf(R.mipmap.icon_live_red, R.mipmap.icon_video_red, R.mipmap.icon_shop_red, R.mipmap.icon_social_red, R.mipmap.icon_my_red)
    private val ic_tab_not_ids = intArrayOf(R.mipmap.icon_live_gray, R.mipmap.icon_video_gray, R.mipmap.icon_shop_gray, R.mipmap.icon_social_gray, R.mipmap.icon_my_gray)

    constructor(context: Context) : super(context) {
        setContextLayout(context)
    }

    constructor(context: Context, attry: AttributeSet) : super(context, attry) {
        setContextLayout(context)
    }

    fun setContextLayout(context: Context) {
        val mView = View.inflate(context, R.layout.live_bom_tab_layout, null)
        val layout = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(mView, layout)
        mTextViewList.add(findViewById(R.id.tv_tab_0))
        mTextViewList.add(findViewById(R.id.tv_tab_1))
        mTextViewList.add(findViewById(R.id.tv_tab_2))
        mTextViewList.add(findViewById(R.id.tv_tab_3))
        mTextViewList.add(findViewById(R.id.tv_tab_4))

        mImageViewList.add(findViewById(R.id.iv_tab_0))
        mImageViewList.add(findViewById(R.id.iv_tab_1))
        mImageViewList.add(findViewById(R.id.iv_tab_2))
        mImageViewList.add(findViewById(R.id.iv_tab_3))
        mImageViewList.add(findViewById(R.id.iv_tab_4))
        mView.findViewById<View>(R.id.ll_tab_0)?.setOnClickListener(OnClickLinsener)
        mView.findViewById<View>(R.id.ll_tab_1)?.setOnClickListener(OnClickLinsener)
        mView.findViewById<View>(R.id.ll_tab_2)?.setOnClickListener(OnClickLinsener)
        mView.findViewById<View>(R.id.ll_tab_3)?.setOnClickListener(OnClickLinsener)
        mView.findViewById<View>(R.id.ll_tab_4)?.setOnClickListener(OnClickLinsener)
    }

    fun setOnLiveBomTabLinsener(mOnLiveBomTabLinsener: OnLiveBomTabLinsener) {
        this.mOnLiveBomTabLinsener = mOnLiveBomTabLinsener
    }

    private val OnClickLinsener = object : OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.ll_tab_0 -> {//点击首页
                    showTab(0)
                    mOnLiveBomTabLinsener?.onTabClcikLinsener(0)
                }
                R.id.ll_tab_1 -> {//点击视频
                    showTab(1)
                    mOnLiveBomTabLinsener?.onTabClcikLinsener(1)
                }
                R.id.ll_tab_2 -> {//添加
                    showTab(2)
                    mOnLiveBomTabLinsener?.onTabClcikLinsener(2)

                }
                R.id.ll_tab_3 -> {//我的
                    showTab(3)
                    mOnLiveBomTabLinsener?.onTabClcikLinsener(3)

                }
                R.id.ll_tab_4 -> {//我的
                    showTab(4)
                    mOnLiveBomTabLinsener?.onTabClcikLinsener(4)

                }
            }
        }
    }

    fun showTab(postion: Int) {
        for (pos in 0..mTextViewList.size - 1) {
            if (postion == pos) {
                mTextViewList.get(pos).setTextColor(context?.resources?.getColor(R.color.filter_slect_text_color)!!)
                mImageViewList.get(pos).setImageResource(ic_tab_ids[pos])
            } else {
                mImageViewList.get(pos).setImageResource(ic_tab_not_ids[pos])
                mTextViewList.get(pos).setTextColor(context?.resources?.getColor(R.color.color_text6)!!)
            }

        }
    }

}