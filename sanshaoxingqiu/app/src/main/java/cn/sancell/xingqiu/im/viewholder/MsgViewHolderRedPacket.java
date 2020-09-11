package cn.sancell.xingqiu.im.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.chatroom.adapter.ChatRoomMsgAdapter;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.list.MsgAdapter;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.http.NimHttpClient;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;

import java.util.HashMap;
import java.util.Map;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.im.extension.RedPacketAttachment;
import cn.sancell.xingqiu.im.redpacket.NIMOpenRpCallback;
import cn.sancell.xingqiu.im.ui.red.RpDetail.RpDetailActivity;
import cn.sancell.xingqiu.im.ui.red.call.NIMRedPacketClient;
import cn.sancell.xingqiu.interfaces.OpenRpListener;

public class MsgViewHolderRedPacket extends MsgViewHolderBase {

    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;    // 红包描述
    private TextView sendTitleText, revTitleText;    // 红包名称

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.red_packet_item;
    }

    @Override
    protected void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_name_send);
        sendView = findViewById(R.id.bri_send);
        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_name_rev);
        revView = findViewById(R.id.bri_rev);
    }

    @Override
    protected void bindContentView() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
        Map<String, Object> map = message.getLocalExtension();

        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendContentText.setText(attachment.getRpContent());
            sendTitleText.setText(attachment.getRpTitle());
            if (map != null) {
                if (map.get(UiHelper.SC_RP_CHECK) == (Integer) 1) {
                    sendView.setAlpha(0.5f);
                }
            }
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revContentText.setText(attachment.getRpContent());
            revTitleText.setText(attachment.getRpTitle());
            if (map != null) {
                if (map.get(UiHelper.SC_RP_CHECK) == (Integer) 1) {
                    revView.setAlpha(0.5f);
                }
            }
        }
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected void onItemClick() {
        // 拆红包
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();

        BaseMultiItemFetchLoadAdapter adapter = getAdapter();
        ModuleProxy proxy = null;
        if (adapter instanceof MsgAdapter) {
            proxy = ((MsgAdapter) adapter).getContainer().proxy;
        } else if (adapter instanceof ChatRoomMsgAdapter) {
            proxy = ((ChatRoomMsgAdapter) adapter).getContainer().proxy;
        }

         Map<String, Object> maps = message.getLocalExtension();
        if (maps != null && maps.get(UiHelper.SC_RP_CHECK) == (Integer) 1) {
            RpDetailActivity.start(context, attachment.getRpId());
            return;
        }
        NIMOpenRpCallback cb = new NIMOpenRpCallback(message.getFromAccount(), message.getSessionId(), message.getSessionType(), proxy);
        //先检查红包状态 - 打开红包
        NIMRedPacketClient.startOpenRpDialog((Activity) context, message.getSessionType(), attachment.getRpId(), cb, () -> {

            Map<String, Object> newMap = new HashMap<>();
            newMap.put(UiHelper.SC_RP_CHECK, 1);//抢了红包¬
            message.setLocalExtension(newMap);
            refreshCurrentItem();
            NIMClient.getService(MsgService.class).updateIMMessage(message);
        });


    }
}
