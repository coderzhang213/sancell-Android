package cn.sancell.xingqiu.live.widget.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zj on 2020/4/1.
 */
public class CouponBin extends CustomAttachment {
    public String hasCoupons;

    public CouponBin() {
        super(CustomAttachmentType.COUP_SUM);
    }


    @Override
    protected void parseData(JSONObject data) {
    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
