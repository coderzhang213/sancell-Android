package cn.sancell.xingqiu.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.widget.effect.BlurTransformation;

/**
 * Created by zj on 2019/12/10.
 */
public class ImageLoaderUtils {
    /**
     * 默认加载方式
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView, int errId) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(errId).error(errId)
                .dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 默认加载方式
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleCrop());

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param resId
     * @param imageView
     */
    public static void loadCircleImageWithRes(Context context, int resId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleCrop());

        Glide.with(context)
                .load(resId)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView, int errId) {
        if (TextUtils.isEmpty(url)) {
            loadCircleImageWithRes(context, errId, imageView);
            return;
        }
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .placeholder(errId)
                .error(errId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleCrop());

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius    圆角大小
     */
    public static void loadRoundImage(Context context, String url, ImageView imageView, int radius) {
        if (imageView == null) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadRoundImage(Context context, Bitmap url, ImageView imageView, int radius) {
        if (imageView == null) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadRoundImage(Context context, String url, ImageView imageView, int radius, int defaultIcon) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH).error(defaultIcon).placeholder(defaultIcon)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadRoundImageBitmap(Context context, Bitmap bitmap, ImageView imageView, int radius) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .load(bitmap)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius    圆角大小
     */
    public static void loadRound2Image(Context context, String url, ImageView imageView, int radius) {

        // 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
        //顶部左边圆角
        RoundedCornersTransformation transformation = new RoundedCornersTransformation
                (radius, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
        //顶部右边圆角
        RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                (radius, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);

        //组合各种Transformation,
        MultiTransformation<Bitmap> mation = new MultiTransformation<>
                //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                (new CenterCrop(), transformation, transformation1);

        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.getWidth(), imageView.getHeight())
                .transform(mation);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载图片指定大小
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadSizeImage(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载本地图片文件
     *
     * @param context
     * @param file
     * @param imageView
     */
    public static void loadFileImage(Context context, File file, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(context)
                .load(file)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载高斯模糊
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius    模糊级数 最大25
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions requestOptions = new RequestOptions()
                .override(300)
                .transforms(new BlurTransformation(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }


    /**
     * 加载gif图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadGifImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}
