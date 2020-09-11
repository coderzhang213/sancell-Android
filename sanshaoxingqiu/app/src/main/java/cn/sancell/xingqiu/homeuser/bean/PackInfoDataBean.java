package cn.sancell.xingqiu.homeuser.bean;

/**
 * Created by ai11 on 2019/7/6.
 */

public class PackInfoDataBean {

    private PackInfoBean parcelInfo;
    private AddressListDataBean.AddressItemBean addressInfo;
    private PackInfoBean.OrderInfoBean orderInfo;
    private ExpressInfo expressInfo;

    private OderInfoDataBean.OrderInvoiceInfo orderInvoiceInfo;

    public OderInfoDataBean.OrderInvoiceInfo getOrderInvoiceInfo() {
        return orderInvoiceInfo;
    }

    public void setOrderInvoiceInfo(OderInfoDataBean.OrderInvoiceInfo orderInvoiceInfo) {
        this.orderInvoiceInfo = orderInvoiceInfo;
    }

    public ExpressInfo getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(ExpressInfo expressInfo) {
        this.expressInfo = expressInfo;
    }

    public class ExpressInfo {
        private String nu;
        private String com;
        private String status;
        private InfoBean data;

        public InfoBean getData() {
            return data;
        }

        public void setData(InfoBean data) {
            this.data = data;
        }

        public String getNu() {
            return nu;
        }

        public void setNu(String nu) {
            this.nu = nu;
        }

        public String getCom() {
            return com;
        }

        public void setCom(String com) {
            this.com = com;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public class InfoBean {
            private String time;
            private String context;
            private String status;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

    public AddressListDataBean.AddressItemBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressListDataBean.AddressItemBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    public PackInfoBean.OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(PackInfoBean.OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public PackInfoBean getParcelInfo() {
        return parcelInfo;
    }

    public void setParcelInfo(PackInfoBean parcelInfo) {
        this.parcelInfo = parcelInfo;
    }

    public class PackInfoBean {
        private int userId;
        private int orderId;
        private int warehouseId;
        private String parcelNumber;
        private String parcelName;
        private int totalAmtE2;
        private int totalMoneyAmtE2;
        private int totalPointAmtE2;
        private int rewardPointE2;
        private int payState;
        private int orderState;
        private int confirmState;
        private int deliveryState;
        private int isEvaluation;
        private int goodsFlag;
        private int isNeedInvoice;
        private String remark;
        public long deductPriceE2;
        public String writeOffCode;
        public int writeOffStatus; //1未使用；2已使用
        public String QRCodeH5Link;//h5


        private OderInfoDataBean.PackListDataBean.PackBean.ProductListBean orderDetail;

        public class OrderInfoBean {
            private String payEndTime;
            private int payClientTypeId;
            private int orderTime;
            private String orderNumber;

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getPayEndTime() {
                return payEndTime;
            }

            public void setPayEndTime(String payEndTime) {
                this.payEndTime = payEndTime;
            }

            public int getPayClientTypeId() {
                return payClientTypeId;
            }

            public void setPayClientTypeId(int payClientTypeId) {
                this.payClientTypeId = payClientTypeId;
            }

            public int getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(int orderTime) {
                this.orderTime = orderTime;
            }
        }

        public int getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(int warehouseId) {
            this.warehouseId = warehouseId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getParcelNumber() {
            return parcelNumber;
        }

        public void setParcelNumber(String parcelNumber) {
            this.parcelNumber = parcelNumber;
        }

        public String getParcelName() {
            return parcelName;
        }

        public void setParcelName(String parcelName) {
            this.parcelName = parcelName;
        }

        public int getTotalAmtE2() {
            return totalAmtE2;
        }

        public void setTotalAmtE2(int totalAmtE2) {
            this.totalAmtE2 = totalAmtE2;
        }

        public int getPayState() {
            return payState;
        }

        public void setPayState(int payState) {
            this.payState = payState;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public int getConfirmState() {
            return confirmState;
        }

        public void setConfirmState(int confirmState) {
            this.confirmState = confirmState;
        }

        public int getDeliveryState() {
            return deliveryState;
        }

        public void setDeliveryState(int deliveryState) {
            this.deliveryState = deliveryState;
        }

        public int getIsNeedInvoice() {
            return isNeedInvoice;
        }

        public void setIsNeedInvoice(int isNeedInvoice) {
            this.isNeedInvoice = isNeedInvoice;
        }

        public OderInfoDataBean.PackListDataBean.PackBean.ProductListBean getOrderDetail() {
            return orderDetail;
        }

        public void setOrderDetail(OderInfoDataBean.PackListDataBean.PackBean.ProductListBean orderDetail) {
            this.orderDetail = orderDetail;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getIsEvaluation() {
            return isEvaluation;
        }

        public void setIsEvaluation(int isEvaluation) {
            this.isEvaluation = isEvaluation;
        }

        public int getGoodsFlag() {
            return goodsFlag;
        }

        public void setGoodsFlag(int goodsFlag) {
            this.goodsFlag = goodsFlag;
        }

        public int getTotalMoneyAmtE2() {
            return totalMoneyAmtE2;
        }

        public void setTotalMoneyAmtE2(int totalMoneyAmtE2) {
            this.totalMoneyAmtE2 = totalMoneyAmtE2;
        }

        public int getTotalPointAmtE2() {
            return totalPointAmtE2;
        }

        public void setTotalPointAmtE2(int totalPointAmtE2) {
            this.totalPointAmtE2 = totalPointAmtE2;
        }

        public int getRewardPointE2() {
            return rewardPointE2;
        }

        public void setRewardPointE2(int rewardPointE2) {
            this.rewardPointE2 = rewardPointE2;
        }
    }


}
