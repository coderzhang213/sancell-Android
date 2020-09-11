package cn.sancell.xingqiu.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.bean.PurchaseInfo;
import cn.sancell.xingqiu.dialog.adapter.PinRuleAdapter;
import cn.sancell.xingqiu.dialog.adapter.PurchaseAdapter;
import cn.sancell.xingqiu.dialog.entity.PinRuleBean;

/**
 * Created by zj on 2019/12/13.
 */
public class PinRuleDialog extends ReminderBaseDialog {
    private List<PinRuleBean> mLists = new ArrayList<>();
    private PinRuleAdapter mAdapter;

    public PinRuleDialog(@NonNull Activity context) {
        super(context);
    }

    @Override
    public View getAddView() {
        return null;
    }

    @Override
    public void setTitle(TextView tv_title) {
        super.setTitle(tv_title);
        tv_title.setText("直拼规则");
    }

    @Override
    public void setSubmitText(TextView tv_new_address) {
        super.setSubmitText(tv_new_address);
        tv_new_address.setText("我知道了");
    }

    @Override
    public void onSubmitLinsener() {
        super.onSubmitLinsener();
        dismiss();
    }

    @Override
    public void setAdapter(RecyclerView rl_list) {
        super.setAdapter(rl_list);
        mAdapter = new PinRuleAdapter(mLists);
        rl_list.setLayoutManager(new LinearLayoutManager(context));
        rl_list.setAdapter(mAdapter);
    }


    public void setDatas(String hour){
        mLists.clear();
        mLists.add(new PinRuleBean("团长","团长是该团第一位购买并支付成功的人"));

        mLists.add(new PinRuleBean("开团","团长发起直拼，购买商品完成支付后，团即可开启"));

        mLists.add(new PinRuleBean("如何邀请好友参团","团长开团后可以将团链接分享给好友，好友也购买该商品即为参团。参团的团员也可以分享团链接邀请更多好友参加"));

        mLists.add(new PinRuleBean("直拼成功",String.format(context.getString(R.string.pin_rule_success),hour)));

        mLists.add(new PinRuleBean("直拼失败",String.format(context.getString(R.string.pin_rule_failed),hour)));

        mLists.add(new PinRuleBean("直拼订单","直拼订单在个人中心-我的直拼中可查看直拼信息和进度"));
        if (mAdapter != null){
            mAdapter.setNewData(mLists);
        }
    }
}
