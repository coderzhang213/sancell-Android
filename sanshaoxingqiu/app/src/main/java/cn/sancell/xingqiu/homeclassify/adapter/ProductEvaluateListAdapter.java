package cn.sancell.xingqiu.homeclassify.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yetland.ratingbar.DtRatingBar;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.util.TextViewLinesUtil;

/**
 * Created by ai11 on 2019/7/17.
 */

public class ProductEvaluateListAdapter extends BaseQuickAdapter<EvaluateListDataBean.EvaluateBean, BaseViewHolder> {
    private Context context;
    private ItemPicAction itemPicAction;

    public ProductEvaluateListAdapter(int layoutResId, @Nullable List<EvaluateListDataBean.EvaluateBean> data) {
        super(layoutResId, data);
    }

    public ProductEvaluateListAdapter(@Nullable List<EvaluateListDataBean.EvaluateBean> data, Context context, ItemPicAction itemPicAction) {
        super(R.layout.item_evaluate_list, data);
        this.itemPicAction=itemPicAction;
        this.context = context;
    }

    public ProductEvaluateListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluateListDataBean.EvaluateBean item) {
        helper.setText(R.id.tv_user_name, item.getUser().getNickNameFormatting());
        if (!StringUtils.isTextEmpty(item.getUser().getGravatar())) {
            ((SimpleDraweeView) helper.getView(R.id.riv_user_photo)).setImageURI(Uri.parse(item.getUser().getGravatar()));
        }
        switch (item.getUser().getRealMemberLevel()) {
            case 1:
                helper.setImageResource(R.id.iv_member_level, R.mipmap.icon_home_user_name0);
                break;
            case 2:
                helper.setImageResource(R.id.iv_member_level, R.mipmap.icon_home_user_name1);
                break;
            case 3:
                helper.setImageResource(R.id.iv_member_level, R.mipmap.icon_home_user_name2);
                break;
            case 4:
                helper.setImageResource(R.id.iv_member_level, R.mipmap.icon_home_user_name3);
                break;
        }
        helper.setText(R.id.tv_time, item.getPublishTime());
        helper.setText(R.id.tv_content, item.getContent());
        helper.getView(R.id.rating_bar).setEnabled(false);
        ((DtRatingBar) helper.getView(R.id.rating_bar)).setRating(item.getScore());
        if (TextViewLinesUtil.getTextViewLines(helper.getView(R.id.tv_content), ScreenUtils.dip2px(context, 302)) > 5) {
            helper.getView(R.id.iv_open_close).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.tv_content)).setMaxLines(5);
            helper.setImageResource(R.id.iv_open_close, R.mipmap.icon_productinfo_evaluate_open);
            helper.getView(R.id.iv_open_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((TextView) helper.getView(R.id.tv_content)).getMaxLines() == 5) {
                        helper.setImageResource(R.id.iv_open_close, R.mipmap.icon_productinfo_evaluate_close);
                        ((TextView) helper.getView(R.id.tv_content)).setMaxLines(100);
                    } else {
                        helper.setImageResource(R.id.iv_open_close, R.mipmap.icon_productinfo_evaluate_open);
                        ((TextView) helper.getView(R.id.tv_content)).setMaxLines(5);
                    }
                }
            });
        } else {
            helper.getView(R.id.iv_open_close).setVisibility(View.GONE);
        }
        if (item.getDetailPicArr() != null) {
            if (item.getDetailPicArr().size() > 1) {
                helper.getView(R.id.sdv_pic).setVisibility(View.GONE);
                helper.getView(R.id.gv_pics).setVisibility(View.VISIBLE);
                ((GridView) helper.getView(R.id.gv_pics)).setSelector(new ColorDrawable(Color.TRANSPARENT));
                ((GridView) helper.getView(R.id.gv_pics)).setAdapter(new ProductEvaluateListPicsAdapter(context, item.getDetailPicArr()));
                ((GridView) helper.getView(R.id.gv_pics)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        itemPicAction.picItemAction(item.getDetailPicArr(), i);
                    }
                });
            } else if (item.getDetailPicArr().size() == 1) {
                helper.getView(R.id.gv_pics).setVisibility(View.GONE);
                helper.getView(R.id.sdv_pic).setVisibility(View.VISIBLE);
                ((SimpleDraweeView) helper.getView(R.id.sdv_pic)).setImageURI(Uri.parse(item.getDetailPicArr().get(0).getCoverPic()));
                helper.getView(R.id.sdv_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemPicAction.picItemAction(item.getDetailPicArr(), 0);
                    }
                });
            } else {
                helper.getView(R.id.gv_pics).setVisibility(View.GONE);
                helper.getView(R.id.sdv_pic).setVisibility(View.GONE);
            }
        } else {
            helper.getView(R.id.gv_pics).setVisibility(View.GONE);
            helper.getView(R.id.sdv_pic).setVisibility(View.GONE);
        }

    }

    public interface ItemPicAction {
        void picItemAction(List<EvaluateListDataBean.EvaluateBean.PicArr> data_pics, int pos);
    }

    private int currentPage = 0;

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getNextPage() {
        return (this.currentPage + 1);
    }

    public void correctCurrentPage() {
        this.currentPage += 1;
    }

    public void resetCurrentPage() {
        this.currentPage = 0;
    }
}
