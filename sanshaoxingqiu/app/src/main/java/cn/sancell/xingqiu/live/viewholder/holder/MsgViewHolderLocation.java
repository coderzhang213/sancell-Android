package cn.sancell.xingqiu.live.viewholder.holder;

import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.imageview.MsgThumbImageView;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;

import cn.sancell.xingqiu.live.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderLocation extends MsgViewHolderBase {

    public MsgThumbImageView mapView;
    public TextView addressText;

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_location;
    }

    @Override
    protected void inflateContentView() {
        mapView = (MsgThumbImageView) view.findViewById(com.netease.nim.uikit.R.id.message_item_location_image);
        addressText = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_location_address);
    }

    @Override
    protected void bindContentView() {
        final LocationAttachment location = (LocationAttachment) message.getAttachment();
        addressText.setText(location.getAddress());

        int[] bound = ImageUtil.getBoundWithLength(getLocationDefEdge(), com.netease.nim.uikit.R.drawable.nim_location_bk, true);
        int width = bound[0];
        int height = bound[1];

        setLayoutParams(width, height, mapView);
        setLayoutParams(width, (int) (0.38 * height), addressText);

        mapView.loadAsResource(com.netease.nim.uikit.R.drawable.nim_location_bk, com.netease.nim.uikit.R.drawable.nim_message_item_round_bg);
    }

    @Override
    protected void onItemClick() {
//        if (NimUIKit.getLocationProvider() != null) {
//            LocationAttachment location = (LocationAttachment) message.getAttachment();
//            NimUIKit.getLocationProvider().openMap(context, location.getLongitude(), location.getLatitude(), location.getAddress());
//        }
    }

    public static int getLocationDefEdge() {
        return (int) (0.5 * ScreenUtil.screenWidth);
    }
}
