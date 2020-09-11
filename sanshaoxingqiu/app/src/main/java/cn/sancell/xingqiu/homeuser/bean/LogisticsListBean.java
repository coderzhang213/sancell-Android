package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/9/26.
 */

public class LogisticsListBean  {
    private String orderId;
    private String parcelId;
    private OrderReceiveInfo orderReceiveInfo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public OrderReceiveInfo getOrderReceiveInfo() {
        return orderReceiveInfo;
    }

    public void setOrderReceiveInfo(OrderReceiveInfo orderReceiveInfo) {
        this.orderReceiveInfo = orderReceiveInfo;
    }

    public class OrderReceiveInfo extends ListBaseBean{
        private List<LogisticsBean> dataList;

        public List<LogisticsBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<LogisticsBean> dataList) {
            this.dataList = dataList;
        }

        public class LogisticsBean {
            private String courierTag;
            private String courierCompany;
            private String courierNumber;

            public String getCourierTag() {
                return courierTag;
            }

            public void setCourierTag(String courierTag) {
                this.courierTag = courierTag;
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
        }
    }
}
