package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/21.
 */

public class OderInfoDataBean extends BaseBean {
    private int id;
    private String orderNumber;
    private int userId;
    private int payState;
    private int orderState;
    private int confirmState;
    private int goodsFlag;
    private int payAmtE2;
    private String payEndTime;
    private int payClientTypeId;
    private int totalAmtE2;
    private int totalMoneyAmtE2;
    private int totalPointAmtE2;
    private int rewardPointE2;
    private int freightE2;
    private int orderTime;
    private int orderPayEndTime;
    private int isNeedInvoice;
    private OrderInvoiceInfo orderInvoiceInfo;

    public int getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(int goodsFlag) {
        this.goodsFlag = goodsFlag;
    }

    public int getIsNeedInvoice() {
        return isNeedInvoice;
    }

    public void setIsNeedInvoice(int isNeedInvoice) {
        this.isNeedInvoice = isNeedInvoice;
    }

    public OrderInvoiceInfo getOrderInvoiceInfo() {
        return orderInvoiceInfo;
    }

    public void setOrderInvoiceInfo(OrderInvoiceInfo orderInvoiceInfo) {
        this.orderInvoiceInfo = orderInvoiceInfo;
    }

    public class OrderInvoiceInfo{
        private int id;
        private String orderId;
        private String orderParcelId;
        private int invoiceType;
        private int invoiceLookedUp;
        private String email;
        private String companyName;
        private String companyIdentifyNumber;
        private String mobile;
        private int makeOutInvoiceStatue;
        private String invoiceTypeStr;
        private String invoiceLookedUpStr;
        private String invoiceUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(int invoiceType) {
            this.invoiceType = invoiceType;
        }

        public int getInvoiceLookedUp() {
            return invoiceLookedUp;
        }

        public void setInvoiceLookedUp(int invoiceLookedUp) {
            this.invoiceLookedUp = invoiceLookedUp;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyIdentifyNumber() {
            return companyIdentifyNumber;
        }

        public void setCompanyIdentifyNumber(String companyIdentifyNumber) {
            this.companyIdentifyNumber = companyIdentifyNumber;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMakeOutInvoiceStatue() {
            return makeOutInvoiceStatue;
        }

        public void setMakeOutInvoiceStatue(int makeOutInvoiceStatue) {
            this.makeOutInvoiceStatue = makeOutInvoiceStatue;
        }

        public String getInvoiceTypeStr() {
            return invoiceTypeStr;
        }

        public void setInvoiceTypeStr(String invoiceTypeStr) {
            this.invoiceTypeStr = invoiceTypeStr;
        }

        public String getInvoiceLookedUpStr() {
            return invoiceLookedUpStr;
        }

        public void setInvoiceLookedUpStr(String invoiceLookedUpStr) {
            this.invoiceLookedUpStr = invoiceLookedUpStr;
        }

        public String getInvoiceUrl() {
            return invoiceUrl;
        }

        public void setInvoiceUrl(String invoiceUrl) {
            this.invoiceUrl = invoiceUrl;
        }
    }

    private AddressListDataBean.AddressItemBean addressInfo;

    public AddressListDataBean.AddressItemBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressListDataBean.AddressItemBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    private PackListDataBean parcelInfo;


    public class PackListDataBean extends ListBaseBean {
        private List<PackBean> dataList;

        public List<PackBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<PackBean> dataList) {
            this.dataList = dataList;
        }

        public class PackBean extends ListBaseBean {
            private String parcelNumber;
            private int orderId;
            private int goodsSupplierId;
            private int deliveryState;
            private String parcelName;
            private ProductListBean orderDetail;
            private String remark;


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

            public int getGoodsSupplierId() {
                return goodsSupplierId;
            }

            public void setGoodsSupplierId(int goodsSupplierId) {
                this.goodsSupplierId = goodsSupplierId;
            }

            public int getDeliveryState() {
                return deliveryState;
            }

            public void setDeliveryState(int deliveryState) {
                this.deliveryState = deliveryState;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public ProductListBean getOrderDetail() {
                return orderDetail;
            }

            public void setOrderDetail(ProductListBean orderDetail) {
                this.orderDetail = orderDetail;
            }

            public String getParcelName() {
                return parcelName;
            }

            public void setParcelName(String parcelName) {
                this.parcelName = parcelName;
            }

            public class ProductListBean {
                private List<ProductBean> dataList;

                public List<ProductBean> getDataList() {
                    return dataList;
                }

                public void setDataList(List<ProductBean> dataList) {
                    this.dataList = dataList;
                }

                public class ProductBean {
                    private int id;
                    private int goodsId;
                    private int goodsNum;
                    private String goodsCoverPic;
                    private int totalAmtE2;
                    private String orderDetailTitle;
                    private String goodsSpecification;

                    //local
                    public String mRemark; //评论

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getGoodsId() {
                        return goodsId;
                    }

                    public void setGoodsId(int goodsId) {
                        this.goodsId = goodsId;
                    }

                    public int getGoodsNum() {
                        return goodsNum;
                    }

                    public void setGoodsNum(int goodsNum) {
                        this.goodsNum = goodsNum;
                    }

                    public String getGoodsCoverPic() {
                        return goodsCoverPic;
                    }

                    public void setGoodsCoverPic(String goodsCoverPic) {
                        this.goodsCoverPic = goodsCoverPic;
                    }

                    public int getTotalAmtE2() {
                        return totalAmtE2;
                    }

                    public void setTotalAmtE2(int totalAmtE2) {
                        this.totalAmtE2 = totalAmtE2;
                    }

                    public String getOrderDetailTitle() {
                        return orderDetailTitle;
                    }

                    public void setOrderDetailTitle(String orderDetailTitle) {
                        this.orderDetailTitle = orderDetailTitle;
                    }

                    public String getGoodsSpecification() {
                        return goodsSpecification;
                    }

                    public void setGoodsSpecification(String goodsSpecification) {
                        this.goodsSpecification = goodsSpecification;
                    }
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getPayAmtE2() {
        return payAmtE2;
    }

    public void setPayAmtE2(int payAmtE2) {
        this.payAmtE2 = payAmtE2;
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

    public int getTotalAmtE2() {
        return totalAmtE2;
    }

    public void setTotalAmtE2(int totalAmtE2) {
        this.totalAmtE2 = totalAmtE2;
    }

    public int getFreightE2() {
        return freightE2;
    }

    public void setFreightE2(int freightE2) {
        this.freightE2 = freightE2;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }


    public PackListDataBean getParcelInfo() {
        return parcelInfo;
    }

    public void setParcelInfo(PackListDataBean parcelInfo) {
        this.parcelInfo = parcelInfo;
    }

    public int getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(int confirmState) {
        this.confirmState = confirmState;
    }

    public int getOrderPayEndTime() {
        return orderPayEndTime;
    }

    public void setOrderPayEndTime(int orderPayEndTime) {
        this.orderPayEndTime = orderPayEndTime;
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
