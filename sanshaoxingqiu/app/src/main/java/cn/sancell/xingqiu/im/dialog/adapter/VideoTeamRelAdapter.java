package cn.sancell.xingqiu.im.dialog.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;

/**
 * @author Alan_Xiong
 * @desc: 视频关联商品
 * @time 2019-11-22 00:08
 */
public class VideoTeamRelAdapter extends BaseQuickAdapter<VideoRelationRes.InfoList, BaseViewHolder> {


    public VideoTeamRelAdapter(@Nullable List<VideoRelationRes.InfoList> data) {
        super(R.layout.recycle_im_team_recommend, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoRelationRes.InfoList item) {

        AppCompatImageView ivHead = helper.getView(R.id.iv_head);
        RequestOptions options = new RequestOptions().transform(new RoundedCorners(ScreenUtil.dip2px(6)));
        Glide.with(mContext).load(item.icon).apply(options).into(ivHead);
        helper.setText(R.id.tv_head_name, item.groupName);
        helper.setText(R.id.tv_sub, item.intro);
        AppCompatTextView tvbtn = helper.getView(R.id.btn_team);
        if (item.inGroup == 1) {
            tvbtn.setSelected(true);
            tvbtn.setText(R.string.join_chat);
        } else {
            tvbtn.setSelected(false);
            tvbtn.setText(R.string.team_apply_to_join);
        }
        helper.addOnClickListener(R.id.btn_team);

    }
}
