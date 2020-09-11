package cn.sancell.xingqiu.homeclassify.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.devio.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.constant.Constants;

/**
 * 主界面选中图片的图片列表适配器
 *
 * @author assur
 */
public class ImageSelectResultAdapter extends BaseAdapter {
    private List<TImage> mDataList = new ArrayList<TImage>();
    private Context mContext;
    private ItemAction itemAction;

    public ImageSelectResultAdapter(Context context, List<TImage> dataList, ItemAction itemAction) {
        this.mContext = context.getApplicationContext();
        this.mDataList = dataList;
        this.itemAction = itemAction;
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == Constants.MAX_IMAGE_SIZE) {
            return Constants.MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
    }

    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == Constants.MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0
                || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {
        //所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = View.inflate(mContext, R.layout.item_image_select_result, null);
        SimpleDraweeView imageIv = convertView
                .findViewById(R.id.sdv_select_pic);
        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAction.deleteAction(position);
            }
        });
        if (isShowAddItem(position)) {
            iv_delete.setVisibility(View.GONE);
            imageIv.setImageResource(R.mipmap.icon_add_pic);
            if(mDataList!=null) {
                if (mDataList.size() == 1) {
                    imageIv.setImageResource(R.mipmap.icon_add_pic1);
                } else if (mDataList.size() == 2) {
                    imageIv.setImageResource(R.mipmap.icon_add_pic2);
                } else if (mDataList.size() == 3) {
                    imageIv.setImageResource(R.mipmap.icon_add_pic3);
                } else if (mDataList.size() == 4) {
                    imageIv.setImageResource(R.mipmap.icon_add_pic4);
                } else if (mDataList.size() == 5) {
                    imageIv.setImageResource(R.mipmap.icon_add_pic5);
                }
            }
        } else {
            iv_delete.setVisibility(View.VISIBLE);
            imageIv.setImageURI(Uri.fromFile(new File(mDataList.get(position).getOriginalPath())));
        }

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }

    public interface ItemAction {
        void deleteAction(int pos);
    }

}
