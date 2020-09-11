package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.util.ScreenUtils

/**
 * 举报
 */
class InfoDialogFgm : BaseDialogFragment() {

    private var tv_report: AppCompatTextView? = null
    private var tvCancel: AppCompatTextView? = null
    private var tv_black: AppCompatTextView? = null
    private var mListener: OnBlackListener? = null
    private var mBlackTye = 1

    companion object {

        fun newInstance(type: Int) =  InfoDialogFgm().apply {
            arguments = Bundle(1).apply {
                putInt("type",type)
            }
        }
    }

    override fun getData() {
        super.getData()
        mBlackTye = arguments!!.getInt("type",1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_info_user, container, false)
        initView(view)
        return view
    }

    override fun initView(view: View) {
        super.initView(view)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tv_report = view.findViewById(R.id.tv_report)
        tv_black = view.findViewById(R.id.tv_black)

        tv_report!!.setOnClickListener {
            dismiss()
            if (mListener != null) {
                mListener!!.onReport()
            }
        }
        tv_black!!.setOnClickListener {
            dismiss()
            if (mListener != null) {
                mListener!!.onBlack(mBlackTye)
            }
        }
        tvCancel?.setOnClickListener { dismiss() }
        if (mBlackTye == 1){
            tv_black!!.text = "拉黑"
        }else{
            tv_black!!.text = "解除拉黑"
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.common_bottomDialog)
        dialog.setContentView(R.layout.dialog_info_user)
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

    interface OnBlackListener {
        fun onBlack(type: Int)
        fun onReport()
    }

    fun setListener(listener: OnBlackListener) {
        mListener = listener
    }

}