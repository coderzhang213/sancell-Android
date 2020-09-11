package cn.sancell.xingqiu.live.widget.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zj on 2020/4/1.
 */
public class LiveCountBin extends CustomAttachment {
    public int onlineUserCount;
    public String pushFlag;

    public LiveCountBin() {
        super(CustomAttachmentType.live_count);
    }


    @Override
    protected void parseData(JSONObject data) {
    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
