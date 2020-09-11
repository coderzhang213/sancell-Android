package cn.sancell.xingqiu.usermember.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/9/4.
 */

public class MemberSavePriceListBean extends ListBaseBean {
    private List<MemberSavePriceBean> dataList;

    public List<MemberSavePriceBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<MemberSavePriceBean> dataList) {
        this.dataList = dataList;
    }

    public class MemberSavePriceBean {
        private int totalPointAmtE2;
        private int saveTotalAmtE2;
        private int monthTotalSavePoint;
        private String month;

        public int getTotalPointAmtE2() {
            return totalPointAmtE2;
        }

        public void setTotalPointAmtE2(int totalPointAmtE2) {
            this.totalPointAmtE2 = totalPointAmtE2;
        }

        public int getSaveTotalAmtE2() {
            return saveTotalAmtE2;
        }

        public void setSaveTotalAmtE2(int saveTotalAmtE2) {
            this.saveTotalAmtE2 = saveTotalAmtE2;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public int getMonthTotalSavePoint() {
            return monthTotalSavePoint;
        }

        public void setMonthTotalSavePoint(int monthTotalSavePoint) {
            this.monthTotalSavePoint = monthTotalSavePoint;
        }
    }
}
