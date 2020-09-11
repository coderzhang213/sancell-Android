package cn.sancell.xingqiu.live.widget.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zj on 2020/4/1.
 */
public class RedEnvelopeInfo extends CustomAttachment {
    public long moneyE2;
    public String nickName;
    public String userId;
    public Long addListTime;//开始显示的时间
    public String gravatar;//用户头像，

    public RedEnvelopeInfo() {
        super(CustomAttachmentType.live_read_pack);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
