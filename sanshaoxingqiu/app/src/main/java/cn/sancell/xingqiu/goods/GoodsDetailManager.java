package cn.sancell.xingqiu.goods;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.ProducAddressInfo;
import cn.sancell.xingqiu.bean.PurchaseInfo;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;

/**
  * @author Alan_Xiong
  *
  * @desc: 商品详情管理类
  * @time 2019-12-27 13:28
  */
public class GoodsDetailManager {

    public static final class Holder {
        private static final GoodsDetailManager instance = new GoodsDetailManager();
    }

    public static GoodsDetailManager getInstance() {
        return Holder.instance;
    }

    public List<PurchaseInfo> getServerList(ProductInfoDataBean bean) {
        List<PurchaseInfo> mList = new ArrayList<>();
        int minBuyNum = bean.getMinBuyNum();
        if (minBuyNum > 1) {
            mList.add(PurchaseInfo.getPurchaseInfo(minBuyNum + "件起售", "购买件数最少" + minBuyNum + "件"));
        }
        ProducAddressInfo goodsRegion = bean.getGoodsRegion();
        if (goodsRegion != null && !TextUtils.isEmpty(goodsRegion.getRegionAlias())) {
            mList.add(PurchaseInfo.getPurchaseInfo(goodsRegion.getRegionAlias(), goodsRegion.getProvinceName()));
        }
        if (bean.getRefundDays() >= 7) {  //能退货
            mList.add(PurchaseInfo.getPurchaseInfo("支持7天退换货", "满足相应条件，消费者可申请退换货"));
        } else {
            mList.add(PurchaseInfo.getPurchaseInfo("不支持无理由退换货", "此商品不支持7天无理由退换货"));
        }
        return mList;
    }

    public String getTitle(@NonNull List<PurchaseInfo> datas) {
        StringBuilder builder = new StringBuilder();
        if (datas.size() > 0){
            for (PurchaseInfo item:datas){
                if (TextUtils.isEmpty(builder)){
                    builder.append(item.getTitle());
                }else{
                    builder.append(" | ").append(item.getTitle());
                }
            }
        }
        return builder.toString();
    }
}
