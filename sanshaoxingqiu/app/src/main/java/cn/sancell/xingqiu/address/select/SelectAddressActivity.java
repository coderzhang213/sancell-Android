package cn.sancell.xingqiu.address.select;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 选择地址
 * @time 2019-12-12 17:09
 */
public class SelectAddressActivity extends BaseMVPToobarActivity<SelectAddressPresenter> implements SelectAddressActivityView {

    @BindView(R.id.rv_address)
    RecyclerView rv_address;
    @BindView(R.id.tv_new_address)
    AppCompatTextView tv_new_address;

    private SelectAddressAdapter mAdapter;


    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectAddressActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected SelectAddressPresenter createPresenter() {
        return new SelectAddressPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activvity_select_address;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("联系人");

        rv_address.setLayoutManager(new LinearLayoutManager(this));

        tv_new_address.setOnClickListener(v -> {
            Intent intent = new Intent(SelectAddressActivity.this, AddressEditActivity.class);
            intent.putExtra(Constants.Key.KEY_1, true);
            startActivity(intent);
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //新建或者重开刷新数据
        mPresenter.getAddressList();
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void getAddressListSuccess(AddressListDataBean res) {

        if (res.getDataList() != null) {
            if (mAdapter == null) {
                mAdapter = new SelectAddressAdapter(res.getDataList());

                mAdapter.setOnItemClickListener(this::setResult);

                rv_address.setAdapter(mAdapter);
            } else {
                mAdapter.setNewData(res.getDataList());
            }

        }

    }

    //回调
    public void setResult(AddressListDataBean.AddressItemBean item) {
        Intent intent = new Intent();
        intent.putExtra(IntentKey.ADDRESS_SELECT_BACK, item);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getAddressError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }
}
