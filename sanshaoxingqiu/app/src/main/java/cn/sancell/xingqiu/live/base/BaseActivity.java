package cn.sancell.xingqiu.live.base;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cn.sancell.xingqiu.live.dialog.ComfirmDialog;
import cn.sancell.xingqiu.live.user.LiveCache;


/**
 * Created by zhukkun on 1/5/17.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Fragment mFragment;
    private static Handler handler;

    public String TAG = this.getClass().getSimpleName();

    protected abstract void handleIntent(Intent intent);

    protected abstract int getContentView();

    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(getContentView());
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ActivityManagers:", getClass().getName());
        LiveCache.setVisibleActivity(this);
    }

    /**
     * 添加
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LiveCache.setVisibleActivity(null);
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private Toast mToast;

    public void showToast(final String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        }
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast.setText(text);
                    mToast.show();
                }
            });
        } else {
            mToast.setText(text);
            mToast.show();
        }
    }

    public void showConfirmDialog(String title, String message, DialogInterface.OnClickListener okEvent, DialogInterface.OnClickListener cancelEvent) {
        ComfirmDialog mCutCityDialog = new ComfirmDialog(this);
        mCutCityDialog.setOnCutCityLinsener(new ComfirmDialog.OnCutCityLinsener() {
            @Override
            public void onConfirmLinsener() {
                okEvent.onClick(mCutCityDialog, 1);
            }

            @Override
            public void onCancerLinsener() {

            }
        });
        mCutCityDialog.setMsg(message);
        mCutCityDialog.show();
        //        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.setPositiveButton(R.string.ok,
//                okEvent);
//        builder.setNegativeButton(R.string.cancel,
//                cancelEvent);
//        builder.show();
    }
}
