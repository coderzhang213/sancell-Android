package cn.sancell.xingqiu.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.entity.EmptyBean;
import cn.sancell.xingqiu.bean.AddressInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.dialog.adapter.AddressAdapter;
import cn.sancell.xingqiu.homeuser.AddressEditActivity;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.usermember.model.UserModel;
import cn.sancell.xingqiu.util.observer.OnObserver;
import cn.sancell.xingqiu.util.observer.ObserverKey;
import cn.sancell.xingqiu.util.observer.ObserverManger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2019/11/27.
 */
public class SelectAddressDialog extends ReminderBaseDialog implements BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {
    private List<AddressListDataBean.AddressItemBean> mLists;
    private AddressAdapter mAddressAdapter;
    private OnAddressSelectLinsener mOnAddressSelectLinsener;

    public SelectAddressDialog(@NonNull Activity context, List<AddressListDataBean.AddressItemBean> mLists) {
        super(context);
        this.mLists = mLists;
        setAdapter(mLists);
    }

    public void setOnAddressSelectLinsener(OnAddressSelectLinsener mOnAddressSelectLinsener) {
        this.mOnAddressSelectLinsener = mOnAddressSelectLinsener;
    }


    @Override
    public View getAddView() {
        return null;
    }

    public void setAdapter(RecyclerView rl_list) {

    }

    public void getAddressList() {

        UserModel.getInstance().getAddressList(new BaseReq()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<AddressListDataBean>(context) {

            @Override
            protected void onSuccess(BaseEntry<AddressListDataBean> info) throws Exception {
                if (info.getRetData().getDataList() != null) {
                    setAdapter(info.getRetData().getDataList());
                } else {
                    rl_not_address.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
            }
        });
    }

    @Override
    public void setTitle(TextView tv_title) {
        super.setTitle(tv_title);
        tv_title.setText("配送地址");
    }

    @Override
    public void setSubmitText(TextView tv_new_address) {
        super.setSubmitText(tv_new_address);
        tv_new_address.setText("新建地址");
    }

    @Override
    public void onSubmitLinsener() {
        super.onSubmitLinsener();
        //新建地址
        Intent intent = new Intent(context, AddressEditActivity.class);
        intent.putExtra(Constants.Key.KEY_1, true);
        context.startActivity(intent);
    }

    /**
     * 更新用户最后一次选择ID
     *
     * @param addressId
     */
    private void upChooseAddress(int addressId) {
        UserModel.getInstance().upChooseAddress(addressId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<EmptyBean>(context) {

            @Override
            protected void onSuccess(BaseEntry<EmptyBean> info) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
            }
        });
    }

    @Override
    public void show() {
        super.show();
        getAddressList();
        ObserverManger.getInstance(ObserverKey.ADD_NEW_ADDRESS).registerObserver(mAddressObserver);

    }

    @Override
    public void dismiss() {
        ObserverManger.getInstance(ObserverKey.ADD_NEW_ADDRESS).removeObserver(mAddressObserver);
        super.dismiss();
    }

    private OnObserver mAddressObserver = new OnObserver() {//监听地址
        @Override
        public void update(Object obj) {
            if (mOnAddressSelectLinsener != null) {
                if (obj != null) {
                    AddressInfo mAddressInfo = (AddressInfo) obj;
                    upChooseAddress(mAddressInfo.addressId);
                    mOnAddressSelectLinsener.addAddressSuccess(mAddressInfo);
                }

            }
            dismiss();
        }
    };

    /**
     * 设置数据
     *
     * @param mLists
     */
    public void setAdapter(List<AddressListDataBean.AddressItemBean> mLists) {
        if (mLists == null || mLists.size() <= 0) {
            rl_not_address.setVisibility(View.VISIBLE);
            return;
        }
        rl_not_address.setVisibility(View.GONE);
        if (mAddressAdapter == null) {
            mAddressAdapter = new AddressAdapter(mLists);
            mAddressAdapter.setOnItemChildClickListener(this);
            rl_list.setLayoutManager(new LinearLayoutManager(context));
            rl_list.setAdapter(mAddressAdapter);
        }
        mAddressAdapter.setNewData(mLists);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        AddressListDataBean.AddressItemBean mAddressItemBean = (AddressListDataBean.AddressItemBean) adapter.getData().get(position);
        //更新用户最后一次选择
        upChooseAddress(mAddressItemBean.getId());
        if (mOnAddressSelectLinsener != null) {
            mOnAddressSelectLinsener.onAddressSelectListener(mAddressItemBean);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_address://创建新地址
                //新建地址
                Intent intent = new Intent(context, AddressEditActivity.class);
                intent.putExtra(Constants.Key.KEY_1, true);
                context.startActivity(intent);
                break;
        }
    }

    public interface OnAddressSelectLinsener {
        void onAddressSelectListener(AddressListDataBean.AddressItemBean mAddressItemBean);

        void addAddressSuccess(AddressInfo info);
    }
}
