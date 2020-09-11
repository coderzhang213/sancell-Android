package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/7/23.
 */

public class SearchKeyListBean extends ListBaseBean {
    private List<KeyBean> dataList;

    public List<KeyBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<KeyBean> dataList) {
        this.dataList = dataList;
    }

}
