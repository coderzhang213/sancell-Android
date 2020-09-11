package cn.sancell.xingqiu.homeclassify.help;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.ProducAddressInfo;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.bean.PurchaseInfo;
import cn.sancell.xingqiu.dialog.PurchaseSpecificationDialog;
import cn.sancell.xingqiu.homeclassify.bean.ProductInfoDataBean;
import cn.sancell.xingqiu.widget.FlowLayout;

/**
 * Created by zj on 2019/12/13.
 */
public class ProductAddressHelp implements View.OnClickListener {
    private Activity mActivity;
    private FlowLayout mFlowLayout;
    List<PurchaseInfo> mList = new ArrayList<>();
    private ProductInfoDataBean productInfoDataBean;

    public ProductAddressHelp(Activity mActivity, ProductInfoDataBean productInfoDataBean) {
        this.mActivity = mActivity;
        this.productInfoDataBean = productInfoDataBean;
        initData();
        initView();

    }

    /**
     * 组装数据
     */
    private void initData() {
        int minBuyNum = productInfoDataBean.getMinBuyNum();
        if (minBuyNum > 1) {
            mList.add(PurchaseInfo.getPurchaseInfo(minBuyNum + "件起售", "购买件数最少" + minBuyNum + "件"));
        }
        ProducAddressInfo goodsRegion = productInfoDataBean.getGoodsRegion();
        if (goodsRegion != null && !TextUtils.isEmpty(goodsRegion.getRegionAlias())) {
            mList.add(PurchaseInfo.getPurchaseInfo(goodsRegion.getRegionAlias(), goodsRegion.getProvinceName()));
        }
        if (productInfoDataBean.getRefundDays() >= 7) {  //能退货
            mList.add(PurchaseInfo.getPurchaseInfo("支持7天退换货", "满足相应条件，消费者可申请退换货"));
        } else {
            mList.add(PurchaseInfo.getPurchaseInfo("不支持无理由退换货", "此商品不支持7天无理由退换货"));

        }
    }

    public void setProductInfoDataBean(ProductInfoDataBean productInfoDataBean) {
        this.productInfoDataBean = productInfoDataBean;

    }

    private void initView() {
        mFlowLayout = mActivity.findViewById(R.id.rl_good_list);
        mActivity.findViewById(R.id.ll_gous_dt).setOnClickListener(this);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                View mView = View.inflate(mActivity, R.layout.view_product_x_layout, null);
                TextView tv_conent = mView.findViewById(R.id.tv_conent);
                TextView tv_gap = mView.findViewById(R.id.tv_gap);
                if (i == 0){
                    tv_gap.setVisibility(View.GONE);
                }else{
                    tv_gap.setVisibility(View.VISIBLE);
                }
                tv_conent.setText(mList.get(i).getTitle());
                mView.setLayoutParams(layoutParams);
                mFlowLayout.addView(mView);
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_gous_dt://购买提示
                PurchaseSpecificationDialog mPurchaseSpecificationDialog = new PurchaseSpecificationDialog(mActivity);
                mPurchaseSpecificationDialog.setDataLists(mList);
                mPurchaseSpecificationDialog.show();
                break;
        }
    }

}
