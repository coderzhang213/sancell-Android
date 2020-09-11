package cn.sancell.xingqiu.live.viewholder.holder;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import cn.sancell.xingqiu.live.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown extends MsgViewHolderBase {
    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return false;
        }
        return true;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
    }
}
