package cn.sancell.xingqiu.util;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homecommunity.live.actviity.LiveAttenListActivity;
import cn.sancell.xingqiu.homecommunity.live.actviity.LivePlayBaseHoemActivity;
import cn.sancell.xingqiu.homecommunity.video.VideoPlayListActivity;
import cn.sancell.xingqiu.homepage.SeckillListActivity;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homepage.bean.HomeBannerDataBean;
import cn.sancell.xingqiu.im.activity.ChatGroupActivity;
import cn.sancell.xingqiu.ktenum.LivePlayType;
import cn.sancell.xingqiu.mall.activity.MallHomeActivity;
import cn.sancell.xingqiu.usermember.MemberVipGiftBuyActivity;
import cn.sancell.xingqiu.usermember.MemberVipGiftListActivity;

/**
 * @author Alan_Xiong
 * @desc: banner 跳转控制
 * @time 2019-11-25 14:27
 */
public class BannerJumpUtils {

    /**
     * banner 跳转
     *
     * @param context
     * @param data
     * @param position
     */
    public static void bannerJump(Context context, List<HomeBannerDataBean.BannerBean> data, int position) {
        if (data == null || data.size() <= 0) {
            return;
        }
        HomeBannerDataBean.BannerBean bannerBean = data.get(position);
        switch (bannerBean.getDataType()) {
            case 1:
                switch (bannerBean.getObjType()) {
                    case 1:  //商品
//                        Intent intent = new Intent(context, ProductInfoActivity.class);
//                        intent.putExtra(Constants.Key.KEY_1, bannerBean.getObjId() + "");
//                        context.startActivity(intent);
                        GoodsDetailActivity.start(context, bannerBean.getObjId());
                        break;
                    case 2:  //秒杀列表界面
                        context.startActivity(new Intent(context, SeckillListActivity.class));
                        break;
                    case 3:  //礼包活动区
                        context.startActivity(new Intent(context, MemberVipGiftBuyActivity.class));
                        break;

                }
                break;
            case 2:  //外链
                if (!StringUtils.isTextEmpty(bannerBean.getViewLink())) {
                    Intent intent = new Intent(context, UrlInfoActivity.class);
                    intent.putExtra(Constants.Key.KEY_1, bannerBean.getViewLink());
                    intent.putExtra(Constants.Key.KEY_2, bannerBean.getTitle());
                    intent.putExtra(Constants.Key.KEY_3, bannerBean.getIsNeedLoginData());
                    context.startActivity(intent);
                }
                break;
            case 3:
                break;
            case 4:
                switch (bannerBean.getObjType()) {
                    case 2:  //秒杀列表区
                        context.startActivity(new Intent(context, SeckillListActivity.class));
                        break;
                    case 3:  //礼包活动区
                        context.startActivity(new Intent(context, MemberVipGiftBuyActivity.class));
                        break;
                    case 4:  //银猩限时礼包列表
                        Intent intent1 = new Intent(context, MemberVipGiftListActivity.class);
                        intent1.putExtra(Constants.Key.KEY_1, 2 + "");
                        intent1.putExtra(Constants.Key.KEY_2, true);
                        context.startActivity(intent1);
                        break;
                }
                break;
            case 7://9宫格
                switch (bannerBean.getObjId()) {//广告位置 1主页banner 2.社群广场banner 3.群组推荐banner 4.直播首页banner 5.视频首页banner 6.整形首页banner 7.美容首页banner 8.体检首页banner 9.美食首页banner 10旅游首页banner 11.主页banner
                    case 2://社群
                    case 3:
                        context.startActivity(new Intent(context, ChatGroupActivity.class));
                        break;

                    case 4://直播
                        context.startActivity(new Intent(context, LiveAttenListActivity.class));
                        break;
                    case 5://视频
                        context.startActivity(new Intent(context, VideoPlayListActivity.class));
                        break;
                    case 1://商城
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        MallHomeActivity.Companion.startIntent(context, bannerBean.getObjId() + "");
                        break;
                }
                break;
            case 8://直播 回放
                LivePlayBaseHoemActivity.Companion.startIntent(context, LivePlayType.HOME_BAND.getType(), bannerBean.getObjId() + "");
                break;
            default:
                break;
        }
    }
}
