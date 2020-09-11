package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.dialog.listener.OnDefinitionSelectLinsener
import cn.sancell.xingqiu.live.nim.PublishParam
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class LiveDefinitionSelectDialog(context: Context, mLinsener: OnDefinitionSelectLinsener) : Dialog(context, R.style.DaoxilaDialog_Alert) {
    private var mOnDefinitionSelectLinsener: OnDefinitionSelectLinsener? = null

    init {
        mOnDefinitionSelectLinsener = mLinsener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_live_definition_layout)
//        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setAttributes(layoutParams)
        //靠底部显示
        window?.setGravity(Gravity.BOTTOM)
        initView()
    }

    fun initView() {
        findViewById<TextView>(R.id.tv_hd).setOnClickListener(mOnClickLinsener)
        findViewById<TextView>(R.id.tv_sd).setOnClickListener(mOnClickLinsener)
        findViewById<TextView>(R.id.tv_ld).setOnClickListener(mOnClickLinsener)
        findViewById<TextView>(R.id.tv_cancer).setOnClickListener(mOnClickLinsener)

    }

    protected val mOnClickLinsener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.tv_hd -> {
                    mOnDefinitionSelectLinsener?.OnSelectDefinition(PublishParam.HD)
                    dismiss()
                }
                R.id.tv_sd -> {
                    mOnDefinitionSelectLinsener?.OnSelectDefinition(PublishParam.SD)
                    dismiss()
                }
                R.id.tv_ld -> {
                    mOnDefinitionSelectLinsener?.OnSelectDefinition(PublishParam.LD)
                    dismiss()
                }
                R.id.tv_cancer -> {
                    dismiss()

                }

            }
        }
    }

}