package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.interfaces.OnFilterLinsenr

class FilterSelectDialog(context: Context, empIsOpenFilter: Boolean) : Dialog(context, R.style.DaoxilaDialog_Alert) {
    private var mOnFilterLinsenr: OnFilterLinsenr? = null
    private var isOpenFilter = false
    var rl_not_filter: RelativeLayout? = null
    var tv_not: TextView? = null
    var rl_filter: RelativeLayout? = null
    var tv_filter: TextView? = null

    init {
        isOpenFilter = empIsOpenFilter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_filter_sel_layou)
        val display = (SCApp.getInstance().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = (display.getHeight() * 0.2).toInt()
        window?.setAttributes(layoutParams)
        //靠底部显示
        window?.setGravity(Gravity.BOTTOM)
        window?.setDimAmount(0f)
        initView()
    }

    fun setOnFilterLinsenr(mOnFilterLinsenr: OnFilterLinsenr) {
        this.mOnFilterLinsenr = mOnFilterLinsenr
    }

    fun selectFilter(index: Int) {
        when (index) {
            0 -> {
                tv_not?.setTextColor(context.resources.getColor(R.color.filter_slect_text_color))
                tv_filter?.setTextColor(context.resources.getColor(R.color.filter_text_color))
                rl_not_filter?.setBackgroundResource(R.drawable.filter_select_bg)
                rl_filter?.setBackgroundResource(0)
            }
            1 -> {
                tv_not?.setTextColor(context.resources.getColor(R.color.filter_text_color))
                tv_filter?.setTextColor(context.resources.getColor(R.color.filter_slect_text_color))
                rl_not_filter?.setBackgroundResource(0)
                rl_filter?.setBackgroundResource(R.drawable.filter_select_bg)
            }
        }
    }

    fun initView() {
        rl_not_filter = findViewById(R.id.rl_not_filter)
        tv_not = findViewById(R.id.tv_not)
        rl_filter = findViewById(R.id.rl_filter)
        tv_filter = findViewById(R.id.tv_filter)
        if (isOpenFilter) {
            selectFilter(1)
        } else {
            selectFilter(0)
        }


        rl_not_filter?.setOnClickListener {
            selectFilter(0)
            mOnFilterLinsenr?.onSelectFilterlinsenr(0)
            dismiss()

        }
        rl_filter?.setOnClickListener {
            selectFilter(1)
            mOnFilterLinsenr?.onSelectFilterlinsenr(1)
            dismiss()
        }

    }

}