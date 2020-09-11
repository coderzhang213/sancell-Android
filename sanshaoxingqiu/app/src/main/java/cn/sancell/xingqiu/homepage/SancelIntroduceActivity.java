package cn.sancell.xingqiu.homepage;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseToobarActivity;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.usermember.MemberVipGiftBuyActivity;
import cn.sancell.xingqiu.util.ScreenUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * 三少平台介绍
 */
public class SancelIntroduceActivity extends BaseToobarActivity {

    @BindView(R.id.iv_introduce)
    ImageView iv_introduce;
    @BindView(R.id.tv_buy_member)
    TextView tv_buy_member;

    private boolean isShowBuyMember;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sancel_introduce;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("为什么选择三少医美？");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        isShowBuyMember = getIntent().getBooleanExtra(Constants.Key.KEY_1, false);
        int w = ScreenUtils.getScreenWidth(this);
        int h = w * 5836 / 750;
        iv_introduce.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        if (isShowBuyMember) {
            tv_buy_member.setVisibility(View.VISIBLE);
        } else {
            tv_buy_member.setVisibility(View.GONE);
        }
        tv_buy_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SancelIntroduceActivity.this, MemberVipGiftBuyActivity.class));
            }
        });
    }
}
