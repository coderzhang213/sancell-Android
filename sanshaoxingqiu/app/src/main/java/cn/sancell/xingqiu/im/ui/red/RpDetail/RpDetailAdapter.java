package cn.sancell.xingqiu.im.ui.red.RpDetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;

/**
  * @author Alan_Xiong
  *
  * @desc: 红包详情
  * @time 2019-11-19 09:50
  */
public class RpDetailAdapter extends BaseQuickAdapter<RpDetailRes.ReceiveDetail, BaseViewHolder> {


    public RpDetailAdapter(@Nullable List<RpDetailRes.ReceiveDetail> data) {
        super(R.layout.recycle_rp_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RpDetailRes.ReceiveDetail item) {

        ImageView ivHeader = helper.getView(R.id.iv_header);
        if (!TextUtils.isEmpty(item.getUserIcon)){
            Glide.with(mContext).load(item.getUserIcon).apply(new RequestOptions().circleCrop()).into(ivHeader);
        }else{
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.mipmap.icon_def_team)).apply(new RequestOptions().circleCrop()).into(ivHeader);
        }
        helper.setText(R.id.tv_name,item.getUserName);
        helper.setText(R.id.tv_money,String.format(mContext.getResources().getString(R.string.price_rmb),item.getGetMoney()));
        helper.setText(R.id.tv_time,item.receiveDate);
        AppCompatTextView tv_lucky = helper.getView(R.id.tv_lucky);
        if (item.isSkyChild){
            tv_lucky.setVisibility(View.VISIBLE);
        }else{
            tv_lucky.setVisibility(View.GONE);
        }

    }
}
