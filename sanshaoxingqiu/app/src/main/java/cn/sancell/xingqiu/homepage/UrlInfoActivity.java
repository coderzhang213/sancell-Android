package cn.sancell.xingqiu.homepage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.bean.SharInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.live.utils.JsonUtils;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.kt.BaseActivity;

/**
 * h5加载页面
 */
public class UrlInfoActivity extends BaseActivity {
    @BindView(R.id.wb_content)
    BridgeWebView wb_content;
    @BindView(R.id.progressBar)
    ProgressBar progressbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.view_top)
    View view_top;

    private String url;
    private String uiTitle;
    private int isNeedLoginData;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private boolean isVipInfo = false;

    public static void start(Context context, String url, String title) {
        Intent intent = new Intent(context, UrlInfoActivity.class);
        intent.putExtra(Constants.Key.KEY_2, title);
        intent.putExtra(Constants.Key.KEY_1, url);
        context.startActivity(intent);
    }

    public static void startVip(Context context, String url, String title) {
        Intent intent = new Intent(context, UrlInfoActivity.class);
        intent.putExtra(Constants.Key.KEY_2, title);
        intent.putExtra(Constants.Key.KEY_1, url);
        intent.putExtra(Constants.Key.KEY_7, true);//是否vip详情加载
        context.startActivity(intent);
    }


    public static void start(Context context, String url, String title, boolean isSteep, int color, boolean backWhite) {
        Intent intent = new Intent(context, UrlInfoActivity.class);
        intent.putExtra(Constants.Key.KEY_2, title);
        intent.putExtra(Constants.Key.KEY_1, url);
        intent.putExtra(IntentKey.HIDE_URL_BAR, isSteep);
        intent.putExtra(IntentKey.WEB_H5_BAR_COLOR, color);
        intent.putExtra(IntentKey.WEB_H5_BAR_BACK_COLOR, backWhite);
        context.startActivity(intent);
    }


    public static void start(Context context, String url, String title, int needLoginInfo) {
        Intent intent = new Intent(context, UrlInfoActivity.class);
        intent.putExtra(Constants.Key.KEY_2, title);
        intent.putExtra(Constants.Key.KEY_1, url);
        intent.putExtra(Constants.Key.KEY_3, needLoginInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initial();
    }

    @Override
    public void setWindeMode() {
        super.setWindeMode();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    protected void initial() {
        ButterKnife.bind(this);
        uiTitle = getIntent().getStringExtra(Constants.Key.KEY_2);
        isVipInfo = getIntent().getBooleanExtra(Constants.Key.KEY_7, false);
        if (isVipInfo) {
            StatusBarUtil.setTranslucentStatus(this);
            StatusBarUtil.setStatusBarDarkTheme(this, true);
            toolbar.setBackgroundResource(R.mipmap.web_title_bg);
            tv_title.setTextColor(getResources().getColor(R.color.vip_color_text));
            toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_white_back));
            view_top.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);

        } else {
            toolbar.setBackgroundColor(getIntent().getIntExtra(IntentKey.WEB_H5_BAR_COLOR, getResources().getColor(R.color.white)));
            if (getIntent().getBooleanExtra(IntentKey.HIDE_URL_BAR, false)) {
                StatusBarUtil.setTranslucentStatus(this);
                StatusBarUtil.setRootViewFitsSystemWindows(this, false);
            } else {
                StatusBarUtil.setStatusBarDarkTheme(this, true);
                StatusBarUtil.setRootViewFitsSystemWindows(this, false);
            }
            //深色toolbar
            if (getIntent().getBooleanExtra(IntentKey.WEB_H5_BAR_BACK_COLOR, true)) {
                toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_black_back));
            } else {
                toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_white_back));
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                tv_title.setTextColor(getResources().getColor(R.color.white));
                view_top.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
                view_top.setBackgroundColor(getIntent().getIntExtra(IntentKey.WEB_H5_BAR_COLOR, getResources().getColor(R.color.white)));
            }
        }

        initToolBar();
        Log.e("webWidth = ", wb_content.getWidth() + "");
        Log.e("screenWidth = ", ScreenUtils.getScreenWidth(this) + "");

        url = getIntent().getStringExtra(Constants.Key.KEY_1);
        // Log.e("keey", url);
        isNeedLoginData = getIntent().getIntExtra(Constants.Key.KEY_3, 0);
        WebSettings ws = wb_content.getSettings();
        ws.setAllowFileAccess(false);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setBuiltInZoomControls(false);
        ws.setDomStorageEnabled(true);
        ws.setSupportZoom(false);
        ws.setJavaScriptEnabled(true);
        ws.setBlockNetworkImage(false);
        ws.setAllowContentAccess(true);
        ws.setAppCacheEnabled(false);
        ws.setDatabaseEnabled(true);
        ws.setTextZoom(100); //文字100显示
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setSavePassword(true);
        ws.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 允许javascript出错
            try {
                Method method = Class.forName("android.webkit.WebView").
                        getMethod("setWebContentsDebuggingEnabled", Boolean.TYPE);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(null, true);
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       /* wb_content.setWebViewClient(new WebViewClient() {
            // 覆写shouldOverrideUrlLoading实现内部显示网页
//
//            @Override
//            public void onLoadResource(android.webkit.WebView view, String url) {
//                super.onLoadResource(view, url);
//                // 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
//                imgReset();//重置webview中img标签的图片大小
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO 自动生成的方法存根
                if (url.startsWith("http://") || url.startsWith("https://")) { // 4.0以上必须要加
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                // handler.cancel();// Android默认的处理方式
                handler.proceed();// 接受所有网站的证书
                // handleMessage(Message msg);// 进行其他处理
            }

        });*/
        wb_content.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按返回键操作并且能回退网页
                if (keyCode == KeyEvent.KEYCODE_BACK && wb_content.canGoBack()) {
                    //后退
                    wb_content.goBack();
                    return true;
                }
            }
            return false;
        });
        wb_content.setWebChromeClient(new WebChromeClient() {

            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getBaseContext(), "不能打开选择的文件", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress == 100) {
                    progressbar.setVisibility(View.GONE);// 加载完网页进度条消失
                } else {
                    progressbar.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
                    progressbar.setProgress(newProgress);// 设置进度值
                }

            }
        });
        if (isNeedLoginData == 1) {  //需要传值给h5
            //StringEntity se = null;
            String reqTime = StringUtils.getCurrentTime();
            String skey = PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");
            String hashToken = RSAUtils.encryptByPublic(reqTime + skey);
            String clientId = "3";
            String postData;
            try {
                postData = "reqTime=" + URLEncoder.encode(reqTime, "UTF-8") +
                        "&skey=" + URLEncoder.encode(skey, "UTF-8") +
                        "&hashToken=" + URLEncoder.encode(hashToken, "UTF-8") +
                        "&clientId=" + URLEncoder.encode(clientId, "UTF-8");
                wb_content.postUrl(url, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            //wb_content.loadUrl("http://devm1.sanshaoxingqiu.cn/VxPay.html");
            wb_content.loadUrl(url);
        }
        wb_content.registerHandler("androidNewWXShare", (data, function) -> {
            if (TextUtils.isEmpty(data)) {
                return;
            }
            SharInfo mSharInfo = JsonUtils.getObject(data, SharInfo.class);
            toShar(mSharInfo);

        });
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (userBean != null && userBean.getmActivityData() != null && userBean.getmActivityData().getIsShow() == 1 && !StringUtils.isTextEmpty(userBean.getmActivityData().getTitle())) {  //h5调起Android端分享处理
            title = userBean.getmActivityData().getTitle();
            description = userBean.getmActivityData().getDesc();
            wb_content.registerHandler("androidWXShare", (data, function) -> {
                //显示js传递给Android的消息
                image = new UMImage(UrlInfoActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));
                DialogUtil.getShareDialog(UrlInfoActivity.this, image, data, title, description, umShareListener);
                //Android返回给JS的消息
                //function.onCallBack("我是js调用Android返回数据：" );
            });
        }
        /**
         * 商品详情
         */
        wb_content.registerHandler("androidIntentToProduct", (data, function) -> {     //h5调起Android端跳转商品详情页
            //显示js传递给Android的消息
//            Intent intent = new Intent(UrlInfoActivity.this, ProductInfoActivity.class);
//            intent.putExtra(Constants.Key.KEY_1, data);
//            startActivity(intent);
            GoodsDetailActivity.start(this, Integer.parseInt(data));

            //Android返回给JS的消息
            //function.onCallBack("我是js调用Android返回数据：" );
        });

        /**
         * 一元购
         */
        wb_content.registerHandler("androidIntentOnePurchase", (data, function) -> {


        });

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (checkServer()) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    /**
     * 检测是否是加载的客服界面
     */
    private boolean checkServer() {
        if (url.contains(UrlConstants.SERVER_API)) {//如果是客服界面，就直接退出这个界面
            finish();
            return true;
        }
        return false;
    }

    public void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (checkServer()) {
                return;
            }
            if (wb_content != null && wb_content.canGoBack()) {
                wb_content.goBack();
            } else {
                finish();
            }
        });
        tv_title.setText(uiTitle);

    }

    /**
     * 跳转去分享
     *
     * @param mSharInfo
     */
    @SuppressLint("CheckResult")
    private void toShar(SharInfo mSharInfo) {
        if (mSharInfo == null) {
            return;
        }
        Glide.with(UrlInfoActivity.this).asBitmap().load(mSharInfo.getShareLogo()).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                image = new UMImage(UrlInfoActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));
                DialogUtil.getShareDialog(UrlInfoActivity.this, image, mSharInfo.getShareUrl(), mSharInfo.getShareTitle(), mSharInfo.getShareDesc(), umShareListener);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                image = new UMImage(UrlInfoActivity.this, resource);
                DialogUtil.getShareDialog(UrlInfoActivity.this, image, mSharInfo.getShareUrl(), mSharInfo.getShareTitle(), mSharInfo.getShareDesc(), umShareListener);

            }
        });
        //显示js传递给Android的消息
        //Android返回给JS的消息
        //function.onCallBack("我是js调用Android返回数据：" );
    }

    private String title = "";
    private String description = "";
    private UMImage image;

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            SCApp.getInstance().showSystemCenterToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SCApp.getInstance().showSystemCenterToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            /*if (platform != SHARE_MEDIA.QQ) {
                SCApp.getInstance().showSystemCenterToast("分享取消");
            }*/
        }
    };

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        wb_content.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img');" +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];" +
                "img.style.maxWidth = '100%'; img.style.height = 'auto';" +
                "}" +
                "})()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = data == null || resultCode != UrlInfoActivity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getBaseContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_url_info;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
