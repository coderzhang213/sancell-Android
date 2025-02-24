package cn.sancell.xingqiu.live.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.netease.nim.uikit.common.fragment.TFragment;

import cn.sancell.xingqiu.R;

/**
 * Created by zhukkun on 1/5/17.
 */
public abstract class RoomBaseFragment extends TFragment {

    public final String Tag = this.getClass().getSimpleName();
    public Activity mActivity;
    protected View findViewById(int id) {
        return getView().findViewById(id);
    }

    private static Handler handler;

    public String TAG = this.getClass().getSimpleName();

    protected abstract void handleIntent(Bundle nBundle);

    protected abstract int getContentView();

    protected abstract void initView();

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        handleIntent(getArguments());
        return inflater.inflate(getContentView(), container, false);
    }

    protected void showToast(final String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext().getApplicationContext(), text, Toast.LENGTH_LONG);
        }
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            getActivity().runOnUiThread(new Runnable() {
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

    protected void showConfirmDialog(String title, String message, DialogInterface.OnClickListener okEvent, DialogInterface.OnClickListener cancelEvent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                okEvent);
        if (cancelEvent != null) {
            builder.setNegativeButton(R.string.cancel,
                    cancelEvent);
        }
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag, "onResume " + this);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Tag, "onPause " + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Tag, "onDestroy " + this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
        Log.d(Tag, "onCreate " +  this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Tag, "onViewCreated " + this);
    }
}
