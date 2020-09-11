package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.dialog.listener.OnComfirmLinsenr
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class ComfirmToDialog(contxt: Context, title: String?) : Dialog(contxt, R.style.DaoxilaDialog_Alert), View.OnClickListener {
    private var str_msg: String = ""
    private var mOnComfrimLinsenr: OnComfirmLinsenr? = null

    init {
        title?.apply {
            str_msg = this
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancer -> {
                mOnComfrimLinsenr?.OnCancerLinsenr()
                dismiss()
            }
            R.id.tv_comfirm -> {
                mOnComfrimLinsenr?.OnComfirmLinsenr()
            }
        }
    }

    fun setOnClickLinsener(mOnComfrimLinsenr: OnComfirmLinsenr) {
        this.mOnComfrimLinsenr = mOnComfrimLinsenr

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_comfrim_to_layout)
        initView()
        //        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = (ScreenUtil.getDisplayWidth() * 0.9).toInt()
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setAttributes(layoutParams)

    }

    private fun initView() {
        val tv_msg = findViewById<TextView>(R.id.tv_content_msg)
        tv_msg.setText(str_msg)
        findViewById<TextView>(R.id.tv_cancer).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_comfirm).setOnClickListener(this)
    }
}