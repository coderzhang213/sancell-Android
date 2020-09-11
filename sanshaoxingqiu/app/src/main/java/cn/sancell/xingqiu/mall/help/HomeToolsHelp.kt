package cn.sancell.xingqiu.mall.help

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.homepage.SearchActivity
import com.google.android.material.appbar.AppBarLayout
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import kotlinx.android.synthetic.main.frament_mall_home_layout.*
import kotlinx.android.synthetic.main.view_search_layout.*

/**
 * Created by zj on 2019/12/27.
 */
class HomeToolsHelp {
    var mFragment: Fragment? = null
    var title_topView: View? = null
    private var apl_btView: AppBarLayout? = null
    private var home_toolbarView: Toolbar? = null
    private var view_banView: View? = null
    private var iv_search_gray_bgView: ImageView? = null
    private var tv_search_default_keywordView: TextView? = null
    private var tv_iv_class: ImageView? = null
    private var tv_iv_back: ImageView? = null
    private var moudleId: String = "1"

    constructor(mFragment: Fragment) {
        this.mFragment = mFragment
        initView()
        initLinsener()
        setViewData()
    }

    fun setMoudleId(emId: String) {
        moudleId = emId
    }

    init {
    }

    fun initView() {
        mFragment?.apply {
            title_topView = title_top
            apl_btView = apl_bt
            home_toolbarView = home_toolbar
            view_banView = view_ban
            tv_iv_class = iv_class
            iv_search_gray_bgView = iv_search_gray_bg
            tv_search_default_keywordView = tv_search_default_keyword
            tv_iv_back = iv_back
        }
        home_toolbarView?.apply {
            setOnClickListener {

                SearchActivity.startIntent(mFragment?.activity, moudleId)
            }
        }

    }

    /**
     * 适配齐刘海
     */
    fun setViewData() {
        val resourceId: Int = mFragment!!.getResources().getIdentifier("status_bar_height", "dimen", "android")
        val viewHeight: Int = mFragment!!.getResources().getDimensionPixelSize(resourceId)
        title_topView?.apply {
            layoutParams.height = viewHeight
        }
        view_banView?.apply {
            layoutParams.height = viewHeight + ScreenUtil.dip2px(49f)
        }
    }

    private fun initLinsener() {
        apl_btView?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(p0: AppBarLayout?, verticalOffset: Int) {
                //verticalOffset始终为0以下的负数
                var percent: Float = Math.abs(verticalOffset * 1.0f) / 500
                if (verticalOffset > 500 || verticalOffset < -500 || percent > 1) { //不用滚动到全部
                    percent = 1f
                }
                setIcon(percent)
                home_toolbarView?.setBackgroundColor(changeAlpha(Color.WHITE, percent))
                title_topView?.setBackgroundColor(changeAlpha(Color.WHITE, percent))
            }
        })
    }

    fun setIcon(percent: Float) {
        if (percent > 0.5) {
            tv_iv_back?.setImageResource(R.mipmap.mall_back)
            tv_iv_class?.setImageResource(R.mipmap.mall_class_sel)
            tv_search_default_keywordView?.setTextColor(Color.parseColor("#111111"))
            iv_search_gray_bgView?.setImageResource(R.drawable.round_color_stroke5_20)
        } else {
            tv_search_default_keywordView?.setTextColor(Color.parseColor("#BABCBF"))
            iv_search_gray_bgView?.setImageResource(R.drawable.round_white_20)
            tv_iv_class?.setImageResource(R.mipmap.mall_class)
            tv_iv_back?.setImageResource(R.mipmap.mall_w_back)
        }

    }

    fun setSearchText(text: String) {
        tv_search_default_keywordView?.setText(text)
    }

    /**
     * 根据百分比改变颜色透明度
     */
    fun changeAlpha(color: Int, fraction: Float): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
    }
}