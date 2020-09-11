package cn.sancell.xingqiu.homepage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.photoview.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private RelativeLayout rl_all;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        rl_all = (RelativeLayout) v.findViewById(R.id.rl_all);
        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        /*RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
        lp1.width = ScreenUtils.getScreenWidth(getActivity());
        lp1.height = ScreenUtils.getScreenWidth(getActivity());
        mImageView.setLayoutParams(lp1);*/
        mAttacher = new PhotoViewAttacher(mImageView);

        Glide.with(getActivity())
                .load(mImageUrl)
                .into(mImageView);
        mAttacher.update();
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        return v;
    }

}
