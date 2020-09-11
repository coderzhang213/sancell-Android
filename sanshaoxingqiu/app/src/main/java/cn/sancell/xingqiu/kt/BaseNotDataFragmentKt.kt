package cn.sancell.xingqiu.kt

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import cn.sancell.xingqiu.R
import handbank.hbwallet.BaseFragment
import handbank.hbwallet.BaseViewModel
import kotlinx.android.synthetic.main.base_title_layout.*

/**
 * Created by zj on 2019/12/23.
 */
abstract class BaseNotDataFragmentKt<VM : BaseViewModel> : BaseFragment<VM>(), View.OnTouchListener {
    private var notView: View? = null
    private var netWorkError: View? = null
    private var loadData //数据加载匡
            : View? = null
    private var iv_not_data_icon //暂无数据图标
            : ImageView? = null
    private var tv_not_data_text //暂无数据文字
            : TextView? = null

    //加载动画
    private var mLadAnimation: AnimationDrawable? = null
    private var loading_iv //动画加载img
            : ImageView? = null
    lateinit var toolbar_navigation //title
            : Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var viewById: LinearLayout?
        if (mRoot != null && mRoot?.parent != null) {
            val parent = mRoot?.parent as ViewGroup
            parent.removeView(mRoot)
        } else { //加一层布局
            val mR = inflater.inflate(R.layout.base_not_title_layout, container, false)
            viewById = mR.findViewById(R.id.ll_add_content)
            if (isShowTitle) {//显示title
                mR.findViewById<View>(R.id.base_title).visibility = View.VISIBLE
                initTitle(mR)
            }
            mRoot = inflater.inflate(getLayoutResId(), container, false)
            mRoot?.setOnTouchListener(this)
            if (isLoadNotDat) { //是否需要增加暂无数据
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                val load = inflater.inflate(R.layout.load_layout, container, false) as RelativeLayout
                load.addView(mRoot, lp)
                notView = inflater.inflate(R.layout.not_data_layout, container, false)
                notView?.setVisibility(View.GONE)
                load.addView(notView, lp)
                netWorkError = inflater.inflate(R.layout.network_error_data_layout, container, false)
                netWorkError?.setVisibility(View.GONE)
                load.addView(netWorkError, lp)
                netWorkError?.setOnClickListener {
                    //调用重新加载数据
                    onReloadData()
                }
                //加载匡
                loadData = inflater.inflate(R.layout.data_load_layout, container, false)
                loadData?.setVisibility(View.GONE)
                loading_iv = loadData?.findViewById(R.id.loading_iv)
                load?.addView(loadData, lp)
                viewById.addView(load)
            } else {
                viewById.addView(mRoot)
            }
            mRoot = viewById
            mInflater = inflater
            // Get savedInstanceState
            savedInstanceState?.let { onRestartInstance(it) }
        }
        return mRoot
    }

    private fun initTitle(view: View) {
        toolbar_navigation = view!!.findViewById(R.id.toolbar_navigation)
        toolbar_navigation.setNavigationOnClickListener { activity!!.onBackPressed() }
    }

    protected abstract fun onReloadData()

    /**
     * 设置没有数据
     */
    fun setNotData(msg: String?, iconId: Int) {
        if (!isLoadNotDat) {
            return
        }
        if (iv_not_data_icon == null) {
            iv_not_data_icon = notView!!.findViewById(R.id.iv_not_data_icon)
        }
        if (tv_not_data_text == null) {
            tv_not_data_text = notView!!.findViewById(R.id.tv_not_data_text)
        }
        iv_not_data_icon!!.setBackgroundResource(iconId)
        tv_not_data_text!!.text = msg
        notView!!.visibility = View.VISIBLE
    }

    /**
     * 适配齐刘海
     */
    fun setSerHeight() {
        if (isShowTitle) {
            val resourceId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
            val viewHeight = context!!.resources.getDimensionPixelSize(resourceId)
            val layoutParams: ViewGroup.LayoutParams = view_title_top.getLayoutParams()
            layoutParams.height = viewHeight
            view_title_top.setLayoutParams(layoutParams)
        }


    }

    /**
     * 如果有分页，统一关闭一些加载效果
     */
    open fun hindLoadStatus() {
        hideLoadData()
    }

    /**
     * 设置左边控件
     */
    fun setRightIcon(icon: Int, onCLickLinsenr: View.OnClickListener) {
        seller_collection_iv.visibility = View.VISIBLE
        seller_collection_iv.setImageResource(icon)
        seller_collection_iv.setOnClickListener(onCLickLinsenr)
    }

    /**
     * 判断当前是否暂无数据了
     *
     * @return
     */
    val notDataIsVisib: Boolean
        get() = if (notView!!.visibility == View.VISIBLE) {
            true
        } else false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        onMyTouch()
        if (null != activity!!.currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            val mInputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        }
        return false
    }

    /**
     * 点击了布局
     */
    fun onMyTouch() { //点击其他地方的时候，EditText失去焦点
        mRoot?.isFocusable = true
        mRoot?.isFocusableInTouchMode = true
        mRoot?.requestFocus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    /**
     * 设置没有数据
     */
    fun setNotData(msg: String?) {
        setNotData(msg, R.mipmap.common_no_data)
    }

    /**
     * @param textName
     */
    protected fun setTitleName(textName: String?) {
        tv_base_title?.text = textName
    }

    /**
     * 设置没有数据
     */
    fun setNotData(iconId: Int) {
        setNotData(activity!!.resources.getString(R.string.not_data), iconId)
    }

    /**
     * 默认图标
     */
    fun setNotData() {
        setNotData(activity!!.resources.getString(R.string.not_data), R.mipmap.common_no_data)
    }

    /**
     * 隐藏我的
     */
    fun hideNotData() {
        if (!isLoadNotDat) {
            return
        }
        if (notView!!.visibility == View.VISIBLE) {
            notView!!.visibility = View.GONE
        }
    }

    /**
     * 暂无网络
     */
    fun showNewWorkError() {
        if (isLoadNotDat && netWorkError!!.visibility == View.GONE) {
            netWorkError!!.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏掉暂无网络
     */
    fun goneNewWorkError() {
        if (isLoadNotDat && netWorkError!!.visibility == View.VISIBLE) {
            netWorkError!!.visibility = View.GONE
        }
    }

    /**
     * 是否需要添加暂无数据
     *
     * @return
     */
    abstract val isLoadNotDat: Boolean

    /**
     * 是否需要显示title
     */
    abstract val isShowTitle: Boolean

    /**
     * 加载动画
     */
    open fun showLoadData() {
        if (isLoadNotDat) {
//            if (mLadAnimation == null) {
//                mLadAnimation = resources.getDrawable(R.drawable.audio_animate) as AnimationDrawable
//                loading_iv!!.setImageDrawable(mLadAnimation)
//            }
//            mLadAnimation?.start()
            loadData!!.visibility = View.VISIBLE
        }
    }

    /**
     * 停止动画
     */
    private fun stiopAnimation() {
        mLadAnimation?.apply {
            if (isRunning) {
                stop()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stiopAnimation()
    }

    /**
     * 隐藏加载框A
     */
    fun hideLoadData() {
        goneNewWorkError()
        if (isLoadNotDat) {
            stiopAnimation()
            loadData!!.visibility = View.GONE
        }
    }


}