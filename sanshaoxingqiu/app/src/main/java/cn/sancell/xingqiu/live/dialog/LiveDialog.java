package cn.sancell.xingqiu.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;

/**
 * Created by zj on 2019/11/27.
 */
public class LiveDialog<T> extends Dialog {
    private RecyclerView rl_list;
    public Context context;
    public List<T> mLists;

    public LiveDialog(@NonNull Context context, List<T> mLists) {
        super(context, R.style.DaoxilaDialog_Alert);
        this.context = context;
        this.mLists = mLists;
        initView(context);
    }

    public LiveDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context context) {

        setContentView(R.layout.dialog_live_dilaog);
        rl_list = findViewById(R.id.rl_list);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window window = getWindow();
        Display display = ((WindowManager) SCApp.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //宽度全屏显示
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //layoutParams.height = (int) (display.getHeight() * 0.6);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(layoutParams);
        //靠底部显示
        window.setGravity(Gravity.BOTTOM);
        setAdapter(rl_list);
    }

    public void setAdapter(RecyclerView rl_list) {

    }
}
