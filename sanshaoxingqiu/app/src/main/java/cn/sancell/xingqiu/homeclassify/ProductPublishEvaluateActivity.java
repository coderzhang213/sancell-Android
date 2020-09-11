package cn.sancell.xingqiu.homeclassify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lcw.library.imagepicker.ImagePicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yetland.ratingbar.DtRatingBar;

import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.compress.CompressImage;
import org.devio.takephoto.compress.CompressImageImpl;
import org.devio.takephoto.model.TImage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeclassify.adapter.ImageSelectResultAdapter;
import cn.sancell.xingqiu.homeclassify.bean.GlideLoader;
import cn.sancell.xingqiu.homeclassify.contract.ProductPublishEvaluateContract;
import cn.sancell.xingqiu.homepage.ImagePagerActivity;
import cn.sancell.xingqiu.homeuser.UserInfoActivity;
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean;
import cn.sancell.xingqiu.tools.GlideEngine;
import cn.sancell.xingqiu.util.MyOSSUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.CusGridView;

public class ProductPublishEvaluateActivity extends BaseMVPToobarActivity<ProductPublishEvaluateContract.PublishEvaluatePresenter>
        implements ProductPublishEvaluateContract.PublishEvaluateView, View.OnClickListener {
    @BindView(R.id.sdv_product_pic)
    SimpleDraweeView sdv_product_pic;
    @BindView(R.id.rating_bar)
    DtRatingBar rating_bar;
    @BindView(R.id.tv_rating_bar)
    TextView tv_rating_bar;
    @BindView(R.id.ed_content)
    EditText ed_content;
    @BindView(R.id.gv_select_pics)
    CusGridView gv_select_pics;
    @BindView(R.id.tv_commit)
    TextView tv_commit;

    private ArrayList<TImage> data_pics = new ArrayList<>();
    private ImageSelectResultAdapter mAdapter;

    /**
     * 上个界面传的商品id和订单id和商品图片
     */
    private String orderId, orderDetailId, product_pic;

    private int evaluationCore = 5;


    private AtomicInteger cound_tag = new AtomicInteger(0);


    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;


    @Override
    protected ProductPublishEvaluateContract.PublishEvaluatePresenter createPresenter() {
        return new ProductPublishEvaluateContract.PublishEvaluatePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_publish_evaluate;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.evaluate_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        orderId = getIntent().getStringExtra(Constants.Key.KEY_1);
        orderDetailId = getIntent().getStringExtra(Constants.Key.KEY_2);
        product_pic = getIntent().getStringExtra(Constants.Key.KEY_3);
        sdv_product_pic.setImageURI(Uri.parse(product_pic));
        loading_view.setCancelable(false);
        tv_commit.setOnClickListener(this);
        rating_bar.setEnabled(true);
        rating_bar.setOnRatingChangeListener(new DtRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(float rating, int stars) {
                switch ((int) rating) {
                    case 1:
                        evaluationCore = 1;
                        tv_rating_bar.setText(R.string.rating_bar1);
                        break;
                    case 2:
                        evaluationCore = 2;
                        tv_rating_bar.setText(R.string.rating_bar2);
                        break;
                    case 3:
                        evaluationCore = 3;
                        tv_rating_bar.setText(R.string.rating_bar3);
                        break;
                    case 4:
                        evaluationCore = 4;
                        tv_rating_bar.setText(R.string.rating_bar4);
                        break;
                    case 5:
                        evaluationCore = 5;
                        tv_rating_bar.setText(R.string.rating_bar5);
                        break;
                }
            }
        });

        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >= 500) {
                    SCApp.getInstance().showSystemCenterToast(R.string.evaluate_content_max_tip);
                }
            }
        });
        gv_select_pics.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImageSelectResultAdapter(this, data_pics, itemAction);
        gv_select_pics.setAdapter(mAdapter);
        gv_select_pics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                    int maxSelectNum = Constants.MAX_IMAGE_SIZE - getDataSize();
                    PictureSelector.create(ProductPublishEvaluateActivity.this).openGallery(PictureMimeType.ofImage())
                            .isWeChatStyle(true)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .maxSelectNum(maxSelectNum)
                            .compress(true)
                            .compressSavePath(file.getPath())
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(PictureConfig.CHOOSE_REQUEST);
//                    ImagePicker.getInstance()
//                            .setTitle(getResources().getString(R.string.evaluate_select_pic))//设置标题
//                            .showCamera(true)//设置是否显示拍照按钮
//                            .showImage(true)//设置是否展示图片
//                            .showVideo(false)//设置是否展示视频
//                            .setMaxCount(maxSelectNum)//设置最大选择图片数目(默认为1，单选)
//                            .setSingleType(true)//设置图片视频不能同时选择
//                            //.setImagePaths(data_pics)//设置历史选择记录
//                            .setImageLoader(new GlideLoader())//设置自定义图片加载器
//                            .start(ProductPublishEvaluateActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                } else {
                    Intent intent = new Intent(ProductPublishEvaluateActivity.this, ImagePagerActivity.class);
                    ArrayList<String> data = new ArrayList<>();
                    for (TImage image : data_pics) {
                        data.add(image.getOriginalPath());
                    }
                    intent.putStringArrayListExtra(Constants.Key.KEY_1, data);
                    intent.putExtra(Constants.Key.KEY_2, position);
                    startActivity(intent);

                }
            }
        });
    }

    ImageSelectResultAdapter.ItemAction itemAction = new ImageSelectResultAdapter.ItemAction() {
        @Override
        public void deleteAction(int pos) {
            data_pics.remove(pos);
            mAdapter.notifyDataSetChanged();
        }
    };


    private int getDataSize() {
        return data_pics == null ? 0 : data_pics.size();
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void publishContentSuccess(String id) {
        if (data_pics.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < data_pics.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(data_pics.get(i).getCompressPath());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imgType", "jpg");
                    jsonObject.put("imgWidth", bitmap.getWidth() + "");
                    jsonObject.put("imgHeight", bitmap.getHeight() + "");
                    jsonObject.put("fileSize", bitmap.getByteCount() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            mPresenter.GetUpLoadPhotoInfo(id, jsonArray.toString(), ProductPublishEvaluateActivity.this);
        } else {
            SCApp.getInstance().showSystemCenterToast(R.string.evaluate_success);
            new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        finish();
                    }
                }
            }.start();
        }
    }

    @Override
    public void getuploadPhotoInfoSuccess(List<UpLoadPhotoInfoBean> upLoadPhotoInfoBeans) {
        showLoading(true);
        for (int i = 0; i < upLoadPhotoInfoBeans.size(); i++) {
            UpLoadPhotoInfoBean temp = upLoadPhotoInfoBeans.get(i);
            MyOSSUtils.getInstance().getOSs(ProductPublishEvaluateActivity.this, temp.getEndpoint(), temp.getAccessKeyId(), RSAUtils.decryptByPublic(temp.getAccessKeySecret()), temp.getSecurityToken());
            MyOSSUtils.getInstance().upImage(ProductPublishEvaluateActivity.this, ossUpCallback, temp.getBackName(), temp.getKeyName(), data_pics.get(i).getCompressPath());
        }
    }

    MyOSSUtils.OssUpCallback ossUpCallback = new MyOSSUtils.OssUpCallback() {
        @Override
        public void successImg(String img_url) {
            if (img_url != null) {
                if (cound_tag.incrementAndGet() >= data_pics.size()) {
                    showLoading(false);
                    SCApp.getInstance().showSystemCenterToast(R.string.evaluate_success);
                    new Thread() {
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } finally {
                                finish();
                            }
                        }
                    }.start();
                }
            } else {
                SCApp.getInstance().showSystemCenterToast(R.string.upload_pic_fail);
            }
        }

        @Override
        public void successVideo(String video_url) {

        }

        @Override
        public void inProgress(long progress, long zong) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            ArrayList<String> mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (mImagePaths != null) {
                int maxSize = 1048576;
                int width = 1080;
                int height = 1920;
                CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(maxSize)
                        .setMaxPixel(width >= height ? width : height)
                        .enableReserveRaw(true)
                        .create();
                ArrayList<TImage> images = new ArrayList<>();
                for (String pathPic : mImagePaths
                ) {
                    images.add(TImage.of(pathPic, TImage.FromType.OTHER));
                }
                CompressImageImpl.of(ProductPublishEvaluateActivity.this, compressConfig, images, new CompressImage.CompressListener() {
                    @Override
                    public void onCompressSuccess(ArrayList<TImage> images) {
                        data_pics.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCompressFailed(ArrayList<TImage> images, String msg) {
                        data_pics.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }
                }).compress();

            }
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> mPictures = PictureSelector.obtainMultipleResult(data);
            if (mPictures != null) {
                int maxSize = 1048576;
                int width = 1080;
                int height = 1920;
                CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(maxSize)
                        .setMaxPixel(width >= height ? width : height)
                        .enableReserveRaw(true)
                        .create();
                ArrayList<TImage> images = new ArrayList<>();
                for (LocalMedia pathPic : mPictures
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        images.add(TImage.of(pathPic.getAndroidQToPath(), TImage.FromType.OTHER));
                    } else {
                        images.add(TImage.of(pathPic.getPath(), TImage.FromType.OTHER));
                    }
                }
                CompressImageImpl.of(ProductPublishEvaluateActivity.this, compressConfig, images, new CompressImage.CompressListener() {
                    @Override
                    public void onCompressSuccess(ArrayList<TImage> images) {
                        data_pics.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCompressFailed(ArrayList<TImage> images, String msg) {
                        data_pics.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }
                }).compress();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                if (ed_content.getText().toString().length() < 5) {
                    SCApp.getInstance().showSystemCenterToast(R.string.evaluate_content_min_tip);
                    return;
                }
                mPresenter.publishEvaluateContent(orderId, orderDetailId, evaluationCore, ed_content.getText().toString(), ProductPublishEvaluateActivity.this);
                break;
        }
    }
}
