package cn.sancell.xingqiu.homepage;

import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * 一元起购攻略
 */
public class OneYuanPurchaseGetMethodActivity extends BaseToobarActivity {
    @BindView(R.id.iv_share)
    ImageView iv_share;
    private String title = "";
    private String description = "";
    private String linkurl = "";
    private UMImage image;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_one_yuan_purchase_get_method;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("1元购攻略");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        UserBean userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (userBean.getmActivityOneDollarData() != null) {
            title = userBean.getmActivityOneDollarData().getTitle();
            description = userBean.getmActivityOneDollarData().getDesc();
            linkurl = userBean.getmActivityOneDollarData().getLink();
            image = new UMImage(this, userBean.getmActivityOneDollarData().getLogoUrl());
        }

        iv_share.setOnClickListener(view -> DialogUtil.getShareDialog(OneYuanPurchaseGetMethodActivity.this, image, linkurl, title, description, umShareListener));

    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(OneYuanPurchaseGetMethodActivity.this).onActivityResult(requestCode, resultCode, data);
    }

}
