package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/10/23.
 */

public class OneYuanPurchaseListBean extends ListBaseBean {
    private List<OneYuanPurchaseBean.OneYuanPurchaseProductBean> dataList;

    public List<OneYuanPurchaseBean.OneYuanPurchaseProductBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OneYuanPurchaseBean.OneYuanPurchaseProductBean> dataList) {
        this.dataList = dataList;
    }
}
