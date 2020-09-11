package cn.sancell.xingqiu.live.viewholder.holder;

import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;

import cn.sancell.xingqiu.live.viewholder.MsgViewHolderBase;

public class MsgViewHolderNotification extends MsgViewHolderBase {

    protected TextView notificationTextView;

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_notification_label);
    }

    @Override
    protected void bindContentView() {
        handleTextNotification(getDisplayText());
    }

    protected String getDisplayText() {
        return TeamNotificationHelper.getTeamNotificationText(message, message.getSessionId());
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}

