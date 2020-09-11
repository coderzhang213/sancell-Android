package cn.sancell.xingqiu.im.action;

import android.app.Activity;
import android.content.Intent;

import com.jrmf360.normallib.rp.JrmfRpClient;
import com.jrmf360.normallib.rp.bean.EnvelopeBean;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.im.extension.RedPacketAttachment;
import cn.sancell.xingqiu.im.ui.red.SendReadPackageActivity;
import cn.sancell.xingqiu.usermember.bean.UserMemberRes;
import cn.sancell.xingqiu.util.AppUtils;

public class RedPacketAction extends BaseAction {

    private String tId; //无法拿到，使用sessionHelper的tid;

    public RedPacketAction(String tid) {
        super(R.drawable.message_plus_rp_selector, R.string.red_packet);
        this.tId = tid;
    }

    private static final int CREATE_GROUP_RED_PACKET = 51;
    private static final int CREATE_SINGLE_RED_PACKET = 10;

    @Override
    public void onClick() {

        if (AppUtils.getUserMemberCanUserRp() == 0) {
            SCApp.getInstance().showSystemCenterToast("成为三少医美粉丝即可使用红包");
            return;
        }

        int requestCode;
        if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_RED_PACKET);
        } else if (getContainer().sessionType == SessionTypeEnum.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
        } else {
            return;
        }
        // 发红包
        SendReadPackageActivity.start(getActivity(), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //发送红包消息
        sendRpMessage(data);
    }

    private void sendRpMessage(Intent data) {
        if (data != null) {
            RedPacketAttachment attachment = new RedPacketAttachment();
            // 红包id，红包信息，红包名称
            attachment.setRpId(data.getStringExtra(IntentKey.rp_id));
            attachment.setRpContent(data.getStringExtra(IntentKey.rp_content));
            attachment.setRpTitle(data.getStringExtra(IntentKey.rp_title));

            String content = getActivity().getString(R.string.rp_push_content);
            // 不存云消息历史记录
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableHistory = false;

            IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

            sendMessage(message);
        }
    }
}
