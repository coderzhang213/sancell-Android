package cn.sancell.xingqiu.homeuser.afterSale;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.afterSale.address.ApplyAdrAdapter;
import cn.sancell.xingqiu.homeuser.afterSale.address.ApplyModifyAddressPresenter;
import cn.sancell.xingqiu.homeuser.afterSale.address.ApplyModifyAddressView;
import cn.sancell.xingqiu.homeuser.afterSale.address.ApplyMultipleEntity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressRegInfo;
import cn.sancell.xingqiu.homeuser.bean.req.EditAddressReq;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
 * @author Alan_Xiong
 * @desc: 修改地址
 * @time 2019-11-28 10:11
 */
public class ApplyModifyAddressActivity extends BaseMVPToobarActivity<ApplyModifyAddressPresenter> implements ApplyModifyAddressView, View.OnClickListener {

    @BindView(R.id.tv_name_phone)
    AppCompatTextView tv_name_phone;
    @BindView(R.id.tv_address)
    AppCompatTextView tv_address;
    @BindView(R.id.rv_address)
    RecyclerView rv_address;
    @BindView(R.id.tv_change)
    AppCompatTextView tv_change;
    @BindView(R.id.tv_not_change)
    AppCompatTextView tv_not_change;
    @BindView(R.id.rv_not_address)
    RecyclerView rv_not_address;//不可配送列表

    private ApplyAdrAdapter mAdapter;
    private ApplyAdrAdapter mNoAdapter;
    private List<ApplyMultipleEntity<AddressListDataBean.AddressItemBean>> mDatas = new ArrayList<>();
    private List<ApplyMultipleEntity<AddressListDataBean.AddressItemBean>> mNotDatas = new ArrayList<>();
    private String mPackageId; //包裹id
    private AddressListDataBean.AddressItemBean mAddressInfo; //当前的地址数据
    private int mSelAddressId; //选中的地址id

    public static void start(Activity context, String packageId, AddressListDataBean.AddressItemBean data, int requestCode) {
        Intent intent = new Intent(context, ApplyModifyAddressActivity.class);
        intent.putExtra("mPackageId", packageId);
        intent.putExtra("addressInfo", data);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected ApplyModifyAddressPresenter createPresenter() {
        return new ApplyModifyAddressPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_change_address;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle(getResources().getString(R.string.apply_modify_address));
        mPackageId = getIntent().getStringExtra("mPackageId");
        mAddressInfo = (AddressListDataBean.AddressItemBean) getIntent().getSerializableExtra("addressInfo");

        if (mAddressInfo != null) {
            tv_name_phone.setText(String.format(getResources().getString(R.string.address_name_phone), mAddressInfo.getAreasName(), mAddressInfo.getMobile()));
            tv_address.setText(new StringBuffer(mAddressInfo.getCodeString().replaceAll("-", "") + mAddressInfo.getAddress()));
        }

        tv_change.setOnClickListener(this);
        tv_not_change.setOnClickListener(this);
        //第一个数据为head
        rv_address.setLayoutManager(new LinearLayoutManager(this));
        rv_address.setNestedScrollingEnabled(true);

        rv_not_address.setLayoutManager(new LinearLayoutManager(this));
        rv_not_address.setNestedScrollingEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        //新建或者进入刷新数据
        mPresenter.getAddressList("", mPackageId);
    }

    private void setNoSelectAdapter() {
        if (mNoAdapter == null) {
            mNoAdapter = new ApplyAdrAdapter(mNotDatas);
            rv_not_address.setAdapter(mNoAdapter);
        } else {
            mNoAdapter.setNewData(mNotDatas);
        }
    }


    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ApplyAdrAdapter(mDatas);
            rv_address.setAdapter(mAdapter);
            initListener();
        } else {
            mAdapter.setNewData(mDatas);
        }
    }

    public void initListener() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_add_address) {
                //新建地址
                Intent intent = new Intent(this, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                startActivity(intent);
            } else if (view.getId() == R.id.rl_check) {
                //选中地址
                mSelAddressId = mDatas.get(position).data.getId();
                if (mDatas.get(position).data.isCheck) {
                    return;
                } else {
                    setDataChecked(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_change) {
            //改变,
            if (mSelAddressId > 0) {
                //发起修改请求
                EditAddressReq req = new EditAddressReq();
                req.addressId = mSelAddressId + "";
                req.parcelId = mPackageId + "";
                mPresenter.modifyAddress(req);

            } else {
                SCApp.getInstance().showSystemCenterToast("请先选择地址");
            }

        } else if (v.getId() == R.id.tv_not_change) {
            //不改变
            finish();
        }
    }

    @Override
    public void getAddressListSuccess(AddressRegInfo res) {
        if (res != null) {

            //可配送
            List<AddressListDataBean.AddressItemBean> inRange = res.getInRange();
            if (inRange != null && inRange.size() > 0) {
                mDatas.clear();
                mDatas.add(0, new ApplyMultipleEntity<>(ApplyMultipleEntity.HEADER, null));
                for (int i = 0; i < inRange.size(); i++) {
                    if (mSelAddressId > 0) {
                        if (inRange.get(i).getAreasId() == mSelAddressId) {
                            inRange.get(i).isCheck = true;
                        }
                    }
                    AddressListDataBean.AddressItemBean addressItemBean = inRange.get(i);
                    addressItemBean.isDelivery = true;
                    mDatas.add(new ApplyMultipleEntity<>(ApplyMultipleEntity.CONTENT, addressItemBean));
                }
                setAdapter();
            }
            //不可配送区域
            List<AddressListDataBean.AddressItemBean> notInRange = res.getNotInRange();
            if (notInRange != null && notInRange.size() > 0) {
                mNotDatas.clear();
                mNotDatas.add(0, new ApplyMultipleEntity<>(ApplyMultipleEntity.NOT_PS_TITLE, null));
                for (int i = 0; i < notInRange.size(); i++) {
                    if (mSelAddressId > 0) {
                        if (notInRange.get(i).getAreasId() == mSelAddressId) {
                            notInRange.get(i).isCheck = true;
                        }
                    }
                    AddressListDataBean.AddressItemBean addressItemBean = notInRange.get(i);
                    addressItemBean.isDelivery = false;
                    mNotDatas.add(new ApplyMultipleEntity<>(ApplyMultipleEntity.NOT_PS, addressItemBean));
                }
                setNoSelectAdapter();

            }

        }
    }

    public void setDataChecked(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            for (int i = 1; i < mDatas.size(); i++) {
                if (position == i) {
                    mDatas.get(i).data.isCheck = true;
                } else {
                    mDatas.get(i).data.isCheck = false;
                }
            }
        }

    }

    @Override
    public void getAddressListError(String error) {
        mDatas.add(0, new ApplyMultipleEntity<>(1, null));
        setAdapter();
        SCApp.getInstance().showSystemCenterToast(error);
    }

    @Override
    public void getModifySuccess() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void getModifyError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }


}
