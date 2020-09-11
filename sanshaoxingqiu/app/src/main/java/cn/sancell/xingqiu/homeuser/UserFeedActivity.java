package cn.sancell.xingqiu.homeuser;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeuser.contract.UserFeedContract;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class UserFeedActivity extends BaseMVPToobarActivity<UserFeedContract.UserFeedPresenter>
        implements UserFeedContract.UserFeedView {

    @BindView(R.id.ed_feed)
    EditText ed_feed;
    @BindView(R.id.ed_contact)
    EditText ed_contact;
    @BindView(R.id.tv_commit_feed)
    TextView tv_commit_feed;

    @Override
    protected UserFeedContract.UserFeedPresenter createPresenter() {
        return new UserFeedContract.UserFeedPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_feed;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.feed_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        tv_commit_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isTextEmpty(ed_feed.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_feed_content_tip);
                    return;
                }
                if (StringUtils.isTextEmpty(ed_contact.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_contact_way_tip);
                    return;
                }
                mPresenter.CommitFeed(ed_feed.getText().toString(), ed_contact.getText().toString(), UserFeedActivity.this);
            }
        });
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void commitFeedSuccess() {
        finish();
    }
}
