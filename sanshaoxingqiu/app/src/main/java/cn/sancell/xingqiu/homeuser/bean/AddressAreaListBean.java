package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.address.AddressBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/19.
 */

public class AddressAreaListBean extends ListBaseBean {
    private List<AddressBean> dataList;

    public List<AddressBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<AddressBean> dataList) {
        this.dataList = dataList;
    }
}
