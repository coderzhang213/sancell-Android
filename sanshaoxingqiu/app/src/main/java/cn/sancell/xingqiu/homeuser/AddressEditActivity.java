package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.address.AddressBean;
import cn.sancell.xingqiu.address.AreaPickerView;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.bean.AddressInfo;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.homeuser.contract.AddressAreaListContract;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;
import cn.sancell.xingqiu.util.observer.ObserverKey;
import cn.sancell.xingqiu.util.observer.ObserverManger;

/**
 * 地址添加编辑界面
 */
public class AddressEditActivity extends BaseMVPToobarActivity<AddressAreaListContract.AresListPresenter>
        implements AddressAreaListContract.AresListView, View.OnClickListener {

    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_phone)
    EditText ed_phone;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.ed_area_detail)
    EditText ed_area_detail;
    @BindView(R.id.tv_label_jia)
    TextView tv_label_jia;
    @BindView(R.id.tv_label_company)
    TextView tv_label_company;
    @BindView(R.id.tv_label_school)
    TextView tv_label_school;
    @BindView(R.id.iv_select_default)
    ImageView iv_select_default;
    @BindView(R.id.ll_select_default)
    LinearLayout ll_select_default;
    @BindView(R.id.tv_save)
    TextView tv_save;

    private boolean isNew;
    private boolean isSelectAddress;
    private boolean isSelectAddressEditStatus;
    private AddressListDataBean.AddressItemBean addressItemBean;
    private boolean isdefault;

    private String select_label = "";
    private String[] labels = new String[]{"家", "公司", "学校"};

    List<AddressBean> addressBeans;
    private int[] i;
    private AreaPickerView areaPickerView;
    private String codeString;

    @Override
    protected AddressAreaListContract.AresListPresenter createPresenter() {
        return new AddressAreaListContract.AresListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_edit;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        isNew = getIntent().getBooleanExtra(Constants.Key.KEY_1, true);
        isSelectAddress = getIntent().getBooleanExtra(Constants.Key.KEY_2, false);
        if (isNew) {
            initActivityTitle("新增联系人");
        } else {
            initActivityTitle("编辑联系人");
            addressItemBean = (AddressListDataBean.AddressItemBean) getIntent().getSerializableExtra(Constants.Key.KEY_3);
            isSelectAddressEditStatus = getIntent().getBooleanExtra(Constants.Key.KEY_4, false);
            if (isSelectAddressEditStatus) {
                tv_fun.setVisibility(View.GONE);
            } else {
                tv_fun.setVisibility(View.VISIBLE);
                tv_fun.setText(R.string.delete_title);
                tv_fun.setOnClickListener(this);
            }
            ed_name.setText(addressItemBean.getConsignee());
            ed_phone.setText(addressItemBean.getMobile());
            ed_area_detail.setText(addressItemBean.getAddress());
            tv_area.setText(addressItemBean.getCodeString());
            tv_area.setTextColor(getResources().getColor(R.color.color_text1));
            select_label = addressItemBean.getMarkName();
            codeString = addressItemBean.getProvinceId() + "-" + addressItemBean.getCityId() + "-" + addressItemBean.getAreasId() + "-" + addressItemBean.getStreetId();
            isdefault = addressItemBean.getIsDefault() == 1 ? true : false;
            if (isdefault) {
                iv_select_default.setImageResource(R.mipmap.icon_car_select_yes);
            } else {
                iv_select_default.setImageResource(R.mipmap.icon_car_select_no);
            }
            switch (addressItemBean.getMarkName()) {
                case "家":
                    tv_label_jia.setBackgroundResource(R.drawable.round_theme_14);
                    tv_label_jia.setTextColor(getResources().getColor(R.color.colorWhite));
                    break;
                case "公司":
                    tv_label_company.setBackgroundResource(R.drawable.round_theme_14);
                    tv_label_company.setTextColor(getResources().getColor(R.color.colorWhite));
                    break;
                case "学校":
                    tv_label_school.setBackgroundResource(R.drawable.round_theme_14);
                    tv_label_school.setTextColor(getResources().getColor(R.color.colorWhite));
                    break;
            }
        }
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        tv_label_jia.setOnClickListener(this);
        tv_label_company.setOnClickListener(this);
        tv_label_school.setOnClickListener(this);
        ll_select_default.setOnClickListener(this);
        tv_area.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        String address = PreferencesUtils.getString(Constants.Key.KEY_address, "");
        if (!StringUtils.isTextEmpty(address)) {
            addressBeans = new Gson().fromJson(address, new TypeToken<List<AddressBean>>() {
            }.getType());
        } else {
            mPresenter.GetAllAddress(AddressEditActivity.this, false);
        }
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
    public void getAresList(String address, boolean isshow) {
        addressBeans = new Gson().fromJson(address, new TypeToken<List<AddressBean>>() {
        }.getType());
        if (isshow) {
            showSelectAddress();
        }
    }

    @Override
    public void newAddressSuccess(AddressInfo info) {
        if (isSelectAddress) {
            setResult(RESULT_OK);
        }
        //去通知添加地址成功
        ObserverManger.getInstance(ObserverKey.ADD_NEW_ADDRESS).notifyObserver(info);
        finish();
    }

    @Override
    public void deleteAddressSuccess() {
        finish();
    }

    public void showSelectAddress() {
        if (areaPickerView == null) {
            int select1 = -1, select2 = -1, select3 = -1, select4 = -1;
            if (addressItemBean != null) {
                for (int j = 0; j < addressBeans.size(); j++) {
                    if (addressBeans.get(j).getValue().equals(addressItemBean.getProvinceId() + "")) {
                        select1 = j;
                        break;
                    }
                }
                if (select1 != -1) {
                    for (int p = 0; p < addressBeans.get(select1).getChildren().size(); p++) {
                        if (addressBeans.get(select1).getChildren().get(p).getValue().equals(addressItemBean.getCityId() + "")) {
                            select2 = p;
                            break;
                        }
                    }
                }
                if (select1 != -1 && select2 != -1) {
                    for (int p = 0; p < addressBeans.get(select1).getChildren().get(select2).getChildren().size(); p++) {
                        if (addressBeans.get(select1).getChildren().get(select2).getChildren().get(p).getValue().equals(addressItemBean.getAreasId() + "")) {
                            select3 = p;
                            break;
                        }
                    }
                }
                if (select1 != -1 && select2 != -1 && select3 != -1) {
                    for (int p = 0; p < addressBeans.get(select1).getChildren().get(select2).getChildren().get(select3).getChildren().size(); p++) {
                        if (addressBeans.get(select1).getChildren().get(select2).getChildren().get(select3).getChildren().get(p).getValue().equals(addressItemBean.getTownId() + "")) {
                            select4 = p;
                            break;
                        }
                    }
                }
            }
            if (addressItemBean != null && select1 != -1) {
                areaPickerView = new AreaPickerView(AddressEditActivity.this, R.style.AddressDialog, addressBeans, select1, select2, select3, select4);
            } else {
                areaPickerView = new AreaPickerView(AddressEditActivity.this, R.style.AddressDialog, addressBeans, null);
            }
            areaPickerView.setAreaPickerViewCallback(new AreaPickerView.AreaPickerViewCallback() {
                @Override
                public void callback(int... value) {
                    i = value;
                    if (value.length == 4) {
                        if (value[3] == -1) {
                            codeString = addressBeans.get(value[0]).getValue() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getValue() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getValue();
                            tv_area.setText(addressBeans.get(value[0]).getLabel() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                        } else {
                            codeString = addressBeans.get(value[0]).getValue() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getValue() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getValue() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).getValue();
                            tv_area.setText(addressBeans.get(value[0]).getLabel() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel() +
                                    "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).getLabel());
                        }
                    } else if (value.length == 3) {
                        codeString = addressBeans.get(value[0]).getValue() +
                                "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getValue() +
                                "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getValue();
                        tv_area.setText(addressBeans.get(value[0]).getLabel() +
                                "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() +
                                "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                    }
                    tv_area.setTextColor(getResources().getColor(R.color.color_text1));
                }
            });
            areaPickerView.show();
        } else {
            //areaPickerView.setSelect(i);
            areaPickerView.reset();
            areaPickerView.show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fun:
                DialogUtil.showOperateDialog(AddressEditActivity.this, getResources().getString(R.string.delete_address_title),
                        getResources().getString(R.string.delete_address_desc),
                        getResources().getString(R.string.delete_address_no),
                        getResources().getString(R.string.delete_address_yes), clickSureAction);
                break;
            case R.id.tv_label_jia:
                select_label = labels[0];
                tv_label_jia.setBackgroundResource(R.drawable.round_theme_14);
                tv_label_jia.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_label_company.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_company.setTextColor(getResources().getColor(R.color.color_text1));
                tv_label_school.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_school.setTextColor(getResources().getColor(R.color.color_text1));
                break;
            case R.id.tv_label_company:
                select_label = labels[1];
                tv_label_jia.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_jia.setTextColor(getResources().getColor(R.color.color_text1));
                tv_label_company.setBackgroundResource(R.drawable.round_theme_14);
                tv_label_company.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_label_school.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_school.setTextColor(getResources().getColor(R.color.color_text1));
                break;
            case R.id.tv_label_school:
                select_label = labels[2];
                tv_label_jia.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_jia.setTextColor(getResources().getColor(R.color.color_text1));
                tv_label_company.setBackgroundResource(R.drawable.round_color_stroke4_14_stroke1);
                tv_label_company.setTextColor(getResources().getColor(R.color.color_text1));
                tv_label_school.setBackgroundResource(R.drawable.round_theme_14);
                tv_label_school.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case R.id.ll_select_default:
                isdefault = !isdefault;
                if (isdefault) {
                    iv_select_default.setImageResource(R.mipmap.icon_car_select_yes);
                } else {
                    iv_select_default.setImageResource(R.mipmap.icon_car_select_no);
                }
                break;
            case R.id.tv_save:
                if (ed_name.getText().toString().length() < 2 || ed_name.getText().toString().length() > 10) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_address_name_errorformat_tip);
                    return;
                }
                if (StringUtils.isTextEmpty(ed_phone.getText().toString())) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_address_phone_tip);
                    return;
                }
                if (ed_phone.getText().toString().length() != 11) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_address_phone_error_tip);
                    return;
                }
                if (StringUtils.isTextEmpty(codeString)) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_address_area_empty_tip);
                    return;
                }
                if (ed_area_detail.getText().toString().length() < 4) {
                    SCApp.getInstance().showSystemCenterToast(R.string.input_address_detail_errorformat_tip);
                    return;
                }
                if (isNew) {
                    mPresenter.NewAddress(ed_name.getText().toString(), ed_phone.getText().toString(), codeString, ed_area_detail.getText().toString(), select_label, isdefault ? "1" : "2", AddressEditActivity.this);
                } else {
                    mPresenter.ModifyAddress(addressItemBean.getId() + "", ed_name.getText().toString(), ed_phone.getText().toString(), codeString, ed_area_detail.getText().toString(), select_label, isdefault ? "1" : "2", AddressEditActivity.this);
                }

                break;
            case R.id.tv_area:
                if (addressBeans != null && addressBeans.size() > 0) {
                    showSelectAddress();
                } else {
                    mPresenter.GetAllAddress(AddressEditActivity.this, true);
                }
                /*if (addressBeans == null) {
                    final StringBuilder address = new StringBuilder();
                    Observable.create((ObservableOnSubscribe<String>) e -> {
                        StringBuilder stringBuilder = new StringBuilder();
                        InputStream inputStream = getResources().getAssets().open("region.json");
                        InputStreamReader isr = new InputStreamReader(inputStream);
                        BufferedReader reader = new BufferedReader(isr);
                        String jsonLine;
                        while ((jsonLine = reader.readLine()) != null) {
                            stringBuilder.append(jsonLine);
                        }
                        reader.close();
                        isr.close();
                        inputStream.close();
                        e.onNext(stringBuilder.toString());
                        e.onComplete();
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {

                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(String str) {
                                    address.append(str);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onComplete() {
                                    addressBeans = new Gson().fromJson(address.toString(), new TypeToken<List<AddressBean>>() {
                                    }.getType());
                                    int select1 = -1, select2 = -1, select3 = -1, select4 = -1;
                                    if (addressItemBean != null) {
                                        for (int j = 0; j < addressBeans.size(); j++) {
                                            if (addressBeans.get(j).getValue().equals(addressItemBean.getProvinceId() + "")) {
                                                select1 = j;
                                                break;
                                            }
                                        }
                                        if(select1!=-1) {
                                            for (int p = 0; p < addressBeans.get(select1).getChildren().size(); p++) {
                                                if (addressBeans.get(select1).getChildren().get(p).getLabel().equals(addressItemBean.getCityId() + "")) {
                                                    select2 = p;
                                                    break;
                                                }
                                            }
                                        }
                                        if(select1!=-1&&select2!=-1) {
                                            for (int p = 0; p < addressBeans.get(select1).getChildren().get(select2).getChildren().size(); p++) {
                                                if (addressBeans.get(select1).getChildren().get(select2).getChildren().get(p).getLabel().equals(addressItemBean.getAreaId() + "")) {
                                                    select3 = p;
                                                    break;
                                                }
                                            }
                                        }
                                        if(select1!=-1&&select2!=-1&&select3!=-1) {
                                            for (int p = 0; p < addressBeans.get(select1).getChildren().get(select2).getChildren().get(select3).getChildren().size(); p++) {
                                                if (addressBeans.get(select1).getChildren().get(select2).getChildren().get(select3).getChildren().get(p).getLabel().equals(addressItemBean.getTownId() + "")) {
                                                    select4 = p;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (areaPickerView == null) {
                                        if (addressItemBean != null && select1 != -1) {
                                            areaPickerView = new AreaPickerView(AddressEditActivity.this, R.style.AddressDialog, addressBeans, select1, select2, select3, select4);
                                        } else {
                                            areaPickerView = new AreaPickerView(AddressEditActivity.this, R.style.AddressDialog, addressBeans,null);
                                        }
                                        areaPickerView.setAreaPickerViewCallback(new AreaPickerView.AreaPickerViewCallback() {
                                            @Override
                                            public void callback(int... value) {
                                                i = value;
                                                if (value.length == 4) {
                                                    tv_area.setText(addressBeans.get(value[0]).getLabel() +
                                                            "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() +
                                                            "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel() +
                                                            "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getChildren().get(value[3]).getLabel());
                                                } else if (value.length == 3) {
                                                    tv_area.setText(addressBeans.get(value[0]).getLabel() +
                                                            "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getLabel() +
                                                            "-" + addressBeans.get(value[0]).getChildren().get(value[1]).getChildren().get(value[2]).getLabel());
                                                }
                                                tv_area.setTextColor(getResources().getColor(R.color.color_text1));
                                            }
                                        });
                                    }
                                    //areaPickerView.setSelect(i);
                                    areaPickerView.show();

                                }
                            });
                } else {
                    areaPickerView.setSelect(i);
                    areaPickerView.show();
                }*/
                break;

        }
    }

    DialogUtil.ClickSureAction clickSureAction = new DialogUtil.ClickSureAction() {
        @Override
        public void sureAction(int postion) {
            mPresenter.DeleteAddress(addressItemBean.getId() + "", AddressEditActivity.this);
        }
    };
}
