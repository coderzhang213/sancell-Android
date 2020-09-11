package cn.sancell.xingqiu.live.viewholder;

import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import cn.sancell.xingqiu.R;

/**
 * Created by hzxuwen on 2016/1/18.
 */
public class ChatRoomViewHolderText extends MsgViewHolderText {

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    public void setNameTextView() {
        //nameContainer.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        ChatRoomViewHolderHelper.setNameTextView((ChatRoomMessage) message, nameTextView, nameIconView, context);
    }

    @Override
    protected void bindContentView() {
        Log.i("keey", "bindContentView2");
        TextView bodyTextView = findViewById(com.netease.nim.uikit.R.id.nim_message_item_text_body);
        bodyTextView.setTextColor(bodyTextView.getContext().getResources().getColor(R.color.text_color_white));

        layoutDirection();
        bodyTextView.setText(getDisplayText());
        // MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
//        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        bodyTextView.setOnLongClickListener(longClickListener);
//        bodyTextView.setOnClickListener(onClickListener);
    }

    private void layoutDirection() {
        TextView bodyTextView = findViewById(com.netease.nim.uikit.R.id.nim_message_item_text_body);
        bodyTextView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
    }
}
