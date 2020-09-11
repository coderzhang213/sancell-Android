package cn.sancell.xingqiu.kt

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
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
import handbank.hbwallet.BaseViewModel
import kotlinx.android.synthetic.main.base_title_layout.*

/**
 * Created by zj on 2019/12/23.
 */
abstract class BaseNotDataActivityKt<VM : BaseViewModel> : BaseActivity<VM>(), View.OnTouchListener {
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
    private lateinit var toolbar_navigation //title
            : Toolbar
    protected var mRoot: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        isSetView=false
        var viewById: LinearLayout?
        val mR = View.inflate(this,R.layout.base_not_title_layout,  null)
        viewById = mR.findViewById(R.id.ll_add_content)
        if (isShowTitle) {//显示title
            mR.findViewById<View>(R.id.base_title).visibility = View.VISIBLE
            initTitle(mR)
        }
        mRoot = View.inflate(this,getLayoutResId(),  null)
        mRoot?.setOnTouchListener(this)
        if (loadNotDat) { //是否需要增加暂无数据
            val load = View.inflate(this,R.layout.load_layout, null) as RelativeLayout
            load.addView(mRoot, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            notView = View.inflate(this,R.layout.not_data_layout, null)
            notView?.setVisibility(View.GONE)
            load.addView(notView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            netWorkError = View.inflate(this,R.layout.network_error_data_layout, null)
            netWorkError?.setVisibility(View.GONE)
            load.addView(netWorkError, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            netWorkError?.setOnClickListener {
                //调用重新加载数据
                onReloadData()
            }
            //加载匡
            loadData = View.inflate(this,R.layout.data_load_layout, null)
            loadData?.setVisibility(View.GONE)
            loading_iv = loadData?.findViewById(R.id.loading_iv)

            load.addView(loadData, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            viewById.addView(load, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            viewById.addView(mRoot, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        }
        mRoot = viewById
        // Get savedInstanceState
        savedInstanceState?.let { }
        setContentView(mRoot)
        super.onCreate(savedInstanceState)
    }

    private fun initTitle(view: View) {
        toolbar_navigation = view.findViewById(R.id.toolbar_navigation)
        toolbar_navigation.setNavigationOnClickListener { onBackPressed() }
    }

    protected abstract fun onReloadData()

    /**
     * 设置没有数据
     */
    fun setNotData(msg: String?, iconId: Int) {
        if (!loadNotDat) {
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
        if (null != currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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
        setNotData(resources.getString(R.string.not_data), iconId)
    }

    /**
     * 默认图标
     */
    fun setNotData() {
        setNotData(resources.getString(R.string.not_data), R.mipmap.common_no_data)
    }

    /**
     * 隐藏我的
     */
    fun hideNotData() {
        if (!loadNotDat) {
            return
        }
        if (notView?.visibility == View.VISIBLE) {
            notView?.visibility = View.GONE
        }
    }

    /**
     * 暂无网络
     */
    fun showNewWorkError() {
        if (loadNotDat && netWorkError?.visibility == View.GONE) {
            netWorkError?.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏掉暂无网络
     */
    fun goneNewWorkError() {
        if (loadNotDat && netWorkError!!.visibility == View.VISIBLE) {
            netWorkError?.visibility = View.GONE
        }
    }

    /**
     * 是否需要添加暂无数据
     *
     * @return
     */
    abstract val loadNotDat: Boolean
    /**
     * 是否需要显示title
     */
    abstract val isShowTitle: Boolean

    /**
     * 加载动画
     */
    open fun showLoadData() {
        if (loadNotDat) {
//            if (mLadAnimation == null) {
//                mLadAnimation = resources.getDrawable(R.drawable.audio_animate) as AnimationDrawable
//                loading_iv!!.setImageDrawable(mLadAnimation)
//            }
//            mLadAnimation?.start()
            loadData?.visibility = View.VISIBLE
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
        if (loadNotDat) {
            stiopAnimation()
            loadData?.visibility = View.GONE
        }
    }


}