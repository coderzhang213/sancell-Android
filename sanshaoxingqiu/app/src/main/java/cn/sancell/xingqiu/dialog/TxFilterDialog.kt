package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import cn.sancell.xingqiu.R
import com.tencent.liteav.demo.beauty.BeautyPanel
import com.tencent.liteav.demo.lvb.camerapush.PusherBeautyKit
import com.tencent.rtmp.TXLivePusher

/**
 * Created by zj on 2020/4/13.
 */
class TxFilterDialog(context: Context) : Dialog(context, R.style.DaoxilaDialog_Alert) {
    private var mLivePusher: TXLivePusher? = null
    private var pusher_beauty_pannel: BeautyPanel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mView = View.inflate(context, R.layout.view_tx_fiter_layout, null)
        // setContentView(R.layout.view_tx_fiter_layout)
        setContentView(mView)
        //宽度全屏显示
        val layoutParams = window?.getAttributes()
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.setAttributes(layoutParams)
        //靠底部显示
        window?.setGravity(Gravity.BOTTOM)
        window?.setDimAmount(0f)
        pusher_beauty_pannel = mView.findViewById(R.id.pusher_beauty)
        pusher_beauty_pannel?.apply {
            val manager = PusherBeautyKit(mLivePusher)
            setProxy(manager)
        }
    }

    fun setTXLivePusher(mLivePusher: TXLivePusher) {
        this.mLivePusher = mLivePusher
    }

}