package cn.sancell.xingqiu.im.viewholder;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.im.extension.GoodsAttachment;

/**
  * @author Alan_Xiong
  *
  * @desc: 商品消息
  * @time 2019-11-19 15:28
  */
public class MsgGoodsViewHolder extends MsgViewHolderBase {

    private ImageView iv_goods;
    private TextView tv_title;
    private TextView tv_desc;
    private RelativeLayout rl_container;

    public MsgGoodsViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.im_msg_goods;
    }


    @Override
    protected void inflateContentView() {
        iv_goods = findViewById(R.id.iv_goods);
        tv_title = findViewById(R.id.tv_title);
    }

    @Override
    protected void bindContentView() {
        GoodsAttachment attachment = (GoodsAttachment) message.getAttachment();

        if (attachment != null){
            if (!isReceivedMessage()) {// 消息方向，自己发送的

                RequestOptions options = new RequestOptions().transform(new RoundedCorners(ScreenUtil.dip2px(10)));
                Glide.with(context).load(attachment.imageUrl).apply(options).into(iv_goods);
                tv_title.setText(attachment.title);
                tv_title.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }else{
                RequestOptions options = new RequestOptions().transform(new RoundedCorners(ScreenUtil.dip2px(10)));
                Glide.with(context).load(attachment.imageUrl).apply(options).into(iv_goods);
                tv_title.setText(attachment.title);
                tv_title.setTextColor(context.getResources().getColor(R.color.color_function_link));
            }
        }

    }


    @Override
    protected void onItemClick() {
        GoodsAttachment goodsAttachment = (GoodsAttachment) message.getAttachment();
      //  context.startActivity(new Intent(context,ProductInfoActivity.class).putExtra(Constants.Key.KEY_1,goodsAttachment.goodsId));
        GoodsDetailActivity.start(context,Integer.parseInt(goodsAttachment.goodsId));
    }

}
