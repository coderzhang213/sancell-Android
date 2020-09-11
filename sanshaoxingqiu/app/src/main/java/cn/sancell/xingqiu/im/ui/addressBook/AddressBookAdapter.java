package cn.sancell.xingqiu.im.ui.addressBook;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.im.entity.res.AddressRes;
import cn.sancell.xingqiu.im.entity.res.TeamBookRes;

/**
  * @author Alan_Xiong
  *
  * @desc: im通讯录adapter
  * @time 2019-11-14 14:03
  */
public class AddressBookAdapter extends BaseQuickAdapter<AddressRes.TeamData, BaseViewHolder> {

    public AddressBookAdapter( @Nullable List<AddressRes.TeamData> data) {
        super(R.layout.recycle_im_address_book, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressRes.TeamData item) {
        AppCompatImageView ivHead = helper.getView(R.id.iv_head);
        RequestOptions options = new RequestOptions().centerCrop().transform(new RoundedCorners(ScreenUtil.dip2px(6)));

        if (!TextUtils.isEmpty(item.icon)){
            Glide.with(mContext).load(item.icon).apply(options).into(ivHead);
        }else{
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.mipmap.icon_def_team)).into(ivHead);
        }
        helper.setText(R.id.tv_head_name,item.groupName);
        helper.setText(R.id.tv_sub,item.intro);

    }
}
