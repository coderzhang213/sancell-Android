package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.interfaces.OnUpComfirLinsener
import com.netease.nim.uikit.common.ToastHelper
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class LiveAddGroupDialog(contxt: Context) : Dialog(contxt, R.style.DaoxilaDialog_Alert), View.OnClickListener {
    var tv_title_alter: TextView? = null
    var et_content: EditText? = null
    var mOnUpRemmodityNameLinsener: OnUpComfirLinsener? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancer -> {
                dismiss()
            }
            R.id.tv_comfirm -> {
                val text = et_content?.text.toString()
                if (TextUtils.isEmpty(text)) {
                    ToastHelper.showToast("请输入群号")

                    return
                }
                mOnUpRemmodityNameLinsener?.onComfirmLinser(text)
            }
        }
    }

    fun setOnUpRemmodityNameLinsener(mOnUpRemmodityNameLinsener: OnUpComfirLinsener) {
        this.mOnUpRemmodityNameLinsener = mOnUpRemmodityNameLinsener

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_live_add_layout)
        //        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = (ScreenUtil.getDisplayWidth() * 0.9).toInt()
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setAttributes(layoutParams)
        initView()
    }

    /**
     * 设置确定内容
     */
    fun setContentMsg(msg: String) {
        tv_title_alter?.setText(msg)
    }

    private fun initView() {
        tv_title_alter = findViewById(R.id.tv_title_alter)
        et_content = findViewById(R.id.et_content)
        findViewById<TextView>(R.id.tv_cancer).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_comfirm).setOnClickListener(this)
    }
}