package cn.sancell.xingqiu.base;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.hujiang.permissiondispatcher.NeedPermission;
import com.netease.nim.uikit.common.media.imagepicker.camera.ConfirmationDialog;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.network.NetWorkMonitorManager;
import cn.sancell.xingqiu.constant.network.NetWorkState;
import cn.sancell.xingqiu.constant.network.onNetWorkStateChangeLinsener;
import cn.sancell.xingqiu.dialog.ComfirmRpDialog;
import cn.sancell.xingqiu.live.dialog.ComfirmDialog;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.observer.OnObserver;
import cn.sancell.xingqiu.util.observer.ObserverKey;
import cn.sancell.xingqiu.util.observer.ObserverManger;
import cn.sancell.xingqiu.widget.Loading_view;


/**
 * @author huyingying
 * @ClassName: BaseActivity
 * @Description:
 * @date 2018/4/2 下午8:39
 */
public class BaseActivity extends RxAppCompatActivity implements BaseView, onNetWorkStateChangeLinsener {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    protected Loading_view loading_view;
    private ComfirmDialog alterDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 隐去标题栏（应用程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        NetWorkMonitorManager.getInstance().register(this);
        /*if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE) {
            //跳到闪屏页
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }*/
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        /*if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }*/
        loading_view = new Loading_view(this, R.style.LoadingDialog);
        ObserverManger.getInstance(ObserverKey.SERVER_ERROR).registerObserver(mServerEorrorObserver);
    }

    private OnObserver mServerEorrorObserver = new OnObserver() {
        @Override
        public void update(Object obj) {
            startActivity(new Intent(BaseActivity.this, ServerErrorActivity.class));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverManger.getInstance(ObserverKey.SERVER_ERROR).removeObserver(mServerEorrorObserver);
        NetWorkMonitorManager.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ActivityManage:", this.getClass().getName());

        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onLoginOut() {

    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            if (loading_view != null) {
                loading_view.show();
            }
        } else {
            if (loading_view != null) {
                loading_view.dismiss();
            }
        }
    }


    @Override
    public void onNetWorkStateChangeLinener(NetWorkState netWorkState) {
        if (netWorkState == NetWorkState.NONE) {
            if (alterDialog == null) {
                alterDialog = NetWorkMonitorManager.getInstance().getAlterDialog(this);
            }
            if (!alterDialog.isShowing()) {
                alterDialog.show();
            }
        } else {
            if (alterDialog != null && alterDialog.isShowing()) {
                alterDialog.dismiss();
            }

        }
    }

    @NeedPermission(permissions = {Manifest.permission.CALL_PHONE})
    public void callPhone(String phoneNum) {
        ComfirmRpDialog dialog = new ComfirmRpDialog(this);
        dialog.setTitleMsg("联系电话："+phoneNum);
        dialog.setListener(new ComfirmRpDialog.OnPhoneCallListener() {
            @Override
            public void onCallListener() {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent.setData(data);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}
