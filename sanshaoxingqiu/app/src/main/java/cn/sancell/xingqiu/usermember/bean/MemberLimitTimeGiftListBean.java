package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;
import cn.sancell.xingqiu.homepage.bean.HomePageLikeListDataBean;
import cn.sancell.xingqiu.homepage.bean.LikeBean;

/**
 * Created by ai11 on 2019/9/11.
 */

public class MemberLimitTimeGiftListBean extends ListBaseBean{
    private List<MemberLimitTimeGiftBean> dataList;

    public List<MemberLimitTimeGiftBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MemberLimitTimeGiftBean> dataList) {
        this.dataList = dataList;
    }

    public class MemberLimitTimeGiftBean{
        private int seckillGoodsStatus;
        private String seckillGoodsStatusStr;
        private int seckillStockNum;
        private LikeBean goodsInfo;

        public int getSeckillGoodsStatus() {
            return seckillGoodsStatus;
        }

        public void setSeckillGoodsStatus(int seckillGoodsStatus) {
            this.seckillGoodsStatus = seckillGoodsStatus;
        }

        public String getSeckillGoodsStatusStr() {
            return seckillGoodsStatusStr;
        }

        public void setSeckillGoodsStatusStr(String seckillGoodsStatusStr) {
            this.seckillGoodsStatusStr = seckillGoodsStatusStr;
        }

        public LikeBean getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(LikeBean goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public int getSeckillStockNum() {
            return seckillStockNum;
        }

        public void setSeckillStockNum(int seckillStockNum) {
            this.seckillStockNum = seckillStockNum;
        }
    }
}
