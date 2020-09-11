package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.util.ScreenUtils
import kotlinx.android.synthetic.main.dialog_black_tip.*

class BlackTipDialog :BaseDialogFragment(){


    private var mListener:OnAddBlackListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_black_tip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        tv_black_cancel.setOnClickListener { dismiss() }
        tv_black_sure.setOnClickListener {
            dismiss()
            if (mListener != null){
                mListener!!.addBlack()
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(mContext, R.style.common_centerDialog)
        dialog.setContentView(R.layout.dialog_black_tip)
        val window = dialog.window
        window.let {
            val lp = window!!.attributes
            lp.gravity = Gravity.CENTER
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.width = ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext, 50f)
            lp.dimAmount = 0.55f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }

    interface OnAddBlackListener{
        fun addBlack()
    }

    fun addBlackListener(listener:OnAddBlackListener){
        mListener = listener
    }

}