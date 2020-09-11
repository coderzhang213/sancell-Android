package cn.sancell.xingqiu.homeuser.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/7/18.
 */

public class EvaluatedProductListDataBean extends ListBaseBean {
    private List<EvaluatedProductBean> dataList;

    public List<EvaluatedProductBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<EvaluatedProductBean> dataList) {
        this.dataList = dataList;
    }

    public class EvaluatedProductBean {
        private int id;
        private String goodsCoverPic;
        private int goodsId;
        private int orderId;
        private int warehouseId;
        private String orderDetailTitle;
        private String goodsSpecification;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsCoverPic() {
            return goodsCoverPic;
        }

        public void setGoodsCoverPic(String goodsCoverPic) {
            this.goodsCoverPic = goodsCoverPic;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(int warehouseId) {
            this.warehouseId = warehouseId;
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
