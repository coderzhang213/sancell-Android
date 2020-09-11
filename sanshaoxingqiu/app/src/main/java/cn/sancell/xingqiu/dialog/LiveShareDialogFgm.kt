package cn.sancell.xingqiu.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.SCApp
import cn.sancell.xingqiu.base.BaseDialogFragment
import cn.sancell.xingqiu.bean.LiveShareBean
import cn.sancell.xingqiu.constant.Constants
import cn.sancell.xingqiu.constant.UiHelper
import cn.sancell.xingqiu.goods.fragment.listener.UmShareListener
import cn.sancell.xingqiu.util.BitmapUtils
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.dialog_share_live.*

/**
 * 直播分享
 */
class LiveShareDialogFgm : BaseDialogFragment() {

    var mInfo: LiveShareBean? = null
    var mListener: OnLiveShareListener? = null


    fun setShareInfo(data: LiveShareBean) {
        mInfo = data
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_share_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_wechat.setOnClickListener {
            dealClick(UiHelper.SHARE_MINPROGRAM)
        }
        tv_moment.setOnClickListener {
            dealClick(UiHelper.SHARE_MOMENT)
        }
        tv_qq.setOnClickListener {
            dealClick(UiHelper.SHARE_QQ)
        }
        tv_qzone.setOnClickListener {
            dealClick(UiHelper.SHARE_QZONE)
        }
        tv_sina.setOnClickListener {
            dealClick(UiHelper.SHARE_SINA)
            dismiss()
        }
        tv_report.setOnClickListener {
            dealClick(UiHelper.SHARE_REPROT)
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
    }


    private fun dealClick(type: Int) {
        val webInfo = UMWeb(mInfo?.pageUrl)
        //非举报
        if (type != UiHelper.SHARE_REPROT && type != UiHelper.SHARE_MINPROGRAM) {
            webInfo.description = mInfo?.shareDesc
            webInfo.title = mInfo?.shareTitle
            webInfo.setThumb(mInfo?.image)
        }

        when (type) {
            UiHelper.SHARE_WECHAT -> {
                ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(UmShareListener())
                        .withMedia(webInfo).share()
            }
            UiHelper.SHARE_MOMENT -> {
                ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(UmShareListener())
                        .withMedia(webInfo).share()
            }
            UiHelper.SHARE_QQ -> {
                ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(UmShareListener())
                        .withMedia(webInfo).share()
            }
            UiHelper.SHARE_QZONE -> {
                ShareAction(activity).setPlatform(SHARE_MEDIA.QZONE).setCallback(UmShareListener())
                        .withMedia(webInfo).share()
            }
            UiHelper.SHARE_SINA -> {
                ShareAction(activity).setPlatform(SHARE_MEDIA.SINA).setCallback(UmShareListener())
                        .withMedia(webInfo).share()
            }
            UiHelper.SHARE_REPROT -> {
                if (mListener != null) {
                    mListener!!.onReport()
                }
            }
            UiHelper.SHARE_MINPROGRAM -> {
                shareMinProgram()
            }
        }
        dismiss()

    }

    private fun shareMinProgram() {
        val minProgram = WXMiniProgramObject()
        minProgram.userName = Constants.MIN_PROGRAM_NAME
        minProgram.webpageUrl = mInfo?.pageUrl
        minProgram.path = mInfo?.path
        minProgram.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
        val msg = WXMediaMessage(minProgram)
        msg.title = mInfo?.shareTitle
        msg.description = mInfo?.shareDesc
        msg.thumbData = BitmapUtils.bmpToByteArray(mInfo?.bitmap,true)

        val req = SendMessageToWX.Req()
        req.transaction = ""
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        UMShareAPI.get(mContext)
        val send = SCApp.api.sendReq(req)
        Log.i("isSend", send.toString())

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_bottomDialog)
        dialog.setContentView(R.layout.dialog_share_live)
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

    interface OnLiveShareListener {
        fun onReport()

    }

    fun setListener(listener: OnLiveShareListener) {
        mListener = listener
    }
}