package cn.sancell.xingqiu.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2019/12/17.
 */
public class ServerErrorActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_error_layout);

    }

}
