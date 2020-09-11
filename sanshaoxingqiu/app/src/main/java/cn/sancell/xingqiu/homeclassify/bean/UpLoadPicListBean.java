package cn.sancell.xingqiu.homeclassify.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean;

/**
 * Created by ai11 on 2019/7/23.
 */

public class UpLoadPicListBean extends ListBaseBean{
    private List<UpLoadPhotoInfoBean> dataList;

    public List<UpLoadPhotoInfoBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<UpLoadPhotoInfoBean> dataList) {
        this.dataList = dataList;
    }
}
