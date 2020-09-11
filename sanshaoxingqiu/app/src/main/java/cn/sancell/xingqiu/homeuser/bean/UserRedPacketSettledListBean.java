package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/26.
 */

public class UserRedPacketSettledListBean extends ListBaseBean {
    private List<RedPacketSettledBean> dataList;

    public List<RedPacketSettledBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<RedPacketSettledBean> dataList) {
        this.dataList = dataList;
    }

    public class RedPacketSettledBean{

    }
}
