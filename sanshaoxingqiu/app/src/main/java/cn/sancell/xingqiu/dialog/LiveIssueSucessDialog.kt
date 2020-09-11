package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import cn.sancell.xingqiu.R
import com.netease.nim.uikit.common.util.sys.ScreenUtil

class LiveIssueSucessDialog(context: Context) : Dialog(context, R.style.DaoxilaDialog_Alert) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_issu_live_layout)
        //        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = (ScreenUtil.getDisplayWidth() * 0.8).toInt()
        layoutParams?.height = (ScreenUtil.getDisplayWidth() * 0.7).toInt()
        window?.setAttributes(layoutParams)
        initView()
    }

    fun initView() {

    }
}