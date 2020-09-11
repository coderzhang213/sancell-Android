package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.bean.VoucherInfo
import cn.sancell.xingqiu.dialog.adapter.CouponOrderListAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.netease.nim.uikit.common.util.sys.ScreenUtil
import kotlinx.android.synthetic.main.dialog_chose_voucher.*

/**
 * 选取代金券
 */
class ChoseVoucherDialog : BaseDialogFragment() {

    private val showHeight = ScreenUtil.dip2px(360f)

    private var mListener: OnVoucherChoose? = null
    private var rv_voucher:RecyclerView? = null
    private var iv_close:AppCompatImageView? = null

    private var data:List<VoucherInfo> = ArrayList()
    private var choseId:String = ""

    companion object {
        fun newInstance() = ChoseVoucherDialog().apply {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_chose_voucher, container, false)
        rv_voucher = view.findViewById(R.id.rv_voucher)
        iv_close =  view.findViewById(R.id.iv_close)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_voucher?.layoutManager = LinearLayoutManager(context)
        iv_close?.setOnClickListener { dismiss() }
        initData()
    }


    fun setListener(listener: OnVoucherChoose) {
        mListener = listener;
    }

    fun initData(){
        val adapter = CouponOrderListAdapter(data)
        adapter.setChoseId(choseId)
        rv_voucher?.adapter = adapter
        adapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                mListener?.onItemChose(data[position])
                dismiss()
            }

        })
    }

    /**
     * choseId 已经选中的id
     */
    fun setData(data: List<VoucherInfo>, choseId: String) {
        this.data = data;
        this.choseId = choseId
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_bottomDialog)
        dialog.setContentView(R.layout.dialog_video_rel)
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(resources.getDrawable(R.drawable.round_white_8))
            val lp = window.attributes
            lp.gravity = Gravity.BOTTOM
            lp.height = showHeight
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.dimAmount = 0.55f
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }

    public interface OnVoucherChoose {
        fun onItemChose(item: VoucherInfo)
    }
}