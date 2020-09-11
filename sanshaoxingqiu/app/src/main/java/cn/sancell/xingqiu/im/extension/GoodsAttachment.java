package cn.sancell.xingqiu.im.extension;

import com.alibaba.fastjson.JSONObject;

import cn.sancell.xingqiu.constant.IntentKey;

/**
  * @author Alan_Xiong
  *
  * @desc: 自定义商品 附件
  * @time 2019-11-19 13:38
  */
public class GoodsAttachment extends CustomAttachment {

    public String describe;

    public String imageUrl;

    public String goodsId;

    public String title;

    public String link_url;



    public GoodsAttachment() {
        super(CustomAttachmentType.Goods);
    }

    @Override
    protected void parseData(JSONObject data) {
        //转化数据
        describe = data.getString(IntentKey.KEY_CONTENT);
        imageUrl = data.getString(IntentKey.KEY_URL);
        goodsId = data.getString(IntentKey.KEY_GOODID);
        title = data.getString(IntentKey.KEY_TITLE);
        link_url = data.getString(IntentKey.KEY_LINKURL);

    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(IntentKey.KEY_CONTENT,describe);
        data.put(IntentKey.KEY_URL,imageUrl);
        data.put(IntentKey.KEY_GOODID,goodsId);
        data.put(IntentKey.KEY_TITLE,title);
        data.put(IntentKey.KEY_LINKURL,link_url);
        return data;
    }

}
