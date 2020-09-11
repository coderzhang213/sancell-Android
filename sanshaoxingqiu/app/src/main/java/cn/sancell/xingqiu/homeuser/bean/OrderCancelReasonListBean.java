package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OrderCancelReasonListBean extends ListBaseBean {

    private List<OrderCancelReasonBean> dataList;

    public List<OrderCancelReasonBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderCancelReasonBean> dataList) {
        this.dataList = dataList;
    }

    public class OrderCancelReasonBean {
        private String reasonStr;
        private int reasonId;
        private boolean isSelect;

        public String getReasonStr() {
            return reasonStr;
        }

        public void setReasonStr(String reasonStr) {
            this.reasonStr = reasonStr;
        }

        public int getReasonId() {
            return reasonId;
        }

        public void setReasonId(int reasonId) {
            this.reasonId = reasonId;
        }


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }


}
