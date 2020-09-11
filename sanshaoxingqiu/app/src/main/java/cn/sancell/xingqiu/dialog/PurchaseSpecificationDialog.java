package cn.sancell.xingqiu.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.sancell.xingqiu.bean.PurchaseInfo;
import cn.sancell.xingqiu.dialog.adapter.PurchaseAdapter;

/**
 * Created by zj on 2019/12/13.
 */
public class PurchaseSpecificationDialog extends ReminderBaseDialog {
    private List<PurchaseInfo> mLists = new ArrayList<>();
    private PurchaseAdapter mPurchaseAdapter;

    public PurchaseSpecificationDialog(@NonNull Activity context) {
        super(context);
    }

    @Override
    public View getAddView() {
        return null;
    }

    @Override
    public void setTitle(TextView tv_title) {
        super.setTitle(tv_title);
        tv_title.setText("购买说明");
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
        mPurchaseAdapter = new PurchaseAdapter(mLists);
        rl_list.setLayoutManager(new LinearLayoutManager(context));
        rl_list.setAdapter(mPurchaseAdapter);
    }

    /**
     * 设置新数据
     * @param mEmpLists
     */
    public void setDataLists(List<PurchaseInfo> mEmpLists) {
        mLists.clear();
        mLists.addAll(mEmpLists);
        mPurchaseAdapter.setNewData(mLists);

    }
}
