package cn.sancell.xingqiu.homeshoppingcar.adapter;

import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.widget.RelativeSizeTextView;

/**
 * Created by ai11 on 2019/6/29.
 */

public class CreateOrderPackAdapter extends BaseQuickAdapter<HomeShoppingCarDataBean.ShopingCarProductBean, BaseViewHolder> {
    boolean isOnePack = true;
    int supplierId;
    int packName = 1;
    private OnOrderListener mListener;

    private Map<String, EditText> hangYeEditTextMap = new HashMap<>();

    public CreateOrderPackAdapter(int layoutResId, @Nullable List<HomeShoppingCarDataBean.ShopingCarProductBean> data) {
        super(layoutResId, data == null ? new ArrayList<>():data);
    }

    public CreateOrderPackAdapter(@Nullable List<HomeShoppingCarDataBean.ShopingCarProductBean> data) {
        super(R.layout.item_create_order_packlist,  data == null ? new ArrayList<>():data);
        if (data != null && data.size() > 0) {
            supplierId = data.get(0).getSupplierId();
            for (HomeShoppingCarDataBean.ShopingCarProductBean temp : data) {
                if (supplierId == temp.getSupplierId()) {
                    isOnePack = true;
                } else {
                    isOnePack = false;
                    break;
                }
            }
        }
    }


    public Map<String, EditText> getHangYeEditTextMap() {
        return hangYeEditTextMap;
    }

    public CreateOrderPackAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeShoppingCarDataBean.ShopingCarProductBean item) {
        helper.setText(R.id.tv_product_name, item.getGoodsTitle());
        helper.setText(R.id.tv_product_specs, item.getSpecification());
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setTagText(StringUtils.getPrice(item.getUserRealPriceE2()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartText("¥");
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setStartProportion(0.78f);
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndText(StringUtils.getPriceDecimal(item.getUserRealPriceE2()));
        ((RelativeSizeTextView) helper.getView(R.id.tv_product_price)).setEndProportion(0.78f);
        helper.setText(R.id.tv_product_num, item.getGoodsNum() + "件");
        ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getCoverPic()));
        //((SimpleDraweeView) helper.getView(R.id.sdv_pic)).getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(8f));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                if (!isOnePack) {
                    if (position == 1) {
                        holder.getView(R.id.line).setVisibility(View.GONE);
                        holder.getView(R.id.tv_pack_name).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_pack_name, "包裹" + packName);
                    } else {
                        if (mData.get(position - 1).getSupplierId() == mData.get(position - 2).getSupplierId()) {
                            holder.getView(R.id.line).setVisibility(View.GONE);
                            holder.getView(R.id.tv_pack_name).setVisibility(View.GONE);
                        } else {
                            holder.getView(R.id.line).setVisibility(View.VISIBLE);
                            holder.getView(R.id.tv_pack_name).setVisibility(View.VISIBLE);
                            packName++;
                            holder.setText(R.id.tv_pack_name, "包裹" + packName);
                        }

                    }
                } else {
                    holder.getView(R.id.tv_pack_name).setVisibility(View.GONE);
                    holder.getView(R.id.line).setVisibility(View.GONE);
                }
                if (position == mData.size()) {
                    holder.getView(R.id.tv_remarks_tip).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ed_remarks).setVisibility(View.VISIBLE);
                    hangYeEditTextMap.put(mData.get(position - 1).getSupplierId() + "", holder.getView(R.id.ed_remarks));
                } else {
                    if (mData.get(position - 1).getSupplierId() != mData.get(position).getSupplierId()) {
                        holder.getView(R.id.tv_remarks_tip).setVisibility(View.VISIBLE);
                        holder.getView(R.id.ed_remarks).setVisibility(View.VISIBLE);
                        hangYeEditTextMap.put(mData.get(position - 1).getSupplierId() + "", holder.getView(R.id.ed_remarks));
                    } else {
                        holder.getView(R.id.tv_remarks_tip).setVisibility(View.GONE);
                        holder.getView(R.id.ed_remarks).setVisibility(View.GONE);
                    }
                }
                EditText et_backUp = holder.getView(R.id.ed_remarks);
                et_backUp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s) && s.length() >= 50) {
                            if (mListener != null) {
                                mListener.onBackUpFull();
                            }
                        }
                    }
                });

                break;
        }
    }

    public void setListener(OnOrderListener listener) {
        mListener = listener;
    }

    public interface OnOrderListener {
        void onBackUpFull();
    }
}
