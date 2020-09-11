package cn.sancell.xingqiu.startup;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hujiang.permissiondispatcher.NeedPermission;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.HomeTabsActivity;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.dialog.PrivacyDialog;
import cn.sancell.xingqiu.dialog.listener.OnInviteSureListener;
import cn.sancell.xingqiu.login.CodeLoginActivity;
import cn.sancell.xingqiu.startup.contract.StartupContract;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.ScreenUtils;
import me.jessyan.autosize.utils.LogUtils;


public class StartupActivity extends BaseMVPActivity<StartupContract.StartupPresenter>
        implements StartupContract.StartView {
    @BindView(R.id.network_error)
    View mNetworkErrorLayout;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    int REQUEST_CODE_CONTACT = 101;  //获取手机状态请求
    String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_SETTING = 102;  //跳转设置权限请求
    private boolean flag;

    @Override
    protected StartupContract.StartupPresenter createPresenter() {
        return new StartupContract.StartupPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_startup;
    }

    @Override
    protected void initial() {
        if (getIntent() != null && getIntent().getData() != null) {
            SCApp.schemeUri = getIntent().getData();
            LogUtils.d("schememData = :" + SCApp.schemeUri);
        } else {
            if (!isTaskRoot()) {
                finish();
                return;
            }
        }
        ButterKnife.bind(this);
        //getData();

        int is_show = PreferencesUtils.getInt(Constants.Key.IS_SHOW_PRIVATE, 0);
        if (is_show == 0) {
            PrivacyDialog mPrivacyDialog = new PrivacyDialog(this);
            mPrivacyDialog.setmOnInviteSureListener(new OnInviteSureListener() {
                @Override
                public void onSure() {
                    PreferencesUtils.put(Constants.Key.IS_SHOW_PRIVATE, 1);
                    getData();
                }

                @Override
                public void onBack() {

                }
            });
            mPrivacyDialog.show();
        } else {
            getData();
        }
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @NeedPermission(permissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO})
    public void getData() {
        mPresenter.StartUp(StartupActivity.this);
    }


    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.StartUp(StartupActivity.this);
            }
        });
    }

    @Override
    public void alreadyLogin() {
        mNetworkErrorLayout.setVisibility(View.GONE);
        finish();
        //不要引导页
        //if (PreferencesUtils.getBoolean(Constants.Key.KEY_isSecondGuide, false)) {
        //是否含im key，不含key 跳转登陆
        if (!TextUtils.isEmpty(PreferencesUtils.getString(Constants.Key.key_im_accid, "")) &&
                !TextUtils.isEmpty(PreferencesUtils.getString(Constants.Key.key_im_token, ""))) {

            //startActivity(new Intent(StartupActivity.this, MainActivity.class));
            HomeTabsActivity.Companion.start(this, 0);
            overridePendingTransition(0, 0);
        } else {
            startActivity(new Intent(StartupActivity.this, CodeLoginActivity.class));
            overridePendingTransition(0, 0);
        }
        finish();
//        } else {
//            Intent intent = new Intent(StartupActivity.this, GuideActivity.class);
//            intent.putExtra(Constants.Key.KEY_1, true);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//        }

    }

    @Override
    public void notLogin() {
        mNetworkErrorLayout.setVisibility(View.GONE);

        //不要引导页
        //  if (PreferencesUtils.getBoolean(Constants.Key.KEY_isSecondGuide, false)) {
        startActivity(new Intent(StartupActivity.this, CodeLoginActivity.class));
        overridePendingTransition(0, 0);
        finish();
//        } else {
//            Intent intent = new Intent(StartupActivity.this, GuideActivity.class);
//            intent.putExtra(Constants.Key.KEY_1, false);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//        }
    }

    public void showMessageDialog(String title, String message, final boolean isbackdeal) {
        View view = getLayoutInflater().inflate(R.layout.message_tip_dialog,
                null);
        Dialog dialog_message = null;
        if (dialog_message == null) {
            dialog_message = new Dialog(StartupActivity.this, R.style.transparentFrameWindowStyle);
            dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(StartupActivity.this) * 8 / 10,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            // 设置点击外围解散
            dialog_message.setCanceledOnTouchOutside(true);
            TextView tv_titel = (TextView) view.findViewById(R.id.tv_tip_title);
            TextView tv_message = (TextView) view.findViewById(R.id.tv_tip_message);
            tv_titel.setText(title);
            tv_message.setText(message);
            TextView btn_dialog_sure = (TextView) view.findViewById(R.id.btn_sure);
            final Dialog finalDialog_message = dialog_message;
            btn_dialog_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalDialog_message.dismiss();
                }
            });
            dialog_message.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (isbackdeal) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_CODE_SETTING);
                    } else {
                        flag = false;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            });
        }
        dialog_message.show();
    }

}
