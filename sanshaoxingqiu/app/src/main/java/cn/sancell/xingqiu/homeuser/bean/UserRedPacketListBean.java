package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.util.StringUtils;

/**
 * Created by ai11 on 2019/8/23.
 */

public class UserRedPacketListBean extends ListBaseBean {
    private List<RedPacketBean> dataList;

    private int allSpendingE2;
    private int allIncomeE2;

    public List<RedPacketBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<RedPacketBean> dataList) {
        this.dataList = dataList;
    }

    public class RedPacketBean {
        private int pointMoneyE2;
        private int pointToAccountStatus;
        private int userId;
        private String orderId;
        private String orderParcelId;
        private String warehouseId;
        private int increaseType;
        private int pointFromType;
        private String pointFromTypeStr;
        private String publishTimeStr;
        private String title;
        private String logoPic;

        public String getPublishTimeStr() {
            return publishTimeStr;
        }

        public void setPublishTimeStr(String publishTimeStr) {
            this.publishTimeStr = publishTimeStr;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLogoPic() {
            return logoPic;
        }

        public void setLogoPic(String logoPic) {
            this.logoPic = logoPic;
        }

        public int getPointMoneyE2() {
            return pointMoneyE2;
        }

        public void setPointMoneyE2(int pointMoneyE2) {
            this.pointMoneyE2 = pointMoneyE2;
        }

        public int getPointToAccountStatus() {
            return pointToAccountStatus;
        }

        public void setPointToAccountStatus(int pointToAccountStatus) {
            this.pointToAccountStatus = pointToAccountStatus;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderParcelId() {
            return orderParcelId;
        }

        public void setOrderParcelId(String orderParcelId) {
            this.orderParcelId = orderParcelId;
        }

        public String getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(String warehouseId) {
            this.warehouseId = warehouseId;
        }

        public int getIncreaseType() {
            return increaseType;
        }

        public void setIncreaseType(int increaseType) {
            this.increaseType = increaseType;
        }

        public int getPointFromType() {
            return pointFromType;
        }

        public void setPointFromType(int pointFromType) {
            this.pointFromType = pointFromType;
        }

        public String getPointFromTypeStr() {
            return pointFromTypeStr;
        }

        public void setPointFromTypeStr(String pointFromTypeStr) {
            this.pointFromTypeStr = pointFromTypeStr;
        }
    }

    public int getAllSpendingE2() {
        return allSpendingE2;
    }

    public void setAllSpendingE2(int allSpendingE2) {
        this.allSpendingE2 = allSpendingE2;
    }

    public int getAllIncomeE2() {
        return allIncomeE2;
    }

    public void setAllIncomeE2(int allIncomeE2) {
        this.allIncomeE2 = allIncomeE2;
    }
}
