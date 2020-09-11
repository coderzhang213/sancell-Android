package cn.sancell.xingqiu.live.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.constant.UiHelper
import com.jrmf360.normallib.base.utils.KeyboardUtil

/**
 * 修改个人简介
 */
class ModifyIntroDialogFgm : BaseDialogFragment() {

    private var ivClose: AppCompatImageView? = null
    private var etIntro: AppCompatEditText? = null
    private var btnSure: AppCompatButton? = null

    private var mListener: OnIntroSureListener? = null

    private var mHistory: String? = ""

    companion object {

        fun newInstance(historyIntro: String?) = ModifyIntroDialogFgm().apply {
            arguments = Bundle(1).apply {
                putString(UiHelper.HISTORY_INTRO, historyIntro)
            }
        }
    }

    override fun getData() {
        super.getData()
        mHistory = arguments?.getString(UiHelper.HISTORY_INTRO)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_modify_intro, container, false)
        initView(view)
        return view
    }

    override fun initView(view: View) {
        ivClose = view.findViewById(R.id.iv_close)
        etIntro = view.findViewById(R.id.et_intro)
        btnSure = view.findViewById(R.id.btn_sure)
        etIntro?.apply {
            if (!TextUtils.isEmpty(mHistory)){
                setText(mHistory)
            }
            requestFocus()
        }

        ivClose?.setOnClickListener {
            mListener?.hide()
            dismiss()
        }
        btnSure?.setOnClickListener {
            mListener?.onSure(etIntro?.text.toString())
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_bottomDialog)
        dialog.setContentView(R.layout.dialog_modify_intro)
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

    fun setIntroSureListener(listener: OnIntroSureListener) {
        mListener = listener
    }

    interface OnIntroSureListener {
        fun onSure(intro: String)

        fun hide()

    }

}