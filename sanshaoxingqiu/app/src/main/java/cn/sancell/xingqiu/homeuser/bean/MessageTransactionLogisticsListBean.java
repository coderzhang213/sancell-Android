package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/8/26.
 */

public class MessageTransactionLogisticsListBean extends ListBaseBean {
    private List<TransactionLogisticsBean> dataList;

    public List<TransactionLogisticsBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<TransactionLogisticsBean> dataList) {
        this.dataList = dataList;
    }

    public class TransactionLogisticsBean{
        private int logisticsNoticeType;
        private String orderId;
        private String orderParcelId;
        private String warehouseId;
        private int readStatus;  //读取状态 1：已读 2：未读
        private String toUserId;
        private String publicTimeStr;
        private String courierCompany;
        private String courierNumber;
        private OrderDetailInfo orderDetailInfo;
        private int isEvaluation;  //是否已经评价 1：已经评价 2：未评价
        public class OrderDetailInfo{
            private String orderDetailTitle;
            private String goodsCoverPic;

            public String getOrderDetailTitle() {
                return orderDetailTitle;
            }

            public void setOrderDetailTitle(String orderDetailTitle) {
                this.orderDetailTitle = orderDetailTitle;
            }

            public String getGoodsCoverPic() {
                return goodsCoverPic;
            }

            public void setGoodsCoverPic(String goodsCoverPic) {
                this.goodsCoverPic = goodsCoverPic;
            }
        }

        public int getLogisticsNoticeType() {
            return logisticsNoticeType;
        }

        public void setLogisticsNoticeType(int logisticsNoticeType) {
            this.logisticsNoticeType = logisticsNoticeType;
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

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getPublicTimeStr() {
            return publicTimeStr;
        }

        public void setPublicTimeStr(String publicTimeStr) {
            this.publicTimeStr = publicTimeStr;
        }

        public String getCourierCompany() {
            return courierCompany;
        }

        public void setCourierCompany(String courierCompany) {
            this.courierCompany = courierCompany;
        }

        public String getCourierNumber() {
            return courierNumber;
        }

        public void setCourierNumber(String courierNumber) {
            this.courierNumber = courierNumber;
        }

        public OrderDetailInfo getOrderDetailInfo() {
            return orderDetailInfo;
        }

        public void setOrderDetailInfo(OrderDetailInfo orderDetailInfo) {
            this.orderDetailInfo = orderDetailInfo;
        }

        public int getIsEvaluation() {
            return isEvaluation;
        }

        public void setIsEvaluation(int isEvaluation) {
            this.isEvaluation = isEvaluation;
        }

        public String getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(String warehouseId) {
            this.warehouseId = warehouseId;
        }
    }
}
