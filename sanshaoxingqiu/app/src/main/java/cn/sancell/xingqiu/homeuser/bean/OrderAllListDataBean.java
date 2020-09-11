package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/20.
 */

public class OrderAllListDataBean extends ListBaseBean {
    private List<OrderAllBean> dataList;

    public List<OrderAllBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderAllBean> dataList) {
        this.dataList = dataList;
    }

    public static class OrderAllBean {
        private String orderNumber;
        private int id;
        private int payState;
        private int orderState;
        private int confirmState;
        private int deliveryState;
        private int goodsFlag;
        private int payAmtE2;
        private int orderTime;
        private int totalAmtE2;
        private int totalMoneyAmtE2;
        private int orderPayEndTime;


        private OrderPackListBean parcelInfo;

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

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPayState() {
            return payState;
        }


        public void setPayState(int payState) {
            this.payState = payState;
        }

        public int getOrderPayEndTime() {
            return orderPayEndTime;
        }

        public void setOrderPayEndTime(int orderPayEndTime) {
            this.orderPayEndTime = orderPayEndTime;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public int getPayAmtE2() {
            return payAmtE2;
        }

        public void setPayAmtE2(int payAmtE2) {
            this.payAmtE2 = payAmtE2;
        }

        public int getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(int orderTime) {
            this.orderTime = orderTime;
        }

        public int getTotalAmtE2() {
            return totalAmtE2;
        }

        public void setTotalAmtE2(int totalAmtE2) {
            this.totalAmtE2 = totalAmtE2;
        }

        public int getTotalMoneyAmtE2() {
            return totalMoneyAmtE2;
        }

        public void setTotalMoneyAmtE2(int totalMoneyAmtE2) {
            this.totalMoneyAmtE2 = totalMoneyAmtE2;
        }

        public int getGoodsFlag() {
            return goodsFlag;
        }

        public void setGoodsFlag(int goodsFlag) {
            this.goodsFlag = goodsFlag;
        }

        public OrderPackListBean getParcelInfo() {
            return parcelInfo;
        }


        public void setParcelInfo(OrderPackListBean parcelInfo) {
            this.parcelInfo = parcelInfo;
        }

        public class OrderPackListBean extends ListBaseBean {
            private List<OrderPackBean> dataList;

            public List<OrderPackBean> getDataList() {
                return dataList;
            }

            public void setDataList(List<OrderPackBean> dataList) {
                this.dataList = dataList;
            }

            public class OrderPackBean {
                private int id;
                private int orderId;
                private int goodsSupplierId;
                private int deliveryState;
                private int payState;
                private int orderState;
                private int confirmState;
                private int totalAmtE2;
                private int totalMoneyAmtE2;
                private int totalPointAmtE2;
                private int warehouseId;
                private int isEvaluation;
                private String parcelName;
                private String parcelNumber;
                private String orderNumber;
                private PackProductListBean orderDetail;

                public int getOrderId() {
                    return orderId;
                }

                public void setOrderId(int orderId) {
                    this.orderId = orderId;
                }

                public int getIsEvaluation() {
                    return isEvaluation;
                }

                public void setIsEvaluation(int isEvaluation) {
                    this.isEvaluation = isEvaluation;
                }

                public int getWarehouseId() {
                    return warehouseId;
                }

                public void setWarehouseId(int warehouseId) {
                    this.warehouseId = warehouseId;
                }

                public String getOrderNumber() {
                    return orderNumber;
                }

                public void setOrderNumber(String orderNumber) {
                    this.orderNumber = orderNumber;
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

                public String getParcelNumber() {
                    return parcelNumber;
                }

                public void setParcelNumber(String parcelNumber) {
                    this.parcelNumber = parcelNumber;
                }

                public int getTotalAmtE2() {
                    return totalAmtE2;
                }

                public void setTotalAmtE2(int totalAmtE2) {
                    this.totalAmtE2 = totalAmtE2;
                }

                public String getParcelName() {
                    return parcelName;
                }

                public void setParcelName(String parcelName) {
                    this.parcelName = parcelName;
                }

                public int getDeliveryState() {
                    return deliveryState;
                }

                public void setDeliveryState(int deliveryState) {
                    this.deliveryState = deliveryState;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getGoodsSupplierId() {
                    return goodsSupplierId;
                }

                public void setGoodsSupplierId(int goodsSupplierId) {
                    this.goodsSupplierId = goodsSupplierId;
                }

                public PackProductListBean getOrderDetail() {
                    return orderDetail;
                }

                public void setOrderDetail(PackProductListBean orderDetail) {
                    this.orderDetail = orderDetail;
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

                public class PackProductListBean extends ListBaseBean {
                    private List<ProductBean> dataList;

                    public List<ProductBean> getDataList() {
                        return dataList;
                    }

                    public void setDataList(List<ProductBean> dataList) {
                        this.dataList = dataList;
                    }

                    public class ProductBean {
                        private int orderId;
                        private String orderNumber;
                        private int goodsId;
                        private String goodsCoverPic;
                        private int goodsNum;
                        private String orderDetailTitle;
                        private String goodsSpecification;

                        public String getGoodsSpecs() {
                            return goodsSpecification;
                        }

                        public void setGoodsSpecs(String goodsSpecs) {
                            this.goodsSpecification = goodsSpecs;
                        }

                        public String getGoodsName() {
                            return orderDetailTitle;
                        }

                        public void setGoodsName(String goodsName) {
                            this.orderDetailTitle = goodsName;
                        }

                        public int getOrderId() {
                            return orderId;
                        }

                        public void setOrderId(int orderId) {
                            this.orderId = orderId;
                        }

                        public String getOrderNumber() {
                            return orderNumber;
                        }

                        public void setOrderNumber(String orderNumber) {
                            this.orderNumber = orderNumber;
                        }

                        public int getGoodsId() {
                            return goodsId;
                        }

                        public void setGoodsId(int goodsId) {
                            this.goodsId = goodsId;
                        }

                        public String getGoodsCoverPic() {
                            return goodsCoverPic;
                        }

                        public void setGoodsCoverPic(String goodsCoverPic) {
                            this.goodsCoverPic = goodsCoverPic;
                        }

                        public int getGoodsNum() {
                            return goodsNum;
                        }

                        public void setGoodsNum(int goodsNum) {
                            this.goodsNum = goodsNum;
                        }
                    }
                }

            }
        }
    }
}
