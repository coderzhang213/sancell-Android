package cn.sancell.xingqiu.homepage.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.library.banner.RecyclerViewBannerBase;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by ai11 on 2019/6/7.
 */

public class HomePageBannerAdapter extends RecyclerView.Adapter<HomePageBannerAdapter.NormalHolder> {

    private RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener;
    private Context context;
    private List<String> urlList;

    public HomePageBannerAdapter(Context context, List<String> urlList, RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener) {
        this.context = context;
        this.urlList = urlList;
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public HomePageBannerAdapter.NormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homepage_banner, parent, false);
        NormalHolder vh = new NormalHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NormalHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        holder.bannerItem.setImageURI(Uri.parse(urlList.get(position % urlList.size())));
        holder.bannerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position % urlList.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView bannerItem;

        NormalHolder(View itemView) {
            super(itemView);
            bannerItem = (SimpleDraweeView) itemView;
        }
    }
}
