package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

/**
 * Created by ai11 on 2019/7/3.
 */

public class OrderAllListNoPayListDataBean {
    private int orderId;
    private String orderNumber;
    private String orderStateName;
    private int goodsFlag;
    private int payState;
    private int orderState;
    private int confirmState;
    private int deliveryState;
    private int showState;
    private int payAmtE2;
    private int productNum;
    private List<String> showProdutPics;
    private String singleProductName;
    private String singleProductSpecs;
    private int singleProductNum;
    private int packId;
    private int warehouseId;
    private String packName;
    private int orderPayEndTime;
    private List<String> productIds;
    public int isEvaluation; //嗮单评价

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public int getOrderPayEndTime() {
        return orderPayEndTime;
    }

    public void setOrderPayEndTime(int orderPayEndTime) {
        this.orderPayEndTime = orderPayEndTime;
    }


    public int getShowState() {
        return showState;
    }

    public void setShowState(int showState) {
        this.showState = showState;
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

    public int getSingleProductNum() {
        return singleProductNum;
    }

    public void setSingleProductNum(int singleProductNum) {
        this.singleProductNum = singleProductNum;
    }

    public OrderAllListNoPayListDataBean() {

    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
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


    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public List<String> getShowProdutPics() {
        return showProdutPics;
    }

    public void setShowProdutPics(List<String> showProdutPics) {
        this.showProdutPics = showProdutPics;
    }

    public String getSingleProductName() {
        return singleProductName;
    }

    public void setSingleProductName(String singleProductName) {
        this.singleProductName = singleProductName;
    }

    public String getSingleProductSpecs() {
        return singleProductSpecs;
    }

    public void setSingleProductSpecs(String singleProductSpecs) {
        this.singleProductSpecs = singleProductSpecs;
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getGoodsFlag() {
        return goodsFlag;
    }

    public void setGoodsFlag(int goodsFlag) {
        this.goodsFlag = goodsFlag;
    }
}
