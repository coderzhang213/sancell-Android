package cn.sancell.xingqiu.util;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import cn.sancell.xingqiu.R;

public class PriceUtils {

    private static final class Holder {
        private static final PriceUtils instance = new PriceUtils();
    }

    public static PriceUtils getInstance() {
        return Holder.instance;
    }


    /**
     * 获取价格文本
     *
     * @param context
     * @param numPrice price * 100
     * @param textSize 文本大小sp
     * @return
     */
    public SpannableStringBuilder getMainPrice(Context context, int numPrice, int textSize) {

        String realPrice = BigDecimalUtils.div(numPrice + "", "100", 2);
        String[] str = realPrice.split("[.]");
        if (str.length > 1) {
            str[1] = "." + str[1];
            return FontUtils.getInstance().changeTextSize(context, textSize, String.format(context.getResources().getString(R.string.rmb), realPrice)
                    , context.getResources().getString(R.string.common_rmb_symbol), str[1]);
        } else if (TextUtils.equals(str[0], "0")) { //0特殊处理
            return FontUtils.getInstance().changeTextSize(context, textSize, String.format(context.getResources().getString(R.string.rmb), realPrice)
                    , context.getResources().getString(R.string.common_rmb_symbol), str[0]);
        } else {
            String zero = ".00";
            realPrice += zero;
            return FontUtils.getInstance().changeTextSize(context, textSize, String.format(context.getResources().getString(R.string.rmb), realPrice), "¥", zero);
        }
    }

    public String getPrice(long numPrice) {
        return BigDecimalUtils.div(numPrice + "", "100", 2);
    }

    public String getPriceWithSyp(long numPrice) {
        return "¥" + BigDecimalUtils.div(numPrice + "", "100", 2);
    }

    public String getPriceWithSypAndSpace(long numPrice) {
        return "¥ " + BigDecimalUtils.div(numPrice + "", "100", 2);
    }
}
