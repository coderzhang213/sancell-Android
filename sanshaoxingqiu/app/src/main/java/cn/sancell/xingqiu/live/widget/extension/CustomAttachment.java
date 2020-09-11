package cn.sancell.xingqiu.live.widget.extension;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import cn.sancell.xingqiu.im.extension.CustomAttachParser;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public abstract class CustomAttachment implements MsgAttachment {

    protected int type;

    CustomAttachment(int type) {
        this.type = type;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(type, packData());
    }

    public int getType() {
        return type;
    }

    protected abstract void parseData(JSONObject data);
    protected abstract JSONObject packData();
}
