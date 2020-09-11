package cn.sancell.xingqiu.live.widget.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zj on 2020/4/1.
 */
public class LiveLikeBin extends CustomAttachment {
    public int likeNumber;

    public LiveLikeBin() {
        super(CustomAttachmentType.like);
    }


    @Override
    protected void parseData(JSONObject data) {
    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
