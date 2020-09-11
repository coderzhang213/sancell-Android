package cn.sancell.xingqiu.homeuser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.adapter.AddressListAdapter;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.contract.UserAddressListContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.widget.SancellToobarClassicsHeader;

public class UserAddressListActivity extends BaseMVPToobarActivity<UserAddressListContract.UserAddressListPresenter>
        implements UserAddressListContract.UserAddressListrView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_address_list)
    RecyclerView rcv_address_list;
    @BindView(R.id.tv_add_address)
    TextView tv_add_address;
    @BindView(R.id.empty)
    View mEmptyLayout;
    private AddressListAdapter addressListAdapter;
    private List<AddressListDataBean.AddressItemBean> data_addresslist;

    private boolean isSelectAddress;
    private String addressId="";

    public static void start(Activity activity,int requestCode){
        Intent intent = new Intent(activity,UserAddressListActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected UserAddressListContract.UserAddressListPresenter createPresenter() {
        return new UserAddressListContract.UserAddressListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_address_list;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle("联系人管理");
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        isSelectAddress = getIntent().getBooleanExtra(Constants.Key.KEY_1, false);
        addressId = getIntent().getStringExtra(Constants.Key.KEY_2);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectAddress){
                    for (AddressListDataBean.AddressItemBean item:data_addresslist
                         ) {
                        if(addressId.equals(item.getId()+"")){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.Key.KEY_1, item);
                            Intent intent1 = new Intent();
                            intent1.putExtras(bundle);
                            setResult(RESULT_OK, intent1);
                            break;
                        }
                    }
                }
                finish();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setRefreshHeader(new SancellToobarClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.GetAddressList(UserAddressListActivity.this);
            }
        });
        rcv_address_list.setLayoutManager(new LinearLayoutManager(this));
        tv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAddressListActivity.this, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                intent.putExtra(Constants.Key.KEY_2, false);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.GetAddressList(this);
    }

    DialogUtil.ClickSureAction clickSureAction = new DialogUtil.ClickSureAction() {
        @Override
        public void sureAction(int postion) {
            mPresenter.DeleteAddress(data_addresslist.get(postion).getId() + "", UserAddressListActivity.this);
        }
    };


    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void getUserAddressList(List<AddressListDataBean.AddressItemBean> data) {
        mNetworkErrorLayout.setVisibility(View.GONE);
        refreshLayout.finishRefresh();
        addressListAdapter = null;
        data_addresslist = null;
        data_addresslist = data;
        if (data_addresslist != null && data_addresslist.size() > 0) {
            mEmptyLayout.setVisibility(View.GONE);
            addressListAdapter = new AddressListAdapter(data_addresslist);
            if (data_addresslist != null && data_addresslist.size() == 10) {
                tv_add_address.setTextColor(getResources().getColor(R.color.colorWhite_tran66));
                tv_add_address.setClickable(false);
                addressListAdapter.addFooterView(getLayoutInflater().inflate(R.layout.layout_addresslist_limittip_foot, (ViewGroup) rcv_address_list.getParent(), false));
            } else {
                tv_add_address.setClickable(true);
                tv_add_address.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            addressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.tv_edit:
                            Intent intent = new Intent(UserAddressListActivity.this, AddressEditActivity.class);
                            intent.putExtra(Constants.Key.KEY_1, false);
                            intent.putExtra(Constants.Key.KEY_2, false);
                            intent.putExtra(Constants.Key.KEY_3, data_addresslist.get(position));
                            intent.putExtra(Constants.Key.KEY_4, isSelectAddress);
                            startActivity(intent);
                            break;
                        case R.id.rl_item:
                            if (isSelectAddress) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.Key.KEY_1, data_addresslist.get(position));
                                Intent intent1 = new Intent();
                                intent1.putExtras(bundle);
                                setResult(RESULT_OK, intent1);
                                finish();
                            }
                            break;
                    }
                }
            });
            addressListAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
                @Override
                public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.rl_item && !isSelectAddress) {
                        DialogUtil.showOperateDialog(UserAddressListActivity.this, "删除地址", "删除当前地址，后续可重新添加。", "点错了", "确认删除", position, clickSureAction);
                    }
                    return false;
                }
            });
            rcv_address_list.setAdapter(addressListAdapter);
        } else {
            addressListAdapter = new AddressListAdapter(data_addresslist);
            rcv_address_list.setAdapter(addressListAdapter);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.GetAddressList(UserAddressListActivity.this);
            }
        });
    }

    @Override
    public void deleteAddressSuccess() {
        mPresenter.GetAddressList(this);
    }


    @Override
    /**
     * 返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isSelectAddress&&data_addresslist!=null){
                for (AddressListDataBean.AddressItemBean item:data_addresslist
                        ) {
                    if(addressId.equals(item.getId()+"")){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.Key.KEY_1, item);
                        Intent intent1 = new Intent();
                        intent1.putExtras(bundle);
                        setResult(RESULT_OK, intent1);
                        break;
                    }
                }
            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
