package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.live.activity.LiveMineEarnActivity
import cn.sancell.xingqiu.live.activity.UserCenterActivity

class UserEarnDialogFgm : BaseDialogFragment() {

    private var tvEarn: AppCompatTextView? = null
    private var tvCancel: AppCompatTextView? = null
    private var tvCenter: AppCompatTextView? = null

    companion object {

        fun newInstance(): UserEarnDialogFgm {
            return UserEarnDialogFgm()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_user_earn, container, false)
        initView(view)
        return view
    }

    override fun initView(view: View) {
        super.initView(view)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvEarn = view.findViewById(R.id.tv_earn)
        tvCenter = view.findViewById(R.id.tv_center)

        tvEarn?.setOnClickListener {
            LiveMineEarnActivity.start(mContext)
            dismiss()
        }
        tvCenter?.setOnClickListener {
            dismiss()
            UserCenterActivity.start(mContext)
        }
        tvCancel?.setOnClickListener { dismiss() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.common_bottomDialog)
        dialog.setContentView(R.layout.dialog_user_earn)
        val window = dialog.window
        window.let {
            val lp = window!!.attributes
            lp.gravity = Gravity.BOTTOM
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.dimAmount = 0.55f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }

}