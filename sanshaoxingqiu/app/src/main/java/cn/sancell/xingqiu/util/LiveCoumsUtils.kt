package cn.sancell.xingqiu.util

import cn.sancell.xingqiu.live.utils.JsonUtils
import cn.sancell.xingqiu.live.widget.extension.*
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment

/**
 * Created by zj on 2020/4/1.
 */
object LiveCoumsUtils {
    fun jsonToType(mCoustInfoBase: CoustInfoBase): MsgAttachment? {
        var mInfo: MsgAttachment? = null
        when (mCoustInfoBase.type) {
            CousmMsgType.ONLINE_USERS.type -> {//在线人数
                mInfo = JsonUtils.getObject(mCoustInfoBase.data, LiveCountBin::class.java)
            }
            CousmMsgType.READ_ENVELOPE.type -> {//红包
                mInfo = JsonUtils.getObject(mCoustInfoBase.data, RedEnvelopeInfo::class.java)
            }
            CousmMsgType.LIKE_SUM.type -> {//点赞
                mInfo = JsonUtils.getObject(mCoustInfoBase.data, LiveLikeBin::class.java)
            }
            CousmMsgType.COUPON.type -> {//点赞
                mInfo = JsonUtils.getObject(mCoustInfoBase.data, CouponBin::class.java)
            }
        }

        return mInfo
    }
}