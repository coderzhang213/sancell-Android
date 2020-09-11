package cn.sancell.xingqiu.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R

/**
 * 主播认证异常
 */
class LiveIdentifyErrorView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var tvFailed: AppCompatTextView

    private var tvFailedDesc: AppCompatTextView

    private var btnTry: AppCompatButton

    private var ivFailed: AppCompatImageView

    private var mListener: OnRetryClick? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_identify_error_view, rootView as ViewGroup, false)

        tvFailed = view.findViewById(R.id.tv_failed)
        tvFailedDesc = view.findViewById(R.id.tv_failed_desc)
        btnTry = view.findViewById(R.id.btn_try)
        ivFailed = view.findViewById(R.id.iv_failed)
        addView(view)
    }

    fun setFailedStr(statesStr: String, descStr: String) {

        if (statesStr.isEmpty()) {
            tvFailed.visibility = View.GONE
        } else {
            tvFailed.visibility = View.VISIBLE
            tvFailed.text = statesStr
        }
        tvFailedDesc.text = descStr

    }

    fun showBtn(show: Boolean) {

        if (show) {
            btnTry.visibility = View.VISIBLE
            btnTry.setOnClickListener {
                if (mListener != null) {
                    mListener!!.retry()
                }
            }
        } else {
            btnTry.visibility = View.GONE
        }
    }

    fun setErrorImg(viewId: Int) {
        ivFailed.setImageResource(viewId)
    }

    fun setBtnListener(onRetryClick: OnRetryClick) {
        mListener = onRetryClick
    }

    interface OnRetryClick {
        fun retry()
    }

}