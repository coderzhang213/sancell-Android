package cn.sancell.xingqiu.live.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatTextView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.util.FansUtils
import kotlinx.android.synthetic.main.dialog_user_like.*

class LikeDialogFgm : BaseDialogFragment() {

    private var tvName: AppCompatTextView? = null
    private var tvCount: AppCompatTextView? = null
    private var mLikeCount = 0
    private var mName: String? = ""

    companion object {
        fun newInstance(name: String, count: Int) = LikeDialogFgm().apply {
            arguments = Bundle(2).apply {
                putString(UiHelper.LIVE_USER_NAME, name)
                putInt(UiHelper.LIVE_USER_LIKE_COUNT, count)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_user_like, container, false)
        initView(view)
        return view
    }

    override fun getData() {
        super.getData()
        arguments.let {
            mName = it!!.getString(UiHelper.LIVE_USER_NAME)
            mLikeCount = it.getInt(UiHelper.LIVE_USER_LIKE_COUNT, 0)
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        tvName = view.findViewById(R.id.tv_name)
        tvCount = view.findViewById(R.id.tv_like_count)
        mName = "" + mName + ""
        tvName?.text = String.format(getString(R.string.like_user_name), mName)
        tvCount?.text = FansUtils.getLikeCount(count = mLikeCount)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_centerDialog)
        dialog.setContentView(R.layout.dialog_user_like)
        val window = dialog.window
        window.let {
            window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.round_white_8))
            val lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.dimAmount = 0.55f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }
}