package cn.sancell.xingqiu.homeshoppingcar.bean;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;

/**
 * Created by ai11 on 2019/6/13.
 */

public class HomeShoppingCarDataBean extends BaseBean {
    private NormalGoodsData normalGoodsData;
    private UnderStockGoodsData underStockGoodsData;
    private FailureGoodsData failureGoodsData;

    public NormalGoodsData getNormalGoodsData() {
        return normalGoodsData;
    }

    public void setNormalGoodsData(NormalGoodsData normalGoodsData) {
        this.normalGoodsData = normalGoodsData;
    }

    public UnderStockGoodsData getUnderStockGoodsData() {
        return underStockGoodsData;
    }

    public void setUnderStockGoodsData(UnderStockGoodsData underStockGoodsData) {
        this.underStockGoodsData = underStockGoodsData;
    }

    public FailureGoodsData getFailureGoodsData() {
        return failureGoodsData;
    }

    public void setFailureGoodsData(FailureGoodsData failureGoodsData) {
        this.failureGoodsData = failureGoodsData;
    }

    public class NormalGoodsData extends ListBaseBean {
        private List<ShopingCarProductBean> dataList;

        public List<ShopingCarProductBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<ShopingCarProductBean> dataList) {
            this.dataList = dataList;
        }
    }

    public class UnderStockGoodsData extends ListBaseBean {
        private List<ShopingCarProductBean> dataList;

        public List<ShopingCarProductBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<ShopingCarProductBean> dataList) {
            this.dataList = dataList;
        }
    }

    public class FailureGoodsData extends ListBaseBean {
        private List<ShopingCarProductBean> dataList;

        public List<ShopingCarProductBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<ShopingCarProductBean> dataList) {
            this.dataList = dataList;
        }
    }


    public class ShopingCarProductBean implements Serializable {
        private String carId;
        private String goodsId;
        private String coverPic;
        private String coverPicThumb;
        private String goodsTitle;
        private int goodsNum;
        private int userRealPriceE2;
        private int isSelected;
        private int type;
        private int supplierId;
        private String goodsSpecification;
        private int goodsMinBuyNum;
        private int goodsStockNumber;
        private int userRealAllProfitSharingE2;
        public List<String> provinceIdList;

        private LikeBean.SeckillInfo seckillInfo;

        public LikeBean.SeckillInfo getSeckillInfo() {
            return seckillInfo;
        }

        public void setSeckillInfo(LikeBean.SeckillInfo seckillInfo) {
            this.seckillInfo = seckillInfo;
        }

        public int getUserRealAllProfitSharingE2() {
            return userRealAllProfitSharingE2;
        }

        public void setUserRealAllProfitSharingE2(int userRealAllProfitSharingE2) {
            this.userRealAllProfitSharingE2 = userRealAllProfitSharingE2;
        }

        public int getGoodsMinBuyNum() {
            return goodsMinBuyNum;
        }

        public void setGoodsMinBuyNum(int goodsMinBuyNum) {
            this.goodsMinBuyNum = goodsMinBuyNum;
        }

        public int getGoodsStockNumber() {
            return goodsStockNumber;
        }

        public void setGoodsStockNumber(int goodsStockNumber) {
            this.goodsStockNumber = goodsStockNumber;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public String getCoverPicThumb() {
            return coverPicThumb;
        }

        public void setCoverPicThumb(String coverPicThumb) {
            this.coverPicThumb = coverPicThumb;
        }

        public String getGoodsTitle() {
            return goodsTitle;
        }

        public void setGoodsTitle(String goodsTitle) {
            this.goodsTitle = goodsTitle;
        }

        public int getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(int goodsNum) {
            this.goodsNum = goodsNum;
        }

        public int getUserRealPriceE2() {
            return userRealPriceE2;
        }

        public void setUserRealPriceE2(int userRealPriceE2) {
            this.userRealPriceE2 = userRealPriceE2;
        }

        public int getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(int isSelected) {
            this.isSelected = isSelected;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public String getSpecification() {
            return goodsSpecification;
        }

        public void setSpecification(String specification) {
            this.goodsSpecification = specification;
        }
    }
}
