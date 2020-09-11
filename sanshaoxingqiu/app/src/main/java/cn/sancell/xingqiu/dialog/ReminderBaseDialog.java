package cn.sancell.xingqiu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;

/**
 * Created by zj on 2019/12/13.
 */
public abstract class ReminderBaseDialog extends Dialog implements View.OnClickListener {
    public RecyclerView rl_list;
    public Activity context;
    public View rl_not_address;
    private TextView tv_title;
    private TextView tv_new_address;
    private TextView tv_read_packer;//
    private double mScreenRatio = 0.6;

    public ReminderBaseDialog(@NonNull Activity context) {
        super(context, R.style.DaoxilaDialog_Alert);
        this.context = context;
        initView();
    }

    public void setScreenRaiton(double screenRatio) {
        this.mScreenRatio = screenRatio;
    }

    private void initView() {
        setContentView(R.layout.dialog_select_address);
        rl_list = findViewById(R.id.rl_list);
        rl_not_address = findViewById(R.id.rl_not_address);
        tv_new_address = findViewById(R.id.tv_new_address);
        LinearLayout ll_add_view_content = findViewById(R.id.ll_add_view_content);
        View mAddView = getAddView();
//        if (mAddView != null) {
//            ll_add_view_content.addView(mAddView);
//        }
        setAddView(ll_add_view_content);
        tv_new_address.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        Window window = getWindow();
        Display display = ((WindowManager) SCApp.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //宽度全屏显示
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (display.getHeight() * mScreenRatio);
        //layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(layoutParams);
        //靠底部显示
        window.setGravity(Gravity.BOTTOM);
        setTitle(tv_title);
        setSubmitText(tv_new_address);
        setAdapter(rl_list);

    }

    public abstract View getAddView();

    public void setAdapter(RecyclerView rl_list) {

    }

    public void setAddView(LinearLayout ll_view) {

    }

    public void setTitle(TextView tv_title) {

    }

    public void setSubmitText(TextView tv_new_address) {

    }

    /**
     * 底部按钮
     */
    public void onSubmitLinsener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_address://创建新地址
                onSubmitLinsener();
                break;

        }
    }
}
