package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.util.ScreenUtils
import kotlinx.android.synthetic.main.dialog_make_fans.*

class MakeFansDialogFgm : BaseDialogFragment() {

    init {
        isTransparent = true
    }

    companion object {
        fun newInstance(desc: String) = MakeFansDialogFgm().apply {
            arguments = Bundle(1).apply {
                putString("desc", desc)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_make_fans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_close.setOnClickListener {
            dismiss()
        }
        tv_fans_level.text = arguments?.getString("desc")

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_centerDialog)
        dialog.setContentView(R.layout.dialog_make_fans)
        val window = dialog.window
        if (window != null) {
            val lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.width = ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext, 50f)
            lp.dimAmount = 0.55f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        return dialog
    }
}