package cn.sancell.xingqiu.im.action;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.im.extension.GoodsAttachment;
import cn.sancell.xingqiu.im.ui.search.SearchGoodsMsgActivity;

/**
 * @author Alan_Xiong
 * @desc: 自定义商品信息
 * @time 2019-11-19 14:20
 */
public class GoodsAction extends BaseAction {
    public static final int GOODS_INFO_MSG_TEAM = 61;

    public GoodsAction() {
        super(R.mipmap.icon_msg_good_type, R.string.msg_good);
    }

    @Override
    public void onClick() {
        int requestCode = 61;
        if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(GOODS_INFO_MSG_TEAM);
        }
        //跳转商品的选择
        SearchGoodsMsgActivity.start(getActivity(), requestCode, "", "1");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendGoodsMsg(data);
    }

    /**
     * 发送消息
     *
     * @param data
     */
    public void sendGoodsMsg(Intent data) {
        if (data != null) {
            GoodsAttachment attachment = new GoodsAttachment();
            attachment.describe = data.getStringExtra(IntentKey.KEY_CONTENT);
            attachment.title = data.getStringExtra(IntentKey.KEY_TITLE);
            attachment.goodsId = data.getStringExtra(IntentKey.KEY_GOODID);
            attachment.imageUrl = data.getStringExtra(IntentKey.KEY_URL);

            String content = getActivity().getString(R.string.good_push_content);

            // 不存云消息历史记录
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableHistory = false;

            IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

            sendMessage(message);
        }
    }
}
